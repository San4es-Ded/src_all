package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.data.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import pyrock.utility.render.ColorRGBA;

public class AuctionBuyListPanel {
   private final List<AuctionBuyListPanel.InternalType0282> internalField0416 = new ArrayList<>();
   private float internalField1047;
   private float internalField1049;
   private float internalField1046;
   private float internalField1456;
   private float internalField1457 = 20.0F;
   private float internalField1458 = 0.0F;
   private float internalField1459 = 0.0F;

   public boolean internalMethod05687(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
      this.internalField1047 = localValue2;
      this.internalField1049 = localValue3;
      this.internalField1046 = localValue4;
      this.internalField1456 = localValue5;
      this.internalMethod02005();
      this.internalMethod02006();
      if (this.internalField0416.isEmpty()) {
         return false;
      } else {
         this.internalMethod05686(localValue1, localValue2, localValue3, localValue4, localValue5);
         localValue1.drawText(
            Fonts.internalField1154.internalMethod01432(8.0F),
            "\u0421\u043a\u0443\u043f\u043a\u0430",
            localValue2 + 10.0F,
            localValue3 + 10.0F,
            ThemeColors.internalField1312
         );
         this.internalMethod05529(localValue1, localValue2, localValue3);
         return true;
      }
   }

   public void internalMethod02005() {
      this.internalField0416.clear();
      AuctionItem.internalMethod00895()
         .stream()
         .filter(localValue0 -> localValue0.internalMethod00723() != null && !localValue0.internalMethod00723().isEmpty())
         .forEach(
            localValue1 -> this.internalField0416
               .add(
                  new AuctionBuyListPanel.InternalType0282(
                     localValue1.internalMethod00723(),
                     localValue1.internalMethod00349(),
                     localValue1.internalMethod04343(),
                     localValue1.internalMethod00891(),
                     localValue1.internalMethod05344(),
                     localValue1.internalMethod00347()
                  )
               )
         );
   }

   private void internalMethod05686(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
      localValue1.drawBlurredRect(localValue2, localValue3, localValue4, localValue5, 45.0F, 5.0F, CornerRadii.internalMethod03908(8.0F), ColorRGBA.WHITE.withAlpha(255.0F));
      localValue1.drawSquircle(
         localValue2,
         localValue3,
         localValue4,
         localValue5,
         2.0F,
         CornerRadii.internalMethod03908(8.0F),
         ThemeColors.internalMethod07738().withAlpha(255.0F * ThemeColors.internalMethod02435().internalMethod08704())
      );
   }

   public void internalMethod05529(UiRenderContext localValue1, float localValue2, float localValue3) {
      float localValue4 = localValue2 + 10.0F;
      float localValue5 = localValue3 + 26.0F;
      float localValue6 = this.internalField1456 - 26.0F;
      int localValue7 = Math.max(1, (int)((this.internalField1046 - 20.0F) / (16.0F + this.internalField1457)));
      float localValue8 = Fonts.internalField1154.internalMethod01432(8.0F).internalMethod04890() * 2.0F + 3.0F;
      float localValue9 = (16.0F - localValue8) / 2.0F;
      ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue2, localValue5, this.internalField1046, localValue6);
      int localValue10 = 0;

      for (AuctionBuyListPanel.InternalType0282 localValue12 : this.internalField0416) {
         if (!localValue12.internalMethod05444().isEmpty()) {
            int localValue13 = localValue10 / localValue7;
            int localValue14 = localValue10 % localValue7;
            float localValue15 = localValue4 + localValue14 * (16.0F + this.internalField1457);
            float localValue16 = localValue5 + localValue13 * 24.0F - this.internalField1458;
            if (this.internalMethod06350(localValue16, localValue5, localValue6)) {
               this.internalMethod07473(localValue1, localValue12, localValue15, localValue16, localValue9);
            }

            localValue10++;
         }
      }

