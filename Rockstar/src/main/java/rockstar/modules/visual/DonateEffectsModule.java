package rockstar.modules.visual;









import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import moscow.rockstar.mixin.accessors.AbstractSoundInstanceAccessor;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.SoundEvent;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Donate Effects",
   category = ModuleCategory.VISUALS,
   internalMethod08049 = true,
   internalMethod09633 = "modules.descriptions.donate_effects"
)
public class DonateEffectsModule extends Module {
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private MultiSelectSetting.InternalType0091 internalField1074;
   private MultiSelectSetting.InternalType0091 internalField1073;
   private MultiSelectSetting.InternalType0091 internalField1072;
   private MultiSelectSetting.InternalType0091 internalField1491;
   private final Map<Integer, DonateEffectsModule.InternalType0123> internalField0543 = new HashMap<>();
   private final List<DonateEffectsModule.InternalType0304> internalField0416 = new CopyOnWriteArrayList<>();
   private static final long internalField0229 = 1000L;
   private static final double internalField0194 = 4.0;
   private final Map<Integer, DonateEffectsModule.InternalType0305> internalField0544 = new ConcurrentHashMap<>();
   private static final Map<String, Identifier> internalField1197 = Map.of(
      "potion-radiation",
      RockstarClient.id("icons/potions/radio.png"),
      "potion-paladin",
      RockstarClient.id("icons/potions/shield.png"),
      "potion-assassin",
      RockstarClient.id("icons/potions/sword.png"),
      "potion-holy-water",
      RockstarClient.id("icons/potions/holy.png"),
      "potion-popper",
      RockstarClient.id("icons/potions/bomb.png"),
      "potion-drowsiness",
      RockstarClient.id("icons/potions/moon.png"),
      "potion-rage",
      RockstarClient.id("icons/potions/angry.png")
   );
   private final EventListener<ReceivePacketEvent> internalField0157 = localValue1 -> {
      if (this.internalField0650.internalMethod04496() || this.internalField0651.internalMethod04496()) {
         if (localValue1.getPacket() instanceof ExplosionS2CPacket localValue2 && this.internalMethod06524(localValue2.center())) {
            this.internalMethod01459(this.internalField1072.isSelected(), localValue2.center(), 8.0F, 3.0F, 2.0F, new ColorRGBA(255.0F, 155.0F, 0.0F));
         }

         if (localValue1.getPacket() instanceof PlaySoundS2CPacket localValue4) {
            String localValue6 = localValue4.getSound().getIdAsString();
            if (ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
               if (localValue6.contains("minecraft:entity.illusioner.mirror_move") && (localValue4.getVolume() == 1.0F || localValue4.getPitch() == 0.0F)) {
                  this.internalMethod02145(this.internalField0245.isSelected(), internalMethod02094(localValue4), 20.0F, 1.5F, 2.0F);
               }

               if ((localValue6.contains("minecraft:entity.wither.break_block") || localValue6.contains("minecraft:block.piston.extend")) && localValue4.getVolume() == 0.7F
                  || localValue4.getVolume() == 0.2F && localValue4.getPitch() == 1.0F
                  || localValue4.getPitch() == 0.5) {
                  this.internalMethod01459(this.internalField1072.isSelected(), internalMethod02094(localValue4), 5.0F, 3.0F, 2.0F, new ColorRGBA(255.0F, 255.0F, 255.0F));
               }

               if (localValue6.contains("minecraft:entity.illusioner.cast_spell") && (localValue4.getVolume() == 1.0F || localValue4.getPitch() == 1.0F)) {
                  this.internalMethod01459(this.internalField0244.isSelected(), internalMethod02094(localValue4), 5.0F, 3.0F, 2.0F, new ColorRGBA(255.0F, 255.0F, 255.0F));
               }

               if (localValue6.contains("minecraft:entity.illusioner.prepare_blindness") && (localValue4.getVolume() == 1.0F || localValue4.getPitch() == 0.0F)) {
                  this.internalMethod01459(this.internalField1075.isSelected(), internalMethod02094(localValue4), 15.0F, 1.5F, 2.0F, new ColorRGBA(255.0F, 255.0F, 255.0F));
               }
            } else if (ServerUtils.internalMethod01786(KnownServer.internalField1567)) {
               if (localValue6.contains("minecraft:item.firecharge.use") && (localValue4.getVolume() == 0.5 || localValue4.getPitch() == 1.0F)) {
                  this.internalMethod01459(this.internalField1074.isSelected(), internalMethod02094(localValue4), 20.0F, 1.5F, 2.0F, new ColorRGBA(255.0F, 155.0F, 0.0F));
               }

               if (localValue6.contains("minecraft:block.beacon.activate") && (localValue4.getVolume() == 0.5 || localValue4.getPitch() == 1.0F)) {
                  this.internalMethod01459(this.internalField0244.isSelected(), internalMethod02094(localValue4), 5.0F, 3.0F, 2.0F, new ColorRGBA(255.0F, 255.0F, 255.0F));
               }

               if (localValue6.contains("minecraft:entity.illusioner.mirror_move") && (localValue4.getVolume() == 0.5 || localValue4.getPitch() == 1.0F)) {
                  this.internalMethod01459(this.internalField1075.isSelected(), internalMethod02094(localValue4), 15.0F, 1.5F, 2.0F, new ColorRGBA(255.0F, 255.0F, 255.0F));
               }

               if (localValue6.contains("minecraft:entity.illusioner.prepare_blindness") && (localValue4.getVolume() == 0.5 || localValue4.getPitch() == 1.0F)) {
                  this.internalMethod02145(this.internalField0245.isSelected(), internalMethod02094(localValue4), 20.0F, 1.5F, 2.0F);
               }
            } else if (ServerUtils.internalMethod08700()) {
               if (localValue6.contains("minecraft:entity.generic.explode") && (localValue4.getVolume() == 1.0F || localValue4.getPitch() == 1.0F)) {
                  this.internalMethod01459(this.internalField1072.isSelected(), internalMethod02094(localValue4), 8.0F, 3.0F, 2.0F, new ColorRGBA(255.0F, 155.0F, 0.0F));
               }

               if (localValue6.contains("minecraft:block.beacon.deactivate") && (localValue4.getVolume() == 1.5 || localValue4.getPitch() == 1.0F)) {
                  this.internalMethod01459(
                     this.internalField1491.isSelected(), internalMethod02094(localValue4), 25.0F, 1.5F, 2.0F, new ColorRGBA(255.0F, 255.0F, 255.0F)
                  );
               }
            }
         }
      }
   };
   private final EventListener<SoundEvent> internalField0158 = localValue1 -> {
      if (this.internalField0650.internalMethod04496() || this.internalField0651.internalMethod04496()) {
         if (ServerUtils.internalMethod08700()) {
            SoundInstance localValue2 = localValue1.getSound();
            if (localValue2 != null) {
               String localValue3 = localValue2.getId().toString();
               float localValue4 = ((AbstractSoundInstanceAccessor)(Object)localValue2).rockstar$getVolume();
               if (localValue3.equals("minecraft:entity.generic.explode") && localValue4 == 4.0F) {
                  BlockPos localValue5 = new BlockPos((int)localValue2.getX(), (int)localValue2.getY(), (int)localValue2.getZ());
                  this.internalMethod01459(this.internalField1073.isSelected(), localValue5.toCenterPos(), 4.0F, 1.5F, 2.0F, new ColorRGBA(255.0F, 155.0F, 0.0F));
               }
            }
         }
      }
   };
   private final EventListener<WorldChangeEvent> internalField1028 = localValue1 -> {
      this.internalField0543.clear();
      this.internalField0416.clear();
      this.internalField0544.clear();
   };
   private final EventListener<Render3DEvent> internalField1029 = localValue1 -> {
      if (!this.internalField0416.isEmpty()) {
         if (internalField0149 != null && internalField0149.world != null) {
            MatrixStack localValue2 = localValue1.getMatrices();
            localValue2.push();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            RenderSystem.enableDepthTest();
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);
            HashMap<Identifier, List<DonateEffectsModule.InternalType0304>> localValue3 = new HashMap<>();

            for (DonateEffectsModule.InternalType0304 localValue5 : this.internalField0416) {
               localValue3.computeIfAbsent(localValue5.internalField0354, localValue0 -> new ArrayList<>()).add(localValue5);
            }

            for (Entry localValue12 : (Iterable<Entry>)(Iterable<?>)localValue3.entrySet()) {
               Identifier localValue6 = (Identifier)localValue12.getKey();
               List localValue7 = (List)localValue12.getValue();
               RenderSystem.setShaderTexture(0, localValue6);
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               BufferBuilder localValue8 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

               for (DonateEffectsModule.InternalType0304 localValue10 : (Iterable<DonateEffectsModule.InternalType0304>)(Iterable<?>)localValue7) {
                  if (!localValue10.internalMethod00830()) {
                     localValue10.internalMethod00829();
                     localValue10.internalMethod03204(localValue1, localValue8);
                  }
               }

               BuiltBuffer localValue13 = localValue8.endNullable();
               if (localValue13 != null) {
                  BufferRenderer.drawWithGlobalProgram(localValue13);
               }
            }

            this.internalField0416.removeIf(DonateEffectsModule.InternalType0304::internalMethod00830);
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, 0);
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.disableDepthTest();
            localValue2.pop();
         }
      }
   };

   public DonateEffectsModule() {
      this.internalMethod09295();
      ShockwaveEffectManager.internalField0763.internalMethod03369();
   }

   private void internalMethod09295() {
      this.internalField0650 = new BooleanSetting(this, "modules.settings.donate_effects.sonar");
      this.internalField0651 = new BooleanSetting(this, "modules.settings.donate_effects.shockwave").internalMethod06630();
      this.internalField1261 = new BooleanSetting(this, "modules.settings.donate_effects.potions").internalMethod06630();
      this.internalField0675 = new MultiSelectSetting(
         this, "modules.settings.donate_effects.targets", () -> !this.internalField0650.internalMethod04496() && !this.internalField0651.internalMethod04496()
      );
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.donate_effects.targets.dez").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.donate_effects.targets.aura").select();
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.donate_effects.targets.pil").select();
      this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.donate_effects.targets.fire").select();
      this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.donate_effects.targets.boom").select();
      this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.donate_effects.targets.trapka").select();
      this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.donate_effects.targets.stun").select();
   }

   private boolean internalMethod06524(Vec3d localValue1) {
      long localValue2 = System.currentTimeMillis();

      for (DonateEffectsModule.InternalType0305 localValue5 : this.internalField0544.values()) {
         if (localValue5.internalMethod02543() > localValue2 && localValue5.internalMethod05723().squaredDistanceTo(localValue1) <= 16.0) {
            return true;
         }
      }

      return false;
   }

   private void internalMethod09296() {
      if (internalField0149.world == null) {
         if (!this.internalField0544.isEmpty()) {
            this.internalField0544.clear();
         }
      } else {
         long localValue1 = System.currentTimeMillis();

         for (Entity localValue4 : internalField0149.world.getEntities()) {
            if (localValue4 instanceof TntEntity localValue5) {
               this.internalField0544.put(localValue5.getId(), new DonateEffectsModule.InternalType0305(localValue5.getEntityPos(), localValue1 + 1000L));
            }
         }

         this.internalField0544.values().removeIf(localValue2 -> localValue2.internalMethod02543() <= localValue1);
      }
   }

   private static Vec3d internalMethod02094(PlaySoundS2CPacket localValue0) {
      return new BlockPos((int)localValue0.getX(), (int)localValue0.getY(), (int)localValue0.getZ()).toCenterPos();
   }

   private void internalMethod02145(boolean localValue1, Vec3d localValue2, float localValue3, float localValue4, float localValue5) {
      this.internalMethod01459(localValue1, localValue2, localValue3, localValue4, localValue5, ThemeColors.internalMethod02531());
   }

   private void internalMethod01459(boolean localValue1, Vec3d localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6) {
      if (this.internalField0650.internalMethod04496() && localValue1) {
         BlockScanEffectRenderer.internalField0186.internalMethod04515(localValue2, localValue3, localValue4, localValue5, localValue6);
      }

      if (this.internalField0651.internalMethod04496()) {
         ShockwaveEffectManager.internalField0763.internalMethod04641(localValue2, 8.0F, 1.0F, localValue6, 0.5F);
      }
   }

   @Override
   public void internalMethod08229() {
      super.internalMethod08229();
      this.internalMethod09296();
      if (!this.internalField1261.internalMethod04496()) {
         if (!this.internalField0543.isEmpty()) {
            this.internalField0543.clear();
         }
      } else if (internalField0149 != null && internalField0149.player != null && internalField0149.world != null) {
         HashSet localValue1 = new HashSet();

         for (Entity localValue3 : internalField0149.world.getEntities()) {
            if (localValue3 instanceof PotionEntity localValue4) {
               int localValue5 = localValue4.getId();
               localValue1.add(localValue5);
               DonateEffectsModule.InternalType0123 localValue6 = this.internalField0543.get(localValue5);
               Vec3d localValue7 = localValue4.getEntityPos();
               if (localValue6 != null) {
                  this.internalField0543.put(localValue5, new DonateEffectsModule.InternalType0123(localValue7, localValue6.internalField0878));
               } else {
                  this.internalField0543.put(localValue5, new DonateEffectsModule.InternalType0123(localValue7, localValue4.getStack().copy()));
               }
            }
         }

         Iterator localValue23 = this.internalField0543.entrySet().iterator();

         while (localValue23.hasNext()) {
            Entry localValue24 = (Entry)localValue23.next();
            int localValue25 = (Integer)localValue24.getKey();
            if (!localValue1.contains(localValue25)) {
               DonateEffectsModule.InternalType0123 localValue26 = (DonateEffectsModule.InternalType0123)localValue24.getValue();
               Vec3d localValue27 = localValue26.internalField0283;
               String localValue28 = CustomItemUtils.internalMethod05508(localValue26.internalField0878);
               if (localValue28 == null) {
                  localValue23.remove();
               } else {
                  Identifier localValue8 = internalField1197.getOrDefault(localValue28, RockstarClient.id("icons/add.png"));

                  ColorRGBA localValue9 = switch (localValue28) {
                     case "potion-radiation" -> new ColorRGBA(99.0F, 255.0F, 0.0F);
                     case "potion-paladin" -> new ColorRGBA(74.0F, 180.0F, 255.0F);
                     case "potion-assassin" -> new ColorRGBA(255.0F, 68.0F, 68.0F);
                     case "potion-holy-water" -> new ColorRGBA(255.0F, 255.0F, 255.0F);
                     case "potion-popper" -> new ColorRGBA(255.0F, 170.0F, 0.0F);
                     case "potion-drowsiness" -> new ColorRGBA(136.0F, 85.0F, 255.0F);
                     case "potion-rage" -> new ColorRGBA(255.0F, 69.0F, 0.0F);
                     default -> ThemeColors.internalMethod02531();
                  };

                  for (int localValue10 = 0; localValue10 < 20; localValue10++) {
                     double localValue29 = 1.5;
                     double localValue13 = Math.random() * Math.PI * 2.0;
                     double localValue15 = Math.sqrt(Math.random()) * localValue29;
                     double localValue17 = Math.cos(localValue13) * localValue15;
                     double localValue19 = Math.sin(localValue13) * localValue15;
                     Vec3d localValue21 = localValue27.add(localValue17, 0.0, localValue19);
                     boolean localValue22 = "potion-popper".equals(localValue28) || "potion-paladin".equals(localValue28);
                     this.internalField0416.add(new DonateEffectsModule.InternalType0304(localValue21, localValue9, localValue8, localValue22));
                  }

                  localValue23.remove();
               }
            }
         }
      }
   }

   static final class InternalType0123 {
      final Vec3d internalField0283;
      final ItemStack internalField0878;

      InternalType0123(Vec3d localValue1, ItemStack localValue2) {
         this.internalField0283 = localValue1;
         this.internalField0878 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0123[pos=" + this.internalField0283 + ", stack=" + this.internalField0878 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         DonateEffectsModule.InternalType0123 other = (DonateEffectsModule.InternalType0123) localValue1;
         return java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0878, other.internalField0878);
      }

      public Vec3d internalMethod06417() {
         return this.internalField0283;
      }

      public ItemStack internalMethod01666() {
         return this.internalField0878;
      }
   }

   static class InternalType0304 {
      double internalField0194;
      double internalField0193;
      double internalField1045;
      double internalField1043;
      double internalField1042;
      double internalField1044;
      long internalField0229;
      long internalField0230;
      ColorRGBA internalField0777;
      Identifier internalField0354;
      boolean internalField0277;

      InternalType0304(Vec3d localValue1, ColorRGBA localValue2, Identifier localValue3, boolean localValue4) {
         this.internalField0194 = localValue1.x;
         this.internalField0193 = localValue1.y;
         this.internalField1045 = localValue1.z;
         this.internalField0777 = localValue2;
         this.internalField0354 = localValue3;
         this.internalField0277 = localValue4;
         this.internalField1043 = 0.0;
         this.internalField1042 = 0.003 + Math.random() * 0.003;
         this.internalField1044 = 0.0;
         this.internalField0229 = System.currentTimeMillis();
         this.internalField0230 = 800L;
      }

      boolean internalMethod00830() {
         return System.currentTimeMillis() - this.internalField0229 > this.internalField0230;
      }

      void internalMethod00829() {
         long localValue1 = System.currentTimeMillis();
         long localValue3 = localValue1 - this.internalField0229;
         if (localValue3 >= 0L) {
            this.internalField0194 = this.internalField0194 + this.internalField1043;
            this.internalField0193 = this.internalField0193 + this.internalField1042;
            this.internalField1045 = this.internalField1045 + this.internalField1044;
         }
      }

      float internalMethod00828() {
         long localValue1 = System.currentTimeMillis() - this.internalField0229;
         float localValue3 = MathHelper.clamp((float)localValue1 / (float)this.internalField0230, 0.0F, 1.0F);
         return 1.0F - localValue3;
      }

      void internalMethod03204(Render3DEvent localValue1, BufferBuilder localValue2) {
         MatrixStack localValue3 = localValue1.getMatrices();
         Camera localValue4 = MinecraftClientAccess.internalField0149.gameRenderer.getCamera();
         float localValue5 = 0.15F;
         float localValue6 = this.internalMethod00828();
         localValue3.push();
         HudRenderUtils.internalMethod03474(localValue3, new Vec3d(this.internalField0194, this.internalField0193, this.internalField1045));
         localValue3.multiply(localValue4.getRotation());
         int localValue7 = this.internalField0277 ? 180 : 0;
         ColorRGBA localValue8 = this.internalField0777.mulAlpha(0.9F * localValue6);
         RenderPipeline.internalMethod04257(localValue3, localValue2, -localValue5 / 2.0F, -localValue5 / 2.0F, 0.0, localValue5, localValue5, localValue8, localValue7);
         localValue3.pop();
      }
   }

   static final class InternalType0305 {
      private final Vec3d internalField0283;
      private final long internalField0229;

      InternalType0305(Vec3d localValue1, long localValue2) {
         this.internalField0283 = localValue1;
         this.internalField0229 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0305[pos=" + this.internalField0283 + ", expireAt=" + this.internalField0229 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         DonateEffectsModule.InternalType0305 other = (DonateEffectsModule.InternalType0305) localValue1;
         return java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229);
      }

      public Vec3d internalMethod05723() {
         return this.internalField0283;
      }

      public long internalMethod02543() {
         return this.internalField0229;
      }
   }
}
