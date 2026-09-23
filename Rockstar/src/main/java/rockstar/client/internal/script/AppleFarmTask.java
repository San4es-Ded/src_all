package rockstar.client.internal.script;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pyrock.events.player.ClientPlayerTickEvent;

public class AppleFarmTask extends AutoFarmModeState {
   private static final int internalField0227 = 4;
   private static final int internalField0228 = 5;
   private static final float internalField0205 = 0.1F;
   private final SliderSetting internalField0383;
   BlockPos internalField0352;
   AppleFarmTask.InternalType0207 internalField0092;
   private int internalField1055;
   private int internalField1056;
   private Rotation internalField0118;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private boolean internalField0277;
   private final EventListener<ClientPlayerTickEvent> internalField0157 = new EventListener<ClientPlayerTickEvent>() {
      public void onEvent(ClientPlayerTickEvent localValue1) {
         if (MinecraftClientAccess.internalField0149.player != null
            && MinecraftClientAccess.internalField0149.world != null
            && AppleFarmTask.this.internalField0352 != null
            && AppleFarmTask.this.internalField0092 != null) {
            if (AppleFarmTask.this.internalMethod04695()) {
               switch (AppleFarmTask.this.internalField0092) {
                  case internalField0092:
                     AppleFarmTask.this.internalMethod08375();
                     break;
                  case internalField0093:
                     AppleFarmTask.this.internalMethod08383();
                     break;
                  case internalField0986:
                     AppleFarmTask.this.internalMethod03495(true);
                     break;
                  case internalField0987:
                     AppleFarmTask.this.internalMethod03495(false);
               }
            }
         }
      }

      @Override
      public int internalMethod07175() {
         return -1;
      }
   };

   public AppleFarmTask(AutoFarmModule localValue1, ModeSetting localValue2) {
      super(localValue1, localValue2, "modules.settings.auto_farm.modes.apple");
      this.internalField0383 = new SliderSetting(localValue1, "modules.settings.auto_farm.apple.bonemeal_delay", () -> !this.isSelected())
         .internalMethod08673(10.0F)
         .internalMethod05900(0.0F)
         .internalMethod02732(1000.0F)
         .internalMethod08074(150.0F)
         .internalMethod06240("ms");
   }

   @Override
   public void internalMethod04694() {
      if (internalField0149.player != null && internalField0149.world != null) {
         List localValue1 = this.internalMethod04507();
         if (localValue1.isEmpty()) {
            this.internalMethod05961("modules.apple_farm.no_dirt");
            this.internalMethod08387();
         } else {
            if (localValue1.size() > 1) {
               this.internalMethod04510("modules.apple_farm.multiple_dirt");
            }

            if (!this.internalMethod03251(Items.OAK_SAPLING)) {
               this.internalMethod05961("modules.apple_farm.no_sapling");
               this.internalMethod08387();
            } else if (!this.internalMethod03251(Items.BONE_MEAL)) {
               this.internalMethod05961("modules.apple_farm.no_bonemeal");
               this.internalMethod08387();
            } else {
               if (!this.internalMethod08321(localValue0 -> localValue0.getItem() instanceof HoeItem)) {
                  this.internalMethod04510("modules.apple_farm.no_hoe_recommend");
               }

               this.internalField0352 = this.internalMethod02829(localValue1);
               this.internalMethod05875(AppleFarmTask.InternalType0207.internalField0092);
               this.internalField1055 = 0;
            }
         }
      } else {
         this.internalMethod08387();
      }
   }

   private BlockPos internalMethod02829(List<BlockPos> localValue1) {
      Vec3d localValue2 = internalField0149.player.getEyePos();
      BlockPos localValue3 = (BlockPos)localValue1.getFirst();
      double localValue4 = Double.MAX_VALUE;

      for (BlockPos localValue7 : localValue1) {
         double localValue8 = localValue2.squaredDistanceTo(Vec3d.ofCenter(localValue7));
         if (localValue8 < localValue4) {
            localValue4 = localValue8;
            localValue3 = localValue7;
         }
      }

      return localValue3;
   }

   @Override
   public void internalMethod04697() {
      this.internalField0352 = null;
      this.internalField0092 = null;
      this.internalField0118 = null;
      this.internalField1056 = 0;
   }

