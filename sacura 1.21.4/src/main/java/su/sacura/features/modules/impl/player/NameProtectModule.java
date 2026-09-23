 package su.sacura.features.modules.impl.player;
 
 import su.sacura.Sacura;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.StringSetting;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Name Protect", category = Category.PLAYER)
 public class NameProtectModule extends Module {
   public final StringSetting text = (new StringSetting("Никнейм", "t.me/sacuraproject")).setDescription("Напишите свой желанный никнейм");
   
   public NameProtectModule() {
     addSettings(new ISetting[] { (ISetting)this.text });
   }
   
   public String getCustomName() {
     return ((NameProtectModule)Sacura.getInstance().getModuleManager().getModule(NameProtectModule.class)).enable ? ((String)this.text.get()).replaceAll("&", "§") : mc.getGameProfile().getName();
   }
   
   public String getProtectedName(String originalName) {
     if (!((NameProtectModule)Sacura.getInstance().getModuleManager().getModule(NameProtectModule.class)).enable)
       return originalName; 
     if (isSelf(originalName))
       return applyFormatting((String)this.text.get()); 
     return originalName;
   }
   
   private String applyFormatting(String name) {
     return name.replace('&', '§');
   }
   
   private boolean isSelf(String name) {
     return name.equals(mc.getSession().getUsername());
   }
 }


