 package su.sacura.util.impl.lua;
 
 import com.google.common.eventbus.Subscribe;
 import java.io.File;
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.client.MinecraftClient;
 import net.minecraft.network.packet.Packet;
 import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
 import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
 import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
 import net.minecraft.text.Text;
 import org.luaj.vm2.Globals;
 import org.luaj.vm2.LuaValue;
 import org.luaj.vm2.lib.jse.JsePlatform;
 import su.sacura.Sacura;
 import su.sacura.events.packet.EventPacket;
 import su.sacura.events.player.EntityDeathEvent;
 import su.sacura.events.player.EventAttack;
 import su.sacura.events.render.DrawEvent;
 import su.sacura.events.render.EventRender3D;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.util.impl.lua.api.ClientApi;
 import su.sacura.util.impl.lua.api.GlobalApi;
 import su.sacura.util.impl.lua.api.ModulesApi;
 import su.sacura.util.impl.lua.api.PlayerApi;
 import su.sacura.util.impl.lua.api.Render3DApi;
 import su.sacura.util.impl.lua.api.RenderApi;
 import su.sacura.util.impl.lua.api.SettingsApi;
 import su.sacura.util.impl.lua.render.LuaFontManager;
 
 public class LuaManager {
   private final List<LuaScript> scripts = new ArrayList<>();
   
   private final File scriptsFolder;
   
   private final Globals globals;
   
   public LuaManager() {
     this.scriptsFolder = new File((MinecraftClient.getInstance()).runDirectory, "sacura/lua");
     if (!this.scriptsFolder.exists())
       this.scriptsFolder.mkdirs(); 
     this.globals = null;
     LuaFontManager.init();
   }
   
   public void init() {
     Sacura.getInstance().getEventBus().register(this);
     loadScripts();
   }
   
   @Subscribe
   public void onRender(DrawEvent event) {
     RenderApi.currentContext = event.getDrawContext();
     for (LuaScript script : this.scripts) {
       if (script.enable) {
         LuaValue onRender = script.getGlobals().get("onRender");
         if (onRender != null && !onRender.isnil())
           try {
             onRender.call();
           } catch (Exception e) {
             e.printStackTrace();
           }  
       } 
     } 
     RenderApi.updateInput();
     RenderApi.currentContext = null;
   }
   
   @Subscribe
   public void onRender3D(EventRender3D event) {
     for (LuaScript script : this.scripts) {
       if (script.enable) {
         LuaValue onRender3D = script.getGlobals().get("onRender3D");
         if (onRender3D != null && !onRender3D.isnil())
           try {
             onRender3D.call((LuaValue)LuaValue.valueOf(event.getDeltatick().getTickDelta(false)));
           } catch (Exception e) {
             e.printStackTrace();
           }  
       } 
     } 
   }
   
   @Subscribe
   public void onPacket(EventPacket event) {
     if (event.isSendPacket()) {
       Packet packet = event.getPacket();
       if (packet instanceof ChatMessageC2SPacket) {
         ChatMessageC2SPacket chatMessageC2SPacket = (ChatMessageC2SPacket)packet;
         String message = chatMessageC2SPacket.chatMessage();
         if (message.equalsIgnoreCase(".lua reload")) {
           event.isCancel = true;
           loadScripts();
           if ((MinecraftClient.getInstance()).player != null)
             (MinecraftClient.getInstance()).player.sendMessage(Text.of("§a[Lua] Scripts reloaded!"), false); 
         } 
       } else {
         packet = event.getPacket();
         if (packet instanceof PlayerInteractEntityC2SPacket) {
           PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = (PlayerInteractEntityC2SPacket)packet;
           for (LuaScript script : this.scripts) {
             if (script.enable) {
               LuaValue onAttack = script.getGlobals().get("onAttack");
               if (onAttack != null && !onAttack.isnil())
                 onAttack.call(); 
             } 
           } 
         } 
       } 
     } else {
       Packet packet = event.getPacket();
       if (packet instanceof GameMessageS2CPacket) {
         GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)packet;
         String text = gameMessageS2CPacket.content().getString();
         for (LuaScript script : this.scripts) {
           if (script.enable) {
             LuaValue onChat = script.getGlobals().get("onChat");
             if (onChat != null && !onChat.isnil())
               onChat.call((LuaValue)LuaValue.valueOf(text)); 
           } 
         } 
       } 
     } 
   }
   
   @Subscribe
   public void onAttack(EventAttack event) {
     for (LuaScript script : this.scripts) {
       if (script.enable) {
         LuaValue onAttack = script.getGlobals().get("onAttack");
         if (onAttack != null && !onAttack.isnil())
           try {
             onAttack.call((LuaValue)LuaValue.valueOf(event.getTarget().getName().getString()));
           } catch (Exception e) {
             e.printStackTrace();
           }  
       } 
     } 
   }
   
   @Subscribe
   public void onDeath(EntityDeathEvent event) {
     for (LuaScript script : this.scripts) {
       if (script.enable) {
         LuaValue onKill = script.getGlobals().get("onKill");
         if (onKill != null && !onKill.isnil())
           try {
             onKill.call((LuaValue)LuaValue.valueOf(event.getEntity().getName().getString()));
           } catch (Exception e) {
             e.printStackTrace();
           }  
       } 
     } 
   }
   
   public void loadScripts() {
     List<String> enabledScripts = new ArrayList<>();
     for (LuaScript script : this.scripts) {
       if (script.enable) {
         enabledScripts.add(script.name);
         script.toggle();
       } 
       (Sacura.getInstance().getModuleManager()).module.remove(script);
     } 
     this.scripts.clear();
     File[] files = this.scriptsFolder.listFiles((dir, name) -> name.endsWith(".lua"));
     if (files == null)
       return; 
     for (File file : files) {
       try {
         Globals scriptGlobals = JsePlatform.standardGlobals();
         (new GlobalApi()).register(scriptGlobals);
         (new RenderApi()).register(scriptGlobals);
         (new PlayerApi()).register(scriptGlobals);
         (new Render3DApi()).register(scriptGlobals);
         (new ClientApi()).register(scriptGlobals);
         (new ModulesApi()).register(scriptGlobals);
         String name = file.getName().replace(".lua", "");
         LuaScript script = new LuaScript(name, "Lua Script " + name, Category.LUA, file, scriptGlobals);
         (new SettingsApi(script)).register(scriptGlobals);
         script.load();
         if (enabledScripts.contains(name))
           script.toggle(); 
         this.scripts.add(script);
         (Sacura.getInstance().getModuleManager()).module.add(script);
       } catch (Exception e) {
         e.printStackTrace();
       } 
     } 
   }
 }


