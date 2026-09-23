package rockstar.client.esp;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.game.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import globals.client.Information;
import globals.client.ui.CosmeticRender;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.render.ChatRenderEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.window.ChatClickEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

public class NametagEspFeature extends EspFeature {
   private static final int internalField0227 = 11;
   private final List<Entity> internalField0416 = new ArrayList<>();
   private final Map<Entity, Packets.InternalType0018> internalField0543 = new HashMap<>();
   private final Map<ItemEntity, List<NametagEspFeature.InternalType0173>> internalField0544 = new HashMap<>();
   private final Map<Entity, Text> internalField1197 = new HashMap<>();
   private final Map<Entity, Float> internalField1196 = new HashMap<>();
   private final Map<Entity, Float> internalField1195 = new HashMap<>();
   private final BooleanSetting internalField0650 = this.internalMethod02236("esp.nametags");
   private final BooleanSetting internalField0651 = this.internalMethod06816(localValue0 -> new BooleanSetting(localValue0, "esp.nametags.show_armor"));
   private final BooleanSetting internalField1261 = this.internalMethod06816(localValue0 -> new BooleanSetting(localValue0, "esp.nametags.show_item_use"));
   private final BooleanSetting internalField1263 = new BooleanSetting(
      this, "esp.nametags.background", () -> !this.internalMethod06206(EntityTargetType.internalField0964)
   );
   private final ItemIconAtlas internalField0856 = new ItemIconAtlas();
   private HudContextMenu internalField0574;
   private String internalField0248;
   private long internalField0229;
   private final EventListener<PreHudRenderEvent> internalField0157 = localValue1x -> {
      this.internalMethod08025();
      if (this.internalMethod06968()) {
         MatrixStack localValue2x = rockstar.client.render.GuiMatrixCompat.toLegacyStack(localValue1x.getContext().getMatrices());
         float localValue3x = localValue1x.getTickDelta();
         this.internalMethod00692();
         List localValue4 = this.internalMethod00032();
         TextBatchRenderer localValue5 = new TextBatchRenderer(Fonts.internalField0449, 5.0F);
         TextBatchRenderer localValue6 = new TextBatchRenderer(Fonts.internalField0449, 0.0F);
         this.internalMethod03203(localValue5, localValue6, localValue2x, localValue4, localValue3x);
         this.internalMethod04952(localValue5, localValue2x, localValue3x);
         this.internalMethod05811(localValue5, localValue2x, localValue4, localValue3x);
         this.internalField0856.internalMethod01277();
         localValue5.internalMethod00327(this.internalField0856.internalMethod01276(), 14.0F, 4.0F);
         localValue6.internalMethod03841();
         localValue5.internalMethod03841();
         this.internalMethod06425(localValue1x, localValue2x, localValue3x);
         this.internalMethod07470(localValue1x, localValue2x, localValue3x);
         this.internalMethod08166(localValue1x, localValue2x, localValue3x);

         for (Entity localValue8 : this.internalField0416) {
            Vec3d localValue9 = ScreenProjectionUtils.internalMethod02822(localValue8, localValue3x).add(0.0, localValue8.getBoundingBox().getLengthY() / 2.0, 0.0);
            Vec2f localValue10 = ScreenProjectionUtils.internalMethod00612(localValue9);
            if (localValue10 != null && localValue8.getType() == EntityType.PLAYER) {
               PlayerEntity localValue11 = (PlayerEntity)localValue8;
               BooleanSetting localValue12 = this.internalMethod02672("esp.nametags.show_item_use", this.internalMethod07193(localValue11));
               if (localValue12 != null && localValue12.internalMethod04496()) {
                  this.internalMethod01962(localValue1x, localValue2x, localValue11, localValue10);
               }
            }
         }

         MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_FLAT);
         this.internalMethod07863(localValue1x, localValue2x, localValue3x);
         if (!(internalField0149.currentScreen instanceof ChatScreen)) {
            this.internalField0574 = null;
         }
      }
   };
   private final EventListener<ChatRenderEvent> internalField0158 = localValue1x -> {
      if (this.internalMethod06968()) {
         UiRenderContext localValue2x = UiRenderContext.internalMethod02316(
            localValue1x.getContext(),
            internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().x(),
            internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().y(),
            MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false)
         );
         if (this.internalField0574 != null) {
            this.internalField0574.internalMethod03398(localValue2x);
         }
      }
   };
   private final EventListener<ChatClickEvent> internalField1028 = localValue1x -> {
      if (this.internalMethod06968()) {
         if (this.internalField0574 != null) {
            this.internalField0574.internalMethod01643(localValue1x.getX(), localValue1x.getY(), MouseButton.internalMethod01669(localValue1x.getButton()));
            if (this.internalField0574.internalMethod04933(localValue1x.getX(), localValue1x.getY())) {
               return;
            }

            this.internalField0574.internalMethod05781(false);
         }

         for (Entity localValue3x : this.internalField0416) {
            if (localValue3x.getType() == EntityType.PLAYER) {
               Vec2f localValue4 = this.internalMethod00076(localValue3x, 1.0F);
               if (localValue4 != null && this.internalMethod06064(localValue3x, localValue4, localValue1x.getX(), localValue1x.getY())) {
                  this.internalMethod06984(localValue1x.getX(), localValue1x.getY(), localValue3x);
                  return;
               }
            }
         }

         for (Entity localValue6 : this.internalField0543.keySet()) {
            Vec2f localValue7 = this.internalMethod00076(localValue6, 1.0F);
            if (localValue7 != null && this.internalMethod00135(localValue6, localValue7, localValue1x.getX(), localValue1x.getY())) {
               this.internalMethod06984(localValue1x.getX(), localValue1x.getY(), localValue6);
               return;
            }
         }
      }
   };
   private final EventListener<ReceivePacketEvent> internalField1029 = localValue1x -> {
      if (this.internalField0248 != null && localValue1x.getPacket() instanceof GameMessageS2CPacket localValue2x) {
         String localValue4 = localValue2x.content().getString().toLowerCase(Locale.ROOT);
         if (localValue4.contains("unknown command")
            || localValue4.contains("unknown or incomplete command")
            || localValue4.contains("command not found")
            || localValue4.contains("no permission")
            || localValue4.contains("not have permission")
            || localValue4.contains("insufficient permission")
            || localValue4.contains("not allowed to use")
            || localValue4.contains("cannot use this command")
            || localValue4.contains("can't use this command")
            || localValue4.contains("\u043d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u0430\u044f \u043a\u043e\u043c\u0430\u043d\u0434\u0430")
            || localValue4.contains("\u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430")
            || localValue4.contains("\u043d\u0435\u0442 \u043f\u0440\u0430\u0432")
            || localValue4.contains("\u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432")
            || localValue4.contains("\u043d\u0435 \u0445\u0432\u0430\u0442\u0430\u0435\u0442 \u043f\u0440\u0430\u0432")
            || localValue4.contains("\u0434\u043e\u0441\u0442\u0443\u043f \u0437\u0430\u043f\u0440\u0435\u0449\u0435\u043d")
            || localValue4.contains("\u0434\u043e\u0441\u0442\u0443\u043f \u0437\u0430\u043f\u0440\u0435\u0449\u0451\u043d")) {
            this.internalField0229 = 0L;
         }
      }
   };

   public NametagEspFeature() {
      super(
         "nametags",
         EntityTargetType.internalField0027,
         EntityTargetType.internalField0028,
         EntityTargetType.internalField0963,
         EntityTargetType.internalField0964
      );
      this.internalMethod00430(this.internalField1263, new EntityTargetType[]{EntityTargetType.internalField0964});
      this.internalField1263.internalMethod06630();
      this.internalMethod02246(new EntityTargetType[]{EntityTargetType.internalField0964});
      this.internalMethod02197(
         new PlayerTargetType[]{PlayerTargetType.internalField0025, PlayerTargetType.internalField0961, PlayerTargetType.internalField0962}
      );
      BooleanSetting localValue1 = this.internalMethod02672("esp.nametags.show_armor", PlayerTargetType.internalField0025);
      BooleanSetting localValue2 = this.internalMethod02672("esp.nametags.show_armor", PlayerTargetType.internalField0961);
      BooleanSetting localValue3 = this.internalMethod02672("esp.nametags.show_armor", PlayerTargetType.internalField0962);
      if (localValue1 != null) {
         localValue1.internalMethod06630();
      }

      if (localValue2 != null) {
         localValue2.internalMethod06630();
      }

      if (localValue3 != null) {
         localValue3.internalMethod06630();
      }
   }

   @Override
   public void internalMethod06439(UiRenderContext localValue1, Entity localValue2, float localValue3, float localValue4, EntityTargetType localValue5, PlayerTargetType localValue6) {
      localValue1.getMatrices().pushMatrix();
      localValue1.getMatrices().translate(0.0F, -localValue2.getHeight() * 15.0F);
      localValue1.getMatrices().translate(localValue3, localValue4);
      localValue1.getMatrices().scale(0.6F, 0.6F);
      localValue1.getMatrices().translate(-localValue3, -localValue4);
      if (localValue5 == EntityTargetType.internalField0027) {
         this.internalMethod04293(localValue1, localValue2, localValue3, localValue4, localValue6);
      } else if (localValue5 == EntityTargetType.internalField0964 && localValue2 instanceof ItemEntity localValue7) {
         this.internalMethod06855(localValue1, localValue7, localValue3, localValue4);
      } else if (localValue5 == EntityTargetType.internalField0028 || localValue5 == EntityTargetType.internalField0963) {
         this.internalMethod04346(localValue1, localValue2, localValue3, localValue4, localValue5);
      }

      localValue1.getMatrices().popMatrix();
   }

   private void internalMethod04293(UiRenderContext localValue1, Entity localValue2, float localValue3, float localValue4, PlayerTargetType localValue5) {
      if (localValue5 == PlayerTargetType.internalField0962) {
         this.internalMethod03940(localValue1, localValue2, localValue3, localValue4);
      } else {
         String localValue6 = localValue2.getName().getString();
         SizedFont localValue7 = Fonts.internalField0449.internalMethod01432(11.0F);
         float localValue8 = localValue7.internalMethod00965(localValue6 + " ");
         float localValue9 = localValue7.internalMethod00965("[20]");
         float localValue10 = 22.0F + localValue8 + localValue9 + 4.0F;
         float localValue11 = localValue3 - localValue10 / 2.0F;
         float localValue12 = localValue4 - 11.0F;
         localValue1.drawRoundedRect(localValue11, localValue12, localValue10, 22.0F, CornerRadii.internalMethod03908(5.0F), new ColorRGBA(12.0F, 12.0F, 12.0F, 235.0F));
         if (localValue2 instanceof AbstractClientPlayerEntity localValue13) {
            localValue1.drawHead(localValue13, localValue11 + 4.0F, localValue12 + 4.0F, 14.0F, CornerRadii.internalMethod03908(4.0F), ColorRGBA.WHITE);
         }

         float localValue16 = localValue11 + 4.0F + 14.0F + 4.0F;
         float localValue14 = localValue12 + (22.0F - localValue7.internalMethod04890()) / 2.0F;
         localValue1.drawText(localValue7, localValue6 + " ", localValue16, localValue14, ColorRGBA.WHITE);
         localValue1.drawText(localValue7, "[20]", localValue16 + localValue8, localValue14, new ColorRGBA(255.0F, 85.0F, 85.0F));
         BooleanSetting localValue15 = this.internalMethod02672("esp.nametags.show_armor", localValue5);
         if (localValue15 != null && localValue15.internalMethod04496()) {
            this.internalMethod05606(localValue1, localValue3, localValue12 - 12.0F);
         }
      }
   }

   private void internalMethod03940(UiRenderContext localValue1, Entity localValue2, float localValue3, float localValue4) {
      String localValue5 = localValue2.getName().getString();
      float localValue6 = Fonts.internalField0449.internalMethod01432(11.0F).internalMethod00965(localValue5) + 31.0F;
      float localValue7 = 26.0F;
      float localValue8 = localValue3 - localValue6 / 2.0F;
      float localValue9 = localValue4 - localValue7 / 2.0F;
      localValue1.drawRoundedRect(localValue8, localValue9, localValue6, localValue7, CornerRadii.internalMethod03908(7.0F), new ColorRGBA(12.0F, 12.0F, 12.0F, 255.0F));
      localValue1.drawText(Fonts.internalField1154.internalMethod01432(11.0F), localValue5, localValue8 + 9.0F, localValue9 + 9.0F, ColorRGBA.WHITE);
      BooleanSetting localValue10 = this.internalMethod02672("esp.nametags.show_armor", PlayerTargetType.internalField0962);
      if (localValue10 != null && localValue10.internalMethod04496()) {
         this.internalMethod05606(localValue1, localValue3, localValue9 - 20.0F);
      }
   }

   private void internalMethod05606(UiRenderContext localValue1, float localValue2, float localValue3) {
      ItemStack localValue4 = new ItemStack(Items.NETHERITE_HELMET);
      ItemStack localValue5 = new ItemStack(Items.NETHERITE_CHESTPLATE);
      ItemStack localValue6 = new ItemStack(Items.NETHERITE_LEGGINGS);
      ItemStack localValue7 = new ItemStack(Items.NETHERITE_BOOTS);
      ItemStack localValue8 = new ItemStack(Items.NETHERITE_SWORD);
      ItemStack localValue9 = new ItemStack(Items.TOTEM_OF_UNDYING);
      List localValue10 = List.of(localValue4, localValue5, localValue6, localValue7, localValue8, localValue9);
      float localValue11 = localValue10.size() * 12.0F;
      float localValue12 = localValue2 - localValue11 / 2.0F;

      for (int localValue13 = 0; localValue13 < localValue10.size(); localValue13++) {
         localValue1.drawItem((ItemStack)localValue10.get(localValue13), (int)(localValue12 + localValue13 * 12), (int)localValue3, 0.75F);
      }
   }

   private void internalMethod06855(UiRenderContext localValue1, ItemEntity localValue2, float localValue3, float localValue4) {
      String localValue5 = localValue2.getStack().getName().getString();
      float localValue6 = Fonts.internalField0449.internalMethod01432(11.0F).internalMethod00965(localValue5);
      float localValue7 = Fonts.internalField0449.internalMethod01432(11.0F).internalMethod04890();
      if (this.internalField1263.internalMethod04496()) {
         localValue1.drawRect(localValue3 - localValue6 / 2.0F - 3.0F, localValue4 - 3.0F, localValue6 + 6.0F, localValue7 + 6.0F, new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F));
      }

      localValue1.drawText(Fonts.internalField0449.internalMethod01432(11.0F), localValue5, localValue3 - localValue6 / 2.0F, localValue4, ColorRGBA.WHITE);
   }

   private void internalMethod04346(UiRenderContext localValue1, Entity localValue2, float localValue3, float localValue4, EntityTargetType localValue5) {
      if (localValue2 instanceof LivingEntity localValue6) {
         String localValue7 = localValue2.getName().getString();
         int localValue8 = (int)localValue6.getHealth();
         String localValue9 = localValue7 + " [" + localValue8 + "]";
         float localValue10 = Fonts.internalField0449.internalMethod01432(11.0F).internalMethod00965(localValue9);
         float localValue11 = Fonts.internalField0449.internalMethod01432(11.0F).internalMethod04890();
         localValue1.drawRect(localValue3 - localValue10 / 2.0F - 3.0F, localValue4 - 3.0F, localValue10 + 6.0F, localValue11 + 6.0F, new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F));
         localValue1.drawText(Fonts.internalField0449.internalMethod01432(11.0F), localValue7 + " ", localValue3 - localValue10 / 2.0F, localValue4, ColorRGBA.WHITE);
         localValue1.drawText(
            Fonts.internalField0449.internalMethod01432(11.0F),
            "[" + localValue8 + "]",
            localValue3 - localValue10 / 2.0F + Fonts.internalField0449.internalMethod01432(11.0F).internalMethod00965(localValue7 + " "),
            localValue4,
            new ColorRGBA(255.0F, 85.0F, 85.0F)
         );
      }
   }

   public boolean internalMethod05662(Entity localValue1) {
      if (localValue1 instanceof PlayerEntity localValue2) {
         if (localValue2 == internalField0149.player) {
            return internalField0149.options.getPerspective().isFirstPerson() ? false : this.internalMethod06170(PlayerTargetType.internalField0026);
         } else if (this.internalMethod01787(localValue2)) {
            return this.internalMethod06170(PlayerTargetType.internalField0962);
         } else {
            return RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue2.getName().getString())
               ? this.internalMethod06170(PlayerTargetType.internalField0961)
               : this.internalMethod06170(PlayerTargetType.internalField0025);
         }
      } else if (localValue1 instanceof ItemEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0964);
      } else if (localValue1 instanceof HostileEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0028);
      } else {
         return localValue1 instanceof AnimalEntity ? this.internalMethod06206(EntityTargetType.internalField0963) : false;
      }
   }

   private float internalMethod05661(Entity localValue1) {
      if (internalField0149.player.age % 25 == 0) {
      }

      float localValue2 = localValue1.distanceTo(internalField0149.player);
      return MathHelper.clamp(1.0F - localValue2 / 20.0F, 0.5F, 1.0F);
   }

   private boolean internalMethod01787(PlayerEntity localValue1) {
      String localValue2 = localValue1.getName().getString();

      for (Packets.InternalType0018 localValue4 : Information.getVisiblePlayers()) {
         if (localValue4.gameInfo() != null && localValue2.equals(localValue4.gameInfo().nickname())) {
            return true;
         }
      }

      return false;
   }

   private PlayerTargetType internalMethod07193(PlayerEntity localValue1) {
      if (localValue1 == internalField0149.player) {
         return PlayerTargetType.internalField0026;
      } else {
         return RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue1.getName().getString())
            ? PlayerTargetType.internalField0961
            : PlayerTargetType.internalField0025;
      }
   }

   private Vec2f internalMethod00076(Entity localValue1, float localValue2) {
      Vec3d localValue3 = ScreenProjectionUtils.internalMethod02822(localValue1, localValue2).add(0.0, localValue1.getBoundingBox().getLengthY() + 0.65, 0.0);
      return ScreenProjectionUtils.internalMethod00612(localValue3);
   }

   private void internalMethod00692() {
      this.internalField0416.clear();
      this.internalField0543.clear();
      this.internalField0544.clear();
      this.internalField1197.clear();
      this.internalField1196.clear();
      this.internalField1195.clear();

      for (Entity localValue2 : internalField0149.world.getEntities()) {
         if (this.internalMethod05662(localValue2)
            && (localValue2.getType() == EntityType.PLAYER || localValue2.getType() == EntityType.ITEM || localValue2 instanceof HostileEntity || localValue2 instanceof AnimalEntity)) {
            boolean localValue3 = false;

            for (Packets.InternalType0018 localValue5 : Information.getVisiblePlayers()) {
               if (localValue5.gameInfo() != null && localValue5.gameInfo().nickname() != null && localValue5.gameInfo().nickname().equals(localValue2.getName().getString())) {
                  this.internalField0543.put(localValue2, localValue5);
                  localValue3 = true;
                  break;
               }
            }

            if (!localValue3) {
               this.internalField0416.add(localValue2);
            }
         }
      }
   }

   private List<List<ItemEntity>> internalMethod00032() {
      LinkedList localValue1 = new LinkedList();
      HashSet localValue2 = new HashSet();

      for (Entity localValue4 : this.internalField0416) {
         if (localValue4 instanceof ItemEntity localValue5 && !localValue2.contains(localValue5)) {
            LinkedList localValue6 = new LinkedList();
            localValue6.add(localValue5);
            localValue2.add(localValue5);

            for (Entity localValue8 : this.internalField0416) {
               if (localValue8 instanceof ItemEntity localValue9 && !localValue2.contains(localValue9) && localValue5.squaredDistanceTo(localValue9) < 1.0) {
                  localValue6.add(localValue9);
                  localValue2.add(localValue9);
               }
            }

            localValue1.add(localValue6);
         }
      }

      return localValue1;
   }

   private void internalMethod03203(TextBatchRenderer localValue1, TextBatchRenderer localValue2, MatrixStack localValue3, List<List<ItemEntity>> localValue4, float localValue5) {
      for (Entity localValue7 : this.internalField0416) {
         if (localValue7.getType() == EntityType.PLAYER || localValue7 instanceof HostileEntity || localValue7 instanceof AnimalEntity) {
            Vec2f localValue8 = this.internalMethod00076(localValue7, localValue5);
            if (localValue8 != null) {
               this.internalMethod01552(localValue1, localValue3, localValue7, localValue8);
            }
         }
      }

      for (List localValue11 : localValue4) {
         if (!localValue11.isEmpty()) {
            Vec2f localValue13 = this.internalMethod00076((Entity)localValue11.getFirst(), localValue5);
            if (localValue13 != null && this.internalField1263.internalMethod04496()) {
               this.internalMethod05544(localValue2, localValue3, localValue11, localValue13);
            }
         }
      }

      for (Entity localValue12 : this.internalField0416) {
         if (localValue12.getType() == EntityType.ITEM) {
            Vec2f localValue14 = this.internalMethod00076(localValue12, localValue5);
            if (localValue14 != null) {
               this.internalMethod05346(localValue2, localValue3, (ItemEntity)localValue12, localValue14);
            }
         }
      }
   }

   private void internalMethod05811(TextBatchRenderer localValue1, MatrixStack localValue2, List<List<ItemEntity>> localValue3, float localValue4) {
      for (Entity localValue6 : this.internalField0416) {
         if (localValue6.getType() == EntityType.PLAYER || localValue6 instanceof HostileEntity || localValue6 instanceof AnimalEntity) {
            Vec2f localValue7 = this.internalMethod00076(localValue6, localValue4);
            if (localValue7 != null) {
               this.internalMethod03166(localValue1, localValue2, localValue6, localValue7);
            }
         }
      }

      for (List localValue10 : localValue3) {
         if (!localValue10.isEmpty()) {
            Vec2f localValue12 = this.internalMethod00076((Entity)localValue10.getFirst(), localValue4);
            if (localValue12 != null) {
               this.internalMethod00307(localValue1, localValue2, localValue10, localValue12);
            }
         }
      }

      for (Entity localValue11 : this.internalField0416) {
         if (localValue11.getType() == EntityType.ITEM) {
            Vec2f localValue13 = this.internalMethod00076(localValue11, localValue4);
            if (localValue13 != null) {
               this.internalMethod05491(localValue1, localValue2, (ItemEntity)localValue11, localValue13);
            }
         }
      }
   }

   private Text internalMethod04364(Entity localValue1) {
      return this.internalField1197
         .computeIfAbsent(
            localValue1,
            localValue1x -> {
               Text localValue2 = this.internalMethod08325(localValue1x);
               return (Text)(localValue1x instanceof PlayerEntity && this.internalMethod00693()
                  ? localValue2
                  : TextUtils.internalMethod00893(localValue2, Fonts.internalField0449));
            }
         );
   }

   private float internalMethod03391(Entity localValue1) {
      return this.internalField1196.computeIfAbsent(localValue1, this::internalMethod08413);
   }

   private float internalMethod08413(Entity localValue1) {
      Text localValue2 = this.internalMethod04364(localValue1);
      if (localValue1 instanceof PlayerEntity && this.internalMethod00693()) {
         float localValue4 = FadeoutTextRenderer.internalMethod01776(Fonts.internalField0449, Fonts.internalField1537, localValue2.getString(), 11.0F);
         return 22.0F + localValue4 + 4.0F;
      } else {
         float localValue3 = TextBatchRenderer.internalMethod06796(Fonts.internalField0449, localValue2, 11.0F);
         return localValue1 instanceof PlayerEntity ? 22.0F + localValue3 + 4.0F : localValue3 + 8.0F;
      }
   }

   private boolean internalMethod03392(Entity localValue1) {
      if (!PostProcessRenderer.internalMethod08007()) {
         return false;
      } else {
         String localValue2 = this.internalMethod04364(localValue1).getString();
         return PostProcessRenderer.internalMethod03546(localValue2) != localValue2;
      }
   }

   private float internalMethod07994(Entity localValue1) {
      return !PostProcessRenderer.internalMethod08007() ? this.internalMethod03391(localValue1) : this.internalField1195.computeIfAbsent(localValue1, localValue1x -> {
         float[] localValue2 = new float[1];
         PostProcessRenderer.internalMethod02021(() -> localValue2[0] = this.internalMethod08413(localValue1x));
         return localValue2[0];
      });
   }

   private void internalMethod04952(TextBatchRenderer localValue1, MatrixStack localValue2, float localValue3) {
      for (Entity localValue5 : this.internalField0416) {
         if (localValue5.getType() == EntityType.PLAYER && localValue5 instanceof AbstractClientPlayerEntity localValue6) {
            Vec2f localValue7 = this.internalMethod00076(localValue5, localValue3);
            if (localValue7 != null) {
               int localValue8 = this.internalField0856.internalMethod05441(localValue6.getSkin().body().texturePath());
               if (localValue8 >= 0) {
                  float localValue9 = this.internalMethod05661(localValue5);
                  float localValue10 = this.internalMethod03391(localValue5);
                  localValue2.push();
                  localValue2.translate(localValue7.x, localValue7.y, 0.0F);
                  localValue2.scale(localValue9, localValue9, 1.0F);
                  float localValue11 = -localValue10 / 2.0F + 4.0F;
                  float localValue12 = 7.0F;
                  localValue1.internalMethod05922(
                     localValue2.peek().getPositionMatrix(),
                     localValue11,
                     localValue12,
                     this.internalField0856.internalMethod03360(localValue8),
                     this.internalField0856.internalMethod03436(localValue8),
                     this.internalField0856.internalMethod01275(),
                     -this.internalMethod07994(localValue5) / 2.0F + 4.0F
                  );
                  localValue2.pop();
               }
            }
         }
      }
   }

   private void internalMethod06425(PreHudRenderEvent localValue1, MatrixStack localValue2, float localValue3) {
      for (Entity localValue5 : this.internalField0416) {
         if (localValue5.getType() == EntityType.PLAYER) {
            PlayerEntity localValue6 = (PlayerEntity)localValue5;
            BooleanSetting localValue7 = this.internalMethod02672("esp.nametags.show_armor", this.internalMethod07193(localValue6));
            if (localValue7 != null && localValue7.internalMethod04496()) {
               Vec2f localValue8 = this.internalMethod00076(localValue5, localValue3);
               if (localValue8 != null) {
                  this.internalMethod00031(localValue1, localValue2, localValue6, localValue8);
               }
            }
         }
      }
   }

   private void internalMethod07470(PreHudRenderEvent localValue1, MatrixStack localValue2, float localValue3) {
      for (Entity localValue5 : this.internalField0416) {
         if (localValue5.getType() == EntityType.ITEM) {
            Vec2f localValue6 = this.internalMethod00076(localValue5, localValue3);
            if (localValue6 != null) {
               this.internalMethod04188(localValue1, localValue2, (ItemEntity)localValue5, localValue6);
            }
         }
      }
   }

   private Text internalMethod08802(Entity localValue1) {
      NameProtectModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
      FriendListManager localValue3 = RockstarClient.getInstance().internalMethod03375();
      boolean localValue4 = localValue2.internalMethod03520().internalMethod04496();
      String localValue5 = localValue2.internalMethod02003().internalMethod08926();
      boolean localValue6 = localValue4 && localValue3.internalMethod00380(localValue1.getName().getString()) && localValue2.isEnabled();
      boolean localValue7 = localValue1 == internalField0149.player && localValue2.isEnabled();
      boolean localValue8 = PostProcessRenderer.internalMethod07188();
      if (localValue8 && (localValue6 || localValue7)) {
         localValue2.internalMethod08287(localValue1.getName().getString());
         localValue6 = false;
         localValue7 = false;
      }

      MutableText localValue9 = Text.empty();
      if (localValue1 instanceof PlayerEntity localValue10) {
         int localValue11 = (int)GameUtils.internalMethod02919(localValue10);
         localValue9 = Text.of(" [" + (localValue11 == 1000 ? "?" : localValue11) + "]").copy().withColor(-2142128);
      }

      if (localValue6) {
         return Text.of(localValue5).copy().append(localValue9);
      } else if (localValue7) {
         return Text.of(localValue2.internalMethod01362().internalMethod08926()).copy().append(localValue9);
      } else {
         return localValue2.isEnabled() && localValue2.internalMethod04251().internalMethod04496()
            ? Text.of(localValue2.internalMethod08287(localValue1.getName().getString())).copy().append(localValue9)
            : localValue1.getDisplayName().copy().append(localValue9);
      }
   }

   private Text internalMethod08325(Entity localValue1) {
      if (localValue1 instanceof PlayerEntity) {
         return this.internalMethod08802(localValue1);
      } else if (localValue1 instanceof LivingEntity localValue2) {
         int localValue3 = (int)localValue2.getHealth();
         MutableText localValue4 = Text.of(" [" + localValue3 + "]").copy().withColor(-2142128);
         return localValue1.getDisplayName().copy().append(localValue4);
      } else {
         return localValue1.getDisplayName().copy();
      }
   }

   private void internalMethod01552(TextBatchRenderer localValue1, MatrixStack localValue2, Entity localValue3, Vec2f localValue4) {
      if (localValue3 instanceof LivingEntity) {
         float localValue5 = this.internalMethod05661(localValue3);
         float localValue6 = this.internalMethod03391(localValue3);
         localValue2.push();
         localValue2.translate(localValue4.x, localValue4.y, 0.0F);
         localValue2.scale(localValue5, localValue5, 1.0F);
         ColorRGBA localValue7;
         if (localValue3 instanceof PlayerEntity localValue8 && RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue8.getName().getString())) {
            localValue7 = new ColorRGBA(14.0F, 32.0F, 16.0F, 235.0F);
         } else {
            localValue7 = new ColorRGBA(12.0F, 12.0F, 12.0F, 235.0F);
         }

         float localValue10 = this.internalMethod07994(localValue3);
         localValue1.internalMethod02746(localValue2.peek().getPositionMatrix(), -localValue6 / 2.0F, 3.0F, localValue6, 22.0F, localValue7, -localValue10 / 2.0F, localValue10);
         if (this.internalMethod03392(localValue3)) {
            float localValue9 = Math.max(localValue6, localValue10);
            localValue1.internalMethod06644(localValue2.peek().getPositionMatrix(), -localValue9 / 2.0F, 3.0F, localValue9, 22.0F);
         }

         localValue2.pop();
      }
   }

   private void internalMethod03166(TextBatchRenderer localValue1, MatrixStack localValue2, Entity localValue3, Vec2f localValue4) {
      if (localValue3 instanceof LivingEntity) {
         float localValue5 = this.internalMethod05661(localValue3);
         float localValue6 = this.internalMethod03391(localValue3);
         localValue2.push();
         localValue2.translate(localValue4.x, localValue4.y, 0.0F);
         localValue2.scale(localValue5, localValue5, 1.0F);
         Matrix4f localValue7 = localValue2.peek().getPositionMatrix();
         Text localValue8 = this.internalMethod04364(localValue3);
         float localValue9 = Fonts.internalField0449.internalMethod01432(11.0F).internalMethod04890();
         float localValue10 = 3.0F + (22.0F - localValue9) / 2.0F;
         float localValue11 = this.internalMethod07994(localValue3);
         float localValue12;
         float localValue13;
         if (localValue3 instanceof PlayerEntity) {
            localValue12 = -localValue6 / 2.0F + 4.0F + 14.0F + 4.0F;
            localValue13 = -localValue11 / 2.0F + 4.0F + 14.0F + 4.0F;
         } else {
            localValue12 = -localValue1.internalMethod02068(localValue8, 11.0F) / 2.0F;
            localValue13 = localValue12;
         }

         if (!(localValue3 instanceof PlayerEntity) || !this.internalMethod00693()) {
            localValue1.internalMethod02159(localValue7, localValue8, 11.0F, localValue12, localValue10, 0.0F, localValue13);
         }

         if (localValue3 instanceof PlayerEntity localValue14 && !this.internalMethod00693()) {
            ItemStack localValue15 = localValue14.getOffHandStack();
            CustomItemUtils.InternalType0254 localValue16 = CustomItemUtils.internalMethod07586(localValue15);
            if (localValue16 != null
               && (
                  localValue16.internalMethod08988() || localValue16.internalMethod08992() && localValue16.internalMethod07218() != CustomItemUtils.InternalType0440.internalField1338
               )) {
               Text localValue17 = this.internalMethod07156(localValue15, localValue16);
               float localValue18 = localValue1.internalMethod02068(localValue17, 9.0F);
               localValue1.internalMethod02261(localValue7, localValue17, 9.0F, -localValue18 / 2.0F, 27.0F, 0.0F);
            }
         }

         localValue2.pop();
      }
   }

   private boolean internalMethod00693() {
      return ServerUtils.internalMethod01786(KnownServer.internalField1220)
         || ServerUtils.internalMethod01786(KnownServer.internalField1571);
   }

   private void internalMethod08166(PreHudRenderEvent localValue1, MatrixStack localValue2, float localValue3) {
      if (this.internalMethod00693()) {
         FadeoutTextRenderer localValue4 = new FadeoutTextRenderer(Fonts.internalField0449, Fonts.internalField1537);
         localValue4.internalMethod01439(localValue1.getContext());

         for (Entity localValue6 : this.internalField0416) {
            if (localValue6.getType() == EntityType.PLAYER) {
               Vec2f localValue7 = this.internalMethod00076(localValue6, localValue3);
               if (localValue7 != null) {
                  float localValue8 = this.internalMethod05661(localValue6);
                  float localValue9 = this.internalMethod03391(localValue6);
                  Text localValue10 = this.internalMethod04364(localValue6);
                  float localValue11 = Fonts.internalField0449.internalMethod01432(11.0F).internalMethod04890();
                  float localValue12 = 3.0F + (22.0F - localValue11) / 2.0F;
                  float localValue13 = -localValue9 / 2.0F + 4.0F + 14.0F + 4.0F;
                  localValue2.push();
                  localValue2.translate(localValue7.x, localValue7.y, 0.0F);
                  localValue2.scale(localValue8, localValue8, 1.0F);
                  localValue4.internalMethod05677(localValue2.peek().getPositionMatrix(), localValue10, 11.0F, localValue13, localValue12, 0.0F);
                  localValue2.pop();
               }
            }
         }

         localValue4.internalMethod05430();
      }
   }

   private void internalMethod00031(PreHudRenderEvent localValue1, MatrixStack localValue2, PlayerEntity localValue3, Vec2f localValue4) {
      float localValue5 = this.internalMethod05661(localValue3);
      localValue2.push();
      localValue2.translate(localValue4.x, localValue4.y, 0.0F);
      localValue2.scale(localValue5, localValue5, 1.0F);
      LinkedList localValue6 = new LinkedList();
         localValue6.add(rockstar.client.util.LegacyItemTypes.armorItems(localValue3).get(3));
         localValue6.add(rockstar.client.util.LegacyItemTypes.armorItems(localValue3).get(2));
         localValue6.add(rockstar.client.util.LegacyItemTypes.armorItems(localValue3).get(1));
         localValue6.add(rockstar.client.util.LegacyItemTypes.armorItems(localValue3).get(0));
      localValue6.add(localValue3.getMainHandStack());
      localValue6.add(localValue3.getOffHandStack());
      localValue6.removeIf(value -> ((ItemStack)value).isEmpty());
      if (!localValue6.isEmpty()) {
         float localValue7 = (localValue6.size() - 1) * 18.0F + 16.0F;
         float localValue8 = -localValue7 / 2.0F;

         try (CustomDrawContext.InternalType0486 localValue9 = localValue1.getContext().beginItemBatch()) {
            for (int localValue10 = 0; localValue10 < localValue6.size(); localValue10++) {
               localValue1.getContext().drawBatchItem((ItemStack)localValue6.get(localValue10), localValue8 + localValue10 * 18.0F, -15.0F);
            }
         }
      }

      localValue2.pop();
   }

   private void internalMethod01962(PreHudRenderEvent localValue1, MatrixStack localValue2, PlayerEntity localValue3, Vec2f localValue4) {
      if (localValue3.isUsingItem()) {
         ItemStack localValue5 = localValue3.getActiveItem();
         if (!localValue5.isEmpty()) {
            UseAction localValue6 = localValue5.getUseAction();
            if (localValue6 == UseAction.EAT || localValue6 == UseAction.DRINK) {
               int localValue7 = localValue5.getMaxUseTime(localValue3);
               int localValue8 = localValue3.getItemUseTimeLeft();
               if (localValue7 > 0) {
                  float localValue9 = 1.0F - (float)localValue8 / localValue7;
                  float localValue10 = localValue3.distanceTo(internalField0149.player);
                  float localValue11 = MathHelper.clamp(1.0F - localValue10 / 20.0F, 0.5F, 1.0F) * 0.4F;
                  float localValue12 = 80.0F;
                  float localValue13 = 80.0F;
                  localValue2.push();
                  localValue2.translate(localValue4.x - localValue12 / 2.0F, localValue4.y - localValue13 / 2.0F, 0.0F);
                  HudRenderUtils.internalMethod08976(localValue2, localValue12 / 2.0F, localValue13 / 2.0F, localValue11);
                  localValue1.getContext()
                     .drawBlurredRect(0.0F, 0.0F, localValue12, localValue13, 25.0F, 3.0F, CornerRadii.internalMethod03908(14.0F), ThemeColors.internalField1312);
                  localValue1.getContext()
                     .drawSquircle(0.0F, 0.0F, localValue12, localValue13, 3.0F, CornerRadii.internalMethod03908(14.0F), new ColorRGBA(9.0F, 9.0F, 11.0F).mulAlpha(0.5F));
                  localValue1.getContext().drawCircleProgress(localValue12 / 2.0F, localValue13 / 2.0F, 28.0F, 4.0F, localValue9, ThemeColors.internalMethod02531());
                  localValue1.getContext().drawItem(localValue5, 24.0F, 24.0F, 2.0F);
                  HudRenderUtils.internalMethod00012(localValue2);
                  localValue2.pop();
               }
            }
         }
      }
   }

   private void internalMethod05544(TextBatchRenderer localValue1, MatrixStack localValue2, List<ItemEntity> localValue3, Vec2f localValue4) {
      if (!localValue3.isEmpty()) {
         float localValue5 = this.internalMethod05661((Entity)localValue3.getFirst());
         List localValue6 = this.internalMethod06843(localValue3);
         if (!localValue6.isEmpty()) {
            int localValue7 = (int)Fonts.internalField0449.internalMethod01432(11.0F).internalMethod04890();
            int localValue8 = 0;

            for (NametagEspFeature.InternalType0173 localValue10 : (Iterable<NametagEspFeature.InternalType0173>)(Iterable<?>)localValue6) {
               localValue8 = Math.max(localValue8, (int)this.internalMethod05773(localValue1, localValue10));
            }

            localValue2.push();
            localValue2.translate(localValue4.x, localValue4.y, 0.0F);
            localValue2.scale(localValue5, localValue5, 1.0F);
            int localValue11 = localValue7 * localValue6.size() + 3 * (localValue6.size() - 1);
            localValue1.internalMethod04362(localValue2.peek().getPositionMatrix(), -localValue8 / 2.0F - 3.0F, 2.0F, localValue8 + 6, localValue11 + 6, new ColorRGBA(0.0F, 0.0F, 0.0F, 150.0F));
            localValue2.pop();
         }
      }
   }

   private void internalMethod00307(TextBatchRenderer localValue1, MatrixStack localValue2, List<ItemEntity> localValue3, Vec2f localValue4) {
      if (!localValue3.isEmpty()) {
         float localValue5 = this.internalMethod05661((Entity)localValue3.getFirst());
         List localValue6 = this.internalMethod06843(localValue3);
         if (!localValue6.isEmpty()) {
            int localValue7 = (int)Fonts.internalField0449.internalMethod01432(11.0F).internalMethod04890();
            localValue2.push();
            localValue2.translate(localValue4.x, localValue4.y, 0.0F);
            localValue2.scale(localValue5, localValue5, 1.0F);
            Matrix4f localValue8 = localValue2.peek().getPositionMatrix();

            for (int localValue9 = 0; localValue9 < localValue6.size(); localValue9++) {
               NametagEspFeature.InternalType0173 localValue10 = (NametagEspFeature.InternalType0173)localValue6.get(localValue9);
               float localValue11 = localValue1.internalMethod02068(localValue10.internalMethod01031(), 11.0F);
               float localValue12 = -this.internalMethod05773(localValue1, localValue10) / 2.0F;
               float localValue13 = 5 + localValue9 * (localValue7 + 3);
               localValue1.internalMethod02261(localValue8, localValue10.internalMethod01031(), 11.0F, localValue12, localValue13, 0.0F);
               localValue1.internalMethod00978(localValue8, this.internalMethod00824(localValue10), 11.0F, localValue12 + localValue11, localValue13, 0.0F, ColorRGBA.WHITE.getRGB());
            }

            localValue2.pop();
         }
      }
   }

   private void internalMethod05346(TextBatchRenderer localValue1, MatrixStack localValue2, ItemEntity localValue3, Vec2f localValue4) {
      List localValue5 = ItemNbtUtils.internalMethod00825(localValue3.getStack());
      if (!localValue5.isEmpty()) {
         float localValue6 = this.internalMethod05661(localValue3);
         int localValue7 = Math.min(localValue5.size(), 9);
         int localValue8 = (int)Math.ceil(localValue5.size() / 9.0F);
         int localValue9 = localValue7 * 18 + 4;
         int localValue10 = localValue8 * 18 + 4;
         localValue2.push();
         localValue2.translate(localValue4.x, localValue4.y, 0.0F);
         localValue2.scale(localValue6, localValue6, 1.0F);
         localValue1.internalMethod04362(localValue2.peek().getPositionMatrix(), -localValue9 / 2.0F, -localValue10 / 2.0F, localValue9, localValue10, new ColorRGBA(0.0F, 0.0F, 0.0F, 180.0F));
         localValue2.pop();
      }
   }

   private void internalMethod04188(PreHudRenderEvent localValue1, MatrixStack localValue2, ItemEntity localValue3, Vec2f localValue4) {
      List localValue5 = ItemNbtUtils.internalMethod00825(localValue3.getStack());
      if (!localValue5.isEmpty()) {
         float localValue6 = this.internalMethod05661(localValue3);
         int localValue7 = Math.min(localValue5.size(), 9);
         int localValue8 = (int)Math.ceil(localValue5.size() / 9.0F);
         int localValue9 = localValue7 * 18 + 4;
         int localValue10 = localValue8 * 18 + 4;
         localValue2.push();
         localValue2.translate(localValue4.x, localValue4.y, 0.0F);
         localValue2.scale(localValue6, localValue6, 1.0F);

         try (CustomDrawContext.InternalType0486 localValue11 = localValue1.getContext().beginItemBatch()) {
            for (int localValue12 = 0; localValue12 < localValue5.size(); localValue12++) {
               int localValue13 = localValue12 % 9 * 18 - localValue9 / 2 + 3;
               int localValue14 = localValue12 / 9 * 18 - localValue10 / 2 + 3;
               localValue1.getContext().drawBatchItem((ItemStack)localValue5.get(localValue12), localValue13, localValue14);
            }
         }

         localValue2.pop();
      }
   }

   private void internalMethod05491(TextBatchRenderer localValue1, MatrixStack localValue2, ItemEntity localValue3, Vec2f localValue4) {
      List localValue5 = ItemNbtUtils.internalMethod00825(localValue3.getStack());
      if (!localValue5.isEmpty()) {
         float localValue6 = this.internalMethod05661(localValue3);
         int localValue7 = Math.min(localValue5.size(), 9);
         int localValue8 = (int)Math.ceil(localValue5.size() / 9.0F);
         int localValue9 = localValue7 * 18 + 4;
         int localValue10 = localValue8 * 18 + 4;
         localValue2.push();
         localValue2.translate(localValue4.x, localValue4.y, 0.0F);
         localValue2.scale(localValue6, localValue6, 1.0F);
         Matrix4f localValue11 = localValue2.peek().getPositionMatrix();
         localValue1.internalMethod00978(localValue11, localValue3.getDisplayName().getString(), 11.0F, -localValue9 / 2.0F + 0.5F, -localValue10 / 2.0F - 11.0F, 0.0F, ColorRGBA.WHITE.getRGB());

         for (int localValue12 = 0; localValue12 < localValue5.size(); localValue12++) {
            ItemStack localValue13 = (ItemStack)localValue5.get(localValue12);
            if (localValue13.getCount() > 1) {
               float localValue14 = localValue12 % 9 * 18 - localValue9 / 2.0F + 3.0F;
               float localValue15 = localValue12 / 9 * 18 - localValue10 / 2.0F + 3.0F;
               String localValue16 = String.valueOf(localValue13.getCount());
               localValue1.internalMethod00978(
                  localValue11,
                  localValue16,
                  11.0F,
                  localValue14 + 16.0F - Fonts.internalField0449.internalMethod01432(11.0F).internalMethod00965(localValue16),
                  localValue15 + 9.0F,
                  0.0F,
                  ColorRGBA.WHITE.getRGB()
               );
            }
         }

         localValue2.pop();
      }
   }

   private void internalMethod07863(PreHudRenderEvent localValue1, MatrixStack localValue2, float localValue3) {
      for (Entity localValue5 : this.internalField0543.keySet()) {
         Vec2f localValue6 = this.internalMethod00076(localValue5, localValue3);
         if (localValue6 != null) {
            Packets.InternalType0018 localValue7 = this.internalField0543.get(localValue5);
            NameProtectModule localValue8 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
            FriendListManager localValue9 = RockstarClient.getInstance().internalMethod03375();
            boolean localValue10 = localValue8.internalMethod03520().internalMethod04496();
            String localValue11 = localValue8.internalMethod02003().internalMethod08926();
            boolean localValue12 = localValue10 && localValue9.internalMethod00380(localValue5.getName().getString()) && localValue8.isEnabled();
            if (localValue12 && PostProcessRenderer.internalMethod07188()) {
               localValue8.internalMethod08287(localValue5.getName().getString());
               localValue12 = false;
            }

            MutableText localValue13;
            if (localValue12) {
               localValue13 = Text.of(localValue11).copy();
            } else if (localValue8.isEnabled() && localValue8.internalMethod04251().internalMethod04496()) {
               localValue13 = Text.of(localValue8.internalMethod08287(localValue5.getName().getString())).copy();
            } else {
               localValue13 = localValue5.getDisplayName().copy();
            }

            float localValue14 = localValue5.distanceTo(internalField0149.player);
            float localValue15 = MathHelper.clamp((1.0F - localValue14 / 20.0F) * 1.5F, 0.5F, 0.7F);
            boolean localValue16 = "none".equals(localValue7.visibility()) || "hidden".equals(localValue7.visibility());
            int localValue17 = localValue16 ? 80 : 255;
            String localValue18 = internalMethod07483(localValue13);
            String localValue19 = PostProcessRenderer.internalMethod03546(localValue18);
            if (localValue19 != localValue18) {
               float[] localValue20 = new float[1];
               PostProcessRenderer.internalMethod02021(() -> localValue20[0] = this.internalMethod07122(localValue18, localValue7));
               float localValue21 = Math.max(localValue20[0], this.internalMethod07122(localValue19, localValue7));
               PostProcessRenderer.internalMethod01602(
                  this.internalMethod03271(localValue2, localValue6, localValue15, localValue21),
                  () -> PostProcessRenderer.internalMethod02021(() -> this.internalMethod02200(localValue1, localValue2, localValue7, localValue18, localValue6, localValue15, localValue17))
               );
            }

            PostProcessRenderer.internalMethod02021(() -> this.internalMethod02200(localValue1, localValue2, localValue7, localValue19, localValue6, localValue15, localValue17));
            if (localValue5 instanceof PlayerEntity localValue22) {
               BooleanSetting localValue23 = this.internalMethod02672("esp.nametags.show_armor", PlayerTargetType.internalField0962);
               if (localValue23 != null && localValue23.internalMethod04496()) {
                  this.internalMethod04143(localValue1, localValue2, localValue22, localValue6, localValue15);
               }
            }
         }
      }
   }

   private void internalMethod02200(PreHudRenderEvent localValue1, MatrixStack localValue2, Packets.InternalType0018 localValue3, String localValue4, Vec2f localValue5, float localValue6, int localValue7) {
      float localValue8 = this.internalMethod07122(localValue4, localValue3);
      float localValue9 = 26.0F;
      localValue2.push();
      localValue2.translate(localValue5.x, localValue5.y, 0.0F);
      localValue2.scale(localValue6, localValue6, 1.0F);
      localValue2.translate(-localValue8 / 2.0F, -localValue9 / 2.0F, 0.0F);
      localValue1.getContext().drawRoundedRect(0.0F, 0.0F, localValue8, localValue9, CornerRadii.internalMethod03908(7.0F), new ColorRGBA(12.0F, 12.0F, 12.0F, localValue7));
      CosmeticRender.draw(
         localValue1.getContext(),
         Fonts.internalField1154.internalMethod01432(11.0F),
         localValue4,
         9.0F,
         9.0F,
         localValue3.nickStyle(),
         localValue3.badge(),
         ColorRGBA.WHITE.withAlpha(localValue7),
         9.0F
      );
      localValue2.pop();
   }

   private int[] internalMethod03271(MatrixStack localValue1, Vec2f localValue2, float localValue3, float localValue4) {
      localValue1.push();
      localValue1.translate(localValue2.x, localValue2.y, 0.0F);
      localValue1.scale(localValue3, localValue3, 1.0F);
      localValue1.translate(-localValue4 / 2.0F, -13.0F, 0.0F);
      int[] localValue5 = PostProcessRenderer.internalMethod04696(localValue1.peek().getPositionMatrix(), -2.0F, -2.0F, localValue4 + 2.0F, 28.0F);
      localValue1.pop();
      return localValue5;
   }

   private void internalMethod04143(PreHudRenderEvent localValue1, MatrixStack localValue2, PlayerEntity localValue3, Vec2f localValue4, float localValue5) {
      localValue2.push();
      localValue2.translate(localValue4.x, localValue4.y, 0.0F);
      localValue2.scale(localValue5, localValue5, 1.0F);
      LinkedList localValue6 = new LinkedList();
         localValue6.add(rockstar.client.util.LegacyItemTypes.armorItems(localValue3).get(3));
         localValue6.add(rockstar.client.util.LegacyItemTypes.armorItems(localValue3).get(2));
         localValue6.add(rockstar.client.util.LegacyItemTypes.armorItems(localValue3).get(1));
         localValue6.add(rockstar.client.util.LegacyItemTypes.armorItems(localValue3).get(0));
      localValue6.add(localValue3.getMainHandStack());
      localValue6.add(localValue3.getOffHandStack());
      localValue6.removeIf(value -> ((ItemStack)value).isEmpty());
      if (!localValue6.isEmpty()) {
         float localValue7 = (localValue6.size() - 1) * 18.0F + 16.0F;
         float localValue8 = -localValue7 / 2.0F;

         try (CustomDrawContext.InternalType0486 localValue9 = localValue1.getContext().beginItemBatch()) {
            for (int localValue10 = 0; localValue10 < localValue6.size(); localValue10++) {
               localValue1.getContext().drawBatchItem((ItemStack)localValue6.get(localValue10), (int)(localValue8 + localValue10 * 18), -30.0F);
            }
         }
      }

      localValue2.pop();
   }

   public boolean internalMethod00775(double localValue1, double localValue3) {
      if (!this.internalMethod06968()) {
         return false;
      } else {
         for (Entity localValue6 : this.internalField0416) {
            if (localValue6.getType() == EntityType.PLAYER) {
               Vec2f localValue7 = this.internalMethod00076(localValue6, 1.0F);
               if (localValue7 != null && this.internalMethod06064(localValue6, localValue7, localValue1, localValue3)) {
                  return true;
               }
            }
         }

         for (Entity localValue9 : this.internalField0543.keySet()) {
            Vec2f localValue10 = this.internalMethod00076(localValue9, 1.0F);
            if (localValue10 != null && this.internalMethod00135(localValue9, localValue10, localValue1, localValue3)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean internalMethod06064(Entity localValue1, Vec2f localValue2, double localValue3, double localValue5) {
      float localValue7 = this.internalMethod05661(localValue1);
      float localValue8 = this.internalMethod03391(localValue1);
      float localValue9 = localValue8 * localValue7;
      float localValue10 = 22.0F * localValue7;
      float localValue11 = localValue2.x - localValue9 / 2.0F;
      float localValue12 = localValue2.y + 3.0F * localValue7;
      return UiUtils.internalMethod05785(localValue11, localValue12, localValue9, localValue10, localValue3, localValue5);
   }

   private boolean internalMethod00135(Entity localValue1, Vec2f localValue2, double localValue3, double localValue5) {
      float localValue7 = this.internalMethod01722(localValue1, this.internalField0543.get(localValue1));
      float localValue8 = 26.0F;
      float localValue9 = localValue1.distanceTo(internalField0149.player);
      float localValue10 = MathHelper.clamp((1.0F - localValue9 / 20.0F) * 1.5F, 0.5F, 0.7F);
      float localValue11 = localValue7 * localValue10;
      float localValue12 = localValue8 * localValue10;
      float localValue13 = localValue2.x - localValue11 / 2.0F;
      float localValue14 = localValue2.y - localValue12 / 2.0F;
      return UiUtils.internalMethod05785(localValue13, localValue14, localValue11, localValue12, localValue3, localValue5);
   }

   private float internalMethod01722(Entity localValue1, Packets.InternalType0018 localValue2) {
      return this.internalMethod07122(internalMethod07483(this.internalMethod08200(localValue1)), localValue2);
   }

   private float internalMethod07122(String localValue1, Packets.InternalType0018 localValue2) {
      String localValue3 = localValue2 == null ? null : localValue2.badge();
      return CosmeticRender.width(Fonts.internalField1154.internalMethod01432(11.0F), localValue1, localValue3, 9.0F) + 31.0F;
   }

   private static String internalMethod07483(Text localValue0) {
      return TextUtils.internalMethod00893(localValue0, Fonts.internalField1154).getString();
   }

   private Text internalMethod08200(Entity localValue1) {
      NameProtectModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
      FriendListManager localValue3 = RockstarClient.getInstance().internalMethod03375();
      boolean localValue4 = localValue2.internalMethod03520().internalMethod04496() && localValue3.internalMethod00380(localValue1.getName().getString()) && localValue2.isEnabled();
      if (localValue4 && PostProcessRenderer.internalMethod07188()) {
         localValue2.internalMethod08287(localValue1.getName().getString());
         localValue4 = false;
      }

      if (localValue4) {
         return Text.of(localValue2.internalMethod02003().internalMethod08926()).copy();
      } else {
         return localValue2.isEnabled() && localValue2.internalMethod04251().internalMethod04496()
            ? Text.of(localValue2.internalMethod08287(localValue1.getName().getString())).copy()
            : localValue1.getDisplayName().copy();
      }
   }

   private void internalMethod06984(float localValue1, float localValue2, Entity localValue3) {
      FriendListManager localValue4 = RockstarClient.getInstance().internalMethod03375();
      TargetSelectionManager localValue5 = RockstarClient.getInstance().internalMethod04463();
      String localValue6 = localValue3.getName().getString();
      NameProtectModule localValue7 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
      String localValue8 = localValue7.isEnabled() ? localValue7.internalMethod08287(localValue6) : localValue6;
      this.internalField0574 = new HudContextMenu(localValue1, localValue2, 100.0F, 6.0F)
         .internalMethod04738(localValue8)
         .internalMethod03873()
         .internalMethod05004(LanguageManager.internalMethod07214("friend"), localValue4.internalMethod00380(localValue6), localValue2x -> {
            if (localValue2x) {
               localValue4.internalMethod00379(localValue6);
            } else {
               localValue4.internalMethod06965(localValue6);
            }
         })
         .internalMethod05004(LanguageManager.internalMethod07214("enemy"), localValue5.internalMethod04582(localValue6), localValue2x -> {
            if (localValue2x) {
               localValue5.internalMethod04581(localValue6);
            } else {
               localValue5.internalMethod02995(localValue6);
            }
         })
         .internalMethod03323(LanguageManager.internalMethod07214("invsee"), "invsee", localValue2x -> {
            this.internalMethod03272(localValue6);
            localValue2x.internalMethod05781(false);
         })
         .internalMethod03323(LanguageManager.internalMethod07214("copy"), "copy", localValue1x -> {
            TextUtils.internalMethod05864(localValue6);
            localValue1x.internalMethod05781(false);
         });
   }

   private void internalMethod03272(String localValue1) {
      if (internalField0149.player != null) {
         this.internalField0248 = localValue1;
         this.internalField0229 = System.currentTimeMillis() + 1500L;

         try {
            internalField0149.player.networkHandler.sendChatCommand("invsee " + localValue1);
         } catch (RuntimeException localValue3) {
            this.internalField0229 = 0L;
         }
      }
   }

   private void internalMethod08025() {
      if (this.internalField0248 != null) {
         if (internalField0149.currentScreen instanceof HandledScreen) {
            this.internalField0248 = null;
            this.internalField0229 = 0L;
         } else if (internalField0149.player != null && internalField0149.world != null) {
            if (System.currentTimeMillis() >= this.internalField0229) {
               String localValue1 = this.internalField0248;
               this.internalField0248 = null;
               this.internalField0229 = 0L;
               RockstarClient.getInstance()
                  .internalMethod05348()
                  .internalMethod04610(RockstarClient.getInstance().internalMethod05348().internalMethod03606() + "invsee " + localValue1);
            }
         } else {
            this.internalField0248 = null;
            this.internalField0229 = 0L;
         }
      }
   }

   private Text internalMethod07156(ItemStack localValue1, CustomItemUtils.InternalType0254 localValue2) {
      if (localValue2 == null || localValue1.contains(DataComponentTypes.CUSTOM_NAME)) {
         MutableText localValue3 = TextUtils.internalMethod00893(localValue1.getName(), Fonts.internalField0449);
         if (!localValue3.getString().isBlank()) {
            return localValue3;
         }
      }

      ColorRGBA localValue5 = localValue2 != null ? localValue2.internalMethod01667(localValue1) : null;
      String localValue4 = localValue2 != null ? localValue2.internalMethod00671(localValue1) : localValue1.getItem().getName().getString();
      return Text.literal(TextUtils.internalMethod06864(localValue4)).withColor((localValue5 != null ? localValue5 : ColorRGBA.WHITE).getRGB() & 16777215);
   }

   private List<NametagEspFeature.InternalType0173> internalMethod06843(List<ItemEntity> localValue1) {
      return this.internalField0544.computeIfAbsent((ItemEntity)localValue1.getFirst(), localValue2 -> {
         LinkedHashMap localValue3 = new LinkedHashMap();

         for (ItemEntity localValue5 : localValue1) {
            ItemStack localValue6 = localValue5.getStack();
            Text localValue7 = this.internalMethod07156(localValue6, CustomItemUtils.internalMethod03238(localValue6));
            NametagEspFeature.InternalType0173 localValue8 = (NametagEspFeature.InternalType0173)localValue3.get(localValue7.getString());
            int localValue9 = (localValue8 == null ? 0 : localValue8.internalMethod00201()) + localValue6.getCount();
            localValue3.put(localValue7.getString(), new NametagEspFeature.InternalType0173(localValue7, localValue9));
         }

         return new ArrayList<>(localValue3.values());
      });
   }

   private float internalMethod05773(TextBatchRenderer localValue1, NametagEspFeature.InternalType0173 localValue2) {
      return localValue1.internalMethod02068(localValue2.internalMethod01031(), 11.0F)
         + Fonts.internalField0449.internalMethod01432(11.0F).internalMethod00965(this.internalMethod00824(localValue2));
   }

   private String internalMethod00824(NametagEspFeature.InternalType0173 localValue1) {
      return " " + localValue1.internalMethod00201() + "x";
   }

   public static Text internalMethod06809(Entity localValue0) {
      if (localValue0.getDisplayName() == null) {
         return Text.empty();
      } else {
         NameProtectModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
         String localValue2 = localValue1.isEnabled() ? localValue1.internalMethod08287(localValue0.getName().getString()) : localValue0.getDisplayName().getString();
         MutableText localValue3 = Text.of(localValue2).copy();
         if (localValue0 instanceof LivingEntity localValue4) {
            int localValue5 = localValue0 instanceof PlayerEntity localValue6 ? (int)GameUtils.internalMethod02919(localValue6) : (int)localValue4.getHealth();
            if (!localValue3.getString().endsWith(" ")) {
               localValue3.append(" ");
            }

            return localValue3.append(Text.of("[" + (localValue5 == 1000 ? "?" : localValue5) + "]").copy().withColor(-2142128));
         } else {
            return localValue3;
         }
      }
   }

   @Generated
   public List<Entity> internalMethod01965() {
      return this.internalField0416;
   }

   @Generated
   public Map<Entity, Packets.InternalType0018> internalMethod09641() {
      return this.internalField0543;
   }

   @Generated
   public Map<ItemEntity, List<NametagEspFeature.InternalType0173>> internalMethod09300() {
      return this.internalField0544;
   }

   @Generated
   public Map<Entity, Text> internalMethod09430() {
      return this.internalField1197;
   }

   @Generated
   public Map<Entity, Float> internalMethod09903() {
      return this.internalField1196;
   }

   @Generated
   public Map<Entity, Float> internalMethod09947() {
      return this.internalField1195;
   }

   @Generated
   public BooleanSetting internalMethod01205() {
      return this.internalField0650;
   }

   @Generated
   public BooleanSetting internalMethod01881() {
      return this.internalField0651;
   }

   @Generated
   public BooleanSetting internalMethod09024() {
      return this.internalField1261;
   }

   @Generated
   public BooleanSetting internalMethod09147() {
      return this.internalField1263;
   }

   @Generated
   public ItemIconAtlas internalMethod03862() {
      return this.internalField0856;
   }

   @Generated
   public HudContextMenu internalMethod00731() {
      return this.internalField0574;
   }

   @Generated
   public String internalMethod02235() {
      return this.internalField0248;
   }

   @Generated
   public long internalMethod00663() {
      return this.internalField0229;
   }

   @Generated
   public EventListener<PreHudRenderEvent> internalMethod05359() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<ChatRenderEvent> internalMethod06639() {
      return this.internalField0158;
   }

   @Generated
   public EventListener<ChatClickEvent> internalMethod08048() {
      return this.internalField1028;
   }

   @Generated
   public EventListener<ReceivePacketEvent> internalMethod08301() {
      return this.internalField1029;
   }

   static final class InternalType0173 {
      private final Text internalField0125;
      private final int internalField0227;

      InternalType0173(Text localValue1, int localValue2) {
         this.internalField0125 = localValue1;
         this.internalField0227 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0173[name=" + this.internalField0125 + ", count=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0125);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         NametagEspFeature.InternalType0173 other = (NametagEspFeature.InternalType0173) localValue1;
         return java.util.Objects.equals(this.internalField0125, other.internalField0125)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Text internalMethod01031() {
         return this.internalField0125;
      }

      public int internalMethod00201() {
         return this.internalField0227;
      }
   }
}
