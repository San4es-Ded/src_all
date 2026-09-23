package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.Animations;
import aethereal.render.ColorUtil;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.class_266;
import net.minecraft.class_269;
import net.minecraft.class_332;
import net.minecraft.class_355;
import net.minecraft.class_640;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({class_355.class})
public abstract class PlayerListHudMixin {
   @ModifyArgs(
      method = {"method_1919"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_332;method_25294(IIIII)V",
         ordinal = 2
      ),
      require = 0
   )
   private void render(Args args, @Local(name = {"list"}) List<class_640> list, @Local(name = {"w"}) int w) {
      if (Interface.aM_.field_1724 != null && w < list.size() && Interface.aM_.field_1724.method_5667().equals(list.get(w).method_2966().getId())) {
         args.set(4, ColorUtil.a(60, 140, 255, 128));
      }
   }

   @Inject(
      method = {"method_1921"},
      at = {@At("HEAD")}
   )
   private void setVisible(boolean visible, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("TAB").c()) {
         animations.r().a(visible);
      }
   }

   @Inject(
      method = {"method_1919"},
      at = {@At("HEAD")}
   )
   private void headRender(class_332 context, int scaledWindowWidth, class_269 scoreboard, @Nullable class_266 objective, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("TAB").c()) {
         context.method_51448().method_22903();
         context.method_51448().method_46416(0.0F, -200.0F * (1.0F - animations.r().c()), 0.0F);
      }
   }

   @Inject(
      method = {"method_1919"},
      at = {@At("RETURN")}
   )
   private void render(class_332 context, int scaledWindowWidth, class_269 scoreboard, @Nullable class_266 objective, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("TAB").c()) {
         context.method_51448().method_22909();
      }
   }
}
