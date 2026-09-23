package wtf.wyvern.client.modules.impl.misc;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import wtf.wyvern.core.events.impl.input.EventKey;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.utility.game.other.InventoryUtil;
import wtf.wyvern.utility.game.other.NetworkUtils;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.wyvern.utility.game.player.PlayerInventoryComponent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "HWHelper",
        category = Category.MISC,
        description = "Помощник для сервера HolyWorld"
)
public class HWHelper extends Module {
    public static final HWHelper INSTANCE = new HWHelper();

    public final BindSetting useStun = new BindSetting("Стан");
    public final BindSetting useTrapka = new BindSetting("Трапка");
    public final BindSetting useExplosive = new BindSetting("Взрывная штучка");
    public final BindSetting useSnow = new BindSetting("Ком Снега");
    public final BindSetting useExplosiveTrap = new BindSetting("Взрывная трапка");

    boolean canUse = false;
    public boolean slow = false;
    private boolean progress = false;
    private boolean allow;
    private int savedInvSlot = -1;
    private int savedHotbarSlot = -1;
    private long delay = -1L;

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    Item swapItem;

    @FastNative
    private void useItemAndClick(Item item) {
        if (mc.player == null) return;
        if (InventoryUtil.findItem(item) < 0) return;
        if (progress) return;

        swapItem = item;
        progress = true;
        allow = true;
        canUse = true;
    }

    @FastNative
    @EventTarget
    public void onUse(EventUpdate e) {
        if (swapItem == null) return;
        itemController(swapItem);
    }

    private void itemController(Item item) {
        int slot = InventoryUtil.findItem(item);
        if (slot >= 0) {

            if (allow && InventoryUtil.findHotbar(Items.ENDER_PEARL) >= 0) {
                int hotbarSlot = slot - 36;

                NetworkUtils.sendPacket(new UpdateSelectedSlotC2SPacket(hotbarSlot));
                PlayerIntersectionUtil.sendSequencedPacket(sequence ->
                        new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, sequence, mc.player.getYaw(), mc.player.getPitch()));
                NetworkUtils.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

                delay = System.currentTimeMillis() + 150;
                allow = false;

            } else if (allow) {
                PlayerInventoryComponent.disableMoveKeys();

                if (!MovingUtil.hasPlayerMovement()) {
                    int hotbarSlot = mc.player.getInventory().selectedSlot % 8 + 1;

                    savedInvSlot = slot;
                    savedHotbarSlot = hotbarSlot;

                    PlayerIntersectionUtil.clickSlot(
                            mc.player.playerScreenHandler.syncId,
                            InventoryUtil.indexToSlot(savedInvSlot),
                            hotbarSlot,
                            SlotActionType.SWAP,
                            mc.player
                    );

                    NetworkUtils.sendPacket(new UpdateSelectedSlotC2SPacket(hotbarSlot));
                    PlayerIntersectionUtil.sendSequencedPacket(sequence ->
                            new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, sequence, mc.player.getYaw(), mc.player.getPitch()));
                    NetworkUtils.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

                    delay = System.currentTimeMillis() + 200;

                    if (mc.currentScreen == null) {
                        NetworkUtils.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                    }

                    allow = false;
                }
            }
        }

        if (delay >= 0L && System.currentTimeMillis() >= delay) {

            if (savedInvSlot != -1 && savedHotbarSlot != -1) {
                PlayerInventoryComponent.disableMoveKeys();

                if (!MovingUtil.hasPlayerMovement()) {
                    NetworkUtils.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));

                    PlayerIntersectionUtil.clickSlot(
                            mc.player.playerScreenHandler.syncId,
                            InventoryUtil.indexToSlot(savedInvSlot),
                            savedHotbarSlot,
                            SlotActionType.SWAP,
                            mc.player
                    );

                    if (mc.currentScreen == null) {
                        NetworkUtils.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                    }

                    savedInvSlot = -1;
                    savedHotbarSlot = -1;
                    delay = -1L;
                    swapItem = null;
                    progress = false;
                    canUse = false;
                }

            } else {
                NetworkUtils.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
                delay = -1L;
                swapItem = null;
                progress = false;
                canUse = false;
            }
        }
    }

    @FastNative
    @EventTarget
    public void onKey(EventKey e) {
        if (mc.currentScreen == null && mc.player != null) {
            if (canUse) return;
            boolean keyWasPressed = e.getKeyCode() == useStun.getKeyCode() ||
                    e.getKeyCode() == useTrapka.getKeyCode() ||
                    e.getKeyCode() == useExplosive.getKeyCode() ||
                    e.getKeyCode() == useSnow.getKeyCode() ||
                    e.getKeyCode() == useExplosiveTrap.getKeyCode();

            if (keyWasPressed) {
                this.slow = true;
            }

            if (e.getAction() == 1) {
                if (useStun.getKeyCode() == e.getKeyCode()) {
                    useItemAndClick(Items.NETHER_STAR);
                }

                if (useTrapka.getKeyCode() == e.getKeyCode()) {
                    useItemAndClick(Items.POPPED_CHORUS_FRUIT);
                }

                if (useExplosive.getKeyCode() == e.getKeyCode()) {
                    useItemAndClick(Items.FIRE_CHARGE);
                }

                if (useSnow.getKeyCode() == e.getKeyCode()) {
                    useItemAndClick(Items.SNOWBALL);
                }

                if (useExplosiveTrap.getKeyCode() == e.getKeyCode()) {
                    useItemAndClick(Items.PRISMARINE_SHARD);
                }
            }
        }
    }

    @FastNative
    @EventTarget
    public void onUpdate(EventUpdate e) {
        this.slow = false;
    }

    @FastNative
    @Override
    public void onEnable() {
        super.onEnable();
        swapItem = null;
        progress = false;
        allow = false;
        canUse = false;
        delay = -1L;
        savedInvSlot = -1;
        savedHotbarSlot = -1;
    }

    @FastNative
    @Override
    public void onDisable() {
        super.onDisable();
        swapItem = null;
        progress = false;
        allow = false;
        canUse = false;
        delay = -1L;
        savedInvSlot = -1;
        savedHotbarSlot = -1;
    }
}
