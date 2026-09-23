 package su.sacura.features.modules.api.core;
 
 import com.google.common.eventbus.Subscribe;
 import java.util.ArrayList;
 import java.util.List;
 import su.sacura.Sacura;
 import su.sacura.events.input.KeyEvent;
 import su.sacura.features.modules.api.bind.Bind;
 import su.sacura.features.modules.impl.combat.ShiftTapModule;
 import su.sacura.features.modules.impl.display.ClientSoundsModule;
 import su.sacura.features.modules.impl.display.InterfaceModule;
 import su.sacura.features.modules.impl.movement.AutoSprintModule;
 import su.sacura.features.modules.impl.movement.FreeLookModule;
 import su.sacura.features.modules.impl.player.AutoDuelModule;
 import su.sacura.features.modules.impl.player.AutoMessageModule;
 import su.sacura.features.modules.impl.player.AutoRespawnModule;
 import su.sacura.features.modules.impl.player.DeathCoordsModule;
 import su.sacura.features.modules.impl.player.ElytraHelperModule;
 import su.sacura.features.modules.impl.player.FuntimeHelper;
 import su.sacura.features.modules.impl.player.ItemReleaseModule;
 import su.sacura.features.modules.impl.player.ItemScrollerModule;
 import su.sacura.features.modules.impl.player.NameProtectModule;
 import su.sacura.features.modules.impl.player.NoDelayModule;
 import su.sacura.features.modules.impl.player.RWHelperModule;
 import su.sacura.features.modules.impl.render.AspectRatioModule;
 import su.sacura.features.modules.impl.render.BetterMinecraftModule;
 import su.sacura.features.modules.impl.render.BlockHighLightModule;
 import su.sacura.features.modules.impl.render.CustomHitBoxModule;
 import su.sacura.features.modules.impl.render.DeathEffectModule;
 import su.sacura.features.modules.impl.render.FullBrightModule;
 import su.sacura.features.modules.impl.render.HitColorModule;
 import su.sacura.features.modules.impl.render.ItemPhysicModule;
 import su.sacura.features.modules.impl.render.JumpCirclesModule;
 import su.sacura.features.modules.impl.render.NoOverlayModule;
 import su.sacura.features.modules.impl.render.ParticlesModule;
 import su.sacura.features.modules.impl.render.SwingAnimationsModule;
 import su.sacura.features.modules.impl.render.TargetESPModule;
 import su.sacura.features.modules.impl.render.TrailsModule;
 import su.sacura.features.modules.impl.render.ViewModelModule;
 import su.sacura.features.modules.impl.render.WorldTweaksModule;
 
 public class ModuleManager {
   public List<Module> module = new ArrayList<>();
   
   public void init() {
     this.module.add(new AspectRatioModule());
     this.module.add(new TrailsModule());
     this.module.add(new FullBrightModule());
     this.module.add(new ItemPhysicModule());
     this.module.add(new ViewModelModule());
     this.module.add(new SwingAnimationsModule());
     this.module.add(new JumpCirclesModule());
     this.module.add(new NoOverlayModule());
     this.module.add(new ParticlesModule());
     this.module.add(new TargetESPModule());
     this.module.add(new WorldTweaksModule());
     this.module.add(new HitColorModule());
     this.module.add(new CustomHitBoxModule());
     this.module.add(new DeathEffectModule());
     this.module.add(new BetterMinecraftModule());
     this.module.add(new BlockHighLightModule());
     this.module.add(new AutoSprintModule());
     this.module.add(new FreeLookModule());
     this.module.add(new ShiftTapModule());
     this.module.add(new AutoRespawnModule());
     this.module.add(new ItemScrollerModule());
     this.module.add(new DeathCoordsModule());
     this.module.add(new AutoDuelModule());
     this.module.add(new NameProtectModule());
     this.module.add(new FuntimeHelper());
     this.module.add(new ElytraHelperModule());
     this.module.add(new RWHelperModule());
     this.module.add(new ItemReleaseModule());
     this.module.add(new AutoMessageModule());
     this.module.add(new NoDelayModule());
     this.module.add(new InterfaceModule());
     this.module.add(new ClientSoundsModule());
     Sacura.getInstance().getEventBus().register(this);
   }
   
   public <T> T getModule(Class<T> clazz) {
     for (Module module : this.module) {
       if (module.getClass() == clazz)
         return (T)module; 
     } 
     return null;
   }
   
   @Subscribe
   public void event(KeyEvent event) {
     if (event.screen() != null)
       return; 
     for (Module m : this.module) {
       if (m.bind.getKey() == event.key()) {
         if (m.bind.getMode() == Bind.BindMode.TOGGLE && event.action() == 1) {
           m.toggle();
           continue;
         } 
         if (m.bind.getMode() == Bind.BindMode.HOLD) {
           if (event.action() == 1) {
             m.onEnable();
             continue;
           } 
           if (event.action() == 0)
             m.onDisable(); 
         } 
       } 
     } 
   }
 }


