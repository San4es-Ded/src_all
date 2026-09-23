package rockstar.client.module;




import rockstar.client.util.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import pyrock.events.game.GameTickEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleEntry;
import rockstar.client.RockstarClient;
import rockstar.client.util.KeybindUtils;
import rockstar.client.internal.core.ElementLookupException;
import rockstar.client.module.Module;

public class ModuleManager {
    private final Map<Class<? extends ModuleEntry>, ModuleEntry> internalField0543 = new IdentityHashMap<Class<? extends ModuleEntry>, ModuleEntry>();
    private final List<ModuleEntry> internalField0416 = new ArrayList<ModuleEntry>();
    private static int internalField0227;
    private final EventListener<ClientPlayerTickEvent> internalField0157;
    private final EventListener<HudRenderEvent> internalField0158;
    private final EventListener<KeyPressEvent> internalField1028 = keyPressEvent -> {
        if (MinecraftClient.getInstance().currentScreen != null) {
            return;
        }
        if (keyPressEvent.getAction() != 1) {
            return;
        }
        int n = KeybindUtils.internalMethod06867();
        for (ModuleEntry typedValue145 : this.getModules()) {
            if (!typedValue145.isAvailable() || !KeybindUtils.internalMethod01369(typedValue145.getKeybind(), keyPressEvent.getKey(), n)) continue;
            typedValue145.toggle();
        }
    };
    private final EventListener<MouseEvent> internalField1029 = mouseEvent -> {
        if (MinecraftClient.getInstance().currentScreen != null) {
            return;
        }
        if (mouseEvent.getAction() != 1) {
            return;
        }
        int n = KeybindUtils.internalMethod06867();
        for (ModuleEntry typedValue145 : this.getModules()) {
            if (!typedValue145.isAvailable() || !KeybindUtils.internalMethod01369(typedValue145.getKeybind(), mouseEvent.getButton(), n)) continue;
            typedValue145.toggle();
        }
    };
    private final EventListener<GameTickEvent> internalField1030 = gameTickEvent -> {
        if (!Module.internalMethod08664()) {
            return;
        }
        for (ModuleEntry typedValue145 : this.getModules()) {
            if (!typedValue145.isEnabled() || typedValue145.isAvailable()) continue;
            typedValue145.setEnabled(false, true);
        }
    };

    public static void internalMethod03046() {
        ++internalField0227;
    }

