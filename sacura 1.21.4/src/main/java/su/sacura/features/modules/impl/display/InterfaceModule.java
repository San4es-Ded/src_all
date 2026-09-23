 package su.sacura.features.modules.impl.display;
 
 import com.google.common.eventbus.Subscribe;
 import su.sacura.display.overlay.KeybindRender;
 import su.sacura.display.overlay.WatermarkRender;
 import su.sacura.events.render.DrawEvent;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.features.modules.settings.impl.ModeListSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Interface", category = Category.DISPLAY)
 public class InterfaceModule extends Module {
   ModeListSetting select = (new ModeListSetting("Выберите элементы худа", new BooleanSetting[] { new BooleanSetting("Watermark", 
           Boolean.valueOf(true)), new BooleanSetting("Keybinds", 
           Boolean.valueOf(true)), new BooleanSetting("Potions", 
           Boolean.valueOf(true)), new BooleanSetting("Music", 
           Boolean.valueOf(true)), new BooleanSetting("Targethud", 
           Boolean.valueOf(true)), new BooleanSetting("Information", 
           Boolean.valueOf(true)) })).setDescription("Отображает выбранную информацию");
   
   ModeListSetting watermarkSelect = (new ModeListSetting("Элементы ватермарки", new BooleanSetting[] { new BooleanSetting("Логотип", 
           Boolean.valueOf(true)), new BooleanSetting("Никнейм", 
           Boolean.valueOf(true)), new BooleanSetting("Кадры в секунду", 
           Boolean.valueOf(true)), new BooleanSetting("Латенси", 
           Boolean.valueOf(true)), new BooleanSetting("Скорость", 
           Boolean.valueOf(true)), new BooleanSetting("Тики сервера", 
           Boolean.valueOf(true)) })).setVisible(() -> (Boolean)this.select.getValueByName("Watermark").get()).setDescription("Отображает выбранные элементы");
   
   public InterfaceModule() {
     addSettings(new ISetting[] { (ISetting)this.select, (ISetting)this.watermarkSelect });
     toggle();
   }
   
   @Subscribe
   public void onRender2D(DrawEvent e) {
     int w = sr.getWidth();
     int h = sr.getHeight();
     if (((Boolean)this.select.getValueByName("Watermark").get()).booleanValue())
       WatermarkRender.render(e.getDrawContext(), w, h, this.watermarkSelect); 
     if (((Boolean)this.select.getValueByName("Keybinds").get()).booleanValue())
       KeybindRender.render(e.getDrawContext(), w, h); 
   }
 }


