package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "FullBright",
   category = Category.RENDER,
   description = "Максимальное освещение"
)
@FastNative
public class FullBright extends Module {
   public static final FullBright INSTANCE = new FullBright();
}
