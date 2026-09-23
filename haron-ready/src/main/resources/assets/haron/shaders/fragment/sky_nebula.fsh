#version 150

in vec2 texCoord;
out vec4 fragColor;

// Используем точные названия униформ из твоей базы Haron
uniform float time;
uniform vec2 screenSize;
uniform vec4 baseColor;
uniform float alpha;

float noise(in vec2 p) {
    return sin(p.x * 0.5) * sin(p.y * 0.5);
}

float fbm(in vec2 p) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    mat2 rot = mat2(cos(0.5), sin(0.5), -sin(0.5), cos(0.5));
    for (int i = 0; i < 5; ++i) {
        v += a * noise(p);
        p = rot * p * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

void main() {
    // Приводим координаты экрана к нормальному виду на основе screenSize
    vec2 uv = (gl_FragCoord.xy * 2.0 - screenSize.xy) / screenSize.y;
    float t = time * 0.4; // Плавная скорость

    vec2 q = vec2(fbm(uv + vec2(0.0, 0.0)), fbm(uv + vec2(5.2, 1.3)));
    vec2 r = vec2(fbm(uv + 4.0 * q + vec2(1.7, 9.2) + t), fbm(uv + 4.0 * q + vec2(8.3, 2.8) + t));
    float f = fbm(uv + 4.0 * r);

    // Смешиваем цвета: baseColor из настроек клиента станет основным цветом туманности!
    vec3 spaceBackground = baseColor.rgb * 0.15; // Темный фон
    vec3 fireClouds = baseColor.rgb * 1.2;       // Яркие огненные облака
    vec3 highlights = vec3(0.35, 0.08, 0.3);     // Фиолетово-розовые завихрения для объема

    vec3 color = mix(spaceBackground, fireClouds, clamp(f * f * 4.0, 0.0, 1.0));
    color = mix(color, highlights, clamp(length(q), 0.0, 1.0));
    color = color * f * f * (3.5 - 2.0 * f);

    color = clamp(color, 0.0, 1.0);

    fragColor = vec4(color, alpha);
}