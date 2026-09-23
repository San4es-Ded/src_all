#version 150

uniform sampler2D Sampler0;
uniform vec4 fillColor;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    float maskAlpha = texture(Sampler0, TexCoord).a;
    if (maskAlpha < 0.01) {
        discard;
    }

    float alpha = clamp(maskAlpha, 0.0, 1.0) * fillColor.a;
    if (alpha < 0.002) {
        discard;
    }
    OutColor = vec4(fillColor.rgb, alpha);
}
