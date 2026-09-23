package ru.prism.manager.event_impl;

import ru.prism.manager.events.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DropItemEvent extends CancellableEvent {
    private final int slot;
    private final boolean dropEntireStack;
}
