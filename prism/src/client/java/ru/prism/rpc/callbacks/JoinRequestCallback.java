package ru.prism.rpc.callbacks;


import com.sun.jna.Callback;
import ru.prism.rpc.DiscordUser;

public interface JoinRequestCallback extends Callback {
    void apply(final DiscordUser p0);
}
