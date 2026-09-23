#version 150

in vec2 vClip;
out vec4 fragColor;

uniform float Time;
uniform float Speed;
uniform float Density;
uniform float Scale;
uniform vec3 SkyColor;
uniform vec2 ScreenSize;

// 0 = процедурные клубящиеся облака (по умолчанию), 1 = магма/турбулентность -
// одни и те же режимы, что и в SkyShader, для визуальной согласованности
uniform int SkyMode;

// сила тонирования поверх текстуры предмета (0 = не видно, 1 = полностью цвет шейдера)
uniform float Opacity;

// --- тот же устойчивый value noise, что и в sky_shader.frag ---
// (см. комментарий там: hash без sin()/без произведения координат,
// чтобы не терять точность float на GPU при больших входных числах)
float hash(vec3 p) {
    p = fract(p * vec3(0.1031, 0.1030, 0.0973));
    p += dot(p, p.yzx + 33.33);
    return fract((p.x + p.y) * p.z);
}

float noise(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    vec3 u = f * f * (3.0 - 2.0 * f);

    float n000 = hash(i + vec3(0.0, 0.0, 0.0));
    float n100 = hash(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash(i + vec3(1.0, 1.0, 1.0));

    float nx00 = mix(n000, n100, u.x);
    float nx10 = mix(n010, n110, u.x);
    float nx01 = mix(n001, n101, u.x);
    float nx11 = mix(n011, n111, u.x);

    float nxy0 = mix(nx00, nx10, u.y);
    float nxy1 = mix(nx01, nx11, u.y);

    return mix(nxy0, nxy1, u.z);
}

float fbm(vec3 p) {
    float value = 0.0;
    float amplitude = 0.5;
    for (int i = 0; i < 5; i++) {
        value += amplitude * noise(p);
        p *= 1.9;
        amplitude *= 0.55;
    }
    return value;
}

// --- пресет "магма", тот же turbulence-эффект joltz0r, что и в sky_shader.frag,
// но здесь он сэмплируется прямо по экранным координатам (нет камеры/направления луча) ---
#define MAGMA_ITER 10
vec3 magmaColor(vec2 screenUv) {
    vec2 p = (screenUv - 0.5) * Scale * 8.0 - vec2(20.0);
    vec2 i = p;
    float c = 1.0;
    float inten = 0.05;
    float t0 = Time * Speed;
    for (int n = 0; n < MAGMA_ITER; n++) {
        float t = t0 * (0.5 - (2.0 / float(n + 1)));
        i = p + vec2(cos(t - i.x) + sin(t + i.y), sin(t - i.y) + cos(t + i.x));
        c += 1.0 / length(vec2(p.x / (sin(i.x + t) / inten), p.y / (cos(i.y + t) / inten)));
    }
    c /= float(MAGMA_ITER);
    c = 1.5 - sqrt(max(c, 0.0));

    vec3 base = vec3(c * c * c * c) + vec3(0.2 * sin(t0) + 0.5, 0.2, 0.4);
    base *= mix(0.6, 1.4, Density);
    return base * SkyColor * 3.5;
}

vec3 cloudsColor(vec2 screenUv) {
    float time = Time * Speed;
    vec3 flow = vec3(time * 0.12, -time * 0.045, time * 0.08);
    vec3 p = vec3(screenUv * Scale * 4.0, 0.0) + flow;

    float broad = fbm(p * 0.72);
    float wisps = fbm(p * 1.28 + vec3(broad * 1.8));
    float folds = 1.0 - abs(fbm(p * 1.55 - flow * 1.15) * 2.0 - 1.0);

    float density = smoothstep(0.48, 0.86, broad * 0.62 + wisps * 0.42 + folds * 0.18 + (Density - 0.55));
    float brightFolds = smoothstep(0.62, 0.96, folds * wisps);

    vec3 cloudShadow = SkyColor * 0.35;
    vec3 cloudLight = SkyColor * 0.85;
    vec3 clouds = mix(cloudShadow, cloudLight, clamp(wisps * 0.72 + brightFolds * 0.85, 0.0, 1.0));

    return mix(SkyColor * 0.3, clouds, density);
}

void main() {
    // экранные UV в диапазоне [0,1], не зависят от камеры - оверлей "плывёт" по экрану,
    // а не по предмету, это нормально для тонирующего эффекта поверх геометрии руки
    vec2 screenUv = gl_FragCoord.xy / ScreenSize;

    vec3 color = (SkyMode == 1) ? magmaColor(screenUv) : cloudsColor(screenUv);

    fragColor = vec4(color, clamp(Opacity, 0.0, 1.0));
}
