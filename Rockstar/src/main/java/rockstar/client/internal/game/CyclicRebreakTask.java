package rockstar.client.internal.game;




import rockstar.client.bot.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EmptyBlockView;

public class CyclicRebreakTask implements BotBehavior {
   private CyclicRebreakTask.InternalType0016 internalField0134;
   private BlockPos internalField0352;
   private Direction internalField0150;
   private long internalField0229;
   private int internalField0227;
   private int internalField0228;
   private boolean internalField0277;
   private boolean internalField0276;
   private int internalField1053;

   public CyclicRebreakTask() {
      this.internalField0134 = CyclicRebreakTask.InternalType0016.internalField0134;
      this.internalField0150 = Direction.UP;
      this.internalField1053 = -1;
   }

   @Override
   public void internalMethod07229(BotTargetManager localValue1) {
      if (localValue1 != null) {
         switch (this.internalField0134) {
            case internalField0134:
               this.internalMethod04546(localValue1);
               break;
            case internalField0133:
               this.internalMethod08825(localValue1);
               break;
            case internalField1018:
               this.internalMethod09068(localValue1);
               break;
            case internalField1019:
               this.internalMethod08940(localValue1);
               break;
            case internalField1020:
               this.internalMethod08190(localValue1);
         }
      }
   }

   private void internalMethod04546(BotTargetManager localValue1) {
      BotTargetManager.InternalType0481 localValue2 = this.internalMethod07086(localValue1);
      if (localValue2 == null) {
         this.internalMethod07295();
         localValue1.internalMethod09733();
      } else {
         this.internalField0352 = localValue2.internalMethod04871().getBlockPos().toImmutable();
         this.internalField0150 = localValue2.internalMethod04871().getSide();
         this.internalMethod09817(localValue1);
         if (this.internalMethod03138(localValue1)) {
            this.internalField0227 = 0;
            if (!this.internalField0276) {
               this.internalField0134 = CyclicRebreakTask.InternalType0016.internalField0133;
               this.internalField0228 = 0;
               this.internalField0277 = false;
            } else {
               this.internalField0134 = CyclicRebreakTask.InternalType0016.internalField1018;
            }
         }
      }
   }

   private void internalMethod08825(BotTargetManager localValue1) {
      if (this.internalField0352 != null && this.internalMethod04799(localValue1, this.internalField0352)) {
         this.internalMethod09817(localValue1);
         if (this.internalMethod03138(localValue1)) {
            Direction localValue2 = this.internalField0150 == null ? localValue1.internalMethod00513(this.internalField0352) : this.internalField0150;
            if (!this.internalField0277) {
               localValue1.internalMethod06497(this.internalField0352, localValue2);
               this.internalField0277 = true;
               this.internalField0228 = 0;
            } else {
               this.internalField0228++;
               if (this.internalField0228 >= Math.max(1, localValue1.internalMethod06687().internalMethod09841())) {
                  localValue1.internalMethod05595(this.internalField0352, localValue2);
                  this.internalField0276 = true;
                  this.internalField0277 = false;
                  this.internalField0228 = 0;
                  this.internalField0134 = CyclicRebreakTask.InternalType0016.internalField1018;
               }
            }
         }
      } else {
         this.internalField0134 = CyclicRebreakTask.InternalType0016.internalField0134;
         this.internalField0277 = false;
      }
   }

   private void internalMethod09068(BotTargetManager localValue1) {
      if (++this.internalField0227 >= Math.max(1, localValue1.internalMethod06687().internalMethod07718())) {
         this.internalField1053 = localValue1.internalMethod00273().internalMethod05054("\u0413\u0420\u0418\u0424", "GRIEF");
         if (this.internalField1053 > 0) {
            localValue1.internalMethod04382("/sellwood");
            this.internalField0134 = CyclicRebreakTask.InternalType0016.internalField1019;
            this.internalField0227 = 0;
            return;
         }

         this.internalField0227 = 0;
      }

      BotTargetManager.InternalType0481 localValue2 = this.internalMethod07086(localValue1);
      if (localValue2 != null) {
         this.internalField0352 = localValue2.internalMethod04871().getBlockPos().toImmutable();
         this.internalField0150 = localValue2.internalMethod04871().getSide();
      }

      if (this.internalField0352 != null && this.internalMethod04799(localValue1, this.internalField0352)) {
         this.internalMethod09817(localValue1);
         if (this.internalMethod03138(localValue1)) {
            long localValue3 = System.currentTimeMillis();
            if (localValue3 - this.internalField0229 >= localValue1.internalMethod06687().internalMethod07751()) {
               Direction localValue5 = this.internalField0150 == null ? localValue1.internalMethod00513(this.internalField0352) : this.internalField0150;
               localValue1.internalMethod05233(new BlockHitResult(this.internalField0352.toCenterPos(), localValue5, this.internalField0352, false));
               this.internalField0229 = localValue3;
            }
         }
      } else {
         this.internalField0134 = CyclicRebreakTask.InternalType0016.internalField0134;
      }
   }

