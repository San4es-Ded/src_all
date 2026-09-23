package su.sacura.util.impl.render.engine;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector4i;

public interface DrawEngine {
  void quad(Matrix4f paramMatrix4f, BufferBuilder paramBufferBuilder, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  void quad(Matrix4f paramMatrix4f, BufferBuilder paramBufferBuilder, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt);
  
  void quadTexture(MatrixStack.Entry paramEntry, BufferBuilder paramBufferBuilder, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Vector4i paramVector4i);
  
  void quad(Matrix4f paramMatrix4f, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt);
}


