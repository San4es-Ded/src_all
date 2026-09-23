package rockstar.modules.other;






import rockstar.client.util.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.item.ItemStack;
import pyrock.events.game.PickupEvent;
import pyrock.events.window.ContainerClickEvent;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.CustomItemUtils;
import rockstar.client.module.Module;
import rockstar.client.notification.ItemNotification;

@ModuleInfo(name="Item Pickup", category=ModuleCategory.OTHER, internalMethod08049=true, internalMethod09633="modules.descriptions.item_pickup")
public class ItemPickupModule
extends Module {
    private final EventListener<PickupEvent> internalField0157 = pickupEvent -> {
        ItemStack itemStack = pickupEvent.getItemStack();
        if (pickupEvent.getEntity() != ItemPickupModule.internalField0149.player) {
            return;
        }
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
        if (nestedValue2032 != null) {
            String string = nestedValue2032.internalMethod00671(itemStack);
            RockstarClient.getInstance().internalMethod02503().internalMethod02784(new ItemNotification(LanguageManager.internalMethod07214("alerts.donate_picked") + string, itemStack).internalMethod03390(string).internalMethod05942(nestedValue2032.internalMethod01667(itemStack)));
        }
    };
    private final EventListener<ContainerClickEvent> internalField0158 = containerClickEvent -> {};
}