   private void internalMethod08940(BotTargetManager localValue1) {
      localValue1.internalMethod09733();
      if (++this.internalField0227 >= Math.max(1, localValue1.internalMethod06687().internalMethod07746())) {
         localValue1.internalMethod04382("/hub");
         this.internalField0134 = CyclicRebreakTask.InternalType0016.internalField1020;
         this.internalField0227 = 0;
      }
   }

   private void internalMethod08190(BotTargetManager localValue1) {
      localValue1.internalMethod09733();
      if (++this.internalField0227 >= Math.max(1, localValue1.internalMethod06687().internalMethod07746())) {
         localValue1.internalMethod05456(new CyclicRebreakRejoinBehavior(this.internalField1053));
      }
   }

   private BotTargetManager.InternalType0481 internalMethod07086(BotTargetManager localValue1) {
      BotTargetManager.InternalType0481 localValue2 = localValue1.internalMethod01916(localValue1.internalMethod06687().internalMethod09202());
      return localValue2 != null && this.internalMethod04799(localValue1, localValue2.internalMethod04871().getBlockPos()) ? localValue2 : null;
   }

   private boolean internalMethod04799(BotTargetManager localValue1, BlockPos localValue2) {
      if (localValue2 != null && (localValue1.internalMethod00273().internalMethod03187(localValue2) || localValue1.internalMethod00273().internalMethod04617(localValue2))) {
         BlockState localValue3 = localValue1.internalMethod00273().internalMethod00820(localValue2);
         return localValue3.isAir() || localValue3.getHardness(EmptyBlockView.INSTANCE, localValue2) < 0.0F
            ? false
            : !localValue1.internalMethod06687().internalMethod04130() || this.internalMethod00797(localValue3);
      } else {
         return false;
      }
   }

   private boolean internalMethod00797(BlockState localValue1) {
      Identifier localValue2 = Registries.BLOCK.getId(localValue1.getBlock());
      if (localValue2 == null) {
         return false;
      } else {
         String localValue3 = localValue2.toString();
         return localValue3.contains("_log")
            || localValue3.contains("_wood")
            || localValue3.equals("minecraft:crimson_stem")
            || localValue3.equals("minecraft:warped_stem")
            || localValue3.equals("minecraft:stripped_crimson_stem")
            || localValue3.equals("minecraft:stripped_warped_stem");
      }
   }

   private void internalMethod09817(BotTargetManager localValue1) {
      if (this.internalField0352 != null) {
         Vec3d localValue2 = this.internalField0352.toCenterPos();
         localValue1.internalMethod05389(localValue2.x, localValue2.y, localValue2.z);
         localValue1.internalMethod09733();
      }
   }

   private boolean internalMethod03138(BotTargetManager localValue1) {
      return this.internalField0352 != null && localValue1.internalMethod01197(this.internalField0352.toCenterPos(), localValue1.internalMethod06687().internalMethod09973());
   }

   private void internalMethod07295() {
      this.internalField0352 = null;
      this.internalField0150 = Direction.UP;
   }

   @Override
   public String internalMethod06553() {
      return this.internalField0352 == null
         ? "CyclicRebreak " + this.internalField0134
         : "CyclicRebreak "
            + this.internalField0134
            + " "
            + this.internalField0352.getX()
            + " "
            + this.internalField0352.getY()
            + " "
            + this.internalField0352.getZ();
   }

   static enum InternalType0016 {
      internalField0134,
      internalField0133,
      internalField1018,
      internalField1019,
      internalField1020;
   }
}
