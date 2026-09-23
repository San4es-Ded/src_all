package rockstar.client.internal.game;




import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;
import rockstar.client.internal.core.ServerTypeChecks;

public class PilbAssist
extends AbstractAssistSwapItem {
    public PilbAssist() {
        super("modules.settings.assist.pilb", Items.SUGAR.getDefaultStack(), AssistItemCategory.internalField0460);
    }

    @Override
    public boolean internalMethod03236() {
        return ServerTypeChecks.internalMethod06293();
    }
}

