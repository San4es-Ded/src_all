package rockstar.client.internal.script;






import rockstar.client.rotation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.jetbrains.annotations.Nullable;

public final class ElytraFlightTask implements NewtonTask {
   private static final double internalField0194 = 0.08;
   private final int internalField0227;
   private final int internalField0228;
   private final int internalField1053;
   private final boolean internalField0277;
   private final ElytraFlightTask.InternalType0009 internalField0084 = new ElytraFlightTask.InternalType0009();
   private boolean internalField0276;
   private boolean internalField1099;
   @Nullable
   private String internalField0248;
   private boolean internalField1100;
   private boolean internalField1102;
   private int internalField1055;
   private int internalField1056;
   private int internalField1054;
   private int internalField1464 = -100;
   private int internalField1470 = -1000;
   private double internalField0193 = Double.MAX_VALUE;
   private double internalField1045;
   private float internalField0205;
   private float internalField0206;
   private boolean internalField1101;
   private ElytraFlightTask.InternalType0008 internalField0082;
   private int internalField1465;
   private boolean internalField1516;
   private int internalField1463;
   private int internalField1466;
   private boolean internalField1517;
   private boolean internalField1512;
   private boolean internalField1515;
   private int internalField1467;
   private int internalField1469;
   private boolean internalField1514;
   private int internalField1468;
   private double internalField1043;
   private int internalField1740;
   private boolean internalField1513;
   private int internalField1741;
   @Nullable
   private Vec3d internalField0283;
   private long internalField0229;
   @Nullable
   private CompletableFuture<List<Vec3d>> internalField0641;
   private List<Vec3d> internalField0416;
   private int internalField1736;
   private int internalField1735;
   private volatile List<Vec3d> internalField0417;

   public ElytraFlightTask(int localValue1, int localValue2, int localValue3, boolean localValue4) {
      this.internalField0082 = ElytraFlightTask.InternalType0008.internalField0082;
      this.internalField0229 = Long.MIN_VALUE;
      this.internalField0416 = List.of();
      this.internalField1735 = -10000;
      this.internalField0417 = List.of();
      this.internalField0227 = localValue1;
      this.internalField0228 = localValue2;
      this.internalField1053 = localValue3;
      this.internalField0277 = localValue4;
   }

   public ElytraFlightTask(BlockPos localValue1) {
      this(localValue1.getX(), localValue1.getY(), localValue1.getZ(), true);
   }

   @Override
   public String internalMethod01129() {
      return "elytra";
   }

   @Override
   public String internalMethod05788() {
      if (this.internalField0276) {
         return "\u043f\u0430\u0443\u0437\u0430";
      } else if (this.internalField1099) {
         return "\u0433\u043e\u0442\u043e\u0432\u043e";
      } else if (this.internalField1513) {
         return "\u043f\u043e\u0441\u0430\u0434\u043a\u0430";
      } else {
         int localValue1 = (int)Math.round(Math.sqrt(Math.max(0.0, this.internalField0193)));
         if (!this.internalField1100) {
            return "\u0441\u0442\u0430\u0440\u0442";
         } else if (this.internalField1517) {
            return "\u043e\u0431\u0445\u043e\u0434 \u0442\u0443\u043f\u0438\u043a\u0430";
         } else {
            return this.internalField1514
               ? "\u043e\u0431\u0445\u043e\u0434 \u043f\u043e \u0432\u044b\u0441\u043e\u0442\u0435"
               : (
                     this.internalField1101
                        ? "\u0430\u0432\u0430\u0440\u0438\u0439\u043d\u044b\u0439 \u043d\u0430\u0431\u043e\u0440 \u0432\u044b\u0441\u043e\u0442\u044b, "
                        : ""
                  )
                  + "\u0434\u043e \u0446\u0435\u043b\u0438 "
                  + localValue1
                  + "\u043c";
         }
      }
   }

   public List<Vec3d> internalMethod02052() {
      return this.internalField0417;
   }

   public Vec3d internalMethod01030() {
      return new Vec3d(this.internalField0227 + 0.5, this.internalField0277 ? this.internalField0228 + 0.5 : this.internalMethod04085(), this.internalField1053 + 0.5);
   }

