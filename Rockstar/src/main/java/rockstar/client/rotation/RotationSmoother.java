package rockstar.client.rotation;


import rockstar.client.*;
import rockstar.client.rotation.Rotation;

@FunctionalInterface
public interface RotationSmoother {
    public Rotation returnStep(Rotation localValue1, Rotation localValue2);
}

