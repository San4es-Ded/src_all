package rockstar.modules.player;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.InputEvent;

@ModuleInfo(
   name = "Scaffold",
   internalMethod09633 = "modules.descriptions.scaffold",
   category = ModuleCategory.PLAYER
)
public class ScaffoldModule extends Module {
   private static final int internalField0227 = 4;
   private static final double internalField0194 = 0.001;
   private static final double internalField0193 = 0.02;
   private RangeSetting internalField0672;
   private SliderSetting internalField0383;
   private ModeSetting internalField0668;
   ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting.InternalType0088 internalField1066;
   ModeSetting.InternalType0088 internalField1067;
   private ModeSetting internalField0669;
   private ModeSetting.InternalType0088 internalField1068;
   private ModeSetting.InternalType0088 internalField1065;
   private ModeSetting.InternalType0088 internalField1480;
   private ModeSetting.InternalType0088 internalField1481;
   private ModeSetting internalField1272;
   private ModeSetting.InternalType0088 internalField1483;
   private ModeSetting.InternalType0088 internalField1485;
   private ModeSetting.InternalType0088 internalField1484;
   private ModeSetting.InternalType0088 internalField1482;
   private ModeSetting.InternalType0088 internalField1479;
   private ModeSetting.InternalType0088 internalField1478;
   private ModeSetting internalField1269;
   private ModeSetting.InternalType0088 internalField1749;
   private ModeSetting.InternalType0088 internalField1761;
   private ModeSetting.InternalType0088 internalField1751;
   private ModeSetting.InternalType0088 internalField1752;
   private ModeSetting.InternalType0088 internalField1753;
   private ModeSetting.InternalType0088 internalField1754;
   private ModeSetting.InternalType0088 internalField1755;
   private ModeSetting.InternalType0088 internalField1750;
   private ModeSetting internalField1270;
   private ModeSetting.InternalType0088 internalField1756;
   private ModeSetting.InternalType0088 internalField1757;
   private ModeSetting.InternalType0088 internalField1758;
   private SliderSetting internalField0382;
   private SliderSetting internalField1142;
   private SliderSetting internalField1140;
   private BooleanSetting internalField0650;
   private ModeSetting internalField1271;
   private ModeSetting.InternalType0088 internalField1762;
   private ModeSetting.InternalType0088 internalField1760;
   private ModeSetting.InternalType0088 internalField1759;
   private ModeSetting.InternalType0088 internalField1763;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private SliderSetting internalField1141;
   private SliderSetting internalField1143;
   private BooleanSetting internalField1263;
   BooleanSetting internalField1262;
   private RangeSetting internalField0673;
   private RangeSetting internalField1274;
   private BooleanSetting internalField1264;
   BooleanSetting internalField1587;
   BooleanSetting internalField1590;
   private BooleanSetting internalField1594;
   private BooleanSetting internalField1588;
   BooleanSetting internalField1589;
   SliderSetting internalField1529;
   private SliderSetting internalField1534;
   private RangeSetting internalField1273;
   private RangeSetting internalField1275;
   private SliderSetting internalField1535;
   private SliderSetting internalField1536;
   private SliderSetting internalField1533;
   private SliderSetting internalField1532;
   private SliderSetting internalField1531;
   private SliderSetting internalField1530;
   private BooleanSetting internalField1591;
   private BooleanSetting internalField1592;
   private BooleanSetting internalField1593;
   private BooleanSetting internalField1811;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final ArrayDeque<BlockPos> internalField0881 = new ArrayDeque<>(4);
   private final ArrayDeque<Vec3d> internalField0880 = new ArrayDeque<>(5);
   private ScaffoldModule.InternalType0337 internalField0608;
   private ScaffoldModule.InternalType0197 internalField0893;
   private BlockPos internalField0352;
   private BlockPos internalField0351;
   private Direction internalField0150;
   ScaffoldModule.InternalType0335 internalField0605;
   ScaffoldModule.InternalType0198 internalField0894;
   private ScaffoldModule.InternalType0192 internalField0793;
   private BlockPos internalField1131;
   private float internalField0205;
   private int internalField1053;
   private int internalField1055;
   private int internalField1056;
   int internalField1054;
   private int internalField1464;
   private int internalField1470;
   private int internalField1465;
   private float internalField0206;
   private int internalField1463;
   private double internalField1045;
   private boolean internalField0277;
   private boolean internalField0276;
   private boolean internalField1099;
   private boolean internalField1100;
   private float internalField1048;
   private long internalField0229;
   private float internalField1047;
   private final EventListener<ClientPlayerTickEvent> internalField0157;
   private final EventListener<ClientPlayerTickEvent> internalField0158;
   private final EventListener<InputEvent> internalField1028;
   private final EventListener<WorldChangeEvent> internalField1029;

   public ScaffoldModule() {
      this.internalField0894 = ScaffoldModule.InternalType0198.internalField0894;
      this.internalField0205 = Float.NaN;
      this.internalField1045 = Double.NaN;
      this.internalField0157 = new EventListener<ClientPlayerTickEvent>() {
         public void onEvent(ClientPlayerTickEvent localValue1) {
            ScaffoldModule.this.internalMethod09651();
            ScaffoldModule.this.internalMethod09838();
            ScaffoldModule.this.internalMethod09836();
            ScaffoldModule.this.internalMethod09855();
         }

         @Override
         public int internalMethod07175() {
            return 2;
         }
      };
      this.internalField0158 = new EventListener<ClientPlayerTickEvent>() {
         public void onEvent(ClientPlayerTickEvent localValue1) {
            ScaffoldModule.this.internalMethod09857();
         }

         @Override
         public int internalMethod07175() {
            return -2;
         }
      };
      this.internalField1028 = new EventListener<InputEvent>() {
         public void onEvent(InputEvent localValue1) {
            ScaffoldModule.this.internalField0894 = ScaffoldModule.InternalType0198.internalMethod05766(localValue1);
            ScaffoldModule.this.internalField0605 = ScaffoldModule.this.internalField0894.internalMethod08927()
               ? ScaffoldModule.this.internalMethod04119(ScaffoldModule.this.internalField0894)
               : null;
            if (ScaffoldModule.this.internalField1067.isSelected()) {
               ScaffoldModule.this.internalMethod00052(localValue1);
            }

            if (ScaffoldModule.this.internalField1590.internalMethod04496() && ScaffoldModule.this.internalField0237.isSelected()) {
               ScaffoldModule.this.internalMethod04530(localValue1);
            }

            if (ScaffoldModule.this.internalField1589.internalMethod04496()
               && ScaffoldModule.this.internalMethod04837() > ScaffoldModule.this.internalField1529.internalMethod08576()) {
               localValue1.setForward(0.0F);
               localValue1.setStrafe(0.0F);
            }

            if (ScaffoldModule.this.internalField1054 > 0) {
               localValue1.setSneak(true);
               ScaffoldModule.this.internalField1054--;
            }

            if (ScaffoldModule.this.internalField1262.internalMethod04496()
               && ScaffoldModule.this.internalField0237.isSelected()
               && ScaffoldModule.this.internalMethod04531(localValue1)) {
               localValue1.setSneak(true);
            }

            ScaffoldModule.InternalType0334 localValue2 = ScaffoldModule.this.internalMethod03951();
            if (localValue2.internalMethod02800()) {
               localValue1.setJump(true);
            }

            if (localValue2.internalMethod02806()) {
               localValue1.setForward(0.0F);
               localValue1.setStrafe(0.0F);
            }

            if (localValue2.internalMethod08579()) {
               localValue1.setForward(-1.0F);
               localValue1.setStrafe(0.0F);
            }

            if (localValue2.internalMethod02799() > 0) {
               localValue1.setSneak(true);
               ScaffoldModule.this.internalField1054 = Math.max(ScaffoldModule.this.internalField1054, localValue2.internalMethod02799());
            }

            if (ScaffoldModule.this.internalField1587.internalMethod04496()
               && ScaffoldModule.this.internalField0237.isSelected()
               && ScaffoldModule.this.internalMethod09837()) {
               localValue1.setSneak(false);
            }
         }

         @Override
         public int internalMethod07175() {
            return -1;
         }
      };
      this.internalField1029 = localValue1 -> this.internalMethod10002();
      this.internalMethod09649();
   }

