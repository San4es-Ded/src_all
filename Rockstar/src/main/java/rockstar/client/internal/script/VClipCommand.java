package rockstar.client.internal.script;





import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.core.OperationResult;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.MinecraftClientAccess;

public class VClipCommand
implements MinecraftClientAccess {
    public CommandNode internalMethod05383() {
        return CommandBuilder.internalMethod07482("vclip", typedValue125 -> typedValue125.internalMethod05325("v", "verticalclip").internalMethod06148("commands.vclip.description").internalMethod01539("distance", typedValue130 -> typedValue130.internalMethod00776(string -> {
            try {
                return OperationResult.internalMethod00116(Double.parseDouble(string));
            }
            catch (NumberFormatException numberFormatException) {
                return OperationResult.internalMethod05941(LanguageManager.internalMethod07214("commands.vclip.invalid"));
            }
        })).internalMethod00262(this::internalMethod05626)).internalMethod04146();
    }

    private void internalMethod05626(ParsedCommand typedValue127) {
        double d = (Double)typedValue127.internalMethod02266().getFirst();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Vec3d vec3d = minecraftClient.player.getEntityPos();
        minecraftClient.player.setPosition(vec3d.add(0.0, d, 0.0));
    }
}
