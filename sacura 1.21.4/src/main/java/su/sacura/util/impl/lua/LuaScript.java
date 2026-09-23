 package su.sacura.util.impl.lua;
 
 import java.io.File;
 import org.luaj.vm2.Globals;
 import org.luaj.vm2.LuaValue;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.impl.Category;
 
 public class LuaScript extends Module {
   private final File scriptFile;
   
   private final Globals globals;
   
   private LuaValue chunk;
   
   private LuaValue onEnableFunc;
   
   private LuaValue onDisableFunc;
   
   private LuaValue onUpdateFunc;
   
   public Globals getGlobals() {
     return this.globals;
   }
   
   public LuaScript(String name, String desc, Category category, File scriptFile, Globals globals) {
     super(name, desc, category);
     this.scriptFile = scriptFile;
     this.globals = globals;
   }
   
   public void load() {
     reload();
   }
   
   public void reload() {
     try {
       this.chunk = this.globals.loadfile(this.scriptFile.getAbsolutePath());
       this.chunk.call();
       this.onEnableFunc = this.globals.get("onEnable");
       this.onDisableFunc = this.globals.get("onDisable");
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   public void onEnable() {
     super.onEnable();
     if (this.onEnableFunc != null && !this.onEnableFunc.isnil())
       try {
         this.onEnableFunc.call();
       } catch (Exception e) {
         e.printStackTrace();
       }  
   }
   
   public void onDisable() {
     super.onDisable();
     if (this.onDisableFunc != null && !this.onDisableFunc.isnil())
       try {
         this.onDisableFunc.call();
       } catch (Exception e) {
         e.printStackTrace();
       }  
   }
 }


