package rockstar.modules.other;





import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pyrock.events.network.ReceivePacketEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.TextSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.TextUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="Auto Auth", category=ModuleCategory.OTHER, internalMethod09633="modules.descriptions.auto_auth")
public class AutoAuthModule
extends Module {
    private BooleanSetting internalField0650;
    private TextSetting internalField0384;
    private final Map<String, String> internalField0543 = new HashMap<String, String>();
    private final EventListener<ReceivePacketEvent> internalField0157 = receivePacketEvent -> {
        Object object = receivePacketEvent.getPacket();
        if (object instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)object;
            if (AutoAuthModule.internalField0149.player != null) {
                object = gameMessageS2CPacket.content().getString().toLowerCase();
                String string = TextUtils.internalMethod07495();
                String string2 = this.internalField0650.internalMethod04496() ? string : this.internalField0384.internalMethod08926();
                this.internalField0543.put(AutoAuthModule.internalField0149.player.getDisplayName().getString(), " " + string);
                if (((String)object).contains("\u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u0443\u0439\u0442\u0435\u0441\u044c") || ((String)object).contains("/reg")) {
                    AutoAuthModule.internalField0149.player.networkHandler.sendChatCommand(String.format("reg %s %s", string2, string2));
                } else if (((String)object).contains("\u0430\u0432\u0442\u043e\u0440\u0438\u0437\u0443\u0439\u0442\u0435\u0441\u044c") || ((String)object).contains("/login") || ((String)object).contains("/l") && ((String)object).matches("/l(\\s|$)")) {
                    AutoAuthModule.internalField0149.player.networkHandler.sendChatCommand(String.format("l %s", string2));
                }
            }
        }
    };

    public AutoAuthModule() {
        this.internalMethod09884();
    }

    private void internalMethod09884() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.auto_auth.random");
        this.internalField0384 = new TextSetting((SettingOwner)this, "modules.settings.auto_auth.password", this.internalField0650::internalMethod04496).internalMethod00011("123123");
    }

    public Map<String, String> internalMethod06147() {
        return Collections.unmodifiableMap(this.internalField0543);
    }

    public void internalMethod07079(String string, String string2) {
        this.internalField0543.put(string, string2);
    }
}
