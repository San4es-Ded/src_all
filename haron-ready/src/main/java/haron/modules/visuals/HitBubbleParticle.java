package haron.modules.visuals;

final class HitBubbleParticle {
    final double x;
    final double y;
    final double z;
    final String textureKey;
    final long spawnTime;

    HitBubbleParticle(double d, double d2, double d3, String string, long l) {
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.textureKey = string;
        this.spawnTime = l;
    }
}

