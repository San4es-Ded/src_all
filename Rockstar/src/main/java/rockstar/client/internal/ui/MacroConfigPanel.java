package rockstar.client.internal.ui;





import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class MacroConfigPanel {
   private final List<SettingComponent<?>> internalField0416 = new ArrayList<>();
   private final ButtonSetting internalField0663;
   private final LegacyUiElement internalField0241;
   private final AssistSwapItem internalField0347;
   private boolean internalField0277 = false;

   public MacroConfigPanel(AssistSwapItem localValue1, Runnable localValue2) {
      this.internalField0347 = localValue1;
      this.internalField0241 = new LegacyUiElement() {
         @Override
         protected void internalMethod05619(UiRenderContext localValue1) {
         }
      };

      for (Setting localValue4 : localValue1.getSettings()) {
         SettingComponent localValue5 = UiUtils.internalMethod06603(localValue4, this.internalField0241);
         if (localValue5 != null) {
            this.internalField0416.add(localValue5);
         }
      }

      this.internalField0663 = new ButtonSetting(new SettingOwner() {
         private final List<Setting> internalField0416 = new ArrayList<>();

         @Override
         public List<Setting> getSettings() {
            return this.internalField0416;
         }
      }, "macro.delete").internalMethod07149(localValue2);
      SettingComponent localValue6 = UiUtils.internalMethod06603(this.internalField0663, this.internalField0241);
      if (localValue6 != null) {
         this.internalField0416.add(localValue6);
      }
   }

   public int internalMethod02557() {
      return this.internalField0347.internalMethod03234();
   }

   public void internalMethod03326(int localValue1) {
      this.internalField0347.internalMethod01910(localValue1);
   }

   public float internalMethod02556() {
      float localValue1 = 0.0F;

      for (SettingComponent localValue3 : this.internalField0416) {
         localValue1 += localValue3.internalMethod07809();
      }

      return localValue1;
   }

   public boolean internalMethod02558() {
      return this.internalField0277;
   }

   public void internalMethod03327(boolean localValue1) {
      this.internalField0277 = localValue1;
   }

   @Generated
   public List<SettingComponent<?>> internalMethod01283() {
      return this.internalField0416;
   }

   @Generated
   public ButtonSetting internalMethod03509() {
      return this.internalField0663;
   }

   @Generated
   public LegacyUiElement internalMethod01250() {
      return this.internalField0241;
   }

   @Generated
   public AssistSwapItem internalMethod02499() {
      return this.internalField0347;
   }
}
