#version 150

uniform vec4 u_Color;
uniform vec2 u_Resolution;
uniform float u_Scale;
uniform float u_Time;
uniform float u_Fov;
uniform vec2 u_CameraDir;

out vec4 fragColor;

mat3 rotX(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(1.0, 0.0, 0.0,
                0.0, c, s,
                0.0, -s, c);
}

mat3 rotY(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(c, 0.0, s,
                0.0, 1.0, 0.0,
                -s, 0.0, c);
}

void main() {
    vec2 resolution = max(u_Resolution, vec2(1.0));
    vec2 screen = gl_FragCoord.xy / resolution * 2.0 - 1.0;
    screen.x *= resolution.x / resolution.y;
    float tanFov = tan(radians(u_Fov) * 0.5);
    vec3 vWorldDir = normalize(vec3(screen * tanFov, 1.0));
    vWorldDir = normalize(rotY(u_CameraDir.x) * rotX(u_CameraDir.y) * vWorldDir);

    vec3 dir = normalize(vWorldDir);
    vec2 wc = vec2(atan(dir.x, dir.z), asin(clamp(dir.y, -1.0, 1.0))) * u_Scale;
    vec2 p = wc * 0.05;
    float pLen = length(p);
    float angle = atan(p.y, p.x + 1e-6) + u_Time * 0.2 + 302.2 - 20.0 * (0.5 * pLen + 0.5);
    p = vec2(pLen * cos(angle), pLen * sin(angle)) * 30.0;
    float spd = u_Time * 0.5;
    vec2 p2 = p;
    for (int i = 0; i < 5; i++) {
        p2 += sin(max(p.x, p.y)) + p;
        p += 0.5 * vec2(cos(5.1123314 + 0.353 * p2.y + spd * 0.131121), sin(p2.x - 0.113 * spd));
        p -= cos(p.x + p.y) - sin(p.x * 0.711 - p.y);
    }

    float contrastMod = 0.375 + 1.45;
    float paintRes = clamp(length(p) * 0.035 * contrastMod, 0.0, 2.0);
    float c1p = max(0.0, 1.0 - contrastMod * abs(1.0 - paintRes));
    float c2p = max(0.0, 1.0 - contrastMod * abs(paintRes));
    float c3p = 1.0 - min(1.0, c1p + c2p);
    float light = 0.5 * max(c1p * 5.0 - 4.0, 0.0) + 0.7 * max(c2p * 5.0 - 4.0, 0.0);
    vec3 col1 = u_Color.rgb;
    vec3 col2 = u_Color.rgb * vec3(0.5, 1.2, 0.8);
    vec3 col3 = u_Color.rgb * vec3(1.2, 0.6, 1.1);
    vec3 col = (0.13 * col1) + (0.87 * (col1 * c1p + col2 * c2p + col3 * c3p)) + vec3(light);
    fragColor = vec4(clamp(col, 0.0, 1.0), u_Color.a);
}
