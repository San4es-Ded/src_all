#version 150

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 FragCoord;

void main() {
    gl_Position = vec4(Position.xy, 0.0, 1.0);
    FragCoord = Position.xy * 0.5 + 0.5;
}
