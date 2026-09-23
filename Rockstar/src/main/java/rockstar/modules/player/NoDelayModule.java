package rockstar.modules.player;



import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.*;

import lombok.Generated;
import moscow.rockstar.mixin.minecraft.client.IMinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;

@ModuleInfo(name="No Delay", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.no_delay")
public class NoDelayModule
extends Module {
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private SliderSetting internalField0383;
    private BooleanSetting internalField1261;
    private BooleanSetting internalField1263;

    public NoDelayModule() {
        this.internalMethod09403();
    }

    private void internalMethod09403() {
        this.internalField0650 = new BooleanSetting((SettingOwner)this, "modules.settings.no_delay.jump", "modules.settings.no_delay.jump.description").internalMethod06630();
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "modules.settings.no_delay.right_click", "modules.settings.no_delay.right_click.description");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.no_delay.right_click_delay", () -> !this.internalField0651.internalMethod04496()).internalMethod05900(1.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(1.0f).internalMethod06240(" ms");
        this.internalField1261 = new BooleanSetting((SettingOwner)this, "modules.settings.no_delay.exp", this.internalField0651::internalMethod04496);
        this.internalField1263 = new BooleanSetting((SettingOwner)this, "modules.settings.no_delay.potions", this.internalField0651::internalMethod04496);
    }

    @Override
    public void internalMethod08229() {
        if (this.internalField0651.internalMethod04496()) {
            IMinecraftClient iMinecraftClient = (IMinecraftClient)internalField0149;
            int n = this.internalMethod08871();
            if (iMinecraftClient.getUseCooldown() > n) {
                iMinecraftClient.setUseCooldown(n);
            }
        }
        if (this.internalField1261.internalMethod04496() && (NoDelayModule.internalField0149.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE || NoDelayModule.internalField0149.player.getOffHandStack().getItem() == Items.EXPERIENCE_BOTTLE)) {
            ((IMinecraftClient)internalField0149).setUseCooldown(0);
        }
        if (this.internalField1263.internalMethod04496() && (this.internalMethod03113(NoDelayModule.internalField0149.player.getMainHandStack().getItem()) || this.internalMethod03113(NoDelayModule.internalField0149.player.getOffHandStack().getItem()))) {
            ((IMinecraftClient)internalField0149).setUseCooldown(0);
        }
        super.internalMethod08229();
    }

    private boolean internalMethod03113(Item item) {
        return item == Items.POTION || item == Items.GLASS_BOTTLE;
    }

    public int internalMethod08871() {
        return Math.max(1, (int)Math.ceil(this.internalField0383.internalMethod08576() / 50.0f));
    }

    @Generated
    public BooleanSetting internalMethod01813() {
        return this.internalField0650;
    }

    @Generated
    public BooleanSetting internalMethod02483() {
        return this.internalField0651;
    }

    @Generated
    public SliderSetting internalMethod07651() {
        return this.internalField0383;
    }

    @Generated
    public BooleanSetting internalMethod07986() {
        return this.internalField1261;
    }

    @Generated
    public BooleanSetting internalMethod08108() {
        return this.internalField1263;
    }
}
