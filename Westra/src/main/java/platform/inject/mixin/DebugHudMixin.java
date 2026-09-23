package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_340;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_340.class})
public class DebugHudMixin implements Interface {
   @ModifyReturnValue(
      method = {"method_1835"},
      at = {@At("RETURN")}
   )
   private List<String> onGetLeftText(List<String> original) {
      return this.replaceText(original, true);
   }

   @ModifyReturnValue(
      method = {"method_1839"},
      at = {@At("RETURN")}
   )
   private List<String> onGetRightText(List<String> original) {
      return this.replaceText(original, false);
   }

   @Unique
   private List<String> replaceText(List<String> lines, boolean hasXyz) {
      if (Westra.h().d().t().h().m()) {
         class_243 rayEnd = aM_.method_1561()
            .field_4686
            .method_19326()
            .method_1019(class_243.method_1030(aM_.method_1561().field_4686.method_19329(), aM_.method_1561().field_4686.method_19330()).method_1021(20.0));
         class_3965 class_3965VarMethod_17742 = Objects.requireNonNull(aM_.field_1687)
            .method_17742(new class_3959(aM_.method_1561().field_4686.method_19326(), rayEnd, class_3960.field_17559, class_242.field_1348, aM_.field_1724));
         String cameraXYZ = String.format(
            Locale.ROOT,
            "%.3f / %.5f / %.3f",
            aM_.method_1561().field_4686.method_19326().field_1352,
            aM_.method_1561().field_4686.method_19326().field_1351,
            aM_.method_1561().field_4686.method_19326().field_1350
         );
         String blockLine = "—";
         if (class_3965VarMethod_17742 instanceof class_3965 && class_3965VarMethod_17742.method_17783() == class_240.field_1332) {
            class_2338 pos = class_3965VarMethod_17742.method_17777();
            blockLine = aM_.field_1687.method_8320(pos).method_26204().method_9518().getString()
               + " ["
               + pos.method_10263()
               + ", "
               + pos.method_10264()
               + ", "
               + pos.method_10260()
               + "]";
         }

         for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (hasXyz && line.contains("XYZ:") && line.contains(" / ")) {
               line = line.replaceFirst("XYZ:\\s*[\\d.-]+\\s*/\\s*[\\d.-]+\\s*/\\s*[\\d.-]+", "XYZ: " + cameraXYZ);
            }

            if (line.contains("Targeted Block")) {
               int prefixEnd = line.indexOf(58) + 1;
               line = line.substring(0, Math.min(prefixEnd, line.length())).trim() + " " + blockLine;
            }

            lines.set(i, line);
         }

         return lines;
      } else {
         return lines;
      }
   }
}
