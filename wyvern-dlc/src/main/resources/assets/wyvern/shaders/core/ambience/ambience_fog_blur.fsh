#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform float FogDensity;
uniform float HorizonCenter;
uniform float HorizonSoftness;
uniform vec3 FogColor;
uniform vec3 FogColorSecondary;
uniform float FogTint;

in vec2 TexCoord;
out vec4 OutColor;

vec3 softLight(vec3 base, vec3 blend) {
    vec3 result;
    result.r = blend.r <= 0.5 ? base.r - (1.0 - 2.0 * blend.r) * base.r * (1.0 - base.r)
                               : base.r + (2.0 * blend.r - 1.0) * (sqrt(base.r) - base.r);
    result.g = blend.g <= 0.5 ? base.g - (1.0 - 2.0 * blend.g) * base.g * (1.0 - base.g)
                               : base.g + (2.0 * blend.g - 1.0) * (sqrt(base.g) - base.g);
    result.b = blend.b <= 0.5 ? base.b - (1.0 - 2.0 * blend.b) * base.b * (1.0 - base.b)
                               : base.b + (2.0 * blend.b - 1.0) * (sqrt(base.b) - base.b);
    return result;
}

void main() {
    vec2 uv = TexCoord;
    vec3 sharp = texture(Sampler0, uv).rgb;
    vec3 blurred = texture(Sampler1, uv).rgb;

    float distToHorizon = uv.y - HorizonCenter;
    float aboveHorizon = smoothstep(-HorizonSoftness * 0.42, HorizonSoftness * 1.02, distToHorizon);
    float fogFactor = clamp(pow(aboveHorizon, 0.88) * FogDensity, 0.0, 0.96);

    vec3 themeNear = mix(FogColorSecondary * 0.72, FogColorSecondary, 0.55);
    vec3 themeFar = mix(FogColor, FogColor * 1.08 + FogColorSecondary * 0.12, 0.35);
    vec3 themeFog = mix(themeNear, themeFar, pow(fogFactor, 0.78));

    float tint = clamp(FogTint, 0.0, 1.0);
    vec3 coloredBlur = mix(blurred, themeFog, tint * 0.88);
    coloredBlur = mix(coloredBlur, softLight(coloredBlur, themeFog), tint * 0.42);

    vec3 fogged = mix(sharp, coloredBlur, fogFactor);
    fogged = mix(fogged, themeFog, fogFactor * fogFactor * tint * 0.62);

    OutColor = vec4(fogged, 1.0);
}
