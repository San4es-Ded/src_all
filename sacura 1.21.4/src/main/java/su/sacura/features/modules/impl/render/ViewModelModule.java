 package su.sacura.features.modules.impl.render;
 
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "View Model", category = Category.RENDER)
 public class ViewModelModule extends Module {
   public final SliderSetting right_x = (new SliderSetting("Правая рука X", 0.0F, -2.0F, 2.0F, 0.1F)).setDescription("Двигает правую руку по оси X");
   
   public final SliderSetting right_y = (new SliderSetting("Правая рука Y", 0.0F, -2.0F, 2.0F, 0.1F)).setDescription("Двигает правую руку по оси Y");
   
   public final SliderSetting right_z = (new SliderSetting("Правая рука Z", 0.0F, -2.0F, 2.0F, 0.1F)).setDescription("Двигает правую руку по оси Z");
   
   public final SliderSetting left_x = (new SliderSetting("Левая рука X", 0.0F, -2.0F, 2.0F, 0.1F)).setDescription("Двигает левую руку по оси X");
   
   public final SliderSetting left_y = (new SliderSetting("Левая рука Y", 0.0F, -2.0F, 2.0F, 0.1F)).setDescription("Двигает левую руку по оси Y");
   
   public final SliderSetting left_z = (new SliderSetting("Левая рука Z", 0.0F, -2.0F, 2.0F, 0.1F)).setDescription("Двигает левую руку по оси Z");
   
   public ViewModelModule() {
     addSettings(new ISetting[] { (ISetting)this.right_x, (ISetting)this.right_y, (ISetting)this.right_z, (ISetting)this.left_x, (ISetting)this.left_y, (ISetting)this.left_z });
   }
 }


