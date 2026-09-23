#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform vec3 color;
uniform vec3 color2;
uniform vec3 itemColorL;
uniform vec3 itemColorR;
uniform vec2 itemColorValid;
uniform float intensity;
uniform float emission;
uniform float thickness;
uniform float useSource;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    // Sampler0 = размытые руки (kawase), альфа даёт покрытие шлейфа.
    // Sampler1 = 2x1 текстура со средним цветом предмета каждой руки:
    // левая половина экрана — вторая рука, правая — основная. Линейная
    // фильтрация плавно перетекает между цветами рук в центре экрана.
    vec4 tex = texture(Sampler0, TexCoord);
    // Thin trails are emitted mostly from the sharp hand/item mask instead of
    // the whole bloom cloud. This prevents feedback from growing into a halo.
    float sharpCov = texture(Sampler2, TexCoord).a;
    float broadness = pow(clamp(thickness, 0.0, 1.0), 1.35);
    float cov = mix(sharpCov, tex.a, broadness);
    // Even the minimum density must leave a readable trail during a fast
    // attack swing. Density controls how airy it is, but never turns the
    // source into an almost fully transparent one-frame flash.
    float effectiveEmission = mix(0.20, 1.0, clamp(emission, 0.0, 1.0));
    float a = clamp(cov * intensity, 0.0, 1.0) * effectiveEmission;
    if (a <= 0.001) discard;
    vec3 grad = mix(color, color2, TexCoord.y);
    // itemColorL/R — цвет предмета каждой руки, посчитанный на CPU из спрайта
    // предмета. Не зависит от позы/анимации рук (SwingAnimation HMI и т.п.).
    // GPU-усреднение из Sampler1 остаётся фолбэком для предметов без
    // стандартного спрайта; пустая рука откатывается на градиент темы.
    float side = smoothstep(0.42, 0.58, TexCoord.x);
    vec4 hand = texture(Sampler1, vec2(TexCoord.x, 0.5));
    vec3 gpuCol = clamp(hand.rgb, 0.0, 1.0);
    vec3 cpuCol = clamp(mix(itemColorL, itemColorR, side), 0.0, 1.0);
    float cpuValid = clamp(mix(itemColorValid.x, itemColorValid.y, side), 0.0, 1.0);
    vec3 src = mix(mix(grad, gpuCol, clamp(hand.a, 0.0, 1.0)), cpuCol, cpuValid);
    // Усреднение тянет цвет к серому; поднимаем насыщенность, чтобы шлейф
    // оставался ярко окрашенным в цвет предмета.
    float lum = dot(src, vec3(0.299, 0.587, 0.114));
    src = clamp(mix(vec3(lum), src, 1.35), 0.0, 1.0);
    vec3 col = mix(grad, src, clamp(useSource, 0.0, 1.0));
    OutColor = vec4(col * a, a);
}
