package rockstar.client.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.command.RenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Executes 1.21.11's extracted render commands against the vertex-consumer
 * provider selected by Rockstar's mask, glow and view-model passes.
 */
public final class LegacyRenderCompat {
    private LegacyRenderCompat() {
    }

    public static void renderItem(
        LivingEntity entity,
        ItemStack stack,
        ItemDisplayContext displayContext,
        boolean leftHand,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light
    ) {
        renderItem(entity, stack, displayContext, leftHand, matrices, vertexConsumers, entity.getEntityWorld(), light, OverlayTexture.DEFAULT_UV,
            entity.getId() + displayContext.ordinal());
    }

    public static void renderItem(
        LivingEntity entity,
        ItemStack stack,
        ItemDisplayContext displayContext,
        boolean leftHand,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        World world,
        int light,
        int overlay,
        int seed
    ) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        ItemRenderState state = new ItemRenderState();
        MinecraftClient.getInstance().getItemModelManager().clearAndUpdate(
            state, stack, normalizeHand(displayContext, leftHand), world, entity, seed
        );
        renderItemState(state, matrices, vertexConsumers, light, overlay, 0);
    }

    public static void renderItemState(
        ItemRenderState state,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        int overlay,
        int outlineColor
    ) {
        execute(vertexConsumers, queue -> state.render(matrices, queue, light, overlay, outlineColor));
    }

    public static void renderArm(
        PlayerEntityRenderer<?> renderer,
        boolean left,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        Identifier skinTexture,
        boolean sleeveVisible
    ) {
        execute(vertexConsumers, queue -> {
            if (left) renderer.renderLeftArm(matrices, queue, light, skinTexture, sleeveVisible);
            else renderer.renderRightArm(matrices, queue, light, skinTexture, sleeveVisible);
        });
    }

    public static void renderEntity(
        EntityRenderManager manager,
        Entity entity,
        float tickProgress,
        double x,
        double y,
        double z,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers
    ) {
        EntityRenderState state = manager.getAndUpdateRenderState(entity, tickProgress);
        execute(vertexConsumers, queue -> manager.render(
            state,
            MinecraftClient.getInstance().gameRenderer.getEntityRenderStates().cameraRenderState,
            x,
            y,
            z,
            matrices,
            queue
        ));
    }

    public static void execute(VertexConsumerProvider vertexConsumers, java.util.function.Consumer<OrderedRenderCommandQueue> submitter) {
        MinecraftClient client = MinecraftClient.getInstance();
        OrderedRenderCommandQueueImpl queue = new OrderedRenderCommandQueueImpl();
        DelegatingImmediate adapter = vertexConsumers instanceof VertexConsumerProvider.Immediate immediate
            ? null
            : new DelegatingImmediate(vertexConsumers);
        VertexConsumerProvider.Immediate immediate = adapter == null
            ? (VertexConsumerProvider.Immediate)vertexConsumers
            : adapter;
        RenderDispatcher dispatcher = new RenderDispatcher(
            queue,
            client.getBlockRenderManager(),
            immediate,
            client.getAtlasManager(),
            new OutlineVertexConsumerProvider(),
            immediate,
            client.textRenderer
        );
        try {
            submitter.accept(queue);
            dispatcher.render();
            dispatcher.endLayeredCustoms();
        } finally {
            dispatcher.close();
            if (adapter != null) adapter.closeAdapter();
        }
    }

    private static ItemDisplayContext normalizeHand(ItemDisplayContext context, boolean leftHand) {
        if (!leftHand) return context;
        return switch (context) {
            case FIRST_PERSON_RIGHT_HAND -> ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
            case THIRD_PERSON_RIGHT_HAND -> ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            default -> context;
        };
    }

    private static final class DelegatingImmediate extends VertexConsumerProvider.Immediate {
        private final VertexConsumerProvider delegate;

        private DelegatingImmediate(VertexConsumerProvider delegate) {
            super(new BufferAllocator(256), new Object2ObjectLinkedOpenHashMap<RenderLayer, BufferAllocator>());
            this.delegate = delegate;
        }

        @Override
        public VertexConsumer getBuffer(RenderLayer layer) {
            return this.delegate.getBuffer(layer);
        }

        @Override
        public void drawCurrentLayer() {
        }

        @Override
        public void draw() {
        }

        @Override
        public void draw(RenderLayer layer) {
        }

        private void closeAdapter() {
            this.allocator.close();
        }
    }
}
