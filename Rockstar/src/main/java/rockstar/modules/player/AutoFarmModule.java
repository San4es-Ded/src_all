package rockstar.modules.player;







import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;

import lombok.Generated;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import pyrock.events.window.KeyPressEvent;
import rockstar.client.internal.script.AppleFarmTask;
import rockstar.client.internal.script.AutoSellTask;
import rockstar.client.internal.script.CropFarmTask;
import rockstar.client.internal.inventory.AutoFarmModeState;
import rockstar.client.internal.script.MineFarmTask;
import rockstar.client.internal.script.MushroomFarmTask;
import rockstar.client.internal.ui.PotionCombinerMode;
import rockstar.client.internal.script.PotionFarmTask;
import rockstar.client.internal.script.SwordFarmTask;
import rockstar.client.internal.script.FarmStatsTracker;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;

@ModuleInfo(name="Auto Farm", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.auto_farm")
public class AutoFarmModule
extends Module {
    private final FarmStatsTracker internalField0849 = new FarmStatsTracker(this::internalMethod02711);
    private ModeSetting internalField0668;
    private AppleFarmTask internalField0680;
    private SwordFarmTask internalField0846;
    private PotionFarmTask internalField0699;
    private PotionCombinerMode internalField0698;
    private CropFarmTask internalField0693;
    private MushroomFarmTask internalField0697;
    private MineFarmTask internalField0696;
    private AutoSellTask internalField0692;
    private AutoFarmModeState internalField0694;
    private final EventListener<KeyPressEvent> internalField0157 = keyPressEvent -> {
        if (keyPressEvent.getKey() != 256 || keyPressEvent.getAction() != 1) {
            return;
        }
        if (!(AutoFarmModule.internalField0149.currentScreen instanceof HandledScreen) || AutoFarmModule.internalField0149.player == null) {
            return;
        }
        ScreenHandler screenHandler = AutoFarmModule.internalField0149.player.currentScreenHandler;
        if (screenHandler != null && screenHandler != AutoFarmModule.internalField0149.player.playerScreenHandler) {
            this.disable();
        }
    };

    public AutoFarmModule() {
        this.internalMethod09512();
    }

    private void internalMethod09512() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.auto_farm.mode");
        this.internalField0680 = new AppleFarmTask(this, this.internalField0668);
        this.internalField0846 = new SwordFarmTask(this, this.internalField0668);
        this.internalField0699 = new PotionFarmTask(this, this.internalField0668);
        this.internalField0698 = new PotionCombinerMode(this, this.internalField0668);
        this.internalField0693 = new CropFarmTask(this, this.internalField0668);
        this.internalField0697 = new MushroomFarmTask(this, this.internalField0668);
        this.internalField0696 = new MineFarmTask(this, this.internalField0668);
        this.internalField0692 = new AutoSellTask(this, this.internalField0668);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.internalField0694 = this.internalMethod03394();
        this.internalField0849.internalMethod01589();
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this.internalField0849);
        if (this.internalField0694 != null) {
            RockstarClient.getInstance().internalMethod03317().internalMethod00647(this.internalField0694);
            this.internalField0694.internalMethod04694();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.internalField0849.internalMethod01595();
        RockstarClient.getInstance().internalMethod03317().internalMethod07237(this.internalField0849);
        if (this.internalField0694 != null) {
            this.internalField0694.internalMethod04697();
            RockstarClient.getInstance().internalMethod03317().internalMethod07237(this.internalField0694);
            this.internalField0694 = null;
        }
    }

    @Override
    public void internalMethod08229() {
        super.internalMethod08229();
        this.internalField0849.internalMethod08497();
        AutoFarmModeState typedValue313 = this.internalMethod03394();
        if (typedValue313 != this.internalField0694) {
            if (this.internalField0694 != null) {
                this.internalField0694.internalMethod04697();
                RockstarClient.getInstance().internalMethod03317().internalMethod07237(this.internalField0694);
            }
            this.internalField0694 = typedValue313;
            if (this.internalField0694 != null) {
                RockstarClient.getInstance().internalMethod03317().internalMethod00647(this.internalField0694);
                this.internalField0694.internalMethod04694();
            }
        }
        if (this.internalField0694 != null) {
            this.internalField0694.internalMethod08382();
        }
    }

    private AutoFarmModeState internalMethod03394() {
        AutoFarmModeState typedValue313;
        ModeSetting.InternalType0088 nestedValue2011 = this.internalField0668.internalMethod07418();
        return nestedValue2011 instanceof AutoFarmModeState ? (typedValue313 = (AutoFarmModeState)nestedValue2011) : null;
    }

    public AutoFarmModeState internalMethod02711() {
        return this.internalField0694;
    }

    @Generated
    public FarmStatsTracker internalMethod03987() {
        return this.internalField0849;
    }
}
