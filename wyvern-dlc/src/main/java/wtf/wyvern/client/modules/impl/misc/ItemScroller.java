package wtf.wyvern.client.modules.impl.misc;

import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.input.EventMouse;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "ItemScroller",
   description = "Перемещение преметов без задержки",
   category = Category.MISC
)
@FastNative
public final class ItemScroller extends Module {
   public static final ItemScroller INSTANCE = new ItemScroller();
   public boolean mouseHold;

   @EventTarget
   private void onMouse(EventMouse e) {
      if (e.getButton() == 0 && e.getAction() == 1) {
         this.mouseHold = true;
      }

      if (this.mouseHold && e.getAction() == 0) {
         this.mouseHold = false;
      }

   }
}
