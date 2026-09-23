package ru.prism.module.impl.utils;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.BrushItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.OnAStickItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.other.Instance;
import ru.prism.utils.other.TimerUtil;

@ModuleInfo(
        name = "Chest Sorter",
        desc = "Сливает стаки и раскладывает вещи по категориям, забирает и скидывает содержимое сундука.",
        category = Category.UTILITIES,
        autoEnabled = true,
        allowDisable = false,
        hidden = true
)
public class ChestSorter extends Module {

    public static ChestSorter get() {
        return Instance.get(ChestSorter.class);
    }

    public final SliderSetting delay = new SliderSetting(this, "Задержка", 40, 0, 300, 10);

    public static final int BUTTON_COUNT = 3;
    private static final int BUTTON_SIZE = 12;
    private static final int BUTTON_GAP = 1;
    private static final int BUTTON_MARGIN = 4;

    private static final Identifier BUTTON_TEXTURE = Identifier.ofVanilla("widget/button");
    private static final Identifier BUTTON_TEXTURE_HOVERED = Identifier.ofVanilla("widget/button_highlighted");

    private static final String[] TOOLTIPS = {
            "Отсортировать сундук",
            "Выложить",
            "Положить"
    };

    private static final String[][] BUTTON_ICONS = {
            {
                    "########",
                    "########",
                    "........",
                    "######..",
                    "######..",
                    "........",
                    "####....",
                    "####...."
            },
            {
                    ".######.",
                    ".#....#.",
                    ".######.",
                    "...##...",
                    "...##...",
                    ".######.",
                    "..####..",
                    "...##..."
            },
            {
                    "...##...",
                    "..####..",
                    ".######.",
                    "...##...",
                    "...##...",
                    ".######.",
                    ".#....#.",
                    ".######."
            }
    };


    private boolean sorting;
    private SortMode mode = SortMode.MERGING;
    private int currentSlot;
    private int lastSourceCount = -1;
    private int idleClicks;
    private final TimerUtil timer = new TimerUtil();

    public static boolean isSupportedScreen(HandledScreen<?> screen) {
        if (screen == null || screen.getScreenHandler() == null) return false;
        ScreenHandler handler = screen.getScreenHandler();
        return (handler instanceof GenericContainerScreenHandler || handler instanceof ShulkerBoxScreenHandler)
                && handler.slots.size() > 36;
    }

    public static int buttonX(int screenX, int backgroundWidth, int index) {
        int total = BUTTON_COUNT * BUTTON_SIZE + (BUTTON_COUNT - 1) * BUTTON_GAP;
        int startX = screenX + backgroundWidth - BUTTON_MARGIN - total;
        return startX + index * (BUTTON_SIZE + BUTTON_GAP);
    }

    public static int buttonY(int screenY) {
        return screenY + 3;
    }

    public static boolean isHovered(double mouseX, double mouseY, int bx, int by) {
        return mouseX >= bx && mouseX <= bx + BUTTON_SIZE && mouseY >= by && mouseY <= by + BUTTON_SIZE;
    }

    public void renderButton(DrawContext context, int bx, int by, int mouseX, int mouseY, int index) {
        boolean hovered = isHovered(mouseX, mouseY, bx, by);

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED,
                hovered ? BUTTON_TEXTURE_HOVERED : BUTTON_TEXTURE, bx, by, BUTTON_SIZE, BUTTON_SIZE);

        int offset = (BUTTON_SIZE - BUTTON_ICONS[index][0].length()) / 2;
        drawIcon(context, bx + offset, by + offset + 1, BUTTON_ICONS[index], 0x80000000);
        drawIcon(context, bx + offset, by + offset, BUTTON_ICONS[index], 0xFFFFFFFF);

