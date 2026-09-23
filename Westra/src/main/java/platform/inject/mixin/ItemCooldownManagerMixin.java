package platform.inject.mixin;

import aethereal.mixin.IItemCooldownManager;
import java.util.stream.StreamSupport;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1796;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2960;
import net.minecraft.class_9334;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1796.class})
public abstract class ItemCooldownManagerMixin implements IItemCooldownManager {
   @Override
   public void setHealCooldown(int duration) {
      ((class_1796)this).method_7906(class_2960.method_60655("westra", "heal"), duration);
   }

   @Inject(
      method = {"method_62836"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getGroup(class_1799 stack, CallbackInfoReturnable<class_2960> cir) {
      if (stack.method_7909() == class_1802.field_8574) {
         class_1844 contents = (class_1844)stack.method_57824(class_9334.field_49651);
         boolean heal = contents != null
            && StreamSupport.<class_1293>stream(contents.method_57397().spliterator(), false).anyMatch(effect -> effect.method_5579() == class_1294.field_5915);
         if (heal) {
            cir.setReturnValue(class_2960.method_60655("westra", "heal"));
         }
      }
   }
}
