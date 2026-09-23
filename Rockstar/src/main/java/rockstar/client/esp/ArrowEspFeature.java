package rockstar.client.esp;








import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.render.*;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

public class ArrowEspFeature extends EspFeature {
   private static final Identifier internalField0354 = RockstarClient.id("textures/arrow.png");
   private static final float internalField0205 = 35.0F;
   private static PlayerTargetType internalField0025;
   private static EntityTargetType internalField0027;
   private final BooleanSetting internalField0650 = this.internalMethod02236("esp.arrows");
   private final BooleanSetting internalField0651 = this.internalMethod02651(
      (localValue0, localValue1x) -> new BooleanSetting(localValue0, "esp.arrows.lines", () -> !localValue1x.internalMethod04496())
   );
   private final BooleanSetting internalField1261 = this.internalMethod02651(
      (localValue0, localValue1x) -> new BooleanSetting(localValue0, "theme.sync", () -> !localValue1x.internalMethod04496()).internalMethod06630()
   );
   private final ColorSetting internalField0665 = this.internalMethod04340(
      "theme.sync",
      (localValue0, localValue1x, localValue2x) -> new ColorSetting(localValue0, "esp.arrows.color", () -> !localValue1x.internalMethod04496() || localValue2x.internalMethod04496())
         .internalMethod04886(ThemeColors.internalMethod02531())
   );
   private final SliderSetting internalField0383 = this.internalMethod04340(
      "esp.arrows.lines",
      (localValue0, localValue1x, localValue2x) -> new SliderSetting(localValue0, "esp.arrows.distance", () -> !localValue1x.internalMethod04496() || localValue2x.internalMethod04496())
         .internalMethod08673(0.1F)
         .internalMethod05900(1.5F)
         .internalMethod02732(10.0F)
         .internalMethod08074(5.0F)
   );
   private final BooleanSetting internalField1263 = this.internalMethod04340(
      "esp.arrows.lines",
      (localValue0, localValue1x, localValue2x) -> new BooleanSetting(
         localValue0, "esp.arrows.hide_on_screen", "esp.arrows.hide_on_screen.desc", () -> !localValue1x.internalMethod04496() || localValue2x.internalMethod04496()
      )
   );
   private BooleanSetting internalField1262;
   private final Map<Integer, ArrowEspFeature.InternalType0081> internalField0543 = new HashMap<>();
   private final EventListener<PreHudRenderEvent> internalField0157 = localValue1x -> {
      if (internalField0149.player != null && internalField0149.world != null && this.internalMethod06968()) {
         boolean localValue2x = false;

         for (Entity localValue4x : internalField0149.world.getEntities()) {
            if (this.internalMethod04296(localValue4x) && !this.internalMethod02111(localValue4x)) {
               localValue2x = true;
               break;
            }
         }

         if (!localValue2x) {
            this.internalMethod07261(localValue1x.getTickDelta());
         } else {
            for (Entity localValue12 : internalField0149.world.getEntities()) {
               if (this.internalMethod04296(localValue12) && !this.internalMethod02111(localValue12)) {
                  boolean localValue5 = this.internalMethod08195(localValue12) && this.internalMethod00209(localValue12, localValue1x.getTickDelta());
                  int localValue6 = localValue12.getId();
                  ArrowEspFeature.InternalType0081 localValue7 = this.internalField0543.get(localValue6);
                  if (localValue7 == null) {
                     if (localValue5) {
                        continue;
                     }

                     localValue7 = new ArrowEspFeature.InternalType0081();
                     this.internalField0543.put(localValue6, localValue7);
                  }

                  localValue7.internalField0277 = localValue5;
                  localValue7.internalField0205 = this.internalMethod00208(localValue12, localValue1x.getTickDelta());
                  localValue7.internalField0777 = this.internalMethod02852(localValue12);
                  localValue7.internalField0206 = this.internalMethod04295(localValue12);
               }
            }

            this.internalMethod07261(localValue1x.getTickDelta());
            CustomDrawContext localValue11 = localValue1x.getContext();
            org.joml.Matrix3x2fStack localValue13 = localValue11.getMatrices();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            RenderSystem.disableCull();
            localValue13.pushMatrix();
            localValue13.translate(internalField0389.internalMethod03585() / 2.0F, internalField0389.internalMethod03589() / 2.0F);
            TexturedQuadBatch localValue14 = new TexturedQuadBatch(VertexFormats.POSITION_TEXTURE_COLOR, localValue13);

            for (Entry localValue16 : this.internalField0543.entrySet()) {
               ArrowEspFeature.InternalType0081 localValue8 = (ArrowEspFeature.InternalType0081)localValue16.getValue();
               float localValue9 = localValue8.internalField0808.internalMethod02881();
               if (!(localValue9 <= 0.0F)) {
                  HudRenderUtils.internalMethod02865(localValue13, 0.0F, 0.0F, localValue8.internalField0205);
                  HudRenderUtils.internalMethod08976(localValue13, 0.0F, 0.0F, 2.0F - localValue9);
                  localValue11.drawTexture(internalField0354, -17.5F, -17.5F + localValue8.internalField0206 * 10.0F, 35.0F, 35.0F, localValue8.internalField0777.mulAlpha(localValue9));
                  HudRenderUtils.internalMethod00012(localValue13);
                  HudRenderUtils.internalMethod00012(localValue13);
               }
            }

            localValue14.internalMethod09053();
            localValue13.popMatrix();
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
         }
      }
   };
   private final EventListener<Render3DEvent> internalField0158 = localValue1x -> {
      if (internalField0149.player != null && internalField0149.world != null && this.internalMethod06968()) {
         boolean localValue2x = false;

         for (Entity localValue4x : internalField0149.world.getEntities()) {
            if (this.internalMethod04296(localValue4x) && this.internalMethod02111(localValue4x)) {
               localValue2x = true;
               break;
            }
         }

         if (localValue2x) {
            MatrixStack localValue11 = localValue1x.getMatrices();
            HudRenderUtils.internalMethod02691(false);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder localValue12 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

            for (Entity localValue6 : internalField0149.world.getEntities()) {
               if (this.internalMethod04296(localValue6) && this.internalMethod02111(localValue6)) {
                  Vec3d localValue7 = ScreenProjectionUtils.internalMethod02822(localValue6, localValue1x.getTickDelta());
                  ColorRGBA localValue8 = this.internalMethod02852(localValue6);
                  float localValue9 = localValue6 instanceof LivingEntity localValue10 ? localValue10.getHeight() / 2.0F : 0.25F;
                  Render3DUtils.internalMethod01606(localValue11, localValue12, localValue7.add(0.0, localValue9, 0.0), localValue8);
               }
            }

            HudRenderUtils.internalMethod05816(localValue12);
            HudRenderUtils.internalMethod04670();
         }
      }
   };

