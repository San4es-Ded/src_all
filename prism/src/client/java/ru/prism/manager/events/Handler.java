package ru.prism.manager.events;


import ru.prism.Client;

public abstract class Handler {
    public Handler() {
        Client.eventHandler().subscribe(this);
    }
}