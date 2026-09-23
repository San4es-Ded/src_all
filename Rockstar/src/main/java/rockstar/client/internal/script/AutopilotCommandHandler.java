package rockstar.client.internal.script;









import rockstar.client.util.*;
import rockstar.client.rotation.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.core.OperationResult;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.script.ElytraFlightTask;

public class AutopilotCommandHandler {
    private static AutopilotCommandHandler internalField0830;
    private ElytraFlightTask internalField0111;
    private boolean internalField0277;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (!this.internalField0277 || this.internalField0111 == null) {
            return;
        }
        if (this.internalField0111.internalMethod08147()) {
            this.internalField0277 = false;
            this.internalField0111 = null;
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.autopilot.stopped")));
            return;
        }
        if (NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03684().orElse(null) != this.internalField0111) {
            this.internalField0277 = false;
            this.internalField0111 = null;
        }
    };

    private void internalMethod06092() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public AutopilotCommandHandler() {
        internalField0830 = this;
        this.internalMethod06092();
    }

    public void internalMethod00817(Vec3d vec3d) {
        if (vec3d == null || !NewtonCoreManager.internalMethod00010()) {
            return;
        }
        this.internalMethod06094();
        BlockPos blockPos = BlockPos.ofFloored((Position)vec3d);
        this.internalField0111 = new ElytraFlightTask(blockPos);
        this.internalField0277 = true;
        NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(this.internalField0111);
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.autopilot.start", vec3d.getX(), vec3d.getY(), vec3d.getZ())));
    }

    public CommandNode internalMethod02061() {
        return CommandBuilder.internalMethod00593("autopilot").internalMethod05325("ap", "pilot", "\u0430\u0432\u0442\u043e\u043f\u0438\u043b\u043e\u0442", "\u043f\u0438\u043b\u043e\u0442").internalMethod06148("commands.autopilot.description").internalMethod01539("x", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(AutopilotCommandHandler::internalMethod04746)).internalMethod01539("y", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(AutopilotCommandHandler::internalMethod04746)).internalMethod01539("z", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(AutopilotCommandHandler::internalMethod04746)).internalMethod00262(this::internalMethod01569).internalMethod04146();
    }

    private static OperationResult internalMethod04746(String string) {
        try {
            Double.parseDouble(string);
            return OperationResult.internalMethod00116(string);
        }
        catch (NumberFormatException numberFormatException) {
            return OperationResult.internalMethod05941(LanguageManager.internalMethod07214("commands.autopilot.invalid"));
        }
    }

    private void internalMethod01569(ParsedCommand typedValue127) {
        String string = (String)typedValue127.internalMethod02266().get(0);
        String string2 = (String)typedValue127.internalMethod02266().get(1);
        String string3 = (String)typedValue127.internalMethod02266().get(2);
        if (string == null || string2 == null || string3 == null) {
            if (this.internalField0277) {
                this.internalMethod06094();
                ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.autopilot.stopping")));
            } else {
                ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.autopilot.not_active")));
            }
            return;
        }
        try {
            this.internalMethod00817(new Vec3d(Double.parseDouble(string), Double.parseDouble(string2), Double.parseDouble(string3)));
        }
        catch (NumberFormatException numberFormatException) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.autopilot.invalid")));
        }
    }

    private void internalMethod06094() {
        ElytraFlightTask typedValue307 = this.internalField0111;
        this.internalField0111 = null;
        this.internalField0277 = false;
        if (typedValue307 != null && NewtonCoreManager.internalMethod00010() && NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03684().orElse(null) == typedValue307) {
            NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03476();
        }
    }

    @Generated
    public static AutopilotCommandHandler internalMethod05304() {
        return internalField0830;
    }
}
