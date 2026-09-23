package rockstar.client.internal.game;




import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;

public class SnowAssist
extends AbstractAssistSwapItem {
    public SnowAssist() {
        super("modules.settings.assist.snow", Items.SNOWBALL.getDefaultStack(), AssistItemCategory.internalField0460);
    }

    @Override
    public boolean internalMethod03236() {
        return true;
    }
}

