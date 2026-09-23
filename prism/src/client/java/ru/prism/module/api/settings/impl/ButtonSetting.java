package ru.prism.module.api.settings.impl;

import ru.prism.module.api.Module;
import ru.prism.module.api.settings.Setting;
import ru.prism.utils.animation.Animation;

import java.util.function.Supplier;

/**
 * Сеттинг-кнопка: при клике выполняет действие. В отличие от BooleanSetting
 * не хранит вкл/выкл состояние — это просто триггер (например, сброс настроек).
 */
public class ButtonSetting extends Setting<Boolean> {

    private final Runnable action;
    // вспышка при нажатии (для подсветки в GUI)
    public final Animation pressAnim = new Animation();

    public ButtonSetting(Module parent, String name, Runnable action) {
        super(parent, name, false);
        this.action = action;
    }

    public void press() {
        if (action != null) action.run();
        pressAnim.set(1F);
        pressAnim.run(0F, 0.45F, ru.prism.utils.animation.Easings.QUAD_OUT);
    }

    @Override
    public ButtonSetting setVisible(Supplier<Boolean> value) {
        return (ButtonSetting) super.setVisible(value);
    }
}
