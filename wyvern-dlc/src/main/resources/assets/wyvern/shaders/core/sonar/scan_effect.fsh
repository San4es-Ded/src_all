#version 150

uniform mat4 invViewMat;
uniform mat4 invProjMat;
uniform vec3 pos;
uniform vec3 center;
uniform float radius;
uniform sampler2D depthTex;

uniform float width;
uniform float sharpness;
uniform vec4 outerColor;
uniform vec4 midColor;
uniform vec4 innerColor;
uniform vec4 scanlineColor;
uniform int DebugMode;
uniform vec4 ColorModulator;

in vec2 texCoord;
out vec4 OutColor;

float scanlines() {
    float first = fract(gl_FragCoord.y * 0.23);
    float second = fract((gl_FragCoord.y + gl_FragCoord.x * 0.045) * 0.165);
    float band1 = smoothstep(0.02, 0.22, first) * (1.0 - smoothstep(0.58, 0.86, first));
    float band2 = smoothstep(0.03, 0.18, second) * (1.0 - smoothstep(0.62, 0.88, second));
    return clamp(band1 * 0.82 + band2 * 0.36, 0.0, 1.0);
}

vec3 worldPosition(vec2 uv, float depth) {
    float z = depth * 2.0 - 1.0;
    vec4 clipPosition = vec4(uv * 2.0 - 1.0, z, 1.0);
    vec4 viewPosition = invProjMat * clipPosition;
    viewPosition /= max(viewPosition.w, 1e-6);
    vec4 worldPositionValue = invViewMat * viewPosition;
    return pos + worldPositionValue.xyz;
}

void main() {
    vec4 color = vec4(0.0);
    if (DebugMode == 3) {
        OutColor = vec4(1.0, 0.0, 1.0, 0.75);
        return;
    }

    vec2 uv = texCoord;
    float depth = texture(depthTex, uv).r;
    if (DebugMode == 1) {
        float value = depth >= 1.0 ? 0.0 : 1.0 - depth;
        OutColor = vec4(value, value * 0.35, 0.0, 0.85);
        return;
    }
    if (depth >= 1.0) {
        OutColor = vec4(0.0);
        return;
    }

    vec3 point = worldPosition(uv, depth);
    float distanceToCenter = distance(point, center);
    if (DebugMode == 2) {
        float ringMask = distanceToCenter < radius && distanceToCenter > radius - width ? 1.0 : 0.0;
        OutColor = vec4(ringMask * 0.45, ringMask * 0.72, ringMask, ringMask * 0.92);
        return;
    }

    if (distanceToCenter < radius && distanceToCenter > radius - width) {
        float difference = 1.0 - (radius - distanceToCenter) / max(width, 1e-5);
        difference = clamp(difference, 0.0, 1.0);

        float edgePower = pow(difference, max(1.0, sharpness * 0.35));
        float line = scanlines();
        float lineMask = 0.20 + 0.80 * line;
        float bodyMask = smoothstep(0.02, 0.22, difference);
        float edgeMask = smoothstep(0.82, 1.0, difference);

        vec4 gradient = mix(innerColor, midColor, pow(difference, 0.80));
        gradient = mix(gradient, outerColor, edgePower);
        vec4 stripe = scanlineColor * (0.28 + 0.72 * line) * (0.48 + 0.52 * bodyMask);

        color = gradient * (0.38 + 0.62 * lineMask) + stripe;
        color.rgb += outerColor.rgb * edgeMask * 0.42;
        color.a *= bodyMask * (0.50 + 0.50 * lineMask);
        color.rgb *= 1.35;
        color.a = min(color.a * 1.18, 1.0);
    }

    OutColor = color;
}
