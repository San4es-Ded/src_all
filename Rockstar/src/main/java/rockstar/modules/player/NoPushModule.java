package rockstar.modules.player;



import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.*;

import lombok.Generated;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;

@ModuleInfo(name="No Push", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.no_push")
public class NoPushModule
extends Module {
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;

    public NoPushModule() {
        this.internalMethod09815();
    }

    private void internalMethod09815() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.no_push.remove_from");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.no_push.remove_from.entities", "modules.settings.no_push.remove_from.entities.description").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.no_push.remove_from.fluids", "modules.settings.no_push.remove_from.fluids.description");
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.no_push.remove_from.bubble_columns", "modules.settings.no_push.remove_from.bubble_columns.description");
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.no_push.remove_from.blocks", "modules.settings.no_push.remove_from.blocks.description").select();
    }

    @Generated
    public MultiSelectSetting internalMethod04344() {
        return this.internalField0675;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod02708() {
        return this.internalField0245;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod02897() {
        return this.internalField0244;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod07982() {
        return this.internalField1075;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08017() {
        return this.internalField1074;
    }
}
