package rockstar.modules.combat;




import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;

import lombok.Generated;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.internal.inventory.EntityTargetFilter;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;

@ModuleInfo(name="Hitboxes", category=ModuleCategory.COMBAT, internalMethod09633="modules.descriptions.hitboxes")
public class HitboxesModule
extends Module {
    private SliderSetting internalField0383;
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private MultiSelectSetting.InternalType0091 internalField1491;

    public HitboxesModule() {
        this.internalMethod09916();
    }

    private void internalMethod09916() {
        this.internalField0383 = new SliderSetting(this, "modules.settings.hitboxes.size").internalMethod05900(0.0f).internalMethod02732(1.0f).internalMethod08673(0.1f).internalMethod08074(0.3f);
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.hitboxes.targets");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.hitboxes.targets.players").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.hitboxes.targets.animals").select();
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.hitboxes.targets.mobs").select();
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.hitboxes.targets.invisibles").select();
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.hitboxes.targets.naked_players").select();
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "rockUsers");
        this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.hitboxes.targets.friends");
    }

    public boolean internalMethod01528(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return false;
        }
        EntityTargetFilter typedValue183 = new EntityTargetFilter.InternalType0309().internalMethod00547(this.internalField0245.isSelected()).internalMethod06455(this.internalField0244.isSelected()).internalMethod08543(this.internalField1075.isSelected()).internalMethod07990(this.internalField1074.isSelected()).internalMethod09114(this.internalField1073.isSelected()).internalMethod09525(this.internalField1491.isSelected()).internalMethod08126(this.internalField1072.isSelected()).internalMethod03528();
        if (livingEntity instanceof ClientPlayerEntity) {
            return false;
        }
        if (livingEntity.isDead()) {
            return false;
        }
        if (RockstarClient.getInstance().internalMethod06896()) {
            return false;
        }
        return typedValue183.internalMethod05417((Entity)livingEntity);
    }

    @Generated
    public SliderSetting internalMethod03348() {
        return this.internalField0383;
    }
}
