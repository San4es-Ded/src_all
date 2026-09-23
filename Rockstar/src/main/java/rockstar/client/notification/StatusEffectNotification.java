package rockstar.client.notification;


import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.notification.IconNotification;

public class StatusEffectNotification
extends IconNotification {
    private final RegistryEntry<StatusEffect> internalField0151;

    public StatusEffectNotification(String string, RegistryEntry<StatusEffect> registryEntry) {
        super(string, null, ColorRGBA.fromInt(((StatusEffect)registryEntry.value()).getColor()));
        this.internalField0151 = registryEntry;
    }

    public StatusEffectNotification internalMethod01683(String string) {
        this.internalField0247 = string;
        return this;
    }

    public StatusEffectNotification internalMethod02349(ColorRGBA colorRGBA) {
        this.internalField0777 = colorRGBA;
        return this;
    }

    @Override
    protected void internalMethod01476(CustomDrawContext customDrawContext, float f, float f2, float f3) {
        customDrawContext.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED,
            InGameHud.getEffectTexture(this.internalField0151),
            Math.round(f),
            Math.round(f2),
            10,
            10,
            f3
        );
    }
}
