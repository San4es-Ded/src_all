#version 150

// Режим «Блюр»: рисует размытую версию сцены только в области рук/предмета (по маске).
//   Sampler0 — размытая сцена текущего кадра
//   Sampler1 — маска руки (.r)

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform float strength;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    float m = texture(Sampler1, TexCoord).r;
    float a = clamp(smoothstep(0.05, 0.6, m) * strength, 0.0, 1.0);
    if (a <= 0.001) discard;

    vec3 blurred = texture(Sampler0, TexCoord).rgb;
    OutColor = vec4(blurred, a);
}
