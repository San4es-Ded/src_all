 package su.sacura.features.modules.impl.render;
 
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.util.impl.math.api.Animation;
 import su.sacura.util.impl.math.api.Easing;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Better Minecraft", category = Category.RENDER)
 public class BetterMinecraftModule extends Module {
   public final BooleanSetting smoothTab = (new BooleanSetting("Плавный таб", Boolean.valueOf(true))).setDescription("Анимация скольжения таба");
   
   public final SliderSetting speedTab = (new SliderSetting("Скорость", 250.0F, 1.0F, 1000.0F, 1.0F)).setDescription("Скорость скольжения таба").setVisible(() -> (Boolean)this.smoothTab.get());
   
   public final BooleanSetting smoothThirdPerson = (new BooleanSetting("Плавная перспективы", Boolean.valueOf(true))).setDescription("Анимация интерполяции перспективы");
   
   public final SliderSetting speedThirdPerson = (new SliderSetting("Скорость", 600.0F, 1.0F, 1000.0F, 1.0F)).setDescription("Скорость интереполяции перспективы").setVisible(() -> (Boolean)this.smoothThirdPerson.get());
   
   public final BooleanSetting smoothHotBar = (new BooleanSetting("Плавный хотбар", Boolean.valueOf(true))).setDescription("Анимация скольжения хотбара");
   
   public final SliderSetting speedHotBar = (new SliderSetting("Скорость", 0.05F, 0.01F, 1.0F, 0.01F)).setDescription("Скорость скольжения хотбара").setVisible(() -> (Boolean)this.smoothHotBar.get());
   
   public final BooleanSetting chatImprove = (new BooleanSetting("Красивый чат", Boolean.valueOf(true))).setDescription("Обрезает фон чата до ширины текста");
   
   public final BooleanSetting chatAnimation = (new BooleanSetting("Анимация чата", Boolean.valueOf(true))).setDescription("Новое сообщение выдвигается из угла");
   
   public final BooleanSetting improvedScoreboard = (new BooleanSetting("Улучшенный скорборд", Boolean.valueOf(true))).setDescription("Закругленные края у скорборда");
   
   private final Animation tabOpenAnimation = new Animation(((Float)this.speedTab.get()).intValue(), 1.0D, false, Easing.BOTH_SINE);
   
   public Animation getTabOpenAnimation() {
     return this.tabOpenAnimation;
   }
   
   private final Animation thirdPersonAnimation = new Animation(((Float)this.speedThirdPerson.get()).intValue(), 1.0D, false, Easing.BOTH_SINE);
   
   public Animation getThirdPersonAnimation() {
     return this.thirdPersonAnimation;
   }
   
   private boolean tabPressed = false;
   
   public boolean isTabPressed() {
     return this.tabPressed;
   }
   
   public void setTabPressed(boolean tabPressed) {
     this.tabPressed = tabPressed;
   }
   
   public BetterMinecraftModule() {
     addSettings(new ISetting[] { (ISetting)this.smoothTab, (ISetting)this.speedTab, (ISetting)this.smoothThirdPerson, (ISetting)this.speedThirdPerson, (ISetting)this.smoothHotBar, (ISetting)this.speedHotBar, (ISetting)this.chatImprove, (ISetting)this.chatAnimation, (ISetting)this.improvedScoreboard });
   }
 }


