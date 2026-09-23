package rockstar.modules.player;





import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.*;

import moscow.rockstar.mixin.minecraft.client.IMinecraftClient;
import net.minecraft.component.DataComponentTypes;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.module.Module;

@ModuleInfo(name="Auto Eat", category=ModuleCategory.PLAYER)
public class AutoEatModule
extends Module {
    private boolean internalField0277;
    private SliderSetting internalField0383;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if ((float)AutoEatModule.internalField0149.player.getHungerManager().getFoodLevel() <= this.internalField0383.internalMethod08576()) {
            SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
            InventorySlot typedValue222 = typedValue228.internalMethod03297(itemStack -> itemStack.getItem().getDefaultStack().contains(DataComponentTypes.FOOD));
            if (!AutoEatModule.internalField0149.player.getOffHandStack().contains(DataComponentTypes.FOOD) && typedValue222 != null) {
                typedValue222.internalMethod06663();
            }
            this.internalField0277 = true;
            if (AutoEatModule.internalField0149.currentScreen != null && !AutoEatModule.internalField0149.player.isUsingItem()) {
                ((IMinecraftClient)internalField0149).idoItemUse();
            } else {
                AutoEatModule.internalField0149.options.useKey.setPressed(true);
            }
        } else if (this.internalField0277) {
            this.internalField0277 = false;
            AutoEatModule.internalField0149.options.useKey.setPressed(false);
        }
    };

    public AutoEatModule() {
        this.internalMethod09533();
    }

    private void internalMethod09533() {
        this.internalField0383 = new SliderSetting(this, "modules.settings.auto_eat.food").internalMethod08673(1.0f).internalMethod05900(1.0f).internalMethod02732(20.0f).internalMethod08074(15.0f);
    }
}
