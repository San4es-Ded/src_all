package wtf.wyvern.core.modules;

import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.option.Perspective;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.util.math.MathHelper;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.input.EventKey;
import wtf.wyvern.core.events.impl.other.EventGameUpdate;
import wtf.wyvern.core.events.impl.render.EventHudRender;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.core.macro.Macro;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.setting.Setting;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.impl.combat.*;
import wtf.wyvern.client.modules.impl.misc.*;
import wtf.wyvern.client.modules.impl.movement.*;
import wtf.wyvern.client.modules.impl.player.*;
import wtf.wyvern.client.modules.impl.movement.NoFall;
import wtf.wyvern.client.modules.impl.render.*;
import wtf.wyvern.client.gui.screens.menu.MenuScreen;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.interfaces.IMinecraft;

public final class ModuleManager implements IMinecraft {
    private final List<Module> modules = new ArrayList<>();
    private final Map<String, Module> modulesByName = new HashMap<>();
    private final List<BooleanSetting> animatedBooleans = new ArrayList<>();
    private final List<ModeSetting.Value> animatedModeValues = new ArrayList<>();
    private final List<MultiBooleanSetting.Value> animatedMultiValues = new ArrayList<>();
    private boolean isBack;
    private boolean isRotated;
    private float acceleration;

    public ModuleManager() {
        init();
        EventManager.register(this);
    }

    private void init() {
        registerCombat();
        registerMovement();
        registerRender();
        registerPlayer();
        registerClient();
        registerMisc();
    }

    private void registerCombat() {
        registerModule(CrystalAura.INSTANCE);
        registerModule(NoFriendDamage.INSTANCE);
        registerModule(ClickPearl.INSTANCE);
        registerModule(Velocity.INSTANCE);
        registerModule(Aura.INSTANCE);
        registerModule(AutoSwap.INSTANCE);
        registerModule(AimAssist.INSTANCE);
        registerModule(TriggerBot.INSTANCE);
        registerModule(AntiBot.INSTANCE);
        registerModule(AutoExplosion.INSTANCE);
        registerModule(FastCriticals.INSTANCE);
        registerModule(MaceSwap.INSTANCE);
        registerModule(AimBow.INSTANCE);
        registerModule(PearlTarget.INSTANCE);
        registerModule(AutoTotem.INSTANCE);
        registerModule(WebTrap.INSTANCE);
    }

    private void registerMovement() {
        registerModule(NoWeb.INSTANCE);
        registerModule(Flight.INSTANCE);
        registerModule(GrimGlide.INSTANCE);
        registerModule(AutoSprint.INSTANCE);
        registerModule(Timer.INSTANCE);
        registerModule(AirStuck.INSTANCE);
        registerModule(InventoryMove.INSTANCE);
        registerModule(ElytraRecast.INSTANCE);
        registerModule(ElytraMotion.INSTANCE);
        registerModule(NoFall.INSTANCE);
        registerModule(Speed.INSTANCE);
        registerModule(ElytraBooster.INSTANCE);
        registerModule(AntiElytraTarget.INSTANCE);
        registerModule(NoSlow.INSTANCE);
    }

    private void registerRender() {
        registerModule(TargetESP.INSTANCE);
        registerModule(FullBright.INSTANCE);
        registerModule(Hands.INSTANCE);
        registerModule(Predictions.INSTANCE);
        registerModule(ItemReplacer.INSTANCE);
        registerModule(Ambience.INSTANCE);
        registerModule(MotionBlur.INSTANCE);
        registerModule(EntityESP.INSTANCE);
        registerModule(ClientSounds.INSTANCE);
        registerModule(CustomSky.INSTANCE);
        registerModule(BlockOverlay.INSTANCE);
        registerModule(SwingAnimation.INSTANCE);
        registerModule(FireworkESP.INSTANCE);
        registerModule(Interface.INSTANCE);
        registerModule(KillEffect.INSTANCE);
        registerModule(AspectRatio.INSTANCE);
        registerModule(LootBeamsModule.INSTANCE);
        registerModule(NoRender.INSTANCE);
        registerModule(ViewModel.INSTANCE);
        registerModule(Particles.INSTANCE);
        registerModule(ShulkerPreview.INSTANCE);
        registerModule(HitMarker.INSTANCE);
        registerModule(HitEffect.INSTANCE);
        registerModule(GhostPlayer.INSTANCE);
        registerModule(JumpCircle.INSTANCE);
        registerModule(LineGlyphes.INSTANCE);
        registerModule(Trail.INSTANCE);
        registerModule(AntiInvisible.INSTANCE);
        registerModule(MasEffectsModule.INSTANCE);
        registerModule(Arrows.INSTANCE);
        registerModule(Svetych.INSTANCE);
        registerModule(Sonar.INSTANCE);
        registerModule(HitBubbles.INSTANCE);
        registerModule(WardenBoost.INSTANCE);
        registerModule(Menu.INSTANCE);
    }

