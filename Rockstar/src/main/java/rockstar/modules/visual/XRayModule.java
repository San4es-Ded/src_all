package rockstar.modules.visual;








import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;
import pyrock.events.game.AncientDebrisEvent;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.setting.RegistryListSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.GameUtils;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.render.Render3DUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="XRay", category=ModuleCategory.VISUALS, internalMethod09633="modules.descriptions.xray")
public class XRayModule
extends Module {
    private final Set<BlockPos> internalField0546 = ConcurrentHashMap.newKeySet();
    private RegistryListSetting internalField0649;
    private static final Map<Block, ColorRGBA> internalField0543 = new HashMap<Block, ColorRGBA>();
    private final Map<BlockPos, Long> internalField0544 = new ConcurrentHashMap<BlockPos, Long>();
    private static final List<Block> internalField0416 = List.of(Blocks.ANCIENT_DEBRIS, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.NETHER_QUARTZ_ORE);
    private int internalField0227 = 0;
    private int internalField0228 = 0;
    private int internalField1053 = 0;
    private int internalField1055 = 0;
    private final EventListener<Render3DEvent> internalField0157 = render3DEvent -> {
        if (XRayModule.internalField0149.world == null || XRayModule.internalField0149.player == null) {
            return;
        }
        MatrixStack matrixStack = render3DEvent.getMatrices();
        Camera camera = XRayModule.internalField0149.gameRenderer.getCamera();
        Vec3d vec3d = camera.getCameraPos();
        matrixStack.push();
        matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.blendFunc((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE);
        RenderSystem.lineWidth((float)10.0f);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        double d = 999999.0;
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        for (BlockPos blockPos : this.internalField0546) {
            if (XRayModule.internalField0149.player.squaredDistanceTo(blockPos.toCenterPos()) > d) continue;
            Box box = this.internalMethod01063(blockPos);
            Block block = XRayModule.internalField0149.world.getBlockState(blockPos).getBlock();
            if (block != Blocks.ANCIENT_DEBRIS && this.internalMethod04131(blockPos)) {
                block = Blocks.ANCIENT_DEBRIS;
            }
            Render3DUtils.internalMethod02535(render3DEvent.getMatrices(), bufferBuilder, box, this.internalMethod03440(block).withAlpha(30.0f));
        }
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrixStack.pop();
    };
    private final EventListener<WorldChangeEvent> internalField0158 = worldChangeEvent -> {
        this.internalField0546.clear();
        this.internalField0544.clear();
    };
    private final EventListener<AncientDebrisEvent> internalField1028 = ancientDebrisEvent -> {
        if (!this.internalMethod09685() || !this.internalField0649.internalMethod05586(Blocks.ANCIENT_DEBRIS)) {
            return;
        }
        long l = System.currentTimeMillis() + 12000L;
        for (BlockPos blockPos : ancientDebrisEvent.getPositions()) {
            BlockPos blockPos2 = blockPos.toImmutable();
            this.internalField0544.put(blockPos2, l);
            this.internalField0546.add(blockPos2);
        }
    };

    private void internalMethod09684() {
        this.internalField0649 = new RegistryListSetting(this, "modules.settings.xray.big_block");
    }

    public XRayModule() {
        this.internalMethod09684();
        this.internalMethod09891();
    }

    public void internalMethod00622(WorldChunk worldChunk) {
        if (XRayModule.internalField0149.world == null || worldChunk == null) {
            return;
        }
        int n = worldChunk.getPos().getStartX();
        int n2 = worldChunk.getPos().getStartZ();
        for (int i = 0; i < 16; ++i) {
            for (int j = XRayModule.internalField0149.world.getBottomY(); j < XRayModule.internalField0149.world.getTopYInclusive(); ++j) {
                for (int k = 0; k < 16; ++k) {
                    BlockPos blockPos = new BlockPos(n + i, j, n2 + k);
                    BlockState blockState = worldChunk.getBlockState(blockPos);
                    if (blockState.isAir() || !this.internalMethod01139(blockState.getBlock())) continue;
                    this.internalField0546.add(blockPos);
                }
            }
        }
    }

    @Override
    public void onEnable() {
        if (!GameUtils.internalMethod00471()) {
            return;
        }
        this.internalMethod09891();
        this.internalField0546.clear();
        ClientChunkManager clientChunkManager = XRayModule.internalField0149.world.getChunkManager();
        int n = XRayModule.internalField0149.options != null ? (Integer)XRayModule.internalField0149.options.getViewDistance().getValue() : 8;
        Runnable runnable = this.internalMethod02570(n, clientChunkManager);
        if (internalField0149.isOnThread()) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
        } else {
            runnable.run();
        }
        super.onEnable();
    }

    @NotNull
    private Runnable internalMethod02570(int n, ClientChunkManager clientChunkManager) {
        int n2 = Math.max(1, n);
        int n3 = XRayModule.internalField0149.player.getChunkPos().x;
        int n4 = XRayModule.internalField0149.player.getChunkPos().z;
        Runnable runnable = () -> {
            for (int i = -n2; i <= n2; ++i) {
                for (int j = -n2; j <= n2; ++j) {
                    WorldChunk worldChunk = clientChunkManager.getChunk(n3 + i, n4 + j, ChunkStatus.FULL, false);
                    if (worldChunk == null) continue;
                    this.internalMethod00622(worldChunk);
                }
            }
        };
        return runnable;
    }

    @Override
    public void onDisable() {
        this.internalField0546.clear();
        this.internalField0544.clear();
        this.internalField0227 = 0;
        this.internalField0228 = 0;
        this.internalField1053 = 0;
        this.internalField1055 = 0;
        super.onDisable();
    }

    @Override
    public void internalMethod08229() {
        this.internalMethod09883();
        this.internalMethod09882();
        this.internalMethod09686();
        super.internalMethod08229();
    }

    private void internalMethod09686() {
        this.internalField0546.removeIf(blockPos -> !this.internalMethod05532((BlockPos)blockPos));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void internalMethod09882() {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        Set<BlockPos> set = this.internalField0546;
        synchronized (set) {
            for (BlockPos blockPos : this.internalField0546) {
                Block block = XRayModule.internalField0149.world.getBlockState(blockPos).getBlock();
                if (block == Blocks.DIAMOND_ORE && this.internalField0649.internalMethod05586(Blocks.DIAMOND_ORE)) {
                    ++n;
                    continue;
                }
                if (block == Blocks.ANCIENT_DEBRIS) {
                    ++n2;
                    continue;
                }
                if (block == Blocks.GOLD_ORE && this.internalField0649.internalMethod05586(Blocks.GOLD_ORE)) {
                    ++n3;
                    continue;
                }
                if (block != Blocks.LAPIS_ORE || !this.internalField0649.internalMethod05586(Blocks.LAPIS_ORE)) continue;
                ++n4;
            }
        }
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField1053 = n3;
        this.internalField1055 = n4;
    }

    public boolean internalMethod01139(Block block) {
        return this.internalField0649.internalMethod05586(block);
    }

    private ColorRGBA internalMethod03440(Block block) {
        return internalField0543.getOrDefault(block, ThemeColors.internalField1312);
    }

    private boolean internalMethod05532(BlockPos blockPos) {
        if (XRayModule.internalField0149.player == null || XRayModule.internalField0149.options == null) {
            return false;
        }
        int n = (Integer)XRayModule.internalField0149.options.getViewDistance().getValue();
        double d = (double)Math.max(1, n + 1) * 16.0;
        double d2 = d * d;
        return XRayModule.internalField0149.player.squaredDistanceTo(Vec3d.ofCenter((Vec3i)blockPos)) <= d2;
    }

    public boolean internalMethod09685() {
        if (XRayModule.internalField0149.world == null) {
            return false;
        }
        if (XRayModule.internalField0149.world.getRegistryKey() != World.NETHER) {
            return false;
        }
        return ServerUtils.internalMethod01786(KnownServer.internalField0578) || ServerUtils.internalMethod01786(KnownServer.internalField1218) || ServerUtils.internalMethod01786(KnownServer.internalField0579);
    }

    public boolean internalMethod04131(BlockPos blockPos) {
        return this.internalField0544.containsKey(blockPos);
    }

    private void internalMethod09883() {
        if (this.internalField0544.isEmpty()) {
            return;
        }
        long l = System.currentTimeMillis();
        this.internalField0544.entrySet().removeIf(entry -> {
            if ((Long)entry.getValue() <= l) {
                this.internalField0546.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }

    private Box internalMethod01063(BlockPos blockPos) {
        BlockState blockState = XRayModule.internalField0149.world.getBlockState(blockPos);
        VoxelShape voxelShape = blockState.getOutlineShape((BlockView)XRayModule.internalField0149.world, blockPos);
        if (voxelShape.isEmpty()) {
            return new Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(blockPos);
        }
        return voxelShape.getBoundingBox().offset(blockPos);
    }

    private static void internalMethod07074(Block block, ColorRGBA colorRGBA) {
        internalField0543.put(block, colorRGBA);
    }

    private void internalMethod09891() {
        if (this.internalField0649.internalMethod02610() > 0) {
            return;
        }
        internalField0416.forEach(this.internalField0649::internalMethod03936);
    }

    @Generated
    public Set<BlockPos> internalMethod01204() {
        return this.internalField0546;
    }

    @Generated
    public RegistryListSetting internalMethod03929() {
        return this.internalField0649;
    }

    @Generated
    public int internalMethod08996() {
        return this.internalField0227;
    }

    @Generated
    public int internalMethod08997() {
        return this.internalField0228;
    }

    @Generated
    public int internalMethod09006() {
        return this.internalField1053;
    }

    @Generated
    public int internalMethod09007() {
        return this.internalField1055;
    }

    static {
        XRayModule.internalMethod07074(Blocks.ANCIENT_DEBRIS, new ColorRGBA(255.0f, 131.0f, 54.0f));
        XRayModule.internalMethod07074(Blocks.DIAMOND_ORE, new ColorRGBA(121.0f, 54.0f, 255.0f));
        XRayModule.internalMethod07074(Blocks.DEEPSLATE_DIAMOND_ORE, new ColorRGBA(145.0f, 92.0f, 255.0f));
        XRayModule.internalMethod07074(Blocks.EMERALD_ORE, new ColorRGBA(80.0f, 255.0f, 140.0f));
        XRayModule.internalMethod07074(Blocks.DEEPSLATE_EMERALD_ORE, new ColorRGBA(64.0f, 214.0f, 119.0f));
        XRayModule.internalMethod07074(Blocks.GOLD_ORE, new ColorRGBA(255.0f, 215.0f, 0.0f));
        XRayModule.internalMethod07074(Blocks.DEEPSLATE_GOLD_ORE, new ColorRGBA(255.0f, 191.0f, 0.0f));
        XRayModule.internalMethod07074(Blocks.NETHER_GOLD_ORE, new ColorRGBA(255.0f, 203.0f, 96.0f));
        XRayModule.internalMethod07074(Blocks.IRON_ORE, new ColorRGBA(210.0f, 210.0f, 210.0f));
        XRayModule.internalMethod07074(Blocks.DEEPSLATE_IRON_ORE, new ColorRGBA(180.0f, 180.0f, 180.0f));
        XRayModule.internalMethod07074(Blocks.LAPIS_ORE, new ColorRGBA(0.0f, 71.0f, 179.0f));
        XRayModule.internalMethod07074(Blocks.DEEPSLATE_LAPIS_ORE, new ColorRGBA(21.0f, 92.0f, 200.0f));
        XRayModule.internalMethod07074(Blocks.REDSTONE_ORE, new ColorRGBA(255.0f, 64.0f, 64.0f));
        XRayModule.internalMethod07074(Blocks.DEEPSLATE_REDSTONE_ORE, new ColorRGBA(214.0f, 48.0f, 48.0f));
        XRayModule.internalMethod07074(Blocks.COPPER_ORE, new ColorRGBA(255.0f, 140.0f, 80.0f));
        XRayModule.internalMethod07074(Blocks.DEEPSLATE_COPPER_ORE, new ColorRGBA(235.0f, 120.0f, 68.0f));
        XRayModule.internalMethod07074(Blocks.COAL_ORE, new ColorRGBA(84.0f, 84.0f, 84.0f));
        XRayModule.internalMethod07074(Blocks.DEEPSLATE_COAL_ORE, new ColorRGBA(64.0f, 64.0f, 64.0f));
        XRayModule.internalMethod07074(Blocks.NETHER_QUARTZ_ORE, new ColorRGBA(233.0f, 233.0f, 233.0f));
    }
}
