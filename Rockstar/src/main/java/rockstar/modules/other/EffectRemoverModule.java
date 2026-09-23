package rockstar.modules.other;




import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.entity.effect.StatusEffects;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;

@ModuleInfo(name="Effect Remover", category=ModuleCategory.OTHER)
public class EffectRemoverModule
extends Module {
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (EffectRemoverModule.internalField0149.player == null) {
            return;
        }
        if (this.internalField0245.isSelected()) {
            EffectRemoverModule.internalField0149.player.removeStatusEffect(StatusEffects.LEVITATION);
        }
        if (this.internalField0244.isSelected()) {
            EffectRemoverModule.internalField0149.player.removeStatusEffect(StatusEffects.JUMP_BOOST);
        }
        if (this.internalField1075.isSelected()) {
            EffectRemoverModule.internalField0149.player.removeStatusEffect(StatusEffects.SLOW_FALLING);
        }
    };

    public EffectRemoverModule() {
        this.internalMethod09553();
    }

    private void internalMethod09553() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.effect_remover.remove");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.effect_remover.remove.levitation").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.effect_remover.remove.jump_boost").select();
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.effect_remover.remove.slow_fall").select();
    }
}