    private void registerPlayer() {
        registerModule(SPJoiner.INSTANCE);
        registerModule(AutoEat.INSTANCE);
        registerModule(AntiThorns.INSTANCE);
        registerModule(AutoArmor.INSTANCE);
        registerModule(FastUse.INSTANCE);
        registerModule(NoDelay.INSTANCE);
        registerModule(AutoPotion.INSTANCE);
        registerModule(NoPush.INSTANCE);
        registerModule(AutoTool.INSTANCE);
        registerModule(AutoFish.INSTANCE);
        registerModule(ItemAim.INSTANCE);
        registerModule(HelpMessage.INSTANCE);
        registerModule(NoClip.INSTANCE);
    }

    private void registerClient() {
    }

    private void registerMisc() {
        registerModule(AutoDupe.INSTANCE);
        registerModule(FreeCam.INSTANCE);
        registerModule(ElytraHelper.INSTANCE);
        registerModule(NameProtect.INSTANCE);
        registerModule(SRPSpoofer.INSTANCE);
        registerModule(AutoAccept.INSTANCE);
        registerModule(ItemScroller.INSTANCE);
        registerModule(LonyGriefHelper.INSTANCE);
        registerModule(NoInteract.INSTANCE);
        registerModule(ScoreboardHealth.INSTANCE);
        registerModule(ServerHelper.INSTANCE);
        registerModule(AutoRespawn.INSTANCE);
        registerModule(HolyWorldHelper.INSTANCE);
        registerModule(FTHelper.INSTANCE);
        registerModule(LeaveTracker.INSTANCE);
        registerModule(FakePlayer.INSTANCE);
        registerModule(AutoWarden.INSTANCE);
        registerModule(AuctionHelper.INSTANCE);
        registerModule(UseTracker.INSTANCE);
    }

