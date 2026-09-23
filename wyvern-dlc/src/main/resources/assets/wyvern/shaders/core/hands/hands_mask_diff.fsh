#version 150

uniform sampler2D Sampler0; // scene after hands
uniform sampler2D Sampler1; // scene before hands
uniform sampler2D Sampler2; // depth before hands
uniform sampler2D Sampler3; // depth after hands
uniform vec2 texelSize;

in vec2 TexCoord;
out vec4 OutColor;

float rawDepth(vec2 uv) {
    float depthBefore = texture(Sampler2, uv).r;
    float depthAfter = texture(Sampler3, uv).r;
    return smoothstep(0.000002, 0.000035, depthBefore - depthAfter);
}

float rawColor(vec2 uv) {
    vec3 afterColor = texture(Sampler0, uv).rgb;
    vec3 beforeColor = texture(Sampler1, uv).rgb;
    vec3 delta = abs(afterColor - beforeColor);
    float colorDiff = max(delta.r, max(delta.g, delta.b));
    return smoothstep(0.045, 0.145, colorDiff);
}

float seededMask(vec2 uv) {
    float depth = rawDepth(uv);
    float nearbyDepth = depth;
    vec2 px = texelSize * 1.5;
    nearbyDepth = max(nearbyDepth, rawDepth(clamp(uv + vec2( px.x, 0.0), texelSize, vec2(1.0) - texelSize)));
    nearbyDepth = max(nearbyDepth, rawDepth(clamp(uv + vec2(-px.x, 0.0), texelSize, vec2(1.0) - texelSize)));
    nearbyDepth = max(nearbyDepth, rawDepth(clamp(uv + vec2(0.0,  px.y), texelSize, vec2(1.0) - texelSize)));
    nearbyDepth = max(nearbyDepth, rawDepth(clamp(uv + vec2(0.0, -px.y), texelSize, vec2(1.0) - texelSize)));
    nearbyDepth = max(nearbyDepth, rawDepth(clamp(uv + vec2( px.x,  px.y), texelSize, vec2(1.0) - texelSize)));
    nearbyDepth = max(nearbyDepth, rawDepth(clamp(uv + vec2(-px.x,  px.y), texelSize, vec2(1.0) - texelSize)));
    nearbyDepth = max(nearbyDepth, rawDepth(clamp(uv + vec2( px.x, -px.y), texelSize, vec2(1.0) - texelSize)));
    nearbyDepth = max(nearbyDepth, rawDepth(clamp(uv + vec2(-px.x, -px.y), texelSize, vec2(1.0) - texelSize)));
    // Depth is the trusted hand seed. Color may fill glint/transparent hand pixels only
    // next to that seed, so unrelated HUD and animated world pixels cannot enter the mask.
    return max(depth, rawColor(uv) * smoothstep(0.04, 0.32, nearbyDepth));
}

void main() {
    vec2 uv = TexCoord;
    float center = seededMask(uv);
    float support = seededMask(uv + vec2(texelSize.x, 0.0))
                  + seededMask(uv - vec2(texelSize.x, 0.0))
                  + seededMask(uv + vec2(0.0, texelSize.y))
                  + seededMask(uv - vec2(0.0, texelSize.y));

    // Keep the mask contiguous.  The previous hard coherence gate could punch
    // tiny holes through the hand fill, exposing bright scene pixels as dots.
    float coherent = smoothstep(0.18, 1.15, support);
    // First-person hands naturally continue past the bottom of the viewport.
    // Gating uv.y here cut their wrists/items into a visible straight line.
    // First-person hands may continue through the side edges. Fading the mask
    // there visibly cuts wide hand animations before the viewport boundary.
    float sealed = smoothstep(0.8, 2.2, support);
    float mask = clamp(max(center * coherent, sealed), 0.0, 1.0);

    OutColor = vec4(mask, mask, mask, mask);
}