      ScissorStack.internalMethod07643();
   }

   private void internalMethod07473(UiRenderContext localValue1, AuctionBuyListPanel.InternalType0282 localValue2, float localValue3, float localValue4, float localValue5) {
      String localValue6 = localValue2.internalMethod06797() != null ? localValue2.internalMethod06797() : localValue2.internalMethod05444().getName().getString();
      String localValue7;
      if (localValue2.internalMethod04646() == AuctionItem.InternalType0158.internalField0592) {
         localValue7 = "\u0434\u043e " + localValue2.internalMethod03681();
      } else {
         localValue7 = "-" + (int)localValue2.internalMethod03680() + "% \u0440\u044b\u043d\u043a\u0430";
      }

      localValue1.drawItem(localValue2.internalMethod05444(), localValue3, localValue4, 1.0F);
      localValue1.drawText(Fonts.internalField1154.internalMethod01432(8.0F), localValue6, localValue3 + 20.0F, localValue4 + localValue5, ThemeColors.internalField1312);
      localValue1.drawText(
         Fonts.internalField1154.internalMethod01432(8.0F),
         localValue7,
         localValue3 + 20.0F,
         localValue4 + localValue5 + Fonts.internalField1154.internalMethod01432(8.0F).internalMethod04890() + 3.0F,
         ThemeColors.internalField1312
      );
   }

   private boolean internalMethod06350(float localValue1, float localValue2, float localValue3) {
      return localValue1 + 24.0F >= localValue2 && localValue1 <= localValue2 + localValue3;
   }

   private void internalMethod02006() {
      float localValue1 = 0.0F;

      for (AuctionBuyListPanel.InternalType0282 localValue3 : this.internalField0416) {
         if (!localValue3.internalMethod05444().isEmpty()) {
            String localValue4 = localValue3.internalMethod06797() != null ? localValue3.internalMethod06797() : localValue3.internalMethod05444().getName().getString();
            String localValue5;
            if (localValue3.internalMethod04646() == AuctionItem.InternalType0158.internalField0592) {
               localValue5 = "\u0434\u043e " + localValue3.internalMethod03681();
            } else {
               localValue5 = "-" + (int)localValue3.internalMethod03680() + "% \u0440\u044b\u043d\u043a\u0430";
            }

            float localValue6 = Fonts.internalField1154.internalMethod01432(8.0F).internalMethod00965(localValue4);
            float localValue7 = Fonts.internalField1154.internalMethod01432(8.0F).internalMethod00965(localValue5);
            localValue1 = Math.max(localValue1, Math.max(localValue6, localValue7));
         }
      }

      this.internalField1457 = 20.0F + localValue1 + 5.0F;
   }

   public float internalMethod02004() {
      if (this.internalField0416.isEmpty()) {
         return 150.0F;
      } else {
         float localValue1 = 0.0F;

         for (AuctionBuyListPanel.InternalType0282 localValue3 : this.internalField0416) {
            if (!localValue3.internalMethod05444().isEmpty()) {
               String localValue4 = localValue3.internalMethod06797() != null ? localValue3.internalMethod06797() : localValue3.internalMethod05444().getName().getString();
               String localValue5;
               if (localValue3.internalMethod04646() == AuctionItem.InternalType0158.internalField0592) {
                  localValue5 = "\u0434\u043e " + localValue3.internalMethod03681();
               } else {
                  localValue5 = "-" + (int)localValue3.internalMethod03680() + "% \u0440\u044b\u043d\u043a\u0430";
               }

               float localValue6 = Fonts.internalField1154.internalMethod01432(8.0F).internalMethod00965(localValue4);
               float localValue7 = Fonts.internalField1154.internalMethod01432(8.0F).internalMethod00965(localValue5);
               localValue1 = Math.max(localValue1, Math.max(localValue6, localValue7));
            }
         }

         float localValue8 = 36.0F + localValue1 + 5.0F;
         float localValue9 = 20.0F + localValue8;
         float localValue10 = 20.0F + localValue8 * 2.0F;
         return Math.max(localValue9, Math.min(localValue10, 300.0F));
      }
   }

   public void internalMethod06349(double localValue1, double localValue3, double localValue5) {
      if (UiUtils.internalMethod05785(this.internalField1047, this.internalField1049, this.internalField1046, this.internalField1456, localValue1, localValue3)) {
         this.internalField1458 -= (float)localValue5 * 20.0F;
         this.internalField1458 = Math.max(0.0F, Math.min(this.internalField1458, this.internalField1459));
      }
   }

   public float internalMethod02320(float localValue1) {
      int localValue2 = Math.max(1, (int)((this.internalField1046 - 20.0F) / (16.0F + this.internalField1457)));
      if (this.internalField0416.isEmpty()) {
         return Math.min(50.0F, localValue1);
      } else {
         int localValue3 = (this.internalField0416.size() + localValue2 - 1) / localValue2;
         float localValue4 = 26.0F + localValue3 * 24.0F + 10.0F;
         return Math.min(localValue4, localValue1);
      }
   }

   public void internalMethod02446(double localValue1, double localValue3, MouseButton localValue5) {
      if (localValue5 == MouseButton.internalField0101) {
         int localValue6 = Math.max(1, (int)((this.internalField1046 - 20.0F) / (16.0F + this.internalField1457)));
         float localValue7 = this.internalField1456 - 26.0F;
         int localValue8 = 0;

         for (AuctionBuyListPanel.InternalType0282 localValue10 : this.internalField0416) {
            if (!localValue10.internalMethod05444().isEmpty()) {
               int localValue11 = localValue8 / localValue6;
               int localValue12 = localValue8 % localValue6;
               float localValue13 = this.internalField1047 + 10.0F + localValue12 * (16.0F + this.internalField1457);
               float localValue14 = this.internalField1049 + 26.0F + localValue11 * 24.0F - this.internalField1458;
               if (this.internalMethod06350(localValue14, this.internalField1049 + 26.0F, localValue7)
                  && UiUtils.internalMethod05785(localValue13, localValue14, 36.0F + this.internalField1457 - 5.0F, 24.0, localValue1, localValue3)) {
                  AuctionItem.internalMethod04444(localValue10.internalMethod03287());
                  return;
               }

               localValue8++;
            }
         }
      }
   }

   @Generated
   public List<AuctionBuyListPanel.InternalType0282> internalMethod00270() {
      return this.internalField0416;
   }

   public static final class InternalType0282 {
      private final ItemStack internalField0878;
      private final long internalField0229;
      private final String internalField0248;
      private final String internalField0247;
      private final AuctionItem.InternalType0158 internalField0592;
      private final double internalField0194;

      public InternalType0282(ItemStack localValue1, long localValue2, String localValue4, String localValue5, AuctionItem.InternalType0158 localValue6, double localValue7) {
         this.internalField0878 = localValue1;
         this.internalField0229 = localValue2;
         this.internalField0248 = localValue4;
         this.internalField0247 = localValue5;
         this.internalField0592 = localValue6;
         this.internalField0194 = localValue7;
      }

      @Override
      public final String toString() {
         return "InternalType0282[item=" + this.internalField0878 + ", maxPrice=" + this.internalField0229 + ", customName=" + this.internalField0248 + ", id=" + this.internalField0247 + ", mode=" + this.internalField0592 + ", percentage=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0592);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionBuyListPanel.InternalType0282 other = (AuctionBuyListPanel.InternalType0282) localValue1;
         return java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField0592, other.internalField0592)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public ItemStack internalMethod05444() {
         return this.internalField0878;
      }

      public long internalMethod03681() {
         return this.internalField0229;
      }

      public String internalMethod06797() {
         return this.internalField0248;
      }

      public String internalMethod03287() {
         return this.internalField0247;
      }

      public AuctionItem.InternalType0158 internalMethod04646() {
         return this.internalField0592;
      }

      public double internalMethod03680() {
         return this.internalField0194;
      }
   }
}
