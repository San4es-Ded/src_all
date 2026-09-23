 package su.sacura.features.modules.impl.render;
 
 import com.google.common.eventbus.Subscribe;
 import su.sacura.events.render.FogEvent;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.features.modules.settings.impl.ModeListSetting;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.util.impl.render.providers.ColorProvider;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "World Tweaks", category = Category.RENDER)
 public class WorldTweaksModule extends Module {
   public final ModeListSetting world = (new ModeListSetting("Настройки мира", new BooleanSetting[] { new BooleanSetting("Время", 
           Boolean.valueOf(true)), new BooleanSetting("Туман", 
           Boolean.valueOf(false)) })).setDescription("Позволяет настроить мир");
   
   public final SliderSetting time = (new SliderSetting("Время", 12.0F, 0.0F, 24.0F, 1.0F)).setVisible(() -> (Boolean)this.world.getValueByName("Время").get()).setDescription("Устанавливает значение времени");
   
   public final SliderSetting distance = (new SliderSetting("Дистанция тумана", 100.0F, 20.0F, 200.0F, 1.0F)).setVisible(() -> (Boolean)this.world.getValueByName("Туман").get()).setDescription("Устанавливает расстояние тумана");
   
   public WorldTweaksModule() {
     addSettings(new ISetting[] { (ISetting)this.world, (ISetting)this.time, (ISetting)this.distance });
   }
   
   @Subscribe
   public void onFog(FogEvent e) {
     if (((Boolean)this.world.getValueByName("Туман").get()).booleanValue()) {
       e.setDistance(((Float)this.distance.get()).floatValue());
       e.setColor(ColorProvider.getColorStyle(0.0F));
       e.cancel();
     } 
   }
 }


