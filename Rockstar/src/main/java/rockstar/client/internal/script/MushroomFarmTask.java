package rockstar.client.internal.script;










import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;

public class MushroomFarmTask extends AutoFarmModeState {
   private static final int internalField0227 = 4;
   private static final float internalField0205 = 1.0F;
   private final BooleanSetting internalField0650;
   private final BooleanSetting internalField0651;
   private final BooleanSetting internalField1261;
   BlockPos internalField0352;
   private BlockPos internalField0351;
   MushroomFarmTask.InternalType0368 internalField0743;
   private int internalField1465;
   private final List<ItemEntity> internalField0416 = new ArrayList<>();
   private ItemEntity internalField0831;
   private final Set<BlockPos> internalField0546 = new HashSet<>();
   private BlockPos internalField1131;
   private MushroomFarmTask.InternalType0367 internalField0741;
   private int internalField1463;
   private int internalField1466;
   private int internalField1467;
   private long internalField1058;
   private boolean internalField0277;
   private long internalField1060;
   private final Stopwatch internalField0519;
   private final Stopwatch internalField0518;
   private Rotation internalField0118;
   private int internalField1469;
   private int internalField1468;
   private int internalField1740;
   private final EventListener<ClientPlayerTickEvent> internalField0157;

   public MushroomFarmTask(AutoFarmModule localValue1, ModeSetting localValue2) {
      super(localValue1, localValue2, "modules.settings.auto_farm.modes.mushroom");
      this.internalField0741 = MushroomFarmTask.InternalType0367.internalField0741;
      this.internalField1463 = -1;
      this.internalField1466 = -1;
      this.internalField1467 = Integer.MAX_VALUE;
      this.internalField0519 = new Stopwatch();
      this.internalField0518 = new Stopwatch();
      this.internalField0157 = new EventListener<ClientPlayerTickEvent>() {
         public void onEvent(ClientPlayerTickEvent localValue1) {
            if (MinecraftClientAccess.internalField0149.player != null
               && MinecraftClientAccess.internalField0149.world != null
               && MushroomFarmTask.this.internalField0352 != null
               && MushroomFarmTask.this.internalField0743 != null) {
               switch (MushroomFarmTask.this.internalField0743) {
                  case internalField0743:
                     MushroomFarmTask.this.internalMethod08651();
                     break;
                  case internalField0744:
                     MushroomFarmTask.this.internalMethod08843();
                     break;
                  case internalField1296:
                     MushroomFarmTask.this.internalMethod09341();
                     break;
                  case internalField1295:
                     MushroomFarmTask.this.internalMethod09348();
                     break;
                  case internalField1294:
                     MushroomFarmTask.this.internalMethod10005();
                     break;
                  case internalField1293:
                     MushroomFarmTask.this.internalMethod10006();
                     break;
                  case internalField1605:
                     MushroomFarmTask.this.internalMethod10009();
                     break;
                  case internalField1606:
                     MushroomFarmTask.this.internalMethod10010();
                     break;
                  case internalField1604:
                     MushroomFarmTask.this.internalMethod10065();
               }
            }
         }

         @Override
         public int internalMethod07175() {
            return -1;
         }
      };
      this.internalField0650 = new BooleanSetting(localValue1, "modules.settings.auto_farm.mushroom.pickup", () -> !this.isSelected()).internalMethod06630();
      this.internalField0651 = new BooleanSetting(localValue1, "modules.settings.auto_farm.mushroom.auto_deposit", () -> !this.isSelected());
      this.internalField1261 = new BooleanSetting(localValue1, "modules.settings.auto_farm.mushroom.auto_refill", () -> !this.isSelected()).internalMethod06630();
   }