        if (hovered) {
            context.drawTooltip(mc.textRenderer, Text.literal(TOOLTIPS[index]), mouseX, mouseY);
        }
    }

    private static void drawIcon(DrawContext context, int x, int y, String[] icon, int color) {
        for (int row = 0; row < icon.length; row++) {
            String line = icon[row];
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) != '#') continue;
                context.fill(x + col, y + row, x + col + 1, y + row + 1, color);
            }
        }
    }

    public void onButtonClick(ScreenHandler handler, int index) {
        if (index == 0) {
            startSorting(handler);
        } else {
                startMove(handler, index == 1);
        }
    }

    public void startSorting(ScreenHandler handler) {
        if (handler == null) return;
        if (handler.slots.size() - 36 <= 0) return;
        this.sorting = true;
        this.mode = SortMode.MERGING;
        this.currentSlot = 0;
        this.idleClicks = 0;
        this.timer.reset();
    }

    private void startMove(ScreenHandler handler, boolean takeFromContainer) {
        if (handler == null) return;
        if (handler.slots.size() - 36 <= 0) return;
        this.sorting = true;
        this.mode = takeFromContainer ? SortMode.TAKING : SortMode.DEPOSITING;
        this.currentSlot = 0;
        this.lastSourceCount = -1;
        this.idleClicks = 0;
        this.timer.reset();
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (!sorting || mc.player == null) return;

        ScreenHandler handler = mc.player.currentScreenHandler;
        if (handler == null || handler == mc.player.playerScreenHandler) {
            sorting = false;
            return;
        }

        if (!timer.passed(delay.getValue().longValue())) return;

        int containerSlots = handler.slots.size() - 36;
        if (containerSlots <= 0) {
            sorting = false;
            return;
        }

        if (mode == SortMode.TAKING) {
            moveAll(handler, 0, containerSlots);
            return;
        }

        if (mode == SortMode.DEPOSITING) {
            moveAll(handler, containerSlots, handler.slots.size());
            return;
        }

        if (mode == SortMode.MERGING) {
            boolean merged = false;

            for (int i = 0; i < containerSlots && !merged; i++) {
                ItemStack stackI = handler.slots.get(i).getStack();
                if (stackI.isEmpty() || stackI.getCount() >= stackI.getMaxCount()) continue;

                for (int j = i + 1; j < containerSlots; j++) {
                    ItemStack stackJ = handler.slots.get(j).getStack();
                    if (!canMerge(stackI, stackJ)) continue;

                    mc.interactionManager.clickSlot(handler.syncId, j, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    if (!handler.getCursorStack().isEmpty()) {
                        mc.interactionManager.clickSlot(handler.syncId, j, 0, SlotActionType.PICKUP, mc.player);
                    }

                    merged = true;
                    break;
                }
            }

            if (merged) {
                timer.reset();
                return;
            }

            mode = SortMode.SORTING;
            currentSlot = 0;
        }

        if (mode == SortMode.SORTING) {
            while (currentSlot < containerSlots) {
                int minSlot = currentSlot;
                ItemStack minStack = handler.slots.get(currentSlot).getStack();

                for (int k = currentSlot + 1; k < containerSlots; k++) {
                    ItemStack stackK = handler.slots.get(k).getStack();
                    if (compareStacks(stackK, minStack) < 0) {
                        minSlot = k;
                        minStack = stackK;
                    }
                }

                if (minSlot != currentSlot) {
                    mc.interactionManager.clickSlot(handler.syncId, currentSlot, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(handler.syncId, minSlot, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(handler.syncId, currentSlot, 0, SlotActionType.PICKUP, mc.player);
                    currentSlot++;
                    timer.reset();
                    return;
                }

                currentSlot++;
            }

            sorting = false;
        }
    }

    private void moveAll(ScreenHandler handler, int fromStart, int fromEnd) {
        int count = 0;
        int target = -1;

        for (int i = fromStart; i < fromEnd; i++) {
            if (!handler.slots.get(i).getStack().isEmpty()) {
                count++;
                if (target < 0) target = i;
            }
        }

        if (target < 0) {
            sorting = false;
            return;
        }

        if (count == lastSourceCount) {
            if (++idleClicks > 30) {
                sorting = false;
                return;
            }
        } else {
            idleClicks = 0;
        }
        lastSourceCount = count;

        mc.interactionManager.clickSlot(handler.syncId, target, 0, SlotActionType.QUICK_MOVE, mc.player);
        timer.reset();
    }

    private boolean canMerge(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) return false;
        if (a.getItem() != b.getItem()) return false;
        return a.getCount() < a.getMaxCount() && ItemStack.areItemsAndComponentsEqual(a, b);
    }

    private int compareStacks(ItemStack a, ItemStack b) {
        if (a.isEmpty() && b.isEmpty()) return 0;
        if (a.isEmpty()) return 1;
        if (b.isEmpty()) return -1;

        int rankA = sortRank(a);
        int rankB = sortRank(b);
        if (rankA != rankB) return Integer.compare(rankA, rankB);

        String idA = Registries.ITEM.getId(a.getItem()).toString();
        String idB = Registries.ITEM.getId(b.getItem()).toString();
        int cmp = idA.compareTo(idB);
        return cmp != 0 ? cmp : Integer.compare(b.getCount(), a.getCount());
    }

    private int sortRank(ItemStack stack) {
        EquippableComponent equippable = stack.get(DataComponentTypes.EQUIPPABLE);
        if (equippable != null) {
            return switch (equippable.slot()) {
                case HEAD -> 0;
                case CHEST -> 1;
                case LEGS -> 2;
                case FEET -> 3;
                case BODY -> 4;
                case OFFHAND -> 5;
                default -> 60;
            };
        }

        Item item = stack.getItem();
        String id = Registries.ITEM.getId(item).getPath();

        if (id.endsWith("_sword")) return 10;
        if (item instanceof AxeItem) return 11;
        if (item instanceof MaceItem) return 12;
        if (item instanceof TridentItem) return 13;
        if (item instanceof BowItem) return 14;
        if (item instanceof CrossbowItem) return 15;
        if (item instanceof ArrowItem || item instanceof SpectralArrowItem) return 16;

        if (id.endsWith("_pickaxe")) return 20;
        if (item instanceof ShovelItem) return 21;
        if (item instanceof HoeItem) return 22;
        if (item instanceof ShearsItem) return 23;
        if (item instanceof FishingRodItem) return 24;
        if (item instanceof FlintAndSteelItem) return 25;
        if (item instanceof BrushItem) return 26;
        if (item instanceof OnAStickItem) return 27;

        if (stack.contains(DataComponentTypes.FOOD)) return 30;
        if (item instanceof PotionItem || item instanceof ThrowablePotionItem) return 31;

        if (item instanceof BlockItem) return 50;

        return 40;
    }

    @Override
    protected void onDisable() {
        sorting = false;
        super.onDisable();
    }

    private enum SortMode {
        MERGING,
        SORTING,
        TAKING,
        DEPOSITING
    }
}
