package haron.modules.utilities;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;

@ModuleInfo(a="Optimizations", b="Повышение FPS: частицы, тени, погода, облака, дальние сущности, оптимизация моделей и мира", c=ModuleCategory.UTILITIES)
public class Optimizations
extends HaronModule {
    public static Optimizations INSTANCE;
    public final BooleanSetting noParticles = new BooleanSetting("Убрать частицы", true);
    public final BooleanSetting noEntityShadows = new BooleanSetting("Убрать тени сущностей", true);
    public final BooleanSetting noWeather = new BooleanSetting("Не рендерить погоду", true);
    public final BooleanSetting noClouds = new BooleanSetting("Убрать облака", true);
    public final BooleanSetting noFog = new BooleanSetting("Убрать туман", true);
    public final BooleanSetting noGlint = new BooleanSetting("Убрать глинт энчантов", true);
    public final BooleanSetting limitEntityDistance = new BooleanSetting("Резать дальние сущности", true);
    public final NumberSetting entityRenderDistance = new NumberSetting("Дальность сущностей", 48.0f, 16.0f, 128.0f, 8.0f);
    public final BooleanSetting maxEntityCulling = new BooleanSetting("Лимит мобов на экране", true);
    public final NumberSetting maxVisibleEntities = new NumberSetting("Макс. сущностей", 128.0f, 32.0f, 512.0f, 16.0f);
    public final BooleanSetting limitNametags = new BooleanSetting("Резать дальние ники", true);
    public final NumberSetting nametagDistance = new NumberSetting("Дальность ников", 24.0f, 8.0f, 96.0f, 4.0f);
    public final BooleanSetting limitArmor = new BooleanSetting("Резать дальнюю броню", true);
    public final NumberSetting armorDistance = new NumberSetting("Дальность брони", 32.0f, 8.0f, 96.0f, 4.0f);
    public final BooleanSetting fastChests = new BooleanSetting("Упростить сундуки", true);
    public final BooleanSetting noDropRotation = new BooleanSetting("Статичный дроп предм.", true);
    public final BooleanSetting noTotemAnimation = new BooleanSetting("Убрать анимацию тотема", true);
    public final BooleanSetting noExplosionParticles = new BooleanSetting("Убрать взрывы/кристаллы", true);
    public final BooleanSetting animatedTextures = new BooleanSetting("Анимированные текстуры", false);
    public final BooleanSetting noSky = new BooleanSetting("Убрать небо", true);
    public final BooleanSetting noStars = new BooleanSetting("Убрать звёзды", true);
    public final BooleanSetting noWater = new BooleanSetting("Убрать воду", true);
    public final BooleanSetting noEntities = new BooleanSetting("Убрать сущности (кроме игрока)", true);
    public final BooleanSetting shortRenderDist = new BooleanSetting("Уменьшить дальность (6 чанков)", true);
    public final BooleanSetting noBlockEntities = new BooleanSetting("Убрать блок-сущности (сундуки, печи)", true);

    public static boolean cullDistantEntities() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.limitEntityDistance.k() != false;
    }

    public static boolean disableTextureAnimations() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.animatedTextures.k() == false;
    }

    public static boolean isEntityLimitReached(int n) {
        if (!INSTANCE.k() || !((Boolean)Optimizations.INSTANCE.maxEntityCulling.k()).booleanValue()) {
            return false;
        }
        return (float)n > Optimizations.INSTANCE.maxVisibleEntities.get();
    }

    public static double maxNametagDistanceSq() {
        double d = Optimizations.INSTANCE.nametagDistance.get();
        return d * d;
    }

    public static double maxEntityDistanceSq() {
        double d = Optimizations.INSTANCE.entityRenderDistance.get();
        return d * d;
    }

    public static boolean hideSky() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noSky.k() != false;
    }

    public static boolean hideClouds() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noClouds.k() != false;
    }

    public static boolean hideWater() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noWater.k() != false;
    }

    public static boolean hideFog() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noFog.k() != false;
    }

    public static boolean hideGlint() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noGlint.k() != false;
    }

    public static boolean shortDist() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.shortRenderDist.k() != false;
    }

    public static boolean hideStars() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noStars.k() != false;
    }

    public static boolean stopDropRotation() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noDropRotation.k() != false;
    }

    public static boolean hideTotemAnimation() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noTotemAnimation.k() != false;
    }

    public static double maxArmorDistanceSq() {
        double d = Optimizations.INSTANCE.armorDistance.get();
        return d * d;
    }

    public static boolean hideEntities() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noEntities.k() != false;
    }

    public static boolean hideEntityShadows() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noEntityShadows.k() != false;
    }

    public static boolean hideBlockEntities() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noBlockEntities.k() != false;
    }

    public static boolean cullDistantArmor() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.limitArmor.k() != false;
    }

    public static boolean cullParticles() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noParticles.k() != false;
    }

    public static boolean hideExplosions() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noExplosionParticles.k() != false;
    }

    public static boolean cullNametags() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.limitNametags.k() != false;
    }

    public static boolean hideWeather() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.noWeather.k() != false;
    }

    public static boolean useFastChests() {
        return INSTANCE.k() && (Boolean)Optimizations.INSTANCE.fastChests.k() != false;
    }

    public Optimizations() {
        INSTANCE = this;
    }
}

