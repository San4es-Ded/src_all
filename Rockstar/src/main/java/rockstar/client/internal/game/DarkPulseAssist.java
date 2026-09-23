package rockstar.client.internal.game;




import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.core.ServerTypeChecks;
import rockstar.client.internal.inventory.AbstractKeywordPotionSwapItem;

public class DarkPulseAssist
extends AbstractKeywordPotionSwapItem {
    public DarkPulseAssist() {
        super("modules.settings.assist.dark_pulse", Items.FIREWORK_STAR, AssistItemCategory.internalField0460, "\u0442\u0435\u043c\u043d", "\u043f\u0443\u043b\u044c\u0441");
    }

    @Override
    public boolean internalMethod03236() {
        return ServerTypeChecks.internalMethod08904();
    }
}

