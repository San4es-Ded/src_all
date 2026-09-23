package rockstar.client.internal.game;




import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.core.ServerTypeChecks;
import rockstar.client.internal.inventory.AbstractKeywordPotionSwapItem;

public class AntiFlightAssist
extends AbstractKeywordPotionSwapItem {
    public AntiFlightAssist() {
        super("modules.settings.assist.anti_flight", Items.FIREWORK_STAR, AssistItemCategory.internalField0460, "\u0430\u043d\u0442\u0438", "\u043f\u043e\u043b\u0435\u0442");
    }

    @Override
    public boolean internalMethod03236() {
        return ServerTypeChecks.internalMethod08904();
    }
}

