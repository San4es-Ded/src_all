package rockstar.client.internal.core;


import rockstar.client.*;
import rockstar.client.internal.core.NewtonTaskNodeEvent;
import rockstar.client.internal.core.NewtonTaskFailedEvent;
import rockstar.client.internal.core.NewtonTaskFinishedEvent;
import rockstar.client.internal.core.NewtonTaskStartedEvent;

public sealed interface NewtonTaskEvent
permits NewtonTaskStartedEvent, NewtonTaskFinishedEvent, NewtonTaskFailedEvent, NewtonTaskNodeEvent {
}

