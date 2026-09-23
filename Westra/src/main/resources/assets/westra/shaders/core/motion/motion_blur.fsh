#version 150

// Перенос post/motion_blur.fsh из Pulse. Оригинал держал параметры в блоке
// layout(std140) uniform MotionBlurParams — в формате шейдеров 1.21.4 таких блоков нет,
// поэтому блок разложен на обычные uniform'ы. Сама математика не менялась.

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform mat4 MvInverse;
uniform mat4 ProjInverse;
uniform mat4 PrevModelView;
uniform mat4 PrevProjection;
uniform vec4 CameraPos;
uniform vec4 PrevCameraPos;
uniform vec2 ViewRes;
uniform float BlendFactor;
uniform float InverseSamples;
uniform float HandDepthThreshold;
uniform float SampleCount;
uniform float HalfSamples;
uniform float BlurAlgorithm;

in vec2 TexCoord;
out vec4 OutColor;

vec3 reproject(vec3 screen_pos) {
    vec3 ndc = screen_pos * 2.0 - 1.0;
    vec4 view_pos4 = ProjInverse * vec4(ndc, 1.0);
    vec3 view_pos = view_pos4.xyz / view_pos4.w;

    vec3 world_pos = (MvInverse * vec4(view_pos, 1.0)).xyz + (CameraPos.xyz - PrevCameraPos.xyz);
    vec4 prev_proj = PrevProjection * (PrevModelView * vec4(world_pos, 1.0));

    return (prev_proj.xyz / prev_proj.w) * 0.5 + 0.5;
}

vec2 clampLength(vec2 velocity) {
    float lenSq = dot(velocity, velocity);
    return (lenSq > 0.16) ? velocity * (0.4 * inversesqrt(lenSq)) : velocity;
}

float noise(vec2 pos) {
    return fract(52.9829189 * fract(0.06711056 * pos.x + 0.00583715 * pos.y));
}

void main() {
    ivec2 texel = ivec2(gl_FragCoord.xy);

    float depth = texelFetch(Sampler1, texel, 0).x;

    if (depth < HandDepthThreshold) {
        OutColor = texture(Sampler0, TexCoord);
        return;
    }

    vec2 velocity = TexCoord - reproject(vec3(TexCoord, depth)).xy;
    velocity = clampLength(velocity);

    vec2 totalOffset = BlendFactor * velocity;
    vec2 baseStep = totalOffset * InverseSamples;

    vec3 color_sum = vec3(0.0);
    vec2 seed = TexCoord * ViewRes;

    bool centerBlur = BlurAlgorithm > 0.5;
    int samples = int(SampleCount);
    for (int i = 0; i < samples; ++i) {
        float fi = float(i);

        float jitter = noise(seed + vec2(fi, fi * 1.4));
        float offset_centered = fi - HalfSamples;
        float sample_index = centerBlur ? offset_centered : fi;
        float sample_offset = sample_index + jitter;

        vec2 pos = TexCoord + sample_offset * baseStep;
        vec3 c = texture(Sampler0, pos).rgb;

        color_sum += c * c;
    }
    OutColor = vec4(sqrt(color_sum * InverseSamples), 1.0);
}
