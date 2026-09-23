#version 150

#moj_import <wyvern:common.glsl>

in vec2 FragCoord;
in vec2 TexCoord;
in vec4 FragColor;

uniform sampler2D Sampler0;
uniform vec2 Size;
uniform vec2 InvResolution;
uniform vec4 Radius;
uniform float Smoothness;
uniform float BlurRadius;

out vec4 OutColor;

// Eight precomputed directions avoid doing trigonometry for every blurred pixel.
// Three weighted rings retain a smooth, wide blur with 24 texture reads instead
// of the previous 80 reads.
const vec2 DIRECTIONS[8] = vec2[8](
    vec2( 1.0,  0.0),
    vec2( 0.70710678,  0.70710678),
    vec2( 0.0,  1.0),
    vec2(-0.70710678,  0.70710678),
    vec2(-1.0,  0.0),
    vec2(-0.70710678, -0.70710678),
    vec2( 0.0, -1.0),
    vec2( 0.70710678, -0.70710678)
);

void main() {
    float mask = ralpha(Size, FragCoord, Radius, Smoothness);
    if (mask <= 0.0) {
        discard;
    }

    vec2 texelRadius = vec2(BlurRadius) * InvResolution;
    vec3 average = texture(Sampler0, TexCoord).rgb * 4.0;

    for (int i = 0; i < 8; ++i) {
        vec2 offset = DIRECTIONS[i] * texelRadius;
        average += texture(Sampler0, TexCoord + offset * 0.33).rgb * 2.0;
        average += texture(Sampler0, TexCoord + offset * 0.66).rgb;
        average += texture(Sampler0, TexCoord + offset).rgb;
    }
    average /= 36.0;

    vec4 color = vec4(average, 1.0) * FragColor;
    color.a *= mask;

    OutColor = color;
}
