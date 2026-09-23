#version 330 core

in vec2 uv;
out vec4 finalColor;

uniform sampler2D BlurredSampler;
uniform sampler2D MaskSampler;

layout(std140) uniform HandCompositeUniforms {
    vec4 FlameColor;
    float Time;
};

void main() {
    float hand = texture(MaskSampler, uv).a;
    float coverage = texture(BlurredSampler, uv).a;
    float alpha = clamp(coverage * FlameColor.a * 1.10, 0.0, 1.0);
    alpha *= 1.0 - smoothstep(0.72, 0.98, hand);
    if (alpha <= 0.001) {
        discard;
    }
    vec3 color = FlameColor.rgb * (1.0 + alpha * 0.42);
    finalColor = vec4(color * alpha, alpha);
}
