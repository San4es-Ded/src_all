package wtf.wyvern.client.modules.impl.misc;

import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "NoInteract",
   category = Category.MISC,
   description = "Не дает открыть контейнера"
)
@FastNative
public final class NoInteract extends Module {
   private final BooleanSetting onlyOnPvP = new BooleanSetting("Только в ПВП", false);
   public static final NoInteract INSTANCE = new NoInteract();

   public boolean needToWork() {
      return !this.onlyOnPvP.isEnabled() || Wyvern.getInstance().getServerHandler().isPvp();
   }
}
