package ru.prism.utils.player; // Или ваш собственный пакет для интерфейсов

public interface ITimerSpeed {
    float getSpeed();
    void setSpeed(float speed);

    default void resetSpeed() {
        setSpeed(1.0F);
    }
}