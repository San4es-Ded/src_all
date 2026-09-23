package wtf.wyvern.utility.game.client;
import wtf.astroguard.J2C.FastNative;

/**
 * Контроль скорости игрового тика (аналог устаревшего {@code mc.timer} из старых версий).
 * <p>
 * В 1.21.4 класс {@code net.minecraft.util.Timer} отсутствует — система тиков
 * переработана в {@code RenderTickCounter.Dynamic}, где скорость задаётся полем
 * {@code targetMillisPerTick}. Реальная модификация выполняется в
 * {@link wtf.wyvern.mixin.client.RenderTickCounterMixin}, который
 * масштабирует millis-per-tick на значение, хранимое здесь.
 * <p>
 * Множитель интерпретируется так же, как в присланной базе:
 * {@code 1.0F} — норма, {@code 2.0F} — вдвое быстрее.
 */
@FastNative
public final class TimerService {

    /** Норма: 50ms на тик = 20 tps. */
    public static final float DEFAULT_TICKS_PER_SECOND = 20.0F;

    private static float multiplier = 1.0F;

    private TimerService() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Устанавливает множитель скорости игры.
     *
     * @param multiplier 1.0 = норма, {@code > 1.0} — ускорение, {@code < 1.0} — замедление.
     */
    public static void setSpeed(float multiplier) {
        TimerService.multiplier = multiplier;
    }

    /** Сбрасывает скорость к норме (1.0). */
    public static void resetSpeed() {
        TimerService.multiplier = 1.0F;
    }

    /** @return текущий множитель скорости. */
    public static float getSpeed() {
        return multiplier;
    }

    /**
     * Применяет множитель к «сырому» значению миллисекунд на тик, полученному
     * из ванильного {@code targetMillisPerTick}. Вызывается из миксина.
     *
     * @param millisPerTick исходное значение ms/tick
     * @return значение ms/tick с учётом множителя
     */
    public static float applyMultiplier(float millisPerTick) {
        if (multiplier <= 0.0F) {
            return millisPerTick;
        }
        return millisPerTick / multiplier;
    }
}
