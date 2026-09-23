package ru.pulse.mixin;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.text.Style;
import net.minecraft.text.StringVisitable.Visitor;
import net.minecraft.text.StringVisitable.StyledVisitor;
import net.minecraft.text.PlainTextContent.Literal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pulse.client.MinecraftContext;
import pulse.module.ModuleRegistry;
import pulse.modules.utilities.StreamerMode;

@Mixin(Literal.class)
public class TextMixin implements MinecraftContext {
   @Shadow
   @Final
   private String string;
   @Unique
   private static final Pattern holyWorldPattern = Pattern.compile("(РЎРѕР»РѕР›Р°Р№С‚|Р”СѓРѕР›Р°Р№С‚|РўСЂРёРѕР›Р°Р№С‚|РљР»Р°РЅР›Р°Р№С‚)\\s*#(\\d{1,2})");
   @Unique
   private static final Pattern funTimePattern = Pattern.compile("РђРЅР°СЂС…РёСЏ-(\\d+)");

   @Redirect(
      require = 0,
      method = "visit",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/text/StringVisitable$Visitor;accept(Ljava/lang/String;)Ljava/util/Optional;")
   )
   private <ConfigTextDialog> Optional<ConfigTextDialog> redirectVisitor(Visitor<ConfigTextDialog> class_5245Var, String str) {
      return class_5245Var.accept(this.getProcessedText(str));
   }

   @Redirect(
      require = 0,
      method = "visit",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/text/StringVisitable$StyledVisitor;accept(Lnet/minecraft/text/Style;Ljava/lang/String;)Ljava/util/Optional;")
   )
   private <ConfigTextDialog> Optional<ConfigTextDialog> redirectStyledVisitor(StyledVisitor<ConfigTextDialog> class_5246Var, Style StyleVar, String str) {
      return class_5246Var.accept(StyleVar, this.getProcessedText(str));
   }

   @Unique
   private String getProcessedText(String str) {
      if (str == null) {
         return str;
      }

      try {
         String strHideServerNumber = str;
         if (this.shouldReplaceNick() && c.getSession() != null) {
            String username = c.getSession().getUsername();
            if (username != null && !username.isEmpty()) {
               strHideServerNumber = strHideServerNumber.replace(username, "pulse");
            }
         }

         if (this.shouldHideServerNumber()) {
            strHideServerNumber = this.hideServerNumber(strHideServerNumber);
         }

         return strHideServerNumber;
      } catch (Throwable t) {
         return str;
      }
   }

   @Unique
   private boolean shouldReplaceNick() {
      try {
         StreamerMode streamerMode = ModuleRegistry.STREAMER_MODE;
         return streamerMode != null && streamerMode.k() && streamerMode.hidePlayerName.k() && c != null && c.getSession() != null;
      } catch (Exception e) {
         return false;
      }
   }

   @Unique
   private boolean shouldHideServerNumber() {
      try {
         StreamerMode streamerMode = ModuleRegistry.STREAMER_MODE;
         if (streamerMode != null && streamerMode.k() && streamerMode.hideServerNumber.k() && c != null && c.getCurrentServerEntry() != null) {
            String lowerCase = c.getCurrentServerEntry().address.toLowerCase();
            return lowerCase.contains("holyworld") || lowerCase.contains("funtime");
         } else {
            return false;
         }
      } catch (Exception e) {
         return false;
      }
   }

   @Unique
   private String hideServerNumber(String str) {
      if (str == null) {
         return str;
      }

      Matcher matcher = holyWorldPattern.matcher(str);
      if (matcher.find()) {
         return matcher.replaceAll("pulsevisuals.pro");
      }

      Matcher matcher2 = funTimePattern.matcher(str);
      return matcher2.find() ? matcher2.replaceAll("pulsevisuals.pro") : str;
   }
}
