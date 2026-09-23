package rockstar.client.internal.ui;






import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.animation.*;
import rockstar.client.internal.framework.*;
import rockstar.client.*;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.internal.ui.HudStatListWidget;
import rockstar.client.internal.framework.AnimatedStatusEntry;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;

public class PlayerStatsHud
extends HudStatListWidget {
    private final AnimatedStatusEntry internalField0365;
    private final AnimatedStatusEntry internalField0364;
    private final BooleanSetting internalField0650;
    private final AnimatedValue internalField0808;

    public PlayerStatsHud() {
        super("hud.player", "hud/player");
        this.internalField0365 = new AnimatedStatusEntry(this.internalField0675, "FPS", "FPS");
        this.internalField0364 = new AnimatedStatusEntry(this.internalField0675, "speed", "BPS");
        this.internalField0650 = new BooleanSetting(this, "hud.player.speedY").internalMethod06630();
        this.internalField0808 = new AnimatedValue(300L, 0.0f, Easing.internalField1624);
        this.showing = true;
        this.pos(2.5f, 22.667f);
    }

    @Override
    public void update(UiRenderContext iII) {
        super.update(iII);
        double d = !this.internalField0650.internalMethod04496() ? Math.hypot(PlayerStatsHud.internalField0149.player.getX() - PlayerStatsHud.internalField0149.player.lastX, PlayerStatsHud.internalField0149.player.getZ() - PlayerStatsHud.internalField0149.player.lastZ) : Math.hypot(PlayerStatsHud.internalField0149.player.getY() - PlayerStatsHud.internalField0149.player.lastY, Math.hypot(PlayerStatsHud.internalField0149.player.getX() - PlayerStatsHud.internalField0149.player.lastX, PlayerStatsHud.internalField0149.player.getZ() - PlayerStatsHud.internalField0149.player.lastZ));
        this.internalField0364.internalMethod05945(String.format("%.2f", d * 20.0).replace(",", "."));
        this.internalField0365.internalMethod05945("" + Math.round(this.internalField0808.internalMethod07059(internalField0149.getCurrentFps())));
    }
}

