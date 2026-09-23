#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform vec4 flameColor;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    float hand = texture(Sampler1, TexCoord).a;
    float coverage = texture(Sampler0, TexCoord).a;
    float alpha = clamp(coverage * flameColor.a * 1.10, 0.0, 1.0);
    alpha *= 1.0 - smoothstep(0.72, 0.98, hand);
    if (alpha <= 0.001) {
        discard;
    }
    vec3 color = flameColor.rgb * (1.0 + alpha * 0.42);
    OutColor = vec4(color * alpha, alpha);
}
