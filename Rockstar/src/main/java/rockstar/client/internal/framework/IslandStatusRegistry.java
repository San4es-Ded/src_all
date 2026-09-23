package rockstar.client.internal.framework;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.internal.script.AutoFarmStatusWidget;
import rockstar.client.internal.script.DynamicIslandDefaultStatus;
import rockstar.client.internal.script.LootWaypointHud;
import rockstar.client.internal.script.MineshaftTimerHud;
import rockstar.client.internal.script.ModuleStatusHud;
import rockstar.client.internal.script.MusicLyricsHud;
import rockstar.client.internal.script.SilentAlertHud;
import rockstar.client.internal.script.PvpModeHud;

public final class IslandStatusRegistry {
    private IslandStatusRegistry() {
    }

    public static void internalMethod07184(MultiSelectSetting typedValue173) {
        new SilentAlertHud(typedValue173);
        new ModuleStatusHud(typedValue173);
        new AutoFarmStatusWidget(typedValue173);
        new PvpModeHud(typedValue173);
        new LootWaypointHud(typedValue173).deselect();
        new MineshaftTimerHud(typedValue173);
        new MusicLyricsHud(typedValue173);
        new DynamicIslandDefaultStatus(typedValue173).alwaysEnabled();
    }
}
