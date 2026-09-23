package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.Animations;
import aethereal.render.EasingList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_303;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_338;
import net.minecraft.class_5481;
import net.minecraft.class_7469;
import net.minecraft.class_7591;
import net.minecraft.class_303.class_7590;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({class_338.class})
public abstract class ChatHudMixin {
   @Shadow
   @Final
   private List<class_303> field_2061;

   @Shadow
   private void method_44813() {
   }

   @ModifyArgs(
      method = {"method_1805(Lnet/minecraft/class_332;IIIZ)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_332;method_25294(IIIII)V"
      ),
      require = 0
   )
   private void onRenderLineBackground(Args args, @Local class_7590 line) {
      if (Westra.h().d().t().as().m() && Westra.h().d().t().as().q().c() && ((Integer)args.get(4) & 16777215) == 0) {
         args.set(2, (Integer)args.get(0) + Interface.aM_.field_1772.method_30880(line.comp_896()) + 5);
      }
   }

   @WrapOperation(
      method = {"method_1805(Lnet/minecraft/class_332;IIIZ)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_332;method_35720(Lnet/minecraft/class_327;Lnet/minecraft/class_5481;III)I"
      )}
   )
   private int onRenderLineText(
      class_332 context,
      class_327 renderer,
      class_5481 text,
      int x,
      int y,
      int color,
      Operation<Integer> original,
      @Local class_7590 line,
      @Local(argsOnly = true,ordinal = 0) int currentTick
   ) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Появление сообщений").c()) {
         double t = Math.max(0.0, Math.min(1.0, (currentTick - line.comp_895() + Interface.aM_.method_61966().method_60637(false)) / 9.0));
         double progress = EasingList.p.ease((float)t);
         int alpha = (int)Math.round((color >>> 24 & 0xFF) * progress);
         context.method_51448().method_22903();
         context.method_51448().method_46416((float)(-(1.0 - progress) * 8.0), 0.0F, 0.0F);
         int result = (Integer)original.call(new Object[]{context, renderer, text, x, y, color & 16777215 | alpha << 24});
         context.method_51448().method_22909();
         return result;
      } else {
         return (Integer)original.call(new Object[]{context, renderer, text, x, y, color});
      }
   }

   @Inject(
      method = {"method_44811(Lnet/minecraft/class_2561;Lnet/minecraft/class_7469;Lnet/minecraft/class_7591;)V"},
      at = {@At("TAIL")}
   )
   private void onAddMessage(class_2561 message, class_7469 signatureData, class_7591 indicator, CallbackInfo ci) {
      if (this.field_2061.size() >= 2) {
         class_303 current = this.field_2061.get(0);
         class_303 previous = this.field_2061.get(1);
         String currentText = current.comp_893().getString();
         String previousText = previous.comp_893().getString();
         int counterIndex = previousText.lastIndexOf(" [x");
         if (counterIndex != -1 && previousText.endsWith("]")) {
            String originalText = previousText.substring(0, counterIndex);
            if (originalText.equals(currentText)) {
               this.updateMessage(current, message, Integer.parseInt(previousText.substring(counterIndex + 3, previousText.length() - 1)) + 1);
               this.field_2061.remove(1);
               this.method_44813();
            }
         } else {
            if (previousText.equals(currentText)) {
               this.updateMessage(current, message, 2);
               this.field_2061.remove(1);
               this.method_44813();
            }
         }
      }
   }

   @Unique
   private void updateMessage(class_303 lastMessage, class_2561 message, int count) {
      class_303 updatedLine = new class_303(
         lastMessage.comp_892(),
         class_2561.method_43473()
            .method_10852(message.method_27661())
            .method_10852(class_2561.method_43470(" [x" + count + "]").method_27692(class_124.field_1080)),
         lastMessage.comp_915(),
         lastMessage.comp_894()
      );
      this.field_2061.set(0, updatedLine);
   }
}
