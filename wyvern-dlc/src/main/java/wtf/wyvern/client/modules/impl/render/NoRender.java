package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.List;
import wtf.wyvern.core.events.impl.render.EventCamera;
import wtf.wyvern.core.events.impl.server.EventPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "NoRender",
   category = Category.RENDER,
   description = "Убирает лишние элементы с экрана"
)
@FastNative
public final class NoRender extends Module {
   public static final NoRender INSTANCE = new NoRender();
   private final MultiBooleanSetting settings = MultiBooleanSetting.create("Убрать", List.of("Огонь", "Плохие эффекты", "Камера клип", "Тряска", "Затуманивание", "Частицы", "Тотемы", "Скорборд", "Тайтлы"));

   public boolean isRemoveFire() {
      return this.isEnabled() && this.settings.isEnable(0);
   }

   public boolean isRemoveBadEffect() {
      return this.isEnabled() && this.settings.isEnable(1);
   }

   public boolean isRemoveShake() {
      return this.isEnabled() && this.settings.isEnable("Тряска");
   }

   public boolean isRemoveParticles() {
      return this.isEnabled() && this.settings.isEnable("Частицы");
   }

   public boolean isRemoveFog() {
      return this.isEnabled() && this.settings.isEnable("Затуманивание");
   }

   public boolean isRemoveTotem() {
      return this.isEnabled() && this.settings.isEnable("Тотемы");
   }

   public boolean isRemoveScoreboard() {
      return this.isEnabled() && this.settings.isEnable("Скорборд");
   }

   public boolean isRemoveTitles() {
      return this.isEnabled() && this.settings.isEnable("Тайтлы");
   }

   @EventTarget
   private void onPacket(EventPacket event) {
      if (!event.isReceive() || mc.world == null) return;
      if (isRemoveParticles() && event.getPacket() instanceof ParticleS2CPacket) {
         event.cancel();
      } else if (isRemoveTotem() && event.getPacket() instanceof EntityStatusS2CPacket packet
              && packet.getStatus() == 35) {
         event.cancel();
      }
   }

   @EventTarget
   private void onCamera(EventCamera e) {
      e.setCameraClip(this.settings.isEnable("Камера клип"));
      e.cancel();
   }
}
