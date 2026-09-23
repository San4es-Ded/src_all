package rockstar.client.internal.script;







import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.network.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import globals.client.Information;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pyrock.events.game.InternalAttackEvent;
import pyrock.events.game.SendMessageEvent;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.network.ServerConnectionEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.profile.Profile;

public class SessionReporter {
   private ModeSetting.InternalType0088 internalField0237;
   private int internalField0227;
   private boolean internalField0277;
   private final List<ModeSetting.InternalType0088> internalField0416 = new ArrayList<>();
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      AuraModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
      if (this.internalField0237 != localValue2.internalMethod01895().internalMethod07418()) {
         this.internalField0227++;
         this.internalField0277 = false;
         if (!this.internalField0416.contains(localValue2.internalMethod01895().internalMethod07418())) {
            this.internalField0416.add(localValue2.internalMethod01895().internalMethod07418());
         }

         if (this.internalField0237 != null && !this.internalField0416.contains(this.internalField0237)) {
            this.internalField0416.add(this.internalField0237);
         }
      }
   };
   private final EventListener<ServerConnectionEvent> internalField0158 = localValue1 -> {
      this.internalField0227 = 0;
      this.internalField0416.clear();
      WebhookNotifier.internalMethod03328(
         String.format(
            "\ud83e\uddd4 %s(%s) \u0437\u0430\u0448\u0451\u043b \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440 %s(%s) \u0441 \u044e\u0437\u0435\u0440\u043d\u0435\u0439\u043c\u043e\u043c %s",
            Profile.getUsername(),
            this.internalMethod00162(),
            ServerUtils.internalMethod04844(localValue1.getAddress().getAddress(), false),
            localValue1.getAddress().getAddress(),
            MinecraftClientAccess.internalField0149.getSession().getUsername()
         )
      );
   };
   private final EventListener<ReceivePacketEvent> internalField1028 = localValue1 -> {
      if (localValue1.getPacket() instanceof GameMessageS2CPacket localValue2 && MinecraftClientAccess.internalField0149.player != null) {
         for (String localValue6 : this.internalMethod00291()) {
            if (localValue2.content().getString().contains(localValue6)) {
               WebhookNotifier.internalMethod03328(
                  String.format(
                     "\u26a0\ufe0f %s(%s) \u043f\u043e\u043b\u0443\u0447\u0438\u043b \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 %s (\u043d\u0438\u043a\u043d\u0435\u0439\u043c \u044e\u0437\u0435\u0440\u0430 %s)",
                     Profile.getUsername(),
                     this.internalMethod00162(),
                     localValue2.content().getString(),
                     MinecraftClientAccess.internalField0149.getSession().getUsername()
                  )
               );
            }
         }
      }
   };
   private final EventListener<SendMessageEvent> internalField1029 = localValue1 -> {
      for (String localValue5 : this.internalMethod00291()) {
         if (localValue1.getMessage().contains(localValue5)) {
            WebhookNotifier.internalMethod03328(
               String.format(
                  "\u26a0\ufe0f %s(%s) \u043e\u0442\u043f\u0440\u0430\u0432\u0438\u043b \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 %s (\u043d\u0438\u043a\u043d\u0435\u0439\u043c \u044e\u0437\u0435\u0440\u0430 %s)",
                  Profile.getUsername(),
                  this.internalMethod00162(),
                  localValue1.getMessage(),
                  MinecraftClientAccess.internalField0149.getSession().getUsername()
               )
            );
         }
      }
   };
   private final EventListener<InternalAttackEvent> internalField1030 = localValue1 -> {
      AuraModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
      this.internalField0237 = localValue2.internalMethod01895().internalMethod07418();
      if (this.internalField0227 > 3 && !this.internalField0277 && this.internalField0416.size() >= 3) {
         WebhookNotifier.internalMethod03328(
            String.format(
               "\u26a0\ufe0f %s(%s) \u0442\u0435\u0441\u0442\u0438\u0442 \u0440\u043e\u0442\u0430\u0446\u0438\u0438 (%s) \u043d\u0430 \u0438\u0433\u0440\u043e\u043a\u0435 %s (\u043d\u0438\u043a\u043d\u0435\u0439\u043c \u044e\u0437\u0435\u0440\u0430 %s)",
               Profile.getUsername(),
               this.internalMethod00162(),
               this.internalMethod03612(),
               localValue1.getEntity().getName().getString(),
               MinecraftClientAccess.internalField0149.getSession().getUsername()
            )
         );
         this.internalField0277 = true;
      }

      for (String localValue6 : this.internalMethod00291()) {
         if (localValue1.getEntity().getName().getString().contains(localValue6)) {
            WebhookNotifier.internalMethod03328(
               String.format(
                  "\u26a0\ufe0f %s(%s) \u0430\u0442\u0430\u043a\u0443\u0435\u0442 %s (\u043d\u0438\u043a\u043d\u0435\u0439\u043c \u044e\u0437\u0435\u0440\u0430 %s)",
                  Profile.getUsername(),
                  this.internalMethod00162(),
                  localValue1.getEntity().getName().getString(),
                  MinecraftClientAccess.internalField0149.getSession().getUsername()
               )
            );
         }
      }
   };
   private final EventListener<SendPacketEvent> internalField1027 = localValue0 -> {};

   public SessionReporter() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public String internalMethod03612() {
      StringBuilder localValue1 = new StringBuilder();

      for (ModeSetting.InternalType0088 localValue3 : this.internalField0416) {
         localValue1.append(LanguageManager.internalMethod07214(localValue3.getName()));
         if (localValue3 != this.internalField0416.getLast()) {
            localValue1.append(", ");
         }
      }

      return localValue1.toString();
   }

   public String internalMethod00162() {
      String localValue1 = "null";
      if (Information.getPreferUser() != null) {
         localValue1 = Information.getPreferUser().username();
      }

      return localValue1;
   }

   public String[] internalMethod00291() {
      return new String[]{"postmarketOS"};
   }
}
