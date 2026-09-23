package wtf.wyvern.core.notify;

import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wtf.wyvern.core.events.impl.other.EventModuleToggle;
import wtf.wyvern.core.events.impl.player.EventPickupItem;
import wtf.wyvern.core.events.impl.server.EventChatReceive;
import wtf.wyvern.client.ui.interfaces.component.NotifyComponent;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.utility.interfaces.IMinecraft;

public class NotifyManager implements IMinecraft {
   private static NotifyManager instance;
   private final List<NotifyComponent> notifyComponents = new ArrayList<>();

   public NotifyManager() {
      EventManager.register(this);
   }

   public static NotifyManager getInstance() {
      if (instance == null) {
         instance = new NotifyManager();
      }

      return instance;
   }

   public void setNotifyComponent(NotifyComponent component) {
      this.notifyComponents.add(component);
   }

   @EventTarget
   public void onModuleToggle(EventModuleToggle event) {
      for (NotifyComponent notifyComponent : this.notifyComponents) {
         notifyComponent.addNotification(event.getModule(), event.isEnabled());
      }
   }

   @EventTarget
   public void onChat(EventChatReceive event) {
      String message = event.getMessage().getString().toLowerCase();
      if (message.contains("spec")) {
         sendTextNotify("SD", Text.literal("Кто-то упомянул ")
                 .append(Text.literal("Spec").formatted(Formatting.RED)));
      }
   }

   @EventTarget
   public void onPickup(EventPickupItem event) {
      if (event.getEntity() == mc.player) {
         ItemStack stack = event.getItemStack();
         for (NotifyComponent notifyComponent : this.notifyComponents) {
            notifyComponent.addItemNotification("Подобрал " + stack.getName().getString(), "SD");
         }
      }
   }

   /** Уведомление с настоящей иконкой предмета — используется модулем UseTracker. */
   public void addUseNotification(ItemStack stack, Text text) {
      for (NotifyComponent notifyComponent : this.notifyComponents) {
         notifyComponent.addUseNotification(stack, text);
      }
   }

   private void sendTextNotify(String icon, Text text) {
      for (NotifyComponent notifyComponent : this.notifyComponents) {
         notifyComponent.addTextNotification(icon, text);
      }
   }

   public void addNotification(Module module, boolean enabled) {
      for (NotifyComponent notifyComponent : this.notifyComponents) {
         notifyComponent.addNotification(module, enabled);
      }
   }

   public void addNotification(String title, Text text) {
      sendTextNotify("\uf05a", Text.literal(title + " " + text.getString()));
   }
}