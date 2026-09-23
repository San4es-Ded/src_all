#version 150

uniform vec4 ColorModulator;
uniform float Time;
uniform vec2 Resolution;
uniform vec4 ThemeColor;
uniform float OutlineWidth;
uniform float GlowStrength;
uniform float FillAmount;
uniform float Alpha;
uniform float WaveSpeed;
uniform float WaveScale;

in vec2 texCoord;
in vec4 vertexColor;
in vec3 worldPos;
in vec3 viewNormal;
in vec3 viewDir;

out vec4 fragColor;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}

float noise(vec2 p) {
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
    float amplitude = 0.5;
    for (int i = 0; i < 4; i++) {
        value += noise(p) * amplitude;
        p *= 2.0;
        amplitude *= 0.5;
    }
    return value;
}

void main() {
    vec3 normal = normalize(viewNormal);
    vec3 view = normalize(viewDir);

    float waveA = sin((worldPos.y + worldPos.x * 0.35 + worldPos.z * 0.2) * (6.0 * WaveScale) - Time * WaveSpeed * 3.2) * 0.5 + 0.5;
    float waveB = sin((worldPos.x - worldPos.z * 0.45) * (8.0 * WaveScale) + Time * WaveSpeed * 2.4) * 0.5 + 0.5;
    float distortion = fbm(worldPos.xz * (2.5 * WaveScale) + vec2(Time * WaveSpeed * 0.35, -Time * WaveSpeed * 0.28));
    float pattern = mix(waveA, waveB, 0.45) + distortion * 0.35;
    pattern = clamp(pattern, 0.0, 1.0);

    float fresnel = pow(1.0 - max(dot(normal, view), 0.0), max(0.35, 2.2 - OutlineWidth * 0.35));
    float pulse = 0.82 + 0.18 * sin(Time * WaveSpeed * 1.6);

    vec3 baseFill = mix(vertexColor.rgb, ThemeColor.rgb, FillAmount * 0.75);
    vec3 waveTint = mix(baseFill, ThemeColor.rgb * 1.2, pattern * FillAmount);
    vec3 glow = ThemeColor.rgb * (fresnel * (0.9 + pattern * 0.8)) * GlowStrength * pulse;

    vec3 finalColor = waveTint + glow;
    float finalAlpha = vertexColor.a * ThemeColor.a * Alpha * ColorModulator.a;

    fragColor = vec4(finalColor * ColorModulator.rgb, finalAlpha);
}
