package haron.hud.core;

import haron.effects.SpecialPotionTracker;
import haron.effects.ParticleSystem;
import haron.hud.core.HudService;
import haron.markers.AutoEventMarkerTracker;
import haron.markers.MarkerHudElement;
import haron.media.MediaPlayerHudElement;
import haron.module.ModuleAccessService;
import haron.player.TargetTracker;
import java.util.ArrayList;
import java.util.List;

public final class HudServices {
    private static final List<HudService> SERVICES = new ArrayList<HudService>();
    public static final MediaPlayerHudElement MEDIA = new MediaPlayerHudElement();
    public static final TargetTracker TARGETS = new TargetTracker();
    public static final MarkerHudElement MAP_MARKERS = new MarkerHudElement();
    public static final AutoEventMarkerTracker MAP_MARKER_MODULE = new AutoEventMarkerTracker();
    public static final ModuleAccessService MODULE_ACCESS = new ModuleAccessService();
    public static final ParticleSystem PARTICLES = new ParticleSystem();
    public static final SpecialPotionTracker POTION_EFFECTS = new SpecialPotionTracker();
    private HudServices() {
    }

    public static List<HudService> getServices() {
        return new ArrayList<HudService>(SERVICES);
    }

    public static void initialize() {
        SERVICES.clear();
        HudServices.register(MEDIA);
        HudServices.register(TARGETS);
        HudServices.register(MAP_MARKERS);
        HudServices.register(MAP_MARKER_MODULE);
        HudServices.register(MODULE_ACCESS);
        HudServices.register(PARTICLES);
        HudServices.register(POTION_EFFECTS);
    }

    private static void register(HudService volr072) {
        SERVICES.add(volr072);
        volr072.initialize();
    }
}
