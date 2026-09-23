#version 150

in vec2 FragCoord;
in vec4 FragColor;

uniform vec2 Size;
uniform float Time;
uniform vec4 ThemeColor;
uniform vec4 ThemeColor2;
uniform vec4 ColorModulator;

out vec4 OutColor;

// --- value noise + fbm -------------------------------------------------------
float hash(vec2 p) {
    p = fract(p * vec2(127.1, 311.7));
    p += dot(p, p + 34.45);
    return fract(p.x * p.y);
}

float valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.55;
    for (int i = 0; i < 5; ++i) {
        value += amplitude * valueNoise(p);
        p = p * 2.03 + vec2(17.3, 9.1);
        amplitude *= 0.5;
    }
    return value;
}
// -----------------------------------------------------------------------------

void main() {
    // FragCoord is in GUI space (y down); flip so uv.y = 1.0 is the screen TOP.
    vec2 uv = vec2(FragCoord.x, 1.0 - FragCoord.y);
    float aspect = Size.x / max(Size.y, 1.0);
    vec2 suv = vec2(uv.x * aspect, uv.y);
    vec2 centered = uv - 0.5;
    centered.x *= aspect;
    vec3 primary = clamp(ThemeColor.rgb, 0.0, 1.0);
    vec3 secondary = clamp(ThemeColor2.rgb, 0.0, 1.0);
    vec3 softAccent = mix(primary, secondary, 0.28);

    // Near-black base with a faint lift in the currently selected theme.
    vec3 base = mix(vec3(0.006) + primary * 0.004, vec3(0.009) + softAccent * 0.016,
                    smoothstep(0.0, 1.0, uv.y));

    // Slow-drifting theme-colored smoke: two fbm layers moving independently.
    float t = Time * 0.012;
    float smokeA = fbm(suv * 1.9 + vec2(t, -t * 0.6));
    float smokeB = fbm(suv * 3.1 - vec2(t * 0.7, t * 0.4) + 41.0);
    float smoke = smokeA * 0.72 + smokeB * 0.28;
    smoke = smoothstep(0.42, 0.98, smoke);

    // The smoke is denser toward the upper half and the sides, like the reference.
    float smokeMask = 0.35
            + 0.65 * smoothstep(0.15, 0.85, uv.y)
            + 0.35 * (1.0 - smoothstep(0.0, 0.35, uv.x))
            + 0.30 * smoothstep(0.70, 1.05, uv.x);
    vec3 smokeColor = mix(primary * 0.16, softAccent * 0.25,
                          smoothstep(0.3, 1.0, smoke));
    base += smokeColor * smoke * smokeMask * 0.9;

    // Subtle topographic contour lines, visible only in the corners.
    float field = fbm(suv * 2.6 + vec2(7.7, 3.3) + t * 0.15);
    float bands = abs(fract(field * 9.0) - 0.5);
    float contour = 1.0 - smoothstep(0.02, 0.075, bands);
    float cornerMask = smoothstep(0.45, 1.0, length(centered * vec2(0.85, 1.15)));
    base += contour * cornerMask * softAccent * 0.105;

    // Gentle vignette keeps the center readable.
    float vignette = 1.0 - smoothstep(0.42, 1.05, length(centered * vec2(0.75, 0.95)));
    base *= mix(0.55, 1.0, vignette);

    OutColor = vec4(base, 1.0) * ColorModulator * FragColor;
}
