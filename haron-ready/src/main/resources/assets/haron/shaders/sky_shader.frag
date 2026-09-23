#version 150

in vec2 vClip;
out vec4 fragColor;

uniform float Time;
uniform float Speed;
uniform float Density;
uniform float Scale;
uniform vec3 SkyColor;
uniform vec2 ScreenSize;

// 0 = процедурные грозовые облака (по умолчанию), 1 = магма/турбулентность
uniform int SkyMode;

// ориентация камеры в мировых координатах
uniform float CamYaw;
uniform float CamPitch;
uniform float CamFovRad;

// --- простой value noise, написан с нуля ---
// hash без sin() и без произведения координат: обе операции на некоторых GPU
// теряют точность на больших входных числах (потеря точности при range reduction
// внутри sin(), либо потеря точности при перемножении координат) и дают
// резкие плоские прямоугольные разрывы на границах ячеек шума.
// Вместо этого - серия dot()+fract() шагов, каждый из которых работает
// только с числами в диапазоне [0,1), поэтому точность не деградирует
// независимо от того, как далеко улетела мировая координата.
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

// fractal brownian motion - мягкие клубящиеся слои шума друг на друге
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

// строим направление луча камеры в мировом пространстве по yaw/pitch/fov,
// используя стандартную формулу Minecraft (yaw=0 -> юг +Z, pitch>0 -> смотрим вниз),
// так что облака "прибиты" к небесной сфере, а не к экрану
vec3 getWorldRayDir() {
    float aspect = ScreenSize.x / ScreenSize.y;
    float tanHalfFov = tan(CamFovRad * 0.5);

    float sy = sin(CamYaw),   cy = cos(CamYaw);
    float sp = sin(CamPitch), cp = cos(CamPitch);

    // forward = направление взгляда камеры (формула Minecraft: yaw=0 -> +Z, pitch>0 -> вниз)
    vec3 forward = vec3(-sy * cp, -sp, cy * cp);
    // right и up строим относительно мирового "вверх" (0,1,0), стандартный camera basis
    vec3 right = normalize(cross(forward, vec3(0.0, 1.0, 0.0)));
    vec3 up = cross(right, forward);

    vec3 dir = forward
        + right * (vClip.x * aspect * tanHalfFov)
        + up * (vClip.y * tanHalfFov);

    return normalize(dir);
}

// --- пресет "магма": turbulence-эффект joltz0r (glslsandbox.com/e#108691.0), ---
// адаптирован под наши uniform-имена и 3D направление луча вместо 2D surfacePosition
#define MAGMA_ITER 10
vec3 magmaSky(vec3 dir) {
    // берём горизонтальный срез направления как 2D-плоскость, как в оригинале -
    // получаем турбулентность, "обёрнутую" вокруг игрока по кругу
    vec2 p = dir.xz / max(0.15, abs(dir.y) + 0.15) * Scale - vec2(20.0);
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
    // Density и SkyColor из наших настроек продолжают влиять и на этот пресет,
    // чтобы GUI оставался согласованным между стилями
    base *= mix(0.6, 1.4, Density);
    return base * SkyColor * 3.5;
}

void main() {
    vec3 dir = getWorldRayDir();

    if (SkyMode == 1) {
        fragColor = vec4(magmaSky(dir), 1.0);
        return;
    }

    float time = Time * Speed;

    // луч, движущийся во времени тремя разными скоростями по осям -
    // так слои шума ползут не по прямой, а закручиваются друг относительно друга
    vec3 flow = vec3(time * 0.12, -time * 0.045, time * 0.08);
    vec3 p = dir * (Scale * 0.72) + flow;

    // три октавы шума разного масштаба, каждая по-своему деформирует предыдущую -
    // даёт клубящийся, "штормовой" рисунок вместо равномерных пятен
    float broad = fbm(p * 0.72);
    float wisps = fbm(p * 1.28 + vec3(broad * 1.8));
    float folds = 1.0 - abs(fbm(p * 1.55 - flow * 1.15) * 2.0 - 1.0);

    float density = smoothstep(0.48, 0.86, broad * 0.62 + wisps * 0.42 + folds * 0.18 + (Density - 0.55));
    float brightFolds = smoothstep(0.62, 0.96, folds * wisps);

    // тёмный зенит, более светлый горизонт - классический грозовой градиент неба
    float horizon = clamp(dir.y * 0.62 + 0.46, 0.0, 1.0);
    vec3 deepSky = SkyColor * 0.45;
    vec3 horizonSky = SkyColor * 0.65;
    vec3 base = mix(horizonSky, deepSky, smoothstep(0.0, 0.82, horizon));

    vec3 cloudShadow = SkyColor * 0.35;
    vec3 cloudLight = SkyColor * 0.85;
    vec3 clouds = mix(cloudShadow, cloudLight, clamp(wisps * 0.72 + brightFolds * 0.85, 0.0, 1.0));

    vec3 color = mix(base, clouds, density);

    fragColor = vec4(color, 1.0);
}
