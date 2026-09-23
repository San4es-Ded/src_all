package rockstar.modules.visual;






import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Arm;
import org.joml.Quaternionf;
import pyrock.events.render.HandRenderEvent;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ButtonSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.ui.SwingPresetEditorScreen;
import rockstar.client.internal.core.SwingTransform;
import rockstar.client.module.Module;
import rockstar.modules.combat.AuraModule;

@ModuleInfo(name="Swing Animation", category=ModuleCategory.VISUALS, internalMethod08049=true, internalMethod09633="modules.descriptions.swing_animation")
public class SwingAnimationModule
extends Module {
    private BooleanSetting internalField0650;
    private ButtonSetting internalField0663;
    private final EventListener<HandRenderEvent> internalField0157 = handRenderEvent -> {
        if (this.internalMethod02588(handRenderEvent.getItemStack()) && handRenderEvent.getArm() == SwingAnimationModule.internalField0149.options.getMainArm().getValue()) {
            MatrixStack matrixStack = handRenderEvent.getMatrices();
            float f = handRenderEvent.getSwingProgress();
            float f2 = handRenderEvent.getEquipProgress();
            SwingTransform typedValue112 = RockstarClient.getInstance().internalMethod00061().internalMethod01686(f);
            if (handRenderEvent.getArm() == Arm.LEFT) {
                typedValue112 = this.internalMethod05289(typedValue112);
            }
            matrixStack.translate(typedValue112.internalMethod01052(), typedValue112.internalMethod01057(), typedValue112.internalMethod08670());
            matrixStack.translate(typedValue112.internalMethod08672(), typedValue112.internalMethod08693(), typedValue112.internalMethod08695());
            matrixStack.multiply(new Quaternionf().rotationXYZ((float)Math.toRadians(typedValue112.internalMethod09416()), (float)Math.toRadians(typedValue112.internalMethod09417()), (float)Math.toRadians(typedValue112.internalMethod09426())));
            matrixStack.translate(-typedValue112.internalMethod01052(), -typedValue112.internalMethod01057(), -typedValue112.internalMethod08670());
            handRenderEvent.cancel();
        }
    };

    public SwingAnimationModule() {
        this.internalMethod09197();
    }

    private void internalMethod09197() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.swing_animation.only_aura");
        this.internalField0663 = new ButtonSetting(this, "modules.settings.swing_animation.open_menu").internalMethod07149(() -> internalField0149.setScreen((Screen)new SwingPresetEditorScreen()));
    }

    private SwingTransform internalMethod05289(SwingTransform typedValue112) {
        return new SwingTransform(-typedValue112.internalMethod01052(), typedValue112.internalMethod01057(), typedValue112.internalMethod08670(), -typedValue112.internalMethod08672(), typedValue112.internalMethod08693(), typedValue112.internalMethod08695(), typedValue112.internalMethod09416(), -typedValue112.internalMethod09417(), -typedValue112.internalMethod09426());
    }

    public boolean internalMethod02588(ItemStack itemStack) {
        AuraModule internalValue0004 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
        Entity entity = RockstarClient.getInstance().internalMethod04463().internalMethod04526();
        Item item = itemStack.getItem();
        if (this.internalField0650.internalMethod04496() && (!internalValue0004.isEnabled() || entity == null)) {
            return false;
        }
        return item != Items.AIR && item != Items.FILLED_MAP && item != Items.CROSSBOW && item != Items.BOW && item != Items.TRIDENT && item.getUseAction(itemStack) != UseAction.DRINK && item.getUseAction(itemStack) != UseAction.EAT;
    }
}
