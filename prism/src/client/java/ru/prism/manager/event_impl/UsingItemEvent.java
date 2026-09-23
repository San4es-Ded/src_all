package ru.prism.manager.event_impl;

import ru.prism.manager.events.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsingItemEvent extends CancellableEvent {
    byte type;
}
