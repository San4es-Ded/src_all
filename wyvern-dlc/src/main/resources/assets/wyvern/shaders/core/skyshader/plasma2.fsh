#version 150

uniform vec2 u_Resolution;
uniform float u_Time;
uniform float u_Fov;
uniform vec2 u_CameraDir;
uniform float u_Scale;
uniform float u_Intensity;
uniform float u_Alpha;
uniform vec3 u_Color;
uniform vec3 u_Color2;

out vec4 fragColor;

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

float hash(vec3 p) {
    p = fract(p * 0.1031);
    p += dot(p, p.yzx + 33.33);
    return fract((p.x + p.y) * p.z);
}

float valueNoise(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    vec3 u = f * f * (3.0 - 2.0 * f);
    return mix(
        mix(mix(hash(i), hash(i + vec3(1.0, 0.0, 0.0)), u.x),
            mix(hash(i + vec3(0.0, 1.0, 0.0)), hash(i + vec3(1.0, 1.0, 0.0)), u.x), u.y),
        mix(mix(hash(i + vec3(0.0, 0.0, 1.0)), hash(i + vec3(1.0, 0.0, 1.0)), u.x),
            mix(hash(i + vec3(0.0, 1.0, 1.0)), hash(i + vec3(1.0, 1.0, 1.0)), u.x), u.y),
        u.z);
}

float fbm(vec3 p) {
    float value = 0.0;
    float amplitude = 0.55;
    for (int i = 0; i < 5; ++i) {
        value += amplitude * valueNoise(p);
        p = p * 2.03 + vec3(17.3, 9.1, 13.7);
        amplitude *= 0.5;
    }
    return value;
}

void main() {
    vec2 uv = gl_FragCoord.xy / max(u_Resolution, vec2(1.0));
    vec2 screen = uv * 2.0 - 1.0;
    float aspect = u_Resolution.x / max(u_Resolution.y, 1.0);
    float tanV = tan(radians(u_Fov) * 0.5);
    vec3 rayView = normalize(vec3(screen.x * tanV * aspect, screen.y * tanV, 1.0));
    vec3 ray = normalize(rotY(u_CameraDir.x) * rotX(u_CameraDir.y) * rayView);

    vec3 primary = clamp(u_Color, 0.0, 1.0);
    vec3 secondary = clamp(u_Color2, 0.0, 1.0);
    vec3 softAccent = mix(primary, secondary, 0.28);
    float spatialScale = mix(1.1, 4.8, clamp((u_Scale - 1.0) / 19.0, 0.0, 1.0));
    float t = u_Time * 0.012;
    // Rotate the whole cloud field around the vertical sky axis and twist
    // different elevations at slightly different speeds. This creates a
    // visible vortex without attaching the effect to the player's screen.
    float spin = u_Time * 0.085;
    vec3 flowRay = rotY(spin) * ray;
    flowRay = rotY(flowRay.y * 1.35 + sin(u_Time * 0.045) * 0.18) * flowRay;
    vec3 contourRay = rotY(-spin * 0.58 + ray.y * 0.82) * ray;

    // The MainMenu smoke rebuilt in 3D so the sky is seamless and remains
    // anchored while the camera turns.
    float smokeA = fbm(flowRay * spatialScale * 1.9 + vec3(t, -t * 0.6, t * 0.35));
    float smokeB = fbm(flowRay * spatialScale * 3.1
                     + vec3(-t * 0.7, -t * 0.4, t * 0.5) + 41.0);
    float smoke = smoothstep(0.42, 0.98, smokeA * 0.72 + smokeB * 0.28);
    float upperSky = smoothstep(-0.65, 0.8, ray.y);
    float smokeMask = mix(0.48, 1.0, upperSky);

    vec3 base = mix(vec3(0.006) + primary * 0.004,
                    vec3(0.009) + softAccent * 0.016, upperSky);
    vec3 smokeColor = mix(primary * 0.16, softAccent * 0.25,
                          smoothstep(0.3, 1.0, smoke));
    base += smokeColor * smoke * smokeMask * 0.9;

    float field = fbm(contourRay * spatialScale * 2.6 + vec3(7.7, 3.3, 5.1) + t * 0.15);
    float bands = abs(fract(field * 9.0) - 0.5);
    float contour = 1.0 - smoothstep(0.02, 0.075, bands);
    float contourMask = smoothstep(0.15, 0.95, abs(ray.y));
    base += contour * contourMask * softAccent * 0.105;

    float energy = mix(0.72, 1.48,
            clamp((u_Intensity - 0.1) / 0.9, 0.0, 1.0));
    fragColor = vec4(clamp(base * energy, 0.0, 1.0), clamp(u_Alpha, 0.0, 1.0));
}
