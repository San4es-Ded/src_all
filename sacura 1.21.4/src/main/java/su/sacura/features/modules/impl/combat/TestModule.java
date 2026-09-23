 package su.sacura.features.modules.impl.combat;
 
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.BindSetting;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.features.modules.settings.impl.ColorSetting;
 import su.sacura.features.modules.settings.impl.ModeListSetting;
 import su.sacura.features.modules.settings.impl.ModeSetting;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.features.modules.settings.impl.StringSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Test Module", category = Category.MOVEMENT)
 public class TestModule extends Module {
   BooleanSetting testBoolean = (new BooleanSetting("Test Boolean", Boolean.valueOf(false))).setDescription("This test checkbox");
   
   SliderSetting testSlider = (new SliderSetting("Test Slider", 30.0F, 10.0F, 100.0F, 1.0F)).setDescription("This test slider");
   
   ModeSetting testMode = (new ModeSetting("Test Mode", "chuppachups", new String[] { "velttp", "chuppachups", "administrata" })).setDescription("This test mode");
   
   ModeListSetting testModeList = (new ModeListSetting("Test Mode List", new BooleanSetting[] { new BooleanSetting("chuppachups", 
           Boolean.valueOf(true)), new BooleanSetting("velttp", 
           Boolean.valueOf(true)), new BooleanSetting("administrata", 
           Boolean.valueOf(false)), new BooleanSetting("pexi", 
           Boolean.valueOf(false)), new BooleanSetting("violence", 
           Boolean.valueOf(true)) })).setDescription("This test mode list");
   
   ColorSetting colorTest = (new ColorSetting("Color penis", Integer.valueOf(-1))).setDescription("This test color");
   
   BindSetting bindTest = (new BindSetting("Key pidor", Integer.valueOf(-1))).setDescription("Кнопка чтобы стать пидором!");
   
   StringSetting stringTest = (new StringSetting("Напиши если не пидор", "sacuraproject")).setDescription("Надо плакать!");
   
   public TestModule() {
     addSettings(new ISetting[] { (ISetting)this.testBoolean, (ISetting)this.testSlider, (ISetting)this.testMode, (ISetting)this.testModeList, (ISetting)this.colorTest, (ISetting)this.bindTest, (ISetting)this.stringTest });
   }
 }


