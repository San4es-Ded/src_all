package ru.prism.manager.event_impl;

import ru.prism.manager.events.CancellableEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SwingDurationEvent extends CancellableEvent {
    float animation;
}

