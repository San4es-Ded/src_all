#version 150

uniform sampler2D Sampler0;
uniform vec2 texelSize;
uniform float width;
uniform vec4 outlineColor;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    float thickness = max(0.5, width);
    vec2 texel = thickness * texelSize;

    float center = texture(Sampler0, TexCoord).a;
    float minimumAlpha = center;
    float maximumAlpha = center;
    vec2 directions[8];
    directions[0] = vec2(1.0, 0.0);
    directions[1] = vec2(-1.0, 0.0);
    directions[2] = vec2(0.0, 1.0);
    directions[3] = vec2(0.0, -1.0);
    directions[4] = vec2(0.707, 0.707);
    directions[5] = vec2(-0.707, 0.707);
    directions[6] = vec2(0.707, -0.707);
    directions[7] = vec2(-0.707, -0.707);
    for (int index = 0; index < 8; index++) {
        float alpha = texture(Sampler0, TexCoord + directions[index] * texel).a;
        minimumAlpha = min(minimumAlpha, alpha);
        maximumAlpha = max(maximumAlpha, alpha);
    }

    float edge = smoothstep(0.05, 0.5, maximumAlpha - minimumAlpha);
    float alpha = edge * outlineColor.a;
    if (alpha < 0.004) {
        discard;
    }
    OutColor = vec4(outlineColor.rgb, alpha);
}
