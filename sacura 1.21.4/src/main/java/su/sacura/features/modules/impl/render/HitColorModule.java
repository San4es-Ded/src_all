 package su.sacura.features.modules.impl.render;
 
 import java.util.Objects;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.features.modules.settings.impl.ColorSetting;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Hit Color", category = Category.RENDER)
 public class HitColorModule extends Module {
   public final SliderSetting alpha = (new SliderSetting("Прозрачность", 0.8F, 0.1F, 1.0F, 0.05F)).setDescription("Прозрачность при ударе");
   
   public final BooleanSetting customColor = (new BooleanSetting("Свой цвет", Boolean.valueOf(false))).setDescription("Использовать свой цвет удара");
   
   public final ColorSetting hitColor;
   
   public HitColorModule() {
     Objects.requireNonNull(this.customColor);
     this.hitColor = (new ColorSetting("Цвет", Integer.valueOf(-1))).setVisible(this.customColor::get).setDescription("Цвет при ударе");
     addSettings(new ISetting[] { (ISetting)this.alpha, (ISetting)this.customColor, (ISetting)this.hitColor });
   }
 }


