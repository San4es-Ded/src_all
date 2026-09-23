package haron.effects;

import net.minecraft.util.math.Vec3d;

public class ParticleRenderData {
    final Vec3d position;
    final float size;
    final float rotationDegrees;
    final int red;
    final int green;
    final int blue;
    final int alpha;
    final float averageBrightness;

    ParticleRenderData(Vec3d position, float size, float rotationDegrees, int red, int green, int blue, int alpha, float averageBrightness) {
        this.position = position;
        this.size = size;
        this.rotationDegrees = rotationDegrees;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.averageBrightness = averageBrightness;
    }

}
