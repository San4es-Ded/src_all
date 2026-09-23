package wtf.wyvern.client.modules.impl.misc;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import wtf.wyvern.core.events.impl.input.EventKey;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.utility.game.other.InventoryUtil;
import wtf.wyvern.utility.game.other.NetworkUtils;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.wyvern.utility.game.player.PlayerInventoryComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "LGHelper",
        category = Category.MISC,
        description = "Помощник для сервера LonyGrief"
)
public class LonyGriefHelper extends Module {
    public static final LonyGriefHelper INSTANCE = new LonyGriefHelper();

    public final BindSetting useBlazeRod = new BindSetting("Ливалка");
    public final BindSetting cryingObsidianKey = new BindSetting("Трапка");
    public final BindSetting clayKey = new BindSetting("Ливалка с платформой");
    public final BooleanSetting autoGps = new BooleanSetting("АвтоГпс", true);
    public final BooleanSetting stop = new BooleanSetting("Остановка", false);

    boolean canUse = false;
    public boolean slow = false;
    private boolean progress = false;
    private final Pattern pattern = Pattern.compile("Его координаты: (-?\\d+)\\. (-?\\d+)\\. (-?\\d+)\\.");

    private boolean allow;
    private long delay = -1L;
    private int savedInvSlot = -1;
    private int savedHotbarSlot = -1;

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
            if (allow) {
                PlayerInventoryComponent.disableMoveKeys();
                if (stop.isEnabled()) PlayerInventoryComponent.unPressMoveKeys();

                if (!MovingUtil.hasPlayerMovement() || (!stop.isEnabled() && !mc.player.isSprinting())) {
                    int hotbarSlot = mc.player.getInventory().selectedSlot;
                    savedInvSlot = slot;
                    savedHotbarSlot = hotbarSlot;

                    PlayerIntersectionUtil.clickSlot(
                            mc.player.playerScreenHandler.syncId,
                            InventoryUtil.indexToSlot(savedInvSlot),
                            savedHotbarSlot,
                            SlotActionType.SWAP,
                            mc.player
                    );

                    PlayerIntersectionUtil.sendSequencedPacket(sequence ->
                            new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, sequence, mc.player.getYaw(), mc.player.getPitch()));
                    NetworkUtils.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

                    delay = System.currentTimeMillis() + 100;

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
                if (stop.isEnabled()) PlayerInventoryComponent.unPressMoveKeys();

                if (!MovingUtil.hasPlayerMovement() || (!stop.isEnabled() && !mc.player.isSprinting())) {
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
                delay = -1L;
                swapItem = null;
                progress = false;
                canUse = false;
            }
        }
    }

    @FastNative
    @EventTarget
    public void onPacket(EventPacket event) {
        if (autoGps.isEnabled() && event.isReceive() && event.getPacket() instanceof GameMessageS2CPacket gameMessage) {
            String text = gameMessage.content().getString();

            if (text.contains("Его координаты:")) {
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String x = matcher.group(1);
                    String z = matcher.group(3);
                    if (mc.player != null) {
                        mc.player.networkHandler.sendChatCommand("gps " + x + " " + z);
                    }
                }
            }
        }
    }

    @FastNative
    @EventTarget
    public void onKey(EventKey e) {
        if (mc.currentScreen == null && mc.player != null) {
            if (canUse) return;
            boolean keyWasPressed = e.getKeyCode() == useBlazeRod.getKeyCode() ||
                    e.getKeyCode() == cryingObsidianKey.getKeyCode() ||
                    e.getKeyCode() == clayKey.getKeyCode();

            if (keyWasPressed) {
                this.slow = true;
            }

            if (e.getAction() == 1) {
                if (useBlazeRod.getKeyCode() == e.getKeyCode()) {
                    useItemAndClick(Items.MAGMA_CREAM);
                }
                if (cryingObsidianKey.getKeyCode() == e.getKeyCode()) {
                    useItemAndClick(Items.CRYING_OBSIDIAN);
                }
                if (clayKey.getKeyCode() == e.getKeyCode()) {
                    useItemAndClick(Items.CLAY_BALL);
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
