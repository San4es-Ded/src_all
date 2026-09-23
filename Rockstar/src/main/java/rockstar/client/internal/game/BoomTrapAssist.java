package rockstar.client.internal.game;




import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;
import rockstar.client.internal.core.ServerTypeChecks;

public class BoomTrapAssist
extends AbstractAssistSwapItem {
    public BoomTrapAssist() {
        super("modules.settings.assist.boom_trap", Items.PRISMARINE_SHARD.getDefaultStack(), AssistItemCategory.internalField0460);
    }

    @Override
    public boolean internalMethod03236() {
        return ServerTypeChecks.internalMethod08905();
    }
}

