package rockstar.client.internal.rotation;




import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.*;
import net.minecraft.entity.LivingEntity;
import rockstar.client.setting.ModeSetting;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.RotationManager;
import rockstar.modules.combat.AuraModule;

public abstract class AuraRotationMode
extends ModeSetting.InternalType0088
implements MinecraftClientAccess {
    public AuraRotationMode(ModeSetting typedValue170, String string) {
        super(typedValue170, string);
    }

    public abstract void rotate(RotationManager localValue1, float localValue2, boolean localValue3, boolean localValue4, RotationBehavior localValue5, LivingEntity localValue6);

    public AuraModule aura() {
        return RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
    }

    public void attack() {
    }

    public void targetNull() {
    }

    public void enabled() {
    }

    public void update() {
    }

    public boolean canAttack() {
        return true;
    }
}
