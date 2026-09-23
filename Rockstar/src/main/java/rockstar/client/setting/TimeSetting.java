package rockstar.client.setting;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.EnumSet;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public class TimeSetting extends AbstractSetting {
   final EnumSet<TimeSetting.InternalType0178> internalField0832 = EnumSet.allOf(TimeSetting.InternalType0178.class);
   private int internalField0227 = 24;
   private int internalField0228 = 60;
   private int internalField1053;
   private ValueTransform<Integer> internalField0445 = localValue0 -> localValue0;

   public TimeSetting(@NotNull SettingOwner localValue1, String localValue2, String localValue3, @NotNull BooleanSupplier localValue4) {
      super(localValue1, localValue2, localValue4);
   }

   public TimeSetting(@NotNull SettingOwner localValue1, String localValue2, @NotNull BooleanSupplier localValue3) {
      super(localValue1, localValue2, localValue3);
   }

   public TimeSetting(@NotNull SettingOwner localValue1, String localValue2, String localValue3) {
      super(localValue1, localValue2);
   }

   public TimeSetting(@NotNull SettingOwner localValue1, String localValue2) {
      super(localValue1, localValue2);
   }

   public TimeSetting internalMethod03569(TimeSetting.InternalType0178... localValue1) {
      this.internalField0832.clear();
      if (localValue1 != null) {
         for (TimeSetting.InternalType0178 localValue5 : localValue1) {
            if (localValue5 != null) {
               this.internalField0832.add(localValue5);
            }
         }
      }

      if (this.internalField0832.isEmpty()) {
         this.internalField0832.add(TimeSetting.InternalType0178.internalField1313);
      }

      this.internalMethod04593(this.internalField1053);
      return this;
   }

   public TimeSetting internalMethod04919(boolean localValue1) {
      return this.internalMethod01960(TimeSetting.InternalType0178.internalField0779, localValue1);
   }

   public TimeSetting internalMethod01760(boolean localValue1) {
      return this.internalMethod01960(TimeSetting.InternalType0178.internalField0780, localValue1);
   }

   public TimeSetting internalMethod08823(boolean localValue1) {
      return this.internalMethod01960(TimeSetting.InternalType0178.internalField1313, localValue1);
   }

   private TimeSetting internalMethod01960(TimeSetting.InternalType0178 localValue1, boolean localValue2) {
      if (localValue2) {
         this.internalField0832.add(localValue1);
      } else if (this.internalField0832.size() > 1) {
         this.internalField0832.remove(localValue1);
      }

      this.internalMethod04593(this.internalField1053);
      return this;
   }

   public TimeSetting internalMethod06907(int localValue1) {
      this.internalField0227 = Math.max(1, localValue1);
      this.internalMethod04593(this.internalField1053);
      return this;
   }

   public TimeSetting internalMethod03793(int localValue1) {
      this.internalField0228 = Math.max(1, Math.min(60, localValue1));
      this.internalMethod04593(this.internalField1053);
      return this;
   }

   public TimeSetting internalMethod08384(int localValue1) {
      this.internalMethod04593(localValue1);
      return this;
   }

   public TimeSetting internalMethod02845(ValueTransform<Integer> localValue1) {
      if (localValue1 != null) {
         this.internalField0445 = localValue1;
      }

      return this;
   }

   public void internalMethod04593(int localValue1) {
      int localValue2 = this.internalMethod04592(this.internalField0445.changed(Math.max(0, localValue1)));
      if (this.internalField1053 != localValue2) {
         this.notifyChanged();
         this.internalField1053 = localValue2;
      }
   }

   public boolean internalMethod06950(TimeSetting.InternalType0178 localValue1) {
      return this.internalField0832.contains(localValue1);
   }

   public int internalMethod02081() {
      return this.internalMethod06950(TimeSetting.InternalType0178.internalField0779) ? this.internalField1053 / 3600 % this.internalField0227 : 0;
   }

   public int internalMethod02085() {
      return this.internalMethod06950(TimeSetting.InternalType0178.internalField0780) ? this.internalField1053 / 60 % this.internalField0228 : 0;
   }

   public int internalMethod08552() {
      return this.internalMethod06950(TimeSetting.InternalType0178.internalField1313) ? this.internalField1053 % 60 : 0;
   }

   public void internalMethod04662(int localValue1) {
      this.internalMethod04593(Math.floorMod(localValue1, this.internalField0227) * 3600 + this.internalMethod02085() * 60 + this.internalMethod08552());
   }

   public void internalMethod07861(int localValue1) {
      this.internalMethod04593(this.internalMethod02081() * 3600 + Math.floorMod(localValue1, this.internalField0228) * 60 + this.internalMethod08552());
   }

   public void internalMethod07872(int localValue1) {
      this.internalMethod04593(this.internalMethod02081() * 3600 + this.internalMethod02085() * 60 + Math.floorMod(localValue1, 60));
   }

   public long internalMethod02082() {
      return this.internalField1053 * 1000L;
   }

   public int internalMethod08553() {
      return this.internalField1053 * 20;
   }

   public String internalMethod08164() {
      StringBuilder localValue1 = new StringBuilder();

      for (TimeSetting.InternalType0178 localValue3 : this.internalField0832) {
         int localValue4 = switch (localValue3) {
            case internalField0779 -> this.internalMethod02081();
            case internalField0780 -> this.internalMethod02085();
            case internalField1313 -> this.internalMethod08552();
         };
         if (localValue1.isEmpty()) {
            localValue1.append(localValue4);
         } else {
            localValue1.append(':').append(localValue4 < 10 ? "0" + localValue4 : String.valueOf(localValue4));
         }
      }

      return localValue1.toString();
   }

   private int internalMethod04592(int localValue1) {
      localValue1 = Math.max(0, localValue1);
      int localValue2 = this.internalMethod06950(TimeSetting.InternalType0178.internalField0779) ? localValue1 / 3600 % this.internalField0227 : 0;
      int localValue3 = this.internalMethod06950(TimeSetting.InternalType0178.internalField0780) ? localValue1 / 60 % this.internalField0228 : 0;
      int localValue4 = this.internalMethod06950(TimeSetting.InternalType0178.internalField1313) ? localValue1 % 60 : 0;
      return localValue2 * 3600 + localValue3 * 60 + localValue4;
   }

   @Override
   public JsonElement toJson() {
      return new JsonPrimitive(this.internalField1053);
   }

   @Override
   public void fromJson(JsonElement localValue1) {
      if (localValue1 != null && localValue1.isJsonPrimitive() && localValue1.getAsJsonPrimitive().isNumber()) {
         double localValue2 = localValue1.getAsDouble();
         if (Double.isFinite(localValue2) && localValue2 >= 0.0 && localValue2 <= 2.147483647E9) {
            this.internalMethod04593((int)localValue2);
         }
      }
   }

   @Override
   public boolean isValidJson(JsonElement localValue1) {
      if (localValue1 != null && localValue1.isJsonPrimitive() && localValue1.getAsJsonPrimitive().isNumber()) {
         double localValue2 = localValue1.getAsDouble();
         return Double.isFinite(localValue2) && localValue2 >= 0.0 && localValue2 <= 2.147483647E9;
      } else {
         return false;
      }
   }

   @Override
   public UiContainer createComponent() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod03907(
            new TextLabel(Fonts.internalField1154.internalMethod01432(8.0F), () -> LanguageManager.internalMethod07214(this.internalField0248))
               .internalMethod02959(localValue0 -> ThemeColors.internalField1613.mulAlpha(0.75F + 0.25F * localValue0.hover()))
               .internalMethod02902()
               .fill()
         )
         .internalMethod03907(
            new TextLabel(Fonts.internalField1154.internalMethod01432(7.0F), this::internalMethod08164)
               .internalMethod02959(localValue0 -> ThemeColors.internalField1310)
         )
         .internalMethod03062(6.0F)
         .internalMethod01192(FlexDirection.internalField1246)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03514(Insets.internalMethod00105(6.0F, 0.0F, 0.0F, 0.0F))
         .internalMethod09609();
      NumberWheelPicker localValue2 = new NumberWheelPicker() {
         private final EnumSet<TimeSetting.InternalType0178> internalField0832 = EnumSet.noneOf(TimeSetting.InternalType0178.class);

         {
            this.internalMethod06971();
         }

         private void internalMethod06971() {
            this.internalField0832.clear();
            this.internalField0832.addAll(TimeSetting.this.internalField0832);
            TimeSetting.this.internalMethod06674(this);
         }

         @Override
         public void onTick(float localValue1, float localValue2x, float localValue3) {
            if (!this.internalField0832.equals(TimeSetting.this.internalField0832)) {
               this.internalMethod06971();
            }

            super.onTick(localValue1, localValue2x, localValue3);
         }
      };
      return new UiContainer()
         .internalMethod01192(FlexDirection.internalField0629)
         .internalMethod03062(5.0F)
         .internalMethod03907(localValue1)
         .internalMethod03907(
            new UiContainer()
               .internalMethod01192(FlexDirection.internalField0629)
               .internalMethod09609()
               .internalMethod03514(Insets.internalMethod00105(0.0F, 0.0F, 4.0F, 0.0F))
               .internalMethod03907(localValue2.internalMethod03157(12.5F).internalMethod01310(5).internalMethod07555())
         );
   }

   void internalMethod06674(NumberWheelPicker localValue1) {
      localValue1.internalMethod08280();
      if (this.internalMethod06950(TimeSetting.InternalType0178.internalField0779)) {
         localValue1.internalMethod05476(
            this.internalField0227, this::internalMethod02081, this::internalMethod04662, TimeSetting.InternalType0178.internalField0779::internalMethod06929
         );
      }

      if (this.internalMethod06950(TimeSetting.InternalType0178.internalField0780)) {
         localValue1.internalMethod05476(
            this.internalField0228, this::internalMethod02085, this::internalMethod07861, TimeSetting.InternalType0178.internalField0780::internalMethod06929
         );
      }

      if (this.internalMethod06950(TimeSetting.InternalType0178.internalField1313)) {
         localValue1.internalMethod05476(60, this::internalMethod08552, this::internalMethod07872, TimeSetting.InternalType0178.internalField1313::internalMethod06929);
      }
   }

   @Generated
   public EnumSet<TimeSetting.InternalType0178> internalMethod01646() {
      return this.internalField0832;
   }

   @Generated
   public int internalMethod08562() {
      return this.internalField0227;
   }

   @Generated
   public int internalMethod08563() {
      return this.internalField0228;
   }

   @Generated
   public int internalMethod09221() {
      return this.internalField1053;
   }

   @Generated
   public ValueTransform<Integer> internalMethod07621() {
      return this.internalField0445;
   }

   public static enum InternalType0178 {
      internalField0779(3600, "time.unit.hours"),
      internalField0780(60, "time.unit.minutes"),
      internalField1313(1, "time.unit.seconds");

      private final int internalField0227;
      private final String internalField0248;

      private InternalType0178(int localValue3, String localValue4) {
         this.internalField0227 = localValue3;
         this.internalField0248 = localValue4;
      }

      public int internalMethod00144() {
         return this.internalField0227;
      }

      public String internalMethod06929() {
         return LanguageManager.internalMethod07214(this.internalField0248);
      }
   }
}
