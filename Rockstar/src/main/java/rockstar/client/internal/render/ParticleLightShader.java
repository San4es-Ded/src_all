package rockstar.client.internal.render;


import rockstar.client.*;
import java.util.List;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class ParticleLightShader extends ShaderProgramBase {
   public static final int internalField0227 = 48;
   private GlUniform internalField0855;
   private GlUniform internalField0854;
   private final GlUniform[] internalField0139 = new GlUniform[48];
   private final GlUniform[] internalField0140 = new GlUniform[48];

   public ParticleLightShader(Identifier localValue1) {
      super(localValue1, VertexFormats.POSITION_TEXTURE_COLOR);
   }

   @Override
   public void internalMethod06856() {
      this.internalField0855 = this.internalMethod05981("InvViewProj");
      this.internalField0854 = this.internalMethod05981("Ambient");

      for (int localValue1 = 0; localValue1 < 48; localValue1++) {
         this.internalField0139[localValue1] = this.internalMethod05981("L" + localValue1 + "Pos");
         this.internalField0140[localValue1] = this.internalMethod05981("L" + localValue1 + "Col");
      }

      super.internalMethod06856();
   }

   public void internalMethod02350(Matrix4f localValue1, float localValue2, List<ParticleLightShader.InternalType0228> localValue3) {
      if (this.internalField0855 != null) {
         this.internalField0855.set(localValue1);
      }

      if (this.internalField0854 != null) {
         this.internalField0854.set(localValue2);
      }

      for (int localValue4 = 0; localValue4 < 48; localValue4++) {
         ParticleLightShader.InternalType0228 localValue5 = localValue4 < localValue3.size() ? (ParticleLightShader.InternalType0228)localValue3.get(localValue4) : null;
         if (this.internalField0139[localValue4] != null) {
            if (localValue5 == null) {
               this.internalField0139[localValue4].set(0.0F, 0.0F, 0.0F, 0.0F);
            } else {
               this.internalField0139[localValue4].set(localValue5.internalMethod00743(), localValue5.internalMethod00745(), localValue5.internalMethod08356(), localValue5.internalMethod08357());
            }
         }

         if (this.internalField0140[localValue4] != null) {
            if (localValue5 == null) {
               this.internalField0140[localValue4].set(0.0F, 0.0F, 0.0F, 0.0F);
            } else {
               this.internalField0140[localValue4].set(localValue5.internalMethod08366(), localValue5.internalMethod08367(), localValue5.internalMethod09764(), localValue5.internalMethod09765());
            }
         }
      }
   }

   public static final class InternalType0228 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;
      private final float internalField1049;
      private final float internalField1046;
      private final float internalField1456;
      private final float internalField1457;

      public InternalType0228(float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
         this.internalField1049 = localValue5;
         this.internalField1046 = localValue6;
         this.internalField1456 = localValue7;
         this.internalField1457 = localValue8;
      }

      @Override
      public final String toString() {
         return "InternalType0228[x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", z=" + this.internalField1048 + ", radius=" + this.internalField1047 + ", r=" + this.internalField1049 + ", g=" + this.internalField1046 + ", b=" + this.internalField1456 + ", intensity=" + this.internalField1457 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ParticleLightShader.InternalType0228 other = (ParticleLightShader.InternalType0228) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046)
            && java.util.Objects.equals(this.internalField1456, other.internalField1456)
            && java.util.Objects.equals(this.internalField1457, other.internalField1457);
      }

      public float internalMethod00743() {
         return this.internalField0205;
      }

      public float internalMethod00745() {
         return this.internalField0206;
      }

      public float internalMethod08356() {
         return this.internalField1048;
      }

      public float internalMethod08357() {
         return this.internalField1047;
      }

      public float internalMethod08366() {
         return this.internalField1049;
      }

      public float internalMethod08367() {
         return this.internalField1046;
      }

      public float internalMethod09764() {
         return this.internalField1456;
      }

      public float internalMethod09765() {
         return this.internalField1457;
      }
   }
}
