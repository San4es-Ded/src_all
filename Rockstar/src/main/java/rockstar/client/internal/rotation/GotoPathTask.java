package rockstar.client.internal.rotation;






import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public final class GotoPathTask implements NewtonTask {
   private final PathGoal internalField0925;
   @Nullable
   private PathExecutor internalField0906;
   @Nullable
   private CompletableFuture<Optional<ComputedPath>> internalField0641;
   private boolean internalField0277;
   private boolean internalField0276;
   @Nullable
   private String internalField0248;
   private long internalField0229 = 0L;
   private int internalField0227 = 0;
   @Nullable
   private CompletableFuture<Optional<ComputedPath>> internalField0640;
   @Nullable
   private ComputedPath internalField0905;
   @Nullable
   private PathNode internalField0923;
   private int internalField1055 = -1;
   private long internalField1059;
   @Nullable
   private AtomicBoolean internalField0020;
   private boolean internalField1099;
   private int internalField1054 = -1;
   private int internalField1464;
   private boolean internalField1100;
   private int internalField1470;
   public GotoPathTask(PathGoal localValue1) {
      this.internalField0925 = localValue1;
   }

   @Override
   public String internalMethod01129() {
      return "goto";
   }

   @Override
   public String internalMethod05788() {
      if (this.internalField0277) {
         return "\u043f\u0430\u0443\u0437\u0430";
      } else if (this.internalField0276) {
         return "\u0433\u043e\u0442\u043e\u0432\u043e";
      } else if (this.internalField0641 != null && !this.internalField0641.isDone()) {
         return "\u043f\u043e\u0438\u0441\u043a \u043f\u0443\u0442\u0438...";
      } else {
         return this.internalField0906 == null
            ? "\u043f\u0443\u0442\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d"
            : "\u0448\u0430\u0433 "
               + (this.internalField0906.internalMethod03485() + 1)
               + "/"
               + this.internalField0906.internalMethod00712().internalMethod02878().size();
      }
   }

   @Override
   public boolean internalMethod04087() {
      if (this.internalField0277) {
         return false;
      } else if (this.internalField0276) {
         return true;
      } else {
         MinecraftClient localValue1 = MinecraftClient.getInstance();
         if (localValue1.player == null) {
            return false;
         } else {
            SilentRotationHelper.internalMethod06440();
            if (this.internalMethod06823(localValue1)) {
               return false;
            } else if (this.internalField1100) {
               return this.internalMethod08738(localValue1);
            } else {
               if (this.internalField0641 != null) {
                  if (!this.internalField0641.isDone()) {
                     this.internalMethod06822(localValue1);
                     return false;
                  }

                  Optional localValue2 = this.internalField0641.getNow(Optional.empty());
                  this.internalField0641 = null;
                  if (localValue2.isEmpty()) {
                     ChatMessageHelper.internalMethod06835("\u041f\u0443\u0442\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
                     this.internalField0248 = "\u043f\u0443\u0442\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d";
                     this.internalMethod04086();
                     return true;
                  }

                  ComputedPath localValue3 = (ComputedPath)localValue2.get();
                  this.internalField0906 = new PathExecutor(localValue3, this.internalField0925);
                  NewtonCoreManager.internalMethod00114().internalMethod01913(this.internalField0906);
                  ChatMessageHelper.internalMethod09026(
                     "\u041f\u0443\u0442\u044c \u043d\u0430\u0439\u0434\u0435\u043d: " + localValue3.internalMethod02878().size() + " \u0448\u0430\u0433\u043e\u0432"
                  );
                  NewtonEventDispatcher.internalMethod06392(this.internalMethod01129(), localValue3.internalMethod02878().size());
               }

               if (this.internalField0906 == null) {
                  this.internalMethod09445();
                  this.internalMethod06822(localValue1);
                  return false;
               } else if (this.internalField1054 >= 0) {
                  this.internalMethod07431(localValue1);
                  return false;
               } else if (this.internalMethod07943()) {
                  if (this.internalMethod07432(localValue1)) {
                     return false;
                  } else {
                     this.internalMethod09445();
                     this.internalField0906 = null;
                     NewtonCoreManager.internalMethod00114().internalMethod01913(null);
                     return false;
                  }
               } else {
                  this.internalMethod07942();
                  this.internalMethod08100();
                  int localValue9 = this.internalField0906.internalMethod03485();
                  PathExecutor.InternalType0263 localValue10 = this.internalField0906.internalMethod04868();
                  this.internalMethod05175(localValue9, this.internalField0906);
                  switch (localValue10) {
                     case internalField0720:
                        return false;
                     case internalField0719:
                        if (this.internalField0925
                           .internalMethod05088((int)Math.floor(localValue1.player.getX()), (int)Math.floor(localValue1.player.getY()), (int)Math.floor(localValue1.player.getZ()))) {
                           ChatMessageHelper.internalMethod00196("\u0414\u043e\u0448\u043b\u0438 \u0434\u043e \u0446\u0435\u043b\u0438");
                           this.internalField0276 = true;
                           this.internalMethod09446();
                           return true;
                        } else if (this.internalField0905 != null
                           && this.internalField0923 != null
                           && this.internalField0923.equals(this.internalField0906.internalMethod00712().internalMethod01269())) {
                           ComputedPath localValue11 = this.internalField0905;
                           this.internalField0905 = null;
                           this.internalField0923 = null;
                           this.internalField1099 = false;
                           this.internalField0906 = new PathExecutor(localValue11, this.internalField0925);
                           NewtonCoreManager.internalMethod00114().internalMethod01913(this.internalField0906);
                           NewtonEventDispatcher.internalMethod06392(this.internalMethod01129(), localValue11.internalMethod02878().size());
                           return false;
                        } else {
                           this.internalMethod08107();
                           this.internalField0906 = null;
                           NewtonCoreManager.internalMethod00114().internalMethod01913(null);
                           Vec3d localValue4 = this.internalField0925.internalMethod07298();
                           double localValue5 = Math.hypot(localValue4.x - localValue1.player.getX(), localValue4.z - localValue1.player.getZ());
                           double localValue7 = localValue4.y - localValue1.player.getY();
                           if (localValue5 <= 2.0 && localValue7 > -3.5 && localValue7 < 0.62) {
                              this.internalField1100 = true;
                              this.internalField1470 = 0;
                              return false;
                           }

                           this.internalMethod09445();
                           return false;
                        }
                     case internalField1287:
                        this.internalField0227++;
                        if (this.internalField0227 > 5) {
                           ChatMessageHelper.internalMethod06835(
                              "\u0421\u043b\u0438\u0448\u043a\u043e\u043c \u043c\u043d\u043e\u0433\u043e \u0441\u0431\u043e\u0435\u0432, \u043e\u0441\u0442\u0430\u043d\u0430\u0432\u043b\u0438\u0432\u0430\u0435\u043c\u0441\u044f"
                           );
                           this.internalField0248 = "\u0441\u043b\u0438\u0448\u043a\u043e\u043c \u043c\u043d\u043e\u0433\u043e \u0441\u0431\u043e\u0435\u0432";
                           this.internalMethod04086();
                           return true;
                        }

                        this.internalMethod08107();
                        this.internalField0906 = null;
                        NewtonCoreManager.internalMethod00114().internalMethod01913(null);
                        this.internalMethod09445();
                        return false;
                     default:
                        return false;
                  }
               }
            }
         }
      }
   }

   private void internalMethod07942() {
      if (this.internalField0906 != null && this.internalField0640 == null && this.internalField0905 == null) {
         List localValue1 = this.internalField0906.internalMethod00712().internalMethod02878();
         int localValue2 = localValue1.size() - this.internalField0906.internalMethod03485();
         PathNode localValue3 = this.internalField0906.internalMethod00712().internalMethod01269();
         boolean localValue4 = this.internalField0925.internalMethod05088(localValue3.internalMethod02945(), localValue3.internalMethod02949(), localValue3.internalMethod07945());
         if (localValue2 <= 8) {
            if (!localValue4 && !this.internalField1099) {
               this.internalField1055 = -1;
               this.internalField0923 = localValue3;
               this.internalField0020 = new AtomicBoolean();
               this.internalField0640 = NewtonPathfinder.internalMethod06790(localValue3, this.internalField0925, this.internalField0020);
            }
         } else if (!localValue4) {
            long localValue5 = System.currentTimeMillis();
            if (localValue5 - this.internalField1059 >= 2500L) {
               int localValue7 = this.internalField0906.internalMethod03485() + 12;
               if (localValue7 < localValue1.size()) {
                  this.internalField1059 = localValue5;
                  this.internalField1055 = localValue7;
                  this.internalField0923 = this.internalField0906.internalMethod00712().internalMethod05363().get(localValue7);
                  this.internalField0020 = new AtomicBoolean();
                  this.internalField0640 = NewtonPathfinder.internalMethod06790(this.internalField0923, this.internalField0925, this.internalField0020);
               }
            }
         }
      }
   }

   private void internalMethod08100() {
      if (this.internalField0640 != null && this.internalField0640.isDone()) {
         Optional localValue1 = this.internalField0640.getNow(Optional.empty());
         this.internalField0640 = null;
         boolean localValue2 = this.internalField1055 >= 0;
         if (!localValue1.isEmpty() && !((ComputedPath)localValue1.get()).internalMethod02878().isEmpty()) {
            if (localValue2) {
               this.internalMethod01073((ComputedPath)localValue1.get());
               this.internalField1055 = -1;
               this.internalField0923 = null;
            } else {
               this.internalField0905 = (ComputedPath)localValue1.get();
            }
         } else {
            if (!localValue2) {
               this.internalField1099 = true;
            }

            this.internalField1055 = -1;
            if (localValue2) {
               this.internalField0923 = null;
            }
         }
      }
   }

   private void internalMethod01073(ComputedPath localValue1) {
      if (this.internalField0906 != null && this.internalField0923 != null) {
         List localValue2 = this.internalField0906.internalMethod00712().internalMethod05363();
         List localValue3 = this.internalField0906.internalMethod00712().internalMethod02878();
         int localValue4 = this.internalField0906.internalMethod03485();
         int localValue5 = this.internalField1055;
         if (localValue5 > localValue4 && localValue5 < localValue2.size() && ((PathNode)localValue2.get(localValue5)).equals(this.internalField0923)) {
            if (localValue1.internalMethod00570().equals(this.internalField0923)) {
               PathNode localValue6 = this.internalField0906.internalMethod00712().internalMethod01269();
               PathNode localValue7 = localValue1.internalMethod01269();
               boolean localValue8 = this.internalField0925.internalMethod05088(localValue7.internalMethod02945(), localValue7.internalMethod02949(), localValue7.internalMethod07945());
               if (localValue8
                  || !(
                     this.internalField0925.internalMethod05087(localValue7.internalMethod02945(), localValue7.internalMethod02949(), localValue7.internalMethod07945())
                        >= this.internalField0925.internalMethod05087(localValue6.internalMethod02945(), localValue6.internalMethod02949(), localValue6.internalMethod07945()) - 1.0
                  )) {
                  ArrayList localValue9 = new ArrayList(localValue2.subList(localValue4, localValue5 + 1));
                  localValue9.addAll(localValue1.internalMethod05363().subList(1, localValue1.internalMethod05363().size()));
                  ArrayList localValue10 = new ArrayList(localValue3.subList(localValue4, localValue5));
                  localValue10.addAll(localValue1.internalMethod02878());
                  this.internalField0906 = new PathExecutor(new ComputedPath(localValue9, localValue10), this.internalField0925);
                  NewtonCoreManager.internalMethod00114().internalMethod01913(this.internalField0906);
                  NewtonEventDispatcher.internalMethod06392(this.internalMethod01129(), localValue10.size());
               }
            }
         }
      }
   }

   private void internalMethod08107() {
      if (this.internalField0020 != null) {
         this.internalField0020.set(true);
      }

      this.internalField0020 = null;
      this.internalField0640 = null;
      this.internalField0905 = null;
      this.internalField0923 = null;
      this.internalField1055 = -1;
      this.internalField1099 = false;
   }

   private boolean internalMethod07432(MinecraftClient localValue1) {
      if (this.internalField0906 != null && localValue1.player != null) {
         PathfindBlockView localValue2;
         try {
            localValue2 = new PathfindBlockView();
         } catch (IllegalStateException localValue23) {
            return false;
         }

         List localValue3 = this.internalField0906.internalMethod00712().internalMethod05363();
         double localValue4 = localValue1.player.getX();
         double localValue6 = localValue1.player.getY();
         double localValue8 = localValue1.player.getZ();
         int localValue10 = this.internalField0906.internalMethod03485();
         int localValue11 = Math.min(localValue3.size() - 1, localValue10 + 15);
         int localValue12 = -1;
         double localValue13 = 3.5;

         for (int localValue15 = localValue10; localValue15 <= localValue11; localValue15++) {
            PathNode localValue16 = (PathNode)localValue3.get(localValue15);
            double localValue17 = localValue16.internalMethod02949() - localValue6;
            if (!(localValue17 > 1.2) && !(localValue17 < -2.5)) {
               double localValue19 = Math.hypot(localValue16.internalMethod02945() + 0.5 - localValue4, localValue16.internalMethod07945() + 0.5 - localValue8);
               if (!(localValue19 >= localValue13)) {
                  double localValue21 = Math.max(localValue6, (double)localValue16.internalMethod02949()) + 0.05;
                  if (localValue2.internalMethod03056(
                     (int)Math.floor(localValue4), (int)Math.floor(localValue8), localValue16.internalMethod02945(), localValue16.internalMethod07945(), localValue21, localValue6 + 1.85
                  )) {
                     localValue12 = localValue15;
                     localValue13 = localValue19;
                  }
               }
            }
         }

         if (localValue12 < 0) {
            return false;
         } else {
            this.internalField1054 = localValue12;
            this.internalField1464 = 0;
            NewtonCoreManager.internalMethod00114().internalMethod01913(null);
            return true;
         }
      } else {
         return false;
      }
   }

   private void internalMethod07431(MinecraftClient localValue1) {
      if (localValue1.player != null && this.internalField0906 != null) {
         List localValue2 = this.internalField0906.internalMethod00712().internalMethod05363();
         if (this.internalField1054 >= localValue2.size()) {
            this.internalField1054 = -1;
         } else {
            PathNode localValue3 = (PathNode)localValue2.get(this.internalField1054);
            double localValue4 = localValue1.player.getX();
            double localValue6 = localValue1.player.getY();
            double localValue8 = localValue1.player.getZ();
            double localValue10 = Math.hypot(localValue3.internalMethod02945() + 0.5 - localValue4, localValue3.internalMethod07945() + 0.5 - localValue8);
            double localValue12 = localValue6 - localValue3.internalMethod02949();
            if (localValue10 < 0.5 && localValue12 > -1.2 && localValue12 < 1.2) {
               this.internalField0906.internalMethod01173(this.internalField1054);
               NewtonCoreManager.internalMethod00114().internalMethod01913(this.internalField0906);
               this.internalField1054 = -1;
            } else if (++this.internalField1464 <= 60 && !(localValue10 > 6.0)) {
               float localValue14 = (float)Math.toDegrees(Math.atan2(localValue3.internalMethod07945() + 0.5 - localValue8, localValue3.internalMethod02945() + 0.5 - localValue4)) - 90.0F;
               SilentRotationHelper.internalMethod05978(new Rotation(localValue14, 0.0F));
               MovementInputController localValue15 = NewtonCoreManager.internalMethod00114().internalMethod00183();
               localValue15.internalMethod01281();
               localValue15.internalMethod09358(false);
               localValue15.internalMethod03557(false);
               localValue15.internalMethod08033(false);
               localValue15.internalMethod08045(false);
               localValue15.internalMethod08371(false);
               localValue15.internalMethod03508(true);
               boolean localValue16 = localValue1.player.isTouchingWater()
                  ? localValue6 < localValue3.internalMethod02949() + 0.2
                  : localValue1.player.horizontalCollision && localValue1.player.isOnGround();
               localValue15.internalMethod08359(localValue16);
            } else {
               this.internalField1054 = -1;
               this.internalField0906 = null;
               this.internalMethod09445();
            }
         }
      } else {
         this.internalField1054 = -1;
      }
   }

   private boolean internalMethod06823(MinecraftClient localValue1) {
      if (localValue1.player == null) {
         return false;
      } else if (!localValue1.player.isSubmergedInWater()) {
         return false;
      } else if (localValue1.player.getAir() >= 80) {
         return false;
      } else {
         SilentRotationHelper.internalMethod05978(new Rotation(localValue1.player.getYaw(), -90.0F));
         MovementInputController localValue2 = NewtonCoreManager.internalMethod00114().internalMethod00183();
         localValue2.internalMethod01281();
         localValue2.internalMethod03508(true);
         localValue2.internalMethod03557(false);
         localValue2.internalMethod08033(false);
         localValue2.internalMethod08045(false);
         localValue2.internalMethod08371(false);
         localValue2.internalMethod09358(true);
         localValue2.internalMethod08359(true);
         return true;
      }
   }

   private void internalMethod06822(MinecraftClient localValue1) {
      MovementInputController localValue2 = NewtonCoreManager.internalMethod00114().internalMethod00183();
      localValue2.internalMethod01281();
      localValue2.internalMethod03508(false);
      localValue2.internalMethod03557(false);
      localValue2.internalMethod08033(false);
      localValue2.internalMethod08045(false);
      localValue2.internalMethod09358(false);
      localValue2.internalMethod08371(false);
      localValue2.internalMethod08359(localValue1.player != null && localValue1.player.isTouchingWater());
   }

   private boolean internalMethod07943() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.player != null && this.internalField0906 != null) {
         AbstractPathStep localValue2 = this.internalField0906.internalMethod02430();
         if (localValue2 == null) {
            return false;
         } else {
            double localValue3 = localValue1.player.getX();
            double localValue5 = localValue1.player.getY();
            double localValue7 = localValue1.player.getZ();
            PathNode localValue9 = localValue2.internalMethod01873();
            PathNode localValue10 = localValue2.internalMethod02540();
            double localValue11 = Math.hypot(localValue3 - (localValue9.internalMethod02945() + 0.5), localValue7 - (localValue9.internalMethod07945() + 0.5))
               + Math.abs(localValue5 - localValue9.internalMethod02949());
            double localValue13 = Math.hypot(localValue3 - (localValue10.internalMethod02945() + 0.5), localValue7 - (localValue10.internalMethod07945() + 0.5))
               + Math.abs(localValue5 - localValue10.internalMethod02949());
            return Math.min(localValue11, localValue13) > 2.5;
         }
      } else {
         return false;
      }
   }

   private void internalMethod09445() {
      long localValue1 = System.currentTimeMillis();
      if (localValue1 - this.internalField0229 >= 250L) {
         this.internalField0229 = localValue1;
         if (this.internalField0641 == null || this.internalField0641.isDone()) {
            this.internalMethod08107();
            MinecraftClient localValue3 = MinecraftClient.getInstance();
            if (localValue3.player != null) {
               PathNode localValue4 = PathStartLocator.internalMethod05963(localValue3);
               this.internalField0641 = NewtonPathfinder.internalMethod03533(localValue4, this.internalField0925);
            }
         }
      }
   }

   @Override
   public void internalMethod04086() {
      this.internalMethod09446();
   }

   @Override
   public void internalMethod04089() {
      this.internalField0277 = true;
      MovementInputController localValue1 = NewtonCoreManager.internalMethod00114().internalMethod00183();
      localValue1.internalMethod03508(false);
      localValue1.internalMethod03557(false);
      localValue1.internalMethod08033(false);
      localValue1.internalMethod08045(false);
      localValue1.internalMethod08359(false);
      localValue1.internalMethod09358(false);
   }

   @Override
   public void internalMethod08146() {
      this.internalField0277 = false;
   }

   @Override
   public boolean internalMethod04090() {
      return this.internalField0277;
   }

   @Override
   public boolean internalMethod08147() {
      return this.internalField0276;
   }

   @Nullable
   @Override
   public String internalMethod09095() {
      return this.internalField0248;
   }

   private void internalMethod05175(int localValue1, PathExecutor localValue2) {
      List localValue3 = localValue2.internalMethod00712().internalMethod02878();
      int localValue4 = localValue3.size();
      int localValue5 = Math.min(localValue2.internalMethod03485(), localValue4);

      for (int localValue6 = Math.max(localValue1, 0); localValue6 < localValue5; localValue6++) {
         PathNode localValue7 = ((AbstractPathStep)localValue3.get(localValue6)).internalMethod02540();
         NewtonEventDispatcher.internalMethod00498(
            this.internalMethod01129(), localValue7.internalMethod02945(), localValue7.internalMethod02949(), localValue7.internalMethod07945(), localValue6 + 1, localValue4
         );
      }
   }

   private boolean internalMethod08738(MinecraftClient localValue1) {
      if (localValue1.player == null) {
         return false;
      } else {
         int localValue2 = (int)Math.floor(localValue1.player.getX());
         int localValue3 = (int)Math.floor(localValue1.player.getY());
         int localValue4 = (int)Math.floor(localValue1.player.getZ());
         if (this.internalField0925.internalMethod05088(localValue2, localValue3, localValue4)) {
            ChatMessageHelper.internalMethod00196("\u0414\u043e\u0448\u043b\u0438 \u0434\u043e \u0446\u0435\u043b\u0438");
            this.internalField0276 = true;
            this.internalField1100 = false;
            this.internalMethod09446();
            return true;
         } else {
            Vec3d localValue5 = this.internalField0925.internalMethod07298();
            double localValue6 = localValue5.x - localValue1.player.getX();
            double localValue8 = localValue5.z - localValue1.player.getZ();
            double localValue10 = Math.hypot(localValue6, localValue8);
            if (!(localValue10 > 3.0) && this.internalField1470++ <= 100) {
               MovementInputController localValue12 = NewtonCoreManager.internalMethod00114().internalMethod00183();
               localValue12.internalMethod01281();
               localValue12.internalMethod09358(false);
               localValue12.internalMethod08359(false);
               localValue12.internalMethod03557(false);
               localValue12.internalMethod08033(false);
               localValue12.internalMethod08045(false);
               localValue12.internalMethod08371(false);
               if (localValue10 < 0.15) {
                  localValue12.internalMethod03508(false);
               } else {
                  if (localValue10 > 0.35) {
                     float localValue13 = (float)Math.toDegrees(Math.atan2(localValue8, localValue6)) - 90.0F;
                     SilentRotationHelper.internalMethod05978(new Rotation(localValue13, 0.0F));
                  }

                  localValue12.internalMethod03508(true);
               }

               return false;
            } else {
               this.internalField1100 = false;
               this.internalMethod09445();
               return false;
            }
         }
      }
   }

   private void internalMethod09446() {
      MovementInputController localValue1 = NewtonCoreManager.internalMethod00114().internalMethod00183();
      localValue1.internalMethod01287();
      this.internalField0906 = null;
      this.internalField0641 = null;
      this.internalField1100 = false;
      this.internalField1054 = -1;
      this.internalMethod08107();
      NewtonCoreManager.internalMethod00114().internalMethod01913(null);
   }

   @Generated
   public PathGoal internalMethod07164() {
      return this.internalField0925;
   }
}
