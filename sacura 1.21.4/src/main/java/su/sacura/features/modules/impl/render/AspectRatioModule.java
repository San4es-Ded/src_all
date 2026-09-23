 package su.sacura.features.modules.impl.render;
 
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.ModeSetting;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Aspect Ratio", category = Category.RENDER, desc = "Позволяет изменять соотношение сторон экрана")
 public class AspectRatioModule extends Module {
   public final ModeSetting mods = (new ModeSetting("Режим", "16:9", new String[] { "4:3", "16:9", "1:1", "16:10", "Кастомный" })).setDescription("Растягивает выш экран");
   
   public final SliderSetting slider = (new SliderSetting("Соотношение", 1.8F, 0.1F, 5.0F, 0.1F)).setVisible(() -> Boolean.valueOf(this.mods.is("Кастомный"))).setDescription("Кастомное соотношение");
   
   public AspectRatioModule() {
     addSettings(new ISetting[] { (ISetting)this.mods, (ISetting)this.slider });
   }
 }


