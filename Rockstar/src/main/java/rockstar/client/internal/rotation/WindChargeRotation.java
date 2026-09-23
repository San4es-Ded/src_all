package rockstar.client.internal.rotation;





import rockstar.client.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.Items;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.core.StepSequence;
import rockstar.client.internal.core.DelayStep;
import rockstar.client.internal.inventory.ItemSwapAction;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationPriority;

public class WindChargeRotation
extends AbstractAssistSwapItem {
    private final StepSequence internalField0078 = new StepSequence();
    private boolean internalField0277 = false;

    public WindChargeRotation() {
        super("modules.settings.assist.wind_charge", Items.WIND_CHARGE.getDefaultStack(), AssistItemCategory.internalField1161);
    }

    @Override
    public void internalMethod03235() {
        float f = RockstarClient.getInstance().internalMethod02368().internalMethod00024().internalMethod00169();
        this.internalField0277 = true;
        this.internalField0078.internalMethod01234(new DelayStep(100L)).internalMethod01234(new ItemSwapAction(Items.WIND_CHARGE)).internalMethod01234(new DelayStep(100L)).internalMethod05210();
    }

    @Override
    public boolean internalMethod07961() {
        return true;
    }

    @Override
    public boolean internalMethod04619() {
        return this.internalField0078.internalMethod05211() || this.internalField0277;
    }

    @Override
    public void internalMethod04618() {
        if (this.internalField0277 && !this.internalField0078.internalMethod05211()) {
            this.internalField0277 = false;
        }
        if (this.internalField0277) {
            float f = RockstarClient.getInstance().internalMethod02368().internalMethod00024().internalMethod00169();
            RockstarClient.getInstance().internalMethod02368().internalMethod00418(new Rotation(f, 90.0f), RotationBehavior.internalField1003, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1012);
        }
        this.internalField0078.internalMethod05212();
    }
}