   @Override
   public void internalMethod04694() {
      if (internalField0149.player != null && internalField0149.world != null) {
         BlockPos localValue1 = this.internalMethod03805();
         if (localValue1 == null) {
            this.internalMethod05961("modules.mushroom_farm.no_dirt");
            this.internalMethod08387();
         } else if (!this.internalMethod00173(Items.BROWN_MUSHROOM)) {
            this.internalMethod05961("modules.mushroom_farm.no_mushroom");
            this.internalMethod08387();
         } else if (!this.internalMethod00173(Items.BONE_MEAL)) {
            this.internalMethod05961("modules.mushroom_farm.no_bonemeal");
            this.internalMethod08387();
         } else {
            if (!this.internalMethod06036(localValue0 -> localValue0.getItem() instanceof AxeItem)) {
               this.internalMethod04510("modules.mushroom_farm.no_axe_recommend");
            }

            this.internalField0352 = localValue1;
            this.internalField0351 = internalField0149.player.getBlockPos();
            this.internalField1465 = 0;
            this.internalField1740 = 0;
            this.internalField1467 = Integer.MAX_VALUE;
            this.internalField0546.clear();
            this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField0743);
         }
      } else {
         this.internalMethod08387();
      }
   }

   @Override
   public void internalMethod04697() {
      this.internalMethod10067();
      this.internalField0416.clear();
      this.internalField0546.clear();
      this.internalField0831 = null;
      this.internalField1131 = null;
      this.internalField0352 = null;
      this.internalField0351 = null;
      this.internalField0743 = null;
      this.internalMethod10066();
   }

   @Override
   public AutoFarmPhase internalMethod02315() {
      if (this.internalField0743 == null) {
         return AutoFarmPhase.internalField0848;
      } else {
         return switch (this.internalField0743) {
            case internalField0743 -> AutoFarmPhase.internalField1348;
            case internalField0744 -> AutoFarmPhase.internalField1347;
            case internalField1296 -> AutoFarmPhase.internalField0847;
            case internalField1295 -> AutoFarmPhase.internalField1349;
            case internalField1294, internalField1604 -> AutoFarmPhase.internalField1350;
            case internalField1293, internalField1605 -> AutoFarmPhase.internalField1639;
            case internalField1606 -> AutoFarmPhase.internalField1638;
         };
      }
   }

   @Override
   public ItemStack internalMethod04126() {
      return new ItemStack(Items.BROWN_MUSHROOM);
   }

   public void internalMethod08651() {
      if (this.internalMethod08657()) {
         BlockPos localValue1 = this.internalField0352.up();
         if (this.internalMethod08838()) {
            this.internalField1467 = Integer.MAX_VALUE;
            this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1296);
         } else {
            Block localValue2 = internalField0149.world.getBlockState(localValue1).getBlock();
            if (localValue2 == Blocks.BROWN_MUSHROOM) {
               this.internalField1465 = 0;
               this.internalField1740 = 0;
               this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField0744);
            } else if (!internalField0149.world.getBlockState(localValue1).isAir()) {
               this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1296);
            } else if (!this.internalMethod00173(Items.BROWN_MUSHROOM)) {
               if (!this.internalMethod08652()) {
                  this.internalMethod05961("modules.mushroom_farm.no_mushroom");
                  this.internalMethod08387();
               }
            } else if (!this.internalMethod06962(Items.BROWN_MUSHROOM)) {
               this.internalMethod05961("modules.mushroom_farm.no_mushroom");
               this.internalMethod08387();
            } else if (this.internalField1740 >= 20) {
               this.internalMethod05961("modules.mushroom_farm.cant_place");
               this.internalMethod08387();
            } else {
               Vec3d localValue3 = new Vec3d(this.internalField0352.getX() + 0.5, this.internalField0352.getY() + 1.0, this.internalField0352.getZ() + 0.5);
               if (this.internalMethod06268(localValue3)) {
                  if (this.internalField0518.internalMethod02365(50L)) {
                     BlockHitResult localValue4 = new BlockHitResult(localValue3, Direction.UP, this.internalField0352, false);
                     internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue4);
                     internalField0149.player.swingHand(Hand.MAIN_HAND);
                     this.internalField0518.internalMethod00701();
                     this.internalField1740++;
                  }
               }
            }
         }
      }
   }

   public void internalMethod08843() {
      if (this.internalMethod08657()) {
         if (this.internalMethod08838()) {
            this.internalField1467 = Integer.MAX_VALUE;
            this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1296);
         } else {
            BlockPos localValue1 = this.internalField0352.up();
            if (internalField0149.world.getBlockState(localValue1).getBlock() != Blocks.BROWN_MUSHROOM) {
               this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField0743);
            } else if (this.internalField1465 >= 64) {
               this.internalMethod05961("modules.mushroom_farm.no_grow_space");
               this.internalMethod08387();
            } else if (!this.internalMethod00173(Items.BONE_MEAL)) {
               if (!this.internalMethod08652()) {
                  this.internalMethod05961("modules.mushroom_farm.no_bonemeal");
                  this.internalMethod08387();
               }
            } else if (!this.internalMethod06962(Items.BONE_MEAL)) {
               this.internalMethod05961("modules.mushroom_farm.no_bonemeal");
               this.internalMethod08387();
            } else {
               Vec3d localValue2 = new Vec3d(localValue1.getX() + 0.5, localValue1.getY() + 0.5, localValue1.getZ() + 0.5);
               if (this.internalMethod06268(localValue2)) {
                  if (this.internalField0519.internalMethod02365(60L)) {
                     BlockHitResult localValue3 = new BlockHitResult(localValue2, Direction.UP, localValue1, false);
                     internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue3);
                     internalField0149.player.swingHand(Hand.MAIN_HAND);
                     this.internalField1465++;
                     this.internalField0519.internalMethod00701();
                  }
               }
            }
         }
      }
   }

   public void internalMethod09341() {
      int localValue1 = this.internalMethod04007();
      if (localValue1 == 0) {
         this.internalMethod10067();
         this.internalMethod09342();
      } else {
         if (this.internalField1467 == Integer.MAX_VALUE || localValue1 < this.internalField1467) {
            this.internalField1467 = localValue1;
            this.internalField1058 = System.currentTimeMillis();
         }

         BlockPos localValue2 = this.internalMethod08475();
         if (localValue2 != null) {
            this.internalMethod10067();
            this.internalMethod07093(internalField0149.world.getBlockState(localValue2));
            Direction localValue5 = this.internalMethod05896(localValue2);
            Vec3d localValue6 = this.internalMethod01628(localValue2, localValue5);
            if (this.internalMethod06268(localValue6)) {
               if (this.internalField0518.internalMethod02365(50L)) {
                  internalField0149.interactionManager.attackBlock(localValue2, localValue5);
                  internalField0149.interactionManager.updateBlockBreakingProgress(localValue2, localValue5);
                  internalField0149.player.swingHand(Hand.MAIN_HAND);
                  this.internalField0518.internalMethod00701();
               }
            }
         } else if (!this.internalMethod08844()) {
            this.internalMethod04510("modules.mushroom_farm.unreachable");
            this.internalMethod08387();
         } else if (System.currentTimeMillis() - this.internalField1058 > 30000L) {
            this.internalMethod10067();
            this.internalMethod04510("modules.mushroom_farm.unreachable");
            this.internalMethod08387();
         } else {
            BlockPos localValue3 = this.internalMethod08271();
            if (localValue3 == null) {
               this.internalMethod09342();
            } else {
               BlockPos localValue4 = new BlockPos(localValue3.getX(), this.internalField0351.getY(), localValue3.getZ());
               if (!this.internalField0277) {
                  NewtonApiHolder.internalMethod01856().internalMethod00530(new RadiusGoal(localValue4, 1));
                  this.internalField0277 = true;
                  this.internalField1060 = System.currentTimeMillis();
               } else if (!NewtonApiHolder.internalMethod01856().internalMethod00137()) {
                  this.internalField0277 = false;
               }
            }
         }
      }
   }

   private void internalMethod09342() {
      this.internalMethod10067();
      this.internalMethod10066();
      this.internalField1465 = 0;
      this.internalField1467 = Integer.MAX_VALUE;
      if (this.internalField0650.internalMethod04496() && this.internalMethod08844()) {
         this.internalMethod09537();
         this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1295);
         this.internalMethod09347();
      } else {
         this.internalMethod09538();
      }
   }

   private void internalMethod09347() {
      this.internalField0831 = null;
      this.internalField0277 = false;

      while (!this.internalField0416.isEmpty()) {
         ItemEntity localValue1 = this.internalField0416.removeFirst();
         if (localValue1 != null && !localValue1.isRemoved() && localValue1.isAlive()) {
            this.internalField0831 = localValue1;
            return;
         }
      }

      this.internalMethod09538();
   }

   public void internalMethod09348() {
      if (this.internalField0831 == null) {
         this.internalMethod09347();
      } else if (!this.internalField0831.isRemoved() && this.internalField0831.isAlive()) {
         Vec3d localValue1 = this.internalField0831.getEntityPos();
         if (internalField0149.player.getEntityPos().squaredDistanceTo(localValue1) <= 1.9599999999999997) {
            this.internalMethod10067();
            this.internalMethod09347();
         } else if (!this.internalField0277) {
            BlockPos localValue2 = BlockPos.ofFloored(localValue1);
            NewtonApiHolder.internalMethod01856().internalMethod00530(new ColumnGoal(localValue2.getX(), localValue2.getZ()));
            this.internalField0277 = true;
            this.internalField1060 = System.currentTimeMillis();
         } else if (!NewtonApiHolder.internalMethod01856().internalMethod00137()) {
            this.internalField0277 = false;
         } else {
            if (System.currentTimeMillis() - this.internalField1060 > 10000L) {
               this.internalMethod10067();
               this.internalMethod09347();
            }
         }
      } else {
         this.internalMethod10067();
         this.internalMethod09347();
      }
   }

   private void internalMethod09537() {
      this.internalField0416.clear();
      Vec3d localValue1 = internalField0149.player.getEntityPos();
      double localValue2 = 15.0;
      double localValue4 = localValue2 * localValue2;
      ArrayList localValue6 = new ArrayList();

      for (Entity localValue8 : internalField0149.world.getEntities()) {
         if (localValue8 instanceof ItemEntity localValue9
            && !localValue9.isRemoved()
            && localValue9.isAlive()
            && !(localValue1.squaredDistanceTo(localValue9.getEntityPos()) > localValue4)
            && this.internalMethod05970(localValue9.getStack())) {
            localValue6.add(localValue9);
         }
      }

      localValue6.sort(Comparator.comparingDouble(localValue1x -> localValue1.squaredDistanceTo(((net.minecraft.entity.Entity)localValue1x).getEntityPos())));
      this.internalField0416.addAll(localValue6);
   }

   private void internalMethod09538() {
      if (this.internalMethod04005()) {
         this.internalMethod09544();
      } else {
         this.internalMethod10064();
      }
   }

   private boolean internalMethod04005() {
      if (!this.internalField0651.internalMethod04496()) {
         return false;
      } else {
         PlayerInventory localValue1 = internalField0149.player.getInventory();
         return localValue1.getEmptySlot() != -1 ? false : this.internalMethod04008();
      }
   }

   private boolean internalMethod04008() {
      PlayerInventory localValue1 = internalField0149.player.getInventory();
      int localValue2 = 0;

      for (int localValue3 = 0; localValue3 < localValue1.size(); localValue3++) {
         ItemStack localValue4 = localValue1.getStack(localValue3);
         if (localValue4.getItem() == Items.BROWN_MUSHROOM) {
            localValue2 += localValue4.getCount();
         }
      }

      return localValue2 > Items.BROWN_MUSHROOM.getDefaultStack().getMaxCount();
   }

   private void internalMethod09544() {
      this.internalMethod10067();
      this.internalMethod10066();
      this.internalField0277 = false;
      this.internalField1463 = -1;
      this.internalField1466 = -1;
      this.internalField0741 = MushroomFarmTask.InternalType0367.internalField0741;
      BlockPos localValue1 = this.internalMethod00054();
      if (localValue1 == null) {
         this.internalMethod04510("modules.mushroom_farm.no_chest");
         this.internalMethod10064();
      } else {
         this.internalField1131 = localValue1;
         if (this.internalMethod06671(localValue1)) {
            this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1293);
            this.internalField0518.internalMethod00701();
         } else if (this.internalMethod08844()) {
            this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1294);
         } else {
            this.internalMethod04510("modules.mushroom_farm.no_chest");
            this.internalMethod10064();
         }
      }
   }

   private boolean internalMethod08652() {
      if (!this.internalField1261.internalMethod04496()) {
         return false;
      } else {
         BlockPos localValue1 = this.internalMethod00054();
         if (localValue1 == null) {
            return false;
         } else if (!this.internalMethod06671(localValue1) && !this.internalMethod08844()) {
            return false;
         } else {
            this.internalMethod10067();
            this.internalMethod10066();
            this.internalField0277 = false;
            this.internalField1463 = -1;
            this.internalField1466 = -1;
            this.internalField0741 = MushroomFarmTask.InternalType0367.internalField0742;
            this.internalField1131 = localValue1;
            if (this.internalMethod06671(localValue1)) {
               this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1293);
               this.internalField0518.internalMethod00701();
            } else {
               this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1294);
            }

            return true;
         }
      }
   }

   private void internalMethod09545() {
      if (this.internalField0741 == MushroomFarmTask.InternalType0367.internalField0742) {
         if (!this.internalMethod08652()) {
            this.internalMethod04510("modules.mushroom_farm.no_chest");
            this.internalMethod10064();
         }
      } else {
         this.internalMethod09544();
      }
   }

   private BlockPos internalMethod00054() {
      BlockPos localValue1 = internalField0149.player.getBlockPos();
      byte localValue2 = 15;
      BlockPos localValue3 = null;
      double localValue4 = Double.MAX_VALUE;

      for (BlockPos localValue7 : BlockPos.iterateOutwards(localValue1, localValue2, localValue2, localValue2)) {
         BlockPos localValue8 = localValue7.toImmutable();
         if (!this.internalField0546.contains(localValue8) && internalField0149.world.getBlockEntity(localValue8) instanceof ChestBlockEntity) {
            double localValue9 = localValue7.getSquaredDistance(internalField0149.player.getEntityPos());
            if (localValue9 < localValue4) {
               localValue4 = localValue9;
               localValue3 = localValue8;
            }
         }
      }

      return localValue3;
   }

   public void internalMethod10005() {
      if (this.internalField1131 == null) {
         this.internalMethod10064();
      } else if (this.internalMethod06671(this.internalField1131)) {
         this.internalMethod10067();
         this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1293);
         this.internalField0518.internalMethod00701();
      } else if (!this.internalField0277) {
         NewtonApiHolder.internalMethod01856().internalMethod00530(new RadiusGoal(this.internalField1131, 2));
         this.internalField0277 = true;
         this.internalField1060 = System.currentTimeMillis();
      } else if (!NewtonApiHolder.internalMethod01856().internalMethod00137()) {
         this.internalField0277 = false;
      } else {
         if (System.currentTimeMillis() - this.internalField1060 > 20000L) {
            this.internalMethod10067();
            this.internalField0546.add(this.internalField1131);
            this.internalField1131 = null;
            this.internalMethod09545();
         }
      }
   }

   public void internalMethod10006() {
      if (this.internalField1131 == null) {
         this.internalMethod10064();
      } else if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
         this.internalField1463 = -1;
         this.internalField1466 = -1;
         this.internalField0518.internalMethod00701();
         this.internalMethod02274(
            this.internalField0741 == MushroomFarmTask.InternalType0367.internalField0742
               ? MushroomFarmTask.InternalType0368.internalField1606
               : MushroomFarmTask.InternalType0368.internalField1605
         );
      } else if (!this.internalMethod06671(this.internalField1131)) {
         this.internalField0277 = false;
         if (this.internalMethod08844()) {
            this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1294);
         } else {
            this.internalMethod10064();
         }
      } else {
         Vec3d localValue1 = Vec3d.ofCenter(this.internalField1131);
         if (this.internalMethod06268(localValue1)) {
            if (this.internalField0518.internalMethod02365(50L)) {
               BlockHitResult localValue2 = new BlockHitResult(localValue1, Direction.UP, this.internalField1131, false);
               internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue2);
               internalField0149.player.swingHand(Hand.MAIN_HAND);
               this.internalField0518.internalMethod00701();
            }
         }
      }
   }

   public void internalMethod10009() {
      if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1) {
         if (this.internalField0518.internalMethod02365(50L)) {
            if (this.internalField1463 != -1) {
               ItemStack localValue7 = ((Slot)localValue1.slots.get(this.internalField1463)).getStack();
               if (!localValue7.isEmpty() && localValue7.getCount() >= this.internalField1466 && localValue7.getItem() == Items.BROWN_MUSHROOM) {
                  if (this.internalField1131 != null) {
                     this.internalField0546.add(this.internalField1131);
                  }

                  internalField0149.player.closeHandledScreen();
                  this.internalField1131 = null;
                  this.internalField1463 = -1;
                  this.internalField1466 = -1;
                  this.internalMethod09544();
                  return;
               }
            }

            int localValue8 = this.internalMethod04004();
            DefaultedList localValue3 = localValue1.slots;

            for (int localValue4 = 0; localValue4 < localValue3.size(); localValue4++) {
               Slot localValue5 = (Slot)localValue3.get(localValue4);
               if (localValue5.inventory == internalField0149.player.getInventory() && localValue5.getIndex() != localValue8) {
                  ItemStack localValue6 = localValue5.getStack();
                  if (localValue6.getItem() == Items.BROWN_MUSHROOM) {
                     InventoryUtils.internalMethod03592(localValue4);
                     this.internalField1463 = localValue4;
                     this.internalField1466 = localValue6.getCount();
                     this.internalField0518.internalMethod00701();
                     return;
                  }
               }
            }

            this.internalMethod05052(true);
         }
      } else {
         this.internalMethod05052(false);
      }
   }

   private int internalMethod04004() {
      PlayerInventory localValue1 = internalField0149.player.getInventory();
      int localValue2 = -1;
      int localValue3 = -1;

      for (int localValue4 = 0; localValue4 < localValue1.size(); localValue4++) {
         ItemStack localValue5 = localValue1.getStack(localValue4);
         if (localValue5.getItem() == Items.BROWN_MUSHROOM && localValue5.getCount() > localValue3) {
            localValue3 = localValue5.getCount();
            localValue2 = localValue4;
         }
      }

      return localValue2;
   }

   private void internalMethod05052(boolean localValue1) {
      if (localValue1 && internalField0149.currentScreen != null) {
         internalField0149.player.closeHandledScreen();
      }

      this.internalField1131 = null;
      this.internalField1463 = -1;
      this.internalField1466 = -1;
      this.internalMethod10064();
   }

   public void internalMethod10010() {
      if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1) {
         if (this.internalField0518.internalMethod02365(50L)) {
            if (this.internalField1463 != -1) {
               ItemStack localValue6 = ((Slot)localValue1.slots.get(this.internalField1463)).getStack();
               if (!localValue6.isEmpty() && localValue6.getCount() >= this.internalField1466 && this.internalMethod00799(localValue6)) {
                  this.internalMethod05877(true);
                  return;
               }
            }

            DefaultedList localValue7 = localValue1.slots;

            for (int localValue3 = 0; localValue3 < localValue7.size(); localValue3++) {
               Slot localValue4 = (Slot)localValue7.get(localValue3);
               if (localValue4.inventory != internalField0149.player.getInventory()) {
                  ItemStack localValue5 = localValue4.getStack();
                  if (this.internalMethod00799(localValue5)) {
                     InventoryUtils.internalMethod03592(localValue3);
                     this.internalField1463 = localValue3;
                     this.internalField1466 = localValue5.getCount();
                     this.internalField0518.internalMethod00701();
                     return;
                  }
               }
            }

            this.internalMethod05877(true);
         }
      } else {
         this.internalMethod05877(false);
      }
   }

   private boolean internalMethod00799(ItemStack localValue1) {
      if (localValue1.isEmpty()) {
         return false;
      } else if (localValue1.getItem() == Items.BONE_MEAL) {
         return this.internalMethod06961(Items.BONE_MEAL) < 192;
      } else {
         return localValue1.getItem() == Items.BROWN_MUSHROOM ? this.internalMethod06961(Items.BROWN_MUSHROOM) < 16 : false;
      }
   }

   private void internalMethod05877(boolean localValue1) {
      if (localValue1 && internalField0149.currentScreen != null) {
         internalField0149.player.closeHandledScreen();
      }

      this.internalField1131 = null;
      this.internalField1463 = -1;
      this.internalField1466 = -1;
      if (this.internalMethod06961(Items.BONE_MEAL) == 0) {
         this.internalMethod05961("modules.mushroom_farm.no_bonemeal");
         this.internalMethod08387();
      } else {
         this.internalMethod10064();
      }
   }

   private int internalMethod06961(Item localValue1) {
      PlayerInventory localValue2 = internalField0149.player.getInventory();
      int localValue3 = 0;

      for (int localValue4 = 0; localValue4 < localValue2.size(); localValue4++) {
         ItemStack localValue5 = localValue2.getStack(localValue4);
         if (localValue5.getItem() == localValue1) {
            localValue3 += localValue5.getCount();
         }
      }

      return localValue3;
   }

   private void internalMethod10064() {
      this.internalMethod10067();
      this.internalMethod10066();
      this.internalField0277 = false;
      if (this.internalMethod08844() && !this.internalMethod06671(this.internalField0352)) {
         this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField1604);
      } else {
         this.internalField0518.internalMethod00701();
         this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField0743);
      }
   }

   public void internalMethod10065() {
      if (this.internalMethod06671(this.internalField0352) || !this.internalMethod08844()) {
         this.internalMethod10067();
         this.internalField0518.internalMethod00701();
         this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField0743);
      } else if (!this.internalField0277) {
         NewtonApiHolder.internalMethod01856().internalMethod00530(new RadiusGoal(this.internalField0351, 1));
         this.internalField0277 = true;
         this.internalField1060 = System.currentTimeMillis();
      } else if (!NewtonApiHolder.internalMethod01856().internalMethod00137()) {
         this.internalField0277 = false;
      } else {
         if (System.currentTimeMillis() - this.internalField1060 > 20000L) {
            this.internalMethod10067();
            this.internalMethod02274(MushroomFarmTask.InternalType0368.internalField0743);
         }
      }
   }

   private boolean internalMethod08657() {
      BlockState localValue1 = internalField0149.world.getBlockState(this.internalField0352);
      Block localValue2 = localValue1.getBlock();
      if (internalMethod01479(localValue2)) {
         return true;
      } else if (!localValue1.isAir()) {
         return true;
      } else {
         Predicate localValue3 = localValue0 -> ((net.minecraft.item.ItemStack)localValue0).getItem() == Items.DIRT || ((net.minecraft.item.ItemStack)localValue0).getItem() == Items.GRASS_BLOCK;
         if (!this.internalMethod00856(localValue3)) {
            this.internalMethod05961("modules.mushroom_farm.no_dirt_item");
            this.internalMethod08387();
            return false;
         } else {
            Direction[] localValue4 = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP};
            double localValue5 = internalField0149.player.getBlockInteractionRange();
            double localValue7 = localValue5 * localValue5;

            for (Direction localValue12 : localValue4) {
               BlockPos localValue13 = this.internalField0352.offset(localValue12);
               Direction localValue14 = localValue12.getOpposite();
               BlockState localValue15 = internalField0149.world.getBlockState(localValue13);
               if (localValue15.isSideSolidFullSquare(internalField0149.world, localValue13, localValue14)) {
                  Vec3d localValue16 = Vec3d.ofCenter(localValue13).add(localValue14.getOffsetX() * 0.5, localValue14.getOffsetY() * 0.5, localValue14.getOffsetZ() * 0.5);
                  if (!(internalField0149.player.getEyePos().squaredDistanceTo(localValue16) > localValue7)) {
                     if (!this.internalMethod06268(localValue16)) {
                        return false;
                     }

                     BlockHitResult localValue17 = new BlockHitResult(localValue16, localValue14, localValue13, false);
                     internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue17);
                     internalField0149.player.swingHand(Hand.MAIN_HAND);
                     return false;
                  }
               }
            }

            this.internalMethod05961("modules.mushroom_farm.no_dirt_support");
            this.internalMethod08387();
            return false;
         }
      }
   }

   private BlockPos internalMethod03805() {
      BlockPos localValue1 = internalField0149.player.getBlockPos();
      Vec3d localValue2 = internalField0149.player.getEyePos();
      double localValue3 = internalField0149.player.getBlockInteractionRange();
      double localValue5 = localValue3 * localValue3;
      BlockPos localValue7 = null;
      double localValue8 = Double.MAX_VALUE;

      for (int localValue10 = -4; localValue10 <= 4; localValue10++) {
         for (int localValue11 = -4; localValue11 <= 4; localValue11++) {
            for (int localValue12 = -4; localValue12 <= 4; localValue12++) {
               BlockPos localValue13 = localValue1.add(localValue10, localValue11, localValue12);
               if (this.internalMethod05373(localValue13)) {
                  double localValue14 = localValue2.squaredDistanceTo(Vec3d.ofCenter(localValue13.up()));
                  if (!(localValue14 > localValue5) && localValue14 < localValue8) {
                     localValue8 = localValue14;
                     localValue7 = localValue13.toImmutable();
                  }
               }
            }
         }
      }

      return localValue7;
   }

   private boolean internalMethod05373(BlockPos localValue1) {
      return !internalMethod01479(internalField0149.world.getBlockState(localValue1).getBlock()) ? false : internalField0149.world.getBlockState(localValue1.up()).isAir();
   }

   private boolean internalMethod08838() {
      BlockPos localValue1 = this.internalField0352.up();

      for (int localValue2 = -1; localValue2 <= 12; localValue2++) {
         for (int localValue3 = -7; localValue3 <= 7; localValue3++) {
            for (int localValue4 = -7; localValue4 <= 7; localValue4++) {
               if (internalMethod06408(internalField0149.world.getBlockState(localValue1.add(localValue3, localValue2, localValue4)).getBlock())) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private int internalMethod04007() {
      BlockPos localValue1 = this.internalField0352.up();
      int localValue2 = 0;

      for (int localValue3 = -1; localValue3 <= 12; localValue3++) {
         for (int localValue4 = -7; localValue4 <= 7; localValue4++) {
            for (int localValue5 = -7; localValue5 <= 7; localValue5++) {
               if (internalMethod06408(internalField0149.world.getBlockState(localValue1.add(localValue4, localValue3, localValue5)).getBlock())) {
                  localValue2++;
               }
            }
         }
      }

      return localValue2;
   }

   private BlockPos internalMethod08475() {
      BlockPos localValue1 = this.internalField0352.up();
      Vec3d localValue2 = internalField0149.player.getEyePos();
      double localValue3 = internalField0149.player.getBlockInteractionRange();
      double localValue5 = localValue3 * localValue3;
      BlockPos localValue7 = null;
      int localValue8 = Integer.MAX_VALUE;
      double localValue9 = Double.MAX_VALUE;

      for (int localValue11 = -1; localValue11 <= 12; localValue11++) {
         for (int localValue12 = -7; localValue12 <= 7; localValue12++) {
            for (int localValue13 = -7; localValue13 <= 7; localValue13++) {
               BlockPos localValue14 = localValue1.add(localValue12, localValue11, localValue13);
               if (internalMethod06408(internalField0149.world.getBlockState(localValue14).getBlock())) {
                  double localValue15 = localValue2.squaredDistanceTo(Vec3d.ofCenter(localValue14));
                  if (!(localValue15 > localValue5)) {
                     int localValue17 = localValue14.getY();
                     if (localValue17 < localValue8 || localValue17 == localValue8 && localValue15 < localValue9) {
                        localValue8 = localValue17;
                        localValue9 = localValue15;
                        localValue7 = localValue14.toImmutable();
                     }
                  }
               }
            }
         }
      }

      return localValue7;
   }

   private BlockPos internalMethod08271() {
      BlockPos localValue1 = this.internalField0352.up();
      Vec3d localValue2 = internalField0149.player.getEntityPos();
      BlockPos localValue3 = null;
      int localValue4 = Integer.MAX_VALUE;
      double localValue5 = Double.MAX_VALUE;

      for (int localValue7 = -1; localValue7 <= 12; localValue7++) {
         for (int localValue8 = -7; localValue8 <= 7; localValue8++) {
            for (int localValue9 = -7; localValue9 <= 7; localValue9++) {
               BlockPos localValue10 = localValue1.add(localValue8, localValue7, localValue9);
               if (internalMethod06408(internalField0149.world.getBlockState(localValue10).getBlock())) {
                  double localValue11 = Vec3d.ofCenter(localValue10).squaredDistanceTo(localValue2);
                  int localValue13 = localValue10.getY();
                  if (localValue13 < localValue4 || localValue13 == localValue4 && localValue11 < localValue5) {
                     localValue4 = localValue13;
                     localValue5 = localValue11;
                     localValue3 = localValue10.toImmutable();
                  }
               }
            }
         }
      }

      return localValue3;
   }

   private void internalMethod07093(BlockState localValue1) {
      Predicate localValue2 = localValue0 -> ((net.minecraft.item.ItemStack)localValue0).getItem() instanceof AxeItem;
      ItemStack localValue3 = internalField0149.player.getMainHandStack();
      MushroomFarmTask.InternalType0203 localValue4 = this.internalMethod00194(localValue3, localValue1, localValue2);
      InventorySlot localValue5 = null;

      for (InventorySlot localValue7 : InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod02638()) {
         ItemStack localValue8 = localValue7.internalMethod03427();
         MushroomFarmTask.InternalType0203 localValue9 = this.internalMethod00194(localValue8, localValue1, localValue2);
         if (localValue9.internalMethod02449(localValue4)) {
            localValue4 = localValue9;
            localValue5 = localValue7;
         }
      }

      if (localValue4.internalMethod03104()) {
         if (localValue5 instanceof HotbarSlot localValue10) {
            InventoryUtils.internalMethod01980(localValue10);
         } else if (localValue5 instanceof MainInventorySlot localValue11) {
            int localValue12 = internalField0149.player.getInventory().getSelectedSlot();
            InventoryUtils.internalMethod08821(localValue11.internalMethod06662(), localValue12);
         }
      }
   }

   private MushroomFarmTask.InternalType0203 internalMethod00194(ItemStack localValue1, BlockState localValue2, Predicate<ItemStack> localValue3) {
      if (localValue1 != null && !localValue1.isEmpty() && localValue3.test(localValue1)) {
         float localValue4 = localValue1.getMiningSpeedMultiplier(localValue2);
         int localValue5 = EnchantmentUtils.internalMethod03526(localValue1, Enchantments.EFFICIENCY);
         if (localValue4 > 1.0F && localValue5 > 0) {
            localValue4 += localValue5 * localValue5 + 1.0F;
         }

         return new MushroomFarmTask.InternalType0203(localValue4, localValue5, internalMethod00798(localValue1));
      } else {
         return new MushroomFarmTask.InternalType0203(-1.0F, -1, -1.0F);
      }
   }

   private static float internalMethod00798(ItemStack localValue0) {
      if (!localValue0.isEmpty() && localValue0.isDamageable()) {
         int localValue1 = localValue0.getMaxDamage();
         return localValue1 <= 0 ? 1.0F : (float)(localValue1 - localValue0.getDamage()) / localValue1;
      } else {
         return 1.0F;
      }
   }

   private boolean internalMethod06962(Item localValue1) {
      return this.internalMethod00856(localValue1x -> localValue1x.getItem() == localValue1);
   }

   private boolean internalMethod00856(Predicate<ItemStack> localValue1) {
      if (localValue1.test(internalField0149.player.getMainHandStack())) {
         return true;
      } else {
         HotbarSlot localValue2 = InventorySlots.internalMethod02872().internalMethod03297(localValue1);
         if (localValue2 != null) {
            InventoryUtils.internalMethod01980(localValue2);
            return true;
         } else {
            MainInventorySlot localValue3 = InventorySlots.internalMethod03558().internalMethod03297(localValue1);
            if (localValue3 != null) {
               int localValue4 = internalField0149.player.getInventory().getSelectedSlot();
               InventoryUtils.internalMethod08821(localValue3.internalMethod06662(), localValue4);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   private boolean internalMethod05970(ItemStack localValue1) {
      return localValue1.getItem() == Items.BROWN_MUSHROOM;
   }

   private boolean internalMethod00173(Item localValue1) {
      return InventorySlots.internalMethod02872().internalMethod05924(localValue1) || InventorySlots.internalMethod03558().internalMethod05924(localValue1);
   }

   private boolean internalMethod06036(Predicate<ItemStack> localValue1) {
      return InventorySlots.internalMethod02872().internalMethod03297(localValue1) != null || InventorySlots.internalMethod03558().internalMethod03297(localValue1) != null;
   }

   private boolean internalMethod06268(Vec3d localValue1) {
      Rotation localValue2 = RotationUtils.internalMethod05580(localValue1);
      if (this.internalField0118 == null || this.internalField0118.internalMethod00735(localValue2) > 0.5F) {
         this.internalField0118 = localValue2;
         this.internalField1469 = 0;
         this.internalField1468 = 0;
      }

      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(localValue2, RotationBehavior.internalField1003, 180.0F, 180.0F, 180.0F, RotationPriority.internalField1012);
      this.internalField1468++;
      Rotation localValue3 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      if (localValue3 != null && localValue3.internalMethod00735(localValue2) <= 1.0F) {
         this.internalField1469++;
         return this.internalField1469 >= 1;
      } else {
         return this.internalField1468 > 20;
      }
   }

   private void internalMethod10066() {
      this.internalField0118 = null;
      this.internalField1469 = 0;
      this.internalField1468 = 0;
   }

   private boolean internalMethod06671(BlockPos localValue1) {
      double localValue2 = internalField0149.player.getBlockInteractionRange();
      return internalField0149.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(localValue1)) <= localValue2 * localValue2;
   }

   private Direction internalMethod05896(BlockPos localValue1) {
      Vec3d localValue2 = internalField0149.player.getEyePos();
      Vec3d localValue3 = Vec3d.ofCenter(localValue1);
      double localValue4 = localValue2.x - localValue3.x;
      double localValue6 = localValue2.y - localValue3.y;
      double localValue8 = localValue2.z - localValue3.z;
      double localValue10 = Math.abs(localValue4);
      double localValue12 = Math.abs(localValue6);
      double localValue14 = Math.abs(localValue8);
      if (localValue12 >= localValue10 && localValue12 >= localValue14) {
         return localValue6 >= 0.0 ? Direction.UP : Direction.DOWN;
      } else if (localValue10 >= localValue14) {
         return localValue4 >= 0.0 ? Direction.EAST : Direction.WEST;
      } else {
         return localValue8 >= 0.0 ? Direction.SOUTH : Direction.NORTH;
      }
   }

   private Vec3d internalMethod01628(BlockPos localValue1, Direction localValue2) {
      return Vec3d.ofCenter(localValue1).add(localValue2.getOffsetX() * 0.5, localValue2.getOffsetY() * 0.5, localValue2.getOffsetZ() * 0.5);
   }

   private void internalMethod10067() {
      if (NewtonApiHolder.internalMethod05192() && NewtonApiHolder.internalMethod01856().internalMethod00137()) {
         NewtonApiHolder.internalMethod01856().internalMethod00136();
      }

      this.internalField0277 = false;
   }

   private boolean internalMethod08844() {
      return NewtonApiHolder.internalMethod05192();
   }

   private void internalMethod02274(MushroomFarmTask.InternalType0368 localValue1) {
      this.internalField0743 = localValue1;
      this.internalMethod10066();
   }

   private static boolean internalMethod01479(Block localValue0) {
      return localValue0 == Blocks.GRASS_BLOCK || localValue0 == Blocks.DIRT;
   }

   private static boolean internalMethod06408(Block localValue0) {
      return localValue0 == Blocks.BROWN_MUSHROOM_BLOCK || localValue0 == Blocks.MUSHROOM_STEM;
   }

   static final class InternalType0203 {
      private final float internalField0205;
      private final int internalField0227;
      private final float internalField0206;

      InternalType0203(float localValue1, int localValue2, float localValue3) {
         this.internalField0205 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0206 = localValue3;
      }

      boolean internalMethod03104() {
         return this.internalField0205 >= 0.0F;
      }

      boolean internalMethod02449(MushroomFarmTask.InternalType0203 localValue1) {
         if (this.internalField0205 != localValue1.internalField0205) {
            return this.internalField0205 > localValue1.internalField0205;
         } else {
            return this.internalField0227 != localValue1.internalField0227 ? this.internalField0227 > localValue1.internalField0227 : this.internalField0206 > localValue1.internalField0206;
         }
      }

      @Override
      public final String toString() {
         return "InternalType0203[miningSpeed=" + this.internalField0205 + ", efficiency=" + this.internalField0227 + ", durability=" + this.internalField0206 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         MushroomFarmTask.InternalType0203 other = (MushroomFarmTask.InternalType0203) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206);
      }

      public float internalMethod03102() {
         return this.internalField0205;
      }

      public int internalMethod03103() {
         return this.internalField0227;
      }

      public float internalMethod03108() {
         return this.internalField0206;
      }
   }

   static enum InternalType0367 {
      internalField0741,
      internalField0742;
   }

   static enum InternalType0368 {
      internalField0743,
      internalField0744,
      internalField1296,
      internalField1295,
      internalField1294,
      internalField1293,
      internalField1605,
      internalField1606,
      internalField1604;
   }
}
