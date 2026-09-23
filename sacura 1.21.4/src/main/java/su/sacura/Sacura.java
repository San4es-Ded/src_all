 package su.sacura;
 
 import com.google.common.eventbus.EventBus;
 import net.fabricmc.api.ModInitializer;
 import su.sacura.features.modules.api.core.ModuleManager;
 import su.sacura.util.impl.lua.LuaManager;
 
 public class Sacura implements ModInitializer {
   private static Sacura instance;
   
   private EventBus eventBus;
   
   private ModuleManager moduleManager;
   
   private LuaManager luaManager;
   
   public void onInitialize() {
     instance = this;
     this.eventBus = new EventBus();
     this.moduleManager = new ModuleManager();
     this.luaManager = new LuaManager();
     this.moduleManager.init();
     this.luaManager.init();
     this.eventBus.register(this);
     System.out.println("[SACURA] Client initialized");
   }
   
   public static Sacura getInstance() {
     return instance;
   }
   
   public EventBus getEventBus() {
     return this.eventBus;
   }
   
   public ModuleManager getModuleManager() {
     return this.moduleManager;
   }
   
   public LuaManager getLuaManager() {
     return this.luaManager;
   }
 }


