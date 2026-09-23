 package su.sacura.features.modules.api.core;
 
 import it.unimi.dsi.fastutil.objects.ObjectArrayList;
 import java.util.List;
 import su.sacura.Sacura;
 import su.sacura.features.modules.api.bind.Bind;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.impl.display.ClientSoundsModule;
 import su.sacura.util.impl.system.AudioUtil;
 import su.sacura.util.type.ISetting;
 import su.sacura.util.type.MinecraftWrapper;
 
 public class Module implements MinecraftWrapper {
   public String name;
   
   public Bind bind;
   
   public boolean enable;
   
   public boolean extended;
   
   public Category category;
   
   public final String desc;
   
   public float animAlpha;
   
   public final List<ISetting> settings = (List<ISetting>)new ObjectArrayList();
   
   public Module() {
     ModuleAnnotations moduleAnnotations = getClass().<ModuleAnnotations>getAnnotation(ModuleAnnotations.class);
     if (moduleAnnotations == null)
       throw new RuntimeException("ModuleAnnotations = null in " + getClass().getName()); 
     this.name = moduleAnnotations.name();
     this.category = moduleAnnotations.category();
     this.desc = moduleAnnotations.desc();
     this.bind = new Bind(-1, Bind.BindMode.TOGGLE);
     this.enable = false;
     this.extended = false;
   }
   
   public Module(String name, String desc, Category category) {
     this.name = name;
     this.desc = desc;
     this.category = category;
     this.bind = new Bind(-1, Bind.BindMode.TOGGLE);
     this.enable = false;
     this.extended = false;
   }
   
   public void addSettings(ISetting... settings) {
     this.settings.addAll(List.of(settings));
   }
   
   public List<ISetting> getSettings() {
     return this.settings;
   }
   
   public void onEnable() {
     this.enable = true;
     Sacura.getInstance().getEventBus().register(this);
   }
   
   public void onDisable() {
     this.enable = false;
     Sacura.getInstance().getEventBus().unregister(this);
   }
   
   public void toggle() {
     if (this.enable) {
       onDisable();
       playSound(false);
     } else {
       onEnable();
       playSound(true);
     } 
   }
   
   private void playSound(boolean enable) {
     String soundFile;
     ModuleManager manager = Sacura.getInstance().getModuleManager();
     if (manager == null)
       return; 
     ClientSoundsModule clientSounds = manager.<ClientSoundsModule>getModule(ClientSoundsModule.class);
     if (clientSounds == null)
       return; 
     if (!clientSounds.enable)
       return; 
     switch ((String)clientSounds.mode.get()) {
       case "Nursultan":
         soundFile = enable ? "nuron.wav" : "nuroff.wav";
         break;
       case "Akrien":
         soundFile = enable ? "akron.wav" : "akroff.wav";
         break;
       case "Celestial":
         soundFile = enable ? "celon.wav" : "celoff.wav";
         break;
       case "Sacura":
         soundFile = enable ? "tone.wav" : "toned.wav";
         break;
       default:
         return;
     } 
     AudioUtil.playSound(soundFile);
   }
 }


