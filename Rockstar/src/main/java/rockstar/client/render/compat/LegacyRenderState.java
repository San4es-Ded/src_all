package rockstar.client.render.compat;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;

/** Immutable fixed-function state captured when a legacy mesh is submitted. */
public record LegacyRenderState(BlendFunction blend, DepthTestFunction depthTest,
                                boolean depthWrite, boolean cull, boolean writeColor, boolean writeAlpha) {
    public RenderPipeline.Builder apply(RenderPipeline.Builder builder) {
        if (blend == null) builder.withoutBlend();
        else builder.withBlend(blend);
        return builder.withDepthTestFunction(depthTest).withDepthWrite(depthWrite)
            .withCull(cull).withColorWrite(writeColor, writeAlpha);
    }
}
