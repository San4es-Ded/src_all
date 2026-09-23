package pulse.modules.utilities;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import pulse.events.ClientTickEvent;
import pulse.events.PlayerDeathEvent;
import pulse.media.chat.ChatMessages;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.settings.BooleanSetting;
import pulse.settings.TokenSetting;
import pulse.settings.TokenSetting.TokenType;

@ModuleInfo(a = "Auto Respawn", b = "РђРІС‚РѕРјР°С‚РёС‡РµСЃРєРё РІРѕР·СЂРѕР¶РґР°РµС‚ РёРіСЂРѕРєР° РїРѕСЃР»Рµ СЃРјРµСЂС‚Рё", c = ModuleCategory.UTILITIES)
public class AutoRespawn extends ClientModule {
   private final BooleanSetting e = new BooleanSetting("РђРІС‚Рѕ РІРѕР·СЂРѕР¶РґРµРЅРёРµ", "РђРІС‚РѕРјР°С‚РёС‡РµСЃРєРё РІРѕР·СЂРѕР¶РґР°С‚СЊСЃСЏ РїРѕСЃР»Рµ СЃРјРµСЂС‚Рё", true);
   private final BooleanSetting f = new BooleanSetting("РћС‚РїСЂР°РІР»СЏС‚СЊ РєРѕРјР°РЅРґСѓ РїРѕСЃР»Рµ РІРѕР·СЂРѕР¶РґРµРЅРёСЏ", "РћС‚РїСЂР°РІР»СЏС‚СЊ РєРѕРјР°РЅРґСѓ РїРѕСЃР»Рµ РІРѕР·СЂРѕР¶РґРµРЅРёСЏ", false);
   private final TokenSetting g;
   private boolean h;
   private boolean i;
   private int j;
   public static int a;
   public static boolean b;

   public AutoRespawn() {
      TokenSetting tokenSetting = new TokenSetting("РљРѕРјР°РЅРґР°", TokenSetting.TokenType.COMMAND, "/home");
      BooleanSetting booleanSetting = this.f;
      this.g = tokenSetting.a(booleanSetting::k);
      this.h = false;
   }

   @EventHandler
   public void a(PlayerDeathEvent playerDeathEvent) {
      if (playerDeathEvent.a() == c.player) {
         this.i = true;
      }
   }

   @EventHandler
   public void a(ClientTickEvent clientTickEvent) {
      if (c.player != null && c.world != null) {
         if (c.currentScreen instanceof DeathScreen) {
            int i = this.j;
            this.j = 2 * (i | 1) - (i ^ 1);
         }

         if (this.i && !(c.currentScreen instanceof DeathScreen) && c.player.age > 30) {
            ChatMessages.a(this.g.a());
            this.i = false;
            this.j = 0;
         }

         if (c.currentScreen instanceof DeathScreen DeathScreenVar) {
            if (!this.h) {
               this.h = true;
            }

            if (this.e.k() && this.a(DeathScreenVar)) {
               c.player.requestRespawn();
               c.currentScreen = null;
               this.j = 0;
            }
         } else if (this.h) {
            if (c.player.isAlive()) {
               this.h = false;
               if (!this.f.k() || this.g.a().isEmpty()) {
                  return;
               }

               ChatMessages.a(this.g.a());
            }
         }
      }
   }

   private boolean a(DeathScreen DeathScreenVar) {
      try {
         for (Object obj : DeathScreenVar.children()) {
            if (obj instanceof ButtonWidget && ((ButtonWidget)obj).active) {
               return true;
            }
         }

         return false;
      } catch (Exception e) {
         return false;
      }
   }

   @Override
   public void f() {
      super.f();
      this.h = false;
   }

   public static String c(String str, String str2, int i, int i2, int i3, int i4) {
      return null;
   }
}
