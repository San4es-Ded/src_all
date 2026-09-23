#version 150

uniform vec4 u_Color;
uniform vec4 u_Color2;
uniform vec2 u_Resolution;
uniform float u_Scale;
uniform float u_Time;
uniform float u_Fov;
uniform vec2 u_CameraDir;

out vec4 fragColor;

#define TAU 6.28318530718
#define PI 3.14159265
#define MAX_ITER 5
#define INTENSITY 0.005

mat3 rotX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(1.0, 0.0, 0.0,
                0.0,   c,   s,
                0.0,  -s,   c);
}

mat3 rotY(float a) {
    float c = cos(a), s = sin(a);
    return mat3(  c, 0.0,   s,
                0.0, 1.0, 0.0,
                 -s, 0.0,   c);
}

void main() {
    vec2 uv = gl_FragCoord.xy / max(u_Resolution.xy, vec2(1.0));
    vec2 sp = uv * 2.0 - 1.0;
    float aspect = u_Resolution.x / max(u_Resolution.y, 1.0);
    float tanV = tan(radians(u_Fov) * 0.5);
    vec3 rayView = normalize(vec3(sp.x * tanV * aspect, sp.y * tanV, 1.0));
    vec3 ray = normalize(rotY(u_CameraDir.x) * rotX(u_CameraDir.y) * rayView);

    vec3 basisX = normalize(vec3(0.74, 0.18, 0.65));
    vec3 basisY = normalize(vec3(-0.22, 0.96, 0.18));
    vec2 p = vec2(dot(ray, basisX), dot(ray, basisY)) * max(u_Scale, 0.001) * TAU - 250.0;

    vec2 iterPos = p;
    float value = 1.0;
    float timeOffset = u_Time * 0.5 + 23.0;

    for (int n = 0; n < MAX_ITER; n++) {
        float iterTime = timeOffset * (1.0 - (3.5 / float(n + 1)));
        iterPos = p + vec2(
            cos(iterTime - iterPos.x) + sin(iterTime + iterPos.y),
            sin(iterTime - iterPos.y) + cos(iterTime + iterPos.x)
        );

        float sinX = sin(iterPos.x + iterTime);
        float cosY = cos(iterPos.y + iterTime);
        float compX = p.x * INTENSITY / max(abs(sinX), 0.001);
        float compY = p.y * INTENSITY / max(abs(cosY), 0.001);
        value += 1.0 / max(sqrt(compX * compX + compY * compY), 0.001);
    }

    value = 1.17 - pow(value / float(MAX_ITER), 1.4);
    float pwr = value * value;
    pwr = pwr * pwr;
    pwr = pwr * pwr;

    vec3 baseColor = vec3(abs(pwr));
    vec3 enhanced = clamp(baseColor + vec3(0.0, 0.35, 0.5), 0.0, 1.0);
    float luminance = dot(enhanced, vec3(0.299, 0.587, 0.114));
    vec3 themeMix = mix(u_Color.rgb, u_Color2.rgb, clamp(luminance, 0.0, 1.0));
    vec3 color = vec3(luminance) * themeMix * 1.4;

    fragColor = vec4(clamp(color, 0.0, 1.0), u_Color.a);
}