   private void internalMethod09649() {
      this.internalField0672 = new RangeSetting(this, "modules.settings.scaffold.delay")
         .internalMethod08834(0.0F)
         .internalMethod08219(40.0F)
         .internalMethod08853(1.0F)
         .internalMethod01407(0.0F)
         .internalMethod06328(0.0F);
      this.internalField0383 = new SliderSetting(this, "modules.settings.scaffold.min_dist")
         .internalMethod05900(0.0F)
         .internalMethod02732(0.25F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.0F);
      this.internalField0668 = new ModeSetting(this, "modules.settings.scaffold.technique");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.scaffold.technique.normal").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.scaffold.technique.expand");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.scaffold.technique.god_bridge");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.scaffold.technique.breezily");
      this.internalField0669 = new ModeSetting(this, "modules.settings.scaffold.same_y");
      this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.scaffold.same_y.off").select();
      this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.scaffold.same_y.on");
      this.internalField1480 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.scaffold.same_y.falling");
      this.internalField1481 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.scaffold.same_y.hypixel");
      this.internalField1272 = new ModeSetting(this, "modules.settings.scaffold.tower");
      this.internalField1483 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.scaffold.tower.none").select();
      this.internalField1485 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.scaffold.tower.motion_mode");
      this.internalField1484 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.scaffold.tower.pulldown");
      this.internalField1482 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.scaffold.tower.karhu");
      this.internalField1479 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.scaffold.tower.vulcan");
      this.internalField1478 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.scaffold.tower.hypixel");
      this.internalField1269 = new ModeSetting(
         this, "modules.settings.scaffold.rotation_mode", () -> !this.internalField0668.internalMethod06103(this.internalField0237)
      );
      this.internalField1749 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.scaffold.rotation_mode.center");
      this.internalField1761 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.scaffold.rotation_mode.random");
      this.internalField1751 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.scaffold.rotation_mode.stabilized").select();
      this.internalField1752 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.scaffold.rotation_mode.nearest_rotation");
      this.internalField1753 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.scaffold.rotation_mode.reverse_yaw");
      this.internalField1754 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.scaffold.rotation_mode.diagonal_yaw");
      this.internalField1755 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.scaffold.rotation_mode.angle_yaw");
      this.internalField1750 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.scaffold.rotation_mode.edge_point");
      this.internalField1270 = new ModeSetting(this, "modules.settings.scaffold.rotation_timing");
      this.internalField1756 = new ModeSetting.InternalType0088(this.internalField1270, "modules.settings.scaffold.rotation_timing.normal").select();
      this.internalField1757 = new ModeSetting.InternalType0088(this.internalField1270, "modules.settings.scaffold.rotation_timing.on_tick");
      this.internalField1758 = new ModeSetting.InternalType0088(this.internalField1270, "modules.settings.scaffold.rotation_timing.on_tick_snap");
      this.internalField0382 = new SliderSetting(this, "modules.settings.scaffold.rotation_speed")
         .internalMethod08673(5.0F)
         .internalMethod05900(30.0F)
         .internalMethod02732(180.0F)
         .internalMethod08074(180.0F);
      this.internalField1142 = new SliderSetting(this, "modules.settings.scaffold.aim_tolerance")
         .internalMethod08673(0.5F)
         .internalMethod05900(1.0F)
         .internalMethod02732(20.0F)
         .internalMethod08074(10.0F);
      this.internalField1140 = new SliderSetting(this, "modules.settings.scaffold.stable_ticks")
         .internalMethod08673(1.0F)
         .internalMethod05900(0.0F)
         .internalMethod02732(5.0F)
         .internalMethod08074(0.0F);
      this.internalField0650 = new BooleanSetting(this, "modules.settings.scaffold.consider_inventory").internalMethod04836(false);
      this.internalField1271 = new ModeSetting(this, "modules.settings.scaffold.move_correction");
      this.internalField1762 = new ModeSetting.InternalType0088(this.internalField1271, "modules.settings.scaffold.move_correction.off");
      this.internalField1760 = new ModeSetting.InternalType0088(this.internalField1271, "modules.settings.scaffold.move_correction.strict");
      this.internalField1759 = new ModeSetting.InternalType0088(this.internalField1271, "modules.settings.scaffold.move_correction.silent").select();
      this.internalField1763 = new ModeSetting.InternalType0088(this.internalField1271, "modules.settings.scaffold.move_correction.change_look");
      this.internalField0651 = new BooleanSetting(this, "modules.settings.scaffold.auto_block").internalMethod06630();
      this.internalField1261 = new BooleanSetting(this, "modules.settings.scaffold.auto_block.always", () -> !this.internalField0651.internalMethod04496())
         .internalMethod04836(false);
      this.internalField1141 = new SliderSetting(
            this, "modules.settings.scaffold.auto_block.slot_reset_delay", () -> !this.internalField0651.internalMethod04496()
         )
         .internalMethod05900(0.0F)
         .internalMethod02732(40.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(5.0F);
      this.internalField1143 = new SliderSetting(
            this, "modules.settings.scaffold.auto_block.do_not_use_below", () -> !this.internalField0651.internalMethod04496()
         )
         .internalMethod05900(0.0F)
         .internalMethod02732(64.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(1.0F);
      this.internalField1263 = new BooleanSetting(this, "modules.settings.scaffold.ledge").internalMethod06630();
      this.internalField1262 = new BooleanSetting(
            this, "modules.settings.scaffold.eagle", () -> !this.internalField0668.internalMethod06103(this.internalField0237)
         )
         .internalMethod04836(false);
      this.internalField0673 = new RangeSetting(this, "modules.settings.scaffold.eagle.blocks", () -> !this.internalField1262.internalMethod04496())
         .internalMethod08834(0.0F)
         .internalMethod08219(10.0F)
         .internalMethod08853(1.0F)
         .internalMethod01407(0.0F)
         .internalMethod06328(0.0F);
      this.internalField1274 = new RangeSetting(this, "modules.settings.scaffold.eagle.edge_distance", () -> !this.internalField1262.internalMethod04496())
         .internalMethod08834(0.01F)
         .internalMethod08219(1.3F)
         .internalMethod08853(0.01F)
         .internalMethod01407(0.01F)
         .internalMethod06328(0.05F);
      this.internalField1264 = new BooleanSetting(this, "modules.settings.scaffold.eagle.only_on_ground", () -> !this.internalField1262.internalMethod04496())
         .internalMethod06630();
      this.internalField1587 = new BooleanSetting(
            this, "modules.settings.scaffold.down", () -> !this.internalField0668.internalMethod06103(this.internalField0237)
         )
         .internalMethod04836(false);
      this.internalField1590 = new BooleanSetting(
            this, "modules.settings.scaffold.stabilize_movement", () -> !this.internalField0668.internalMethod06103(this.internalField0237)
         )
         .internalMethod06630();
      this.internalField1594 = new BooleanSetting(
            this, "modules.settings.scaffold.ceiling", () -> !this.internalField0668.internalMethod06103(this.internalField0237)
         )
         .internalMethod04836(false);
      this.internalField1588 = new BooleanSetting(
            this, "modules.settings.scaffold.head_hitter", () -> !this.internalField0668.internalMethod06103(this.internalField0237)
         )
         .internalMethod04836(false);
      this.internalField1589 = new BooleanSetting(this, "modules.settings.scaffold.speed_limiter").internalMethod04836(false);
      this.internalField1529 = new SliderSetting(
            this, "modules.settings.scaffold.speed_limiter.speed", () -> !this.internalField1589.internalMethod04496()
         )
         .internalMethod05900(0.01F)
         .internalMethod02732(0.4F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.11F);
      this.internalField1534 = new SliderSetting(
            this, "modules.settings.scaffold.expand.length", () -> !this.internalField0668.internalMethod06103(this.internalField0238)
         )
         .internalMethod05900(1.0F)
         .internalMethod02732(10.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(4.0F);
      this.internalField1273 = new RangeSetting(
            this, "modules.settings.scaffold.breezily.edge_distance", () -> !this.internalField0668.internalMethod06103(this.internalField1067)
         )
         .internalMethod08834(0.25F)
         .internalMethod08219(0.5F)
         .internalMethod08853(0.01F)
         .internalMethod01407(0.45F)
         .internalMethod06328(0.5F);
      this.internalField1275 = new RangeSetting(
            this, "modules.settings.scaffold.god_bridge.sneak_time", () -> !this.internalField0668.internalMethod06103(this.internalField1066)
         )
         .internalMethod08834(1.0F)
         .internalMethod08219(10.0F)
         .internalMethod08853(1.0F)
         .internalMethod01407(1.0F)
         .internalMethod06328(1.0F);
      this.internalField1535 = new SliderSetting(
            this, "modules.settings.scaffold.god_bridge.force_sneak_below", () -> !this.internalField0668.internalMethod06103(this.internalField1066)
         )
         .internalMethod05900(0.0F)
         .internalMethod02732(10.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(3.0F);
      this.internalField1536 = new SliderSetting(
            this, "modules.settings.scaffold.tower.motion", () -> !this.internalField1272.internalMethod06103(this.internalField1485)
         )
         .internalMethod05900(0.0F)
         .internalMethod02732(1.0F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.42F);
      this.internalField1533 = new SliderSetting(
            this, "modules.settings.scaffold.tower.trigger_height", () -> !this.internalField1272.internalMethod06103(this.internalField1485)
         )
         .internalMethod05900(0.76F)
         .internalMethod02732(1.0F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.78F);
      this.internalField1532 = new SliderSetting(
            this, "modules.settings.scaffold.tower.slow", () -> !this.internalField1272.internalMethod06103(this.internalField1485)
         )
         .internalMethod05900(0.0F)
         .internalMethod02732(3.0F)
         .internalMethod08673(0.05F)
         .internalMethod08074(1.0F);
      this.internalField1531 = new SliderSetting(
            this, "modules.settings.scaffold.tower.pulldown_trigger", () -> !this.internalField1272.internalMethod06103(this.internalField1484)
         )
         .internalMethod05900(0.0F)
         .internalMethod02732(0.2F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.1F);
      this.internalField1530 = new SliderSetting(
            this, "modules.settings.scaffold.tower.karhu_trigger", () -> !this.internalField1272.internalMethod06103(this.internalField1482)
         )
         .internalMethod05900(0.0F)
         .internalMethod02732(0.2F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.06F);
      this.internalField1591 = new BooleanSetting(
            this, "modules.settings.scaffold.tower.karhu_pulldown", () -> !this.internalField1272.internalMethod06103(this.internalField1482)
         )
         .internalMethod06630();
      this.internalField1592 = new BooleanSetting(this, "modules.settings.scaffold.reset_sprint").internalMethod06630();
      this.internalField1593 = new BooleanSetting(this, "modules.settings.scaffold.simulate_placement_attempts").internalMethod04836(false);
      this.internalField1811 = new BooleanSetting(
            this, "modules.settings.scaffold.simulate_placement_attempts.failed_only", () -> !this.internalField1593.internalMethod04496()
         )
         .internalMethod06630();
   }

   @Override
   public void onEnable() {
      if (internalField0149.player != null) {
         this.internalField1053 = internalField0149.player.getBlockPos().getY() - 1;
         this.internalField1055 = internalField0149.player.getBlockPos().getY();
         this.internalField1056 = 2;
         this.internalField0276 = internalField0149.player.isOnGround();
      }

      this.internalMethod10002();
      this.internalMethod10057();
      this.internalField1047 = this.internalMethod00883(this.internalField1273);
      this.internalField0519.internalMethod00701();
   }

   @Override
   public void onDisable() {
      this.internalMethod10002();
   }

   void internalMethod09651() {
      if (internalField0149.player != null) {
         boolean localValue1 = internalField0149.player.isOnGround();
         if (localValue1) {
            this.internalField1053 = internalField0149.player.getBlockPos().getY() - 1;
            this.internalField1056++;
            this.internalField0277 = false;
            this.internalField1463 = 0;
         } else {
            this.internalField1463++;
         }

         if (internalField0149.options.jumpKey.isPressed()) {
            this.internalField1055 = internalField0149.player.getBlockPos().getY();
            this.internalField1056 = 2;
         }

         boolean localValue2 = this.internalField0276 && !localValue1 && internalField0149.player.getVelocity().y > 0.0;
         if (localValue2) {
            this.internalField1045 = internalField0149.player.getY();
         }

         this.internalField0276 = localValue1;
      }
   }

   void internalMethod09836() {
      if (internalField0149.player != null) {
         if (this.internalField1588.internalMethod04496() && this.internalField0237.isSelected() && this.internalMethod09856() && this.internalMethod10001()) {
            internalField0149.player.jump();
         }
      }
   }

   void internalMethod09838() {
      if (internalField0149.player != null
         && !this.internalField1272.internalMethod06103(this.internalField1483)
         && this.internalMethod09858()
         && this.internalMethod09094() > 0
         && this.internalMethod09999()) {
         Vec3d localValue1 = internalField0149.player.getVelocity();
         if (this.internalField1272.internalMethod06103(this.internalField1485)) {
            if (!Double.isNaN(this.internalField1045)) {
               if (internalField0149.player.getY() > this.internalField1045 + this.internalField1533.internalMethod08576()) {
                  internalField0149.player
                     .setPosition(internalField0149.player.getX(), Math.floor(internalField0149.player.getY()), internalField0149.player.getZ());
                  Vec3d localValue2 = internalField0149.player.getVelocity();
                  double localValue3 = this.internalField1532.internalMethod08576();
                  internalField0149.player.setVelocity(localValue2.x * localValue3, this.internalField1536.internalMethod08576(), localValue2.z * localValue3);
                  this.internalField1045 = internalField0149.player.getY();
               }
            }
         } else if (this.internalField1272.internalMethod06103(this.internalField1484)) {
            if (!internalField0149.player.isOnGround() && localValue1.y < this.internalField1531.internalMethod08576()) {
               internalField0149.player.setVelocity(localValue1.x, -1.0, localValue1.z);
            }
         } else if (this.internalField1272.internalMethod06103(this.internalField1482)) {
            if (this.internalField1591.internalMethod04496() && !internalField0149.player.isOnGround() && localValue1.y < this.internalField1530.internalMethod08576()) {
               internalField0149.player.setVelocity(localValue1.x, localValue1.y - 1.0, localValue1.z);
            }
         } else if (this.internalField1272.internalMethod06103(this.internalField1479)) {
            if (internalField0149.player.age % 2 == 0) {
               internalField0149.player.setVelocity(localValue1.x, 0.7, localValue1.z);
            } else {
               internalField0149.player.setVelocity(localValue1.x, this.internalMethod10001() ? 0.42 : 0.6, localValue1.z);
            }
         } else {
            if (this.internalField1272.internalMethod06103(this.internalField1478)) {
               if (internalField0149.player.getX() % 1.0 != 0.0 && !this.internalMethod10001()) {
                  internalField0149.player
                     .setVelocity(Math.min(Math.round(internalField0149.player.getX()) - internalField0149.player.getX(), 0.281), localValue1.y, localValue1.z);
               }

               if (this.internalField1463 > 14) {
                  internalField0149.player.setVelocity(localValue1.x * 0.6, localValue1.y - 0.09, localValue1.z * 0.6);
                  return;
               }

               if (this.internalField1463 % 3 == 0) {
                  internalField0149.player.setVelocity(localValue1.x, 0.42, localValue1.z);
                  this.internalMethod07311(0.247 - ThreadLocalRandom.current().nextFloat() / 100.0F);
               } else if (this.internalField1463 % 3 == 2) {
                  internalField0149.player.setVelocity(localValue1.x, 1.0 - internalField0149.player.getY() % 1.0, localValue1.z);
               }
            }
         }
      } else {
         this.internalField1045 = Double.NaN;
      }
   }

   void internalMethod09855() {
      if (!GameUtils.internalMethod00471()) {
         this.internalMethod10000();
      } else {
         ScaffoldModule.InternalType0197 localValue1 = this.internalMethod01304();
         if (localValue1 == null) {
            this.internalMethod10000();
         } else {
            if (this.internalField1261.internalMethod04496() && localValue1.internalMethod04621()) {
               this.internalMethod08676(localValue1.internalMethod04620());
            }

            this.internalField0893 = localValue1;
            ScaffoldModule.InternalType0337 localValue2 = this.internalMethod03953();
            if (localValue2 == null) {
               this.internalMethod10000();
               this.internalField0893 = localValue1;
            } else {
               this.internalMethod03693(localValue2);
               if (this.internalField1270.internalMethod06103(this.internalField1756)) {
                  Rotation localValue3 = this.internalMethod00237(localValue2);
                  if (localValue3 != null) {
                     this.internalMethod03190(localValue3);
                  }
               }
            }
         }
      }
   }

   void internalMethod09857() {
      if (!GameUtils.internalMethod00471()) {
         this.internalMethod10000();
      } else {
         this.internalMethod09998();
         if (this.internalField0608 != null && this.internalField0893 != null) {
            ScaffoldModule.InternalType0197 localValue1 = this.internalMethod01304();
            if (localValue1 == null) {
               this.internalMethod10000();
            } else {
               this.internalField0893 = localValue1;
               ScaffoldModule.InternalType0337 localValue2 = this.internalField0608;
               if (!this.internalMethod03694(localValue2)) {
                  this.internalMethod10000();
               } else {
                  Rotation localValue3 = this.internalMethod00237(localValue2);
                  if (localValue3 == null) {
                     this.internalField1464 = 0;
                  } else {
                     if (!this.internalField1270.internalMethod06103(this.internalField1757)
                        && !this.internalField1270.internalMethod06103(this.internalField1758)) {
                        if (!this.internalMethod06372(localValue3)) {
                           this.internalField1464 = 0;
                           return;
                        }
                     } else {
                        this.internalMethod06371(localValue3);
                        RockstarClient.getInstance().internalMethod02368().internalMethod08198(localValue3);
                        if (this.internalField1270.internalMethod06103(this.internalField1758)) {
                           this.internalMethod03190(localValue3);
                        }
                     }

                     this.internalField1464++;
                     if (this.internalField1464 >= (int)this.internalField1140.internalMethod08576()) {
                        if (this.internalField0519.internalMethod02365(this.internalMethod04835())) {
                           BlockHitResult localValue4 = this.internalMethod01583(localValue2, localValue3);
                           if (localValue4 == null) {
                              this.internalField1464 = 0;
                              this.internalMethod04908(localValue2, localValue1);
                           } else {
                              this.internalMethod02298(localValue1, localValue2, localValue4);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void internalMethod09998() {
      this.internalField1099 = false;
      if (this.internalField1592.internalMethod04496() && internalField0149.player != null) {
         this.internalField1099 = internalField0149.player.isSprinting();
         internalField0149.options.sprintKey.setPressed(false);
         internalField0149.player.setSprinting(false);
      }
   }

   private void internalMethod06371(Rotation localValue1) {
      internalField0149.player
         .networkHandler
         .sendPacket(
            new Full(
               internalField0149.player.getX(),
               internalField0149.player.getY(),
               internalField0149.player.getZ(),
               localValue1.internalMethod00169(),
               MathHelper.clamp(localValue1.internalMethod00171(), -90.0F, 90.0F),
               internalField0149.player.isOnGround(),
               internalField0149.player.horizontalCollision
            )
         );
      RockstarClient.getInstance().internalMethod02368().internalMethod08209().internalMethod03239(localValue1.internalMethod00169());
      RockstarClient.getInstance().internalMethod02368().internalMethod08209().internalMethod03289(localValue1.internalMethod00171());
   }

   private ScaffoldModule.InternalType0337 internalMethod03953() {
      Vec3d localValue1 = this.internalMethod00973(this.internalField0605);
      if (localValue1 == null) {
         localValue1 = internalField0149.player.getEntityPos();
      }

      ModeSetting.InternalType0088 localValue2 = this.internalMethod04139();
      if (localValue2 == this.internalField0238) {
         return this.internalMethod00403(localValue1);
      } else {
         ScaffoldModule.InternalType0337 localValue3 = this.internalMethod04756(this.internalMethod06144(localValue1), localValue2 == this.internalField0237);
         return localValue3 == null && this.internalField0608 != null && this.internalMethod03694(this.internalField0608)
            ? this.internalMethod05439(this.internalField0608, internalField0149.player.getEyePos())
            : localValue3;
      }
   }

   private ScaffoldModule.InternalType0337 internalMethod00403(Vec3d localValue1) {
      int localValue2 = (int)this.internalField1534.internalMethod08576();

      for (int localValue3 = 0; localValue3 <= localValue2; localValue3++) {
         BlockPos localValue4 = this.internalMethod03433(localValue1, localValue3);
         ScaffoldModule.InternalType0337 localValue5 = this.internalMethod04756(
            this.internalMethod00664(this.internalMethod05705(localValue4), ScaffoldModule.InternalType0336.internalField0607), false
         );
         if (localValue5 != null) {
            return localValue5.internalMethod01699(true);
         }
      }

      return null;
   }

   private ScaffoldModule.InternalType0337 internalMethod04756(Set<BlockPos> localValue1, boolean localValue2) {
      Vec3d localValue3 = internalField0149.player.getEyePos();
      Rotation localValue4 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      ScaffoldModule.InternalType0337 localValue5 = null;
      double localValue6 = Double.MAX_VALUE;
      ArrayList localValue8 = new ArrayList(localValue1);
      localValue8.sort(this.internalMethod03613(internalField0149.player.getEntityPos(), this.internalField0605));

      for (BlockPos localValue10 : (Iterable<BlockPos>)(Iterable<?>)localValue8) {
         if (this.internalMethod05350(localValue10)) {
            for (Direction localValue14 : Direction.values()) {
               if (localValue14 != Direction.UP || localValue2) {
                  BlockPos localValue15 = localValue10.offset(localValue14);
                  Direction localValue16 = localValue14.getOpposite();
                  if (this.internalMethod05971(localValue15, localValue16)) {
                     for (Vec3d localValue18 : this.internalMethod05964(localValue15, localValue16, localValue3)) {
                        double localValue19 = internalField0149.player.getBlockInteractionRange();
                        double localValue21 = localValue3.squaredDistanceTo(localValue18);
                        if (!(localValue21 > localValue19 * localValue19)) {
                           ScaffoldModule.InternalType0337 localValue23 = new ScaffoldModule.InternalType0337(
                              localValue10, localValue15, localValue16, localValue18, this.internalMethod00971(localValue3, localValue18), !localValue2 || this.internalMethod09652()
                           );
                           ScaffoldModule.InternalType0337 localValue24 = this.internalMethod05439(localValue23, localValue3);
                           if (localValue24 != null) {
                              Rotation localValue25 = localValue24.internalMethod04292();
                              double localValue26 = localValue21 + this.internalMethod03531(localValue4, localValue25) * 0.15;
                              if (this.internalMethod02375(this.internalMethod04898(localValue4), localValue24)) {
                                 localValue26 -= 2.0;
                              }

                              if (this.internalField0605 != null) {
                                 localValue26 += this.internalField0605.internalMethod01491(localValue24.internalMethod00110().toCenterPos()) * 0.25;
                              }

                              if (localValue26 < localValue6) {
                                 localValue6 = localValue26;
                                 localValue5 = localValue24;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return localValue5;
   }

   private ScaffoldModule.InternalType0337 internalMethod05439(ScaffoldModule.InternalType0337 localValue1, Vec3d localValue2) {
      if (!this.internalMethod03694(localValue1)) {
         return null;
      } else {
         Rotation localValue3 = this.internalMethod07076(localValue1, localValue2);
         ScaffoldModule.InternalType0337 localValue4 = localValue1.internalMethod00907(localValue3);
         if (!localValue4.internalMethod01485() && !this.internalMethod02375(this.internalMethod07424(localValue3, localValue2), localValue4)) {
            Rotation localValue5 = this.internalMethod00857(localValue1, localValue2);
            return localValue5 == null ? null : localValue1.internalMethod00907(localValue5);
         } else {
            return localValue4;
         }
      }
   }

   private Rotation internalMethod00857(ScaffoldModule.InternalType0337 localValue1, Vec3d localValue2) {
      Vec3d localValue3 = internalField0149.player.getVelocity();
      int localValue4 = Math.max(1, (int)Math.ceil(2.0));

      for (int localValue5 = 1; localValue5 <= localValue4; localValue5++) {
         Vec3d localValue6 = localValue2.add(localValue3.x * localValue5, 0.0, localValue3.z * localValue5);
         Rotation localValue7 = this.internalMethod07076(localValue1, localValue6);
         ScaffoldModule.InternalType0337 localValue8 = localValue1.internalMethod00907(localValue7);
         if (localValue8.internalMethod01485() || this.internalMethod02375(this.internalMethod07424(localValue7, localValue6), localValue8)) {
            return localValue7;
         }
      }

      return null;
   }

   private Rotation internalMethod07076(ScaffoldModule.InternalType0337 localValue1, Vec3d localValue2) {
      if (this.internalField0668.internalMethod06103(this.internalField1066)) {
         return this.internalMethod06347(localValue1);
      } else if (this.internalField0668.internalMethod06103(this.internalField1067)) {
         return this.internalMethod07662(localValue1);
      } else {
         return this.internalField0668.internalMethod06103(this.internalField0238)
            ? this.internalMethod00971(localValue2, localValue1.internalMethod00110().toCenterPos())
            : localValue1.internalMethod04292();
      }
   }

   private Rotation internalMethod00237(ScaffoldModule.InternalType0337 localValue1) {
      if (localValue1 == null) {
         return null;
      } else if (this.internalField1066.isSelected()) {
         return this.internalMethod06347(localValue1);
      } else if (this.internalField1067.isSelected()) {
         return this.internalMethod07662(localValue1);
      } else {
         return this.internalField0238.isSelected()
            ? this.internalMethod00971(internalField0149.player.getEyePos(), localValue1.internalMethod00110().toCenterPos())
            : localValue1.internalMethod04292();
      }
   }

   private Rotation internalMethod06347(ScaffoldModule.InternalType0337 localValue1) {
      if (!this.internalField0894.internalMethod08927()) {
         return this.internalMethod08879(localValue1);
      } else {
         float localValue2 = this.internalMethod07313(this.internalMethod02297(this.internalField0894) + 180.0F);
         boolean localValue3 = Math.floorMod((int)localValue2, 90) == 0;
         if (!localValue3) {
            return new Rotation(localValue2, 75.6F);
         } else {
            if (internalField0149.player.isOnGround()) {
               double localValue4 = Math.toRadians(localValue2);
               this.internalField1100 = Math.floor(internalField0149.player.getX() + Math.cos(localValue4) * 0.5) != Math.floor(internalField0149.player.getX())
                  || Math.floor(internalField0149.player.getZ() + Math.sin(localValue4) * 0.5) != Math.floor(internalField0149.player.getZ());
               Vec3d localValue6 = internalField0149.player.getEntityPos().add(Math.cos(localValue4) * 0.6, 0.0, Math.sin(localValue4) * 0.6);
               boolean localValue7 = internalField0149.world.getBlockState(internalField0149.player.getBlockPos().down()).isAir();
               boolean localValue8 = internalField0149.world.getBlockState(BlockPos.ofFloored(localValue6).down()).isAir();
               if (localValue7 && localValue8) {
                  this.internalField1100 = !this.internalField1100;
               }
            }

            return new Rotation(localValue2 + (this.internalField1100 ? 45 : -45), 75.7F);
         }
      }
   }

   private Rotation internalMethod07662(ScaffoldModule.InternalType0337 localValue1) {
      if (!this.internalField0894.internalMethod08927()) {
         return this.internalMethod08879(localValue1);
      } else {
         float localValue2 = this.internalMethod07313(this.internalMethod02297(this.internalField0894) + 180.0F);
         boolean localValue3 = Math.floorMod((int)localValue2, 90) == 0;
         return new Rotation(localValue2, localValue3 ? 80.0F : 75.6F);
      }
   }

   private Rotation internalMethod08879(ScaffoldModule.InternalType0337 localValue1) {
      float localValue2 = (float)Math.floor(localValue1.internalMethod04292().internalMethod00169() / 90.0F) * 90.0F;
      return new Rotation(localValue2 + 45.0F, 75.0F);
   }

   private BlockHitResult internalMethod01583(ScaffoldModule.InternalType0337 localValue1, Rotation localValue2) {
      BlockHitResult localValue3 = this.internalMethod04898(localValue2);
      if (localValue3 != null && this.internalMethod03274(localValue3, localValue1)) {
         return localValue3;
      } else {
         return localValue1.internalMethod01485() ? localValue1.internalMethod04447() : null;
      }
   }

   private boolean internalMethod03274(BlockHitResult localValue1, ScaffoldModule.InternalType0337 localValue2) {
      return this.internalMethod02375(localValue1, localValue2) && this.internalMethod05849(localValue1);
   }

   private boolean internalMethod05849(BlockHitResult localValue1) {
      Vec3d localValue2 = localValue1.getPos().subtract(internalField0149.player.getEyePos());
      Direction localValue3 = localValue1.getSide();
      if (localValue3.getAxis() == Axis.Y) {
         return true;
      } else {
         double localValue4 = localValue3 != Direction.NORTH && localValue3 != Direction.SOUTH ? localValue2.x : localValue2.z;
         return Math.abs(localValue4) >= this.internalField0383.internalMethod08576();
      }
   }

   private Set<BlockPos> internalMethod06144(Vec3d localValue1) {
      BlockPos localValue2 = this.internalMethod05705(BlockPos.ofFloored(localValue1));
      ScaffoldModule.InternalType0336 localValue3 = this.internalMethod09652()
         ? ScaffoldModule.InternalType0336.internalField1233
         : ScaffoldModule.InternalType0336.internalField0607;
      if (this.internalField1594.internalMethod04496() && this.internalField0237.isSelected() && this.internalMethod09839()) {
         localValue3 = ScaffoldModule.InternalType0336.internalField0606;
      }

      Set localValue4 = this.internalMethod00664(localValue2, localValue3);
      Vec3d localValue5 = internalField0149.player.getVelocity();
      this.internalMethod04589(localValue4, localValue1.x + localValue5.x, localValue2.getY(), localValue1.z + localValue5.z);
      Box localValue6 = internalField0149.player.getBoundingBox().offset(localValue5.x, 0.0, localValue5.z);
      this.internalMethod04589(localValue4, localValue6.minX, localValue2.getY(), localValue6.minZ);
      this.internalMethod04589(localValue4, localValue6.minX, localValue2.getY(), localValue6.maxZ);
      this.internalMethod04589(localValue4, localValue6.maxX, localValue2.getY(), localValue6.minZ);
      this.internalMethod04589(localValue4, localValue6.maxX, localValue2.getY(), localValue6.maxZ);
      return localValue4;
   }

   private Set<BlockPos> internalMethod00664(BlockPos localValue1, ScaffoldModule.InternalType0336 localValue2) {
      LinkedHashSet localValue3 = new LinkedHashSet();
      localValue3.add(localValue1);
      localValue3.add(localValue1.north());
      localValue3.add(localValue1.south());
      localValue3.add(localValue1.east());
      localValue3.add(localValue1.west());
      if (localValue2 == ScaffoldModule.InternalType0336.internalField0606) {
         localValue3.add(localValue1.north().east());
         localValue3.add(localValue1.north().west());
         localValue3.add(localValue1.south().east());
         localValue3.add(localValue1.south().west());
         localValue3.add(localValue1.up());
         localValue3.add(localValue1.down());
      } else if (localValue2 == ScaffoldModule.InternalType0336.internalField1233) {
         localValue3.add(localValue1.down());
         localValue3.add(localValue1.down().north());
         localValue3.add(localValue1.down().south());
         localValue3.add(localValue1.down().east());
         localValue3.add(localValue1.down().west());
      }

      return localValue3;
   }

   private void internalMethod04589(Set<BlockPos> localValue1, double localValue2, double localValue4, double localValue6) {
      localValue1.add(BlockPos.ofFloored(localValue2, localValue4, localValue6));
   }

   private BlockPos internalMethod05705(BlockPos localValue1) {
      if (this.internalMethod09858() || this.internalField0277) {
         return this.internalMethod00787(localValue1);
      } else if (this.internalMethod09652()) {
         return localValue1.add(0, -2, 0);
      } else if (this.internalField1594.internalMethod04496() && this.internalField0237.isSelected() && this.internalMethod09839()) {
         return localValue1.add(0, 3, 0);
      } else if (!internalField0149.player.input.playerInput.jump() || this.internalMethod10001() && !internalField0149.player.horizontalCollision) {
         if (this.internalField0669.internalMethod06103(this.internalField1065)) {
            return new BlockPos(localValue1.getX(), this.internalField1053, localValue1.getZ());
         } else if (this.internalField0669.internalMethod06103(this.internalField1480)) {
            return internalField0149.player.getVelocity().y < 0.2 ? new BlockPos(localValue1.getX(), this.internalField1053, localValue1.getZ()) : localValue1.down();
         } else if (!this.internalField0669.internalMethod06103(this.internalField1481)) {
            return localValue1.down();
         } else if (internalField0149.player.getVelocity().y == -0.15233518685055708 && this.internalField1056 >= 2) {
            this.internalField1056 = 0;
            return new BlockPos(localValue1.getX(), this.internalField1055, localValue1.getZ());
         } else {
            return new BlockPos(localValue1.getX(), this.internalField1055 - 1, localValue1.getZ());
         }
      } else {
         return localValue1.down();
      }
   }

   private BlockPos internalMethod00787(BlockPos localValue1) {
      if (this.internalField1272.internalMethod06103(this.internalField1478) && !this.internalMethod10001()) {
         BlockPos[] localValue2 = new BlockPos[]{localValue1.add(0, 0, 1), localValue1.add(0, 0, -1), localValue1.add(1, 0, 0), localValue1.add(-1, 0, 0)};
         BlockPos localValue3 = null;
         double localValue4 = Double.MAX_VALUE;

         for (BlockPos localValue9 : localValue2) {
            double localValue10 = this.internalMethod03341(localValue9).squaredDistanceTo(internalField0149.player.getEntityPos());
            if (localValue10 < localValue4) {
               localValue4 = localValue10;
               localValue3 = localValue9.down();
            }
         }

         if (localValue3 != null && !internalField0149.world.getBlockState(localValue3).isSideSolidFullSquare(internalField0149.world, localValue3, Direction.UP)) {
            return localValue3;
         }
      }

      return localValue1.down();
   }

   private BlockPos internalMethod03433(Vec3d localValue1, int localValue2) {
      float localValue3 = internalField0149.player.getYaw();
      return BlockPos.ofFloored(localValue1).add((int)(-Math.sin(Math.toRadians(localValue3)) * localValue2), 0, (int)(Math.cos(Math.toRadians(localValue3)) * localValue2));
   }

   private Comparator<BlockPos> internalMethod03613(Vec3d localValue1, ScaffoldModule.InternalType0335 localValue2) {
      return (localValue3, localValue4) -> {
         if (localValue2 != null) {
            int localValue5 = Double.compare(localValue2.internalMethod01491(this.internalMethod03341(localValue3)), localValue2.internalMethod01491(this.internalMethod03341(localValue4)));
            if (localValue5 != 0) {
               return localValue5;
            }
         }

         return Double.compare(this.internalMethod03341(localValue3).squaredDistanceTo(localValue1), this.internalMethod03341(localValue4).squaredDistanceTo(localValue1));
      };
   }

   private ScaffoldModule.InternalType0197 internalMethod01304() {
      if (internalField0149.player != null && internalField0149.world != null) {
         int localValue1 = internalField0149.player.getInventory().getSelectedSlot();
         ItemStack localValue2 = internalField0149.player.getInventory().getStack(localValue1);
         if (this.internalMethod02151(localValue2)) {
            return new ScaffoldModule.InternalType0197(Hand.MAIN_HAND, localValue1, localValue2);
         } else if (this.internalMethod02151(internalField0149.player.getOffHandStack())) {
            return new ScaffoldModule.InternalType0197(Hand.OFF_HAND, -1, internalField0149.player.getOffHandStack());
         } else {
            return !this.internalField0651.internalMethod04496() ? null : this.internalMethod01495();
         }
      } else {
         return null;
      }
   }

   private ScaffoldModule.InternalType0197 internalMethod01495() {
      ScaffoldModule.InternalType0197 localValue1 = null;
      ScaffoldModule.InternalType0197 localValue2 = null;
      int localValue3 = (int)this.internalField1143.internalMethod08576();

      for (int localValue4 = 0; localValue4 < 9; localValue4++) {
         ItemStack localValue5 = internalField0149.player.getInventory().getStack(localValue4);
         if (this.internalMethod02151(localValue5)) {
            ScaffoldModule.InternalType0197 localValue6 = new ScaffoldModule.InternalType0197(Hand.MAIN_HAND, localValue4, localValue5);
            if (localValue2 == null || this.internalMethod04692(localValue6, localValue2) > 0) {
               localValue2 = localValue6;
            }

            if (localValue5.getCount() > localValue3 && (localValue1 == null || this.internalMethod04692(localValue6, localValue1) > 0)) {
               localValue1 = localValue6;
            }
         }
      }

      return localValue1 != null ? localValue1 : localValue2;
   }

   private int internalMethod04692(ScaffoldModule.InternalType0197 localValue1, ScaffoldModule.InternalType0197 localValue2) {
      return Integer.compare(this.internalMethod06705(localValue1.internalMethod07654(), true), this.internalMethod06705(localValue2.internalMethod07654(), true));
   }

   private int internalMethod06705(ItemStack localValue1, boolean localValue2) {
      Block localValue3 = ((BlockItem)localValue1.getItem()).getBlock();
      BlockState localValue4 = localValue3.getDefaultState();
      int localValue5 = 0;
      if (!this.internalMethod07333(localValue1)) {
         localValue5 += 1000000;
      }

      if (localValue4.isSideSolidFullSquare(internalField0149.world, BlockPos.ORIGIN, Direction.UP)) {
         localValue5 += 100000;
      }

      if (!localValue4.getCollisionShape(internalField0149.world, BlockPos.ORIGIN).isEmpty()) {
         localValue5 += 10000;
      }

      return localValue5 + (localValue2 ? localValue1.getCount() : 64 - localValue1.getCount());
   }

   private boolean internalMethod02151(ItemStack localValue1) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         if (localValue1.getItem() instanceof BlockItem localValue3) {
            Block localValue4 = localValue3.getBlock();
            if (this.internalMethod03455(localValue4)) {
               return false;
            } else {
               BlockState localValue5 = localValue4.getDefaultState();
               return localValue5.isSideSolidFullSquare(internalField0149.world, BlockPos.ORIGIN, Direction.UP)
                  && !localValue5.getCollisionShape(internalField0149.world, BlockPos.ORIGIN).isEmpty();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean internalMethod03455(Block localValue1) {
      return localValue1 instanceof FallingBlock || localValue1 == Blocks.TNT || localValue1 == Blocks.COBWEB || localValue1 == Blocks.NETHER_PORTAL || localValue1 == Blocks.POWDER_SNOW;
   }

   private boolean internalMethod07333(ItemStack localValue1) {
      if (!(localValue1.getItem() instanceof BlockItem localValue2)) {
         return true;
      } else {
         Block localValue5 = localValue2.getBlock();
         BlockState localValue4 = localValue5.getDefaultState();
         return localValue5.getSlipperiness() > 0.6F
            || localValue5 instanceof BlockWithEntity
            || localValue4.getCollisionShape(internalField0149.world, BlockPos.ORIGIN).isEmpty()
            || localValue5 == Blocks.CRAFTING_TABLE
            || localValue5 == Blocks.SMITHING_TABLE
            || localValue5 == Blocks.FLETCHING_TABLE
            || localValue5 == Blocks.ENCHANTING_TABLE
            || localValue5 == Blocks.CAULDRON
            || localValue5 == Blocks.MAGMA_BLOCK;
      }
   }

   private int internalMethod09094() {
      int localValue1 = this.internalMethod02151(internalField0149.player.getOffHandStack()) ? internalField0149.player.getOffHandStack().getCount() : 0;
      if (!this.internalField0651.internalMethod04496()) {
         ItemStack localValue4 = internalField0149.player.getInventory().getStack(internalField0149.player.getInventory().getSelectedSlot());
         return localValue1 + (this.internalMethod02151(localValue4) ? localValue4.getCount() : 0);
      } else {
         for (int localValue2 = 0; localValue2 < 9; localValue2++) {
            ItemStack localValue3 = internalField0149.player.getInventory().getStack(localValue2);
            if (this.internalMethod02151(localValue3)) {
               localValue1 += localValue3.getCount();
            }
         }

         return localValue1;
      }
   }

   private boolean internalMethod05350(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      return localValue2.isAir() || localValue2.getCollisionShape(internalField0149.world, localValue1).isEmpty() && internalField0149.world.getFluidState(localValue1).isEmpty();
   }

   private boolean internalMethod05971(BlockPos localValue1, Direction localValue2) {
      BlockState localValue3 = internalField0149.world.getBlockState(localValue1);
      return !localValue3.isAir() && !localValue3.getCollisionShape(internalField0149.world, localValue1).isEmpty() && localValue3.isSideSolidFullSquare(internalField0149.world, localValue1, localValue2);
   }

   private boolean internalMethod03694(ScaffoldModule.InternalType0337 localValue1) {
      double localValue2 = internalField0149.player.getBlockInteractionRange();
      return this.internalMethod05350(localValue1.internalMethod00110())
         && this.internalMethod05971(localValue1.internalMethod03865(), localValue1.internalMethod05109())
         && localValue1.internalMethod03865().offset(localValue1.internalMethod05109()).equals(localValue1.internalMethod00110())
         && internalField0149.player.getEyePos().squaredDistanceTo(localValue1.internalMethod03524()) <= localValue2 * localValue2;
   }

   private void internalMethod03190(Rotation localValue1) {
      float localValue2 = this.internalField0382.internalMethod08576();
      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(
            new Rotation(localValue1.internalMethod00169(), localValue1.internalMethod00171()),
            this.internalMethod03910(),
            localValue2,
            localValue2,
            localValue2,
            RotationPriority.internalField1012
         );
   }

   private RotationBehavior internalMethod03910() {
      if (this.internalField1271.internalMethod06103(this.internalField1760)) {
         return RotationBehavior.internalField1004;
      } else if (this.internalField1271.internalMethod06103(this.internalField1759)) {
         return RotationBehavior.internalField1003;
      } else {
         return this.internalField1271.internalMethod06103(this.internalField1763) ? RotationBehavior.internalField1001 : RotationBehavior.internalField0115;
      }
   }

   private boolean internalMethod06372(Rotation localValue1) {
      Rotation localValue2 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      float localValue3 = Math.abs(MathHelper.wrapDegrees(localValue2.internalMethod00169() - localValue1.internalMethod00169()));
      float localValue4 = Math.abs(localValue2.internalMethod00171() - localValue1.internalMethod00171());
      float localValue5 = this.internalField1142.internalMethod08576();
      return localValue3 <= localValue5 && localValue4 <= localValue5;
   }

   private BlockHitResult internalMethod04898(Rotation localValue1) {
      return this.internalMethod07424(localValue1, internalField0149.player.getEyePos());
   }

   private BlockHitResult internalMethod07424(Rotation localValue1, Vec3d localValue2) {
      Vec3d localValue3 = localValue2.add(
         internalField0149.player
            .getRotationVector(localValue1.internalMethod00171(), localValue1.internalMethod00169())
            .multiply(internalField0149.player.getBlockInteractionRange())
      );
      return internalField0149.world.raycast(new RaycastContext(localValue2, localValue3, ShapeType.OUTLINE, FluidHandling.NONE, internalField0149.player));
   }

   private Rotation internalMethod00971(Vec3d localValue1, Vec3d localValue2) {
      double localValue3 = localValue2.x - localValue1.x;
      double localValue5 = localValue2.y - localValue1.y;
      double localValue7 = localValue2.z - localValue1.z;
      double localValue9 = Math.sqrt(localValue3 * localValue3 + localValue7 * localValue7);
      float localValue11 = (float)Math.toDegrees(Math.atan2(localValue7, localValue3)) - 90.0F;
      float localValue12 = (float)(-Math.toDegrees(Math.atan2(localValue5, localValue9)));
      return new Rotation(localValue11, MathHelper.clamp(localValue12, -90.0F, 90.0F));
   }

   private boolean internalMethod02375(BlockHitResult localValue1, ScaffoldModule.InternalType0337 localValue2) {
      if (localValue1 != null && localValue1.getType() == Type.BLOCK) {
         BlockPos localValue3 = localValue1.getBlockPos();
         Direction localValue4 = localValue1.getSide();
         BlockPos localValue5 = localValue3.offset(localValue4);
         return localValue3.equals(localValue2.internalMethod03865()) && localValue4 == localValue2.internalMethod05109() && localValue5.equals(localValue2.internalMethod00110())
            ? this.internalMethod05350(localValue5) && this.internalMethod05971(localValue3, localValue4)
            : false;
      } else {
         return false;
      }
   }

   private List<Vec3d> internalMethod05964(BlockPos localValue1, Direction localValue2, Vec3d localValue3) {
      ArrayList localValue4 = new ArrayList();
      double localValue5 = this.internalField1269.internalMethod06103(this.internalField1750) ? 0.04 : 0.18;
      double localValue7 = this.internalField1269.internalMethod06103(this.internalField1750) ? 0.96 : 0.82;
      double localValue9 = MathHelper.clamp(localValue3.x - localValue1.getX(), localValue5, localValue7);
      double localValue11 = MathHelper.clamp(localValue3.y - localValue1.getY(), localValue5, localValue7);
      double localValue13 = MathHelper.clamp(localValue3.z - localValue1.getZ(), localValue5, localValue7);
      double localValue15 = 0.5;
      double localValue17 = 0.82;
      if (this.internalField1269.internalMethod06103(this.internalField1749)) {
         localValue9 = localValue15;
         localValue11 = localValue15;
         localValue13 = localValue15;
      } else if (this.internalField1269.internalMethod06103(this.internalField1761)) {
         localValue9 = this.internalMethod05168(localValue5, localValue7);
         localValue11 = this.internalMethod05168(localValue5, localValue7);
         localValue13 = this.internalMethod05168(localValue5, localValue7);
      } else if (this.internalField1269.internalMethod06103(this.internalField1753)) {
         localValue9 = 1.0 - localValue9;
         localValue13 = 1.0 - localValue13;
      } else if (this.internalField1269.internalMethod06103(this.internalField1754)) {
         localValue9 = localValue9 < 0.5 ? localValue5 : localValue7;
         localValue13 = localValue13 < 0.5 ? localValue5 : localValue7;
      } else if (this.internalField1269.internalMethod06103(this.internalField1755)) {
         float localValue19 = MathHelper.wrapDegrees(internalField0149.player.getYaw());
         localValue9 = Math.sin(Math.toRadians(localValue19)) > 0.0 ? localValue7 : localValue5;
         localValue13 = Math.cos(Math.toRadians(localValue19)) > 0.0 ? localValue7 : localValue5;
      }

      if (localValue2.getAxis() == Axis.X) {
         double localValue21 = localValue1.getX() + (localValue2 == Direction.EAST ? 1 : 0);
         this.internalMethod00747(localValue4, localValue21, localValue1.getY() + localValue11, localValue1.getZ() + localValue13);
         this.internalMethod00747(localValue4, localValue21, localValue1.getY() + localValue17, localValue1.getZ() + localValue13);
         this.internalMethod00747(localValue4, localValue21, localValue1.getY() + localValue11, localValue1.getZ() + localValue15);
         this.internalMethod00747(localValue4, localValue21, localValue1.getY() + localValue17, localValue1.getZ() + localValue15);
         this.internalMethod00747(localValue4, localValue21, localValue1.getY() + localValue15, localValue1.getZ() + localValue15);
      } else if (localValue2.getAxis() == Axis.Y) {
         double localValue22 = localValue1.getY() + (localValue2 == Direction.UP ? 1 : 0);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue9, localValue22, localValue1.getZ() + localValue13);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue9, localValue22, localValue1.getZ() + localValue15);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue15, localValue22, localValue1.getZ() + localValue13);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue15, localValue22, localValue1.getZ() + localValue15);
      } else {
         double localValue23 = localValue1.getZ() + (localValue2 == Direction.SOUTH ? 1 : 0);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue9, localValue1.getY() + localValue11, localValue23);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue9, localValue1.getY() + localValue17, localValue23);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue15, localValue1.getY() + localValue11, localValue23);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue15, localValue1.getY() + localValue17, localValue23);
         this.internalMethod00747(localValue4, localValue1.getX() + localValue15, localValue1.getY() + localValue15, localValue23);
      }

      if (this.internalField1269.internalMethod06103(this.internalField1752)) {
         Rotation localValue24 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
         localValue4.sort(Comparator.comparingDouble((Vec3d localValue3x) -> this.internalMethod03531(localValue24, this.internalMethod00971(localValue3, localValue3x))));
      } else if (this.internalField1269.internalMethod06103(this.internalField1751) && this.internalField0605 != null) {
         localValue4.sort(Comparator.comparingDouble((Vec3d localValue2x) -> this.internalField0605.internalMethod01491(localValue2x) + localValue2x.squaredDistanceTo(localValue3) * 0.05));
      } else {
         localValue4.sort(Comparator.comparingDouble((Vec3d localValue1x) -> localValue1x.squaredDistanceTo(localValue3)));
      }

      return localValue4;
   }

   private void internalMethod00747(List<Vec3d> localValue1, double localValue2, double localValue4, double localValue6) {
      Vec3d localValue8 = new Vec3d(localValue2, localValue4, localValue6);
      if (!localValue1.contains(localValue8)) {
         localValue1.add(localValue8);
      }
   }

   private double internalMethod03531(Rotation localValue1, Rotation localValue2) {
      return Math.abs(MathHelper.wrapDegrees(localValue1.internalMethod00169() - localValue2.internalMethod00169()))
         + Math.abs(localValue1.internalMethod00171() - localValue2.internalMethod00171());
   }

   private void internalMethod02298(ScaffoldModule.InternalType0197 localValue1, ScaffoldModule.InternalType0337 localValue2, BlockHitResult localValue3) {
      if (this.internalField1592.internalMethod04496()) {
         internalField0149.options.sprintKey.setPressed(false);
         if (this.internalField1099 || internalField0149.player.isSprinting()) {
            SprintResetManager.internalMethod06906(internalField0149.player, () -> this.internalMethod02666(localValue1, localValue2, localValue3), true);
            this.internalField1099 = false;
            return;
         }

         internalField0149.player.setSprinting(false);
      }

      this.internalMethod02666(localValue1, localValue2, localValue3);
   }

   private void internalMethod02666(ScaffoldModule.InternalType0197 localValue1, ScaffoldModule.InternalType0337 localValue2, BlockHitResult localValue3) {
      if (GameUtils.internalMethod00471()) {
         if (localValue1.internalMethod04621()) {
            this.internalMethod08676(localValue1.internalMethod04620());
         }

         ActionResult localValue4 = internalField0149.interactionManager.interactBlock(internalField0149.player, localValue1.internalMethod07192(), localValue3);
         if (localValue4.isAccepted()) {
            internalField0149.player.swingHand(localValue1.internalMethod07192());
            this.internalMethod05349(localValue2.internalMethod00110());
         }
      }
   }

   private void internalMethod05349(BlockPos localValue1) {
      this.internalMethod06646(localValue1);
      this.internalMethod03195(this.internalField0605, this.internalField0605 == null ? null : this.internalMethod07422(this.internalField0605));
      this.internalMethod10003();
      this.internalField0519.internalMethod00701();
      this.internalMethod10000();
   }

   private void internalMethod04908(ScaffoldModule.InternalType0337 localValue1, ScaffoldModule.InternalType0197 localValue2) {
      if (this.internalField1593.internalMethod04496() && this.internalMethod10001() && localValue2 != null && localValue1 != null) {
         if (!this.internalField1811.internalMethod04496() || !localValue1.internalMethod01485()) {
            if (ThreadLocalRandom.current().nextInt(3) == 0) {
               internalField0149.player.swingHand(localValue2.internalMethod07192());
            }
         }
      }
   }

   private void internalMethod03693(ScaffoldModule.InternalType0337 localValue1) {
      if (!localValue1.internalMethod00110().equals(this.internalField0352)
         || !localValue1.internalMethod03865().equals(this.internalField0351)
         || localValue1.internalMethod05109() != this.internalField0150) {
         this.internalField1464 = 0;
      }

      this.internalField0608 = localValue1;
      this.internalField0352 = localValue1.internalMethod00110();
      this.internalField0351 = localValue1.internalMethod03865();
      this.internalField0150 = localValue1.internalMethod05109();
   }

   private void internalMethod10000() {
      this.internalField0608 = null;
      this.internalField0893 = null;
      this.internalField0352 = null;
      this.internalField0351 = null;
      this.internalField0150 = null;
      this.internalField1464 = 0;
   }

   private void internalMethod10002() {
      this.internalMethod10000();
      this.internalField0881.clear();
      this.internalField0880.clear();
      this.internalField0605 = null;
      this.internalField0894 = ScaffoldModule.InternalType0198.internalField0894;
      this.internalField0793 = null;
      this.internalField1131 = null;
      this.internalField0205 = Float.NaN;
      this.internalField1054 = 0;
      this.internalField1470 = 0;
      this.internalField1463 = 0;
      this.internalField1045 = Double.NaN;
      this.internalField0277 = false;
      this.internalField1100 = false;
      this.internalField1048 = 0.0F;
      this.internalField0229 = 0L;
   }

   ScaffoldModule.InternalType0335 internalMethod04119(ScaffoldModule.InternalType0198 localValue1) {
      Vec3d localValue2 = this.internalMethod01435(this.internalMethod02297(localValue1));
      ScaffoldModule.InternalType0192 localValue3 = this.internalMethod06379();
      if (localValue3 == null) {
         return null;
      } else {
         this.internalField0793 = localValue3;
         ScaffoldModule.InternalType0335 localValue4 = this.internalMethod03952();
         Vec3d localValue5;
         if (localValue4 != null && localValue4.internalMethod05098().dotProduct(localValue2) >= 0.5) {
            localValue5 = localValue4.internalMethod04602(internalField0149.player.getEntityPos());
         } else {
            localValue5 = new Vec3d(
               localValue3.internalMethod02469().getX() + 0.5 + localValue3.internalMethod02505(),
               internalField0149.player.getY(),
               localValue3.internalMethod02469().getZ() + 0.5 + localValue3.internalMethod02524()
            );
         }

         return new ScaffoldModule.InternalType0335(new Vec3d(localValue5.x, internalField0149.player.getY(), localValue5.z), localValue2);
      }
   }

   private ScaffoldModule.InternalType0335 internalMethod03952() {
      if (this.internalField0881.size() < 2) {
         return null;
      } else {
         BlockPos localValue1 = null;
         BlockPos localValue2 = null;

         for (BlockPos localValue4 : this.internalField0881) {
            localValue2 = localValue1;
            localValue1 = localValue4;
         }

         if (localValue1 != null && localValue2 != null) {
            Vec3d localValue7 = this.internalMethod03223(localValue2);
            Vec3d localValue8 = this.internalMethod03223(localValue1);
            Vec3d localValue5 = localValue8.subtract(localValue7).normalize();
            Vec3d localValue6 = localValue7.add(localValue8).multiply(0.5);
            return new ScaffoldModule.InternalType0335(localValue6, localValue5);
         } else {
            return null;
         }
      }
   }

   private ScaffoldModule.InternalType0192 internalMethod06379() {
      List localValue1 = this.internalMethod02258();
      if (localValue1.isEmpty()) {
         this.internalField0793 = null;
         this.internalField1131 = null;
         return null;
      } else {
         localValue1.sort(null);
         ScaffoldModule.InternalType0191 localValue2 = (ScaffoldModule.InternalType0191)localValue1.getFirst();
         ScaffoldModule.InternalType0191 localValue3 = this.internalMethod00093(localValue1, localValue2);
         this.internalField1131 = localValue3.internalMethod01481();
         return new ScaffoldModule.InternalType0192(
            localValue3.internalMethod01481(),
            internalField0149.player.getX() - (localValue3.internalMethod01481().getX() + 0.5),
            internalField0149.player.getZ() - (localValue3.internalMethod01481().getZ() + 0.5)
         );
      }
   }

   private List<ScaffoldModule.InternalType0191> internalMethod02258() {
      ArrayList localValue1 = new ArrayList();
      LinkedHashSet localValue2 = new LinkedHashSet();
      double[] localValue3 = new double[]{0.301, 0.0, -0.301};

      for (double localValue7 : localValue3) {
         for (double localValue12 : localValue3) {
            BlockPos localValue14 = BlockPos.ofFloored(
               internalField0149.player.getX() + localValue7, internalField0149.player.getY() - 1.0, internalField0149.player.getZ() + localValue12
            );
            if (localValue2.add(localValue14) && !internalField0149.world.getBlockState(localValue14).getCollisionShape(internalField0149.world, localValue14).isEmpty()) {
               localValue1.add(this.internalMethod07372(localValue14));
            }
         }
      }

      return localValue1;
   }

   private ScaffoldModule.InternalType0191 internalMethod00093(List<ScaffoldModule.InternalType0191> localValue1, ScaffoldModule.InternalType0191 localValue2) {
      ScaffoldModule.InternalType0191 localValue3 = null;
      ScaffoldModule.InternalType0191 localValue4 = null;
      BlockPos localValue5 = this.internalField0881.peekLast();

      for (ScaffoldModule.InternalType0191 localValue7 : localValue1) {
         if (Objects.equals(localValue7.internalMethod01481(), localValue5)) {
            localValue3 = localValue7;
         }

         if (Objects.equals(localValue7.internalMethod01481(), this.internalField1131)) {
            localValue4 = localValue7;
         }
      }

      if (localValue3 != null && localValue3.internalMethod02434(localValue2)) {
         return localValue3;
      } else {
         return localValue4 != null && localValue4.internalMethod02434(localValue2) ? localValue4 : localValue2;
      }
   }

   private ScaffoldModule.InternalType0191 internalMethod07372(BlockPos localValue1) {
      Box localValue2 = internalField0149.player.getBoundingBox();
      List localValue3 = internalField0149.world.getBlockState(localValue1).getCollisionShape(internalField0149.world, localValue1).getBoundingBoxes();
      double localValue4 = Double.POSITIVE_INFINITY;
      double localValue6 = 0.0;

      for (Box localValue9 : (Iterable<Box>)(Iterable<?>)localValue3) {
         Box localValue10 = localValue9.offset(localValue1);
         double localValue11 = Math.min(localValue2.maxX, localValue10.maxX) - Math.max(localValue2.minX, localValue10.minX);
         double localValue13 = Math.min(localValue2.maxZ, localValue10.maxZ) - Math.max(localValue2.minZ, localValue10.minZ);
         if (!(localValue11 <= 0.0) && !(localValue13 <= 0.0)) {
            double localValue15 = Math.abs(localValue2.minY - localValue10.maxY);
            double localValue17 = localValue11 * localValue13;
            if (localValue15 + 0.001 < localValue4) {
               localValue4 = localValue15;
               localValue6 = localValue17;
            } else if (Math.abs(localValue15 - localValue4) <= 0.001) {
               localValue6 += localValue17;
            }
         }
      }

      return new ScaffoldModule.InternalType0191(localValue1, localValue6, localValue4, this.internalMethod06339(this.internalMethod03341(localValue1), internalField0149.player.getEntityPos()));
   }

   private Vec3d internalMethod01435(float localValue1) {
      if (!Float.isNaN(this.internalField0205) && MathHelper.angleBetween(localValue1, this.internalField0205) <= 30.0F) {
         return this.internalMethod00200(this.internalField0205);
      } else {
         float localValue2 = localValue1 / 180.0F * 4.0F + 4.0F;
         float localValue3 = Math.round(localValue2);
         float localValue4 = MathHelper.wrapDegrees((localValue3 - 4.0F) / 4.0F * 180.0F);
         this.internalField0205 = localValue4;
         return this.internalMethod00200(localValue4);
      }
   }

   private void internalMethod06646(BlockPos localValue1) {
      if (!localValue1.equals(this.internalField0881.peekLast())) {
         while (this.internalField0881.size() >= 4) {
            this.internalField0881.removeFirst();
         }

         this.internalField0881.add(localValue1);
      }
   }

   private Vec3d internalMethod00973(ScaffoldModule.InternalType0335 localValue1) {
      if (localValue1 != null && !this.internalMethod07312(0.05)) {
         Vec3d localValue2 = this.internalMethod07422(localValue1);
         if (localValue2 == null) {
            return null;
         } else {
            Vec3d localValue3 = internalField0149.player.getEntityPos();
            Vec3d localValue4 = localValue2.subtract(localValue3);
            Vec3d localValue5 = this.internalMethod06682(localValue2, localValue4);
            Vec3d localValue6 = this.internalMethod02408();
            if (localValue6 != null) {
               float localValue7 = (float)Math.atan2(localValue1.internalMethod05098().z, localValue1.internalMethod05098().x);
               Vec3d localValue8 = localValue2.add(this.internalMethod00212(localValue6, -localValue7));
               return this.internalMethod03595(localValue5, localValue8, this.internalMethod04834());
            } else {
               return this.internalField0793 != null ? localValue5.add(this.internalField0793.internalMethod02505(), 0.0, this.internalField0793.internalMethod02524()) : localValue5;
            }
         }
      } else {
         return null;
      }
   }

   private void internalMethod03195(ScaffoldModule.InternalType0335 localValue1, Vec3d localValue2) {
      if (localValue1 != null && localValue2 != null) {
         float localValue3 = (float)Math.atan2(localValue1.internalMethod05098().z, localValue1.internalMethod05098().x);
         Vec3d localValue4 = this.internalMethod00212(internalField0149.player.getEntityPos().subtract(localValue2), localValue3);
         this.internalField0880.addLast(localValue4);

         while (this.internalField0880.size() > 4) {
            this.internalField0880.removeFirst();
         }
      }
   }

   private Vec3d internalMethod02408() {
      if (this.internalField0880.isEmpty()) {
         return null;
      } else {
         double localValue1 = 0.0;
         double localValue3 = 0.0;
         double localValue5 = 0.0;

         for (Vec3d localValue8 : this.internalField0880) {
            localValue1 += localValue8.x;
            localValue3 += localValue8.y;
            localValue5 += localValue8.z;
         }

         double localValue9 = this.internalField0880.size();
         return new Vec3d(localValue1 / localValue9, localValue3 / localValue9, localValue5 / localValue9);
      }
   }

   private Vec3d internalMethod07422(ScaffoldModule.InternalType0335 localValue1) {
      Vec3d localValue2 = localValue1.internalMethod04602(internalField0149.player.getEntityPos()).add(0.0, -0.1, 0.0);
      Vec3d localValue3 = localValue1.internalMethod05098().normalize();
      Vec3d localValue4 = localValue2;

      for (double localValue5 = 0.0; localValue5 <= 3.0; localValue5 += 0.05) {
         Vec3d localValue7 = localValue2.add(localValue3.multiply(localValue5));
         if (!this.internalMethod05169(localValue7.x, localValue7.z)) {
            return new Vec3d(localValue4.x, internalField0149.player.getY(), localValue4.z);
         }

         localValue4 = localValue7;
      }

      return null;
   }

   private Vec3d internalMethod06682(Vec3d localValue1, Vec3d localValue2) {
      double localValue3 = 0.2;
      return !(localValue3 <= 0.0) && !(localValue2.lengthSquared() < 1.0E-6) ? localValue1.subtract(localValue2.normalize().multiply(localValue3)) : localValue1;
   }

   private double internalMethod04834() {
      byte localValue1 = 2;
      return MathHelper.clamp((double)this.internalField0880.size() / localValue1, 0.0, 1.0);
   }

   ScaffoldModule.InternalType0334 internalMethod03951() {
      if (this.internalField1263.internalMethod04496() && internalField0149.player != null) {
         Rotation localValue1 = this.internalMethod00237(this.internalField0608);
         if (localValue1 == null) {
            localValue1 = RockstarClient.getInstance().internalMethod02368().internalMethod00024();
         }

         if (this.internalMethod07312(0.05)) {
            int localValue2 = this.internalMethod06370(localValue1);
            boolean localValue3 = this.internalMethod09094() <= 0;
            boolean localValue4 = localValue2 >= 1;
            if (localValue3 || localValue4) {
               return new ScaffoldModule.InternalType0334(false, Math.max(1, localValue2), false, false);
            }
         }

         if (!this.internalField1066.isSelected() || !this.internalMethod09650()) {
            return ScaffoldModule.InternalType0334.internalField0604;
         } else if (this.internalMethod09094() < this.internalField1535.internalMethod08576()) {
            return new ScaffoldModule.InternalType0334(false, this.internalMethod00884(this.internalField1275), false, false);
         } else {
            int localValue5 = ThreadLocalRandom.current().nextInt(4);

            return switch (localValue5) {
               case 0 -> new ScaffoldModule.InternalType0334(true, 0, false, false);
               case 1 -> new ScaffoldModule.InternalType0334(false, this.internalMethod00884(this.internalField1275), false, false);
               case 2 -> new ScaffoldModule.InternalType0334(false, 0, true, false);
               default -> new ScaffoldModule.InternalType0334(false, 0, false, true);
            };
         }
      } else {
         return ScaffoldModule.InternalType0334.internalField0604;
      }
   }

   private int internalMethod06370(Rotation localValue1) {
      Rotation localValue2 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      float localValue3 = Math.max(1.0F, this.internalField0382.internalMethod08576());
      double localValue4 = this.internalMethod03531(localValue2, localValue1);
      return (int)Math.ceil(localValue4 / localValue3);
   }

   private boolean internalMethod09650() {
      Vec3d localValue1 = internalField0149.player.getVelocity();
      Vec3d localValue2 = internalField0149.player.getEntityPos().add(localValue1.x, 0.0, localValue1.z);
      return !this.internalMethod05169(localValue2.x, localValue2.z);
   }

   boolean internalMethod04531(InputEvent localValue1) {
      if (this.internalMethod09837()) {
         return false;
      } else if (!internalField0149.player.isOnGround() && this.internalField1264.internalMethod04496()) {
         return false;
      } else if (!internalField0149.player.getAbilities().flying && this.internalField1470 == 0) {
         ScaffoldModule.InternalType0198 localValue2 = ScaffoldModule.InternalType0198.internalMethod05766(localValue1);
         return this.internalMethod03689(localValue2, this.internalField0206);
      } else {
         return false;
      }
   }

   private void internalMethod10003() {
      if (this.internalField1262.internalMethod04496()) {
         this.internalField1470++;
         if (this.internalField1470 > this.internalField1465) {
            this.internalMethod10057();
         }
      }
   }

   private void internalMethod10057() {
      this.internalField1470 = 0;
      this.internalField1465 = this.internalMethod00884(this.internalField0673);
      this.internalField0206 = this.internalMethod00883(this.internalField1274);
   }

   void internalMethod04530(InputEvent localValue1) {
      if (this.internalField0605 != null && (!localValue1.isJump() || !internalField0149.player.isOnGround())) {
         Vec3d localValue2 = this.internalField0605.internalMethod04602(internalField0149.player.getEntityPos());
         Vec3d localValue3 = localValue2.subtract(internalField0149.player.getEntityPos());
         Vec3d localValue4 = new Vec3d(internalField0149.player.getVelocity().x, 0.0, internalField0149.player.getVelocity().z);
         boolean localValue5 = localValue3.dotProduct(localValue4) > 0.0;
         double localValue6 = localValue5 ? 0.075 : 0.2;
         if (!(localValue2.squaredDistanceTo(internalField0149.player.getEntityPos()) < localValue6 * localValue6)) {
            ScaffoldModule.InternalType0198 localValue8 = ScaffoldModule.InternalType0198.internalMethod05766(localValue1);
            ScaffoldModule.InternalType0198 localValue9 = this.internalMethod06289(localValue3, internalField0149.player.getYaw());
            boolean localValue10 = localValue8.internalMethod06296() || localValue8.internalMethod06299();
            boolean localValue11 = localValue8.internalMethod08920() || localValue8.internalMethod08922();
            localValue1.setForward(localValue10 ? localValue1.getForward() : localValue9.internalMethod06295());
            localValue1.setStrafe(localValue11 ? localValue1.getStrafe() : localValue9.internalMethod06298());
         }
      }
   }

   void internalMethod00052(InputEvent localValue1) {
      if (!localValue1.isSneak() && localValue1.getForward() > 0.0F) {
         if (internalField0149.world.getBlockState(internalField0149.player.getBlockPos().down()).isAir()) {
            this.internalField0229 = System.currentTimeMillis();
         } else if (System.currentTimeMillis() - this.internalField0229 > 500L) {
            return;
         }

         double localValue2 = internalField0149.player.getX() - Math.floor(internalField0149.player.getX());
         double localValue4 = internalField0149.player.getZ() - Math.floor(internalField0149.player.getZ());
         double localValue6 = this.internalField1047;
         double localValue8 = 1.0 - localValue6;
         float localValue10 = 0.0F;
         Direction localValue11 = this.internalMethod00159(internalField0149.player.getYaw());
         if (localValue11 == Direction.SOUTH) {
            if (localValue2 > localValue8) {
               localValue10 = 1.0F;
            }

            if (localValue2 < localValue6) {
               localValue10 = -1.0F;
            }
         } else if (localValue11 == Direction.NORTH) {
            if (localValue2 > localValue8) {
               localValue10 = -1.0F;
            }

            if (localValue2 < localValue6) {
               localValue10 = 1.0F;
            }
         } else if (localValue11 == Direction.EAST) {
            if (localValue4 > localValue8) {
               localValue10 = -1.0F;
            }

            if (localValue4 < localValue6) {
               localValue10 = 1.0F;
            }
         } else if (localValue11 == Direction.WEST) {
            if (localValue4 > localValue8) {
               localValue10 = 1.0F;
            }

            if (localValue4 < localValue6) {
               localValue10 = -1.0F;
            }
         }

         if (this.internalField1048 != localValue10 && localValue10 != 0.0F) {
            this.internalField1048 = localValue10;
            this.internalField1047 = this.internalMethod00883(this.internalField1273);
         }

         localValue1.setStrafe(this.internalField1048);
      }
   }

   private boolean internalMethod09652() {
      return this.internalField1587.internalMethod04496() && this.internalField0237.isSelected() && internalField0149.options.sneakKey.isPressed();
   }

   boolean internalMethod09837() {
      BlockPos localValue1 = internalField0149.player.getBlockPos().add(0, -2, 0);
      return this.internalMethod09652() && internalField0149.world.getBlockState(localValue1).isSideSolidFullSquare(internalField0149.world, localValue1, Direction.UP);
   }

   private boolean internalMethod09839() {
      return !internalField0149.world.getBlockState(internalField0149.player.getBlockPos().down()).isAir();
   }

   private boolean internalMethod09856() {
      BlockPos localValue1 = internalField0149.player.getBlockPos().up(2);
      return !internalField0149.world.getBlockState(localValue1).getCollisionShape(internalField0149.world, localValue1).isEmpty() && internalField0149.player.isOnGround();
   }

   private boolean internalMethod09858() {
      boolean localValue1 = !this.internalField1272.internalMethod06103(this.internalField1483) && internalField0149.options.jumpKey.isPressed();
      if (localValue1) {
         this.internalField0277 = true;
      }

      return localValue1;
   }

   private ModeSetting.InternalType0088 internalMethod04139() {
      return this.internalMethod09858() ? this.internalField0237 : this.internalField0668.internalMethod07418();
   }

   private boolean internalMethod09999() {
      Box localValue1 = internalField0149.player.getBoundingBox().expand(0.5, 0.0, 0.5).offset(0.0, -1.05, 0.0);
      return this.internalMethod05425(localValue1);
   }

   private boolean internalMethod07312(double localValue1) {
      if (internalField0149.player != null && internalField0149.world != null) {
         Box localValue3 = internalField0149.player.getBoundingBox();
         return !this.internalMethod05425(localValue3.offset(localValue1, -0.05, 0.0))
            || !this.internalMethod05425(localValue3.offset(-localValue1, -0.05, 0.0))
            || !this.internalMethod05425(localValue3.offset(0.0, -0.05, localValue1))
            || !this.internalMethod05425(localValue3.offset(0.0, -0.05, -localValue1));
      } else {
         return false;
      }
   }

   private boolean internalMethod03689(ScaffoldModule.InternalType0198 localValue1, double localValue2) {
      Vec3d localValue4 = localValue1.internalMethod02891(internalField0149.player.getYaw());
      if (localValue4.lengthSquared() < 1.0E-6) {
         return this.internalMethod07312(localValue2);
      } else {
         Box localValue5 = internalField0149.player.getBoundingBox().offset(localValue4.x * localValue2, -0.05, localValue4.z * localValue2);
         return !this.internalMethod05425(localValue5);
      }
   }

   private boolean internalMethod05425(Box localValue1) {
      double localValue2 = internalField0149.player.getWidth() / 6.0;
      double localValue4 = (localValue1.minX + localValue1.maxX) * 0.5;
      double localValue6 = (localValue1.minZ + localValue1.maxZ) * 0.5;
      return this.internalMethod05169(localValue1.minX + localValue2, localValue1.minZ + localValue2)
         || this.internalMethod05169(localValue4, localValue1.minZ + localValue2)
         || this.internalMethod05169(localValue1.maxX - localValue2, localValue1.minZ + localValue2)
         || this.internalMethod05169(localValue1.minX + localValue2, localValue6)
         || this.internalMethod05169(localValue4, localValue6)
         || this.internalMethod05169(localValue1.maxX - localValue2, localValue6)
         || this.internalMethod05169(localValue1.minX + localValue2, localValue1.maxZ - localValue2)
         || this.internalMethod05169(localValue4, localValue1.maxZ - localValue2)
         || this.internalMethod05169(localValue1.maxX - localValue2, localValue1.maxZ - localValue2);
   }

   private boolean internalMethod05169(double localValue1, double localValue3) {
      BlockPos localValue5 = BlockPos.ofFloored(localValue1, internalField0149.player.getBoundingBox().minY - 0.001, localValue3);
      BlockState localValue6 = internalField0149.world.getBlockState(localValue5);
      return !localValue6.isAir() && !localValue6.getCollisionShape(internalField0149.world, localValue5).isEmpty();
   }

   private void internalMethod07311(double localValue1) {
      if (internalField0149.player != null) {
         double localValue3 = rockstar.client.compat.InputCompat.forward(internalField0149.player.input);
         double localValue5 = rockstar.client.compat.InputCompat.sideways(internalField0149.player.input);
         float localValue7 = internalField0149.player.getYaw();
         if (localValue3 != 0.0 || localValue5 != 0.0) {
            if (localValue3 != 0.0) {
               if (localValue5 > 0.0) {
                  localValue7 += localValue3 > 0.0 ? -45.0F : 45.0F;
               } else if (localValue5 < 0.0) {
                  localValue7 += localValue3 > 0.0 ? 45.0F : -45.0F;
               }

               localValue5 = 0.0;
               localValue3 = localValue3 > 0.0 ? 1.0 : -1.0;
            }

            double localValue8 = Math.sin(Math.toRadians(localValue7 + 90.0));
            double localValue10 = Math.cos(Math.toRadians(localValue7 + 90.0));
            double localValue12 = localValue3 * localValue1 * localValue10 + localValue5 * localValue1 * localValue8;
            double localValue14 = localValue3 * localValue1 * localValue8 - localValue5 * localValue1 * localValue10;
            internalField0149.player.setVelocity(localValue12, internalField0149.player.getVelocity().y, localValue14);
         }
      }
   }

   private boolean internalMethod10001() {
      return internalField0149.player != null
         && (rockstar.client.compat.InputCompat.forward(internalField0149.player.input) != 0.0F || rockstar.client.compat.InputCompat.sideways(internalField0149.player.input) != 0.0F);
   }

   double internalMethod04837() {
      Vec3d localValue1 = internalField0149.player.getVelocity();
      return Math.hypot(localValue1.x, localValue1.z);
   }

   private float internalMethod02297(ScaffoldModule.InternalType0198 localValue1) {
      double localValue2 = localValue1.internalMethod06295();
      double localValue4 = localValue1.internalMethod06298();
      return localValue2 == 0.0 && localValue4 == 0.0
         ? internalField0149.player.getYaw()
         : MathHelper.wrapDegrees((float)Math.toDegrees(GameUtils.internalMethod01047(internalField0149.player.getYaw(), localValue2, localValue4)));
   }

   private ScaffoldModule.InternalType0198 internalMethod06289(Vec3d localValue1, float localValue2) {
      if (localValue1.lengthSquared() < 1.0E-6) {
         return ScaffoldModule.InternalType0198.internalField0894;
      } else {
         double localValue3 = Math.toRadians(localValue2);
         double localValue5 = Math.sin(localValue3);
         double localValue7 = Math.cos(localValue3);
         double localValue9 = localValue1.x;
         double localValue11 = localValue1.z;
         double localValue13 = localValue11 * localValue7 - localValue9 * localValue5;
         double localValue15 = localValue9 * localValue7 + localValue11 * localValue5;
         double localValue17 = Math.max(Math.abs(localValue13), Math.abs(localValue15));
         if (localValue17 > 1.0) {
            localValue13 /= localValue17;
            localValue15 /= localValue17;
         }

         return ScaffoldModule.InternalType0198.internalMethod02578((float)localValue13, (float)localValue15);
      }
   }

   private Vec3d internalMethod00200(float localValue1) {
      double localValue2 = Math.toRadians(localValue1);
      return new Vec3d(-Math.sin(localValue2), 0.0, Math.cos(localValue2)).normalize();
   }

   private Direction internalMethod00159(float localValue1) {
      int localValue2 = MathHelper.floor(MathHelper.wrapDegrees(localValue1) / 90.0F + 0.5) & 3;

      return switch (localValue2) {
         case 0 -> Direction.SOUTH;
         case 1 -> Direction.WEST;
         case 2 -> Direction.NORTH;
         default -> Direction.EAST;
      };
   }

   private float internalMethod07313(float localValue1) {
      return Math.round(localValue1 / 45.0F) * 45.0F;
   }

   private void internalMethod08676(int localValue1) {
      if (localValue1 >= 0 && localValue1 < 9 && internalField0149.player.getInventory().getSelectedSlot() != localValue1) {
         internalField0149.player.getInventory().setSelectedSlot(localValue1);
      }
   }

   private long internalMethod04835() {
      return this.internalMethod00884(this.internalField0672) * 50L;
   }

   private int internalMethod00884(RangeSetting localValue1) {
      int localValue2 = Math.round(Math.min(localValue1.internalMethod06919(), localValue1.internalMethod07967()));
      int localValue3 = Math.round(Math.max(localValue1.internalMethod06919(), localValue1.internalMethod07967()));
      return localValue3 <= localValue2 ? localValue2 : ThreadLocalRandom.current().nextInt(localValue2, localValue3 + 1);
   }

   private float internalMethod00883(RangeSetting localValue1) {
      float localValue2 = Math.min(localValue1.internalMethod06919(), localValue1.internalMethod07967());
      float localValue3 = Math.max(localValue1.internalMethod06919(), localValue1.internalMethod07967());
      return localValue3 <= localValue2 ? localValue2 : (float)this.internalMethod05168(localValue2, localValue3);
   }

   private double internalMethod05168(double localValue1, double localValue3) {
      return ThreadLocalRandom.current().nextDouble(localValue1, localValue3);
   }

   private Vec3d internalMethod03595(Vec3d localValue1, Vec3d localValue2, double localValue3) {
      return new Vec3d(MathHelper.lerp(localValue3, localValue1.x, localValue2.x), MathHelper.lerp(localValue3, localValue1.y, localValue2.y), MathHelper.lerp(localValue3, localValue1.z, localValue2.z));
   }

   private Vec3d internalMethod00212(Vec3d localValue1, float localValue2) {
      double localValue3 = Math.cos(localValue2);
      double localValue5 = Math.sin(localValue2);
      return new Vec3d(localValue1.x * localValue3 + localValue1.z * localValue5, localValue1.y, localValue1.z * localValue3 - localValue1.x * localValue5);
   }

   private Vec3d internalMethod03341(BlockPos localValue1) {
      return new Vec3d(localValue1.getX() + 0.5, localValue1.getY() + 0.5, localValue1.getZ() + 0.5);
   }

   private Vec3d internalMethod03223(BlockPos localValue1) {
      return new Vec3d(localValue1.getX() + 0.5, localValue1.getY(), localValue1.getZ() + 0.5);
   }

   private double internalMethod06339(Vec3d localValue1, Vec3d localValue2) {
      double localValue3 = localValue1.x - localValue2.x;
      double localValue5 = localValue1.z - localValue2.z;
      return localValue3 * localValue3 + localValue5 * localValue5;
   }

   static final class InternalType0191 implements Comparable<ScaffoldModule.InternalType0191> {
      private final BlockPos internalField0352;
      private final double internalField0194;
      private final double internalField0193;
      private final double internalField1045;

      InternalType0191(BlockPos localValue1, double localValue2, double localValue4, double localValue6) {
         this.internalField0352 = localValue1;
         this.internalField0194 = localValue2;
         this.internalField0193 = localValue4;
         this.internalField1045 = localValue6;
      }

      @Override
      public int compareTo(ScaffoldModule.InternalType0191 localValue1) {
         if (this.internalField0193 + 0.001 < localValue1.internalField0193) {
            return -1;
         } else if (localValue1.internalField0193 + 0.001 < this.internalField0193) {
            return 1;
         } else if (this.internalField0194 > localValue1.internalField0194 + 0.02) {
            return -1;
         } else {
            return this.internalField0194 + 0.02 < localValue1.internalField0194 ? 1 : Double.compare(this.internalField1045, localValue1.internalField1045);
         }
      }

      boolean internalMethod02434(ScaffoldModule.InternalType0191 localValue1) {
         return this.internalField0193 > localValue1.internalField0193 + 0.001 ? false : this.internalField0194 + 0.02 >= localValue1.internalField0194;
      }

      @Override
      public final String toString() {
         return "InternalType0191[blockPos=" + this.internalField0352 + ", overlapArea=" + this.internalField0194 + ", surfaceDelta=" + this.internalField0193 + ", horizontalDistanceToPlayerSqr=" + this.internalField1045 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0352);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0193);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1045);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ScaffoldModule.InternalType0191 other = (ScaffoldModule.InternalType0191) localValue1;
         return java.util.Objects.equals(this.internalField0352, other.internalField0352)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0193, other.internalField0193)
            && java.util.Objects.equals(this.internalField1045, other.internalField1045);
      }

      public BlockPos internalMethod01481() {
         return this.internalField0352;
      }

      public double internalMethod06118() {
         return this.internalField0194;
      }

      public double internalMethod06123() {
         return this.internalField0193;
      }

      public double internalMethod07698() {
         return this.internalField1045;
      }
   }

   static final class InternalType0192 {
      private final BlockPos internalField0352;
      private final double internalField0194;
      private final double internalField0193;

      InternalType0192(BlockPos localValue1, double localValue2, double localValue4) {
         this.internalField0352 = localValue1;
         this.internalField0194 = localValue2;
         this.internalField0193 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0192[blockPos=" + this.internalField0352 + ", offsetX=" + this.internalField0194 + ", offsetZ=" + this.internalField0193 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0352);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0193);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ScaffoldModule.InternalType0192 other = (ScaffoldModule.InternalType0192) localValue1;
         return java.util.Objects.equals(this.internalField0352, other.internalField0352)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0193, other.internalField0193);
      }

      public BlockPos internalMethod02469() {
         return this.internalField0352;
      }

      public double internalMethod02505() {
         return this.internalField0194;
      }

      public double internalMethod02524() {
         return this.internalField0193;
      }
   }

   static final class InternalType0197 {
      private final Hand internalField0050;
      private final int internalField0227;
      private final ItemStack internalField0878;

      InternalType0197(Hand localValue1, int localValue2, ItemStack localValue3) {
         this.internalField0050 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0878 = localValue3;
      }

      boolean internalMethod04621() {
         return this.internalField0050 == Hand.MAIN_HAND && this.internalField0227 >= 0;
      }

      @Override
      public final String toString() {
         return "InternalType0197[hand=" + this.internalField0050 + ", hotbarSlot=" + this.internalField0227 + ", stack=" + this.internalField0878 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0050);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ScaffoldModule.InternalType0197 other = (ScaffoldModule.InternalType0197) localValue1;
         return java.util.Objects.equals(this.internalField0050, other.internalField0050)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0878, other.internalField0878);
      }

      public Hand internalMethod07192() {
         return this.internalField0050;
      }

      public int internalMethod04620() {
         return this.internalField0227;
      }

      public ItemStack internalMethod07654() {
         return this.internalField0878;
      }
   }

   static final class InternalType0198 {
      private final boolean internalField0277;
      private final boolean internalField0276;
      private final boolean internalField1099;
      private final boolean internalField1100;
      static final ScaffoldModule.InternalType0198 internalField0894 = new ScaffoldModule.InternalType0198(false, false, false, false);

      private InternalType0198(boolean localValue1, boolean localValue2, boolean localValue3, boolean localValue4) {
         this.internalField0277 = localValue1;
         this.internalField0276 = localValue2;
         this.internalField1099 = localValue3;
         this.internalField1100 = localValue4;
      }

      static ScaffoldModule.InternalType0198 internalMethod05766(InputEvent localValue0) {
         return internalMethod02578(localValue0.getForward(), localValue0.getStrafe());
      }

      static ScaffoldModule.InternalType0198 internalMethod02578(float localValue0, float localValue1) {
         return new ScaffoldModule.InternalType0198(localValue0 > 0.0F, localValue0 < 0.0F, localValue1 > 0.0F, localValue1 < 0.0F);
      }

      boolean internalMethod08927() {
         return this.internalField0277 || this.internalField0276 || this.internalField1099 || this.internalField1100;
      }

      float internalMethod06295() {
         if (this.internalField0277 == this.internalField0276) {
            return 0.0F;
         } else {
            return this.internalField0277 ? 1.0F : -1.0F;
         }
      }

      float internalMethod06298() {
         if (this.internalField1099 == this.internalField1100) {
            return 0.0F;
         } else {
            return this.internalField1099 ? 1.0F : -1.0F;
         }
      }

      Vec3d internalMethod02891(float localValue1) {
         double localValue2 = this.internalMethod06295();
         double localValue4 = this.internalMethod06298();
         double localValue6 = Math.sqrt(localValue2 * localValue2 + localValue4 * localValue4);
         if (localValue6 < 1.0E-6) {
            return Vec3d.ZERO;
         } else {
            double localValue8 = Math.toRadians(localValue1);
            double localValue10 = Math.sin(localValue8);
            double localValue12 = Math.cos(localValue8);
            double localValue14 = (localValue4 * localValue12 - localValue2 * localValue10) / localValue6;
            double localValue16 = (localValue2 * localValue12 + localValue4 * localValue10) / localValue6;
            return new Vec3d(localValue14, 0.0, localValue16);
         }
      }

      @Override
      public final String toString() {
         return "InternalType0198[forward=" + this.internalField0277 + ", backward=" + this.internalField0276 + ", left=" + this.internalField1099 + ", right=" + this.internalField1100 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0276);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1099);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1100);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ScaffoldModule.InternalType0198 other = (ScaffoldModule.InternalType0198) localValue1;
         return java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0276, other.internalField0276)
            && java.util.Objects.equals(this.internalField1099, other.internalField1099)
            && java.util.Objects.equals(this.internalField1100, other.internalField1100);
      }

      public boolean internalMethod06296() {
         return this.internalField0277;
      }

      public boolean internalMethod06299() {
         return this.internalField0276;
      }

      public boolean internalMethod08920() {
         return this.internalField1099;
      }

      public boolean internalMethod08922() {
         return this.internalField1100;
      }
   }

   static final class InternalType0334 {
      private final boolean internalField0277;
      private final int internalField0227;
      private final boolean internalField0276;
      private final boolean internalField1099;
      static final ScaffoldModule.InternalType0334 internalField0604 = new ScaffoldModule.InternalType0334(false, 0, false, false);

      InternalType0334(boolean localValue1, int localValue2, boolean localValue3, boolean localValue4) {
         this.internalField0277 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0276 = localValue3;
         this.internalField1099 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0334[jump=" + this.internalField0277 + ", sneakTime=" + this.internalField0227 + ", stopInput=" + this.internalField0276 + ", stepBack=" + this.internalField1099 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0276);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1099);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ScaffoldModule.InternalType0334 other = (ScaffoldModule.InternalType0334) localValue1;
         return java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0276, other.internalField0276)
            && java.util.Objects.equals(this.internalField1099, other.internalField1099);
      }

      public boolean internalMethod02800() {
         return this.internalField0277;
      }

      public int internalMethod02799() {
         return this.internalField0227;
      }

      public boolean internalMethod02806() {
         return this.internalField0276;
      }

      public boolean internalMethod08579() {
         return this.internalField1099;
      }
   }

   static final class InternalType0335 {
      private final Vec3d internalField0283;
      private final Vec3d internalField0282;

      InternalType0335(Vec3d localValue1, Vec3d localValue2) {
         localValue2 = localValue2.lengthSquared() < 1.0E-6 ? new Vec3d(0.0, 0.0, 1.0) : localValue2.normalize();
         this.internalField0283 = localValue1;
         this.internalField0282 = localValue2;
      }

      Vec3d internalMethod04602(Vec3d localValue1) {
         Vec3d localValue2 = localValue1.subtract(this.internalField0283);
         return this.internalField0283.add(this.internalField0282.multiply(localValue2.dotProduct(this.internalField0282)));
      }

      double internalMethod01491(Vec3d localValue1) {
         return this.internalMethod04602(localValue1).distanceTo(localValue1);
      }

      @Override
      public final String toString() {
         return "InternalType0335[origin=" + this.internalField0283 + ", direction=" + this.internalField0282 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0282);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ScaffoldModule.InternalType0335 other = (ScaffoldModule.InternalType0335) localValue1;
         return java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0282, other.internalField0282);
      }

      public Vec3d internalMethod04438() {
         return this.internalField0283;
      }

      public Vec3d internalMethod05098() {
         return this.internalField0282;
      }
   }

   static enum InternalType0336 {
      internalField0607,
      internalField0606,
      internalField1233;
   }

   static final class InternalType0337 {
      private final BlockPos internalField0352;
      private final BlockPos internalField0351;
      private final Direction internalField0150;
      private final Vec3d internalField0283;
      private final Rotation internalField0118;
      private final boolean internalField0277;

      InternalType0337(BlockPos localValue1, BlockPos localValue2, Direction localValue3, Vec3d localValue4, Rotation localValue5, boolean localValue6) {
         this.internalField0352 = localValue1;
         this.internalField0351 = localValue2;
         this.internalField0150 = localValue3;
         this.internalField0283 = localValue4;
         this.internalField0118 = localValue5;
         this.internalField0277 = localValue6;
      }

      ScaffoldModule.InternalType0337 internalMethod00907(Rotation localValue1) {
         return new ScaffoldModule.InternalType0337(
            this.internalField0352, this.internalField0351, this.internalField0150, this.internalField0283, localValue1, this.internalField0277
         );
      }

      ScaffoldModule.InternalType0337 internalMethod01699(boolean localValue1) {
         return new ScaffoldModule.InternalType0337(
            this.internalField0352, this.internalField0351, this.internalField0150, this.internalField0283, this.internalField0118, localValue1
         );
      }

      BlockHitResult internalMethod04447() {
         return new BlockHitResult(this.internalField0283, this.internalField0150, this.internalField0351, false);
      }

      @Override
      public final String toString() {
         return "InternalType0337[placedBlock=" + this.internalField0352 + ", supportBlock=" + this.internalField0351 + ", side=" + this.internalField0150 + ", hitVec=" + this.internalField0283 + ", rotation=" + this.internalField0118 + ", allowFallbackHit=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0352);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0351);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0150);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0118);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ScaffoldModule.InternalType0337 other = (ScaffoldModule.InternalType0337) localValue1;
         return java.util.Objects.equals(this.internalField0352, other.internalField0352)
            && java.util.Objects.equals(this.internalField0351, other.internalField0351)
            && java.util.Objects.equals(this.internalField0150, other.internalField0150)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0118, other.internalField0118)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public BlockPos internalMethod00110() {
         return this.internalField0352;
      }

      public BlockPos internalMethod03865() {
         return this.internalField0351;
      }

      public Direction internalMethod05109() {
         return this.internalField0150;
      }

      public Vec3d internalMethod03524() {
         return this.internalField0283;
      }

      public Rotation internalMethod04292() {
         return this.internalField0118;
      }

      public boolean internalMethod01485() {
         return this.internalField0277;
      }
   }
}
