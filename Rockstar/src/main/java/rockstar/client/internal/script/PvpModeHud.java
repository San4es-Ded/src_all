package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.script.DynamicIslandLabelStatus;
import rockstar.client.util.GameUtils;
import rockstar.client.server.ServerUtils;

public class PvpModeHud
extends DynamicIslandLabelStatus {
    public PvpModeHud(MultiSelectSetting typedValue173) {
        super(typedValue173, "pvp");
    }

    @Override
    public void prepare(DynamicIslandHud typedValue201) {
        this.internalMethod03185("s", ServerUtils.internalField0227, LanguageManager.internalMethod07214("hud.pvp_mode"), new ColorRGBA(185.0f, 28.0f, 28.0f));
        super.prepare(typedValue201);
    }

    @Override
    public boolean canShow() {
        return ServerUtils.internalField0277 && GameUtils.internalMethod00471();
    }
}

