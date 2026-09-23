#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform vec2 Velocity;
uniform vec2 HandVelocity;
uniform float HandRegionTop;
uniform float Strength;
uniform float Samples;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    vec2 uv = TexCoord;
    float handMask = 1.0 - smoothstep(HandRegionTop - 0.10, HandRegionTop + 0.08, uv.y);
    vec2 vel = mix(Velocity, HandVelocity, handMask) * (Strength * 1.65);

    vec3 color = texture(Sampler0, uv).rgb;
    float weightSum = 1.0;
    int sampleCount = int(clamp(Samples, 4.0, 16.0));

    for (int i = 1; i <= 16; ++i) {
        if (i > sampleCount) {
            break;
        }

        float t = float(i) / float(sampleCount);
        float weight = 1.0 - t * 0.55;
        vec2 offset = vel * pow(t, 0.82);
        color += texture(Sampler0, uv - offset).rgb * weight;
        color += texture(Sampler0, uv - offset * 1.35).rgb * (weight * 0.55);
        color += texture(Sampler1, uv + offset * 0.75).rgb * (weight * 0.48);
        weightSum += weight * 2.05;
    }

    color /= weightSum;

    float motion = clamp(length(mix(Velocity, HandVelocity, handMask)) * 220.0, 0.0, 1.0);
    vec3 previous = texture(Sampler1, uv).rgb;
    float trail = clamp(Strength * 0.10 + motion * Strength * 0.58, 0.0, 0.72);
    trail *= mix(1.0, 0.28, handMask);
    color = mix(color, previous, trail);

    OutColor = vec4(color, 1.0);
}
