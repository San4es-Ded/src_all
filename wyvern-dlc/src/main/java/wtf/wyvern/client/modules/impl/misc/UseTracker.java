package wtf.wyvern.client.modules.impl.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.server.EventPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ModuleAnnotation(
        name = "UseTracker",
        category = Category.MISC,
        description = "Уведомляет о съеденных голдах, зельях и лопнувших тотемах"
)
public final class UseTracker extends Module {
    public static final UseTracker INSTANCE = new UseTracker();

    private final BooleanSetting totems = new BooleanSetting("Тотемы", true);
    private final BooleanSetting apples = new BooleanSetting("Голды", true);
    private final BooleanSetting potions = new BooleanSetting("Зелья", true);
    private final BooleanSetting otherFood = new BooleanSetting("Прочая еда", false);
    private final BooleanSetting self = new BooleanSetting("Себя тоже", true);
    private final SliderSetting range = new SliderSetting("Радиус", 64.0F, 8.0F, 128.0F, 1.0F);

    /** Последний предмет, который игрок держал в руках во время использования. */
    private final Map<UUID, ActiveItem> activeItems = new HashMap<>();

    private UseTracker() {
    }

    @Override
    public void onDisable() {
        activeItems.clear();
        super.onDisable();
    }

    /**
     * Сервер не шлёт EntityStatus на съеденный предмет, поэтому использование
     * считаем сами: пока игрок держит предмет — копим тики, а когда отпустил,
     * сравниваем их с длительностью использования. Не доел/прервали — тиков
     * меньше, и уведомления не будет.
     */
    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            UUID uuid = player.getUuid();
            ActiveItem tracked = activeItems.get(uuid);

            if (player.isUsingItem()) {
                ItemStack active = player.getActiveItem();
                if (active.isEmpty()) {
                    continue;
                }
                if (tracked == null || !ItemStack.areItemsEqual(tracked.stack, active)) {
                    activeItems.put(uuid, new ActiveItem(active.copy(), active.getMaxUseTime(player)));
                } else {
                    tracked.ticks++;
                }
                continue;
            }

            if (tracked != null) {
                activeItems.remove(uuid);
                // -2 тика запаса: старт использования мы замечаем уже после того,
                // как сервер прислал флаг, поэтому пары тиков всегда не хватает.
                if (tracked.maxUseTime > 0 && tracked.ticks >= tracked.maxUseTime - 2) {
                    onConsumed(player, tracked.stack);
                }
            }
        }

        activeItems.keySet().removeIf(uuid -> mc.world.getPlayerByUuid(uuid) == null);
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if (!event.isReceive() || mc.world == null || mc.player == null) {
            return;
        }
        if (!(event.getPacket() instanceof EntityStatusS2CPacket packet)) {
            return;
        }

        if (packet.getStatus() != EntityStatuses.USE_TOTEM_OF_UNDYING || !totems.isEnabled()) {
            return;
        }

        Entity entity = packet.getEntity(mc.world);
        if (!(entity instanceof PlayerEntity player) || !isTracked(player)) {
            return;
        }

        notify(player, new ItemStack(Items.TOTEM_OF_UNDYING), "лопнул",
                Text.literal("Тотем").setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
    }

    private void onConsumed(PlayerEntity player, ItemStack consumed) {
        if (consumed.isEmpty() || !isTracked(player)) {
            return;
        }

        if (consumed.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            if (apples.isEnabled()) {
                notify(player, consumed, "съел",
                        Text.literal("Чарку").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            }
        } else if (consumed.isOf(Items.GOLDEN_APPLE)) {
            if (apples.isEnabled()) {
                notify(player, consumed, "съел",
                        Text.literal("Гепл").setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
            }
        } else if (consumed.isOf(Items.POTION)) {
            if (potions.isEnabled()) {
                notify(player, consumed, "выпил",
                        consumed.getName().copy().setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
            }
        } else if (otherFood.isEnabled()) {
            notify(player, consumed, "использовал",
                    consumed.getName().copy().setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        }
    }

    private boolean isTracked(PlayerEntity player) {
        if (player == mc.player) {
            return self.isEnabled();
        }
        float distance = range.getCurrent();
        return mc.player.squaredDistanceTo(player) <= distance * distance;
    }

    private void notify(PlayerEntity player, ItemStack icon, String action, Text subject) {
        Text message = Text.literal(getDisplayName(player))
                .setStyle(Style.EMPTY.withColor(Formatting.WHITE))
                .append(Text.literal(" " + action + " ").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                .append(subject);
        Wyvern.getInstance().getNotifyManager().addUseNotification(icon, message);
    }

    private String getDisplayName(PlayerEntity player) {
        if (player == mc.player && NameProtect.INSTANCE.isEnabled()) {
            return NameProtect.getCustomName();
        }
        return player.getNameForScoreboard();
    }

    private static final class ActiveItem {
        final ItemStack stack;
        final int maxUseTime;
        int ticks;

        ActiveItem(ItemStack stack, int maxUseTime) {
            this.stack = stack;
            this.maxUseTime = maxUseTime;
        }
    }
}
