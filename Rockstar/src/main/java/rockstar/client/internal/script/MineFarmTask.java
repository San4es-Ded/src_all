package rockstar.client.internal.script;













import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.data.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

public class MineFarmTask extends AutoFarmModeState {
   private static final BlockPos internalField0352 = new BlockPos(-75, 84, 29);
   private static final BlockPos internalField0351 = new BlockPos(-54, 93, 50);
   private static final BlockState internalField0934 = Blocks.STONE.getDefaultState();
   private static final int internalField0228 = 2;
   private final MultiSelectSetting internalField0675;
   private final MultiSelectSetting.InternalType0091 internalField0245;
   private final MultiSelectSetting.InternalType0091 internalField0244;
   private final MultiSelectSetting.InternalType0091 internalField1075;
   private final BooleanSetting internalField0650;
   private final MultiSelectSetting internalField0674;
   private final MultiSelectSetting.InternalType0091 internalField1074;
   private final MultiSelectSetting.InternalType0091 internalField1073;
   private final MultiSelectSetting.InternalType0091 internalField1072;
   private final MultiSelectSetting.InternalType0091 internalField1491;
   private final MultiSelectSetting.InternalType0091 internalField1488;
   private final MultiSelectSetting.InternalType0091 internalField1490;
   private final BooleanSetting internalField0651;
   private final BooleanSetting internalField1261;
   private final MultiSelectSetting internalField1276;
   private final MultiSelectSetting.InternalType0091 internalField1489;
   private final MultiSelectSetting.InternalType0091 internalField1493;
   private final MultiSelectSetting.InternalType0091 internalField1487;
   private final MultiSelectSetting.InternalType0091 internalField1486;
   private final BooleanSetting internalField1263;
   private final TextSetting internalField0384;
   private final SliderSetting internalField0383;
   private final BooleanSetting internalField1262;
   private final SliderSetting internalField0382;
   private final SliderSetting internalField1142;
   private final SliderSetting internalField1140;
   private BooleanSetting internalField1264;
   private SliderSetting internalField1141;
   private static final Set<Item> internalField0546 = Set.of(
      Items.DIAMOND,
      Items.EMERALD,
      Items.ANCIENT_DEBRIS,
      Items.NETHERITE_SCRAP,
      Items.RAW_GOLD,
      Items.RAW_IRON,
      Items.RAW_COPPER,
      Items.GOLD_INGOT,
      Items.IRON_INGOT,
      Items.COPPER_INGOT,
      Items.LAPIS_LAZULI,
      Items.REDSTONE,
      Items.COAL,
      Items.QUARTZ
   );
   private static final Set<Item> internalField0545 = Set.of(
      Items.DIAMOND,
      Items.EMERALD,
      Items.ANCIENT_DEBRIS,
      Items.NETHERITE_SCRAP,
      Items.NETHERITE_INGOT,
      Items.RAW_GOLD,
      Items.RAW_IRON,
      Items.RAW_COPPER,
      Items.GOLD_INGOT,
      Items.IRON_INGOT,
      Items.COPPER_INGOT,
      Items.GOLD_NUGGET,
      Items.IRON_NUGGET,
      Items.LAPIS_LAZULI,
      Items.REDSTONE,
      Items.COAL,
      Items.QUARTZ,
      Items.DIAMOND_BLOCK,
      Items.EMERALD_BLOCK,
      Items.NETHERITE_BLOCK,
      Items.GOLD_BLOCK,
      Items.IRON_BLOCK
   );
   private static final Pattern internalField0293 = Pattern.compile("\u0437\u0430\\s+(\\d+)\\s*\u0448\u0442", 66);
   private MineFarmTask.InternalType0366 internalField0739;
   private volatile List<FunTimeMineInfo> internalField0416;
   private volatile long internalField1060;
   private volatile String internalField1077;
   private volatile long internalField1057;
   private Thread internalField0380;
   private final Stopwatch internalField0519;
   private FunTimeMineInfo internalField0239;
   private int internalField1053;
   private boolean internalField0277;
   private int internalField1055;
   private final Stopwatch internalField0518;
   private boolean internalField0276;
   private long internalField1473;
   private boolean internalField1099;
   private boolean internalField1100;
   private long internalField1477;
   private final Stopwatch internalField1189;
   private final Stopwatch internalField1186;
   private BlockPos internalField1131;
   private BlockPos internalField1132;
   private int internalField1056;
   private int internalField1054;
   private double internalField0193;
   private double internalField1045;
   private final Set<BlockPos> internalField1200;
   private final Stopwatch internalField1188;
   private final Stopwatch internalField1187;
   private int internalField1464;
   private boolean internalField1102;
   private int internalField1470;
   private boolean internalField1101;
   private int internalField1465;
   private final Stopwatch internalField1550;
   private final Stopwatch internalField1557;
   private final Stopwatch internalField1556;
   private int internalField1463;
   private boolean internalField1516;
   private boolean internalField1517;
   private boolean internalField1512;
   private final Stopwatch internalField1555;
   private Item internalField0152;
   private int internalField1466;
   private int internalField1467;
   private final Stopwatch internalField1553;
   private final Set<Item> internalField1199;
   private boolean internalField1515;
   private int internalField1469;
   private int internalField1468;
   private int internalField1740;
   private boolean internalField1514;
   private final Stopwatch internalField1551;
   private final Stopwatch internalField1552;
   private final Stopwatch internalField1554;
   private final Stopwatch internalField1803;
   private final Stopwatch internalField1799;
   private int internalField1741;
   private boolean internalField1513;
   private final Stopwatch internalField1800;
   private final Stopwatch internalField1801;
   private final Stopwatch internalField1802;
   private final EventListener<ClientPlayerTickEvent> internalField0157;
   private final EventListener<ReceivePacketEvent> internalField0158;
   private static final ColorRGBA internalField0777 = new ColorRGBA(96.0F, 180.0F, 255.0F);
   private final EventListener<Render3DEvent> internalField1028;
   private final EventListener<HudRenderEvent> internalField1029;

