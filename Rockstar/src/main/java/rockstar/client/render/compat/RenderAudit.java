package rockstar.client.render.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BuiltBuffer;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/** On-demand one-frame diagnostics; no per-frame allocations while idle. */
public final class RenderAudit {
    private static boolean requested;
    private static JsonArray collecting;
    private static JsonArray lastFrame = new JsonArray();

    private RenderAudit() { }

    public static JsonObject request() {
        requested = true;
        JsonObject result = new JsonObject();
        result.addProperty("nextFrameRequested", true);
        result.add("lastFrame", lastFrame);
        return result;
    }

    public static void beginFrame() {
        if (requested) { collecting = new JsonArray(); requested = false; }
    }

    public static void record(BuiltBuffer mesh, RenderPipeline pipeline, Framebuffer target, Matrix4f view,
                              boolean gui, Vector4f color) {
        if (collecting == null || collecting.size() >= 256) return;
        JsonObject draw = new JsonObject();
        draw.addProperty("pipeline", pipeline.getLocation().toString());
        draw.addProperty("gui", gui);
        draw.addProperty("target", target == MinecraftClient.getInstance().getFramebuffer() ? "main" : target.textureWidth + "x" + target.textureHeight);
        draw.addProperty("depth", pipeline.getDepthTestFunction().name());
        draw.addProperty("cull", pipeline.isCull());
        draw.addProperty("blend", pipeline.getBlendFunction().map(Object::toString).orElse("opaque"));
        draw.addProperty("color", color.toString());
        draw.addProperty("modelView", view.toString());
        Matrix4f projection = rockstar.client.compat.RenderSystem.getProjectionMatrix();
        draw.addProperty("projection", projection.toString());
        var bytes = mesh.getBuffer();
        Vector4f vertex = new Vector4f(bytes.getFloat(0), bytes.getFloat(4), bytes.getFloat(8), 1);
        draw.addProperty("firstVertex", vertex.toString());
        projection.transform(view.transform(vertex));
        draw.addProperty("clipVertex", vertex.toString());
        draw.addProperty("vertices", mesh.getDrawParameters().vertexCount());
        collecting.add(draw);
    }

    public static void endFrame() {
        if (collecting != null) { lastFrame = collecting; collecting = null; }
    }
}
