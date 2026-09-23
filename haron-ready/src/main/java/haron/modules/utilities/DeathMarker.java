package haron.modules.utilities;

import haron.events.PlayerDeathEvent;
import haron.markers.MarkerIcon;
import haron.markers.MarkerRegistry;
import haron.markers.Waypoint;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.ModeSetting;
import haron.settings.StringSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;

@ModuleInfo(a="Death Marker", b="Автоматически ставит метку на место смерти.", c=ModuleCategory.UTILITIES)
public class DeathMarker
extends HaronModule {
    private final BooleanSetting keepPrevious = new BooleanSetting("Сохранять предыдущий", false);
    private final ModeSetting color = new ModeSetting("Цвет", new String[]{"Red", "White", "Yellow", "Cyan"}, "Red");
    private final StringSetting label = new StringSetting("Label", "Death");

    @EventHandler
    public void onDeath(PlayerDeathEvent c5scvm2) {
        if (DeathMarker.c.player == null || c5scvm2.player() != DeathMarker.c.player) {
            return;
        }
        int n = (int)c5scvm2.x();
        int n2 = (int)c5scvm2.y();
        int n3 = (int)c5scvm2.z();
        if (!this.keepPrevious.get()) {
            MarkerRegistry.a().stream().filter(yo0tnu2 -> {
                return yo0tnu2.h();
            }).forEach(MarkerRegistry::b);
        }
        MarkerRegistry.a(new Waypoint(this.label.get().isBlank() ? "Death" : this.label.get(), n, n2, n3, switch (this.color.d()) {
            case "White" -> new Color(255, 255, 255);
            case "Yellow" -> new Color(255, 220, 50);
            case "Cyan" -> new Color(50, 220, 255);
            default -> new Color(220, 50, 50);
        }, MarkerIcon.DEATH));
    }

    public DeathMarker() {
        this.collectSettings();
    }
}

