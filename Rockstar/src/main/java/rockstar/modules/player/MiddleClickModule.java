package rockstar.modules.player;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;

import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.internal.core.ItemCooldownHolder;
import rockstar.client.event.EventListener;
import rockstar.client.internal.script.FriendListManager;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.ClientMessages;
import rockstar.client.module.Module;

@ModuleInfo(name="Middle Click", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.middle_click")
public class MiddleClickModule
extends Module {
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private KeybindSetting internalField0648;
    private KeybindSetting internalField0647;
    private final EventListener<KeyPressEvent> internalField0157 = keyPressEvent -> this.internalMethod02395(keyPressEvent.getKey(), keyPressEvent.getAction());
    private final EventListener<MouseEvent> internalField0158 = mouseEvent -> this.internalMethod02395(mouseEvent.getButton(), mouseEvent.getAction());

    public MiddleClickModule() {
        this.internalMethod09397();
    }

    private void internalMethod09397() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.middle_click.actions").internalMethod03035(1);
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.middle_click.pearl").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.middle_click.friend");
        this.internalField0648 = new KeybindSetting(this, "modules.settings.middle_click.friend_key", () -> !this.internalField0244.isSelected());
        this.internalField0647 = new KeybindSetting(this, "modules.settings.middle_click.pearl_key", () -> !this.internalField0245.isSelected());
    }

    private void internalMethod02395(int n, int n2) {
        if (MiddleClickModule.internalField0149.currentScreen == null && n2 == 1) {
            if (this.internalField0244.isSelected() && this.internalField0648.internalMethod02165(n)) {
                if (MiddleClickModule.internalField0149.targetedEntity instanceof PlayerEntity) {
                    String string = MiddleClickModule.internalField0149.targetedEntity.getName().getString();
                    FriendListManager typedValue140 = RockstarClient.getInstance().internalMethod03375();
                    if (typedValue140.internalMethod00380(string)) {
                        typedValue140.internalMethod06965(string);
                    } else {
                        typedValue140.internalMethod00379(string);
                    }
                } else if (MiddleClickModule.internalField0149.targetedEntity instanceof SlimeEntity) {
                    ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("middle_click.slime_error")));
                }
            }
            if (this.internalField0245.isSelected() && this.internalField0647.internalMethod02165(n)) {
                ItemCooldownHolder.internalField0006.internalMethod07082(Items.ENDER_PEARL);
            }
        }
    }
}
