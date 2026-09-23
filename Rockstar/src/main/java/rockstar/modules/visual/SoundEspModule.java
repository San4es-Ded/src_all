package rockstar.modules.visual;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.SoundEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.utility.render.CustomDrawContext;

@ModuleInfo(
   name = "Sound ESP",
   category = ModuleCategory.VISUALS,
   internalMethod08049 = true,
   internalMethod09633 = "modules.descriptions.sound_esp"
)
public class SoundEspModule extends Module {
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private SizedFont internalField0447 = null;
   private final List<SoundEspModule.InternalType0443> internalField0416 = new ArrayList<>();
   private final EventListener<SoundEvent> internalField0157 = localValue1 -> {
      SoundInstance localValue2 = localValue1.getSound();
      if (this.internalMethod04504(localValue2)) {
         SoundEspModule.InternalType0443 localValue3 = new SoundEspModule.InternalType0443(localValue1.getSound());
         this.internalField0416.add(localValue3);
      }
   };
   private final EventListener<PreHudRenderEvent> internalField0158 = localValue1 -> {
      if (this.internalField0447 == null) {
         this.internalField0447 = Fonts.internalField0449.internalMethod01432(12.0F);
      }

      this.internalField0416.removeIf(SoundEspModule.InternalType0443::internalMethod01664);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      ColoredQuadBatch localValue2 = new ColoredQuadBatch(VertexFormats.POSITION_COLOR, localValue1.getContext().getMatrices());

      for (SoundEspModule.InternalType0443 localValue4 : this.internalField0416) {
         localValue4.internalMethod07053(localValue1.getContext(), this.internalField0447, SoundEspModule.InternalType0442.internalField0898);
      }

      localValue2.internalMethod09053();

      for (SoundEspModule.InternalType0443 localValue8 : this.internalField0416) {
         localValue8.internalMethod07053(localValue1.getContext(), this.internalField0447, SoundEspModule.InternalType0442.internalField0897);
      }

      PlainGlyphBatch localValue7 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, this.internalField0447.internalMethod01335());

      for (SoundEspModule.InternalType0443 localValue5 : this.internalField0416) {
         localValue5.internalMethod07053(localValue1.getContext(), this.internalField0447, SoundEspModule.InternalType0442.internalField1368);
      }

      localValue7.internalMethod09053();
      RenderSystem.disableBlend();
   };

   public SoundEspModule() {
      this.internalMethod09189();
   }

   private void internalMethod09189() {
      this.internalField0675 = new MultiSelectSetting(this, "modules.settings.sound_esp.select");
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.sound_esp.select.trident").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.sound_esp.select.tnt");
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.sound_esp.select.fireworks").select();
   }

   private boolean internalMethod04504(SoundInstance localValue1) {
      String localValue2 = localValue1.getId().toString().toLowerCase();
      if (localValue2.equals("minecraft:entity.generic.explode") && this.internalField0244.isSelected()) {
         return true;
      } else {
         return (localValue2.equals("minecraft:item.trident.throw") || localValue2.equals("minecraft:item.trident.return")) && this.internalField0245.isSelected()
            ? true
            : localValue2.equals("minecraft:entity.firework_rocket.launch") && this.internalField1075.isSelected();
      }
   }

   static enum InternalType0442 {
      internalField0898,
      internalField0897,
      internalField1368;
   }

   static class InternalType0443 {
      private final SoundInstance internalField0790;
      private final Vec3d internalField0283;
      private final Stopwatch internalField0519;

      InternalType0443(SoundInstance localValue1) {
         this.internalField0790 = localValue1;
         this.internalField0283 = new Vec3d(localValue1.getX(), localValue1.getY(), localValue1.getZ());
         this.internalField0519 = new Stopwatch();
      }

      void internalMethod07053(CustomDrawContext localValue1, SizedFont localValue2, SoundEspModule.InternalType0442 localValue3) {
         if (MinecraftClientAccess.internalField0149.player != null) {
            Vec3d localValue4 = this.internalField0283.add(0.0, 0.5, 0.0);
            float localValue5 = (float)MinecraftClientAccess.internalField0149.player.getEntityPos().distanceTo(this.internalField0283);
            WeightedSoundSet localValue6 = this.internalField0790.getSoundSet(MinecraftClientAccess.internalField0149.getSoundManager());
            if (localValue6 != null && localValue6.getSubtitle() != null) {
               MutableText localValue7 = localValue6.getSubtitle().copy().append(" (").append(String.format("%.0f", localValue5) + "m").append(")");
               Vec2f localValue8 = ScreenProjectionUtils.internalMethod00612(localValue4);
               if (localValue8 != null) {
                  float localValue9 = MathHelper.clamp(1.0F - localValue5 / 20.0F, 0.5F, 1.0F);
                  float localValue10 = this.internalMethod00652(localValue8) / 255.0F;
                  Item localValue11 = this.internalMethod06626();
                  float localValue12 = localValue11 != null ? 18.0F : 0.0F;
                  org.joml.Matrix3x2fStack localValue13 = localValue1.getMatrices();
                  localValue13.pushMatrix();
                  localValue13.translate(localValue8.x, localValue8.y);
                  localValue13.scale(localValue9, localValue9);
                  float localValue14 = localValue2.internalMethod06412(localValue7);
                  float localValue15 = localValue14 + localValue12;
                  float localValue16 = -localValue15 / 2.0F;
                  switch (localValue3) {
                     case internalField0898:
                        float localValue17 = localValue2.internalMethod04890();
                        float localValue18 = 4.0F;
                        float localValue19 = 2.0F;
                        localValue1.drawRect(
                           localValue16 - localValue19,
                           -localValue18,
                           localValue15 + localValue19 * 2.0F,
                           localValue17 + localValue18 * 2.0F,
                           ThemeColors.internalField1309.mulAlpha(0.5F).mulAlpha(localValue10)
                        );
                        break;
                     case internalField0897:
                        if (localValue11 != null) {
                           RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue10);
                           MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_FLAT);
                           localValue1.drawItem(new ItemStack(localValue11), (int)localValue16, -3);
                           MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_3D);
                           RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        }
                        break;
                     case internalField1368:
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue10);
                        localValue1.drawText(localValue2, localValue7, (int)(localValue16 + localValue12), 0.0F);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                  }

                  localValue13.popMatrix();
               }
            }
         }
      }

      private Item internalMethod06626() {
         String localValue1 = this.internalField0790.getId().toString().toLowerCase();

         return switch (localValue1) {
            case "minecraft:entity.generic.explode" -> Items.TNT;
            case "minecraft:item.trident.throw", "minecraft:item.trident.return" -> Items.TRIDENT;
            case "minecraft:entity.firework_rocket.launch" -> Items.FIREWORK_ROCKET;
            default -> null;
         };
      }

      public boolean internalMethod01664() {
         return this.internalField0519.internalMethod02365(5000L);
      }

      private float internalMethod00652(Vec2f localValue1) {
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
