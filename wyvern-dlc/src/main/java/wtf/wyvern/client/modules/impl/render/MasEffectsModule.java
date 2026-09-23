package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "MasEffects", category = Category.RENDER, description = "Добавляет эффекты частиц")
@FastNative
public final class MasEffectsModule extends Module {
   public static final MasEffectsModule INSTANCE = new MasEffectsModule();
}
