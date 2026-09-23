package haron.modules.utilities;

import haron.events.PacketEvent;
import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.BooleanSetting;
import haron.util.jeooat;
import meteordevelopment.orbit.EventHandler;

@ModuleInfo(a="Auto Reissue", b="Tracks auction reissue cooldowns.", c=ModuleCategory.UTILITIES)
public class AutoReissue
extends HaronModule {
    private final BooleanSetting autoReissue = new BooleanSetting("Авто повтор", true);
    private final BooleanSetting showCooldownOverlay = new BooleanSetting("Показывать оверлей кулдауна", true);
    public boolean overlayActive = false;
    public jeooat timer = new jeooat();
    public int durationMs = 15000;

    @Override
    public void f() {
        super.f();
        this.overlayActive = false;
        this.timer.b();
    }

    @EventHandler
    public void a(PacketEvent g07m232) {
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
    }
}

