package haron.effects;

import net.minecraft.util.math.Box;

final class BoxFadeSample {
    final Box box;
    final float alpha;

    BoxFadeSample(Box box, float f) {
        this.box = box;
        this.alpha = f;
    }
}
