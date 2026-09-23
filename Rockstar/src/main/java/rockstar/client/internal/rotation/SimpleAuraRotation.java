package rockstar.client.internal.rotation;




import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.*;
import net.minecraft.entity.LivingEntity;
import rockstar.client.setting.ModeSetting;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.internal.rotation.AuraRotationMode;

public class SimpleAuraRotation
extends AuraRotationMode {
    public SimpleAuraRotation(ModeSetting typedValue170) {
        super(typedValue170, "modules.settings.aura.simpleRotation");
    }

    @Override
    public void rotate(RotationManager typedValue269, float f, boolean bl, boolean bl2, RotationBehavior typedValue264, LivingEntity livingEntity) {
        Rotation typedValue266 = RotationUtils.internalMethod04766(livingEntity, this.aura());
        typedValue269.internalMethod00418(typedValue266, typedValue264, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1010);
    }
}
