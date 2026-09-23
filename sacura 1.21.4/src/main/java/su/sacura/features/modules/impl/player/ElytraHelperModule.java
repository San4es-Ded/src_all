package su.sacura.features.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import java.awt.Color;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import su.sacura.events.input.KeyEvent;
import su.sacura.events.tick.EventUpdate;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BindSetting;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.util.impl.player.InventoryUtil;
import su.sacura.util.impl.render.providers.ColorProvider;

@ModuleAnnotations(name="Elytra Helper", category=Category.PLAYER)
public class ElytraHelperModule
        extends Module {
    private final BindSetting elytraKey = new BindSetting("Кнопка элитры", 0).setDescription("Использует элитру");
    private final BindSetting fireworkKey = new BindSetting("Кнопка фейерверка", 0).setDescription("Использует фейерверк");
    private final BooleanSetting autoTakeoff = new BooleanSetting("Авто-взлёт", true).setDescription("Автоматически взлетает на элитре");
    private int takeoffTicks = 0;
    private boolean waitingToGlide = false;
    private boolean sprintResetting = false;
    private int sprintResetTicks = 0;
    private Runnable scheduledSwap = null;

    public ElytraHelperModule() {
        this.addSettings(this.elytraKey, this.fireworkKey, this.autoTakeoff);
    }

    @Subscribe
    public void onUpdate(EventUpdate event) {
        String serverAddress;
        if (ElytraHelperModule.mc.player == null) {
            return;
        }
        if (mc.getCurrentServerEntry() != null && ((serverAddress = ElytraHelperModule.mc.getCurrentServerEntry().address.toLowerCase()).contains("holyworld") || serverAddress.contains("spacetimes"))) {
            if (this.enable) {
                this.toggle();
                ElytraHelperModule.message(String.valueOf(Formatting.RED) + "Эта функция запрещена на этом сервере!");
            }
            return;
        }
        if (((Boolean)this.autoTakeoff.get()).booleanValue()) {
            this.handleAutoTakeoff();
        }
        if (this.sprintResetting) {
            --this.sprintResetTicks;
            mc.player.setSprinting(false); // Было method_5728(false)
            if (this.sprintResetTicks <= 0) {
                this.sprintResetting = false;
            }
        }
        if (this.scheduledSwap != null && !this.sprintResetting) {
            this.scheduledSwap.run();
            this.scheduledSwap = null;
        }
    }

    @Subscribe
    public void onKey(KeyEvent e) {
        if (ElytraHelperModule.mc.player == null || e.action() != 1) {
            return;
        }
        ItemStack equipped = ElytraHelperModule.mc.player.getEquippedStack(EquipmentSlot.CHEST); // Было method_6118(EquipmentSlot.CHEST)
        if (e.key() == ((Integer)this.elytraKey.get()).intValue()) {
            int chestPlateSlot = this.findChestplate();
            int elytraSlot = this.findItemSlot(Items.ELYTRA); // Было Items.field_8833
            if (equipped.getItem() == Items.ELYTRA) { // Было Items.field_8833
                this.startSprintReset(10);
                this.scheduledSwap = () -> {
                    if (chestPlateSlot != -1) {
                        this.swapSlots(chestPlateSlot, 6);
                        ElytraHelperModule.message("Свапнул на " + String.valueOf(Formatting.AQUA) + "нагрудник");
                    } else {
                        this.swapSlots(elytraSlot == -1 ? 6 : elytraSlot, 6);
                        ElytraHelperModule.message(String.valueOf(Formatting.YELLOW) + "Элитра снята (нагрудников нет)");
                    }
                };
            } else if (elytraSlot != -1) {
                this.startSprintReset(3);
                this.scheduledSwap = () -> {
                    this.swapSlots(elytraSlot, 6);
                    ElytraHelperModule.message("Свапнул на " + String.valueOf(Formatting.RED) + "элитру");
                };
            } else {
                ElytraHelperModule.message(String.valueOf(Formatting.RED) + "Элитра не найдена!");
            }
        }
        if (e.key() == ((Integer)this.fireworkKey.get()).intValue() && equipped.getItem() == Items.ELYTRA) { // Было Items.field_8833
            InventoryUtil.inventorySwapClick2(Items.FIREWORK_ROCKET, true, false); // Было Items.field_8639
        }
    }

    private void startSprintReset(int ticks) {
        this.sprintResetting = true;
        this.sprintResetTicks = ticks;
        ElytraHelperModule.mc.player.setSprinting(false); // Было method_5728(false)
    }

    private void handleAutoTakeoff() {
        ItemStack chest = ElytraHelperModule.mc.player.getEquippedStack(EquipmentSlot.CHEST); // Было EquipmentSlot.field_6174
        if (chest.getItem() != Items.ELYTRA) { // Было Items.field_8833
            this.waitingToGlide = false;
            this.takeoffTicks = 0;
            return;
        }
        if (ElytraHelperModule.mc.player.isGliding()) { // Было method_6128()
            this.waitingToGlide = false;
            this.takeoffTicks = 0;
            return;
        }
        if (ElytraHelperModule.mc.player.isTouchingWater() && !this.waitingToGlide) { // Было method_24828()
            ElytraHelperModule.mc.player.jump(); // Было method_6043()
            this.waitingToGlide = true;
            this.takeoffTicks = 0;
            return;
        }
        if (this.waitingToGlide) {
            ++this.takeoffTicks;
            if (this.takeoffTicks >= 2 && ElytraHelperModule.mc.player.getVelocity().y < -0.08 && !ElytraHelperModule.mc.player.isGliding()) { // Было method_18798().y и method_6128()
                InventoryUtil.startFly();
                this.waitingToGlide = false;
                this.takeoffTicks = 0;
            }
            if (this.takeoffTicks > 10) {
                this.waitingToGlide = false;
                this.takeoffTicks = 0;
            }
        }
    }

    private int findItemSlot(Item item) {
        for (int i = 0; i < ElytraHelperModule.mc.player.getInventory().size(); ++i) { // Было method_31548().method_5439()
            ItemStack stack = ElytraHelperModule.mc.player.getInventory().getStack(i); // Было method_31548().method_5438(i)
            if (stack.getItem() != item) continue;
            return i;
        }
        return -1;
    }

    private int findChestplate() {
        Item[] chestplates;
        for (Item item : chestplates = new Item[]{Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.NETHERITE_CHESTPLATE}) { // Было field_22028, field_8058, field_8523, field_8678, field_8873, field_8577
            int slot = this.findItemSlot(item);
            if (slot == -1) continue;
            return slot;
        }
        return -1;
    }

    private void swapSlots(int from, int armorSlot) {
        int slot = from < 9 ? from + 36 : from;
        ElytraHelperModule.mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.SWAP, (PlayerEntity)ElytraHelperModule.mc.player);
        ElytraHelperModule.mc.interactionManager.clickSlot(0, armorSlot, 0, SlotActionType.SWAP, (PlayerEntity)ElytraHelperModule.mc.player);
        ElytraHelperModule.mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.SWAP, (PlayerEntity)ElytraHelperModule.mc.player);
    }

    public static void message(String string) {
        if (mc == null || ElytraHelperModule.mc.player == null || ElytraHelperModule.mc.world == null || ElytraHelperModule.mc.inGameHud == null) {
            return;
        }
        int start = ColorProvider.getColorStyle(1.0f);
        int end = ColorProvider.getColorStyle(100.0f);
        ElytraHelperModule.mc.inGameHud.getChatHud().addMessage(ElytraHelperModule.applyGradient(string, start, end));
    }

    private static Text applyGradient(String string, int startColor, int endColor) {
        MutableText component = Text.empty();
        String name = "(sacura)";
        int length = "(sacura)".length();
        float inv = length <= 1 ? 0.0f : 1.0f / (float)(length - 1);
        for (int i = 0; i < length; ++i) {
            int rgb = ColorProvider.blendColors(startColor, endColor, length == 1 ? 0.5f : (float)i * inv) & 0xFFFFFF;
            component.append((Text)Text.literal((String)String.valueOf("(sacura)".charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)rgb)).withBold(Boolean.valueOf(true))));
        }
        int gray = Color.GRAY.getRGB() & 0xFFFFFF;
        component.append((Text)Text.literal((String)" >> ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)gray)).withBold(Boolean.valueOf(true))));
        component.append((Text)Text.literal((String)string).setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)));
        return component;
    }
}