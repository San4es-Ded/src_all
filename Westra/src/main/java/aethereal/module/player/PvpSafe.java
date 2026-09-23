package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AttackEvent;
import aethereal.event.ChatSendEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.setting.StringSetting;
import aethereal.util.ChatUtil;
import java.util.regex.Pattern;

@ModuleRegister(
   a = "Pvp Safe",
   b = "Блокирует опасные команды, пока вы в бою",
   c = Category.Player
)
public class PvpSafe extends Module {
   private static final Pattern b = Pattern.compile(
      "^/(spawn|home|hub|lobby|rtp|warp|back|logout|server|suicide|kill|auc|auction|trade|duel|pay|msg|tell|w)(\\s.*)?$", 2
   );
   private final SliderSetting c = new SliderSetting("Длительность боя", 15.0F, 3.0F, 60.0F, 1.0F);
   private final BooleanSetting d = new BooleanSetting("Сообщать о блокировке", true);
   private final StringSetting e = new StringSetting("Дополнительные команды", "");
   private long f;
   private float g = -1.0F;
   private long h;

   public PvpSafe() {
      this.a(new Setting[]{this.c, this.d, this.e});
   }

   @Override
   public void b() {
      super.b();
      this.f = 0L;
      this.g = -1.0F;
   }

   @EventTarget
   public void a(AttackEvent event) {
      this.f = System.currentTimeMillis();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null) {
         float health = aM_.field_1724.method_6032();
         if (this.g >= 0.0F && health < this.g) {
            this.f = System.currentTimeMillis();
         }

         this.g = health;
      }
   }

   @EventTarget
   public void a(ChatSendEvent event) {
      String content = event.b();
      if (content != null && content.startsWith("/") && this.q()) {
         String command = content.trim();
         if (b.matcher(command).matches() || this.a(command)) {
            event.a(true);
            if (this.d.c() && System.currentTimeMillis() - this.h > 3000L) {
               this.h = System.currentTimeMillis();
               ChatUtil.a("Команда заблокирована: вы в бою ещё " + this.r() + " с.");
            }
         }
      }
   }

   private boolean a(String command) {
      String extra = this.e.c();
      if (extra != null && !extra.isBlank()) {
         String lower = command.toLowerCase();

         for (String part : extra.split("[,;\\s]+")) {
            String candidate = part.trim().toLowerCase();
            if (!candidate.isEmpty()) {
               if (!candidate.startsWith("/")) {
                  candidate = "/" + candidate;
               }

               if (lower.equals(candidate) || lower.startsWith(candidate + " ")) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean q() {
      return this.f != 0L && System.currentTimeMillis() - this.f < (long)(this.c.c() * 1000.0F);
   }

   private long r() {
      long left = (long)(this.c.c() * 1000.0F) - (System.currentTimeMillis() - this.f);
      return Math.max(1L, left / 1000L);
   }
}
