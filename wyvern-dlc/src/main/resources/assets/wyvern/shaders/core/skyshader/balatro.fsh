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
uniform float uNight;
uniform float uThemeMix;

out vec4 fragColor;

mat3 rotX(float a) { float c=cos(a),s=sin(a); return mat3(1,0,0, 0,c,s, 0,-s,c); }
mat3 rotY(float a) { float c=cos(a),s=sin(a); return mat3(c,0,s, 0,1,0, -s,0,c); }
float hash(vec2 p) { return fract(sin(dot(p, vec2(127.1,311.7))) * 43758.5453); }

void main() {
    vec2 screen = gl_FragCoord.xy / uResolution * 2.0 - 1.0;
    screen.x *= uResolution.x / uResolution.y;
    float tanFov = tan(radians(uFov) * 0.5);
    vec3 ray = normalize(vec3(screen * tanFov, 1.0));
    ray = rotY(uCameraDir.x) * rotX(uCameraDir.y) * ray;

    vec2 sky = vec2(atan(ray.x, ray.z) / 6.2831853, asin(clamp(ray.y,-1.0,1.0)) / 3.1415926);
    float t = uTime * uSpeed;
    vec2 p = sky * vec2(7.0 + uScale * 0.32, 5.2 + uScale * 0.18);
    float angle = 0.22 * sin(t * 0.18);
    p = mat2(cos(angle),-sin(angle),sin(angle),cos(angle)) * p;

    float vortex = sin(length(p) * 2.9 - atan(p.y,p.x) * 4.0 - t * 0.9);
    float ribbon = smoothstep(0.20, 0.94, vortex * 0.5 + 0.5);
    float fine = 0.5 + 0.5 * sin(p.x * 5.0 + sin(p.y * 2.0 + t) * 2.2 - t * 1.4);

    vec2 cardCell = floor(p * 0.72);
    vec2 cardUv = fract(p * 0.72) - 0.5;
    cardUv += (hash(cardCell) - 0.5) * 0.12;
    float diamond = 1.0 - smoothstep(0.16, 0.205, abs(cardUv.x) + abs(cardUv.y));
    diamond *= step(0.69, hash(cardCell + 4.7));

    vec3 red = vec3(0.78, 0.035, 0.055);
    vec3 wine = vec3(0.16, 0.004, 0.025);
    vec3 themeRed = mix(uColor2 * 0.30, uColor, 0.74);
    red = mix(red, themeRed, uThemeMix);
    vec3 color = mix(vec3(0.008,0.002,0.009), wine, 0.48 + 0.38 * ray.y);
    color += red * ribbon * (0.22 + uIntensity * 0.72);
    color += vec3(1.0,0.18,0.10) * fine * ribbon * 0.055;
    color += vec3(1.0,0.58,0.22) * diamond * (0.32 + 0.18 * sin(t * 2.0 + hash(cardCell) * 6.28));
    color *= 1.0 - uRain * 0.18 - uThunder * 0.25;
    color *= 1.0 - uNight * 0.08;
    fragColor = vec4(color, uAlpha);
}
