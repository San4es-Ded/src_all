package rockstar.client.internal.core;


import rockstar.client.*;
import rockstar.client.internal.core.ProfileApiException;

public class MissingProfileException
extends ProfileApiException {
    public MissingProfileException(ProfileApiException typedValue064) {
        super(typedValue064.internalMethod00843(), typedValue064.internalMethod00836(), "Your account doesn't have a Minecraft profile. Please log in to Minecraft and set up your profile.");
    }
}

