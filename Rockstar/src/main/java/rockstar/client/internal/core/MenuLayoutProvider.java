package rockstar.client.internal.core;



import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public interface MenuLayoutProvider {
   public String internalMethod03999();

   public float internalMethod04389();

   public float internalMethod04391();

   public boolean internalMethod04390();

   public float internalMethod08283();

   public float internalMethod08285();

   public List<MenuLayoutProvider.InternalType0257> internalMethod05973();

   public default float internalMethod08297() {
      return this.internalMethod04390() ? Math.max(0.0F, 1.0F - this.internalMethod04391()) : Math.min(1.0F, Math.max(0.0F, this.internalMethod04389()));
   }

   public static MenuLayoutProvider internalMethod05990() {
      MinecraftClient localValue0 = MinecraftClient.getInstance();
      if ((localValue0 == null ? null : localValue0.currentScreen) instanceof MenuLayoutProvider localValue3) {
         return localValue3;
      } else {
         WorldPanelScreen localValue2 = WorldPanelScreen.internalMethod03219();
         return (MenuLayoutProvider)(localValue2 != null ? localValue2 : ModernMenuScreen.internalMethod03027());
      }
   }

   public static final class InternalType0257 {
      private final String internalField0248;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;

      public InternalType0257(String localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
         this.internalField0248 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
      }

      @Override
      public final String toString() {
         return "InternalType0257[name=" + this.internalField0248 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", w=" + this.internalField1048 + ", h=" + this.internalField1047 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         MenuLayoutProvider.InternalType0257 other = (MenuLayoutProvider.InternalType0257) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public String internalMethod00905() {
         return this.internalField0248;
      }

      public float internalMethod01894() {
         return this.internalField0205;
      }

      public float internalMethod01897() {
         return this.internalField0206;
      }

      public float internalMethod08998() {
         return this.internalField1048;
      }

      public float internalMethod08999() {
         return this.internalField1047;
      }
   }
}