   public ArrowEspFeature() {
      super(
         "arrows",
         EntityTargetType.internalField0027,
         EntityTargetType.internalField0028,
         EntityTargetType.internalField0963,
         EntityTargetType.internalField0964
      );
      this.internalMethod02197(
         new PlayerTargetType[]{PlayerTargetType.internalField0025, PlayerTargetType.internalField0961, PlayerTargetType.internalField0962}
      );
      BooleanSetting localValue1 = this.internalMethod06156(PlayerTargetType.internalField0025);
      this.internalField1262 = new BooleanSetting(this, "esp.arrows.hide_naked", () -> !localValue1.internalMethod04496());
      this.internalMethod09589()
         .computeIfAbsent("esp.arrows.hide_naked", localValue0 -> new HashMap<>())
         .put(PlayerTargetType.internalField0025, this.internalField1262);
      BooleanSetting localValue2 = this.internalMethod02672("theme.sync", PlayerTargetType.internalField0961);
      if (localValue2 != null) {
         localValue2.internalMethod02034(false);
      }

      BooleanSetting localValue3 = this.internalMethod02672("theme.sync", PlayerTargetType.internalField0962);
      if (localValue3 != null) {
         localValue3.internalMethod02034(false);
      }

      ColorSetting localValue4 = this.internalMethod02672("esp.arrows.color", PlayerTargetType.internalField0961);
      if (localValue4 != null) {
         localValue4.internalMethod04886(new ColorRGBA(52.0F, 199.0F, 88.0F));
      }
   }

