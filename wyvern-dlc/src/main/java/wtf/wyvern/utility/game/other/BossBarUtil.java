package wtf.wyvern.utility.game.other;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import wtf.wyvern.mixin.accessors.BossBarHudAccessor;

public final class BossBarUtil {

   private BossBarUtil() {
   }

   public static Collection<ClientBossBar> getBossBars() {
      MinecraftClient mc = MinecraftClient.getInstance();
      if (mc.inGameHud == null) {
         return List.of();
      }
      BossBarHudAccessor accessor = (BossBarHudAccessor) mc.inGameHud.getBossBarHud();
      return accessor.wyvern$getBossBars().values();
   }

   public static BossBar findByNameContains(String needleLowercase) {
      for (BossBar bar : getBossBars()) {
         if (bar.getName().getString().toLowerCase(Locale.ROOT).contains(needleLowercase)) {
            return bar;
         }
      }
      return null;
   }
}
