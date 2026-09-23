package moscow.rockstar.mixin.minecraft.text;


import rockstar.client.render.*;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import rockstar.modules.other.NameProtectModule;
import rockstar.client.render.PostProcessRenderer;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={TextVisitFactory.class})
public class TextVisitFactoryMixin
implements MinecraftClientAccess {
    @ModifyArg(method={"visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"}, at=@At(value="INVOKE", target="Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal=0), index=0)
    private static String patchName(String string) {
        NameProtectModule typedValue207 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
        if (typedValue207.isEnabled() && TextVisitFactoryMixin.internalField0149.world != null && TextVisitFactoryMixin.internalField0149.player != null) {
            if (PostProcessRenderer.internalMethod07188()) {
                return string;
            }
            return typedValue207.internalMethod04954(string);
        }
        return string;
    }
}
