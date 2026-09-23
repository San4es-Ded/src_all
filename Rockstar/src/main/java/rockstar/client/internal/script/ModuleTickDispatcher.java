package rockstar.client.internal.script;




import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;
import pyrock.events.player.ClientPlayerTickEvent;

public class ModuleTickDispatcher implements EventListener<ClientPlayerTickEvent> {
   public void onEvent(ClientPlayerTickEvent localValue1) {
      for (ModuleEntry localValue3 : RockstarClient.getInstance().getModuleManager().getModules()) {
         if (localValue3.isEnabled()) {
            localValue3.internalMethod08229();
         }
      }

      BotRosterManager.internalMethod03065().internalMethod02924();
   }
}
