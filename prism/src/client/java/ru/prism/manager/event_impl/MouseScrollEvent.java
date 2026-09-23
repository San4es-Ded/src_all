package ru.prism.manager.event_impl;

import ru.prism.manager.events.CancellableEvent;
import net.minecraft.client.gui.screen.Screen;

public class MouseScrollEvent extends CancellableEvent {
    private final double mouseX, mouseY;
    private final double verticalAmount;
    private final Screen screen;

    public MouseScrollEvent(double mouseX, double mouseY, double verticalAmount, Screen screen) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.verticalAmount = verticalAmount;
        this.screen = screen;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getVerticalAmount() {
        return verticalAmount;
    }

    public Screen getScreen() {
        return screen;
    }
}
