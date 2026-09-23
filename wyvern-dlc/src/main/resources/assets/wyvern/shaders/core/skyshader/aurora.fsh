#version 150

uniform float uTime;
uniform vec2 uResolution;
uniform vec3 uColor;
uniform vec3 uColor2;
uniform float uAlpha;
uniform float uSpeed;
uniform float uScale;
uniform float uIntensity;
uniform vec2 uCameraDir;
uniform float uFov;
uniform float uRain;
uniform float uThunder;
uniform float uThemeMix; // 0 = натуральные цвета, 1 = цвет темы

out vec4 fragColor;

mat3 rotX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(1.0, 0.0, 0.0, 0.0, c, s, 0.0, -s, c);
}

mat3 rotY(float a) {
    float c = cos(a), s = sin(a);
    return mat3(c, 0.0, s, 0.0, 1.0, 0.0, -s, 0.0, c);
}

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), u.x),
               mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), u.x), u.y);
}

float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i = 0; i < 5; i++) {
        v += noise(p) * a;
        p = p * 2.05 + vec2(3.1, 1.7);
        a *= 0.5;
    }
    return v;
}

vec3 skyRay() {
    vec2 uv = gl_FragCoord.xy / uResolution.xy;
    vec2 sp = uv * 2.0 - 1.0;
    float aspect = uResolution.x / uResolution.y;
    float tanV = tan(radians(uFov) * 0.5);
    vec3 rayV = normalize(vec3(sp.x * tanV * aspect, sp.y * tanV, 1.0));
    return normalize(rotY(uCameraDir.x) * rotX(uCameraDir.y) * rayV);
}

void main() {
    vec3 ray = skyRay();
    float t = uTime * uSpeed;
    float height = ray.y;

    // ---------- ночное небо (без звёзд) ----------
    float up = smoothstep(-0.2, 1.0, height);
    vec3 sky = mix(vec3(0.055, 0.035, 0.110), vec3(0.020, 0.020, 0.070), up);

    // seam-free горизонтальная координата: берём напрямую компоненты луча (непрерывны,
    // никакого разрыва atan сзади). Занавесы «висят» вертикально и дрейфуют по времени.
    vec2 hp = vec2(ray.x, ray.z);
    float scale = max(uScale, 1.0);

    vec3 themeTint = mix(uColor, uColor2, 0.5);
    vec3 aurora = vec3(0.0);
    float total = 0.0;

    // 5 занавесов на большой высоте (сияние крупное — от горизонта до зенита)
    for (int i = 0; i < 5; i++) {
        float fi = float(i);
        vec2 c = hp * (1.6 + fi * 0.35) * (0.7 + scale * 0.05);
        vec2 drift = vec2(t * (0.03 + fi * 0.012), t * 0.02);

        float band = fbm(c * 1.4 + drift + fi * 3.1);
        band = smoothstep(0.40, 0.92, band);

        float rays = fbm(c * 5.5 + vec2(0.0, height * 1.4) - drift * 1.5 + fi * 1.7);
        rays = pow(clamp(rays, 0.0, 1.0), 1.4);

        // увеличенная вертикальная зона: сияние тянется почти до зенита
        float base = 0.02 + fi * 0.05;
        float top = 1.05;
        float vzone = smoothstep(base - 0.2, base + 0.12, height) * smoothstep(top, base + 0.04, height);

        float mask = band * rays * vzone;

        float g = clamp((height - base) / (top - base), 0.0, 1.0);
        vec3 col = mix(vec3(0.10, 1.0, 0.45), vec3(0.15, 0.85, 0.75), smoothstep(0.0, 0.5, g));
        col = mix(col, vec3(0.70, 0.20, 1.0), smoothstep(0.45, 1.0, g));
        col = mix(col, themeTint, 0.55 * uThemeMix);

        aurora += col * mask;
        total += mask;
    }

    float glow = clamp(total * (1.3 + uIntensity * 30.0), 0.0, 3.0);
    sky += aurora * glow;

    // мягкое зелёное свечение у нижней кромки
    vec3 groundGlow = mix(vec3(0.05, 0.28, 0.14), themeTint * 0.4, uThemeMix);
    sky += groundGlow * smoothstep(0.0, 0.6, total) * smoothstep(-0.1, 0.3, height);

    sky = mix(sky, sky * vec3(0.5, 0.55, 0.65), clamp(uRain + uThunder, 0.0, 1.0) * 0.4);

    fragColor = vec4(clamp(sky, 0.0, 1.6), uAlpha);
}
