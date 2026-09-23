package wtf.wyvern.mixin.client.render.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.client.modules.impl.misc.ItemScroller;

@Mixin({HandledScreen.class})
public abstract class HandledScreenMixin {
   @Shadow
   @Final
   protected ScreenHandler field_2797;

   @Shadow
   public abstract ScreenHandler method_17577();

   @Shadow
   protected abstract boolean method_2387(Slot var1, double var2, double var4);

   @Shadow
   protected abstract void method_2383(Slot var1, int var2, int var3, SlotActionType var4);

   @Unique
   private boolean attack() {
      return ItemScroller.INSTANCE.mouseHold;
   }

   @Unique
   private boolean shit() {
      return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 344);
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void drawScreenHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (MinecraftClient.getInstance().player == null) {
         return;
      }

      // Slot-independent conditions first: with ItemScroller off (the common case)
      // skip the whole per-slot hover-math loop entirely.
      if (!ItemScroller.INSTANCE.isEnabled() || !this.attack() || !this.shit()) {
         return;
      }

      var slots = MinecraftClient.getInstance().player.currentScreenHandler.slots;
      for(int i1 = 0; i1 < slots.size(); ++i1) {
         Slot slot = (Slot)slots.get(i1);
         if (this.method_2387(slot, (double)mouseX, (double)mouseY) && slot.isEnabled() && !slot.getStack().isEmpty()) {
            this.method_2383(slot, slot.id, 0, SlotActionType.QUICK_MOVE);
         }
      }
   }

}