    public ModuleManager(EventListener<ClientPlayerTickEvent> typedValue136, EventListener<HudRenderEvent> typedValue137) {
        this.internalField0157 = typedValue136;
        this.internalField0158 = typedValue137;
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public final void registerModules() {
        this.registerCombatModules();
        this.registerMovementModules();
        this.registerVisualModules();
        this.registerPlayerModules();
        this.registerOtherModules();
        this.snapshotModuleSettings();
    }

    private void registerCombatModules() {
        this.registerModule(new AimAssistModule());
        this.registerModule(new AimBotModule());
        this.registerModule(new AntiBotModule());
        this.registerModule(new AuraModule());
        this.registerModule(new AutoAnchorModule());
        this.registerModule(new AutoArmorModule());
        this.registerModule(new AutoExplosionModule());
        this.registerModule(new AutoPotionModule());
        this.registerModule(new AutoSoupModule());
        this.registerModule(new AutoThrowModule());
        this.registerModule(new AutoTotemModule());
        this.registerModule(new BackTrackModule());
        this.registerModule(new CriticalsModule());
        this.registerModule(new ElytraTargetModule());
        this.registerModule(new HitboxesModule());
        this.registerModule(new KnockbackTweaksModule());
        this.registerModule(new TriggerBotModule());
        this.registerModule(new VelocityModule());
    }

    private void registerMovementModules() {
        this.registerModule(new AirStuckModule());
        this.registerModule(new AutoSprintModule());
        this.registerModule(new ElytraStrafeModule());
        this.registerModule(new FlightModule());
        this.registerModule(new GrimGlideModule());
        this.registerModule(new HighJumpModule());
        this.registerModule(new NoSlowModule());
        this.registerModule(new SpeedModule());
        this.registerModule(new SpiderModule());
        this.registerModule(new StrafeModule());
        this.registerModule(new SuperFireworkModule());
        this.registerModule(new TimerModule());
        this.registerModule(new WaterSpeedModule());
    }

    private void registerVisualModules() {
        this.registerModule(new AmbienceModule());
        this.registerModule(new AntiInvisibleModule());
        this.registerModule(new BeautifullyModule());
        this.registerModule(new CustomFogModule());
        this.registerModule(new DonateEffectsModule());
        this.registerModule(new EspModule());
        this.registerModule(new InterfaceModule());
        this.registerModule(new KillEffectsModule());
        this.registerModule(new MenuModule());
        this.registerModule(new ObjectInfoModule());
        this.registerModule(new PositionPredictModule());
        this.registerModule(new PotsTimerModule());
        this.registerModule(new PredictionModule());
        this.registerModule(new RemovalsModule());
        this.registerModule(new SoundEspModule());
        this.registerModule(new StorageEspModule());
        this.registerModule(new SwingAnimationModule());
        this.registerModule(new TargetEspModule());
        this.registerModule(new TntTimerModule());
        this.registerModule(new TrapEspModule());
        this.registerModule(new ViewModelModule());
        this.registerModule(new WardenHelperModule());
        this.registerModule(new WaypointsModule());
        this.registerModule(new WorldModule());
        this.registerModule(new XRayModule());
    }

    private void registerPlayerModules() {
        this.registerModule(new AutoBotModule());
        this.registerModule(new AutoBrewModule());
        this.registerModule(new AutoEatModule());
        this.registerModule(new AutoFarmModule());
        this.registerModule(new AutoInvisibleModule());
        this.registerModule(new AutoLeaveModule());
        this.registerModule(new AutoShulkerModule());
        this.registerModule(new AutoSwapModule());
        this.registerModule(new BlinkModule());
        this.registerModule(new BootsSwapModule());
        this.registerModule(new ClanUpgradeModule());
        this.registerModule(new ClickThroughModule());
        this.registerModule(new ElytraUtilsModule());
        this.registerModule(new FreeCameraModule());
        this.registerModule(new GuiMoveModule());
        this.registerModule(new InventoryUtilsModule());
        this.registerModule(new MiddleClickModule());
        this.registerModule(new MineHelperModule());
        this.registerModule(new NoDelayModule());
        this.registerModule(new NoFallModule());
        this.registerModule(new NoInteractModule());
        this.registerModule(new NoPushModule());
        this.registerModule(new NoRotateModule());
        this.registerModule(new NukerModule());
        this.registerModule(new PlayerUtilsModule());
        this.registerModule(new ScaffoldModule());
        this.registerModule(new StealerModule());
        this.registerModule(new TargetPearlModule());
        this.registerModule(new TrackerModule());
    }

    private void registerOtherModules() {
        this.registerModule(new AntiAimModule());
        this.registerModule(new AssistModule());
        this.registerModule(new AuctionModule());
        this.registerModule(new AutoAcceptModule());
        this.registerModule(new AutoAuthModule());
        this.registerModule(new AutoBuyModule());
        this.registerModule(new AutoDuelsModule());
        this.registerModule(new AutoJoinModule());
        this.registerModule(new AutoResellModule());
        this.registerModule(new BaseFinderModule());
        this.registerModule(new DeathCordsModule());
        this.registerModule(new EffectRemoverModule());
        this.registerModule(new FastItemUseModule());
        this.registerModule(new GlobalsMenuModule());
        this.registerModule(new InventoryBuilderModule());
        this.registerModule(new InventoryCleanerModule());
        this.registerModule(new ItemDumperModule());
        this.registerModule(new ItemPickupModule());
        this.registerModule(new KtLeaveModule());
        this.registerModule(new MacroMenuModule());
        this.registerModule(new NameProtectModule());
        this.registerModule(new PanicModule());
        this.registerModule(new SoundsModule());
        this.registerModule(new WebUtilsModule());
    }


    public final void enableDefaultModules() {
        for (ModuleEntry typedValue145 : this.getModules()) {
            if (!typedValue145.isEnabledByDefault()) continue;
            typedValue145.enable();
        }
    }

    public final void registerModule(Module Module2) {
        this.internalField0543.put(Module2.getClass(), Module2);
        this.internalField0416.add(Module2);
    }

    public final <T extends ModuleEntry> T getModuleByName(String string) {
        return (T)this.internalField0416.stream().filter(typedValue145 -> typedValue145.getName().replace(" ", "").equalsIgnoreCase(string) || typedValue145.getName().equalsIgnoreCase(string)).findFirst().orElseThrow(() -> new ElementLookupException(string));
    }

    public final <T extends ModuleEntry> T getModule(Class<T> clazz) {
        return (T)((ModuleEntry)clazz.cast(this.internalField0543.get(clazz)));
    }

    public final void snapshotModuleSettings() {
        for (ModuleEntry typedValue145 : this.getModules()) {
            if (!(typedValue145 instanceof Module)) continue;
            Module Module2 = (Module)typedValue145;
            Module2.internalMethod09922();
        }
    }

    @Generated
    public List<ModuleEntry> getModules() {
        return this.internalField0416;
    }

    @Generated
    public static int internalMethod03045() {
        return internalField0227;
    }

    @Generated
    public EventListener<ClientPlayerTickEvent> internalMethod00449() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<HudRenderEvent> internalMethod01797() {
        return this.internalField0158;
    }
}
