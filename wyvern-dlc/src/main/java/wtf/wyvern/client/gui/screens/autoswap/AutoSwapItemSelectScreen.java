package wtf.wyvern.client.gui.screens.autoswap;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import wtf.wyvern.client.modules.impl.combat.AutoSwap;

/** Inventory picker for an individual triple-swap slot. */
public final class AutoSwapItemSelectScreen extends Screen {
   private static final int COLS = 9, ROWS = 4, CELL = 20;
   private final AutoSwap autoSwap;
   private final int wheelSlot;
   private final int holdKey;

   public AutoSwapItemSelectScreen(AutoSwap autoSwap, int wheelSlot, int holdKey) {
      super(Text.literal("Выбор предмета"));
      this.autoSwap = autoSwap;
      this.wheelSlot = wheelSlot;
      this.holdKey = holdKey;
   }

   @Override
   public boolean shouldPause() { return false; }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      int x = (this.width - COLS * CELL - 16) / 2;
      int y = (this.height - ROWS * CELL - 38) / 2;
      context.fill(0, 0, this.width, this.height, 0x55000000);
      context.fill(x, y, x + COLS * CELL + 16, y + ROWS * CELL + 38, 0xEE17171D);
      context.drawTextWithShadow(this.textRenderer, "Выберите предмет для слота " + (this.wheelSlot + 1), x + 8, y + 8, 0xFFFFFFFF);
      for (int index = 0; index < COLS * ROWS; ++index) {
         int sx = x + 8 + (index % COLS) * CELL;
         int sy = y + 26 + (index / COLS) * CELL;
         boolean hovered = mouseX >= sx && mouseX < sx + 18 && mouseY >= sy && mouseY < sy + 18;
         context.fill(sx, sy, sx + 18, sy + 18, hovered ? 0xFF4D4D5A : 0xFF292932);
         ItemStack stack = this.client.player.getInventory().getStack(index);
         if (!stack.isEmpty()) context.drawItem(stack, sx + 1, sy + 1);
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
      int x = (this.width - COLS * CELL - 16) / 2;
      int y = (this.height - ROWS * CELL - 38) / 2;
      for (int index = 0; index < COLS * ROWS; ++index) {
         int sx = x + 8 + (index % COLS) * CELL;
         int sy = y + 26 + (index / COLS) * CELL;
         if (mouseX >= sx && mouseX < sx + 18 && mouseY >= sy && mouseY < sy + 18) {
            ItemStack stack = this.client.player.getInventory().getStack(index);
            if (!stack.isEmpty()) this.autoSwap.setWheelSlotItem(this.wheelSlot, stack);
            this.client.setScreen(new AutoSwapWheelScreen(this.autoSwap, this.holdKey));
            return true;
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void close() { this.client.setScreen(new AutoSwapWheelScreen(this.autoSwap, this.holdKey)); }
}
