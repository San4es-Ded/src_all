package ru.prism.module.impl.render;


import ru.prism.manager.event_impl.EventUpdate;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(
        name = "Gamma",
        desc = "Врубает полный фуллбрайт, видно даже в пещере у жопы мира.",
        category = Category.WORLD
)
public class Gamma extends Module {


    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.worldRenderer != null) mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.worldRenderer != null) mc.worldRenderer.reload();
    }
    @EventHandler
    public void onUpdate(EventUpdate e) {
        if (mc.player == null) return;
        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
