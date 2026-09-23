package ru.prism.utils.animation;

@FunctionalInterface
public interface Easing {
    double ease(double value);
}