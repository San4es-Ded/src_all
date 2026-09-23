#version 150

uniform sampler2D Sampler0;
uniform float saturation;

in vec2 TexCoord;
out vec4 OutColor;

void main() {
    vec4 scene = texture(Sampler0, TexCoord);
    float luminance = dot(scene.rgb, vec3(0.2126, 0.7152, 0.0722));
    vec3 graded = mix(vec3(luminance), scene.rgb, saturation);

    // Softly compress oversaturated highlights instead of clipping individual
    // channels, preserving detail in skies, particles and enchanted items.
    float peak = max(graded.r, max(graded.g, graded.b));
    if (peak > 1.0) {
        graded /= peak;
    }
    OutColor = vec4(max(graded, vec3(0.0)), scene.a);
}
