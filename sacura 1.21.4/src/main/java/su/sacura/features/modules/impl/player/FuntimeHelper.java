package su.sacura.features.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import su.sacura.events.input.KeyEvent;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BindSetting;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.util.impl.player.InventoryUtil;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.type.ISetting;

@ModuleAnnotations(name = "FunTime Helper", category = Category.PLAYER)
public class FuntimeHelper extends Module {
    private final BindSetting trapka = (new BindSetting("Кнопка трапки", Integer.valueOf(-1))).setDescription("Использует трапку");
    private final BindSetting disorientation = (new BindSetting("Кнопка дезориентации", Integer.valueOf(-1))).setDescription("Использует дезориентацию");
    private final BindSetting plast = (new BindSetting("Кнопка пласта", Integer.valueOf(-1))).setDescription("Использует пласт");
    private final BindSetting godaura = (new BindSetting("Кнопка божьей ауры", Integer.valueOf(-1))).setDescription("Использует божью ауру");
    private final BooleanSetting inventoryUse = (new BooleanSetting("Использовать из инвентаря", Boolean.valueOf(false))).setDescription("Использует из инвентаря предметы");
    private final Map<BindSetting, Item> binds = new LinkedHashMap<>();

    public FuntimeHelper() {
        addSettings(new ISetting[] { (ISetting)this.trapka, (ISetting)this.disorientation, (ISetting)this.plast, (ISetting)this.godaura });
        // Исправлено: field_22021 -> POTION (зелье)
        this.binds.put(this.trapka, Items.POTION);
        // Исправлено: field_8449 -> ENDER_PEARL (эндер-жемчуг)
        this.binds.put(this.disorientation, Items.ENDER_PEARL);
        // Исправлено: field_8551 -> CHORUS_FRUIT (плод хоруса)
        this.binds.put(this.plast, Items.CHORUS_FRUIT);
        // Исправлено: field_8614 -> SHIELD (щит)
        this.binds.put(this.godaura, Items.SHIELD);
    }

    @Subscribe
    public void onEvent(KeyEvent e) {
        if (mc.player == null)
            return;
        if (mc.getCurrentServerEntry() != null) {
            String serverAddress = (mc.getCurrentServerEntry()).address.toLowerCase();
            if (serverAddress.contains("holyworld") || serverAddress.contains("spacetimes")) {
                if (this.enable) {
                    toggle();
                    message(String.valueOf(Formatting.RED) + "Эта функция запрещена на этом сервере!");
                }
                return;
            }
        }
        for (Map.Entry<BindSetting, Item> entry : this.binds.entrySet()) {
            if (e.isKeyDown(((Integer)((BindSetting)entry.getKey()).get()).intValue())) {
                useItem(entry.getValue());
                return;
            }
        }
    }

    private void useItem(Item item) {
        int[] slots = findSlots(item);
        InventoryUtil.use(slots[0], slots[1], ((Boolean)this.inventoryUse.get()).booleanValue());
    }

    private int[] findSlots(Item item) {
        if (mc.player == null)
            return new int[] { -1, -1 };
        PlayerInventory inv = mc.player.getInventory();
        int size = inv.size();
        int hotbarSlot = -1;
        int inventorySlot = -1;
        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                if (i < 9) {
                    if (hotbarSlot == -1)
                        hotbarSlot = i;
                    if (inventorySlot == -1)
                        inventorySlot = i + 36;
                } else if (inventorySlot == -1) {
                    inventorySlot = i;
                }
                if (hotbarSlot != -1 && inventorySlot != -1)
                    break;
            }
        }
        return new int[] { hotbarSlot, inventorySlot };
    }

    public static void message(String string) {
        if (mc == null || mc.player == null || mc.world == null || mc.inGameHud == null)
            return;
        int start = ColorProvider.getColorStyle(1.0F);
        int end = ColorProvider.getColorStyle(100.0F);
        mc.inGameHud.getChatHud().addMessage(applyGradient(string, start, end));
    }

    private static Text applyGradient(String string, int startColor, int endColor) {
        MutableText component = Text.empty();
        String name = "(sacura)";
        int length = "(sacura)".length();
        float inv = (length <= 1) ? 0.0F : (1.0F / (length - 1));
        for (int i = 0; i < length; i++) {
            int rgb = ColorProvider.blendColors(startColor, endColor, (length == 1) ? 0.5F : (i * inv)) & 0xFFFFFF;
            component.append((Text)Text.literal(String.valueOf("(sacura)".charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)).withBold(Boolean.valueOf(true))));
        }
        int gray = Color.GRAY.getRGB() & 0xFFFFFF;
        component.append((Text)Text.literal(" >> ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(gray)).withBold(Boolean.valueOf(true))));
        component.append((Text)Text.literal(string).setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)));
        return (Text)component;
    }
}