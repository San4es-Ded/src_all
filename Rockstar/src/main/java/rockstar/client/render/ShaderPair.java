package rockstar.client.render;


import rockstar.client.*;
import rockstar.client.internal.script.*;
import lombok.Generated;
import net.minecraft.util.Identifier;
import rockstar.client.internal.script.AccentTimeShader;

public class ShaderPair {
    private final AccentTimeShader internalField0439;
    private final AccentTimeShader internalField0438;

    public ShaderPair(Identifier identifier, Identifier identifier2) {
        this.internalField0439 = new AccentTimeShader(identifier);
        this.internalField0438 = new AccentTimeShader(identifier2);
    }

    @Generated
    public AccentTimeShader internalMethod04938() {
        return this.internalField0439;
    }

    @Generated
    public AccentTimeShader internalMethod06188() {
        return this.internalField0438;
    }
}

