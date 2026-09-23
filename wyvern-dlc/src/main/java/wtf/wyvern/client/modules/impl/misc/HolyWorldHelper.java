package wtf.wyvern.client.modules.impl.misc;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.input.EventKey;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.utility.game.other.MessageUtil;

import java.util.List;
import java.util.Locale;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "HolyWorldHelper",
        category = Category.MISC,
        description = "Легитное использование специальных предметов HolyWorld из всего инвентаря"
)
@FastNative
public final class HolyWorldHelper extends Module {
    public static final HolyWorldHelper INSTANCE = new HolyWorldHelper();

    private static final long RESTORE_DELAY_MS = 100L;

    public final BindSetting useStun = new BindSetting("Стан");
    public final BindSetting useTrap = new BindSetting("Трапка");
    public final BindSetting useExplosiveTrap = new BindSetting("Взрывная трапка");
    public final BindSetting useExplosiveThing = new BindSetting("Взрывная штучка");
    public final BindSetting useSnowball = new BindSetting("Ком снега");
    public final BindSetting useJakeLantern = new BindSetting("Светильник Джейка");
    public final BindSetting useExplosiveWand = new BindSetting("Взрывная палочка");
    public final BindSetting useFarewellHum = new BindSetting("Прощальный гул");
    public final BindSetting useExperienceBubble = new BindSetting("Пузырь опыта");
    public final BindSetting useSpecialCompass = new BindSetting("Особый компас");

    private final List<ItemBind> itemBinds = List.of(
            new ItemBind(useStun, "Стан"),
            new ItemBind(useTrap, "Трапка"),
            new ItemBind(useExplosiveTrap, "Взрывная трапка"),
            new ItemBind(useExplosiveThing, "Взрывная штучка", "Взрывная штука"),
            new ItemBind(useSnowball, "Ком снега"),
            new ItemBind(useJakeLantern, "Светильник Джейка"),
            new ItemBind(useExplosiveWand, "Взрывная палочка"),
            new ItemBind(useFarewellHum, "Прощальный гул"),
            new ItemBind(useExperienceBubble, "Пузырь опыта"),
            new ItemBind(useSpecialCompass, "Особый компас")
    );

    private boolean active;
    private boolean inventorySwap;
    private int originalHotbarSlot = -1;
    private int sourceScreenSlot = -1;
    private int swappedHotbarSlot = -1;
    private long restoreAtMs;

    private HolyWorldHelper() {
    }

    @EventTarget
    public void onKey(EventKey event) {
        if (event.getAction() != 1 || mc.currentScreen != null || mc.player == null
                || mc.world == null || mc.interactionManager == null || active) {
            return;
        }

        for (ItemBind itemBind : itemBinds) {
            if (event.getKeyCode() == itemBind.bind().getKeyCode()) {
                use(itemBind);
                return;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!active) {
            return;
        }

        if (mc.player == null || mc.world == null || mc.interactionManager == null) {
            resetState();
            return;
        }

        if (System.currentTimeMillis() >= restoreAtMs) {
            restore();
        }
    }

    private void use(ItemBind itemBind) {
        if (isRequestedHolyWorldItem(mc.player.getOffHandStack(), itemBind)) {
            mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
            mc.player.swingHand(Hand.OFF_HAND);
            return;
        }

        int inventorySlot = findSlot(itemBind);
        if (inventorySlot == -1) {
            MessageUtil.displayWarning("Предмет «" + itemBind.displayName() + "» не найден в инвентаре");
            return;
        }

        originalHotbarSlot = mc.player.getInventory().selectedSlot;

        if (inventorySlot < 9) {
            mc.player.getInventory().selectedSlot = inventorySlot;
            syncSelectedSlot();
        } else {
            sourceScreenSlot = inventorySlot;
            swappedHotbarSlot = originalHotbarSlot;
            clickSwap(sourceScreenSlot, swappedHotbarSlot);
            inventorySwap = true;
        }

        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        mc.player.swingHand(Hand.MAIN_HAND);

        active = true;
        restoreAtMs = System.currentTimeMillis() + RESTORE_DELAY_MS;
    }

    private int findSlot(ItemBind requestedItem) {
        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (isRequestedHolyWorldItem(stack, requestedItem)) {
                return slot;
            }
        }
        return -1;
    }

    private boolean isRequestedHolyWorldItem(ItemStack stack, ItemBind requestedItem) {
        if (stack.isEmpty() || !stack.contains(DataComponentTypes.CUSTOM_NAME)) {
            return false;
        }

        String itemName = normalize(stack.getName().getString());
        ItemBind bestMatch = null;
        int bestMatchLength = -1;

        for (ItemBind candidate : itemBinds) {
            for (String alias : candidate.aliases()) {
                String normalizedAlias = normalize(alias);
                if (containsPhrase(itemName, normalizedAlias) && normalizedAlias.length() > bestMatchLength) {
                    bestMatch = candidate;
                    bestMatchLength = normalizedAlias.length();
                }
            }
        }

        return bestMatch == requestedItem;
    }

    private static boolean containsPhrase(String text, String phrase) {
        return (" " + text + " ").contains(" " + phrase + " ");
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replace('ё', 'е')
                .replaceAll("[^\\p{L}\\p{N}]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }

    private void restore() {
        if (mc.player != null && mc.interactionManager != null) {
            if (inventorySwap && sourceScreenSlot != -1 && swappedHotbarSlot != -1) {
                clickSwap(sourceScreenSlot, swappedHotbarSlot);
                // Принудительно возвращаем выбранный слот на тот, где теперь лежит старый предмет
                mc.player.getInventory().selectedSlot = swappedHotbarSlot;
                syncSelectedSlot();
            } else if (originalHotbarSlot != -1
                    && mc.player.getInventory().selectedSlot != originalHotbarSlot) {
                mc.player.getInventory().selectedSlot = originalHotbarSlot;
                syncSelectedSlot();
            }

            // ЗАМЕДЛЕНИЕ / СБРОС СПРИНТА при возврате предмета
            slowdown();
        }
        resetState();
    }

    /** Сбрасывает спринт локально и на сервере, создавая эффект замедления. */
    private void slowdown() {
        if (mc.player == null) return;

        mc.player.setSprinting(false);

        if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(
                    new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING)
            );
        }
    }

    private void clickSwap(int screenSlot, int hotbarSlot) {
        mc.interactionManager.clickSlot(
                mc.player.playerScreenHandler.syncId,
                screenSlot,
                hotbarSlot,
                SlotActionType.SWAP,
                mc.player
        );
    }

    private void syncSelectedSlot() {
        if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(
                    new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot)
            );
        }
    }

    private void resetState() {
        active = false;
        inventorySwap = false;
        originalHotbarSlot = -1;
        sourceScreenSlot = -1;
        swappedHotbarSlot = -1;
        restoreAtMs = 0L;
    }

    @Override
    public void onEnable() {
        resetState();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        restore();
        super.onDisable();
    }

    private record ItemBind(BindSetting bind, List<String> aliases) {
        private ItemBind(BindSetting bind, String... aliases) {
            this(bind, List.of(aliases));
        }

        private String displayName() {
            return aliases.getFirst();
        }
    }
}
