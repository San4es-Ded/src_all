 package su.sacura.features.modules.impl.display;
 
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.ModeSetting;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Client Sounds", category = Category.DISPLAY)
 public class ClientSoundsModule extends Module {
   public final ModeSetting mode = (new ModeSetting("Мод звука", "Akrien", new String[] { "Nursultan", "Akrien", "Celestial", "Sacura" })).setDescription("Устанавливает звук при вкл/выкл модулей");
   
   public final SliderSetting volume = (new SliderSetting("Громкость", 100.0F, 1.0F, 100.0F, 1.0F)).setDescription("Устанавливает громкость звука");
   
   public ClientSoundsModule() {
     toggle();
     addSettings(new ISetting[] { (ISetting)this.mode, (ISetting)this.volume });
   }
 }


