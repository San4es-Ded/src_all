#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform vec3 color;
uniform vec3 color2;
uniform float exposure;
uniform float useSource;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    vec2 uv = TexCoord;
    vec4 bloom = texture(Sampler0, uv);
    vec4 mask = texture(Sampler1, uv);
    float outer = bloom.a * (1.0 - mask.a);
    vec3 grad = mix(color, color2, uv.y);
    // useSource: bloom carries the actual blurred hand/item pixels; recover their
    // color (alpha-normalized) and re-saturate (blurring washes colors toward grey)
    // so the glow inherits the item's true colors per pixel.
    float srcMode = clamp(useSource, 0.0, 1.0);
    vec3 src = clamp(bloom.rgb / max(bloom.a, 0.001), 0.0, 1.0);
    float lum = dot(src, vec3(0.299, 0.587, 0.114));
    src = clamp(mix(vec3(lum), src, 1.45), 0.0, 1.0);
    grad = mix(grad, src, srcMode);
    float softHalo = pow(max(outer, 0.0), 0.88);
    float brightRim = smoothstep(0.20, 0.68, bloom.a) * (1.0 - mask.a);
    float edgeFade = smoothstep(0.018, 0.070, 1.0 - uv.y);
    float intensity = clamp((softHalo * 0.32 + brightRim * 0.68) * exposure, 0.0, 0.9) * edgeFade;
    if (intensity <= 0.001) discard;
    // In source mode keep the rim boost gentle: the old 2x boost clipped saturated
    // item colors straight to white.
    float boost = mix(0.58 + brightRim * 0.75, 0.72 + brightRim * 0.28, srcMode);
    OutColor = vec4(grad * boost, intensity);
}
