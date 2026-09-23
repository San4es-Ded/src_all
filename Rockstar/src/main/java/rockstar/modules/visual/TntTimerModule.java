package rockstar.modules.visual;









import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

@ModuleInfo(
   name = "TNT Timer",
   category = ModuleCategory.VISUALS,
   internalMethod08049 = true,
   internalMethod09633 = "modules.descriptions.tnt_timer"
)
public class TntTimerModule extends Module {
   private SizedFont internalField0447 = null;
   private final EventListener<PreHudRenderEvent> internalField0157 = localValue1 -> {
      if (this.internalField0447 == null) {
         this.internalField0447 = Fonts.internalField0449.internalMethod01432(12.0F);
      }

      ArrayList localValue2 = new ArrayList();

      for (Entity localValue4 : internalField0149.world.getEntities()) {
         if (localValue4 instanceof TntEntity localValue5) {
            localValue2.add(new TntTimerModule.InternalType0521(localValue5));
         }
      }

      ColoredQuadBatch localValue7 = new ColoredQuadBatch(VertexFormats.POSITION_COLOR, localValue1.getContext().getMatrices());

      for (TntTimerModule.InternalType0521 localValue11 : (Iterable<TntTimerModule.InternalType0521>)(Iterable<?>)localValue2) {
         localValue11.internalMethod06253(localValue1.getContext(), this.internalField0447, TntTimerModule.InternalType0520.internalField0095);
      }

      localValue7.internalMethod09053();

      for (TntTimerModule.InternalType0521 localValue12 : (Iterable<TntTimerModule.InternalType0521>)(Iterable<?>)localValue2) {
         localValue12.internalMethod06253(localValue1.getContext(), this.internalField0447, TntTimerModule.InternalType0520.internalField0094);
      }

      PlainGlyphBatch localValue10 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, this.internalField0447.internalMethod01335());

      for (TntTimerModule.InternalType0521 localValue6 : (Iterable<TntTimerModule.InternalType0521>)(Iterable<?>)localValue2) {
         localValue6.internalMethod06253(localValue1.getContext(), this.internalField0447, TntTimerModule.InternalType0520.internalField0988);
      }

      localValue10.internalMethod09053();
   };

   static enum InternalType0520 {
      internalField0095,
      internalField0094,
      internalField0988;
   }

   static class InternalType0521 {
      private final Vec3d internalField0283;
      private final String internalField0248;

      public InternalType0521(TntEntity localValue1) {
         this.internalField0283 = localValue1.getLerpedPos(MinecraftClientAccess.internalField0149.getRenderTickCounter().getTickProgress(true)).add(0.0, 0.5, 0.0);
         int localValue2 = localValue1.getFuse();
         float localValue3 = localValue2 / 20.0F;
         this.internalField0248 = LanguageManager.internalMethod00160("modules.tnt_timer.format", localValue3);
      }

      void internalMethod06253(CustomDrawContext localValue1, SizedFont localValue2, TntTimerModule.InternalType0520 localValue3) {
         Vec2f localValue4 = ScreenProjectionUtils.internalMethod00612(this.internalField0283);
         if (localValue4 != null) {
            float localValue5 = (float)MinecraftClientAccess.internalField0149.player.getEntityPos().distanceTo(this.internalField0283);
            float localValue6 = MathHelper.clamp(1.0F - localValue5 / 20.0F, 0.5F, 1.0F);
            float localValue7 = this.internalMethod04324(localValue4) / 255.0F;
            org.joml.Matrix3x2fStack localValue8 = localValue1.getMatrices();
            localValue8.pushMatrix();
            localValue8.translate(localValue4.x, localValue4.y);
            localValue8.scale(localValue6, localValue6);
            float localValue9 = localValue2.internalMethod00965(this.internalField0248) + 18.0F;
            float localValue10 = -localValue9 / 2.0F;
            float localValue11 = localValue2.internalMethod04890();
            float localValue12 = 4.0F;
            float localValue13 = 2.0F;
            switch (localValue3) {
               case internalField0095:
                  localValue1.drawRect(
                     localValue10 - localValue13, -localValue12, localValue9 + localValue13 * 2.0F + 4.0F, localValue11 + localValue12 * 2.0F, ThemeColors.internalField1309.mulAlpha(0.5F).mulAlpha(localValue7)
                  );
                  break;
               case internalField0094:
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue7);
                  localValue1.drawItem(new ItemStack(Items.TNT), (int)localValue10, -3);
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                  break;
               case internalField0988:
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue7);
                  localValue1.drawText(localValue2, this.internalField0248, (int)(localValue10 + 20.0F), 0.0F, ColorRGBA.WHITE);
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            localValue8.popMatrix();
         }
      }

      private float internalMethod04324(Vec2f localValue1) {
         if (localValue1 != null && MinecraftClientAccess.internalField0149.getWindow() != null) {
            int localValue2 = MinecraftClientAccess.internalField0149.getWindow().getScaledWidth();
            int localValue3 = MinecraftClientAccess.internalField0149.getWindow().getScaledHeight();
            float localValue4 = localValue1.x - localValue2 / 2.0F;
            float localValue5 = localValue1.y - localValue3 / 2.0F;
            float localValue6 = (float)Math.sqrt(localValue2 * localValue2 + localValue3 * localValue3) / 12.0F;
            return 90.0F + 165.0F * Math.min((float)Math.sqrt(localValue4 * localValue4 + localValue5 * localValue5) / localValue6, 1.0F);
         } else {
            return 255.0F;
         }
      }
   }
}
