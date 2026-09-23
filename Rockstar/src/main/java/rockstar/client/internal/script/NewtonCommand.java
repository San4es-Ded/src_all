package rockstar.client.internal.script;






import rockstar.client.rotation.*;
import rockstar.client.command.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.Optional;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.internal.core.NewtonGlobalState;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.core.AuraAimService;
import rockstar.client.internal.script.ClearAreaCommand;
import rockstar.client.internal.script.FillCommand;
import rockstar.client.internal.script.GotoCommand;
import rockstar.client.internal.script.MineCommand;
import rockstar.client.internal.script.SelectionCommand;
import rockstar.client.internal.core.ChatMessageHelper;
import rockstar.client.internal.core.NewtonTask;

public final class NewtonCommand {
    public CommandNode internalMethod03956() {
        return CommandBuilder.internalMethod07482("newton", typedValue125 -> typedValue125.internalMethod06148("commands.newton.description").internalMethod05325("nt").internalMethod04260(new GotoCommand().internalMethod06327(), new MineCommand().internalMethod07072(), new SelectionCommand().internalMethod07275(), new ClearAreaCommand().internalMethod06163(), new FillCommand().internalMethod01405(), this.internalMethod05361(), this.internalMethod07875(), this.internalMethod08103(), this.internalMethod07989(), this.internalMethod08241()).internalMethod00262(this::internalMethod04052)).internalMethod04146();
    }

    private CommandNode internalMethod05361() {
        return CommandBuilder.internalMethod07482("stop", typedValue125 -> typedValue125.internalMethod06148("commands.newton.stop").internalMethod05325("cancel", "\u0441\u0442\u043e\u043f").internalMethod00262(typedValue127 -> {
            NewtonCoreManager typedValue289 = NewtonCoreManager.internalMethod00114();
            boolean bl = typedValue289.internalMethod06401().internalMethod03477();
            typedValue289.internalMethod06401().internalMethod03476();
            typedValue289.internalMethod00183().internalMethod01287();
            ChatMessageHelper.internalMethod00196(bl ? "\u041f\u0440\u043e\u0446\u0435\u0441\u0441 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d" : "\u041d\u0435\u0442 \u0430\u043a\u0442\u0438\u0432\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u0430");
        })).internalMethod04146();
    }

    private CommandNode internalMethod07875() {
        return CommandBuilder.internalMethod07482("pause", typedValue125 -> typedValue125.internalMethod06148("commands.newton.pause").internalMethod05325("\u043f\u0430\u0443\u0437\u0430").internalMethod00262(typedValue127 -> NewtonCommand.internalMethod00885().ifPresentOrElse(typedValue308 -> {
            typedValue308.internalMethod04089();
            ChatMessageHelper.internalMethod00196("\u041f\u0430\u0443\u0437\u0430");
        }, () -> ChatMessageHelper.internalMethod06835("\u041d\u0435\u0442 \u0430\u043a\u0442\u0438\u0432\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u0430")))).internalMethod04146();
    }

    private CommandNode internalMethod08103() {
        return CommandBuilder.internalMethod07482("resume", typedValue125 -> typedValue125.internalMethod06148("commands.newton.resume").internalMethod05325("\u043f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u044c").internalMethod00262(typedValue127 -> NewtonCommand.internalMethod00885().ifPresentOrElse(typedValue308 -> {
            typedValue308.internalMethod08146();
            ChatMessageHelper.internalMethod00196("\u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0430\u0435\u043c");
        }, () -> ChatMessageHelper.internalMethod06835("\u041d\u0435\u0442 \u0430\u043a\u0442\u0438\u0432\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u0430")))).internalMethod04146();
    }

    private CommandNode internalMethod07989() {
        return CommandBuilder.internalMethod07482("status", typedValue125 -> typedValue125.internalMethod06148("commands.newton.status").internalMethod05325("info", "\u0441\u0442\u0430\u0442\u0443\u0441").internalMethod00262(typedValue127 -> NewtonCommand.internalMethod00885().ifPresentOrElse(typedValue308 -> ChatMessageHelper.internalMethod00196(typedValue308.internalMethod01129() + ": " + typedValue308.internalMethod05788()), () -> ChatMessageHelper.internalMethod06835("\u041d\u0435\u0442 \u0430\u043a\u0442\u0438\u0432\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u0430")))).internalMethod04146();
    }

