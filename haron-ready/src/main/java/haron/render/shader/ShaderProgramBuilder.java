package haron.render.shader;

import haron.render.shader.ShaderStage;
import haron.render.shader.ShaderProgram;
import haron.util.pzdfrg;
import java.io.InputStream;
import ru.haron.Haron;

public class ShaderProgramBuilder {
    private final ShaderProgram program = new ShaderProgram();

    private ShaderProgramBuilder() {
    }

    public static ShaderProgramBuilder create() {
        return new ShaderProgramBuilder();
    }

    public ShaderProgram build() {
        return this.program;
    }

    public ShaderProgramBuilder attach(String string, ShaderStage c54poq2) {
        InputStream inputStream = this.getClass().getResourceAsStream("/assets/haron/shaders/".concat(string));
        if (inputStream == null) {
            Haron.getLOGGER().error("Shader file not found: /assets/pulse/shaders/{}", (Object)string);
            return this;
        }
        this.program.a(pzdfrg.a(inputStream), c54poq2.glType());
        return this;
    }

    public ShaderProgramBuilder link() {
        this.program.c();
        return this;
    }
}

