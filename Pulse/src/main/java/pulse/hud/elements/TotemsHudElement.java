package pulse.hud.elements;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2fStack;
import pulse.hud.core.HudElement;
import pulse.hud.core.HudElementManager;
import pulse.module.ModuleRegistry;
import pulse.render.Renderer2D;
import pulse.render.Renderer2DImpl;

public class TotemsHudElement extends HudElement {
   private boolean settingsBound;
   private float userOffsetX = 0.0F;
   private float userOffsetY = 0.0F;

   public TotemsHudElement(float f, float f2) {
      super(f, f2);
      this.d = 81.0F;
      this.e = 9.0F;
   }

   @Override
   protected void a() {
      if (!this.settingsBound && ModuleRegistry.TOTEMS_HUD != null) {
         this.f().a(ModuleRegistry.TOTEMS_HUD);
         this.settingsBound = true;
      }

      float fScale = this.scale();
      this.d = 81.0F * fScale;
      this.e = 9.0F * fScale;
   }

   private float scale() {
      return ModuleRegistry.TOTEMS_HUD != null ? ModuleRegistry.TOTEMS_HUD.getScale().get() : 1.0F;
   }

   @Override
   public void a(Matrix3x2fStack MatrixStackVar, Renderer2D renderer2D, float f, float f2) {
      if (this.a.player != null) {
         if (HudElementManager.a().isEditing() || !this.a.player.isCreative() && !this.a.player.isSpectator()) {
            float cs = (float)(this.a.getWindow().getScaleFactor() / 2.0);
            float vanillaX = this.a.getWindow().getScaledWidth() / 2.0F + 10.0F;
            float vanillaY = this.a.getWindow().getScaledHeight() - 59.0F;
            if (this.i()) {
               float elementVanillaX = this.l() / cs;
               float elementVanillaY = this.m() / cs;
               this.userOffsetX = elementVanillaX - vanillaX;
               this.userOffsetY = elementVanillaY - vanillaY;
            } else {
               this.b = (vanillaX + this.userOffsetX) * cs;
               this.c = (vanillaY + this.userOffsetY) * cs;
            }

            this.a();
            DrawContext currentDrawContext = Renderer2DImpl.currentDrawContext;
            if (currentDrawContext != null && (HudElementManager.a().isEditing() || ModuleRegistry.TOTEMS_HUD != null && ModuleRegistry.TOTEMS_HUD.a())) {
               int totems = 0;

               for (int i = 0; i < 36; i++) {
                  ItemStack stack = this.a.player.getInventory().getStack(i);
                  if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                     totems += stack.getCount();
                  }
               }

               if (this.a.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
                  totems += this.a.player.getOffHandStack().getCount();
               }

               if (totems == 0 && !HudElementManager.a().isEditing()) {
                  return;
               }

               if (totems == 0 && HudElementManager.a().isEditing()) {
                  totems = 3;
               }

               float fScale = this.scale();
               Matrix3x2fStack matrices = currentDrawContext.getMatrices();
               matrices.pushMatrix();
               matrices.translate(this.b, this.c);
               matrices.scale(fScale, fScale);
               matrices.pushMatrix();
               matrices.scale(0.5F, 0.5F);
               ItemStack totemStack = new ItemStack(Items.TOTEM_OF_UNDYING);

               for (int i = 0; i < Math.min(totems, 10); i++) {
                  int x = (72 - i * 8) * 2;
                  int y = 0;
                  currentDrawContext.drawItem(totemStack, x, y);
               }

               matrices.popMatrix();
               matrices.popMatrix();
            }
         }
      }
   }
}
