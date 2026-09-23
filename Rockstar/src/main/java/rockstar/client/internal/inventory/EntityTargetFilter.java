package rockstar.client.internal.inventory;




import rockstar.client.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import globals.client.Information;
import globals.shared.proto.Packets;
import java.util.Comparator;
import java.util.function.Function;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;

public class EntityTargetFilter implements MinecraftClientAccess {
   boolean internalField0277;
   boolean internalField0276;
   boolean internalField1099;
   boolean internalField1100;
   boolean internalField1102;
   boolean internalField1101;
   boolean internalField1516;
   boolean internalField1517;
   boolean internalField1512;
   float internalField0205 = -1.0F;
   Function<Entity, Float> internalField0571 = localValue1 -> this.internalField0205;
   Comparator<Entity> internalField0757;

   public EntityTargetFilter() {
      this.internalField0757 = EntityComparatorUtils.internalField0757;
   }

   public boolean internalMethod05417(Entity localValue1) {
      if (internalField0149.player != null && internalField0149.world != null && localValue1 != null) {
         if (localValue1 instanceof FakePlayerEntity
            && RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class).isEnabled()) {
            return false;
         } else if (localValue1 instanceof LivingEntity && localValue1 != internalField0149.player) {
            if (localValue1 instanceof LivingEntity localValue2 && localValue2.isDead()) {
               return false;
            } else if (!this.internalMethod03111((Entity)localValue1)) {
               return false;
            } else if (localValue1 instanceof ArmorStandEntity) {
               return this.internalField1517 && (this.internalField1102 || !localValue1.isInvisible());
            } else {
               if (!this.internalField1100) {
                  for (Packets.InternalType0018 localValue3 : Information.getVisiblePlayers()) {
                     if (localValue3.gameInfo() != null && localValue3.gameInfo().nickname() != null && localValue3.gameInfo().nickname().equals(localValue1.getName().getString())) {
                        return false;
                     }
                  }
               }

               switch (localValue1) {
                  case PlayerEntity localValue4:
                     if (AntiBotModule.internalMethod07596(localValue4)) {
                        return false;
                     } else {
                        boolean localValue10 = RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue4.getName().getString());
                        if (!this.internalField1516 && localValue10) {
                           return false;
                        } else if (this.internalField1512 && internalMethod05465(internalField0149.player, localValue4)) {
                           return false;
                        } else {
                           boolean localValue11 = this.internalMethod03664(localValue4);
                           boolean localValue7 = localValue4.isInvisible();
                           if (!localValue7 || this.internalField1102 || this.internalField0277 && !localValue11) {
                              if (!this.internalField0277 && !this.internalField1101) {
                                 return false;
                              }

                              if (this.internalField0277 && this.internalField1101) {
                                 return true;
                              }

                              if (!this.internalField0277) {
                                 return false;
                              }

                              return !localValue11;
                           }

                           return false;
                        }
                     }
                  case AnimalEntity localValue5:
                     if (localValue5.isInvisible() && !this.internalField1102) {
                        return false;
                     }

                     return this.internalField0276;
                  case MobEntity localValue6:
                     if (localValue6.isInvisible() && !this.internalField1102) {
                        return false;
                     }

                     return this.internalField1099;
                  default:
                     return false;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final boolean internalMethod03111(Entity localValue1) {
      if (internalField0149.player == null) {
         return false;
      } else {
         float localValue2 = this.internalField0571.apply(localValue1);
         return localValue2 <= 0.0F ? true : internalField0149.player.getEyePos().distanceTo(RotationUtils.internalMethod01412(localValue1)) <= localValue2;
      }
   }

   private boolean internalMethod03664(PlayerEntity localValue1) {
      for (ItemStack localValue3 : rockstar.client.util.LegacyItemTypes.armorItems(localValue1)) {
         if (localValue3 != null && !localValue3.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public static boolean internalMethod05465(PlayerEntity localValue0, PlayerEntity localValue1) {
      if (localValue0 != null && localValue1 != null && localValue0 != localValue1) {
         Team localValue2 = localValue0.getScoreboardTeam();
         Team localValue3 = localValue1.getScoreboardTeam();
         if (localValue2 != null && localValue3 != null) {
            Formatting localValue4 = localValue2.getColor();
            Formatting localValue5 = localValue3.getColor();
            boolean localValue6 = localValue4 != null && localValue4 != Formatting.RESET;
            boolean localValue7 = localValue5 != null && localValue5 != Formatting.RESET;
            if (localValue6 && localValue7) {
               return localValue4 == localValue5;
            } else {
               String localValue8 = localValue2.getName();
               String localValue9 = localValue3.getName();
               return localValue8 != null && !localValue8.isBlank() && localValue8.equals(localValue9);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Generated
   public boolean internalMethod04708() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod04709() {
      return this.internalField0276;
   }

   @Generated
   public boolean internalMethod08436() {
      return this.internalField1099;
   }

   @Generated
   public boolean internalMethod08437() {
      return this.internalField1100;
   }

   @Generated
   public boolean internalMethod08448() {
      return this.internalField1102;
   }

   @Generated
   public boolean internalMethod08449() {
      return this.internalField1101;
   }

   @Generated
   public boolean internalMethod09729() {
      return this.internalField1516;
   }

   @Generated
   public boolean internalMethod09730() {
      return this.internalField1517;
   }

   @Generated
   public boolean internalMethod09737() {
      return this.internalField1512;
   }

   @Generated
   public float internalMethod04707() {
      return this.internalField0205;
   }

   @Generated
   public Function<Entity, Float> internalMethod02735() {
      return this.internalField0571;
   }

   @Generated
   public Comparator<Entity> internalMethod00532() {
      return this.internalField0757;
   }

   public static class InternalType0309 {
      private final EntityTargetFilter internalField0393 = new EntityTargetFilter();

      public EntityTargetFilter.InternalType0309 internalMethod00547(boolean localValue1) {
         this.internalField0393.internalField0277 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod06455(boolean localValue1) {
         this.internalField0393.internalField0276 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod08543(boolean localValue1) {
         this.internalField0393.internalField1099 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod08126(boolean localValue1) {
         this.internalField0393.internalField1100 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod07990(boolean localValue1) {
         this.internalField0393.internalField1102 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod09114(boolean localValue1) {
         this.internalField0393.internalField1101 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod09525(boolean localValue1) {
         this.internalField0393.internalField1516 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod09298(boolean localValue1) {
         this.internalField0393.internalField1517 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod09220(boolean localValue1) {
         this.internalField0393.internalField1512 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod03468(float localValue1) {
         this.internalField0393.internalField0205 = localValue1;
         this.internalField0393.internalField0571 = localValue1x -> localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod04689(Function<Entity, Float> localValue1) {
         this.internalField0393.internalField0571 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod04109(Comparator<Entity> localValue1) {
         this.internalField0393.internalField0757 = localValue1;
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod07121(Function<Entity, Double> localValue1) {
         this.internalField0393.internalField0757 = EntityComparatorUtils.internalMethod04791(localValue1);
         return this;
      }

      public EntityTargetFilter.InternalType0309 internalMethod08410(Function<Entity, Double> localValue1) {
         this.internalField0393.internalField0757 = EntityComparatorUtils.internalMethod06849(localValue1);
         return this;
      }

      public EntityTargetFilter internalMethod03528() {
         return this.internalField0393;
      }
   }
}
