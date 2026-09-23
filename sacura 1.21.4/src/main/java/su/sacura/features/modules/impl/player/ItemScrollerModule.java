 package su.sacura.features.modules.impl.player;
 
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Item Scroller", category = Category.PLAYER)
 public class ItemScrollerModule extends Module {
   public SliderSetting speed = (new SliderSetting("Задержка", 100.0F, 1.0F, 100.0F, 1.0F)).setDescription("Устанавливает задержку на перетаскивание");
   
   public ItemScrollerModule() {
     addSettings(new ISetting[] { (ISetting)this.speed });
   }
 }


