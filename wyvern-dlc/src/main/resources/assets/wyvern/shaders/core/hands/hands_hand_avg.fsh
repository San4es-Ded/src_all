#version 150

uniform sampler2D Sampler0;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    // Выход 2x1: левый пиксель — средний цвет предмета в левой половине экрана
    // (вторая рука), правый — в правой (основная рука). Вес по насыщенности,
    // чтобы цвет предмета перевешивал кожу самой руки.
    float x0 = TexCoord.x < 0.5 ? 0.0 : 0.5;
    vec3 sum = vec3(0.0);
    float wsum = 0.0;
    float csum = 0.0;
    for (int i = 0; i < 32; i++) {
        for (int j = 0; j < 32; j++) {
            vec2 uv = vec2(x0 + (float(i) + 0.5) * (0.5 / 32.0), (float(j) + 0.5) / 32.0);
            vec4 c = texture(Sampler0, uv);
            if (c.a < 0.5) continue;
            float mx = max(c.r, max(c.g, c.b));
            float mn = min(c.r, min(c.g, c.b));
            float sat = (mx - mn) / max(mx, 0.001);
            float w = 0.04 + sat * sat * (0.25 + mx);
            sum += c.rgb * w;
            wsum += w;
            csum += 1.0;
        }
    }
    // Рука пуста / вне кадра — альфа 0, шлейф откатится на цвет темы.
    if (csum < 4.0) {
        OutColor = vec4(0.0);
        return;
    }
    OutColor = vec4(sum / max(wsum, 0.0001), 1.0);
}
