 package su.sacura.features.modules.impl.render;
 
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.features.modules.settings.impl.ModeListSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "No Overlay", category = Category.RENDER)
 public class NoOverlayModule extends Module {
   public ModeListSetting delete = (new ModeListSetting("Убирать", new BooleanSetting[] { new BooleanSetting("Тряску камеры", 
           Boolean.valueOf(true)), new BooleanSetting("Огонь на экране", 
           Boolean.valueOf(true)), new BooleanSetting("Воду на экране", 
           Boolean.valueOf(true)), new BooleanSetting("Плохие эффекты", 
           Boolean.valueOf(true)), new BooleanSetting("Тень", 
           Boolean.valueOf(true)), new BooleanSetting("Скорборд", 
           Boolean.valueOf(true)) })).setDescription("Убирает выбранные элементы");
   
   public NoOverlayModule() {
     addSettings(new ISetting[] { (ISetting)this.delete });
   }
 }