    private void registerModule(Module module) {
        modules.add(module);
        modulesByName.put(module.getName().toLowerCase(Locale.ROOT), module);

        // Settings are immutable after module construction. Flatten their animation
        // tracks once so the HUD frame does not repeatedly walk every setting and
        // perform the same instanceof chain.
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting booleanSetting) {
                animatedBooleans.add(booleanSetting);
            } else if (setting instanceof ModeSetting modeSetting) {
                animatedModeValues.addAll(modeSetting.getValues());
            } else if (setting instanceof MultiBooleanSetting multiBooleanSetting) {
                animatedMultiValues.addAll(multiBooleanSetting.getBooleanSettings());
            }
        }
    }

    public Module getModule(String name) {
        return name == null ? null : modulesByName.get(name.toLowerCase(Locale.ROOT));
    }

    public Set<Module> getActiveModules() {
        Set<Module> active = new HashSet<>();
        for (Module module : modules) {
            if (module.isEnabled()) {
                active.add(module);
            }
        }
        return active;
    }

    @EventTarget
    public void onKey(EventKey event) {
        if (mc.currentScreen == null && event.getAction() == 1) {
            int keyCode = event.getKeyCode();

            for (Module module : modules) {
                if (module.getKeyCode() == keyCode && module.getKeyCode() != -1) {
                    module.toggle();
                }

                for (Setting setting : module.getSettings()) {
                    if (setting instanceof BooleanSetting booleanSetting) {
                        if (booleanSetting.getKeyCode() == keyCode && booleanSetting.getKeyCode() != -1) {
                            booleanSetting.toggle();
                        }
                    }
                }
            }

            if (mc.getNetworkHandler() != null) {
                for (Macro macro : Wyvern.getInstance().getMacroManager().getItems()) {
                    if (keyCode == macro.getBind()) {
                        String text = macro.getText();
                        // Серверные команды нужно слать пакетом команды, иначе они
                        // уходят обычным сообщением в чат и на большинстве серверов не срабатывают.
                        if (text.startsWith("/")) {
                            mc.getNetworkHandler().sendCommand(text.substring(1));
                        } else {
                            mc.getNetworkHandler().sendChatMessage(text);
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onRender(EventHudRender e) {
        Wyvern.getInstance().getThemeManager().getCurrentTheme().getAnimation().update(1.0F);

        for (Module module : modules) {
            module.getAnimation().update(module.isEnabled());
        }

        for (BooleanSetting setting : animatedBooleans) {
            setting.getAnimation().update(setting.isEnabled());
        }
        for (ModeSetting.Value value : animatedModeValues) {
            value.getAnimation().update(value.isSelected());
        }
        for (MultiBooleanSetting.Value value : animatedMultiValues) {
            value.getAnimation().update(value.isEnabled());
        }

        MenuScreen menuScreen = Wyvern.getInstance().getMenuScreen();
        if (menuScreen.needToClose) {
            if (menuScreen.savedRunnable != null) {
                menuScreen.savedRunnable.run();
            }

            if (menuScreen.openAnimationMetanoise.getValue() <= 0.27F) {
                menuScreen.savedRunnable = null;
                menuScreen.needToClose = false;
                menuScreen.openAnimationMetanoise.setValue(0.0F);
                menuScreen.openAnimationMetanoise.setStartValue(0.0F);
            }
        }
    }

    @EventTarget
    private void onPacket(EventPacket e) {
        if (e.getPacket() instanceof CloseScreenS2CPacket && mc.currentScreen instanceof MenuScreen) {
            e.cancel();
        }
    }

    @EventTarget
    private void onGameUpdate(EventGameUpdate e) {
        if (mc.player == null) return;

        if (!Aura.INSTANCE.isEnabled()
                || Aura.INSTANCE.getTarget() == null
                || Aura.INSTANCE.rotationMode.is("None")) {
            float cameraYaw = mc.gameRenderer.getCamera().getYaw();
            float cameraPitch = mc.gameRenderer.getCamera().getPitch();

            if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT) {
                Aura.INSTANCE.lastYaw = cameraYaw - 180.0F;
                Aura.INSTANCE.lastPitch = -cameraPitch;
            } else {
                Aura.INSTANCE.lastYaw = cameraYaw;
                Aura.INSTANCE.lastPitch = cameraPitch;
            }

            Rotation current = new Rotation(mc.player.getYaw(), mc.player.getPitch());
            float deltaYaw = MathHelper.wrapDegrees(cameraYaw - current.getYaw());
            float deltaPitch = cameraPitch - current.getPitch();

            if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT) {
                deltaYaw = MathHelper.wrapDegrees(cameraYaw - 180.0F - current.getYaw());
                deltaPitch = -cameraPitch - current.getPitch();
            }

            acceleration += 0.0024F;
            float returnProgress = MathHelper.clamp(acceleration, 0.0F, 1.0F);
            float smooth = returnProgress * returnProgress * (3.0F - 2.0F * returnProgress);
            float newYaw = current.getYaw() + deltaYaw * smooth;
            float newPitch = current.getPitch() + deltaPitch * (smooth / 2.0F);

            Rotation smoothRot = new Rotation(newYaw, newPitch);
            RotationComponent.update(smoothRot, 360.0F, 360.0F, 360.0F, 360.0F, 0, 0, false);
        } else {
            acceleration = 0.0F;
        }
    }

    public List<Module> getModules() {
        return modules;
    }

    public boolean isBack() {
        return isBack;
    }

    public boolean isRotated() {
        return isRotated;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setBack(boolean isBack) {
        this.isBack = isBack;
    }

    public void setRotated(boolean isRotated) {
        this.isRotated = isRotated;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
}
