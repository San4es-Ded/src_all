package platform.inject.mixin;

import aethereal.core.Westra;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_320;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_320.class})
public class SessionMixin {
   @ModifyReturnValue(
      method = {"method_1676"},
      at = {@At("RETURN")}
   )
   private String username(String original) {
      return Westra.h() != null && Westra.h().d().h().a() != null ? Westra.h().d().h().a().b() : "WestraUser";
   }
}
