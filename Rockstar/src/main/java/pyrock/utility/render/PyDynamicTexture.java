package pyrock.utility.render;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import moscow.rockstar.mixin.accessors.NativeImageAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.NativeImage.Format;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryUtil;
import rockstar.client.RockstarClient;

public final class PyDynamicTexture implements AutoCloseable {
   private static final AtomicInteger IDS = new AtomicInteger();
   private final int width;
   private final int height;
   private final NativeImage image;
   private final NativeImageBackedTexture texture;
   private final Identifier identifier;
   private final ByteBuffer pixels;
   private boolean closed;

   public PyDynamicTexture(String localValue1, int localValue2, int localValue3) {
      if (localValue2 > 0 && localValue3 > 0 && (long)localValue2 * localValue3 <= 16777216L) {
         this.width = localValue2;
         this.height = localValue3;
         this.image = new NativeImage(Format.RGBA, localValue2, localValue3, false);
         this.texture = new NativeImageBackedTexture(() -> "Rockstar PyDynamicTexture", this.image);
         rockstar.client.render.TextureCompat.setFilter(this.texture, false, false);
         this.identifier = RockstarClient.id("scripts/dynamic/" + sanitize(localValue1) + "_" + IDS.incrementAndGet());
         this.pixels = MemoryUtil.memByteBuffer(((NativeImageAccessor)(Object)this.image).getPointer(), localValue2 * localValue3 * 4);
         MinecraftClient.getInstance().getTextureManager().registerTexture(this.identifier, this.texture);
      } else {
         throw new IllegalArgumentException("invalid dynamic texture size: " + localValue2 + "x" + localValue3);
      }
   }

   public Identifier identifier() {
      return this.identifier;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }

   public void update(Object localValue1) {
      if (this.closed) {
         throw new IllegalStateException("dynamic texture is closed");
      } else if (localValue1 instanceof byte[] localValue2) {
         if (localValue2.length != this.pixels.capacity()) {
            throw new IllegalArgumentException("expected " + this.pixels.capacity() + " RGBA bytes, got " + localValue2.length);
         } else {
            this.pixels.clear();
            this.pixels.put(localValue2);
            this.pixels.clear();
            this.texture.upload();
         }
      } else {
         throw new IllegalArgumentException("dynamic texture pixels must be bytes");
      }
   }

   @Override
   public void close() {
      if (!this.closed) {
         this.closed = true;
         MinecraftClient.getInstance().getTextureManager().destroyTexture(this.identifier);
      }
   }

   private static String sanitize(String localValue0) {
      String localValue1 = localValue0 == null ? "texture" : localValue0.toLowerCase().replaceAll("[^a-z0-9/._-]", "_").replaceAll("_+", "_").replaceAll("^_+|_+$", "");
      return localValue1.isBlank() ? "texture" : localValue1;
   }
}
