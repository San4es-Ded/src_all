package rockstar.modules.player;





import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.module.*;
import rockstar.client.*;

import lombok.Generated;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.server.ServerUtils;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Inventory Utils", category=ModuleCategory.PLAYER)
public class InventoryUtilsModule
extends Module {
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting internalField0674;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private MultiSelectSetting.InternalType0091 internalField1491;
    private MultiSelectSetting.InternalType0091 internalField1488;
    private MultiSelectSetting.InternalType0091 internalField1490;
    private MultiSelectSetting.InternalType0091 internalField1489;
    private MultiSelectSetting.InternalType0091 internalField1493;
    private BooleanSetting internalField0650;
    private SliderSetting internalField0383;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final Stopwatch internalField0518 = new Stopwatch();
    private float internalField0205 = -1.0f;
    private boolean internalField0277;

    public InventoryUtilsModule() {
        this.internalMethod09759();
    }

    private void internalMethod09759() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.inv_utils.targets").internalMethod03035(1);
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.inv_utils.item_scroller").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.inv_utils.slot_lock").select();
        this.internalField0674 = new MultiSelectSetting((SettingOwner)this, "modules.settings.slot_lock.lock", () -> !this.internalField0244.isSelected()).internalMethod03035(1);
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot1").select();
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot2");
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot3");
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot4");
        this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot5");
        this.internalField1488 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot6");
        this.internalField1490 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot7");
        this.internalField1489 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot8");
        this.internalField1493 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.slot_lock.lock.slot9");
        this.internalField0650 = new BooleanSetting(this, "modules.settings.slot_lock.only_pvp", "modules.settings.slot_lock.only_pvp.desc", () -> !this.internalField0244.isSelected()).internalMethod04836(false);
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.inv_utils.delay", () -> !this.internalField0245.isSelected()).internalMethod08074(0.0f).internalMethod02732(250.0f).internalMethod05900(0.0f).internalMethod08673(1.0f).internalMethod06240(" ms");
    }

    public boolean internalMethod06820(int n) {
        MultiSelectSetting.InternalType0091[] nestedValue2016 = new MultiSelectSetting.InternalType0091[]{this.internalField1075, this.internalField1074, this.internalField1073, this.internalField1072, this.internalField1491, this.internalField1488, this.internalField1490, this.internalField1489, this.internalField1493};
        if (this.internalField0650.internalMethod04496() && !ServerUtils.internalField0277) {
            return false;
        }
        return n >= 0 && n < nestedValue2016.length && nestedValue2016[n].isSelected() && this.isEnabled();
    }

    @Generated
    public MultiSelectSetting internalMethod05021() {
        return this.internalField0675;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod00653() {
        return this.internalField0245;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod03702() {
        return this.internalField0244;
    }

    @Generated
    public MultiSelectSetting internalMethod05671() {
        return this.internalField0674;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod07998() {
        return this.internalField1075;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08042() {
        return this.internalField1074;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09121() {
        return this.internalField1073;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08846() {
        return this.internalField1072;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09670() {
        return this.internalField1491;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09689() {
        return this.internalField1488;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09484() {
        return this.internalField1490;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09494() {
        return this.internalField1489;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09381() {
        return this.internalField1493;
    }

    @Generated
    public BooleanSetting internalMethod04963() {
        return this.internalField0650;
    }

    @Generated
    public SliderSetting internalMethod02644() {
        return this.internalField0383;
    }

    @Generated
    public Stopwatch internalMethod03861() {
        return this.internalField0519;
    }

    @Generated
    public Stopwatch internalMethod04559() {
        return this.internalField0518;
    }

    @Generated
    public float internalMethod03252() {
        return this.internalField0205;
    }

    @Generated
    public boolean internalMethod09760() {
        return this.internalField0277;
    }
}
