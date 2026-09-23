package platform.inject.mixin;

import aethereal.config.ThemeInfo;
import aethereal.core.EventManager;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.render.ScaleUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_408.class})
public abstract class ChatScreenMixin {
   @Shadow
   protected class_342 field_2382;
   private static final Pattern WESTRA_NUMBER = Pattern.compile("[0-9]{4,}");

   @Inject(
      method = {"method_25394"},
      at = {@At("TAIL")}
   )
   private void westraNumberHint(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (!EventManager.d() && this.field_2382 != null) {
         String hint = westraFormat(this.field_2382.method_1882());
         if (hint != null) {
            ScaleUtil.a(context, 2);
            class_4587 matrices = context.method_51448();
            float size = 7.0F;
            float width = Fonts.e.a(hint, size) + 8.0F;
            float height = 11.0F;
            float x = this.field_2382.method_46426() / 2.0F - 1.0F;
            float y = this.field_2382.method_46427() / 2.0F - height - 1.5F;
            int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
            int background = ColorUtil.a(Westra.h().d().o().a(ThemeInfo.BACKGROUND_HUD).a(), 0.85F);
            Westra.h().d().i().b(matrices, x, y, width, height, 3.0F, background, 1.0F);
            Westra.h().d().i().a(matrices, x, y, width, height, 3.0F, 0.5F, ColorUtil.a(accent, 0.35F));
            Fonts.e.a(matrices, hint, x + 4.0F, y + (height - Fonts.e.a(size)) / 2.0F - 0.5F, size, ColorUtil.a(255, 255, 255, 255));
            ScaleUtil.a(context);
         }
      }
   }

   private static String westraFormat(String text) {
      if (text != null && !text.isEmpty()) {
         Matcher matcher = WESTRA_NUMBER.matcher(text);
         String longest = null;

         while (matcher.find()) {
            if (longest == null || matcher.group().length() > longest.length()) {
               longest = matcher.group();
            }
         }

         if (longest == null) {
            return null;
         } else {
            StringBuilder result = new StringBuilder();
            int count = 0;

            for (int index = longest.length() - 1; index >= 0; index--) {
               result.append(longest.charAt(index));
               if (++count % 3 == 0 && index > 0) {
                  result.append(' ');
               }
            }

            return result.reverse().toString();
         }
      } else {
         return null;
      }
   }
}