    private CommandNode internalMethod08241() {
        return CommandBuilder.internalMethod07482("neuro", typedValue125 -> typedValue125.internalMethod06148("commands.newton.neuro").internalMethod05325("\u043d\u0435\u0439\u0440\u043e").internalMethod00262(typedValue127 -> {
            if (NewtonGlobalState.internalField1100) {
                NewtonGlobalState.internalField1100 = false;
                ChatMessageHelper.internalMethod00196("\u041d\u0435\u0439\u0440\u043e-\u0440\u043e\u0442\u0430\u0446\u0438\u044f \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u0430 \u2014 \u043f\u043e\u0432\u043e\u0440\u043e\u0442\u044b \u043b\u0438\u043d\u0435\u0439\u043d\u044b\u0435");
                return;
            }
            if (!AuraAimService.internalMethod05078()) {
                ChatMessageHelper.internalMethod06835("\u041c\u043e\u0434\u0435\u043b\u044c \u043d\u0435 \u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u0430 \u2014 \u0441\u043d\u0430\u0447\u0430\u043b\u0430 .neuro load <\u0438\u043c\u044f> (\u0438\u043b\u0438 .neuro train)");
                return;
            }
            NewtonGlobalState.internalField1100 = true;
            ChatMessageHelper.internalMethod00196("\u041d\u0435\u0439\u0440\u043e-\u0440\u043e\u0442\u0430\u0446\u0438\u044f \u0432\u043a\u043b\u044e\u0447\u0435\u043d\u0430 \u2014 \u043c\u043e\u0434\u0435\u043b\u044c \u00ab" + AuraAimService.internalMethod06837() + "\u00bb");
        })).internalMethod04146();
    }

    private static Optional<NewtonTask> internalMethod00885() {
        return NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03684();
    }

    private void internalMethod04052(ParsedCommand typedValue127) {
        ChatMessageHelper.internalMethod00196("Newton \u2014 \u043a\u043e\u043c\u0430\u043d\u0434\u044b:");
        ChatMessageHelper.internalMethod00196(" .newton goto <x> [y] <z> [elytra] \u2014 \u0438\u0434\u0442\u0438 \u043a \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0430\u043c");
        ChatMessageHelper.internalMethod00196(" .newton mine <block> \u2014 \u043a\u043e\u043f\u0430\u0442\u044c \u0431\u043b\u043e\u043a\u0438 \u044d\u0442\u043e\u0433\u043e \u0442\u0438\u043f\u0430");
        ChatMessageHelper.internalMethod00196(" .newton sel \u2014 \u0432\u044b\u0434\u0435\u043b\u0438\u0442\u044c \u0443\u0433\u043e\u043b \u043e\u0431\u043b\u0430\u0441\u0442\u0438 (\u0441\u043c\u043e\u0442\u0440\u044f \u043d\u0430 \u0431\u043b\u043e\u043a), 2 \u0440\u0430\u0437\u0430");
        ChatMessageHelper.internalMethod00196(" .newton cleararea [block|stop] \u2014 \u0440\u0430\u0441\u043a\u043e\u043f\u0430\u0442\u044c \u0432\u044b\u0434\u0435\u043b\u0435\u043d\u043d\u0443\u044e \u043e\u0431\u043b\u0430\u0441\u0442\u044c");
        ChatMessageHelper.internalMethod00196(" .newton fill <block|stop> \u2014 \u0437\u0430\u043f\u043e\u043b\u043d\u0438\u0442\u044c \u0432\u044b\u0434\u0435\u043b\u0435\u043d\u043d\u0443\u044e \u043e\u0431\u043b\u0430\u0441\u0442\u044c");
        ChatMessageHelper.internalMethod00196(" .newton stop / pause / resume / status \u2014 \u0443\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u043e\u043c");
        ChatMessageHelper.internalMethod00196(" .newton neuro \u2014 \u0432\u043a\u043b/\u0432\u044b\u043a\u043b \u043d\u0435\u0439\u0440\u043e-\u0440\u043e\u0442\u0430\u0446\u0438\u044e (\u043d\u0443\u0436\u043d\u0430 \u043c\u043e\u0434\u0435\u043b\u044c \u0438\u0437 .neuro load)");
    }
}

