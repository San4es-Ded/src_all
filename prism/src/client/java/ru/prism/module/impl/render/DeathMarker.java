package ru.prism.module.impl.render;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import ru.prism.command.impl.WayCommand;
import ru.prism.manager.event_impl.EventPacket;
import ru.prism.manager.event_impl.WorldLoadEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.StringSetting;

@ModuleInfo(
        name = "Death Marker",
        desc = "Ставит метку там, где ты откинулся, чтобы потом найти свои шмотки.",
        category = Category.VISUALS
)
public class DeathMarker extends Module {

    public final BooleanSetting keepPrevious = new BooleanSetting(this, "Сохранять прошлое", false);
    public final StringSetting label = new StringSetting(this, "Подпись", "Death");

    private static final byte DEATH_STATUS = 3;

    @Override
    protected void onDisable() {
        clearMarkers();
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        clearMarkers();
    }

    @EventHandler
    public void onPacket(EventPacket e) {
        if (mc.player == null || mc.world == null) return;
        if (!(e.getPacket() instanceof EntityStatusS2CPacket packet)) return;
        if (packet.getStatus() != DEATH_STATUS) return;
        Entity entity = packet.getEntity(mc.world);
        if (entity != mc.player) return;

        WayCommand way = WayCommand.get();
        if (way == null) return;

        if (!keepPrevious.getValue()) way.clearTemp();

        String text = label.getValue();
        if (text == null || text.isBlank()) text = "Death";
        way.addTemp(text, mc.player.getEntityPos());
    }

    private void clearMarkers() {
        WayCommand way = WayCommand.get();
        if (way != null) way.clearTemp();
    }
}
