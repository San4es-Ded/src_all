package haron.render.shader;

import com.google.common.collect.Maps;
import haron.render.shader.ShaderProgram;
import java.util.Map;
import java.util.Optional;

public class ShaderRegistry {
    private final Map<String, ShaderProgram> programs = Maps.newLinkedHashMap();

    public void replace(String string, ShaderProgram s5pbng2) {
        this.programs.replace(string, s5pbng2);
    }

    public void register(String string, ShaderProgram s5pbng2) {
        this.programs.put(string, s5pbng2);
    }

    public Optional<ShaderProgram> find(String string) {
        return Optional.ofNullable(this.programs.get(string));
    }
}