   @Override
   public boolean internalMethod04087() {
      if (this.internalField0276) {
         return false;
      } else if (this.internalField1099) {
         return true;
      } else {
         MinecraftClient localValue1 = MinecraftClient.getInstance();
         if (localValue1.player != null && localValue1.world != null) {
            ClientPlayerEntity localValue2 = localValue1.player;
            ClientWorld localValue3 = localValue1.world;
            this.internalField1055++;
            SilentRotationHelper.internalMethod06440();
            if (!this.internalMethod06592(localValue2)) {
               ChatMessageHelper.internalMethod06835("\u042d\u043b\u0438\u0442\u0440\u0430 \u043d\u0435 \u043d\u0430\u0434\u0435\u0442\u0430");
               this.internalField0248 = "\u044d\u043b\u0438\u0442\u0440\u0430 \u043d\u0435 \u043d\u0430\u0434\u0435\u0442\u0430";
               this.internalMethod04086();
               return true;
            } else {
               if (!this.internalField1513 && this.internalMethod07952(localValue2)) {
                  this.internalMethod04270("\u042d\u043b\u0438\u0442\u0440\u0430 \u043f\u043e\u0447\u0442\u0438 \u0441\u043b\u043e\u043c\u0430\u043d\u0430");
               }

               if (!this.internalField1100) {
                  this.internalField0205 = localValue2.getYaw();
                  this.internalField0206 = MathHelper.clamp(localValue2.getPitch(), -50.0F, 50.0F);
                  boolean localValue4 = localValue3.getDimension().hasCeiling();
                  this.internalField1045 = localValue4 ? MathHelper.clamp(localValue2.getY(), 45.0, 100.0) : MathHelper.clamp(localValue2.getY(), 16.0, 118.0);
                  this.internalField1100 = true;
               }

               if (this.internalField1513) {
                  return this.internalMethod03813(localValue1, localValue2, localValue3);
               } else {
                  double localValue10 = this.internalMethod06591(localValue2);
                  if (this.internalMethod08961(localValue2)) {
                     ChatMessageHelper.internalMethod00196("\u0414\u043e\u043b\u0435\u0442\u0435\u043b\u0438 \u0434\u043e \u0446\u0435\u043b\u0438");
                     this.internalField1099 = true;
                     this.internalMethod08148();
                     return true;
                  } else {
                     MovementInputController localValue6 = NewtonCoreManager.internalMethod00114().internalMethod00183();
                     localValue6.internalMethod01281();
                     localValue6.internalMethod03557(false);
                     localValue6.internalMethod08033(false);
                     localValue6.internalMethod08045(false);
                     localValue6.internalMethod08371(false);
                     if (!localValue2.isGliding()) {
                        this.internalMethod03087(localValue1, localValue2, localValue3, localValue6);
                        return false;
                     } else {
                        this.internalField1054 = 0;
                        this.internalField0082 = ElytraFlightTask.InternalType0008.internalField0082;
                        this.internalField1102 = false;
                        localValue6.internalMethod03508(true);
                        localValue6.internalMethod09358(true);
                        localValue6.internalMethod08359(false);
                        this.internalMethod02360(localValue10);
                        if (this.internalField1056 >= 500) {
                           this.internalMethod04270(
                              "\u041d\u0435 \u043f\u043e\u043b\u0443\u0447\u0430\u0435\u0442\u0441\u044f \u043f\u0440\u0438\u0431\u043b\u0438\u0437\u0438\u0442\u044c\u0441\u044f \u043a \u0446\u0435\u043b\u0438"
                           );
                           return this.internalMethod03813(localValue1, localValue2, localValue3);
                        } else {
                           this.internalMethod05583(localValue3, localValue2);
                           this.internalMethod03413(localValue3, localValue2);
                           ElytraFlightTask.InternalType0157 localValue7 = this.internalMethod01336(localValue3, localValue2);
                           localValue7 = this.internalMethod03968(localValue3, localValue2, localValue7);
                           this.internalField1101 = localValue7.internalMethod06245();
                           if (localValue7.internalMethod06245() && localValue7.internalMethod06244() <= 8) {
                              this.internalField1469++;
                           } else {
                              this.internalField1469 = Math.max(0, this.internalField1469 - 2);
                           }

                           if (this.internalField1469 > 12) {
                              this.internalMethod04270(
                                 "\u0417\u0430\u0436\u0430\u0442 \u0432 \u0443\u0437\u043a\u043e\u043c \u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0441\u0442\u0432\u0435"
                              );
                              return this.internalMethod03813(localValue1, localValue2, localValue3);
                           } else {
                              float localValue8 = this.internalField1101 ? 18.0F : 11.0F;
                              float localValue9 = this.internalField1101 ? 11.0F : 6.0F;
                              this.internalField0205 = internalMethod00386(this.internalField0205, localValue7.internalMethod06243(), localValue8);
                              this.internalField0206 = internalMethod00386(this.internalField0206, localValue7.internalMethod06247(), localValue9);
                              this.internalField0206 = MathHelper.clamp(this.internalField0206, -50.0F, 50.0F);
                              SilentRotationHelper.internalMethod02485(new Rotation(this.internalField0205, this.internalField0206), 45.0F, 35.0F, 45.0F);
                              this.internalMethod01118(localValue1, localValue2, localValue7);
                              return false;
                           }
                        }
                     }
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   private void internalMethod03087(MinecraftClient localValue1, ClientPlayerEntity localValue2, World localValue3, MovementInputController localValue4) {
      this.internalField1054++;
      if (!this.internalField1102) {
         ChatMessageHelper.internalMethod00196("\u0412\u0437\u043b\u0435\u0442\u0430\u044e \u043d\u0430 \u044d\u043b\u0438\u0442\u0440\u0435...");
         this.internalField1102 = true;
      }

      if (this.internalField1054 > 600) {
         if (localValue2.isOnGround()) {
            ChatMessageHelper.internalMethod06835(
               "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0432\u0437\u043b\u0435\u0442\u0435\u0442\u044c (\u043d\u0435\u0442 \u043c\u0435\u0441\u0442\u0430 \u043d\u0430\u0434 \u0433\u043e\u043b\u043e\u0432\u043e\u0439 \u0438\u043b\u0438 \u0444\u0435\u0439\u0435\u0440\u0432\u0435\u0440\u043a\u043e\u0432)"
            );
            this.internalMethod04086();
         } else {
            this.internalMethod04270(
               "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0440\u0430\u0437\u043e\u0433\u043d\u0430\u0442\u044c\u0441\u044f \u043d\u0430 \u044d\u043b\u0438\u0442\u0440\u0435"
            );
         }
      } else if (localValue2.isOnGround() && !this.internalMethod05584(localValue3, localValue2)) {
         ChatMessageHelper.internalMethod06835(
            "\u041d\u0430\u0434 \u0433\u043e\u043b\u043e\u0432\u043e\u0439 \u043d\u0435\u0442 \u043c\u0435\u0441\u0442\u0430 \u0434\u043b\u044f \u0432\u0437\u043b\u0451\u0442\u0430 \u043d\u0430 \u044d\u043b\u0438\u0442\u0440\u0435"
         );
         this.internalMethod04086();
      } else {
         localValue4.internalMethod03508(false);
         localValue4.internalMethod09358(false);
         boolean localValue5;
         if (localValue2.isOnGround()) {
            this.internalField0082 = ElytraFlightTask.InternalType0008.internalField0082;
            this.internalField1465 = 0;
            localValue5 = true;
         } else {
            switch (this.internalField0082) {
               case internalField0082:
                  this.internalField0082 = ElytraFlightTask.InternalType0008.internalField0083;
                  this.internalField1465 = 0;
                  localValue5 = false;
                  break;
               case internalField0083:
                  this.internalField1465++;
                  localValue5 = false;
                  if (this.internalField1465 >= 2) {
                     this.internalField0082 = ElytraFlightTask.InternalType0008.internalField0985;
                     this.internalField1465 = 0;
                  }
                  break;
               case internalField0985:
                  localValue5 = true;
                  if (localValue1.getNetworkHandler() != null && this.internalField1055 - this.internalField1464 >= 2) {
                     localValue1.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(localValue2, Mode.START_FALL_FLYING));
                     this.internalField1464 = this.internalField1055;
                  }

                  this.internalField1465++;
                  if (this.internalField1465 >= 4) {
                     this.internalField0082 = ElytraFlightTask.InternalType0008.internalField0083;
                     this.internalField1465 = 0;
                     localValue5 = false;
                  }
                  break;
               default:
                  localValue5 = false;
            }
         }

         localValue4.internalMethod08359(localValue5 && !this.internalField1516 || localValue5 && localValue2.isOnGround());
         this.internalField1516 = localValue5;
         float localValue6 = this.internalMethod01322(localValue2);
         this.internalField0205 = localValue6;
         this.internalField0206 = -6.0F;
         SilentRotationHelper.internalMethod02485(new Rotation(localValue6, -6.0F), 45.0F, 35.0F, 45.0F);
      }
   }

   private boolean internalMethod05584(World localValue1, ClientPlayerEntity localValue2) {
      Mutable localValue3 = new Mutable();
      int localValue4 = MathHelper.floor(localValue2.getX());
      int localValue5 = MathHelper.floor(localValue2.getZ());
      int localValue6 = MathHelper.floor(localValue2.getY());

      for (int localValue7 = 2; localValue7 <= 3; localValue7++) {
         localValue3.set(localValue4, localValue6 + localValue7, localValue5);
         if (!localValue1.getBlockState(localValue3).getCollisionShape(localValue1, localValue3).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private void internalMethod01118(MinecraftClient localValue1, ClientPlayerEntity localValue2, ElytraFlightTask.InternalType0157 localValue3) {
      if (!this.internalField1513) {
         if (!this.internalField1517) {
            if (this.internalField1055 - this.internalField1470 >= 12) {
               if (this.internalField1514 && this.internalField1043 > localValue2.getY() + 4.0) {
                  if (localValue3.internalMethod06244() >= 38 || localValue3.internalMethod06247() < -12.0F) {
                     this.internalMethod06536(localValue1, localValue2);
                  }
               } else if (localValue3.internalMethod06245()) {
                  if (localValue3.internalMethod06247() < -12.0F) {
                     this.internalMethod06536(localValue1, localValue2);
                  }
               } else if (localValue3.internalMethod06244() >= 38) {
                  if (this.internalField1055 - this.internalField1470 >= 32) {
                     if (this.internalMethod01321(localValue2) < 1.05F || this.internalField0206 < -6.0F) {
                        this.internalMethod06536(localValue1, localValue2);
                     }
                  }
               }
            }
         }
      }
   }

   private void internalMethod06536(MinecraftClient localValue1, ClientPlayerEntity localValue2) {
      if (localValue1.interactionManager != null) {
         Hand localValue3;
         if (localValue2.getOffHandStack().isOf(Items.FIREWORK_ROCKET)) {
            localValue3 = Hand.OFF_HAND;
         } else {
            if (!this.internalMethod01323(localValue2)) {
               return;
            }

            localValue3 = Hand.MAIN_HAND;
         }

         localValue1.interactionManager.interactItem(localValue2, localValue3);
         this.internalField1470 = this.internalField1055;
      }
   }

   private boolean internalMethod01323(ClientPlayerEntity localValue1) {
      if (localValue1.getMainHandStack().isOf(Items.FIREWORK_ROCKET)) {
         return true;
      } else {
         PlayerInventory localValue2 = localValue1.getInventory();

         for (int localValue3 = 0; localValue3 < 9; localValue3++) {
            if (localValue2.getStack(localValue3).isOf(Items.FIREWORK_ROCKET)) {
               BestToolSelector.internalMethod00041(localValue3);
               return true;
            }
         }

         return false;
      }
   }

   private boolean internalMethod08149() {
      return this.internalField1055 - this.internalField1470 < 30;
   }

   private void internalMethod05583(World localValue1, ClientPlayerEntity localValue2) {
      if (this.internalField0641 != null) {
         if (this.internalField0641.isDone()) {
            List localValue4 = this.internalField0641.getNow(List.of());
            this.internalField0641 = null;
            if (!localValue4.isEmpty()) {
               this.internalField0416 = localValue4;
               this.internalField1736 = 0;
            }
         }
      } else {
         boolean localValue3 = this.internalField0416.isEmpty()
            || this.internalField1736 >= this.internalField0416.size()
            || this.internalField1055 - this.internalField1735 > 40
            || this.internalField0416.get(this.internalField1736).distanceTo(localValue2.getEntityPos()) > 28.0;
         if (localValue3) {
            this.internalField1735 = this.internalField1055;
            this.internalField0641 = AirPathFinder.internalMethod01945(localValue1, localValue2.getEntityPos(), this.internalMethod01030());
         }
      }
   }

   @Nullable
   private Vec3d internalMethod03781(World localValue1, ClientPlayerEntity localValue2) {
      while (this.internalField1736 < this.internalField0416.size() && this.internalField0416.get(this.internalField1736).distanceTo(localValue2.getEntityPos()) < 6.0) {
         this.internalField1736++;
      }

      if (this.internalField1736 + 1 < this.internalField0416.size()) {
         Vec3d localValue3 = this.internalField0416.get(this.internalField1736 + 1);
         Vec3d localValue4 = localValue2.getEyePos();
         BlockHitResult localValue5 = localValue1.raycast(new RaycastContext(localValue4, localValue3, ShapeType.COLLIDER, FluidHandling.ANY, localValue2));
         if (localValue5.getType() == Type.MISS) {
            this.internalField1736++;
         }
      }

      return this.internalField1736 >= this.internalField0416.size() ? null : this.internalField0416.get(this.internalField1736);
   }

   private ElytraFlightTask.InternalType0157 internalMethod01336(World localValue1, ClientPlayerEntity localValue2) {
      Vec3d localValue3 = localValue2.getEntityPos();
      Vec3d localValue4 = localValue2.getVelocity();
      Vec3d localValue5 = null;
      float localValue6 = 20.0F;
      if (!this.internalField1513 && !this.internalField1514 && !this.internalField1517) {
         Vec3d localValue7 = this.internalField0416.isEmpty() ? null : this.internalMethod03781(localValue1, localValue2);
         if (localValue7 != null) {
            localValue5 = localValue7;
            if (localValue7.y > localValue2.getY() + 2.0) {
               localValue6 = 34.0F;
            }
         }
      }

      if (localValue5 == null) {
         localValue5 = this.internalMethod07096(localValue1, localValue2, this.internalMethod04916());
      }

      Vec3d localValue38 = localValue5.subtract(localValue2.getEyePos());
      double localValue8 = Math.hypot(localValue38.x, localValue38.z);
      float localValue10 = (float)Math.toDegrees(Math.atan2(localValue38.z, localValue38.x)) - 90.0F;
      float localValue11 = MathHelper.clamp((float)(-Math.toDegrees(Math.atan2(localValue38.y, Math.max(0.001, localValue8)))), -localValue6, localValue6);
      if (this.internalField1517) {
         localValue10 += this.internalField1512 ? 90.0F : -90.0F;
         localValue11 = MathHelper.clamp(localValue11, -20.0F, 0.0F);
      }

      if (localValue1.getDimension().hasCeiling() && localValue2.getY() > 112.0) {
         localValue11 = Math.max(localValue11, 8.0F);
      }

      if (this.internalField1513 && this.internalField0283 != null) {
         double localValue12 = localValue2.getY() - this.internalField0283.y;
         float localValue14 = localValue12 < 5.0 ? 6.0F : 22.0F;
         localValue11 = MathHelper.clamp(localValue11, -50.0F, localValue14);
      }

      int localValue39 = this.internalMethod08149() ? 30 - (this.internalField1055 - this.internalField1470) : 0;
      ElytraFlightTask.InternalType0157 localValue13 = null;
      ElytraFlightTask.InternalType0010 localValue40 = null;
      double localValue15 = Double.NEGATIVE_INFINITY;

      for (float localValue20 : this.internalField0084.internalField0615) {
         for (float localValue24 : this.internalField0084.internalField0616) {
            float localValue25 = localValue10 + localValue20;
            float localValue26 = MathHelper.clamp(localValue11 + localValue24, -50.0F, 50.0F);
            ElytraFlightTask.InternalType0010 localValue27 = this.internalMethod05775(
               localValue1, localValue3, localValue4, this.internalField0205, this.internalField0206, localValue25, localValue26, 7.5F, 4.5F, localValue39, 50
            );
            double localValue28;
            if (!localValue27.internalField0277) {
               localValue28 = 50.0 * 1000.0;
            } else {
               int localValue30 = 50 - localValue27.internalField0227;
               localValue28 = localValue27.internalField0227 * 100.0 - localValue30 * localValue30 * 5.0;
            }

            double localValue53 = Math.abs(RotationUtils.internalMethod08495(localValue25, localValue10)) * 0.22 + Math.abs(localValue26 - localValue11) * 0.16;
            double localValue32 = Math.abs(RotationUtils.internalMethod08495(this.internalField0205, localValue25)) * 0.1 + Math.abs(this.internalField0206 - localValue26) * 0.1;
            double localValue34 = (Math.sqrt(this.internalField0193) - localValue27.internalField0283.distanceTo(this.internalMethod01030())) * 0.5;
            double localValue36 = localValue28 - localValue53 - localValue32 + localValue34;
            if (localValue13 == null || localValue36 > localValue15) {
               localValue15 = localValue36;
               localValue13 = new ElytraFlightTask.InternalType0157(localValue25, localValue26, localValue27.internalField0227, false);
               localValue40 = localValue27;
            }
         }
      }

      if (localValue13 == null) {
         this.internalField0417 = List.of();
         return new ElytraFlightTask.InternalType0157(this.internalField0205, -50.0F, 0, true);
      } else if (!localValue40.internalField0277) {
         this.internalField0417 = List.copyOf(localValue40.internalField0416);
         return localValue13;
      } else {
         ElytraFlightTask.InternalType0157 localValue41 = null;
         ElytraFlightTask.InternalType0010 localValue42 = null;
         double localValue43 = Double.NEGATIVE_INFINITY;

         for (float localValue48 : this.internalField0084.internalField1238) {
            for (float localValue52 : this.internalField0084.internalField1240) {
               float localValue29 = localValue10 + localValue48;
               ElytraFlightTask.InternalType0010 localValue54 = this.internalMethod05775(
                  localValue1, localValue3, localValue4, this.internalField0205, this.internalField0206, localValue29, localValue52, 14.0F, 8.5F, localValue39, 50
               );
               int localValue31 = localValue54.internalField0277 ? localValue54.internalField0227 : 50;
               double localValue55 = Math.abs(RotationUtils.internalMethod08495(this.internalField0205, localValue29));
               double localValue56 = Math.abs(RotationUtils.internalMethod08495(localValue29, localValue10));
               double localValue57 = localValue31 * 10000.0 - localValue55 * 10.0 - localValue56;
               if (localValue41 == null || localValue57 > localValue43) {
                  localValue43 = localValue57;
                  localValue41 = new ElytraFlightTask.InternalType0157(localValue29, localValue52, localValue31, true);
                  localValue42 = localValue54;
               }
            }
         }

         int localValue45 = localValue41 != null ? localValue41.internalMethod06244() : 0;
         if (localValue45 <= 8) {
            this.internalField0417 = List.copyOf(localValue40.internalField0416);
            return new ElytraFlightTask.InternalType0157(this.internalField0205, -50.0F, localValue45, true);
         } else if (localValue41 != null && localValue41.internalMethod06244() >= localValue13.internalMethod06244()) {
            this.internalField0417 = List.copyOf(localValue42.internalField0416);
            return localValue41;
         } else {
            this.internalField0417 = List.copyOf(localValue40.internalField0416);
            return new ElytraFlightTask.InternalType0157(
               localValue13.internalMethod06243(), Math.min(localValue13.internalMethod06247(), -25.0F), localValue13.internalMethod06244(), true
            );
         }
      }
   }

   private void internalMethod03413(World localValue1, ClientPlayerEntity localValue2) {
      if (this.internalField1517) {
         this.internalField1466--;
         if (this.internalField1466 <= 0) {
            this.internalField1517 = false;
            this.internalField1463 = 0;
         }
      } else if (this.internalField1514) {
         this.internalField1468--;
         boolean localValue8 = Math.abs(localValue2.getY() - this.internalField1043) < 4.0;
         if (this.internalField1468 <= 0 || localValue8) {
            this.internalField1514 = false;
            this.internalField1463 = 0;
         }
      } else {
         if (this.internalField1101) {
            this.internalField1463++;
         } else {
            this.internalField1463 = Math.max(0, this.internalField1463 - 2);
         }

         if (this.internalField1463 >= 25) {
            if (this.internalField1467 >= 3) {
               if (this.internalField1740 < 2) {
                  boolean localValue7 = localValue1.getDimension().hasCeiling();
                  this.internalField1043 = localValue7
                     ? Math.max(45.0, localValue2.getY() - 24.0)
                     : Math.min(118.0, Math.max(localValue2.getY() + 24.0, this.internalMethod05582(localValue1, localValue2) + 18.0));
                  this.internalField1514 = true;
                  this.internalField1468 = 70;
                  this.internalField1740++;
                  this.internalField1467 = 0;
                  this.internalField1463 = 0;
                  ChatMessageHelper.internalMethod00196(
                     "\u041e\u0431\u0445\u043e\u0434 \u043d\u0435 \u043f\u043e\u043c\u043e\u0433 \u2014 \u043c\u0435\u043d\u044f\u044e \u0432\u044b\u0441\u043e\u0442\u0443 ("
                        + (int)this.internalField1043
                        + ")"
                  );
               } else {
                  this.internalMethod04270(
                     "\u041d\u0435 \u043f\u043e\u043b\u0443\u0447\u0430\u0435\u0442\u0441\u044f \u043d\u0430\u0439\u0442\u0438 \u043f\u0440\u043e\u0445\u043e\u0434"
                  );
               }
            } else {
               if (this.internalField1467 > 0) {
                  this.internalField1512 = !this.internalField1515;
               } else {
                  Vec3d localValue3 = this.internalMethod01030().subtract(localValue2.getEyePos());
                  float localValue4 = (float)Math.toDegrees(Math.atan2(localValue3.z, localValue3.x)) - 90.0F;
                  ElytraFlightTask.InternalType0010 localValue5 = this.internalMethod05775(
                     localValue1, localValue2.getEntityPos(), localValue2.getVelocity(), this.internalField0205, this.internalField0206, localValue4 + 90.0F, 0.0F, 14.0F, 8.5F, 0, 50
                  );
                  ElytraFlightTask.InternalType0010 localValue6 = this.internalMethod05775(
                     localValue1, localValue2.getEntityPos(), localValue2.getVelocity(), this.internalField0205, this.internalField0206, localValue4 - 90.0F, 0.0F, 14.0F, 8.5F, 0, 50
                  );
                  this.internalField1512 = localValue5.internalField0227 >= localValue6.internalField0227;
               }

               this.internalField1515 = this.internalField1512;
               this.internalField1517 = true;
               this.internalField1466 = 30;
               this.internalField1467++;
               ChatMessageHelper.internalMethod00196(
                  "\u041a\u0440\u0443\u0436\u0443 \u0432 \u0442\u0443\u043f\u0438\u043a\u0435 \u2014 \u043b\u0435\u0447\u0443 \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u043e \u0446\u0435\u043b\u0438"
               );
            }
         }
      }
   }

   private Vec3d internalMethod07096(World localValue1, ClientPlayerEntity localValue2, Vec3d localValue3) {
      if (this.internalField1513) {
         return localValue3;
      } else {
         double localValue4 = localValue2.getX();
         double localValue6 = localValue2.getZ();
         double localValue8 = localValue3.x - localValue4;
         double localValue10 = localValue3.z - localValue6;
         double localValue12 = Math.hypot(localValue8, localValue10);
         if (this.internalField1514) {
            if (localValue12 < 1.0) {
               return new Vec3d(localValue3.x, this.internalField1043, localValue3.z);
            } else {
               double localValue18 = localValue8 / localValue12;
               double localValue16 = localValue10 / localValue12;
               return new Vec3d(localValue4 + localValue18 * 16.0, this.internalField1043, localValue6 + localValue16 * 16.0);
            }
         } else {
            boolean localValue14 = localValue1.getDimension().hasCeiling();
            if (localValue14) {
               if (localValue12 > 32.0) {
                  double localValue20 = MathHelper.clamp(localValue3.y, 45.0, 100.0);
                  return new Vec3d(localValue3.x, localValue20, localValue3.z);
               } else {
                  return localValue3;
               }
            } else {
               if (localValue12 > 40.0) {
                  double localValue15 = this.internalMethod05582(localValue1, localValue2) + 14.0;
                  localValue15 = MathHelper.clamp(localValue15, 16.0, 118.0);
                  if (localValue3.y < localValue15) {
                     return new Vec3d(localValue3.x, localValue15, localValue3.z);
                  }
               }

               return localValue3;
            }
         }
      }
   }

   private double internalMethod05582(World localValue1, ClientPlayerEntity localValue2) {
      Vec3d localValue3 = this.internalMethod01030().subtract(localValue2.getEntityPos());
      double localValue4 = Math.hypot(localValue3.x, localValue3.z);
      double localValue6 = localValue4 > 1.0 ? localValue3.x / localValue4 : 0.0;
      double localValue8 = localValue4 > 1.0 ? localValue3.z / localValue4 : 0.0;
      double localValue10 = localValue1.getBottomY();

      for (int localValue15 : this.internalField0084.internalField0618) {
         int localValue16 = MathHelper.floor(localValue2.getX() + localValue6 * localValue15);
         int localValue17 = MathHelper.floor(localValue2.getZ() + localValue8 * localValue15);
         if (localValue1.isPosLoaded(localValue16, localValue17)) {
            int localValue18 = localValue1.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING, localValue16, localValue17);
            if (localValue18 > localValue10) {
               localValue10 = localValue18;
            }
         }
      }

      return localValue10;
   }

   private ElytraFlightTask.InternalType0157 internalMethod03968(World localValue1, ClientPlayerEntity localValue2, ElytraFlightTask.InternalType0157 localValue3) {
      Vec3d localValue4 = localValue2.getVelocity();
      double localValue5 = localValue4.length();
      if (localValue5 < 0.4) {
         return localValue3;
      } else {
         Vec3d localValue7 = this.internalMethod01030();
         double localValue8 = Math.hypot(localValue7.x - localValue2.getX(), localValue7.z - localValue2.getZ());
         if (localValue8 < 24.0) {
            return localValue3;
         } else {
            Vec3d localValue10 = localValue2.getEyePos();
            double localValue11 = Math.min(48.0, localValue5 * 28.0 + 4.0);
            BlockHitResult localValue13 = localValue1.raycast(new RaycastContext(localValue10, localValue10.add(localValue4.multiply(localValue11 / localValue5)), ShapeType.COLLIDER, FluidHandling.NONE, localValue2));
            if (localValue13.getType() != Type.BLOCK) {
               return localValue3;
            } else if (localValue13.getPos().distanceTo(localValue7) < 12.0) {
               return localValue3;
            } else {
               double localValue14 = localValue13.getPos().distanceTo(localValue10) / Math.max(0.1, localValue5);
               return localValue14 < 14.0
                  ? new ElytraFlightTask.InternalType0157(localValue3.internalMethod06243(), Math.min(localValue3.internalMethod06247(), -35.0F), (int)localValue14, true)
                  : localValue3;
            }
         }
      }
   }

   private Vec3d internalMethod04916() {
      return this.internalField1513 && this.internalField0283 != null ? this.internalField0283 : this.internalMethod01030();
   }

   private void internalMethod04270(String localValue1) {
      if (!this.internalField1513 && !this.internalField1099) {
         ChatMessageHelper.internalMethod06835(localValue1 + " \u2014 \u0437\u0430\u0445\u043e\u0436\u0443 \u043d\u0430 \u043f\u043e\u0441\u0430\u0434\u043a\u0443");
         this.internalField1513 = true;
         this.internalField1741 = 0;
         this.internalField0283 = null;
         this.internalField0229 = Long.MIN_VALUE;
         this.internalField1517 = false;
         this.internalField1514 = false;
         this.internalField1101 = false;
         this.internalField1056 = 0;
      }
   }

   private boolean internalMethod03813(MinecraftClient localValue1, ClientPlayerEntity localValue2, World localValue3) {
      this.internalField1741++;
      if (localValue2.isOnGround()) {
         ChatMessageHelper.internalMethod00196("\u0421\u0435\u043b \u043d\u0430 \u0437\u0435\u043c\u043b\u044e");
         this.internalField1099 = true;
         this.internalMethod08148();
         return true;
      } else if (this.internalField1741 > 300) {
         ChatMessageHelper.internalMethod06835(
            "\u041f\u043e\u0441\u0430\u0434\u043a\u0430 \u043d\u0435 \u0443\u0434\u0430\u043b\u0430\u0441\u044c \u0437\u0430 \u043b\u0438\u043c\u0438\u0442 \u2014 \u043e\u0442\u043c\u0435\u043d\u044f\u044e"
         );
         this.internalField0248 = "\u043f\u043e\u0441\u0430\u0434\u043a\u0430 \u043d\u0435 \u0443\u0434\u0430\u043b\u0430\u0441\u044c";
         this.internalMethod04086();
         return true;
      } else {
         if (this.internalField0283 == null || this.internalField1055 - this.internalField0229 > 20L) {
            Vec3d localValue4 = this.internalMethod04276(localValue3, localValue2);
            if (localValue4 != null) {
               this.internalField0283 = localValue4;
            }

            this.internalField0229 = this.internalField1055;
         }

         MovementInputController localValue13 = NewtonCoreManager.internalMethod00114().internalMethod00183();
         localValue13.internalMethod01281();
         localValue13.internalMethod03557(false);
         localValue13.internalMethod08033(false);
         localValue13.internalMethod08045(false);
         localValue13.internalMethod08371(false);
         localValue13.internalMethod08359(false);
         if (!localValue2.isGliding()) {
            localValue13.internalMethod03508(false);
            localValue13.internalMethod09358(false);
            return false;
         } else if (this.internalField0283 == null) {
            localValue13.internalMethod03508(false);
            localValue13.internalMethod09358(false);
            this.internalField0206 = internalMethod00386(this.internalField0206, -50.0F, 11.0F);
            SilentRotationHelper.internalMethod02485(new Rotation(this.internalField0205, this.internalField0206), 45.0F, 35.0F, 45.0F);
            this.internalField0417 = List.of();
            return false;
         } else {
            localValue13.internalMethod03508(true);
            localValue13.internalMethod09358(true);
            Vec3d localValue5 = this.internalField0283.subtract(localValue2.getEyePos());
            double localValue6 = Math.hypot(localValue5.x, localValue5.z);
            float localValue8 = (float)Math.toDegrees(Math.atan2(localValue5.z, localValue5.x)) - 90.0F;
            float localValue9 = (float)(-Math.toDegrees(Math.atan2(localValue5.y, Math.max(0.001, localValue6))));
            double localValue10 = localValue2.getY() - this.internalField0283.y;
            float localValue12 = localValue10 < 5.0 ? 6.0F : 22.0F;
            localValue9 = MathHelper.clamp(localValue9, -50.0F, localValue12);
            if (localValue10 < 5.0 && localValue6 < 2.5) {
               localValue13.internalMethod03508(false);
               localValue13.internalMethod09358(false);
               localValue9 = Math.min(localValue9, -3.0F);
            }

            this.internalField0205 = internalMethod00386(this.internalField0205, localValue8, 11.0F);
            this.internalField0206 = internalMethod00386(this.internalField0206, localValue9, 6.0F);
            this.internalField0206 = MathHelper.clamp(this.internalField0206, -50.0F, 50.0F);
            SilentRotationHelper.internalMethod02485(new Rotation(this.internalField0205, this.internalField0206), 45.0F, 35.0F, 45.0F);
            this.internalField0417 = List.of(localValue2.getEntityPos(), this.internalField0283);
            return false;
         }
      }
   }

   @Nullable
   private Vec3d internalMethod04276(World localValue1, ClientPlayerEntity localValue2) {
      Mutable localValue3 = new Mutable();
      int localValue4 = MathHelper.floor(localValue2.getX());
      int localValue5 = MathHelper.floor(localValue2.getZ());
      int localValue6 = Math.min(localValue1.getTopYInclusive() - 1, MathHelper.floor(localValue2.getY()));
      Vec3d localValue7 = null;
      double localValue8 = Double.NEGATIVE_INFINITY;
      byte localValue10 = 8;

      for (int localValue11 = -localValue10; localValue11 <= localValue10; localValue11++) {
         for (int localValue12 = -localValue10; localValue12 <= localValue10; localValue12++) {
            int localValue13 = localValue4 + localValue11;
            int localValue14 = localValue5 + localValue12;
            boolean localValue15 = false;

            for (int localValue16 = localValue6; localValue16 > localValue1.getBottomY() + 2; localValue16--) {
               localValue3.set(localValue13, localValue16, localValue14);
               if (!localValue1.isPosLoaded(localValue3)) {
                  localValue15 = true;
                  break;
               }

               BlockState localValue17 = localValue1.getBlockState(localValue3);
               FluidState localValue18 = localValue17.getFluidState();
               if (localValue18.isIn(FluidTags.LAVA) || internalMethod01826(localValue17)) {
                  localValue15 = true;
                  break;
               }

               if (!localValue17.isAir() && !localValue17.isReplaceable() && !localValue17.getCollisionShape(localValue1, localValue3).isEmpty()) {
                  BlockPos localValue19 = new BlockPos(localValue13, localValue16 + 1, localValue14);
                  BlockPos localValue20 = new BlockPos(localValue13, localValue16 + 2, localValue14);
                  if (localValue1.getBlockState(localValue19).getCollisionShape(localValue1, localValue19).isEmpty() && localValue1.getBlockState(localValue20).getCollisionShape(localValue1, localValue20).isEmpty()) {
                     double localValue21 = localValue13 + 0.5;
                     double localValue23 = localValue16 + 1.1;
                     double localValue25 = localValue14 + 0.5;
                     double localValue27 = Math.hypot(localValue21 - localValue2.getX(), localValue25 - localValue2.getZ());
                     double localValue29 = localValue2.getY() - localValue23;
                     if (!(localValue29 < 2.0)) {
                        double localValue31 = Math.toDegrees(Math.atan2(localValue29, Math.max(0.5, localValue27)));
                        double localValue33 = -Math.abs(localValue31 - 20.0) * 2.0 - localValue27 * 0.3 - localValue29 * 0.1;
                        if (localValue33 > localValue8) {
                           localValue8 = localValue33;
                           localValue7 = new Vec3d(localValue21, localValue23, localValue25);
                        }
                     }
                  }
                  break;
               }
            }

            if (localValue15) {
            }
         }
      }

      return localValue7;
   }

   private ElytraFlightTask.InternalType0010 internalMethod05775(
      World localValue1, Vec3d localValue2, Vec3d localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9, int localValue10, int localValue11
   ) {
      Vec3d localValue12 = localValue2;
      Vec3d localValue13 = localValue3;
      float localValue14 = localValue4;
      float localValue15 = localValue5;
      int localValue16 = localValue10;
      ArrayList localValue17 = new ArrayList(localValue11);

      for (int localValue18 = 0; localValue18 < localValue11; localValue18++) {
         localValue14 = internalMethod00386(localValue14, localValue6, localValue8);
         localValue15 = internalMethod00386(localValue15, localValue7, localValue9);
         Vec3d localValue19 = internalMethod00018(localValue14, localValue15);
         double localValue20 = Math.toRadians(localValue15);
         double localValue22 = Math.hypot(localValue19.x, localValue19.z);
         double localValue24 = Math.hypot(localValue13.x, localValue13.z);
         double localValue26 = localValue19.length();
         double localValue28 = Math.cos(localValue20);
         localValue28 = localValue28 * localValue28 * Math.min(1.0, localValue26 / 0.4);
         localValue13 = localValue13.add(0.0, 0.08 * (-1.0 + localValue28 * 0.75), 0.0);
         if (localValue13.y < 0.0 && localValue22 > 0.0) {
            double localValue30 = localValue13.y * -0.1 * localValue28;
            localValue13 = localValue13.add(localValue19.x * localValue30 / localValue22, localValue30, localValue19.z * localValue30 / localValue22);
         }

         if (localValue20 < 0.0 && localValue22 > 0.0) {
            double localValue39 = localValue24 * -Math.sin(localValue20) * 0.04;
            localValue13 = localValue13.add(-localValue19.x * localValue39 / localValue22, localValue39 * 3.2, -localValue19.z * localValue39 / localValue22);
         }

         if (localValue22 > 0.0) {
            localValue13 = localValue13.add((localValue19.x / localValue22 * localValue24 - localValue13.x) * 0.1, 0.0, (localValue19.z / localValue22 * localValue24 - localValue13.z) * 0.1);
         }

         if (localValue16 > 0) {
            double localValue40 = 1.5;
            localValue13 = localValue13.add(
               localValue19.x * 0.1 + (localValue19.x * localValue40 - localValue13.x) * 0.5,
               localValue19.y * 0.1 + (localValue19.y * localValue40 - localValue13.y) * 0.5,
               localValue19.z * 0.1 + (localValue19.z * localValue40 - localValue13.z) * 0.5
            );
            localValue16--;
         }

         localValue13 = new Vec3d(localValue13.x * 0.99, localValue13.y * 0.98, localValue13.z * 0.99);
         Vec3d localValue41 = localValue12;
         localValue12 = localValue12.add(localValue13);
         localValue17.add(localValue12);
         double localValue31 = localValue13.length();
         int localValue33 = Math.max(1, (int)Math.ceil(localValue31 / 0.6));

         for (int localValue34 = 1; localValue34 <= localValue33; localValue34++) {
            Vec3d localValue35 = localValue41.add(localValue13.multiply((double)localValue34 / localValue33));
            ElytraFlightTask.InternalType0156 localValue36 = localValue34 == localValue33 ? this.internalMethod01068(localValue1, localValue35, false) : this.internalMethod01068(localValue1, localValue35, true);
            if (localValue36 == ElytraFlightTask.InternalType0156.internalField0288) {
               return new ElytraFlightTask.InternalType0010(localValue18, true, localValue12, localValue17);
            }

            if (localValue36 == ElytraFlightTask.InternalType0156.internalField1109) {
               return new ElytraFlightTask.InternalType0010(localValue18, true, localValue12, localValue17);
            }
         }
      }

      return new ElytraFlightTask.InternalType0010(localValue11, false, localValue12, localValue17);
   }

   private ElytraFlightTask.InternalType0156 internalMethod01068(World localValue1, Vec3d localValue2, boolean localValue3) {
      Mutable localValue4 = new Mutable();
      boolean localValue5 = false;
      double[][] localValue6 = localValue3 ? this.internalField0084.internalField0037 : this.internalField0084.internalField0036;

      for (double[] localValue10 : localValue6) {
         int localValue11 = MathHelper.floor(localValue2.x + localValue10[0]);
         int localValue12 = MathHelper.floor(localValue2.y + localValue10[1]);
         int localValue13 = MathHelper.floor(localValue2.z + localValue10[2]);
         if (localValue12 <= localValue1.getBottomY() + 2 || localValue12 >= localValue1.getTopYInclusive() - 2) {
            return ElytraFlightTask.InternalType0156.internalField0288;
         }

         localValue4.set(localValue11, localValue12, localValue13);
         if (!localValue1.isPosLoaded(localValue4)) {
            localValue5 = true;
         } else {
            BlockState localValue14 = localValue1.getBlockState(localValue4);
            if (internalMethod01826(localValue14)) {
               return ElytraFlightTask.InternalType0156.internalField0288;
            }

            FluidState localValue15 = localValue14.getFluidState();
            if (localValue15.isIn(FluidTags.LAVA)) {
               return ElytraFlightTask.InternalType0156.internalField0288;
            }

            if (!localValue14.isAir() && !localValue14.isReplaceable() && !localValue14.getCollisionShape(localValue1, localValue4).isEmpty()) {
               return ElytraFlightTask.InternalType0156.internalField0288;
            }
         }
      }

      return localValue5 ? ElytraFlightTask.InternalType0156.internalField1109 : ElytraFlightTask.InternalType0156.internalField0287;
   }

   private static boolean internalMethod01826(BlockState localValue0) {
      return localValue0.isOf(Blocks.LAVA)
         || localValue0.isOf(Blocks.FIRE)
         || localValue0.isOf(Blocks.SOUL_FIRE)
         || localValue0.isOf(Blocks.MAGMA_BLOCK)
         || localValue0.isOf(Blocks.CAMPFIRE)
         || localValue0.isOf(Blocks.SOUL_CAMPFIRE);
   }

   private boolean internalMethod06592(ClientPlayerEntity localValue1) {
      ItemStack localValue2 = localValue1.getEquippedStack(EquipmentSlot.CHEST);
      return !localValue2.isEmpty() && localValue2.isOf(Items.ELYTRA);
   }

   private boolean internalMethod07952(ClientPlayerEntity localValue1) {
      ItemStack localValue2 = localValue1.getEquippedStack(EquipmentSlot.CHEST);
      if (localValue2.isEmpty() || !localValue2.isOf(Items.ELYTRA)) {
         return false;
      } else {
         return !localValue2.isDamageable() ? false : localValue2.getMaxDamage() - localValue2.getDamage() <= 10;
      }
   }

   private void internalMethod02360(double localValue1) {
      if (!this.internalField1517 && !this.internalField1514 && !this.internalField1513) {
         if (!this.internalField1101) {
            if (localValue1 + 9.0 < this.internalField0193) {
               this.internalField0193 = localValue1;
               this.internalField1056 = 0;
               this.internalField1463 = 0;
               this.internalField1467 = 0;
               this.internalField1740 = 0;
            } else {
               this.internalField1056++;
            }
         }
      }
   }

   private double internalMethod01321(ClientPlayerEntity localValue1) {
      return Math.hypot(localValue1.getVelocity().x, localValue1.getVelocity().z);
   }

   private float internalMethod01322(ClientPlayerEntity localValue1) {
      Vec3d localValue2 = this.internalMethod01030().subtract(localValue1.getEyePos());
      return (float)Math.toDegrees(Math.atan2(localValue2.z, localValue2.x)) - 90.0F;
   }

   private double internalMethod06591(ClientPlayerEntity localValue1) {
      double localValue2 = localValue1.getX() - (this.internalField0227 + 0.5);
      double localValue4 = localValue1.getZ() - (this.internalField1053 + 0.5);
      if (!this.internalField0277) {
         return localValue2 * localValue2 + localValue4 * localValue4;
      } else {
         double localValue6 = localValue1.getY() - (this.internalField0228 + 0.5);
         return localValue2 * localValue2 + localValue6 * localValue6 + localValue4 * localValue4;
      }
   }

   private boolean internalMethod08961(ClientPlayerEntity localValue1) {
      double localValue2 = localValue1.getX() - (this.internalField0227 + 0.5);
      double localValue4 = localValue1.getZ() - (this.internalField1053 + 0.5);
      double localValue6 = Math.hypot(localValue2, localValue4);
      double localValue8 = MathHelper.clamp(this.internalMethod01321(localValue1) * 5.0 + 3.0, 8.0, 12.0);
      return localValue6 <= localValue8;
   }

   private double internalMethod04085() {
      return this.internalField0277 ? this.internalField0228 + 0.5 : this.internalField1045;
   }

   private static Vec3d internalMethod00018(float localValue0, float localValue1) {
      float localValue2 = (float)Math.toRadians(localValue0);
      float localValue3 = (float)Math.toRadians(localValue1);
      float localValue4 = MathHelper.cos(localValue3);
      return new Vec3d(-MathHelper.sin(localValue2) * localValue4, -MathHelper.sin(localValue3), MathHelper.cos(localValue2) * localValue4).normalize();
   }

   private static float internalMethod00386(float localValue0, float localValue1, float localValue2) {
      float localValue3 = RotationUtils.internalMethod08495(localValue0, localValue1);
      return Math.abs(localValue3) <= localValue2 ? localValue1 : localValue0 + Math.signum(localValue3) * localValue2;
   }

   @Override
   public void internalMethod04086() {
      this.internalMethod08148();
   }

   @Override
   public void internalMethod04089() {
      this.internalField0276 = true;
      NewtonCoreManager.internalMethod00114().internalMethod00183().internalMethod01287();
   }

   @Override
   public void internalMethod08146() {
      this.internalField0276 = false;
   }

   @Override
   public boolean internalMethod04090() {
      return this.internalField0276;
   }

   @Override
   public boolean internalMethod08147() {
      return this.internalField1099;
   }

   @Nullable
   @Override
   public String internalMethod09095() {
      return this.internalField0248;
   }

   private void internalMethod08148() {
      this.internalField0417 = List.of();
      this.internalField0416 = List.of();
      this.internalField0641 = null;
      NewtonCoreManager.internalMethod00114().internalMethod00183().internalMethod01287();
   }

   static enum InternalType0008 {
      internalField0082,
      internalField0083,
      internalField0985;

      public static ElytraFlightTask.InternalType0008[] internalMethod00063() {
         return values();
      }

      public static ElytraFlightTask.InternalType0008 internalMethod04498(String localValue0) {
         return Enum.valueOf(ElytraFlightTask.InternalType0008.class, localValue0);
      }
   }

   static final class InternalType0009 {
      final double internalField0194 = 8.0;
      final double internalField0193 = 12.0;
      final double internalField1045 = 9.0;
      final int internalField0227 = 500;
      final int internalField0228 = 600;
      final int internalField1053 = 2;
      final int internalField1055 = 4;
      final float internalField0205 = -6.0F;
      final int internalField1056 = 32;
      final int internalField1054 = 12;
      final int internalField1464 = 30;
      final int internalField1470 = 38;
      final float internalField0206 = 1.05F;
      final float internalField1048 = 6.0F;
      final float internalField1047 = 12.0F;
      final int internalField1465 = 10;
      final float internalField1049 = 50.0F;
      final float internalField1046 = 20.0F;
      final float[] internalField0615 = new float[]{-65.0F, -38.0F, -18.0F, 0.0F, 18.0F, 38.0F, 65.0F};
      final float[] internalField0616 = new float[]{-38.0F, -25.0F, -14.0F, -5.0F, 6.0F, 16.0F};
      final float[] internalField1238 = new float[]{0.0F, -35.0F, 35.0F, -70.0F, 70.0F, -110.0F, 110.0F, -150.0F, 150.0F, 180.0F};
      final float[] internalField1240 = new float[]{-50.0F, -35.0F, -20.0F, -8.0F, 6.0F};
      final float internalField1456 = 11.0F;
      final float internalField1457 = 6.0F;
      final float internalField1458 = 18.0F;
      final float internalField1459 = 11.0F;
      final float internalField1460 = 45.0F;
      final float internalField1461 = 35.0F;
      final float internalField1462 = 45.0F;
      final int internalField1463 = 50;
      final float internalField1455 = 7.5F;
      final float internalField1723 = 4.5F;
      final float internalField1731 = 14.0F;
      final float internalField1727 = 8.5F;
      final double internalField1043 = 0.22;
      final double internalField1042 = 0.16;
      final double internalField1044 = 0.1;
      final double internalField1453 = 0.5;
      final int internalField1466 = 16;
      final int internalField1467 = 118;
      final int internalField1469 = 45;
      final int internalField1468 = 100;
      final int internalField1740 = 112;
      final double internalField1449 = 40.0;
      final double internalField1447 = 14.0;
      final int[] internalField0618 = new int[]{0, 8, 20, 36, 56};
      final double internalField1448 = 14.0;
      final int internalField1741 = 40;
      final double internalField1454 = 28.0;
      final double internalField1450 = 6.0;
      final float internalField1728 = 34.0F;
      final int internalField1736 = 2;
      final int internalField1735 = 70;
      final int internalField1748 = 25;
      final int internalField1733 = 30;
      final int internalField1738 = 3;
      final int internalField1739 = 8;
      final int internalField1742 = 12;
      final int internalField1743 = 300;
      final int internalField1744 = 20;
      final int internalField1745 = 8;
      final float internalField1717 = 22.0F;
      final float internalField1718 = 6.0F;
      final double internalField1451 = 5.0;
      final double[][] internalField0036 = new double[][]{
         {0.0, 0.1, 0.0},
         {0.0, 0.9, 0.0},
         {0.0, 1.7, 0.0},
         {0.0, 2.3, 0.0},
         {0.0, -0.5, 0.0},
         {0.8, 0.1, 0.0},
         {0.8, 0.9, 0.0},
         {0.8, 1.7, 0.0},
         {-0.8, 0.1, 0.0},
         {-0.8, 0.9, 0.0},
         {-0.8, 1.7, 0.0},
         {0.0, 0.1, 0.8},
         {0.0, 0.9, 0.8},
         {0.0, 1.7, 0.8},
         {0.0, 0.1, -0.8},
         {0.0, 0.9, -0.8},
         {0.0, 1.7, -0.8},
         {0.6, 0.1, 0.6},
         {-0.6, 0.1, 0.6},
         {0.6, 0.1, -0.6},
         {-0.6, 0.1, -0.6},
         {0.6, 0.9, 0.6},
         {-0.6, 0.9, 0.6},
         {0.6, 0.9, -0.6},
         {-0.6, 0.9, -0.6},
         {0.6, 1.7, 0.6},
         {-0.6, 1.7, 0.6},
         {0.6, 1.7, -0.6},
         {-0.6, 1.7, -0.6}
      };
      final double[][] internalField0037 = new double[][]{
         {0.0, 0.1, 0.0},
         {0.0, 0.9, 0.0},
         {0.0, 1.7, 0.0},
         {0.7, 0.9, 0.0},
         {-0.7, 0.9, 0.0},
         {0.0, 0.9, 0.7},
         {0.0, 0.9, -0.7},
         {0.7, 0.1, 0.0},
         {-0.7, 0.1, 0.0},
         {0.0, 0.1, 0.7},
         {0.0, 0.1, -0.7},
         {0.7, 1.7, 0.0},
         {-0.7, 1.7, 0.0},
         {0.0, 1.7, 0.7},
         {0.0, 1.7, -0.7},
         {0.5, 0.9, 0.5},
         {-0.5, 0.9, 0.5},
         {0.5, 0.9, -0.5},
         {-0.5, 0.9, -0.5}
      };
   }

   static final class InternalType0010 {
      final int internalField0227;
      final boolean internalField0277;
      final Vec3d internalField0283;
      final List<Vec3d> internalField0416;

      InternalType0010(int localValue1, boolean localValue2, Vec3d localValue3, List<Vec3d> localValue4) {
         this.internalField0227 = localValue1;
         this.internalField0277 = localValue2;
         this.internalField0283 = localValue3;
         this.internalField0416 = localValue4;
      }
   }

   static enum InternalType0156 {
      internalField0287,
      internalField0288,
      internalField1109;

      public static ElytraFlightTask.InternalType0156[] internalMethod00314() {
         return values();
      }

      public static ElytraFlightTask.InternalType0156 internalMethod05871(String localValue0) {
         return Enum.valueOf(ElytraFlightTask.InternalType0156.class, localValue0);
      }
   }

   static final class InternalType0157 {
      private final float internalField0205;
      private final float internalField0206;
      private final int internalField0227;
      private final boolean internalField0277;

      InternalType0157(float localValue1, float localValue2, int localValue3, boolean localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField0227 = localValue3;
         this.internalField0277 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0157[yaw=" + this.internalField0205 + ", pitch=" + this.internalField0206 + ", safeTicks=" + this.internalField0227 + ", emergency=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ElytraFlightTask.InternalType0157 other = (ElytraFlightTask.InternalType0157) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public float internalMethod06243() {
         return this.internalField0205;
      }

      public float internalMethod06247() {
         return this.internalField0206;
      }

      public int internalMethod06244() {
         return this.internalField0227;
      }

      public boolean internalMethod06245() {
         return this.internalField0277;
      }
   }
}
