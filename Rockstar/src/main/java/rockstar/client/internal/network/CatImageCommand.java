package rockstar.client.internal.network;






import rockstar.client.ui.*;
import rockstar.client.event.*;
import rockstar.client.command.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import pyrock.events.render.PreHudRenderEvent;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.network.WebImageUtils;
import rockstar.client.MinecraftClientAccess;

public class CatImageCommand
implements MinecraftClientAccess {
    private final AnimatedValue internalField0808 = new AnimatedValue(1000L, Easing.internalField1814);
    private boolean internalField0277 = false;
    private Identifier internalField0354 = null;
    private boolean internalField0276 = false;
    private final EventListener<PreHudRenderEvent> internalField0157 = preHudRenderEvent -> {
        if (this.internalField0354 == null) {
            return;
        }
        if ((double)this.internalField0808.internalMethod02881() == 1.0 && !this.internalField0277) {
            this.internalField0277 = true;
        }
        this.internalField0808.internalMethod07059(this.internalField0277 ? 0.0f : 1.0f);
        if (this.internalField0808.internalMethod02881() == 0.0f && this.internalField0277) {
            return;
        }
        float f = 200.0f;
        float f2 = ((float)internalField0149.getWindow().getScaledWidth() - f) / 2.0f;
        float f3 = ((float)internalField0149.getWindow().getScaledHeight() - f) / 2.0f;
        preHudRenderEvent.getContext().drawTexture(this.internalField0354, f2, f3, f, f, ThemeColors.internalField1312.withAlpha(255.0f * this.internalField0808.internalMethod02881()));
    };

    public CatImageCommand() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public CommandNode internalMethod01374() {
        return CommandBuilder.internalMethod07482("cat", typedValue125 -> typedValue125.internalMethod05325("kitty").internalMethod06148("commands.cat.description").internalMethod00262(typedValue127 -> this.internalMethod02436())).internalMethod04146();
    }

    private void internalMethod02436() {
        if (this.internalField0276) {
            return;
        }
        this.internalField0276 = true;
        CompletableFuture.supplyAsync(() -> {
            try {
                String string = WebImageUtils.internalMethod03682("https://api.thecatapi.com/v1/images/search");
                String string2 = WebImageUtils.internalMethod04770(string);
                if (string2 == null) {
                    return null;
                }
                BufferedImage bufferedImage = ImageIO.read(URI.create(string2).toURL());
                if (bufferedImage == null) {
                    return null;
                }
                return WebImageUtils.internalMethod04372(bufferedImage, false);
            }
            catch (IOException iOException) {
                return null;
            }
        }).thenAccept(nativeImage -> internalField0149.execute(() -> {
            if (nativeImage != null) {
                if (this.internalField0354 != null) {
                    internalField0149.getTextureManager().destroyTexture(this.internalField0354);
                }
                Identifier identifier = RockstarClient.id("temp/cat/" + String.valueOf(UUID.randomUUID()));
                internalField0149.getTextureManager().registerTexture(identifier, (AbstractTexture)new NativeImageBackedTexture(() -> "Rockstar network image", nativeImage));
                this.internalField0354 = identifier;
                this.internalField0808.internalMethod07059(1.0f);
                this.internalField0277 = false;
            }
            this.internalField0276 = false;
        }));
    }
}
