package ru.prism.manager.events;


import ru.prism.Client;

public class Event {
    public String getName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    public void hook() {
        Client.eventHandler().post(this);
    }
}
