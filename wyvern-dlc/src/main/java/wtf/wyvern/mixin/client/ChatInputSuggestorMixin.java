package wtf.wyvern.mixin.client;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatInputSuggestor.SuggestionWindow;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.Wyvern;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin {
   @Final
   @Shadow
   private TextFieldWidget field_21599;
   @Shadow
   private ParseResults<CommandSource> field_21610;
   @Shadow
   private CompletableFuture<Suggestions> field_21611;
   @Shadow
   private SuggestionWindow field_21612;

   @Shadow
   protected abstract void method_23937();

   /**
    * Rebuild the local parse tree on every text change. Keep the prefix inside
    * StringReader and start parsing after it so Brigadier suggestion ranges stay
    * aligned with the actual chat text (".friend add", not "friend add").
    */
   @Inject(method = "refresh", at = @At("HEAD"), cancellable = true)
   private void refreshLocalCommandSuggestions(CallbackInfo ci) {
      String text = this.field_21599.getText();
      String prefix = Wyvern.getInstance().getCommandManager().getPrefix();
      if (!text.startsWith(prefix)) {
         return;
      }

      StringReader commandReader = new StringReader(text);
      commandReader.setCursor(prefix.length());
      this.field_21610 = Wyvern.getInstance().getCommandManager().getDispatcher()
            .parse(commandReader, Wyvern.getInstance().getCommandManager().getSource());

      int commandCursor = Math.max(prefix.length(), this.field_21599.getCursor());
      commandCursor = Math.min(commandCursor, text.length());
      ParseResults<CommandSource> parse = this.field_21610;
      CompletableFuture<Suggestions> suggestions;
      String commandText = text.substring(prefix.length());
      if (commandCursor >= prefix.length() + 3
            && commandText.substring(0, 3).equalsIgnoreCase("gps")
            && commandText.substring(3).trim().isEmpty()) {
         SuggestionsBuilder builder = new SuggestionsBuilder(text, commandCursor);
         builder.suggest("x y z");
         suggestions = CompletableFuture.completedFuture(builder.build());
      } else {
         suggestions = Wyvern.getInstance().getCommandManager()
               .getDispatcher().getCompletionSuggestions(parse, commandCursor);
      }
      this.field_21611 = suggestions;
      this.field_21612 = null;
      suggestions.thenRun(() -> {
         if (this.field_21611 == suggestions) {
            this.method_23937();
         }
      });
      ci.cancel();
   }
}
