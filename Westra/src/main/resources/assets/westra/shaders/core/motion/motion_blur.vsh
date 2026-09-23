#version 150

in vec3 Position;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 TexCoord;

void main() {
    vec4 pos = ProjMat * ModelViewMat * vec4(Position, 1.0);

    gl_Position = pos;
    TexCoord = pos.xy * 0.5 + 0.5;
}
