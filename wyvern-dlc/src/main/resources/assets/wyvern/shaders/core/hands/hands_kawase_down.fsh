#version 150

uniform sampler2D Sampler0;
uniform vec2 uOffset;
uniform vec2 uHalfPixel;
uniform vec2 uSize;

in vec2 TexCoord;
out vec4 OutColor;

vec4 sampleSafe(vec2 uv) {
    if (uv.x <= 0.0 || uv.y <= 0.0 || uv.x >= 1.0 || uv.y >= 1.0) return vec4(0.0);
    return texture(Sampler0, uv);
}

void main() {
    vec2 uv = TexCoord;
    vec2 halfPixel = uHalfPixel * uOffset;

    vec4 sum = sampleSafe(uv) * 4.0;
    sum += sampleSafe(uv - halfPixel);
    sum += sampleSafe(uv + halfPixel);
    sum += sampleSafe(uv + vec2(halfPixel.x, -halfPixel.y));
    sum += sampleSafe(uv - vec2(halfPixel.x, -halfPixel.y));

    OutColor = sum / 8.0;
}
