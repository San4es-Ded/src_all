package ru.prism.module.api;

import ru.prism.Client;

import ru.prism.manager.event_impl.EventKey;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.impl.display.ClickGui;
import ru.prism.module.impl.display.Hud;

import ru.prism.module.impl.display.InterFace;
import ru.prism.module.impl.render.*;
import ru.prism.module.impl.utils.*;

import ru.prism.module.impl.render.*;
import ru.prism.module.impl.utils.*;


import java.util.*;
import java.util.stream.Collectors;


public final class ModuleManager extends LinkedHashMap<Class<? extends Module>, Module> {


    
    public void init() {

        addSorted(

                new Sprint(),

                new ClickGui(),
                new NoRender(),
                new SwingAnimation(),
                new HMI(),
                new Animations(),
                new TabCustomizer(),
                new GlassHands(),
                new GlassBlock(),
                new ShaderSky(),
                new WorldTweaks(),
                new Gamma(),
                new AspectRatio(),
                new Particles(),
                new ChinaHat(),
                new Nimb(),
                new SelfNametag(),
                new LyricsText(),
                new TntTimer(),
                new DeathMarker(),
                new Cosmetics(),
                new JumpCircle(),
                new CrossHair(),
                new TargetEsp(),
                new Trails(),
                new Trajectories(),
                new FtHelper(),
                new Lumen(),
                new FogBlur(),
                new TotemGhost(),
                new ShulkerPreview(),
                new HitboxCustomizer(),
                new HitColor(),
                new ItemPhysics(),
                new KillEffect(),
                new Hands(),
                new InterFace(),

                new AuctionHelper(),
                new PvpSafe(),
                new MaceHelper(),
                new ShiftTab(),

                new ItemScroller(),
                new FastSwap(),
                new AutoPotion(),
                new AutoEat(),
                new FastExp(),
                new ItemSwap(),
                new ElytraSwap(),
                new LockSlot(),
                new ChestSorter(),
                new TapeMouse(),
                new AutoInvest(),
                new FakePlayer(),
                new FreeLook(),
                new Zoom(),
                new StreamerMode(),
                new ChatHelper(),
                new CommandSafe(),
                new SoundController(),
                new AutoRespawn(),
                new AutoReconnect(),
                new AutoLeave(),
                new Optimization()

        );

        this.values().stream()
                .filter(Module::isAutoEnabled)
                .forEach(module -> module.setEnabled(true, false));

        Client.eventHandler().subscribe(this);
    }

    public void addSorted(Module... modules) {
        Arrays.stream(modules)
                .forEach(module -> this.put(module.getClass(), module));
    }

    public void unregister(Module... modules) {
        Arrays.stream(modules).forEach(module -> this.remove(module.getClass()));
    }

    @EventHandler
    public void onKeyboardPress(EventKey event) {
            this.values().stream()
                    .filter(module -> module.getKey() == event.getKey())
                    //.filter(module -> !(module instanceof ClickGui))
                    .forEach(Module::toggle);
    }



    public <T extends Module> T get(final String name) {
        return this.values().stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .map(module -> (T) module)
                .findFirst()
                .orElse(null);
    }


    public <T extends Module> T get(final Class<T> clazz) {
        return this.values().stream()
                .filter(module -> clazz.isAssignableFrom(module.getClass()))
                .map(clazz::cast)
                .findFirst()
                .orElse(null);
    }


    public List<Module> get(final Category category) {
        return this.values().stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }


    public Module getModule(String name) {
        return this.values().stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Module> values() {
        return super.values().stream()
                .sorted(Comparator.comparing(Module::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }
}
