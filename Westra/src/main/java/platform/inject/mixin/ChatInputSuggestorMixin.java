package platform.inject.mixin;

import aethereal.command.CommandProcessor;
import aethereal.core.EventManager;
import aethereal.core.Westra;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import net.minecraft.class_342;
import net.minecraft.class_4717;
import net.minecraft.class_4717.class_464;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({class_4717.class})
public abstract class ChatInputSuggestorMixin {
   @Shadow
   @Final
   class_342 field_21599;
   @Shadow
   boolean field_21614;
   @Shadow
   private ParseResults<class_2172> field_21610;
   @Shadow
   private CompletableFuture<Suggestions> field_21611;
   @Shadow
   private class_464 field_21612;

   @Shadow
   protected abstract void method_23937();

   @WrapMethod(
      method = {"method_23934"}
   )
   private void refresh(Operation<Void> original) {
      try {
         original.call(new Object[0]);
      } catch (Throwable var3) {
      }
   }

   @Inject(
      method = {"method_23934"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/brigadier/StringReader;canRead()Z",
         remap = false
      )},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   public void onRefresh(CallbackInfo callbackInfo, @Local StringReader reader) {
      if (!EventManager.d()) {
         CommandProcessor commandProcessor = Westra.h().d().u();
         String prefix = commandProcessor.i();
         if (reader.canRead(prefix.length()) && reader.getString().startsWith(prefix, reader.getCursor())) {
            String text = this.field_21599.method_1882();
            int cursor = this.field_21599.method_1881();
            StringReader reading = new StringReader(text);
            reading.setCursor(prefix.length());
            this.field_21610 = commandProcessor.a().parse(reading, commandProcessor.h());
            if (cursor >= prefix.length() && (this.field_21612 == null || !this.field_21614)) {
               this.field_21611 = commandProcessor.a().getCompletionSuggestions(this.field_21610, cursor);
               this.field_21611.thenRun(() -> {
                  if (this.field_21611.isDone()) {
                     this.method_23937();
                  }
               });
            }

            callbackInfo.cancel();
         }
      }
   }
}
