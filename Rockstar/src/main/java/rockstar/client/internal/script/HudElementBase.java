package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.ui.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import rockstar.client.compat.RenderSystem;
import pyrock.utility.render.ColorRGBA;

public abstract class HudElementBase extends AbstractHudWidget {
   public HudElementBase(String localValue1, String localValue2) {
      super(localValue1, localValue2);
   }

   @Override
   public void render(UiRenderContext localValue1) {
      this.update(localValue1);
      float localValue2 = this.animation.internalMethod02881() * this.visible.internalMethod02881();
      if (localValue2 != 0.0F) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1.0F, localValue2));
         float localValue3 = this.inertion.getX();
         float localValue4 = this.inertion.getY();
         float localValue5 = this.inertion.getWidth();
         float localValue6 = this.inertion.getHeight();
         float localValue7 = 0.5F + localValue2 * 0.5F - 0.05F * this.selecting.internalMethod02881();
         HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue3 + localValue5 / 2.0F, localValue4 + localValue6 / 2.0F, localValue7);
         localValue1.drawShadow(
            localValue3 - 5.0F,
            localValue4 - 5.0F,
            localValue5 + 10.0F,
            localValue6 + 10.0F,
            15.0F,
            CornerRadii.internalMethod03908(6.0F),
            ColorRGBA.BLACK.withAlpha(63.75F * this.dragAnim.internalMethod02881())
         );
         ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue3, localValue4, localValue5, Math.max(20.0F, localValue6));
         this.renderComponent(localValue1);
         ScissorStack.internalMethod07643();
         HudRenderUtils.internalMethod00012(localValue1.getMatrices());
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   @Override
   public void renderComponent(UiRenderContext localValue1) {
      SizedFont localValue2 = Fonts.internalField1154.internalMethod01432(7.0F);
      if (InterfaceModule.internalMethod09717()) {
         this.inertion.internalMethod00826(this.x, this.y, this.width, this.height, this.isDragging());
      } else {
         this.inertion.set(this.x, this.y, this.width, this.height);
      }

      float localValue3 = this.inertion.getX();
      float localValue4 = this.inertion.getY();
      float localValue5 = this.inertion.getWidth();
      float localValue6 = this.inertion.getHeight();
      localValue1.drawClientRect(localValue3, localValue4, localValue5, Math.max(20.0F, localValue6), this.animation.internalMethod02881(), this.dragAnim.internalMethod02881(), 7.0F);
      float localValue7 = 8.0F;
      localValue1.drawText(
         Fonts.internalField0449.internalMethod01432(7.0F),
         LanguageManager.internalMethod07214(this.name),
         localValue3 + 7.0F,
         localValue4 + UiUtils.internalMethod07116(localValue2.internalMethod04890(), 18.0F) + 0.5F,
         ThemeColors.internalMethod08459()
      );
      localValue1.drawIcon(this.icon, localValue3 + localValue5 - localValue7 - 7.0F, localValue4 + 6.0F, localValue7, ThemeColors.internalField1613);
      if (localValue6 >= 23.0F) {
         localValue1.drawRect(localValue3, localValue4 + 18.0F, localValue5, 4.0F, ThemeColors.internalMethod09447());
      }
   }
}
