package rockstar.client.internal.script;





import rockstar.client.rotation.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.InputEvent;

public final class MovementInputController {
   boolean internalField0277;
   boolean internalField0276;
   boolean internalField1099;
   boolean internalField1100;
   boolean internalField1102;
   boolean internalField1101;
   boolean internalField1516;
   boolean internalField1517;
   private boolean internalField1512;
   private final EventListener<InputEvent> internalField0157 = new EventListener<InputEvent>() {
      public void onEvent(InputEvent localValue1) {
         if (MovementInputController.this.internalField1517) {
            float localValue2 = (MovementInputController.this.internalField0277 ? 1.0F : 0.0F) + (MovementInputController.this.internalField0276 ? -1.0F : 0.0F);
            float localValue3 = (MovementInputController.this.internalField1099 ? 1.0F : 0.0F) + (MovementInputController.this.internalField1100 ? -1.0F : 0.0F);
            localValue1.setForward(localValue2);
            localValue1.setStrafe(localValue3);
            localValue1.setJump(MovementInputController.this.internalField1102);
            localValue1.setSneak(MovementInputController.this.internalField1101);
            localValue1.setSprint(MovementInputController.this.internalField1516);
         }
      }

      @Override
      public int internalMethod07175() {
         return 100;
      }
   };
   private final EventListener<ClientPlayerTickEvent> internalField0158 = localValue1 -> {
      if (this.internalField1517 && this.internalField1516 && this.internalField0277) {
         this.internalMethod08377();
      } else if (this.internalField1512) {
         this.internalMethod08379();
      }
   };

   public void internalMethod03508(boolean localValue1) {
      this.internalField0277 = localValue1;
   }

   public void internalMethod03557(boolean localValue1) {
      this.internalField0276 = localValue1;
   }

   public void internalMethod08033(boolean localValue1) {
      this.internalField1099 = localValue1;
   }

   public void internalMethod08045(boolean localValue1) {
      this.internalField1100 = localValue1;
   }

   public void internalMethod08359(boolean localValue1) {
      this.internalField1102 = localValue1;
   }

   public void internalMethod08371(boolean localValue1) {
      this.internalField1101 = localValue1;
   }

   public void internalMethod09358(boolean localValue1) {
      this.internalField1516 = localValue1;
   }

   public void internalMethod01281() {
      this.internalField1517 = true;
   }

   public void internalMethod01287() {
      this.internalField1517 = false;
      this.internalField0277 = this.internalField0276 = this.internalField1099 = this.internalField1100 = this.internalField1102 = this.internalField1101 = this.internalField1516 = false;
      this.internalMethod08379();
   }

   private void internalMethod08377() {
      if (!this.internalField1512) {
         MinecraftClient localValue1 = MinecraftClient.getInstance();
         if (localValue1.options != null && localValue1.options.sprintKey != null) {
            localValue1.options.sprintKey.setPressed(true);
            this.internalField1512 = true;
         }
      }
   }

   private void internalMethod08379() {
      if (this.internalField1512) {
         MinecraftClient localValue1 = MinecraftClient.getInstance();
         if (localValue1.options != null && localValue1.options.sprintKey != null) {
            localValue1.options.sprintKey.setPressed(false);
         }

         this.internalField1512 = false;
      }
   }

   public static MovementInputController internalMethod05429(NewtonCoreManager localValue0) {
      MovementInputController localValue1 = new MovementInputController();
      localValue0.internalMethod05035().internalMethod00647(localValue1);
      return localValue1;
   }

   @Generated
   public boolean internalMethod01282() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod01288() {
      return this.internalField0276;
   }

   @Generated
   public boolean internalMethod08378() {
      return this.internalField1099;
   }

   @Generated
   public boolean internalMethod08380() {
      return this.internalField1100;
   }

   @Generated
   public boolean internalMethod08385() {
      return this.internalField1102;
   }

   @Generated
   public boolean internalMethod08386() {
      return this.internalField1101;
   }

   @Generated
   public boolean internalMethod09307() {
      return this.internalField1516;
   }

   @Generated
   public boolean internalMethod09308() {
      return this.internalField1517;
   }
}
