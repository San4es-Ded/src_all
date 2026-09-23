#version 150

uniform sampler2D Sampler0; // extracted hand color + silhouette in alpha
uniform sampler2D Sampler1; // blurred scene captured before first-person hands
uniform vec3 color;
uniform vec3 color2;
uniform float fillStrength;
uniform float reflectionStrength;
uniform float glossStrength;
uniform vec2 texelSize;
uniform float time;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    vec4 hand = texture(Sampler0, TexCoord);
    if (hand.a < 0.01) discard;

    vec2 safeUv = clamp(TexCoord, texelSize * 2.0, vec2(1.0) - texelSize * 2.0);
    vec2 reflectedUv = clamp(vec2(1.0 - safeUv.x, safeUv.y),
                             texelSize * 2.0, vec2(1.0) - texelSize * 2.0);
    vec3 localBlur = texture(Sampler1, safeUv).rgb;
    vec3 reflectedBlur = texture(Sampler1, reflectedUv).rgb;
    vec3 reflection = mix(localBlur, reflectedBlur, 0.72);

    vec3 tint = mix(color2, color, smoothstep(0.0, 1.0, TexCoord.y));
    float handLuma = dot(hand.rgb, vec3(0.299, 0.587, 0.114));
    float shading = mix(0.56, 1.0, smoothstep(0.05, 0.85, handLuma));
    vec3 darkFill = tint * (0.24 + 0.22 * shading);
    vec3 tintedReflection = reflection
                          * mix(vec3(0.72), tint * 1.15, clamp(fillStrength, 0.0, 1.0));
    vec3 result = mix(darkFill, tintedReflection, clamp(reflectionStrength, 0.0, 1.0));

    // A broad moving sheen keeps the surface readable as glass instead of a flat fill.
    float bandCenter = 0.48 + sin(TexCoord.x * 8.0 + time * 0.45) * 0.018;
    float sheen = exp(-pow((TexCoord.y - bandCenter) / 0.055, 2.0));

    float minNeighbour = 1.0;
    for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
            if (x == 0 && y == 0) continue;
            vec2 neighbourUv = clamp(TexCoord + vec2(x, y) * texelSize * 1.5,
                                     texelSize * 2.0, vec2(1.0) - texelSize * 2.0);
            minNeighbour = min(minNeighbour, texture(Sampler0, neighbourUv).a);
        }
    }
    float innerRim = clamp(hand.a - minNeighbour, 0.0, 1.0);
    result += tint * (sheen * glossStrength * 0.32 + innerRim * 0.42);

    // Keep protection at the top only; side fading eats wide hand animations.
    float edgeFade = smoothstep(0.012, 0.052, 1.0 - TexCoord.y);
    OutColor = vec4(result, smoothstep(0.02, 0.9, hand.a) * edgeFade);
}
