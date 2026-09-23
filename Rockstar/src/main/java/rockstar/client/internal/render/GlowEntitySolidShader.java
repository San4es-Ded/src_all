package rockstar.client.internal.render;


import rockstar.client.*;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import rockstar.client.internal.render.ShaderProgramBase;

public class GlowEntitySolidShader
extends ShaderProgramBase {
    public GlowEntitySolidShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);
    }
}