   public boolean internalMethod04695() {
      BlockState localValue1 = internalField0149.world.getBlockState(this.internalField0352);
      Block localValue2 = localValue1.getBlock();
      if (localValue2 != Blocks.GRASS_BLOCK && localValue2 != Blocks.DIRT) {
         if (!localValue1.isAir()) {
            return true;
         } else {
            Predicate<ItemStack> localValue3 = localValue0 -> localValue0.getItem() == Items.DIRT || localValue0.getItem() == Items.GRASS_BLOCK;
            if (!this.internalMethod03497(localValue3)) {
               this.internalMethod05961("modules.apple_farm.no_dirt_item");
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
                        if (!this.internalMethod00438(localValue16)) {
                           return false;
                        }

                        BlockHitResult localValue17 = new BlockHitResult(localValue16, localValue14, localValue13, false);
                        internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue17);
                        internalField0149.player.swingHand(Hand.MAIN_HAND);
                        return false;
                     }
                  }
               }

               this.internalMethod05961("modules.apple_farm.no_dirt_support");
               this.internalMethod08387();
               return false;
            }
         }
      } else {
         return true;
      }
   }

   public void internalMethod08375() {
      BlockPos localValue1 = this.internalField0352.up();
      Block localValue2 = internalField0149.world.getBlockState(localValue1).getBlock();
      if (localValue2 == Blocks.OAK_SAPLING) {
         this.internalMethod05875(AppleFarmTask.InternalType0207.internalField0093);
         this.internalField1055 = 0;
      } else if (!internalField0149.world.getBlockState(localValue1).isAir()) {
         this.internalMethod05875(AppleFarmTask.InternalType0207.internalField0987);
      } else if (!this.internalMethod01985(Items.OAK_SAPLING)) {
         this.internalMethod05961("modules.apple_farm.no_sapling");
         this.internalMethod08387();
      } else {
         Vec3d localValue3 = new Vec3d(this.internalField0352.getX() + 0.5, this.internalField0352.getY() + 1.0, this.internalField0352.getZ() + 0.5);
         if (this.internalMethod00438(localValue3)) {
            BlockHitResult localValue4 = new BlockHitResult(localValue3, Direction.UP, this.internalField0352, false);
            internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue4);
            internalField0149.player.swingHand(Hand.MAIN_HAND);
         }
      }
   }

   public void internalMethod08383() {
      BlockPos localValue1 = this.internalField0352.up();
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      Block localValue3 = localValue2.getBlock();
      if (this.internalMethod06003(localValue1)) {
         this.internalMethod05875(AppleFarmTask.InternalType0207.internalField0987);
      } else if (localValue3 != Blocks.OAK_SAPLING) {
         this.internalMethod05875(AppleFarmTask.InternalType0207.internalField0092);
      } else if (this.internalField1055 >= 40) {
         this.internalMethod05961("modules.apple_farm.no_grow_space");
         this.internalMethod08387();
      } else if (!this.internalMethod01985(Items.BONE_MEAL)) {
         this.internalMethod05961("modules.apple_farm.no_bonemeal");
         this.internalMethod08387();
      } else {
         Vec3d localValue4 = new Vec3d(localValue1.getX() + 0.5, localValue1.getY() + 1.0, localValue1.getZ() + 0.5);
         if (this.internalMethod00438(localValue4)) {
            if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
               BlockHitResult localValue5 = new BlockHitResult(localValue4, Direction.UP, localValue1, false);
               internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue5);
               internalField0149.player.swingHand(Hand.MAIN_HAND);
               this.internalField1055++;
               this.internalField0519.internalMethod00701();
            }
         }
      }
   }

   public void internalMethod03495(boolean localValue1) {
      Predicate<Block> localValue2 = localValue1 ? AppleFarmTask::internalMethod07290 : AppleFarmTask::internalMethod02334;
      BlockPos localValue3 = this.internalMethod07159(localValue2);
      if (localValue3 == null) {
         if (localValue1) {
            if (this.internalMethod07159(AppleFarmTask::internalMethod02334) != null) {
               this.internalMethod05875(AppleFarmTask.InternalType0207.internalField0987);
               return;
            }

            if (this.internalMethod00628(AppleFarmTask::internalMethod07290)) {
               if (!this.internalField0277) {
                  this.internalField0277 = true;
                  this.internalField0518.internalMethod00701();
                  return;
               }

               if (!this.internalField0518.internalMethod02365(30000L)) {
                  return;
               }
            }

            this.internalField0277 = false;
            this.internalMethod05875(AppleFarmTask.InternalType0207.internalField0092);
         } else {
            this.internalMethod05875(AppleFarmTask.InternalType0207.internalField0986);
         }
      } else {
         this.internalField0277 = false;
         BlockState localValue4 = internalField0149.world.getBlockState(localValue3);
         if (localValue1) {
            if (!this.internalMethod03393(localValue4)) {
               return;
            }
         } else {
            this.internalMethod00361(localValue4, localValue0 -> localValue0.getItem() instanceof AxeItem);
         }

         Direction localValue5 = this.internalMethod02821(localValue3);
         Vec3d localValue6 = this.internalMethod01818(localValue3, localValue5);
         if (this.internalMethod00438(localValue6)) {
            internalField0149.interactionManager.updateBlockBreakingProgress(localValue3, localValue5);
            internalField0149.player.swingHand(Hand.MAIN_HAND);
         }
      }
   }

   private boolean internalMethod00438(Vec3d localValue1) {
      Rotation localValue2 = RotationUtils.internalMethod05580(localValue1);
      if (this.internalField0118 == null || this.internalField0118.internalMethod00735(localValue2) > 0.5F) {
         this.internalField0118 = localValue2;
         this.internalField1056 = 0;
      }

      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(localValue2, RotationBehavior.internalField1003, 180.0F, 180.0F, 180.0F, RotationPriority.internalField1012);
      Rotation localValue3 = RockstarClient.getInstance().internalMethod02368().internalMethod08209();
      if (localValue3.internalMethod00735(localValue2) <= 1.0F) {
         this.internalField1056++;
         return this.internalField1056 >= 2;
      } else {
         return false;
      }
   }

   private void internalMethod05875(AppleFarmTask.InternalType0207 localValue1) {
      this.internalField0092 = localValue1;
      this.internalField0118 = null;
      this.internalField1056 = 0;
      this.internalField0277 = false;
   }

   private boolean internalMethod01985(Item localValue1) {
      return this.internalMethod03497(localValue1x -> localValue1x.getItem() == localValue1);
   }

   private boolean internalMethod03497(Predicate<ItemStack> localValue1) {
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

   private boolean internalMethod03393(BlockState localValue1) {
      Predicate<ItemStack> localValue2 = localValue0 -> localValue0.getItem() instanceof HoeItem;
      Predicate<ItemStack> localValue3 = localValue2.and(AppleFarmTask::internalMethod03444);
      if (this.internalMethod06071(localValue1, localValue3, true)) {
         return true;
      } else {
         boolean localValue4 = InventorySlots.internalMethod02872().internalMethod03297(localValue2) != null
            || InventorySlots.internalMethod03558().internalMethod03297(localValue2) != null;
         if (localValue4) {
            this.internalMethod05961("modules.apple_farm.no_fresh_hoe");
            this.internalMethod08387();
            return false;
         } else {
            return true;
         }
      }
   }

   private static boolean internalMethod03444(ItemStack localValue0) {
      if (!localValue0.isEmpty() && localValue0.isDamageable()) {
         int localValue1 = localValue0.getMaxDamage();
         if (localValue1 <= 0) {
            return true;
         } else {
            float localValue2 = (float)(localValue1 - localValue0.getDamage()) / localValue1;
            return localValue2 > 0.1F;
         }
      } else {
         return true;
      }
   }

   private void internalMethod00361(BlockState localValue1, Predicate<ItemStack> localValue2) {
      this.internalMethod06071(localValue1, localValue2, false);
   }

   private boolean internalMethod06071(BlockState localValue1, Predicate<ItemStack> localValue2, boolean localValue3) {
      ItemStack localValue4 = internalField0149.player.getMainHandStack();
      AppleFarmTask.InternalType0208 localValue5 = this.internalMethod04281(localValue4, localValue1, localValue2, localValue3);
      InventorySlot localValue6 = null;

      for (InventorySlot localValue8 : InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod02638()) {
         ItemStack localValue9 = localValue8.internalMethod03427();
         AppleFarmTask.InternalType0208 localValue10 = this.internalMethod04281(localValue9, localValue1, localValue2, localValue3);
         if (localValue10.internalMethod03770(localValue5)) {
            localValue5 = localValue10;
            localValue6 = localValue8;
         }
      }

      if (!localValue5.internalMethod02546()) {
         return false;
      } else {
         if (localValue6 instanceof HotbarSlot localValue11) {
            InventoryUtils.internalMethod01980(localValue11);
         } else if (localValue6 instanceof MainInventorySlot localValue12) {
            int localValue13 = internalField0149.player.getInventory().getSelectedSlot();
            InventoryUtils.internalMethod08821(localValue12.internalMethod06662(), localValue13);
         }

         return true;
      }
   }

   private AppleFarmTask.InternalType0208 internalMethod04281(ItemStack localValue1, BlockState localValue2, Predicate<ItemStack> localValue3, boolean localValue4) {
      if (localValue1 != null && !localValue1.isEmpty() && localValue3.test(localValue1)) {
         float localValue5 = localValue1.getMiningSpeedMultiplier(localValue2);
         int localValue6 = EnchantmentUtils.internalMethod03526(localValue1, Enchantments.EFFICIENCY);
         if (localValue5 > 1.0F && localValue6 > 0) {
            localValue5 += localValue6 * localValue6 + 1.0F;
         }

         int localValue7 = localValue4 ? EnchantmentUtils.internalMethod03526(localValue1, Enchantments.FORTUNE) : 0;
         float localValue8 = this.internalMethod03443(localValue1);
         return new AppleFarmTask.InternalType0208(localValue5, localValue6, localValue7, localValue8);
      } else {
         return new AppleFarmTask.InternalType0208(-1.0F, -1, -1, -1.0F);
      }
   }

   private float internalMethod03443(ItemStack localValue1) {
      if (!localValue1.isEmpty() && localValue1.isDamageable()) {
         int localValue2 = localValue1.getMaxDamage();
         return localValue2 <= 0 ? 1.0F : (float)(localValue2 - localValue1.getDamage()) / localValue2;
      } else {
         return 1.0F;
      }
   }

   private List<BlockPos> internalMethod04507() {
      ArrayList localValue1 = new ArrayList();
      BlockPos localValue2 = internalField0149.player.getBlockPos();
      Vec3d localValue3 = internalField0149.player.getEyePos();
      double localValue4 = 16.0;

      for (int localValue6 = -4; localValue6 <= 4; localValue6++) {
         for (int localValue7 = -4; localValue7 <= 4; localValue7++) {
            for (int localValue8 = -4; localValue8 <= 4; localValue8++) {
               BlockPos localValue9 = localValue2.add(localValue6, localValue7, localValue8);
               if (this.internalMethod04713(localValue9)
                  && !(localValue3.squaredDistanceTo(Vec3d.ofCenter(localValue9.up())) > localValue4 * 4.0)
                  && !(Vec3d.ofCenter(localValue9).squaredDistanceTo(internalField0149.player.getEntityPos()) > localValue4 * 4.0)) {
                  localValue1.add(localValue9);
               }
            }
         }
      }

      return localValue1;
   }

   private boolean internalMethod04713(BlockPos localValue1) {
      Block localValue2 = internalField0149.world.getBlockState(localValue1).getBlock();
      return localValue2 != Blocks.GRASS_BLOCK && localValue2 != Blocks.DIRT ? false : internalField0149.world.getBlockState(localValue1.up()).isAir();
   }

   private boolean internalMethod06003(BlockPos localValue1) {
      for (int localValue2 = -1; localValue2 <= 1; localValue2++) {
         for (int localValue3 = -1; localValue3 <= 1; localValue3++) {
            for (int localValue4 = 0; localValue4 <= 12; localValue4++) {
               if (internalMethod02334(internalField0149.world.getBlockState(localValue1.add(localValue2, localValue4, localValue3)).getBlock())) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean internalMethod00628(Predicate<Block> localValue1) {
      BlockPos localValue2 = this.internalField0352.up();

      for (int localValue3 = 0; localValue3 <= 12; localValue3++) {
         for (int localValue4 = -5; localValue4 <= 5; localValue4++) {
            for (int localValue5 = -5; localValue5 <= 5; localValue5++) {
               if (localValue1.test(internalField0149.world.getBlockState(localValue2.add(localValue4, localValue3, localValue5)).getBlock())) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private BlockPos internalMethod07159(Predicate<Block> localValue1) {
      BlockPos localValue2 = this.internalField0352.up();
      Vec3d localValue3 = internalField0149.player.getEyePos();
      double localValue4 = internalField0149.player.getBlockInteractionRange();
      double localValue6 = localValue4 * localValue4;
      BlockPos localValue8 = null;
      int localValue9 = Integer.MAX_VALUE;
      double localValue10 = Double.MAX_VALUE;

      for (int localValue12 = 0; localValue12 <= 12; localValue12++) {
         for (int localValue13 = -5; localValue13 <= 5; localValue13++) {
            for (int localValue14 = -5; localValue14 <= 5; localValue14++) {
               BlockPos localValue15 = localValue2.add(localValue13, localValue12, localValue14);
               if (localValue1.test(internalField0149.world.getBlockState(localValue15).getBlock())) {
                  Direction localValue16 = this.internalMethod02821(localValue15);
                  Vec3d localValue17 = this.internalMethod01818(localValue15, localValue16);
                  double localValue18 = localValue3.squaredDistanceTo(localValue17);
                  if (!(localValue18 > localValue6) && this.internalMethod00509(localValue3, localValue17, localValue15)) {
                     int localValue20 = localValue15.getY();
                     if (localValue20 < localValue9 || localValue20 == localValue9 && localValue18 < localValue10) {
                        localValue9 = localValue20;
                        localValue10 = localValue18;
                        localValue8 = localValue15;
                     }
                  }
               }
            }
         }
      }

      return localValue8;
   }

   private boolean internalMethod00509(Vec3d localValue1, Vec3d localValue2, BlockPos localValue3) {
      BlockHitResult localValue4 = internalField0149.world.raycast(new RaycastContext(localValue1, localValue2, ShapeType.OUTLINE, FluidHandling.NONE, internalField0149.player));
      return localValue4.getType() != Type.BLOCK ? true : localValue4.getBlockPos().equals(localValue3);
   }

   private Direction internalMethod02821(BlockPos localValue1) {
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

   private Vec3d internalMethod01818(BlockPos localValue1, Direction localValue2) {
      return Vec3d.ofCenter(localValue1).add(localValue2.getOffsetX() * 0.5, localValue2.getOffsetY() * 0.5, localValue2.getOffsetZ() * 0.5);
   }

   private boolean internalMethod03251(Item localValue1) {
      return InventorySlots.internalMethod02872().internalMethod05924(localValue1) || InventorySlots.internalMethod03558().internalMethod05924(localValue1);
   }

   private boolean internalMethod08321(Predicate<ItemStack> localValue1) {
      return InventorySlots.internalMethod02872().internalMethod03297(localValue1) != null || InventorySlots.internalMethod03558().internalMethod03297(localValue1) != null;
   }

   private static boolean internalMethod02334(Block localValue0) {
      return localValue0 == Blocks.OAK_LOG;
   }

   private static boolean internalMethod07290(Block localValue0) {
      return localValue0 == Blocks.OAK_LEAVES;
   }

   static enum InternalType0207 {
      internalField0092,
      internalField0093,
      internalField0986,
      internalField0987;
   }

   static final class InternalType0208 {
      private final float internalField0205;
      private final int internalField0227;
      private final int internalField0228;
      private final float internalField0206;

      InternalType0208(float localValue1, int localValue2, int localValue3, float localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0228 = localValue3;
         this.internalField0206 = localValue4;
      }

      boolean internalMethod02546() {
         return this.internalField0205 >= 0.0F;
      }

      boolean internalMethod03770(AppleFarmTask.InternalType0208 localValue1) {
         if (this.internalField0205 != localValue1.internalField0205) {
            return this.internalField0205 > localValue1.internalField0205;
         } else if (this.internalField0227 != localValue1.internalField0227) {
            return this.internalField0227 > localValue1.internalField0227;
         } else {
            return this.internalField0228 != localValue1.internalField0228 ? this.internalField0228 > localValue1.internalField0228 : this.internalField0206 > localValue1.internalField0206;
         }
      }

      @Override
      public final String toString() {
         return "InternalType0208[miningSpeed=" + this.internalField0205 + ", efficiency=" + this.internalField0227 + ", fortune=" + this.internalField0228 + ", durability=" + this.internalField0206 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AppleFarmTask.InternalType0208 other = (AppleFarmTask.InternalType0208) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206);
      }

      public float internalMethod02544() {
         return this.internalField0205;
      }

      public int internalMethod02545() {
         return this.internalField0227;
      }

      public int internalMethod02550() {
         return this.internalField0228;
      }

      public float internalMethod02549() {
         return this.internalField0206;
      }
   }
}
