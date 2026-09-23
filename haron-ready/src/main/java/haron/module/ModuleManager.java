package haron.module;

import haron.effects.JumpCircles;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.hud.EffectNotify;
import haron.modules.hud.ClientColor;
import haron.modules.hud.TargetHud;
import haron.modules.hud.Watermark;
import haron.modules.hud.Potions;
import haron.modules.hud.CooldownsHud;
import haron.modules.hud.Hotkeys;
import haron.modules.hud.ArmorHud;
import haron.modules.utilities.AutoDuel;
import haron.modules.utilities.ElytraSwap;
import haron.modules.utilities.TotemTracker;
import haron.modules.utilities.GpsCommand;
import haron.modules.utilities.HealingHelper;
import haron.modules.utilities.HwHelper;
import haron.modules.utilities.PvpSafe;
import haron.modules.utilities.SoundController;
import haron.modules.utilities.FastExp;
import haron.modules.utilities.AutoLeave;
import haron.modules.utilities.ItemSwap;
import haron.modules.utilities.TapeMouse;
import haron.modules.utilities.Zoom;
import haron.modules.utilities.StreamerMode;
import haron.modules.utilities.Sprint;
import haron.modules.utilities.FakePlayer;
import haron.modules.utilities.FreeLook;
import haron.modules.utilities.AutoInvest;
import haron.modules.utilities.ShiftTab;
import haron.modules.utilities.ItemPickupLogger;
import haron.modules.utilities.FcHelper;
import haron.modules.utilities.DeathMarker;
import haron.modules.utilities.RwHelper;
import haron.modules.utilities.LockSlot;
import haron.modules.utilities.Predictions;
import haron.modules.utilities.AutoReconnect;
import haron.modules.utilities.AutoReissue;
import haron.modules.utilities.Cooldowns;
import haron.modules.utilities.ItemScroller;
import haron.modules.utilities.Optimizations;
import haron.modules.utilities.RwJoiner;
import haron.modules.utilities.AutoRespawn;
import haron.modules.utilities.AutoPotion;
import haron.modules.utilities.ItemHighlighter;
import haron.modules.utilities.AutoEat;
import haron.modules.utilities.FastSwap;
import haron.modules.utilities.FtHelper;
import haron.modules.visuals.WorldParticles;
import haron.modules.visuals.SelfNametag;
import haron.modules.visuals.SkyShader;
import haron.modules.visuals.CustomSwords;
import haron.modules.visuals.HitColor;
import haron.modules.visuals.NoFluid;
import haron.modules.visuals.HitGhost;
import haron.modules.visuals.Nimb;
import haron.modules.visuals.Trails;
import haron.modules.visuals.ShulkerPreview;
import haron.modules.visuals.FullBright;
import haron.modules.visuals.HitBubble;
import haron.modules.visuals.RenderTweaks;
import haron.modules.visuals.Wings;
import haron.modules.visuals.BlockOverlay;
import haron.modules.visuals.HitboxCustomizer;
import haron.modules.visuals.Ghost;
import haron.modules.visuals.AspectRatio;
import haron.modules.visuals.ChinaHat;
import haron.modules.visuals.Animations;
import haron.modules.visuals.HitSounds;
import haron.modules.visuals.TargetEsp;
import haron.modules.visuals.Particles;
import haron.modules.visuals.KillEffect;
import haron.modules.visuals.Cosmetics;
import haron.modules.visuals.HandShader;
import haron.modules.visuals.TimeChanger;
import haron.modules.visuals.WorldCustomizer;
import haron.modules.visuals.Crosshair;
import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
    private static final List<HaronModule> MODULES = new ArrayList<HaronModule>();
    public static final FullBright FULL_BRIGHT = new FullBright();
    public static final Trails TRAILS = new Trails();
    public static final Particles PARTICLES = new Particles();
    public static final WorldParticles WORLD_PARTICLES = new WorldParticles();
    public static final JumpCircles JUMP_CIRCLES = new JumpCircles();
    public static final AspectRatio ASPECT_RATIO = new AspectRatio();
    public static final RenderTweaks RENDER_TWEAKS = new RenderTweaks();
    public static final TargetEsp TARGET_ESP = new TargetEsp();
    public static final HitboxCustomizer HITBOX_CUSTOMIZER = new HitboxCustomizer();
    public static final WorldCustomizer WORLD_CUSTOMIZER = new WorldCustomizer();
    public static final Crosshair CROSSHAIR = new Crosshair();
    public static final HitColor HIT_COLOR = new HitColor();
    public static final NoFluid NO_FLUID = new NoFluid();
    public static final BlockOverlay BLOCK_OVERLAY = new BlockOverlay();
    public static final HitBubble HIT_BUBBLE = new HitBubble();
    public static final SelfNametag SELF_NAMETAG = new SelfNametag();
    public static final TimeChanger TIME_CHANGER = new TimeChanger();
    public static final HitSounds HIT_SOUNDS = new HitSounds();
    public static final ShulkerPreview SHULKER_PREVIEW = new ShulkerPreview();
    public static final Animations ANIMATIONS = new Animations();
    public static final Sprint SPRINT = new Sprint();
    public static final ElytraSwap ELYTRA_SWAP = new ElytraSwap();
    public static final FtHelper FT_HELPER = new FtHelper();
    public static final HealingHelper HEALING_HELPER = new HealingHelper();
    public static final Predictions PREDICTIONS = new Predictions();
    public static final ItemSwap ITEM_SWAP = new ItemSwap();
    public static final FastSwap FAST_SWAP = new FastSwap();
    public static final PvpSafe PVP_SAFE = new PvpSafe();
    public static final Cooldowns COOLDOWNS = new Cooldowns();
    public static final AutoEat AUTO_EAT = new AutoEat();
    public static final AutoInvest AUTO_INVEST = new AutoInvest();
    public static final AutoPotion AUTO_POTION = new AutoPotion();
    public static final AutoRespawn AUTO_RESPAWN = new AutoRespawn();
    public static final AutoReconnect AUTO_RECONNECT = new AutoReconnect();
    public static final AutoLeave AUTO_LEAVE = new AutoLeave();
    public static final FcHelper FC_HELPER = new FcHelper();
    public static final StreamerMode STREAMER_MODE = new StreamerMode();
    public static final HwHelper HW_HELPER = new HwHelper();
    public static final AutoReissue AUTO_REISSUE = new AutoReissue();
    public static final TapeMouse TAPE_MOUSE = new TapeMouse();
    public static final FreeLook FREE_LOOK = new FreeLook();
    public static final ItemScroller ITEM_SCROLLER = new ItemScroller();
    public static final LockSlot LOCK_SLOT = new LockSlot();
    public static final Zoom ZOOM = new Zoom();
    public static final TotemTracker TOTEM_TRACKER = new TotemTracker();
    public static final ItemHighlighter ITEM_HIGHLIGHTER = new ItemHighlighter();
    public static final AutoDuel AUTO_DUEL = new AutoDuel();
    public static final RwHelper RW_HELPER = new RwHelper();
    public static final ShiftTab SHIFT_TAB = new ShiftTab();
    public static final SoundController SOUND_CONTROLLER = new SoundController();
    public static final RwJoiner RW_JOINER = new RwJoiner();
    public static final FastExp FAST_EXP = new FastExp();
    public static final ItemPickupLogger ITEM_PICKUP_LOGGER = new ItemPickupLogger();
    public static final Potions POTIONS_HUD = new Potions();
    public static final TargetHud TARGET_HUD = new TargetHud();
    public static final EffectNotify EFFECT_NOTIFY = new EffectNotify();
    public static final ClientColor CLIENT_COLOR = new ClientColor();
    public static final ArmorHud ARMOR_HUD = new ArmorHud();
    public static final Hotkeys HOTKEYS_HUD = new Hotkeys();
    public static final Watermark WATERMARK = new Watermark();
    public static final ChinaHat CHINA_HAT = new ChinaHat();
    public static final Nimb NIMB = new Nimb();
    public static final CooldownsHud COOLDOWNS_HUD = new CooldownsHud();
    public static final FakePlayer FAKE_PLAYER = new FakePlayer();
    public static final DeathMarker DEATH_MARKER = new DeathMarker();
    public static final GpsCommand GPS_COMMAND = new GpsCommand();
    public static final Optimizations OPTIMIZATIONS = new Optimizations();
    public static final Wings WINGS = new Wings();
    public static final Cosmetics COSMETICS = new Cosmetics();
    public static final Ghost GHOST = new Ghost();
    public static final HitGhost HIT_GHOST = new HitGhost();
    public static final SkyShader SKY_SHADER = new SkyShader();
    public static final HandShader HAND_SHADER = new HandShader();
    public static final KillEffect KILL_EFFECT = new KillEffect();
    public static final CustomSwords CUSTOM_SWORDS = new CustomSwords();

    public static HaronModule findByName(String string) {
        for (HaronModule jxs16t2 : MODULES) {
            if (!jxs16t2.name().equalsIgnoreCase(string)) continue;
            return jxs16t2;
        }
        return null;
    }

    public static List<HaronModule> byCategory(ModuleCategory jtlzd02) {
        ArrayList<HaronModule> arrayList = new ArrayList<HaronModule>();
        for (HaronModule jxs16t2 : MODULES) {
            if (jxs16t2.category() != jtlzd02) continue;
            arrayList.add(jxs16t2);
        }
        return arrayList;
    }

    private ModuleManager() {
    }

    public static <ConfigTextDialog extends HaronModule> ConfigTextDialog get(Class<ConfigTextDialog> clazz) {
        for (HaronModule jxs16t2 : MODULES) {
            if (!clazz.isInstance(jxs16t2)) continue;
            return (ConfigTextDialog)((HaronModule)clazz.cast(jxs16t2));
        }
        return null;
    }

    private static void register(HaronModule ... jxs16tArray) {
        for (HaronModule jxs16t2 : jxs16tArray) {
            MODULES.add(jxs16t2);
            jxs16t2.collectSettings();
        }
    }

    public static void init() {
        MODULES.clear();
        ModuleManager.register(FULL_BRIGHT, TRAILS, PARTICLES, WORLD_PARTICLES, JUMP_CIRCLES, ASPECT_RATIO, RENDER_TWEAKS, TARGET_ESP, HITBOX_CUSTOMIZER, WORLD_CUSTOMIZER, CROSSHAIR, HIT_COLOR, NO_FLUID, BLOCK_OVERLAY, HIT_BUBBLE, SELF_NAMETAG, TIME_CHANGER, HIT_SOUNDS, SHULKER_PREVIEW, ANIMATIONS, SPRINT, ELYTRA_SWAP, FT_HELPER, HEALING_HELPER, PREDICTIONS, ITEM_SWAP, FAST_SWAP, PVP_SAFE, COOLDOWNS, AUTO_EAT, AUTO_INVEST, AUTO_POTION, AUTO_RESPAWN, AUTO_RECONNECT, AUTO_LEAVE, FC_HELPER, STREAMER_MODE, HW_HELPER, AUTO_REISSUE, TAPE_MOUSE, FREE_LOOK, ITEM_SCROLLER, LOCK_SLOT, ZOOM, TOTEM_TRACKER, ITEM_HIGHLIGHTER, AUTO_DUEL, RW_HELPER, SHIFT_TAB, SOUND_CONTROLLER, RW_JOINER, FAST_EXP, ITEM_PICKUP_LOGGER, POTIONS_HUD, TARGET_HUD, EFFECT_NOTIFY, CLIENT_COLOR, ARMOR_HUD, HOTKEYS_HUD, WATERMARK, CHINA_HAT, NIMB, COOLDOWNS_HUD, FAKE_PLAYER, DEATH_MARKER, GPS_COMMAND, OPTIMIZATIONS, WINGS, COSMETICS, GHOST, HIT_GHOST, SKY_SHADER, HAND_SHADER, KILL_EFFECT, CUSTOM_SWORDS);
    }

    public static List<HaronModule> all() {
        return MODULES;
    }
}

