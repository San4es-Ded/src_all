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

float blossom(vec2 uv, float seed) {
    float a = atan(uv.y, uv.x) + seed * 6.2831;
    float r = length(uv);
    float petals = 0.105 + 0.044 * cos(a * 5.0);
    float flower = 1.0 - smoothstep(petals - 0.018, petals + 0.014, r);
    float center = 1.0 - smoothstep(0.020, 0.052, r);
    return flower + center * 0.45;
}

float blossomLayer(vec2 sky, float t, float scale, float layer) {
    vec2 p = sky * vec2(15.0, 9.0) * scale;
    p += vec2(t * (0.15 + layer * 0.04), -t * (0.045 + layer * 0.015));
    vec2 cell = floor(p);
    vec2 uv = fract(p) - 0.5;
    float seed = hash(cell + layer * 17.3);
    uv.x += sin(t * 0.8 + seed * 9.0) * 0.13;
    uv.y += cos(t * 0.55 + seed * 7.0) * 0.08;
    uv *= 1.25 + seed * 1.35;
    return blossom(uv, seed) * step(0.38, seed);
}

void main() {
    vec2 screen = gl_FragCoord.xy / uResolution * 2.0 - 1.0;
    screen.x *= uResolution.x / uResolution.y;
    float tanFov = tan(radians(uFov) * 0.5);
    vec3 ray = normalize(vec3(screen * tanFov, 1.0));
    ray = rotY(uCameraDir.x) * rotX(uCameraDir.y) * ray;

    vec2 sky = vec2(atan(ray.x, ray.z) / 6.2831853, asin(clamp(ray.y,-1.0,1.0)) / 3.1415926);
    float t = uTime * uSpeed;
    float horizon = smoothstep(-0.48, 0.60, ray.y);
    vec3 bottom = vec3(0.95, 0.56, 0.72);
    vec3 top = vec3(0.46, 0.61, 0.94);
    bottom = mix(bottom, uColor, uThemeMix * 0.72);
    top = mix(top, uColor2, uThemeMix * 0.72);
    vec3 color = mix(bottom, top, horizon);

    float cloud = sin(sky.x * (26.0 + uScale) + sin(sky.y * 12.0) + t * 0.12) * 0.5 + 0.5;
    cloud *= smoothstep(0.32, 0.92, cloud) * (1.0 - abs(ray.y) * 0.35);
    color += vec3(1.0,0.82,0.90) * cloud * (0.07 + uIntensity * 0.12);

    float flowersFar = blossomLayer(sky, t, 0.72, 1.0);
    float flowersNear = blossomLayer(sky + vec2(0.13,-0.07), t, 1.08, 2.0);
    vec3 petalPink = vec3(1.0, 0.73, 0.84);
    color = mix(color, petalPink, clamp(flowersFar * 0.55 + flowersNear * 0.88, 0.0, 0.92));
    color += vec3(1.0,0.93,0.72) * flowersNear * 0.16;

    color *= 1.0 - uRain * 0.15 - uThunder * 0.22;
    color = mix(color, color * vec3(0.44,0.48,0.70), uNight * 0.48);
    fragColor = vec4(color, uAlpha);
}
