 package su.sacura.features.modules.impl.player;
 
 import com.google.common.eventbus.Subscribe;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.LivingEntity;
 import su.sacura.events.player.EventAttack;
 import su.sacura.events.tick.EventUpdate;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.features.modules.settings.impl.ModeSetting;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.features.modules.settings.impl.StringSetting;
 import su.sacura.util.impl.math.helper.TimerUtil;
 import su.sacura.util.type.ISetting;
 
 @ModuleAnnotations(name = "Auto Message", category = Category.PLAYER)
 public class AutoMessageModule extends Module {
   private final ModeSetting mode = (new ModeSetting("Отправлять", "После убийства", new String[] { "После убийства", "Во время таргета", "По задержке" })).setDescription("Отправляет сообщения выбранным модом");
   
   private final SliderSetting timer = (new SliderSetting("Задержка", 5000.0F, 0.0F, 35000.0F, 1000.0F)).setVisible(() -> Boolean.valueOf((this.mode.is("По задержке") || this.mode.is("Во время таргета")))).setDescription("Задержка перед отправкой сообщения");
   
   private final StringSetting text = (new StringSetting("Сообщение", "ezz %target%")).setDescription("Сообщение которое будет отправляться");
   
   private final TimerUtil delayTimer = new TimerUtil();
   
   private LivingEntity lastTarget;
   
   private boolean waitingForDeath = false;
   
   public AutoMessageModule() {
     addSettings(new ISetting[] { (ISetting)this.mode, (ISetting)this.timer, (ISetting)this.text });
   }
   
   @Subscribe
   public void event(EventAttack event) {
     if (mc.player == null || mc.world == null)
       return; 
     Entity entity = event.getTarget();
     if (entity instanceof LivingEntity) {
       LivingEntity livingEntity = (LivingEntity)entity;
       if (this.mode.is("После убийства")) {
         this.lastTarget = livingEntity;
         this.waitingForDeath = true;
       } 
       if (this.mode.is("Во время таргета"))
         this.lastTarget = livingEntity; 
     } 
   }
   
   @Subscribe
   public void event(EventUpdate e) {
     if (mc.player == null || mc.world == null)
       return; 
     if (this.mode.is("По задержке")) {
       if (this.delayTimer.hasTimeElapsed(((Float)this.timer.get()).longValue())) {
         sendMessage(replaceTarget((String)this.text.get(), (LivingEntity)null));
         this.delayTimer.reset();
       } 
       return;
     } 
     if (this.mode.is("Во время таргета")) {
       if (this.lastTarget != null && this.delayTimer.hasTimeElapsed(((Float)this.timer.get()).longValue())) {
         sendMessage(replaceTarget((String)this.text.get(), this.lastTarget));
         this.delayTimer.reset();
       } 
       return;
     } 
     if (this.mode.is("После убийства") && this.waitingForDeath && this.lastTarget != null) {
       boolean dead = (this.lastTarget.isDead() || this.lastTarget.getHealth() <= 0.0F);
       boolean unloaded = (mc.world.getEntityById(this.lastTarget.getId()) == null);
       if (dead || unloaded) {
         sendMessage(replaceTarget((String)this.text.get(), this.lastTarget));
         this.waitingForDeath = false;
         this.lastTarget = null;
         this.delayTimer.reset();
       } 
     } 
   }
   
   private void sendMessage(String msg) {
     if (msg == null || msg.trim().isEmpty())
       return; 
     if (mc.player != null)
       mc.player.networkHandler.sendChatMessage(msg); 
   }
   
   private String replaceTarget(String msg, LivingEntity target) {
     return msg.replace("%target%", (target != null) ? target.getName().getString() : "хряк");
   }
 }


