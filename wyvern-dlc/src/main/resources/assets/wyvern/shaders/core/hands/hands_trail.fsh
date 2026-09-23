#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform vec2 texelSize;
uniform float trailLength;
uniform float trailSpeed;
uniform float time;
uniform float deltaTime;

in vec2 TexCoord;
out vec4 OutColor;

float wob(float y, float t) {
    return sin(y * 9.0 + t * 4.3) * 0.6
         + sin(y * 17.0 - t * 2.1) * 0.3
         + sin(y * 4.0 + t * 1.3) * 0.4
         + sin(y * 28.0 + t * 5.7) * 0.15;
}

void main() {
    float speed = clamp(trailSpeed, 0.1, 5.0);
    float strength = clamp(trailLength, 0.0, 1.0);
    float dt = clamp(deltaTime, 0.0005, 0.05);
    float frameScale = dt * 60.0;
    float horizontalWave = sin(time * 1.35) * (0.00045 + strength * 0.00135);
    float turbulence = (wob(TexCoord.y, time) - wob(TexCoord.y, time - dt))
            * 0.018 * dt;
    float sway = (horizontalWave + turbulence) * speed * frameScale;
    float rise = texelSize.y * speed * (0.7 + TexCoord.y * 0.8) * frameScale;
    vec2 previousUv = TexCoord - vec2(sway, rise);

    vec4 history = vec4(0.0);
    if (previousUv.x > 0.0 && previousUv.y > 0.0
            && previousUv.x < 1.0 && previousUv.y < 1.0) {
        history = texture(Sampler0, previousUv) * 0.36;
        history += texture(Sampler0, previousUv + vec2(texelSize.x, 0.0)) * 0.16;
        history += texture(Sampler0, previousUv - vec2(texelSize.x, 0.0)) * 0.16;
        history += texture(Sampler0, previousUv + vec2(0.0, texelSize.y)) * 0.16;
        history += texture(Sampler0, previousUv - vec2(0.0, texelSize.y)) * 0.16;
    }

    float oldAlpha = history.a;
    float retention = pow(mix(0.89, 0.991, strength), frameScale);
    float newAlpha = max(
        0.0,
        oldAlpha * retention - mix(0.030, 0.0017, strength) * frameScale
    );
    float historyScale = oldAlpha > 0.001 ? newAlpha / oldAlpha : 0.0;
    history = vec4(history.rgb * historyScale, newAlpha);

    float injection = texture(Sampler1, TexCoord).a;
    float emission = min(1.0, smoothstep(0.015, 0.28, injection) * 1.10);
    OutColor = max(history, vec4(emission));
}
