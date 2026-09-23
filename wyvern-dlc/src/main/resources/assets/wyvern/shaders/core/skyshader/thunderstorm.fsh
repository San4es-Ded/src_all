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
    return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
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
    for (int i = 0; i < 6; i++) {
        v += noise(p) * a;
        p = p * 2.02 + vec2(5.3, 1.7);
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

    // тёмный космический фон
    float up = smoothstep(-0.4, 0.95, height);
    vec3 sky = mix(vec3(0.020, 0.010, 0.045), vec3(0.004, 0.002, 0.014), up);

    // seam-free координата (компоненты луча непрерывны — нет разреза сзади)
    float scale = max(uScale, 1.0) * 0.55;
    vec2 p = vec2(ray.x, ray.z) * scale + vec2(2.0, 5.0);
    vec2 drift = vec2(t * 0.02, -t * 0.012);

    // двойной domain-warp — «текущая», клубящаяся туманность
    vec2 w1 = vec2(fbm(p * 0.7 + drift), fbm(p * 0.7 + drift + vec2(4.7, 2.3)));
    vec2 q = p + (w1 - 0.5) * 2.4;
    vec2 w2 = vec2(fbm(q * 1.3 - drift * 0.6), fbm(q * 1.3 - drift * 0.6 + vec2(1.9, 8.1)));
    vec2 r = q + (w2 - 0.5) * 1.6;

    float n1 = fbm(r + drift);
    float n2 = fbm(r * 1.9 + vec2(3.1, 7.4) - drift * 0.7);
    float n3 = fbm(r * 3.6 + vec2(-4.0, 2.0) + drift * 0.4);

    float density = n1 * 0.65 + n2 * 0.45 + n3 * 0.25;
    float mask = smoothstep(0.45, 1.05, density);
    // мягко гаснет к горизонту, живёт по всему небу выше
    mask *= smoothstep(-0.45, 0.05, height);

    // палитра: индиго -> бирюза -> маджента, перетекают по шуму
    vec3 indigo = vec3(0.24, 0.10, 0.62);
    vec3 teal   = vec3(0.06, 0.62, 0.82);
    vec3 magenta = vec3(0.90, 0.20, 0.62);
    vec3 neb = mix(indigo, teal, smoothstep(0.25, 0.75, n2));
    neb = mix(neb, magenta, smoothstep(0.55, 0.95, n3));
    neb = mix(neb, mix(uColor, uColor2, 0.5), 0.6 * uThemeMix);

    float breath = 0.82 + 0.18 * sin(t * 0.5 + n1 * 5.0);
    sky += neb * mask * breath * (1.1 + uIntensity * 26.0);

    // светящиеся филаменты-нити (анимированные, мягкие)
    float veins = abs(n2 - n3);
    float filament = pow(smoothstep(0.14, 0.0, veins), 2.5) * mask;
    sky += mix(vec3(0.7, 0.85, 1.0), mix(uColor, uColor2, 0.5), uThemeMix) * filament * 0.7;

    // тонкая пыль
    float dust = fbm(r * 6.5 + drift * 2.0);
    sky += neb * 0.35 * smoothstep(0.62, 0.92, dust) * mask;

    sky = mix(sky, sky * vec3(0.62, 0.62, 0.76), clamp(uRain + uThunder, 0.0, 1.0) * 0.25);

    fragColor = vec4(clamp(sky, 0.0, 1.6), uAlpha);
}
