package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "LootBeams", category = Category.RENDER, description = "Показывает лучи над предметами на земле")
@FastNative
public final class LootBeamsModule extends Module {
   public static final LootBeamsModule INSTANCE = new LootBeamsModule();
}
