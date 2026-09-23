#version 150

in vec2 FragCoord;

out vec4 fragColor;

uniform vec2 u_Resolution;
uniform float u_Time;
uniform float u_Fov;
uniform vec2 u_CameraDir;
uniform float u_Scale;
uniform float u_Intensity;
uniform float u_Alpha;
uniform float u_ThemeMix;
uniform vec3 u_Color;
uniform vec3 u_Color2;

float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(
        mix(hash21(i), hash21(i + vec2(1.0, 0.0)), f.x),
        mix(hash21(i + vec2(0.0, 1.0)), hash21(i + vec2(1.0, 1.0)), f.x),
        f.y
    );
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.52;
    mat2 rotation = mat2(0.82, -0.57, 0.57, 0.82);
    for (int i = 0; i < 5; i++) {
        value += amplitude * valueNoise(p);
        p = rotation * p * 2.03 + 7.17;
        amplitude *= 0.48;
    }
    return value;
}

void main() {
    vec2 resolution = max(u_Resolution, vec2(1.0));
    float aspect = resolution.x / resolution.y;
    vec2 p = vec2((FragCoord.x - 0.5) * aspect, FragCoord.y - 0.5);
    float fovScale = tan(radians(clamp(u_Fov, 30.0, 150.0)) * 0.5);
    float cloudScale = mix(1.65, 0.58, clamp((u_Scale - 1.0) / 19.0, 0.0, 1.0));
    p = p * fovScale * cloudScale + vec2(u_CameraDir.x * 0.32, u_CameraDir.y * 0.42);
    float time = u_Time * 0.045;

    vec2 warp = vec2(
        fbm(p * 1.15 + vec2(time, -time * 0.36)),
        fbm(p * 1.15 + vec2(-time * 0.29, time * 0.42) + 13.7)
    );
    vec2 drifted = p + (warp - 0.5) * 0.72;
    float broadFog = fbm(drifted * 1.85 + vec2(time * 0.35, 0.0));
    float fineFog = fbm(drifted * 3.10 - vec2(time * 0.18, time * 0.12));
    float fog = smoothstep(0.42, 0.82, broadFog * 0.72 + fineFog * 0.36);
    float centerGlow = exp(-dot(p - vec2(0.0, 0.10), p - vec2(0.0, 0.10)) * 3.2);
    float upperMist = smoothstep(0.92, 0.08, FragCoord.y) * 0.18;
    float vignette = smoothstep(1.05, 0.24, length(p * vec2(0.72, 1.0)));
    float breathing = 0.93 + 0.07 * sin(u_Time * 0.16);
    float v = (fog * 0.62 + centerGlow * 0.26 + upperMist) * vignette;
    v *= mix(0.55, 1.65, clamp(u_Intensity, 0.0, 1.0)) * breathing;

    vec3 naturalDark = vec3(0.006, 0.004, 0.012);
    vec3 naturalLow = vec3(0.065, 0.025, 0.105);
    vec3 naturalHigh = vec3(0.300, 0.105, 0.480);
    vec3 themeLow = mix(u_Color, u_Color2, 0.35) * 0.18;
    vec3 themeHigh = mix(u_Color, u_Color2, clamp(fineFog, 0.0, 1.0)) * 0.62;
    vec3 lowColor = mix(naturalLow, themeLow, clamp(u_ThemeMix, 0.0, 1.0));
    vec3 highColor = mix(naturalHigh, themeHigh, clamp(u_ThemeMix, 0.0, 1.0));
    vec3 color = naturalDark + lowColor * clamp(v, 0.0, 1.0)
            + highColor * pow(clamp(v, 0.0, 1.0), 2.2);
    fragColor = vec4(clamp(color, 0.0, 1.0), clamp(u_Alpha, 0.0, 1.0));
}
