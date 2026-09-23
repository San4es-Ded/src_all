package wtf.wyvern.client.modules.impl.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "FTHelper",
        category = Category.MISC,
        description = "Легитное использование специальных предметов mc.funtime.su из всего инвентаря"
)
@FastNative
public final class FTHelper extends Module {
    public static final FTHelper INSTANCE = new FTHelper();

    private static final long RESTORE_DELAY_MS = 1_000L;

    public final BindSetting useDezor = new BindSetting("Дезориентация");
    public final BindSetting useTrap = new BindSetting("Трапка");
    public final BindSetting usePil = new BindSetting("Явная пыль");
    public final BindSetting useSmerch = new BindSetting("Огненный смерч");
    public final BindSetting usePlast = new BindSetting("Пласт");
    public final BindSetting useAura = new BindSetting("Божья аура");
    public final BindSetting useSnow = new BindSetting("Снежок");

    private final List<ItemBind> itemBinds = List.of(
            new ItemBind(useDezor, Items.ENDER_EYE, "Дезориентация"),
            new ItemBind(useTrap, Items.NETHERITE_SCRAP, "Трапка"),
            new ItemBind(usePil, Items.SUGAR, "Явная пыль"),
            new ItemBind(useSmerch, Items.FIRE_CHARGE, "Огненный смерч"),
            new ItemBind(usePlast, Items.DRIED_KELP, "Пласт"),
            new ItemBind(useAura, Items.PHANTOM_MEMBRANE, "Божья аура"),
            new ItemBind(useSnow, Items.SNOWBALL, "Снежок")
    );

    private boolean active;
    private boolean inventorySwap;
    private int originalHotbarSlot = -1;
    private int sourceScreenSlot = -1;
    private int swappedHotbarSlot = -1;
    private long restoreAtMs;

    private FTHelper() {
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
        if (isRequestedFunTimeItem(mc.player.getOffHandStack(), itemBind)) {
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
            if (isRequestedFunTimeItem(mc.player.getInventory().getStack(slot), requestedItem)) {
                return slot;
            }
        }
        return -1;
    }

    private static boolean isRequestedFunTimeItem(ItemStack stack, ItemBind requestedItem) {
        return !stack.isEmpty() && stack.isOf(requestedItem.item());
    }

    private void restore() {
        if (mc.player != null && mc.interactionManager != null) {
            if (inventorySwap && sourceScreenSlot != -1 && swappedHotbarSlot != -1) {
                clickSwap(sourceScreenSlot, swappedHotbarSlot);
            } else if (originalHotbarSlot != -1
                    && mc.player.getInventory().selectedSlot != originalHotbarSlot) {
                mc.player.getInventory().selectedSlot = originalHotbarSlot;
                syncSelectedSlot();
            }
        }
        resetState();
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

    private record ItemBind(BindSetting bind, Item item, String displayName) {
    }
}
