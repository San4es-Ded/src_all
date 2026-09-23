#version 330 core

in vec2 uv;
out vec4 finalColor;

uniform sampler2D PrevSampler;
uniform sampler2D InjectSampler;

layout(std140) uniform HandTrailUniforms {
    vec2 TexelSize;
    float TrailLength;
    float TrailSpeed;
    float Time;
    float DeltaTime;
};

float wob(float y, float time) {
    return sin(y * 9.0 + time * 4.3) * 0.6
         + sin(y * 17.0 - time * 2.1) * 0.3
         + sin(y * 4.0 + time * 1.3) * 0.4
         + sin(y * 28.0 + time * 5.7) * 0.15;
}

void main() {
    float speed = clamp(TrailSpeed, 0.1, 5.0);
    float strength = clamp(TrailLength, 0.0, 1.0);
    float dt = clamp(DeltaTime, 0.0005, 0.05);
    float frameScale = dt * 60.0;
    float horizontalWave = sin(Time * 1.35) * (0.00045 + strength * 0.00135);
    float turbulence = (wob(uv.y, Time) - wob(uv.y, Time - dt))
            * 0.018 * dt;
    float sway = (horizontalWave + turbulence) * speed * frameScale;
    float rise = TexelSize.y * speed * (0.7 + uv.y * 0.8) * frameScale;
    vec2 previousUv = uv - vec2(sway, rise);

    vec4 history = vec4(0.0);
    if (previousUv.x > 0.0 && previousUv.y > 0.0
            && previousUv.x < 1.0 && previousUv.y < 1.0) {
        history = texture(PrevSampler, previousUv) * 0.36;
        history += texture(PrevSampler, previousUv + vec2(TexelSize.x, 0.0)) * 0.16;
        history += texture(PrevSampler, previousUv - vec2(TexelSize.x, 0.0)) * 0.16;
        history += texture(PrevSampler, previousUv + vec2(0.0, TexelSize.y)) * 0.16;
        history += texture(PrevSampler, previousUv - vec2(0.0, TexelSize.y)) * 0.16;
    }

    float oldAlpha = history.a;
    float retention = pow(mix(0.89, 0.991, strength), frameScale);
    float newAlpha = max(
        0.0,
        oldAlpha * retention - mix(0.030, 0.0017, strength) * frameScale
    );
    float historyScale = oldAlpha > 0.001 ? newAlpha / oldAlpha : 0.0;
    history = vec4(history.rgb * historyScale, newAlpha);

    float injection = texture(InjectSampler, uv).a;
    float emission = min(1.0, smoothstep(0.015, 0.28, injection) * 1.10);
    finalColor = max(history, vec4(emission));
}
