#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform vec4 glassColor;
uniform float mirror;
uniform float waveTime;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    vec2 px = 1.0 / vec2(textureSize(Sampler1, 0));
    float maskAlpha = texture(Sampler1, TexCoord).a;
    // Dilate the mask by one pixel so the waved glass never leaves a raw-scene
    // gap along its silhouette and small holes cannot make it blink out.
    maskAlpha = max(maskAlpha,
            max(texture(Sampler1, TexCoord + vec2(px.x, 0.0)).a,
            max(texture(Sampler1, TexCoord - vec2(px.x, 0.0)).a,
            max(texture(Sampler1, TexCoord + vec2(0.0, px.y)).a,
                 texture(Sampler1, TexCoord - vec2(0.0, px.y)).a))));
    if (maskAlpha < 0.01) {
        discard;
    }

    // Wavy refraction through the glass: displace the scene sampling with two
    // travelling sine ripples, clamped so the glass never samples outside.
    vec2 sceneUv = TexCoord;
    vec2 wave = vec2(
            sin(sceneUv.y * 14.0 + waveTime * 2.2),
            cos(sceneUv.x * 18.0 + waveTime * 1.8)
    ) * 0.025;
    sceneUv = clamp(sceneUv + wave, 0.0, 1.0);
    if (mirror > 0.5) {
        sceneUv.x = 1.0 - sceneUv.x;
    }
    vec3 scene = texture(Sampler0, sceneUv).rgb;
    float tintAmount = clamp(glassColor.a, 0.0, 1.0) * 0.5;
    vec3 tinted = mix(scene, glassColor.rgb, tintAmount);
    OutColor = vec4(clamp(tinted, 0.0, 1.0), clamp(maskAlpha, 0.0, 1.0));
}