   public MineFarmTask(AutoFarmModule localValue1, ModeSetting localValue2) {
      super(localValue1, localValue2, "modules.settings.auto_farm.modes.mine");
      this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
      this.internalField0416 = new ArrayList<>();
      this.internalField0519 = new Stopwatch();
      this.internalField1053 = -1;
      this.internalField0518 = new Stopwatch();
      this.internalField1189 = new Stopwatch();
      this.internalField1186 = new Stopwatch();
      this.internalField1200 = new HashSet<>();
      this.internalField1188 = new Stopwatch();
      this.internalField1187 = new Stopwatch();
      this.internalField1464 = -1;
      this.internalField1550 = new Stopwatch();
      this.internalField1557 = new Stopwatch();
      this.internalField1556 = new Stopwatch();
      this.internalField1463 = -1;
      this.internalField1555 = new Stopwatch();
      this.internalField1553 = new Stopwatch();
      this.internalField1199 = new HashSet<>();
      this.internalField1551 = new Stopwatch();
      this.internalField1552 = new Stopwatch();
      this.internalField1554 = new Stopwatch();
      this.internalField1803 = new Stopwatch();
      this.internalField1799 = new Stopwatch();
      this.internalField1741 = -1;
      this.internalField1800 = new Stopwatch();
      this.internalField1801 = new Stopwatch();
      this.internalField1802 = new Stopwatch();
      this.internalField0157 = localValue1x -> {
         if (internalField0149.player != null && internalField0149.world != null) {
            if (!ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
               this.internalMethod05961("modules.mine_farm.not_funtime");
               this.internalMethod08387();
            } else {
               this.internalMethod09356();
               if (!this.internalMethod02277()) {
                  switch (this.internalField0739) {
                     case internalField0739:
                        this.internalMethod09366();
                        break;
                     case internalField0740:
                        this.internalMethod09368();
                        break;
                     case internalField1289:
                        this.internalMethod09552();
                        break;
                     case internalField1290:
                        this.internalMethod09568();
                        break;
                     case internalField1291:
                        this.internalMethod10133();
                        break;
                     case internalField1292:
                        this.internalMethod10134();
                        break;
                     case internalField1599:
                        this.internalMethod09959();
                        break;
                     case internalField1597:
                        this.internalMethod09960();
                        break;
                     case internalField1603:
                        this.internalMethod10089();
                        break;
                     case internalField1602:
                        this.internalMethod10097();
                        break;
                     case internalField1601:
                        this.internalMethod10135();
                        break;
                     case internalField1600:
                        this.internalMethod10136();
                        break;
                     case internalField1598:
                        this.internalMethod10137();
                  }
               }
            }
         }
      };
      this.internalField0158 = localValue1x -> {
         if (localValue1x.getPacket() instanceof GameMessageS2CPacket localValue2x) {
            String localValue4 = localValue2x.content().getString().toLowerCase();
            if (localValue4.contains("\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432")) {
               this.internalField1516 = false;
               this.internalField1517 = false;
            } else if (localValue4.contains("\u043f\u0440\u043e\u0434\u0430\u0432\u0430\u0442\u044c")) {
               if (localValue4.contains("\u0432\u0441\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b")
                  || localValue4.contains("\u0432\u0441\u0451 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b")) {
                  this.internalField1516 = true;
                  this.internalField1517 = false;
               } else if (localValue4.contains("\u043f\u0440\u043e\u0434\u0430\u0432\u0430\u0442\u044c \u043f\u043e")) {
                  this.internalField1516 = false;
                  this.internalField1517 = false;
               }
            }
         }
      };
      this.internalField1028 = localValue1x -> {
         if (internalField0149.world != null && internalField0149.player != null) {
            if (this.internalField0739 == MineFarmTask.InternalType0366.internalField1289
               || this.internalField0739 == MineFarmTask.InternalType0366.internalField1290) {
               MatrixStack localValue2x = localValue1x.getMatrices();
               Camera localValue3 = internalField0149.gameRenderer.getCamera();
               Vec3d localValue4 = localValue3.getCameraPos();
               Box localValue5 = new Box(
                     internalField0352.getX(),
                     internalField0352.getY(),
                     internalField0352.getZ(),
                     internalField0351.getX() + 1,
                     internalField0351.getY() + 1,
                     internalField0351.getZ() + 1
                  )
                  .offset(-localValue4.x, -localValue4.y, -localValue4.z);
               RenderSystem.enableBlend();
               RenderSystem.disableDepthTest();
               RenderSystem.disableCull();
               RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
               RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
               BufferBuilder localValue6 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
               Render3DUtils.internalMethod02535(localValue2x, localValue6, localValue5, internalField0777.mulAlpha(0.1F));
               HudRenderUtils.internalMethod05816(localValue6);
               BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
               Render3DUtils.internalMethod08795(localValue2x, localValue7, localValue5, internalField0777.mulAlpha(0.7F));
               HudRenderUtils.internalMethod05816(localValue7);
               RenderSystem.defaultBlendFunc();
               RenderSystem.enableCull();
               RenderSystem.enableDepthTest();
               RenderSystem.disableBlend();
            }
         }
      };
      this.internalField1029 = localValue1x -> {
         if (this.internalField1264.internalMethod04496()) {
            if (internalField0149.player != null) {
               CustomDrawContext localValue2x = localValue1x.getContext();
               SizedFont localValue3 = Fonts.internalField1157.internalMethod01432(8.0F);
               SizedFont localValue4 = Fonts.internalField0449.internalMethod01432(7.0F);
               ArrayList localValue5 = new ArrayList<>(this.internalField0416);
               localValue5.sort(Comparator.comparingLong(this::internalMethod00496));
               int localValue6 = Math.min(localValue5.size(), (int)this.internalField1141.internalMethod08576());
               float localValue7 = 6.0F;
               float localValue8 = 44.0F;
               float localValue9 = 5.0F;
               float localValue10 = 11.0F;
               float localValue11 = 188.0F;
               float localValue12 = 13.0F;
               float localValue13 = 11.0F;
               float localValue14 = localValue9 * 2.0F + localValue12 + localValue13 + localValue6 * localValue10;
               localValue2x.drawRoundedRect(localValue7, localValue8, localValue11, localValue14, CornerRadii.internalMethod03908(5.0F), new ColorRGBA(14.0F, 14.0F, 16.0F).mulAlpha(0.78F));
               float localValue15 = localValue8 + localValue9;
               localValue2x.drawText(localValue3, "Mine Farm", localValue7 + localValue9, localValue15, ThemeColors.internalMethod08459());
               localValue15 += localValue12;
               String localValue16 = this.internalMethod09089();
               localValue2x.drawText(localValue4, localValue16, localValue7 + localValue9, localValue15, ThemeColors.internalMethod02531());
               localValue15 += localValue13;

               for (int localValue17 = 0; localValue17 < localValue6; localValue17++) {
                  FunTimeMineInfo localValue18 = (FunTimeMineInfo)localValue5.get(localValue17);
                  boolean localValue19 = this.internalField0239 != null && internalMethod07652(localValue18.internalMethod06462()) == this.internalField1053;
                  ColorRGBA localValue20 = localValue19 ? ThemeColors.internalMethod02531() : ThemeColors.internalMethod08459();
                  String localValue21 = localValue18.internalMethod02961()
                     + "  "
                     + internalMethod00178(localValue18.internalMethod09125())
                     + "\u2192"
                     + internalMethod00178(localValue18.internalMethod08068());
                  localValue2x.drawText(localValue4, localValue21, localValue7 + localValue9, localValue15, localValue20);
                  localValue2x.drawRightText(localValue4, internalMethod03746(this.internalMethod00496(localValue18)), localValue7 + localValue11 - localValue9, localValue15, localValue20);
                  localValue15 += localValue10;
               }
            }
         }
      };
      this.internalField0675 = new MultiSelectSetting(localValue1, "modules.settings.mine_farm.rarities", () -> !this.isSelected());
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.mine_farm.rarities.default").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.mine_farm.rarities.legendary").select();
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.mine_farm.rarities.mythical").select();
      this.internalField0650 = new BooleanSetting(localValue1, "modules.settings.mine_farm.dig_all", () -> !this.isSelected());
      this.internalField0674 = new MultiSelectSetting(
         localValue1, "modules.settings.mine_farm.ore_types", () -> !this.isSelected() || this.internalField0650.internalMethod04496()
      );
      this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.mine_farm.ore.diamond").select();
      this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.mine_farm.ore.lapis");
      this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.mine_farm.ore.redstone");
      this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.mine_farm.ore.iron");
      this.internalField1488 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.mine_farm.ore.gold").select();
      this.internalField1490 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.mine_farm.ore.debris").select();
      this.internalField0651 = new BooleanSetting(localValue1, "modules.settings.mine_farm.clean_inventory", () -> !this.isSelected()).internalMethod06630();
      this.internalField1261 = new BooleanSetting(localValue1, "modules.settings.mine_farm.drop_except", () -> !this.isSelected());
      this.internalField1276 = new MultiSelectSetting(
         localValue1, "modules.settings.mine_farm.keep_only", () -> !this.isSelected() || !this.internalField1261.internalMethod04496()
      );
      this.internalField1489 = new MultiSelectSetting.InternalType0091(this.internalField1276, "modules.settings.mine_farm.keep.diamond").select();
      this.internalField1493 = new MultiSelectSetting.InternalType0091(this.internalField1276, "modules.settings.mine_farm.keep.debris").select();
      this.internalField1487 = new MultiSelectSetting.InternalType0091(this.internalField1276, "modules.settings.mine_farm.keep.iron").select();
      this.internalField1486 = new MultiSelectSetting.InternalType0091(this.internalField1276, "modules.settings.mine_farm.keep.gold").select();
      this.internalField1263 = new BooleanSetting(localValue1, "modules.settings.mine_farm.auto_sell", () -> !this.isSelected());
      this.internalField0384 = new TextSetting(
            localValue1, "modules.settings.mine_farm.sell_anarchy", () -> !this.isSelected() || !this.internalField1263.internalMethod04496()
         )
         .internalMethod07009(true)
         .internalMethod00011("");
      this.internalField0383 = new SliderSetting(
            localValue1, "modules.settings.mine_farm.sell_threshold", () -> !this.isSelected() || !this.internalField1263.internalMethod04496()
         )
         .internalMethod08673(1.0F)
         .internalMethod05900(1.0F)
         .internalMethod02732(36.0F)
         .internalMethod08074(10.0F)
         .internalMethod06240(" \u0441\u0442");
      this.internalField1262 = new BooleanSetting(localValue1, "modules.settings.mine_farm.auto_repair", () -> !this.isSelected()).internalMethod06630();
      this.internalField0382 = new SliderSetting(
            localValue1, "modules.settings.mine_farm.repair_threshold", () -> !this.isSelected() || !this.internalField1262.internalMethod04496()
         )
         .internalMethod08673(1.0F)
         .internalMethod05900(1.0F)
         .internalMethod02732(50.0F)
         .internalMethod08074(10.0F)
         .internalMethod06240("%");
      this.internalField1142 = new SliderSetting(
            localValue1, "modules.settings.mine_farm.repair_until", () -> !this.isSelected() || !this.internalField1262.internalMethod04496()
         )
         .internalMethod08673(1.0F)
         .internalMethod05900(20.0F)
         .internalMethod02732(100.0F)
         .internalMethod08074(90.0F)
         .internalMethod06240("%");
      this.internalField1140 = new SliderSetting(
            localValue1, "modules.settings.mine_farm.bottle_target", () -> !this.isSelected() || !this.internalField1262.internalMethod04496()
         )
         .internalMethod08673(1.0F)
         .internalMethod05900(1.0F)
         .internalMethod02732(128.0F)
         .internalMethod08074(64.0F)
         .internalMethod06240(" \u0448\u0442");
      this.internalField1264 = new BooleanSetting(localValue1, "modules.settings.mine_farm.show_hud", () -> !this.isSelected()).internalMethod06630();
      this.internalField1141 = new SliderSetting(
            localValue1, "modules.settings.mine_farm.hud_count", () -> !this.isSelected() || !this.internalField1264.internalMethod04496()
         )
         .internalMethod08673(1.0F)
         .internalMethod05900(3.0F)
         .internalMethod02732(15.0F)
         .internalMethod08074(8.0F);
   }

   @Override
   public void internalMethod04694() {
      if (internalField0149.player == null || internalField0149.world == null) {
         this.internalMethod08387();
      } else if (!ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
         this.internalMethod05961("modules.mine_farm.not_funtime");
         this.internalMethod08387();
      } else {
         this.internalMethod08276();
         this.internalMethod08292();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
      }
   }

   @Override
   public void internalMethod04697() {
      this.internalMethod09354();
      this.internalMethod10032();
      this.internalMethod08276();
      this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
   }

   private void internalMethod08276() {
      this.internalField0239 = null;
      this.internalField1053 = -1;
      this.internalField0277 = false;
      this.internalField1055 = 0;
      this.internalField0276 = false;
      this.internalField1099 = false;
      this.internalField1100 = false;
      this.internalField1464 = -1;
      this.internalField1515 = false;
      this.internalField1740 = 0;
      this.internalField1514 = false;
      this.internalField1513 = false;
      this.internalField1741 = -1;
   }

   private void internalMethod08292() {
      long localValue1 = ++this.internalField1057;
      this.internalField0519.internalMethod00701();
      Thread localValue3 = new Thread(() -> {
         EventApiClient localValue3x = new EventApiClient("8789bb5c493b3d128719e8dfd48d32a4");

         while (this.internalField1057 == localValue1) {
            try {
               List localValue4 = localValue3x.internalMethod02513();
               if (localValue4 != null) {
                  this.internalField0416 = localValue4;
                  this.internalField1060 = System.currentTimeMillis();
                  this.internalField1077 = null;
               }
            } catch (Throwable localValue6) {
               this.internalField1077 = localValue6.getMessage();
            }

            try {
               Thread.sleep(5000L);
            } catch (InterruptedException localValue5) {
            }
         }
      }, "MineFarm-API");
      localValue3.setDaemon(true);
      this.internalField0380 = localValue3;
      localValue3.start();
   }

   private void internalMethod09354() {
      this.internalField1057++;
      if (this.internalField0380 != null) {
         this.internalField0380.interrupt();
         this.internalField0380 = null;
      }
   }

   private void internalMethod09356() {
      boolean localValue1 = this.internalField0380 == null || !this.internalField0380.isAlive();
      boolean localValue2 = this.internalField1060 > 0L && System.currentTimeMillis() - this.internalField1060 > 10000L;
      if ((localValue1 || localValue2) && this.internalField0519.internalMethod02365(5000L)) {
         this.internalMethod08292();
      }
   }

   private long internalMethod00496(FunTimeMineInfo localValue1) {
      long localValue2 = (System.currentTimeMillis() - this.internalField1060) / 1000L;
      return Math.max(0L, localValue1.internalMethod05251() - localValue2);
   }

   private boolean internalMethod07653(String localValue1) {
      if (localValue1 == null) {
         return true;
      } else {
         String localValue2 = localValue1.toLowerCase();

         return switch (localValue2) {
            case "default" -> this.internalField0245.isSelected();
            case "legendary" -> this.internalField0244.isSelected();
            case "mythical" -> this.internalField1075.isSelected();
            default -> true;
         };
      }
   }

   private static int internalMethod07652(String localValue0) {
      if (localValue0 == null) {
         return -1;
      } else {
         String localValue1 = localValue0.replaceAll("[^0-9]", "");
         if (localValue1.isEmpty()) {
            return -1;
         } else {
            try {
               return Integer.parseInt(localValue1);
            } catch (NumberFormatException localValue3) {
               return -1;
            }
         }
      }
   }

   private FunTimeMineInfo internalMethod04673() {
      ArrayList localValue1 = new ArrayList<>(this.internalField0416);
      localValue1.sort(Comparator.comparingLong(this::internalMethod00496));
      int localValue2 = ServerUtils.internalField0228;
      long localValue3 = 10L;

      for (FunTimeMineInfo localValue6 : (Iterable<FunTimeMineInfo>)(Iterable<?>)localValue1) {
         if (this.internalMethod07653(localValue6.internalMethod08068())) {
            int localValue7 = internalMethod07652(localValue6.internalMethod06462());
            if (localValue7 > 0 && localValue7 <= 1000 && (localValue7 == localValue2 || this.internalMethod00496(localValue6) >= localValue3)) {
               return localValue6;
            }
         }
      }

      return null;
   }

   private void internalMethod09366() {
      this.internalMethod10039();
      if (this.internalField1188.internalMethod02365(500L)) {
         this.internalField1188.internalMethod00701();
         if (this.internalMethod09355() && this.internalMethod09357()) {
            this.internalMethod10041();
         } else if (!this.internalMethod02282()) {
            FunTimeMineInfo localValue1 = this.internalMethod04673();
            if (localValue1 != null) {
               this.internalField0239 = localValue1;
               this.internalField1053 = internalMethod07652(localValue1.internalMethod06462());
               if (this.internalField1053 <= 0) {
                  this.internalField0239 = null;
               } else {
                  if (ServerUtils.internalField0228 == this.internalField1053) {
                     this.internalMethod09551();
                  } else {
                     this.internalField0277 = false;
                     this.internalField1055 = 0;
                     this.internalField0518.internalMethod00701();
                     this.internalField0739 = MineFarmTask.InternalType0366.internalField0740;
                  }
               }
            }
         }
      }
   }

   private void internalMethod09368() {
      if (this.internalField0239 == null) {
         this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
      } else if (ServerUtils.internalField0228 == this.internalField1053) {
         this.internalMethod09551();
      } else {
         if (!this.internalField0277 || this.internalField0518.internalMethod02365(25000L)) {
            internalField0149.player.networkHandler.sendChatCommand("an" + this.internalField1053);
            this.internalField0277 = true;
            this.internalField0518.internalMethod00701();
            this.internalField1055++;
            if (this.internalField1055 > 6) {
               this.internalField0239 = null;
               this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
               this.internalField1188.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod09551() {
      this.internalField0276 = false;
      this.internalField1473 = System.currentTimeMillis();
      this.internalField1055 = 0;
      this.internalField0739 = MineFarmTask.InternalType0366.internalField1289;
   }

   private void internalMethod09552() {
      if (this.internalField0239 == null) {
         this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
      } else if (ServerUtils.internalField0228 != this.internalField1053) {
         this.internalField0277 = false;
         this.internalField0518.internalMethod00701();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField0740;
      } else if (this.internalMethod02275() <= 12.0) {
         this.internalMethod09567();
      } else {
         if (!this.internalField0276 || System.currentTimeMillis() - this.internalField1473 > 8000L) {
            internalField0149.player.networkHandler.sendChatCommand("warp mine");
            this.internalField0276 = true;
            this.internalField1473 = System.currentTimeMillis();
            this.internalField1055++;
            if (this.internalField1055 > 8) {
               this.internalField0239 = null;
               this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
               this.internalField1188.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod09567() {
      this.internalField1099 = false;
      this.internalField1100 = this.internalMethod08293();
      this.internalField1186.internalMethod00701();
      this.internalMethod10131();
      this.internalField1200.clear();
      this.internalField1131 = null;
      long localValue1 = this.internalField0239 != null ? this.internalMethod00496(this.internalField0239) : 0L;
      this.internalField1477 = Math.max(30000L, Math.min(300000L, (localValue1 + 45L) * 1000L));
      this.internalField1189.internalMethod00701();
      this.internalField0739 = MineFarmTask.InternalType0366.internalField1290;
   }

   private void internalMethod09568() {
      this.internalMethod10032();
      if (ServerUtils.internalField0228 != this.internalField1053) {
         this.internalField0277 = false;
         this.internalField0518.internalMethod00701();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField0740;
      } else if (this.internalMethod09355() && this.internalMethod09357()) {
         this.internalMethod10041();
      } else if (this.internalMethod02282()) {
         this.internalMethod10032();
      } else {
         if (!this.internalField1100 && this.internalMethod08293()) {
            this.internalField1100 = true;
         }

         List localValue1 = this.internalMethod05427();
         int localValue2 = localValue1.size();
         if (localValue2 == 0) {
            if (this.internalField1100) {
               this.internalMethod10132();
            } else {
               this.internalMethod10039();
               if (this.internalField1189.internalMethod02365(this.internalField1477)) {
                  this.internalField0239 = null;
                  this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
                  this.internalField1188.internalMethod00701();
               }
            }
         } else if (this.internalField1186.internalMethod02365(25000L)) {
            this.internalMethod10132();
         } else {
            this.internalMethod10034();
            double localValue3 = internalField0149.player.getBlockInteractionRange();
            double localValue5 = (localValue3 + 0.6) * (localValue3 + 0.6);
            int localValue7 = internalField0149.player.getBlockY();
            BlockPos localValue8 = null;
            if (this.internalField1131 != null && !this.internalField1200.contains(this.internalField1131) && this.internalMethod07536(this.internalField1131)) {
               localValue8 = this.internalField1131;
            } else {
               for (BlockPos localValue10 : (Iterable<BlockPos>)(Iterable<?>)localValue1) {
                  if (!this.internalField1200.contains(localValue10)) {
                     localValue8 = localValue10;
                     break;
                  }
               }

               this.internalField1131 = localValue8;
            }

            if (localValue8 == null) {
               this.internalMethod10132();
            } else {
               Direction localValue11 = this.internalMethod06197(localValue8) <= localValue5 ? this.internalMethod06290(localValue8, localValue3) : null;
               if (localValue11 != null) {
                  this.internalMethod10131();
                  if (this.internalMethod02011(localValue8, this.internalMethod06097(localValue8, localValue11))) {
                     internalField0149.interactionManager.updateBlockBreakingProgress(localValue8, localValue11);
                     internalField0149.player.swingHand(Hand.MAIN_HAND);
                     this.internalField1186.internalMethod00701();
                  }

                  this.internalField1099 = true;
                  this.internalMethod10039();
               } else {
                  int localValue12 = localValue8.getY() - localValue7;
                  if (localValue12 > 1) {
                     if (localValue12 > localValue3) {
                        this.internalField1200.add(localValue8);
                        this.internalField1131 = null;
                        this.internalMethod10039();
                        return;
                     }

                     if (Math.sqrt(this.internalMethod06197(localValue8)) <= localValue3 + 2.5) {
                        if (!this.internalMethod02910(localValue8, localValue3)) {
                           this.internalField1200.add(localValue8);
                           this.internalField1131 = null;
                        }

                        this.internalMethod10039();
                        return;
                     }
                  }

                  this.internalMethod02909(localValue8, localValue3);
                  this.internalMethod10039();
               }
            }
         }
      }
   }

   private boolean internalMethod02910(BlockPos localValue1, double localValue2) {
      Vec3d localValue4 = internalField0149.player.getEyePos();
      Vec3d localValue5 = Vec3d.ofCenter(localValue1);
      Vec3d localValue6 = localValue5.subtract(localValue4);
      double localValue7 = localValue6.length();
      Vec3d localValue9 = localValue7 <= localValue2 ? localValue5 : localValue4.add(localValue6.multiply(localValue2 / Math.max(localValue7, 1.0E-4)));
      BlockHitResult localValue10 = internalField0149.world.raycast(new RaycastContext(localValue4, localValue9, ShapeType.OUTLINE, FluidHandling.NONE, internalField0149.player));
      if (localValue10.getType() != Type.BLOCK) {
         return false;
      } else {
         BlockPos localValue11 = localValue10.getBlockPos();
         if (!this.internalMethod06198(localValue11)) {
            return false;
         } else if (this.internalMethod06197(localValue11) > (localValue2 + 0.6) * (localValue2 + 0.6)) {
            return false;
         } else {
            if (this.internalMethod02011(localValue11, localValue10.getPos())) {
               internalField0149.interactionManager.updateBlockBreakingProgress(localValue11, localValue10.getSide());
               internalField0149.player.swingHand(Hand.MAIN_HAND);
               this.internalField1186.internalMethod00701();
            }

            this.internalField1099 = true;
            return true;
         }
      }
   }

   private void internalMethod02909(BlockPos localValue1, double localValue2) {
      Vec3d localValue4 = internalField0149.player.getEyePos();
      int localValue5 = internalField0149.player.getBlockY();
      double localValue6 = localValue1.getX() + 0.5 - internalField0149.player.getX();
      double localValue8 = localValue1.getZ() + 0.5 - internalField0149.player.getZ();
      double localValue10 = Math.sqrt(localValue6 * localValue6 + localValue8 * localValue8);
      float localValue12 = (float)(Math.toDegrees(Math.atan2(localValue8, localValue6)) - 90.0);
      double localValue13 = (localValue2 + 0.6) * (localValue2 + 0.6);
      BlockPos localValue15 = null;
      Direction localValue16 = null;
      Vec3d localValue17 = null;
      if (localValue10 > 1.0E-4) {
         Vec3d localValue18 = new Vec3d(localValue6 / localValue10, 0.0, localValue8 / localValue10);
         BlockHitResult localValue19 = internalField0149.world
            .raycast(new RaycastContext(localValue4, localValue4.add(localValue18.multiply(localValue2)), ShapeType.OUTLINE, FluidHandling.NONE, internalField0149.player));
         if (localValue19.getType() == Type.BLOCK
            && this.internalMethod06198(localValue19.getBlockPos())
            && localValue4.squaredDistanceTo(Vec3d.ofCenter(localValue19.getBlockPos())) <= localValue13) {
            localValue15 = localValue19.getBlockPos();
            localValue16 = localValue19.getSide();
            localValue17 = localValue19.getPos();
         }
      }

      boolean localValue23 = false;
      if (localValue15 == null && localValue1.getY() < localValue5 - 1 && localValue10 < 1.4) {
         BlockPos localValue24 = internalField0149.player.getBlockPos().down();
         if (this.internalMethod06198(localValue24)) {
            localValue15 = localValue24;
            localValue16 = Direction.UP;
            localValue17 = Vec3d.ofCenter(localValue24);
            localValue23 = true;
         }
      }

      if (localValue17 == null && localValue10 > 0.5) {
         internalField0149.player.setYaw(localValue12);
         internalField0149.player.setPitch(0.0F);
         internalField0149.player.setHeadYaw(localValue12);
         internalField0149.player.setBodyYaw(localValue12);
      }

      if (!localValue23 && localValue10 > 0.5) {
         internalField0149.options.forwardKey.setPressed(true);
         internalField0149.options.sprintKey.setPressed(true);
      }

      if (localValue15 != null && this.internalMethod02011(localValue15, localValue17)) {
         internalField0149.interactionManager.updateBlockBreakingProgress(localValue15, localValue16);
         internalField0149.player.swingHand(Hand.MAIN_HAND);
         this.internalField1186.internalMethod00701();
      }

      double localValue25 = internalField0149.player.getX();
      double localValue21 = internalField0149.player.getZ();
      if ((localValue25 - this.internalField0193) * (localValue25 - this.internalField0193) + (localValue21 - this.internalField1045) * (localValue21 - this.internalField1045) > 0.0225) {
         this.internalField0193 = localValue25;
         this.internalField1045 = localValue21;
         this.internalField1054 = 0;
      } else {
         this.internalField1054++;
      }

      if (this.internalField1054 >= 8 && localValue15 == null && internalField0149.player.isOnGround()) {
         internalField0149.options.jumpKey.setPressed(true);
         this.internalField1054 = 0;
      }

      this.internalField1099 = true;
   }

   private boolean internalMethod02277() {
      if (internalField0149.player != null && internalField0149.world != null) {
         BlockPos localValue1 = BlockPos.ofFloored(internalField0149.player.getEyePos());
         BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
         if (!localValue2.shouldSuffocate(internalField0149.world, localValue1)) {
            this.internalField1802.internalMethod00701();
            return false;
         } else {
            this.internalMethod10032();
            this.internalMethod10034();
            Block localValue3 = localValue2.getBlock();
            boolean localValue4 = localValue3 != Blocks.BEDROCK && localValue3 != Blocks.BARRIER && localValue2.getFluidState().isEmpty();
            if (localValue4) {
               this.internalMethod04544(Vec3d.ofCenter(localValue1));
               internalField0149.interactionManager.updateBlockBreakingProgress(localValue1, Direction.UP);
               internalField0149.player.swingHand(Hand.MAIN_HAND);
            }

            if (this.internalField1802.internalMethod02365(1500L)) {
               internalField0149.player.networkHandler.sendChatCommand("spawn");
               this.internalField1802.internalMethod00701();
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private boolean internalMethod06198(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      if (localValue2.isAir()) {
         return false;
      } else {
         Block localValue3 = localValue2.getBlock();
         return localValue3 != Blocks.BEDROCK && localValue3 != Blocks.BARRIER ? localValue2.getFluidState().isEmpty() : false;
      }
   }

   private void internalMethod10131() {
      this.internalField1054 = 0;
      if (internalField0149.player != null) {
         this.internalField0193 = internalField0149.player.getX();
         this.internalField1045 = internalField0149.player.getZ();
      }
   }

   private void internalMethod10132() {
      this.internalMethod10032();
      this.internalField0239 = null;
      this.internalField1053 = -1;
      this.internalField1099 = false;
      this.internalField1100 = false;
      this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
      this.internalField1188.internalMethod00701();
   }

   private boolean internalMethod02282() {
      if (!this.internalField1263.internalMethod04496()) {
         return false;
      } else {
         boolean localValue1 = this.internalMethod02012(Items.DIAMOND) >= (int)this.internalField0383.internalMethod08576() * 64;
         if (localValue1) {
            return this.internalMethod02972(false);
         } else {
            return this.internalMethod08278() <= 1 && this.internalMethod08277() ? this.internalMethod02972(true) : false;
         }
      }
   }

   private boolean internalMethod08277() {
      PlayerInventory localValue1 = internalField0149.player.getInventory();

      for (int localValue2 = 0; localValue2 < localValue1.size(); localValue2++) {
         Item localValue3 = localValue1.getStack(localValue2).getItem();
         if (localValue3 != Items.DIAMOND && internalField0546.contains(localValue3)) {
            return true;
         }
      }

      return false;
   }

   private boolean internalMethod02972(boolean localValue1) {
      int localValue2 = internalMethod07652(this.internalField0384.internalMethod08926());
      if (localValue2 > 0 && localValue2 <= 1000) {
         this.internalField1464 = localValue2;
         this.internalField1512 = localValue1;
         this.internalField1102 = false;
         this.internalField1470 = 0;
         this.internalField1101 = false;
         this.internalField1465 = 0;
         this.internalField0518.internalMethod00701();
         this.internalField1557.internalMethod00701();
         this.internalField1550.internalMethod00701();
         this.internalField1556.internalMethod00701();
         this.internalField1463 = -1;
         this.internalField1516 = false;
         this.internalField1517 = false;
         this.internalField1555.internalMethod00701();
         this.internalField0152 = null;
         this.internalField1467 = 0;
         this.internalField1553.internalMethod00701();
         this.internalField1199.clear();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField1291;
         return true;
      } else {
         return false;
      }
   }

   private void internalMethod10133() {
      if (this.internalField1464 <= 0) {
         this.internalMethod09961();
      } else if (ServerUtils.internalField0228 == this.internalField1464) {
         this.internalField1101 = false;
         this.internalField1465 = 0;
         this.internalField1557.internalMethod00701();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField1292;
      } else {
         if (!this.internalField1102 || this.internalField0518.internalMethod02365(25000L)) {
            internalField0149.player.networkHandler.sendChatCommand("an" + this.internalField1464);
            this.internalField1102 = true;
            this.internalField0518.internalMethod00701();
            this.internalField1470++;
            if (this.internalField1470 > 6) {
               this.internalMethod09961();
            }
         }
      }
   }

   private void internalMethod10134() {
      if (ServerUtils.internalField0228 != this.internalField1464) {
         this.internalField1102 = false;
         this.internalField0739 = MineFarmTask.InternalType0366.internalField1291;
      } else if (!this.internalMethod08279() && !this.internalMethod08291()) {
         if (!this.internalField1101 || this.internalField1557.internalMethod02365(4000L)) {
            String localValue1 = "buyer";
            if (localValue1 == null || localValue1.isBlank()) {
               localValue1 = "buyer";
            }

            if (localValue1.startsWith("/")) {
               localValue1 = localValue1.substring(1);
            }

            internalField0149.player.networkHandler.sendChatCommand(localValue1);
            this.internalField1101 = true;
            this.internalField1557.internalMethod00701();
            this.internalField1465++;
            if (this.internalField1465 > 6) {
               this.internalMethod09961();
            }
         }
      } else {
         this.internalField1557.internalMethod00701();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField1599;
      }
   }

   private void internalMethod09959() {
      if (this.internalMethod08279()) {
         this.internalField0739 = MineFarmTask.InternalType0366.internalField1597;
         this.internalField1557.internalMethod00701();
         this.internalField1550.internalMethod00701();
         this.internalField1556.internalMethod00701();
         this.internalField1463 = -1;
      } else if (this.internalMethod08291()) {
         if (this.internalField1550.internalMethod02365(350L)) {
            Slot localValue1 = this.internalMethod04622(Items.LAPIS_LAZULI);
            if (localValue1 != null) {
               this.internalMethod02118(localValue1.id, 0);
               this.internalField1550.internalMethod00701();
            }
         }

         if (this.internalField1557.internalMethod02365(8000L)) {
            this.internalMethod09961();
         }
      } else {
         if (this.internalField1557.internalMethod02365(2500L)) {
            this.internalField1101 = false;
            this.internalField0739 = MineFarmTask.InternalType0366.internalField1292;
         }
      }
   }

   private void internalMethod09960() {
      if (!this.internalMethod08279()) {
         if (this.internalField1557.internalMethod02365(2000L)) {
            this.internalField1101 = false;
            this.internalField0739 = MineFarmTask.InternalType0366.internalField1292;
         }
      } else {
         int localValue1 = this.internalMethod02276();
         if (localValue1 == 0) {
            this.internalMethod09961();
         } else {
            if (this.internalField1463 < 0 || localValue1 < this.internalField1463) {
               this.internalField1463 = localValue1;
               this.internalField1556.internalMethod00701();
            }

            if (this.internalField1556.internalMethod02365(15000L)) {
               this.internalMethod09961();
            } else if (this.internalField1550.internalMethod02365(250L)) {
               Slot localValue2 = this.internalMethod00423();
               if (localValue2 == null) {
                  this.internalMethod09961();
               } else {
                  if (!this.internalField1516) {
                     if (!this.internalMethod01727(localValue2.getStack())) {
                        if (this.internalField1517 && !this.internalField1555.internalMethod02365(2000L)) {
                           return;
                        }

                        this.internalMethod02118(localValue2.id, 1);
                        this.internalField1517 = true;
                        this.internalField1555.internalMethod00701();
                        this.internalField1550.internalMethod00701();
                        return;
                     }

                     this.internalField1516 = true;
                  }

                  Item localValue3 = localValue2.getStack().getItem();
                  int localValue4 = this.internalMethod02012(localValue3);
                  if (localValue3 == this.internalField0152) {
                     if (localValue4 >= this.internalField1466) {
                        if (!this.internalField1553.internalMethod02365(1200L)) {
                           return;
                        }

                        this.internalField1467++;
                        this.internalField1553.internalMethod00701();
                        if (this.internalField1467 >= 3) {
                           this.internalField1199.add(localValue3);
                           this.internalField0152 = null;
                           this.internalField1467 = 0;
                        }

                        return;
                     }

                     this.internalField0152 = null;
                     this.internalField1467 = 0;
                  }

                  this.internalMethod02118(localValue2.id, 0);
                  this.internalField0152 = localValue3;
                  this.internalField1466 = localValue4;
                  this.internalField1553.internalMethod00701();
                  this.internalField1550.internalMethod00701();
               }
            }
         }
      }
   }

   private void internalMethod09961() {
      this.internalMethod09962();
      this.internalField1464 = -1;
      this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
      this.internalField1188.internalMethod00701();
   }

   private void internalMethod09962() {
      if (internalField0149.player != null && internalField0149.player.currentScreenHandler != internalField0149.player.playerScreenHandler) {
         internalField0149.player.closeHandledScreen();
      }
   }

   private String internalMethod01469() {
      return internalField0149.currentScreen instanceof HandledScreen localValue1 ? localValue1.getTitle().getString().toLowerCase() : "";
   }

   private boolean internalMethod08279() {
      return this.internalMethod01469().contains("\u043a\u0443\u043f\u0449\u0438\u043a");
   }

   private boolean internalMethod08291() {
      return this.internalMethod01469().contains("\u0441\u0435\u043a\u0446\u0438");
   }

   private Slot internalMethod04622(Item localValue1) {
      if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue2) {
         for (Slot localValue4 : localValue2.slots) {
            if (localValue4.inventory != internalField0149.player.getInventory() && localValue4.getStack().getItem() == localValue1) {
               return localValue4;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private Slot internalMethod00423() {
      if (!(internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1)) {
         return null;
      } else {
         for (Slot localValue3 : localValue1.slots) {
            if (localValue3.inventory != internalField0149.player.getInventory() && localValue3.hasStack()) {
               ItemStack localValue4 = localValue3.getStack();
               if ((!this.internalField1512 || localValue4.getItem() != Items.DIAMOND)
                  && !this.internalField1199.contains(localValue4.getItem())
                  && this.internalMethod02012(localValue4.getItem()) > 0
                  && this.internalMethod04638(localValue4)) {
                  return localValue3;
               }
            }
         }

         return null;
      }
   }

   private boolean internalMethod04638(ItemStack localValue1) {
      LoreComponent localValue2 = (LoreComponent)localValue1.get(DataComponentTypes.LORE);
      if (localValue2 == null) {
         return false;
      } else {
         for (Text localValue4 : localValue2.lines()) {
            String localValue5 = localValue4.getString().toLowerCase();
            if (localValue5.contains("\u043f\u0440\u043e\u0434\u0430\u0442\u044c") || localValue5.contains("\u0446\u0435\u043d\u0430 \u0437\u0430")) {
               return true;
            }
         }

         return false;
      }
   }

   private int internalMethod02276() {
      if (!(internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1)) {
         return 0;
      } else {
         HashSet localValue6 = new HashSet();

         for (Slot localValue4 : localValue1.slots) {
            if (localValue4.inventory != internalField0149.player.getInventory() && localValue4.hasStack()) {
               ItemStack localValue5 = localValue4.getStack();
               if ((!this.internalField1512 || localValue5.getItem() != Items.DIAMOND) && !this.internalField1199.contains(localValue5.getItem()) && this.internalMethod04638(localValue5)) {
                  localValue6.add(localValue5.getItem());
               }
            }
         }

         int localValue7 = 0;

         for (Item localValue9 : (Iterable<Item>)(Iterable<?>)localValue6) {
            localValue7 += this.internalMethod02012(localValue9);
         }

         return localValue7;
      }
   }

   private void internalMethod02118(int localValue1, int localValue2) {
      if (internalField0149.player.currentScreenHandler != null && internalField0149.interactionManager != null) {
         internalField0149.interactionManager
            .clickSlot(internalField0149.player.currentScreenHandler.syncId, localValue1, localValue2, SlotActionType.PICKUP, internalField0149.player);
      }
   }

   private int internalMethod02012(Item localValue1) {
      int localValue2 = 0;
      PlayerInventory localValue3 = internalField0149.player.getInventory();

      for (int localValue4 = 0; localValue4 < localValue3.size(); localValue4++) {
         ItemStack localValue5 = localValue3.getStack(localValue4);
         if (localValue5.getItem() == localValue1) {
            localValue2 += localValue5.getCount();
         }
      }

      return localValue2;
   }

   private int internalMethod02281() {
      int localValue1 = 0;
      PlayerInventory localValue2 = internalField0149.player.getInventory();

      for (int localValue3 = 0; localValue3 < localValue2.size(); localValue3++) {
         ItemStack localValue4 = localValue2.getStack(localValue3);
         if (internalField0546.contains(localValue4.getItem())) {
            localValue1 += localValue4.getCount();
         }
      }

      return localValue1;
   }

   private boolean internalMethod01727(ItemStack localValue1) {
      LoreComponent localValue2 = (LoreComponent)localValue1.get(DataComponentTypes.LORE);
      if (localValue2 == null) {
         return false;
      } else {
         for (Text localValue4 : localValue2.lines()) {
            String localValue5 = localValue4.getString();
            String localValue6 = localValue5.toLowerCase();
            if (localValue6.contains("\u043f\u0440\u043e\u0434\u0430")
               && (localValue6.contains("\u0432\u0441\u0451") || localValue6.contains("\u0432\u0441\u0435"))
               && (localValue5.contains(">") || localValue5.contains("\u25b6") || localValue5.contains("\u27a4") || localValue5.contains("\u279c"))) {
               return true;
            }
         }

         return false;
      }
   }

   private Rotation internalMethod04544(Vec3d localValue1) {
      Vec3d localValue2 = internalField0149.player.getEyePos();
      double localValue3 = localValue1.x - localValue2.x;
      double localValue5 = localValue1.y - localValue2.y;
      double localValue7 = localValue1.z - localValue2.z;
      double localValue9 = Math.sqrt(localValue3 * localValue3 + localValue7 * localValue7);
      float localValue11 = (float)(Math.toDegrees(Math.atan2(localValue7, localValue3)) - 90.0);
      float localValue12 = MathHelper.clamp((float)(-Math.toDegrees(Math.atan2(localValue5, localValue9))), -90.0F, 90.0F);
      internalField0149.player.setYaw(localValue11);
      internalField0149.player.setPitch(localValue12);
      internalField0149.player.setHeadYaw(localValue11);
      internalField0149.player.setBodyYaw(localValue11);
      return new Rotation(localValue11, localValue12);
   }

   private boolean internalMethod02011(BlockPos localValue1, Vec3d localValue2) {
      Rotation localValue3 = this.internalMethod04544(localValue2);
      if (!localValue1.equals(this.internalField1132)) {
         this.internalField1132 = localValue1;
         this.internalField1056 = 0;
      }

      Rotation localValue4 = RockstarClient.getInstance().internalMethod02368().internalMethod08209();
      if (localValue4.internalMethod00735(localValue3) <= 1.5F) {
         this.internalField1056++;
         return this.internalField1056 >= 2;
      } else {
         return false;
      }
   }

   private void internalMethod10032() {
      if (internalField0149.options != null) {
         internalField0149.options.forwardKey.setPressed(false);
         internalField0149.options.backKey.setPressed(false);
         internalField0149.options.sprintKey.setPressed(false);
         internalField0149.options.jumpKey.setPressed(false);
      }
   }

   private void internalMethod10034() {
      ItemStack localValue1 = internalField0149.player.getMainHandStack();
      float localValue2 = LegacyItemTypes.isPickaxe(localValue1) ? localValue1.getMiningSpeedMultiplier(internalField0934) : -1.0F;
      HotbarSlot localValue3 = null;

      for (HotbarSlot localValue5 : InventorySlots.internalMethod02872().internalMethod02638()) {
         ItemStack localValue6 = localValue5.internalMethod03427();
         if (LegacyItemTypes.isPickaxe(localValue6)) {
            float localValue7 = localValue6.getMiningSpeedMultiplier(internalField0934);
            if (localValue7 > localValue2) {
               localValue2 = localValue7;
               localValue3 = localValue5;
            }
         }
      }

      if (localValue3 != null) {
         InventoryUtils.internalMethod01980(localValue3);
      }
   }

   private List<BlockPos> internalMethod05427() {
      ArrayList localValue1 = new ArrayList();

      for (int localValue2 = internalField0351.getY(); localValue2 >= internalField0352.getY(); localValue2--) {
         for (int localValue3 = internalField0352.getX(); localValue3 <= internalField0351.getX(); localValue3++) {
            for (int localValue4 = internalField0352.getZ(); localValue4 <= internalField0351.getZ(); localValue4++) {
               BlockPos localValue5 = new BlockPos(localValue3, localValue2, localValue4);
               if (this.internalMethod07536(localValue5)) {
                  localValue1.add(localValue5);
               }
            }
         }
      }

      localValue1.sort(Comparator.comparingDouble(this::internalMethod06197));
      return localValue1;
   }

   private boolean internalMethod07536(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      if (localValue2.isAir()) {
         return false;
      } else {
         Block localValue3 = localValue2.getBlock();
         if (localValue3 == Blocks.BEDROCK || localValue3 == Blocks.BARRIER) {
            return false;
         } else if (!localValue2.getFluidState().isEmpty()) {
            return false;
         } else {
            return this.internalField0650.internalMethod04496() ? true : this.internalMethod03596(localValue3);
         }
      }
   }

   private boolean internalMethod03596(Block localValue1) {
      if (!this.internalField1074.isSelected() || localValue1 != Blocks.DIAMOND_ORE && localValue1 != Blocks.DEEPSLATE_DIAMOND_ORE) {
         if (!this.internalField1073.isSelected() || localValue1 != Blocks.LAPIS_ORE && localValue1 != Blocks.DEEPSLATE_LAPIS_ORE) {
            if (!this.internalField1072.isSelected() || localValue1 != Blocks.REDSTONE_ORE && localValue1 != Blocks.DEEPSLATE_REDSTONE_ORE) {
               if (!this.internalField1491.isSelected() || localValue1 != Blocks.IRON_ORE && localValue1 != Blocks.DEEPSLATE_IRON_ORE) {
                  return !this.internalField1488.isSelected() || localValue1 != Blocks.GOLD_ORE && localValue1 != Blocks.DEEPSLATE_GOLD_ORE
                     ? this.internalField1490.isSelected() && localValue1 == Blocks.ANCIENT_DEBRIS
                     : true;
               } else {
                  return true;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private boolean internalMethod08293() {
      for (int localValue1 = internalField0351.getY(); localValue1 >= internalField0352.getY(); localValue1--) {
         for (int localValue2 = internalField0352.getX(); localValue2 <= internalField0351.getX(); localValue2++) {
            for (int localValue3 = internalField0352.getZ(); localValue3 <= internalField0351.getZ(); localValue3++) {
               BlockState localValue4 = internalField0149.world.getBlockState(new BlockPos(localValue2, localValue1, localValue3));
               if (!localValue4.isAir()) {
                  Block localValue5 = localValue4.getBlock();
                  if (localValue5 != Blocks.BEDROCK && localValue5 != Blocks.BARRIER && localValue4.getFluidState().isEmpty()) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private double internalMethod06197(BlockPos localValue1) {
      return internalField0149.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(localValue1));
   }

   private Direction internalMethod06290(BlockPos localValue1, double localValue2) {
      Vec3d localValue4 = internalField0149.player.getEyePos();
      double localValue5 = localValue2 * localValue2;
      Direction localValue7 = null;
      double localValue8 = Double.MAX_VALUE;

      for (Direction localValue13 : Direction.values()) {
         if (this.internalMethod08706(localValue1.offset(localValue13))) {
            Vec3d localValue14 = Vec3d.ofCenter(localValue1).add(localValue13.getOffsetX() * 0.49, localValue13.getOffsetY() * 0.49, localValue13.getOffsetZ() * 0.49);
            double localValue15 = localValue4.squaredDistanceTo(localValue14);
            if (!(localValue15 > localValue5) && !(localValue15 >= localValue8)) {
               BlockHitResult localValue17 = internalField0149.world
                  .raycast(new RaycastContext(localValue4, localValue14, ShapeType.OUTLINE, FluidHandling.NONE, internalField0149.player));
               if (localValue17.getType() == Type.BLOCK && localValue17.getBlockPos().equals(localValue1)) {
                  localValue7 = localValue13;
                  localValue8 = localValue15;
               }
            }
         }
      }

      return localValue7;
   }

   private boolean internalMethod08706(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      return !localValue2.isAir() && !localValue2.isReplaceable() ? localValue2.getCollisionShape(internalField0149.world, localValue1).isEmpty() : true;
   }

   private Vec3d internalMethod06097(BlockPos localValue1, Direction localValue2) {
      return Vec3d.ofCenter(localValue1).add(localValue2.getOffsetX() * 0.5, localValue2.getOffsetY() * 0.5, localValue2.getOffsetZ() * 0.5);
   }

   private double internalMethod02275() {
      double localValue1 = internalField0149.player.getX();
      double localValue3 = internalField0149.player.getZ();
      double localValue5 = MathHelper.clamp(localValue1, internalField0352.getX(), internalField0351.getX());
      double localValue7 = MathHelper.clamp(localValue3, internalField0352.getZ(), internalField0351.getZ());
      double localValue9 = localValue1 - localValue5;
      double localValue11 = localValue3 - localValue7;
      return Math.sqrt(localValue9 * localValue9 + localValue11 * localValue11);
   }

   private void internalMethod10039() {
      if (this.internalField0651.internalMethod04496() || this.internalField1261.internalMethod04496()) {
         if (internalField0149.player.currentScreenHandler != null) {
            double localValue1 = Math.hypot(internalField0149.player.getVelocity().x, internalField0149.player.getVelocity().z);
            boolean localValue3 = localValue1 > 0.06;
            boolean localValue4 = this.internalMethod08278() <= 2;
            if (!localValue3 || localValue4) {
               long localValue5 = localValue3 ? 150L : 0L;
               if (this.internalField1187.internalMethod02365(localValue5)) {
                  int localValue7 = localValue3 ? 1 : 3;
                  int localValue8 = 36 + internalField0149.player.getInventory().getSelectedSlot();
                  SlotCollection localValue9 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
                  int localValue10 = 0;

                  for (InventorySlot localValue12 : (Iterable<InventorySlot>)(Iterable<?>)localValue9.internalMethod02638()) {
                     if (localValue10 >= localValue7) {
                        break;
                     }

                     ItemStack localValue13 = localValue12.internalMethod03427();
                     if (!localValue13.isEmpty() && localValue12.internalMethod06662() != localValue8 && !this.internalMethod08740(localValue13)) {
                        internalField0149.interactionManager
                           .clickSlot(
                              internalField0149.player.currentScreenHandler.syncId, localValue12.internalMethod06662(), 1, SlotActionType.THROW, internalField0149.player
                           );
                        localValue10++;
                     }
                  }

                  if (localValue10 > 0) {
                     this.internalField1187.internalMethod00701();
                  }
               }
            }
         }
      }
   }

   private boolean internalMethod08740(ItemStack localValue1) {
      Item localValue2 = localValue1.getItem();
      if (LegacyItemTypes.isPickaxe(localValue1)
         || LegacyItemTypes.isSword(localValue1)
         || localValue2 instanceof AxeItem
         || localValue2 instanceof ShovelItem
         || localValue2 instanceof HoeItem
         || LegacyItemTypes.isArmor(localValue1)) {
         return true;
      } else if (localValue1.contains(DataComponentTypes.FOOD)) {
         return true;
      } else if (localValue2 == Items.EXPERIENCE_BOTTLE
         || localValue2 == Items.TOTEM_OF_UNDYING
         || localValue2 == Items.ELYTRA
         || localValue2 == Items.ENDER_PEARL
         || localValue2 == Items.ENDER_CHEST) {
         return true;
      } else {
         return this.internalField1261.internalMethod04496() ? this.internalMethod02013(localValue2) : internalField0545.contains(localValue2);
      }
   }

   private boolean internalMethod02013(Item localValue1) {
      if (!this.internalField1489.isSelected() || localValue1 != Items.DIAMOND && localValue1 != Items.DIAMOND_BLOCK) {
         if (!this.internalField1493.isSelected()
            || localValue1 != Items.ANCIENT_DEBRIS && localValue1 != Items.NETHERITE_SCRAP && localValue1 != Items.NETHERITE_INGOT && localValue1 != Items.NETHERITE_BLOCK) {
            return !this.internalField1487.isSelected()
                  || localValue1 != Items.RAW_IRON && localValue1 != Items.IRON_INGOT && localValue1 != Items.IRON_NUGGET && localValue1 != Items.IRON_BLOCK
               ? this.internalField1486.isSelected()
                  && (localValue1 == Items.RAW_GOLD || localValue1 == Items.GOLD_INGOT || localValue1 == Items.GOLD_NUGGET || localValue1 == Items.GOLD_BLOCK)
               : true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private boolean internalMethod09355() {
      if (!this.internalField1262.internalMethod04496()) {
         return false;
      } else if (this.internalField1513 && !this.internalField1801.internalMethod02365(300000L)) {
         return false;
      } else {
         this.internalField1513 = false;
         this.internalMethod10034();
         ItemStack localValue1 = internalField0149.player.getMainHandStack();
         return LegacyItemTypes.isPickaxe(localValue1) && localValue1.isDamageable()
            ? this.internalMethod04635(localValue1) < this.internalField0382.internalMethod08576()
            : false;
      }
   }

   private boolean internalMethod09357() {
      if (this.internalMethod08275() > 0) {
         return true;
      } else if (this.internalField1514 && !this.internalField1554.internalMethod02365(60000L)) {
         return false;
      } else {
         return this.internalMethod09369()
            ? true
            : this.internalField1263.internalMethod04496() && this.internalMethod02281() > 0 && internalMethod07652(this.internalField0384.internalMethod08926()) > 0;
      }
   }

   private void internalMethod10041() {
      this.internalMethod10032();
      this.internalField1803.internalMethod00701();
      this.internalField1741 = -1;
      this.internalField1800.internalMethod00701();
      this.internalField0739 = MineFarmTask.InternalType0366.internalField1603;
   }

   private void internalMethod10087() {
      this.internalField0739 = MineFarmTask.InternalType0366.internalField0739;
      this.internalField1188.internalMethod00701();
   }

   private void internalMethod10089() {
      this.internalMethod10032();
      this.internalMethod10034();
      ItemStack localValue1 = internalField0149.player.getMainHandStack();
      if (LegacyItemTypes.isPickaxe(localValue1) && localValue1.isDamageable()) {
         if (this.internalMethod04635(localValue1) >= this.internalField1142.internalMethod08576()) {
            this.internalMethod10087();
         } else if (this.internalMethod08275() <= 0) {
            this.internalMethod10095();
         } else if (internalField0149.player.getOffHandStack().getItem() != Items.EXPERIENCE_BOTTLE) {
            InventorySlot localValue4 = InventorySlots.internalMethod03558()
               .internalMethod07591(InventorySlots.internalMethod02872())
               .internalMethod03297(localValue0 -> localValue0.getItem() == Items.EXPERIENCE_BOTTLE);
            if (localValue4 == null) {
               this.internalMethod10095();
            } else {
               InventoryUtils.internalMethod01016(localValue4, InventoryUtils.internalMethod06162());
               this.internalField1803.internalMethod00701();
            }
         } else {
            int localValue2 = localValue1.getDamage();
            if (this.internalField1741 >= 0 && localValue2 >= this.internalField1741) {
               if (this.internalField1800.internalMethod02365(12000L)) {
                  this.internalField1513 = true;
                  this.internalField1801.internalMethod00701();
                  if (this.internalField1799.internalMethod02365(5000L)) {
                     this.internalMethod04510("modules.mine_farm.repair_stuck");
                     this.internalField1799.internalMethod00701();
                  }

                  this.internalMethod10087();
                  return;
               }
            } else {
               this.internalField1741 = localValue2;
               this.internalField1800.internalMethod00701();
            }

            float localValue3 = internalField0149.player.getYaw();
            internalField0149.player.setPitch(90.0F);
            internalField0149.player.setHeadYaw(localValue3);
            if (this.internalField1803.internalMethod02365(120L)) {
               internalField0149.interactionManager
                  .sendSequencedPacket(internalField0149.world, localValue1x -> new PlayerInteractItemC2SPacket(Hand.OFF_HAND, localValue1x, localValue3, 90.0F));
               this.internalField1803.internalMethod00701();
            }
         }
      } else {
         this.internalMethod10087();
      }
   }

   private void internalMethod10095() {
      if (!this.internalMethod09369()) {
         if (!this.internalField1263.internalMethod04496()
            || this.internalMethod02281() <= 0
            || internalMethod07652(this.internalField0384.internalMethod08926()) <= 0
            || !this.internalMethod02972(true)) {
            this.internalMethod07782("modules.mine_farm.inventory_full");
         }
      } else {
         this.internalField1515 = false;
         this.internalField1469 = 0;
         this.internalField1468 = 0;
         this.internalField1740 = 0;
         this.internalField1551.internalMethod00701();
         this.internalField1552.internalMethod00701();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField1602;
      }
   }

   private void internalMethod07782(String localValue1) {
      if (internalField0149.currentScreen != null) {
         internalField0149.player.closeHandledScreen();
      }

      this.internalField1514 = true;
      this.internalField1554.internalMethod00701();
      if (this.internalField1799.internalMethod02365(5000L)) {
         this.internalMethod04510(localValue1);
         this.internalField1799.internalMethod00701();
      }

      this.internalMethod10087();
   }

   private String internalMethod08191() {
      String localValue1 = "\u0411\u0443\u0442\u044b\u043b\u043e\u0447\u043a\u0430 \u043e\u043f\u044b\u0442\u0430";
      if (localValue1 == null || localValue1.isBlank()) {
         localValue1 = "\u0431\u0443\u0442\u044b\u043b\u043e\u0447\u043a\u0430 \u043e\u043f\u044b\u0442\u0430";
      }

      return localValue1;
   }

   private boolean internalMethod09367() {
      if (!(internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler)) {
         return false;
      } else {
         return AuctionTooltipParser.internalMethod08274(this.internalMethod01469()) ? true : this.internalMethod01469().contains("\u0431\u0443\u0442\u044b\u043b");
      }
   }

   private void internalMethod10097() {
      if (this.internalMethod09367()) {
         this.internalField1551.internalMethod00701();
         this.internalField1552.internalMethod00701();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField1601;
      } else if (internalField0149.currentScreen != null) {
         if (this.internalField1552.internalMethod02365(2500L)) {
            internalField0149.player.closeHandledScreen();
            this.internalField1552.internalMethod00701();
         }
      } else {
         if (!this.internalField1515 || this.internalField1552.internalMethod02365(3500L)) {
            internalField0149.player.networkHandler.sendChatCommand("ah search " + this.internalMethod08191());
            this.internalField1515 = true;
            this.internalField1552.internalMethod00701();
            this.internalField1469++;
            if (this.internalField1469 > 6) {
               this.internalMethod07782("modules.mine_farm.auction_failed");
            }
         }
      }
   }

   private void internalMethod10135() {
      if (!this.internalMethod09367()) {
         this.internalField1515 = false;
         this.internalField1552.internalMethod00701();
         this.internalField0739 = MineFarmTask.InternalType0366.internalField1602;
      } else if (this.internalField1551.internalMethod02365(400L)) {
         int localValue1 = this.internalMethod08290();
         if (localValue1 < 0) {
            this.internalMethod07782("modules.mine_farm.bottles_not_found");
         } else {
            this.internalMethod02118(localValue1, 0);
            this.internalField1552.internalMethod00701();
            this.internalField0739 = MineFarmTask.InternalType0366.internalField1600;
         }
      }
   }

   private void internalMethod10136() {
      String localValue1 = this.internalMethod01469();
      if (localValue1.contains("\u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435 \u043f\u043e\u043a\u0443\u043f\u043a\u0438")) {
         if (this.internalField1552.internalMethod02365(300L)) {
            this.internalMethod02118(2, 0);
            this.internalField1552.internalMethod00701();
            this.internalField0739 = MineFarmTask.InternalType0366.internalField1598;
         }
      } else if (this.internalMethod09367()) {
         if (this.internalField1552.internalMethod02365(600L)) {
            this.internalField1551.internalMethod00701();
            this.internalField0739 = MineFarmTask.InternalType0366.internalField1601;
         }
      } else {
         if (this.internalField1552.internalMethod02365(2500L)) {
            this.internalField1468++;
            if (this.internalField1468 > 4) {
               this.internalMethod07782("modules.mine_farm.buy_failed");
               return;
            }

            this.internalField1515 = false;
            this.internalField0739 = MineFarmTask.InternalType0366.internalField1602;
         }
      }
   }

   private void internalMethod10137() {
      if (this.internalField1552.internalMethod02365(400L)) {
         if (internalField0149.currentScreen != null) {
            internalField0149.player.closeHandledScreen();
         }

         this.internalField1514 = false;
         this.internalField1740++;
         if (this.internalMethod08275() < (int)this.internalField1140.internalMethod08576() && this.internalMethod09369() && this.internalField1740 < 20) {
            this.internalField1515 = false;
            this.internalField1469 = 0;
            this.internalField1552.internalMethod00701();
            this.internalField0739 = MineFarmTask.InternalType0366.internalField1602;
         } else {
            this.internalField1803.internalMethod00701();
            this.internalField0739 = MineFarmTask.InternalType0366.internalField1603;
         }
      }
   }

   private double internalMethod04635(ItemStack localValue1) {
      return localValue1.isDamageable() && localValue1.getMaxDamage() > 0 ? (double)(localValue1.getMaxDamage() - localValue1.getDamage()) / localValue1.getMaxDamage() * 100.0 : 100.0;
   }

   private int internalMethod08275() {
      int localValue1 = 0;
      PlayerInventory localValue2 = internalField0149.player.getInventory();

      for (int localValue3 = 0; localValue3 < localValue2.size(); localValue3++) {
         ItemStack localValue4 = localValue2.getStack(localValue3);
         if (localValue4.getItem() == Items.EXPERIENCE_BOTTLE) {
            localValue1 += localValue4.getCount();
         }
      }

      return localValue1;
   }

   private boolean internalMethod09369() {
      return this.internalMethod08278() > 0;
   }

   private int internalMethod08278() {
      PlayerInventory localValue1 = internalField0149.player.getInventory();
      int localValue2 = 0;

      for (int localValue3 = 0; localValue3 < 36; localValue3++) {
         if (localValue1.getStack(localValue3).isEmpty()) {
            localValue2++;
         }
      }

      return localValue2;
   }

   private long internalMethod04637(ItemStack localValue1) {
      for (Text localValue3 : localValue1.getTooltip(
         TooltipContext.create(internalField0149.world),
         internalField0149.player,
         internalField0149.options.advancedItemTooltips ? TooltipType.ADVANCED : TooltipType.BASIC
      )) {
         String localValue4 = localValue3.getString();
         String localValue5 = localValue4.toLowerCase();
         if ((localValue4.contains("$") || localValue5.contains("\u0446\u0435\u043d\u0430") || localValue5.contains("\u0441\u0442\u043e\u0438\u043c")) && !localValue4.contains("%")) {
            String localValue6 = localValue4.replaceAll("[^0-9]", "");
            if (!localValue6.isEmpty()) {
               try {
                  long localValue7 = Long.parseLong(localValue6);
                  if (localValue7 > 0L) {
                     return localValue7;
                  }
               } catch (NumberFormatException localValue9) {
               }
            }
         }
      }

      return -1L;
   }

   private int internalMethod08290() {
      if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1) {
         int localValue14 = localValue1.slots.size() - 36;
         double localValue3 = Double.MAX_VALUE;
         int localValue5 = -1;

         for (int localValue6 = 0; localValue6 < localValue14; localValue6++) {
            Slot localValue7 = localValue1.getSlot(localValue6);
            if (localValue7 != null && localValue7.hasStack()) {
               ItemStack localValue8 = localValue7.getStack();
               if (localValue8.getItem() == Items.EXPERIENCE_BOTTLE) {
                  long localValue9 = this.internalMethod04637(localValue8);
                  if (localValue9 > 0L) {
                     int localValue11 = Math.max(1, localValue8.getCount());
                     double localValue12 = (double)localValue9 / localValue11;
                     if (localValue12 < localValue3) {
                        localValue3 = localValue12;
                        localValue5 = localValue7.id;
                     }
                  }
               }
            }
         }

         return localValue5;
      } else {
         return -1;
      }
   }

   private String internalMethod09089() {
      String localValue1 = this.internalField0239 != null ? String.valueOf(this.internalField1053) : "-";

      return switch (this.internalField0739) {
         case internalField0739 -> this.internalField1077 != null
            ? "API: " + this.internalField1077
            : "\u0412\u044b\u0431\u043e\u0440 \u0448\u0430\u0445\u0442\u044b...";
         case internalField0740 -> "\u041f\u0435\u0440\u0435\u0445\u043e\u0434 \u043d\u0430 \u0430\u043d\u0430\u0440\u0445\u0438\u044e " + localValue1;
         case internalField1289 -> "\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442 \u043d\u0430 \u0448\u0430\u0445\u0442\u0443 (\u0430\u043d. " + localValue1 + ")";
         case internalField1290 -> this.internalField1099
            ? "\u041a\u043e\u043f\u0430\u044e \u0448\u0430\u0445\u0442\u0443 (\u0430\u043d. " + localValue1 + ")"
            : "\u0416\u0434\u0443 \u0441\u0431\u0440\u043e\u0441\u0430 (\u0430\u043d. " + localValue1 + ")";
         case internalField1291 -> "\u0418\u0434\u0443 \u043f\u0440\u043e\u0434\u0430\u0432\u0430\u0442\u044c (\u0430\u043d. "
            + (this.internalField1464 > 0 ? this.internalField1464 : "?")
            + ")";
         case internalField1292 -> "\u041e\u0442\u043a\u0440\u044b\u0432\u0430\u044e \u0441\u043a\u0443\u043f\u0449\u0438\u043a\u0430";
         case internalField1599 -> "\u0412\u044b\u0431\u043e\u0440 \u0441\u0435\u043a\u0446\u0438\u0438";
         case internalField1597 -> "\u041f\u0440\u043e\u0434\u0430\u044e \u0440\u0443\u0434\u0443";
         case internalField1603 -> "\u0427\u0438\u043d\u044e \u043a\u0438\u0440\u043a\u0443 (" + this.internalMethod08275() + " \u0431\u0443\u0442.)";
         case internalField1602, internalField1601, internalField1600, internalField1598 -> "\u0417\u0430\u043a\u0443\u043f\u0430\u044e \u0431\u0443\u0442\u044b\u043b\u044c\u043a\u0438 ("
            + this.internalMethod08275()
            + ")";
      };
   }

   private static String internalMethod00178(String localValue0) {
      if (localValue0 == null) {
         return "?";
      } else {
         String localValue1 = localValue0.toLowerCase();

         return switch (localValue1) {
            case "default" -> "def";
            case "legendary" -> "leg";
            case "mythical" -> "myth";
            default -> localValue0;
         };
      }
   }

   private static String internalMethod03746(long localValue0) {
      long localValue2 = localValue0 / 60L;
      long localValue4 = localValue0 % 60L;
      return String.format("%02d:%02d", localValue2, localValue4);
   }

   static enum InternalType0366 {
      internalField0739,
      internalField0740,
      internalField1289,
      internalField1290,
      internalField1291,
      internalField1292,
      internalField1599,
      internalField1597,
      internalField1603,
      internalField1602,
      internalField1601,
      internalField1600,
      internalField1598;
   }
}