   @Override
   public boolean internalMethod05541(PlayerTargetType localValue1) {
      return localValue1 != PlayerTargetType.internalField0026;
   }

   public boolean internalMethod04296(Entity localValue1) {
      if (localValue1 instanceof PlayerEntity localValue2) {
         if (localValue2 == internalField0149.player) {
            return false;
         } else if (RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue2.getName().getString())) {
            return this.internalMethod06170(PlayerTargetType.internalField0961);
         } else {
            return !this.internalMethod06170(PlayerTargetType.internalField0025)
               ? false
               : !this.internalField1262.internalMethod04496() || !this.internalMethod01158(localValue2);
         }
      } else if (localValue1 instanceof HostileEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0028);
      } else if (localValue1 instanceof AnimalEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0963);
      } else {
         return localValue1 instanceof ItemEntity ? this.internalMethod06206(EntityTargetType.internalField0964) : false;
      }
   }

   private boolean internalMethod01158(PlayerEntity localValue1) {
      for (ItemStack localValue3 : rockstar.client.util.LegacyItemTypes.armorItems(localValue1)) {
         if (localValue3 != null && !localValue3.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private PlayerTargetType internalMethod04297(PlayerEntity localValue1) {
      if (localValue1 == internalField0149.player) {
         return PlayerTargetType.internalField0026;
      } else {
         return RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue1.getName().getString())
            ? PlayerTargetType.internalField0961
            : PlayerTargetType.internalField0025;
      }
   }

   private ColorRGBA internalMethod02852(Entity localValue1) {
      BooleanSetting localValue2;
      ColorSetting localValue3;
      if (localValue1 instanceof PlayerEntity localValue4) {
         PlayerTargetType localValue5 = internalField0025 != null ? internalField0025 : this.internalMethod04297(localValue4);
         localValue2 = this.internalMethod02672("theme.sync", localValue5);
         localValue3 = this.internalMethod02672("esp.arrows.color", localValue5);
      } else if (localValue1 instanceof HostileEntity) {
         EntityTargetType localValue6 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0028;
         localValue2 = this.internalMethod05940("theme.sync", localValue6);
         localValue3 = this.internalMethod05940("esp.arrows.color", localValue6);
      } else if (localValue1 instanceof AnimalEntity) {
         EntityTargetType localValue7 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0963;
         localValue2 = this.internalMethod05940("theme.sync", localValue7);
         localValue3 = this.internalMethod05940("esp.arrows.color", localValue7);
      } else {
         if (!(localValue1 instanceof ItemEntity)) {
            return ThemeColors.internalMethod02531();
         }

         EntityTargetType localValue8 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0964;
         localValue2 = this.internalMethod05940("theme.sync", localValue8);
         localValue3 = this.internalMethod05940("esp.arrows.color", localValue8);
      }

      return localValue2 != null && localValue2.internalMethod04496()
         ? ThemeColors.internalMethod02531()
         : (localValue3 != null ? localValue3.internalMethod05620() : ThemeColors.internalMethod02531());
   }

   private float internalMethod04295(Entity localValue1) {
      SliderSetting localValue2;
      if (localValue1 instanceof PlayerEntity localValue3) {
         PlayerTargetType localValue4 = internalField0025 != null ? internalField0025 : this.internalMethod04297(localValue3);
         localValue2 = this.internalMethod02672("esp.arrows.distance", localValue4);
      } else if (localValue1 instanceof HostileEntity) {
         EntityTargetType localValue5 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0028;
         localValue2 = this.internalMethod05940("esp.arrows.distance", localValue5);
      } else if (localValue1 instanceof AnimalEntity) {
         EntityTargetType localValue6 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0963;
         localValue2 = this.internalMethod05940("esp.arrows.distance", localValue6);
      } else {
         if (!(localValue1 instanceof ItemEntity)) {
            return 3.3F;
         }

         EntityTargetType localValue7 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0964;
         localValue2 = this.internalMethod05940("esp.arrows.distance", localValue7);
      }

      return localValue2 != null ? localValue2.internalMethod08576() : 3.3F;
   }

   private boolean internalMethod02111(Entity localValue1) {
      BooleanSetting localValue2;
      if (localValue1 instanceof PlayerEntity localValue3) {
         PlayerTargetType localValue4 = internalField0025 != null ? internalField0025 : this.internalMethod04297(localValue3);
         localValue2 = this.internalMethod02672("esp.arrows.lines", localValue4);
      } else if (localValue1 instanceof HostileEntity) {
         EntityTargetType localValue5 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0028;
         localValue2 = this.internalMethod05940("esp.arrows.lines", localValue5);
      } else if (localValue1 instanceof AnimalEntity) {
         EntityTargetType localValue6 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0963;
         localValue2 = this.internalMethod05940("esp.arrows.lines", localValue6);
      } else {
         if (!(localValue1 instanceof ItemEntity)) {
            return false;
         }

         EntityTargetType localValue7 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0964;
         localValue2 = this.internalMethod05940("esp.arrows.lines", localValue7);
      }

      return localValue2 != null && localValue2.internalMethod04496();
   }

   private boolean internalMethod08195(Entity localValue1) {
      BooleanSetting localValue2;
      if (localValue1 instanceof PlayerEntity localValue3) {
         PlayerTargetType localValue4 = internalField0025 != null ? internalField0025 : this.internalMethod04297(localValue3);
         localValue2 = this.internalMethod02672("esp.arrows.hide_on_screen", localValue4);
      } else if (localValue1 instanceof HostileEntity) {
         EntityTargetType localValue5 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0028;
         localValue2 = this.internalMethod05940("esp.arrows.hide_on_screen", localValue5);
      } else if (localValue1 instanceof AnimalEntity) {
         EntityTargetType localValue6 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0963;
         localValue2 = this.internalMethod05940("esp.arrows.hide_on_screen", localValue6);
      } else {
         if (!(localValue1 instanceof ItemEntity)) {
            return false;
         }

         EntityTargetType localValue7 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0964;
         localValue2 = this.internalMethod05940("esp.arrows.hide_on_screen", localValue7);
      }

      return localValue2 != null && localValue2.internalMethod04496();
   }

   private boolean internalMethod00209(Entity localValue1, float localValue2) {
      Vec3d localValue3 = ScreenProjectionUtils.internalMethod02822(localValue1, localValue2);
      float localValue4 = localValue1.getWidth() / 2.0F;
      float localValue5 = localValue1.getHeight();
      float localValue6 = Float.MAX_VALUE;
      float localValue7 = Float.MAX_VALUE;
      float localValue8 = -Float.MAX_VALUE;
      float localValue9 = -Float.MAX_VALUE;
      boolean localValue10 = false;

      for (int localValue11 = 0; localValue11 < 8; localValue11++) {
         Vec2f localValue12 = ScreenProjectionUtils.internalMethod00612(
            localValue3.add((localValue11 & 1) == 0 ? -localValue4 : localValue4, (localValue11 & 2) == 0 ? 0.0 : localValue5, (localValue11 & 4) == 0 ? -localValue4 : localValue4)
         );
         if (localValue12 != null) {
            localValue10 = true;
            localValue6 = Math.min(localValue6, localValue12.x);
            localValue8 = Math.max(localValue8, localValue12.x);
            localValue7 = Math.min(localValue7, localValue12.y);
            localValue9 = Math.max(localValue9, localValue12.y);
         }
      }

      return !localValue10 ? false : localValue8 >= 0.0F && localValue6 <= internalField0389.internalMethod03585() && localValue9 >= 0.0F && localValue7 <= internalField0389.internalMethod03589();
   }

   private void internalMethod07261(float localValue1) {
      Iterator localValue2 = this.internalField0543.entrySet().iterator();

      while (localValue2.hasNext()) {
         Entry localValue3 = (Entry)localValue2.next();
         int localValue4 = (Integer)localValue3.getKey();
         ArrowEspFeature.InternalType0081 localValue5 = (ArrowEspFeature.InternalType0081)localValue3.getValue();
         Entity localValue6 = internalField0149.world.getEntityById(localValue4);
         boolean localValue7 = localValue6 != null && this.internalMethod04296(localValue6) && !this.internalMethod02111(localValue6) && !localValue5.internalField0277;
         localValue5.internalField0808.internalMethod07061(500L);
         localValue5.internalField0808.internalMethod07062(localValue7);
         if (!localValue7 && localValue5.internalField0808.internalMethod02881() == 0.0F) {
            localValue2.remove();
         }
      }
   }

   private float internalMethod00208(Entity localValue1, float localValue2) {
      Vec3d localValue3 = ScreenProjectionUtils.internalMethod02822(localValue1, localValue2);
      Vec3d localValue4 = internalField0149.gameRenderer.getCamera().getCameraPos();
      double localValue5 = localValue3.x - localValue4.x;
      double localValue7 = localValue3.z - localValue4.z;
      float localValue9 = internalField0149.gameRenderer.getCamera().getYaw();
      double localValue10 = Math.toDegrees(Math.atan2(localValue7, localValue5));
      return (float)(localValue10 - (localValue9 - 90.0F));
   }

   @Generated
   public BooleanSetting internalMethod00211() {
      return this.internalField0650;
   }

   @Generated
   public BooleanSetting internalMethod00936() {
      return this.internalField0651;
   }

   @Generated
   public BooleanSetting internalMethod08065() {
      return this.internalField1261;
   }

   @Generated
   public ColorSetting internalMethod00278() {
      return this.internalField0665;
   }

   @Generated
   public SliderSetting internalMethod06100() {
      return this.internalField0383;
   }

   @Generated
   public BooleanSetting internalMethod08201() {
      return this.internalField1263;
   }

   @Generated
   public BooleanSetting internalMethod09028() {
      return this.internalField1262;
   }

   @Generated
   public Map<Integer, ArrowEspFeature.InternalType0081> internalMethod09548() {
      return this.internalField0543;
   }

   @Generated
   public EventListener<PreHudRenderEvent> internalMethod02906() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<Render3DEvent> internalMethod01114() {
      return this.internalField0158;
   }

   @Generated
   public static void internalMethod06486(PlayerTargetType localValue0) {
      internalField0025 = localValue0;
   }

   @Generated
   public static PlayerTargetType internalMethod06506() {
      return internalField0025;
   }

   @Generated
   public static void internalMethod06563(EntityTargetType localValue0) {
      internalField0027 = localValue0;
   }

   @Generated
   public static EntityTargetType internalMethod06507() {
      return internalField0027;
   }

   static class InternalType0081 {
      AnimatedValue internalField0808 = new AnimatedValue(300L, Easing.internalField0812);
      boolean internalField0277;
      float internalField0205;
      ColorRGBA internalField0777 = ThemeColors.internalMethod02531();
      float internalField0206 = 3.3F;
   }
}
