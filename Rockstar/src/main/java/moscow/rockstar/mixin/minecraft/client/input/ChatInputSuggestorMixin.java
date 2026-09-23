package moscow.rockstar.mixin.minecraft.client.input;

import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.RockstarClient;

@Mixin(value={ChatInputSuggestor.class})
public abstract class ChatInputSuggestorMixin {
    @Shadow
    @Final
    TextFieldWidget textField;
    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow
    @Nullable
    private ChatInputSuggestor.SuggestionWindow window;

    @Shadow
    public abstract void show(boolean localValue1);

    @Inject(method={"refresh"}, at={@At(value="INVOKE", target="Lcom/mojang/brigadier/StringReader;canRead()Z", remap=false)}, cancellable=true)
    private void injectAutoCompletion(CallbackInfo callbackInfo) {
        String string;
        String string2 = this.textField.getText();
        if (string2.startsWith(string = RockstarClient.getInstance().internalMethod05348().internalMethod03606())) {
            this.pendingSuggestions = RockstarClient.getInstance().internalMethod05348().internalMethod05798(string2, this.textField.getCursor());
            this.pendingSuggestions.thenRun(() -> {
                try {
                    if (this.pendingSuggestions.isDone() && !this.pendingSuggestions.get().isEmpty() && this.window == null) {
                        this.show(false);
                        callbackInfo.cancel();
                    }
                }
                catch (InterruptedException | ExecutionException exception) {
                    // empty catch block
                }
            });
        }
    }
}

