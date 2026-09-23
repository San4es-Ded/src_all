#version 150

in vec2 Position;
out vec2 vClip;

void main() {
    vClip = Position;
    // z близко к дальней плоскости (1.0), чтобы шейдер рисовался
    // только там, где depth buffer ещё не занят блоками/сущностями
    gl_Position = vec4(Position, 0.999999, 1.0);
}
