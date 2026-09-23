package rockstar.client.esp;




import rockstar.client.setting.*;
import rockstar.client.event.*;
import rockstar.client.*;
import globals.client.Information;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import pyrock.events.render.Render3DEvent;

public class ScriptEspFeature extends EspFeature {
   private final Object internalField0290;
   private final BooleanSetting internalField0650;
   private volatile ScriptEspFeature.InternalType0175 internalField0146;
   private volatile ScriptEspFeature.InternalType0174 internalField0145;
   private volatile Predicate<Entity> internalField0486;
   private final EventListener<Render3DEvent> internalField0157 = localValue1x -> {
      if (this.internalField0146 != null || this.internalField0145 != null) {
         if (internalField0149.world != null && internalField0149.player != null) {
            if (EspManager.internalMethod03145() && this.internalMethod06968()) {
               ArrayList localValue2x = new ArrayList();

               for (Entity localValue4 : internalField0149.world.getEntities()) {
                  if (this.internalMethod04487(localValue4) && (this.internalField0486 == null || this.internalField0486.test(localValue4))) {
                     localValue2x.add(localValue4);
                  }
               }

               if (!localValue2x.isEmpty()) {
                  ScriptEspFeature.InternalType0174 localValue7 = this.internalField0145;
                  if (localValue7 != null) {
                     localValue7.render(localValue2x, localValue1x);
                  } else {
                     ScriptEspFeature.InternalType0175 localValue8 = this.internalField0146;
                     if (localValue8 != null) {
                        for (Entity localValue6 : (Iterable<Entity>)(Iterable<?>)localValue2x) {
                           localValue8.render(localValue6, localValue1x);
                        }
                     }
                  }
               }
            }
         }
      }
   };

   public ScriptEspFeature(Object localValue1, String localValue2, EntityTargetType... localValue3) {
      super(localValue2, new ItemTargetType[]{ItemTargetType.internalField0013}, localValue3);
      this.internalField0290 = localValue1;
      this.internalField0650 = this.internalMethod02236(localValue2);
   }

   public Object internalMethod05817() {
      return this.internalField0290;
   }

   public BooleanSetting internalMethod01413() {
      return this.internalField0650;
   }

   public void internalMethod02083(ScriptEspFeature.InternalType0175 localValue1) {
      this.internalField0146 = localValue1;
   }

   public void internalMethod02080(ScriptEspFeature.InternalType0174 localValue1) {
      this.internalField0145 = localValue1;
   }

   public void internalMethod05137(Predicate<Entity> localValue1) {
      this.internalField0486 = localValue1;
   }

   public void internalMethod07221(EntityTargetType localValue1) {
      this.internalMethod02246(new EntityTargetType[]{localValue1});
   }

   public void internalMethod07634(PlayerTargetType localValue1) {
      this.internalMethod02197(new PlayerTargetType[]{localValue1});
   }

   private boolean internalMethod04487(Entity localValue1) {
      if (localValue1 instanceof PlayerEntity localValue2) {
         if (!this.internalMethod05593(EntityTargetType.internalField0027)) {
            return false;
         } else if (localValue2 == internalField0149.player) {
            return this.internalMethod06170(PlayerTargetType.internalField0026);
         } else if (internalMethod03529(localValue2)) {
            return this.internalMethod06170(PlayerTargetType.internalField0962);
         } else {
            return RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue2.getName().getString())
               ? this.internalMethod06170(PlayerTargetType.internalField0961)
               : this.internalMethod06170(PlayerTargetType.internalField0025);
         }
      } else if (localValue1 instanceof HostileEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0028);
      } else if (localValue1 instanceof AnimalEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0963);
      } else {
         return localValue1 instanceof ItemEntity ? this.internalMethod02927(ItemTargetType.internalField0013) : false;
      }
   }

   private static boolean internalMethod03529(PlayerEntity localValue0) {
      String localValue1 = localValue0.getName().getString();

      for (Packets.InternalType0018 localValue3 : Information.getVisiblePlayers()) {
         if (localValue3.gameInfo() != null && localValue1.equals(localValue3.gameInfo().nickname())) {
            return true;
         }
      }

      return false;
   }

   public interface InternalType0174 {
      void render(List<Entity> localValue1, Render3DEvent localValue2);
   }

   public interface InternalType0175 {
      void render(Entity localValue1, Render3DEvent localValue2);
   }
}
