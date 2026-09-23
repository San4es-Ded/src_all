package rockstar.client.esp;





import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.render.*;
import globals.client.Information;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import pyrock.events.game.GameTickEvent;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ColorSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.esp.EspFeature;
import rockstar.client.esp.PlayerTargetType;
import rockstar.client.ui.ThemeColors;
import rockstar.client.esp.EntityTargetType;
import rockstar.client.internal.render.JumpShockwaveRenderer;

public class JumpCircleEspFeature
extends EspFeature {
    private final BooleanSetting internalField0650 = this.internalMethod02236("esp.jump_circles");
    private final BooleanSetting internalField0651 = this.internalMethod02651((typedValue091, typedValue164) -> new BooleanSetting((SettingOwner)typedValue091, "theme.sync", () -> !typedValue164.internalMethod04496()).internalMethod06630());
    private final ColorSetting internalField0665 = this.internalMethod04340("theme.sync", (typedValue091, typedValue164, typedValue165) -> new ColorSetting(typedValue091, "esp.jump_circles.color", () -> !typedValue164.internalMethod04496() || typedValue165.internalMethod04496()).internalMethod04886(ThemeColors.internalMethod02531()));
    private final SliderSetting internalField0383 = this.internalMethod02651((typedValue091, typedValue164) -> new SliderSetting((SettingOwner)typedValue091, "esp.jump_circles.radius", () -> !typedValue164.internalMethod04496()).internalMethod05900(0.5f).internalMethod02732(2.5f).internalMethod08673(0.1f).internalMethod08074(1.5f));
    private final SliderSetting internalField0382 = this.internalMethod02651((typedValue091, typedValue164) -> new SliderSetting((SettingOwner)typedValue091, "esp.jump_circles.width", () -> !typedValue164.internalMethod04496()).internalMethod05900(0.05f).internalMethod02732(1.5f).internalMethod08673(0.05f).internalMethod08074(1.0f));
    private final SliderSetting internalField1142 = this.internalMethod02651((typedValue091, typedValue164) -> new SliderSetting((SettingOwner)typedValue091, "esp.jump_circles.strength", () -> !typedValue164.internalMethod04496()).internalMethod05900(0.1f).internalMethod02732(2.0f).internalMethod08673(0.1f).internalMethod08074(2.0f));
    private final SliderSetting internalField1140 = this.internalMethod02651((typedValue091, typedValue164) -> new SliderSetting((SettingOwner)typedValue091, "esp.jump_circles.expand", () -> !typedValue164.internalMethod04496()).internalMethod05900(100.0f).internalMethod02732(800.0f).internalMethod08673(25.0f).internalMethod08074(800.0f));
    private final SliderSetting internalField1141 = this.internalMethod02651((typedValue091, typedValue164) -> new SliderSetting((SettingOwner)typedValue091, "esp.jump_circles.fade", () -> !typedValue164.internalMethod04496()).internalMethod05900(200.0f).internalMethod02732(1500.0f).internalMethod08673(25.0f).internalMethod08074(1500.0f));
    private final CopyOnWriteArrayList<InternalType0169> internalField0204 = new CopyOnWriteArrayList();
    private final Map<UUID, InternalType0170> internalField0543 = new HashMap<UUID, InternalType0170>();
    private final JumpShockwaveRenderer internalField0338 = new JumpShockwaveRenderer();
    private final EventListener<GameTickEvent> internalField0157 = gameTickEvent -> {
        if (JumpCircleEspFeature.internalField0149.world == null || JumpCircleEspFeature.internalField0149.player == null) {
            return;
        }
        if (!this.internalMethod06968()) {
            return;
        }
        HashSet<UUID> hashSet = new HashSet<UUID>();
        for (PlayerEntity playerEntity : JumpCircleEspFeature.internalField0149.world.getPlayers()) {
            PlayerTargetType typedValue092;
            boolean bl;
            UUID uUID = playerEntity.getUuid();
            hashSet.add(uUID);
            boolean bl2 = playerEntity.isOnGround();
            double d = playerEntity.getY();
            InternalType0170 nestedValue0073 = this.internalField0543.get(uUID);
            if (nestedValue0073 == null) {
                this.internalField0543.put(uUID, new InternalType0170(bl2, playerEntity.getX(), d, playerEntity.getZ(), d));
                continue;
            }
            if (bl2) {
                nestedValue0073.internalField0193 = playerEntity.getX();
                nestedValue0073.internalField1045 = d;
                nestedValue0073.internalField1043 = playerEntity.getZ();
            }
            boolean bl3 = bl = d - nestedValue0073.internalField0194 > 0.02 || playerEntity.getVelocity().y > 0.0;
            if (nestedValue0073.internalField0277 && !bl2 && bl && this.internalMethod06170(typedValue092 = JumpCircleEspFeature.internalMethod05051(playerEntity))) {
                this.internalMethod02663(typedValue092, nestedValue0073.internalField0193, this.internalMethod00038(nestedValue0073.internalField0193, nestedValue0073.internalField1045, nestedValue0073.internalField1043), nestedValue0073.internalField1043);
            }
            nestedValue0073.internalField0277 = bl2;
            nestedValue0073.internalField0194 = d;
        }
        this.internalField0543.keySet().retainAll(hashSet);
    };
    private final EventListener<WorldChangeEvent> internalField0158 = worldChangeEvent -> {
        this.internalField0204.clear();
        this.internalField0543.clear();
    };
    private final EventListener<Render3DEvent> internalField1028 = render3DEvent -> {
        if (this.internalField0204.isEmpty()) {
            return;
        }
        if (!this.internalMethod06968()) {
            this.internalMethod06433();
            return;
        }
        List<JumpShockwaveRenderer.InternalType0218> list = this.internalMethod02814(render3DEvent.getCamera());
        if (list.isEmpty()) {
            return;
        }
        Matrix4f matrix4f = new Matrix4f((Matrix4fc)render3DEvent.getProjectionMatrix()).mul((Matrix4fc)render3DEvent.getPositionMatrix()).invert();
        this.internalField0338.internalMethod01211(matrix4f, list);
    };

    public JumpCircleEspFeature() {
        super("jump_circles", EntityTargetType.internalField0027);
        this.internalMethod02197(PlayerTargetType.internalField0026);
        this.internalField0338.internalMethod04237();
    }

    private void internalMethod02663(PlayerTargetType typedValue092, double d, double d2, double d3) {
        ColorRGBA colorRGBA;
        BooleanSetting typedValue164 = (BooleanSetting)this.internalMethod02672("theme.sync", typedValue092);
        ColorSetting typedValue167 = (ColorSetting)this.internalMethod02672("esp.jump_circles.color", typedValue092);
        SliderSetting typedValue174 = (SliderSetting)this.internalMethod02672("esp.jump_circles.radius", typedValue092);
        SliderSetting typedValue175 = (SliderSetting)this.internalMethod02672("esp.jump_circles.width", typedValue092);
        SliderSetting typedValue176 = (SliderSetting)this.internalMethod02672("esp.jump_circles.strength", typedValue092);
        SliderSetting typedValue177 = (SliderSetting)this.internalMethod02672("esp.jump_circles.expand", typedValue092);
        SliderSetting typedValue178 = (SliderSetting)this.internalMethod02672("esp.jump_circles.fade", typedValue092);
        if (typedValue174 == null || typedValue175 == null || typedValue176 == null || typedValue177 == null || typedValue178 == null) {
            return;
        }
        colorRGBA = typedValue164 != null && typedValue164.internalMethod04496()
            ? ThemeColors.internalMethod02531()
            : (typedValue167 != null ? typedValue167.internalMethod05620() : null);
        if (colorRGBA == null) {
            colorRGBA = ThemeColors.internalMethod02531();
        }
        int n = colorRGBA.getRGB();
        float f = (float)(n >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n & 0xFF) / 255.0f;
        float f4 = (float)(n >>> 24 & 0xFF) / 255.0f;
        this.internalField0204.add(new InternalType0169(d, d2, d3, typedValue174.internalMethod08576(), typedValue175.internalMethod08576(), (long)typedValue177.internalMethod08576(), (long)typedValue178.internalMethod08576(), typedValue176.internalMethod08576(), f, f2, f3, f4));
    }

    private double internalMethod00038(double d, double d2, double d3) {
        if (JumpCircleEspFeature.internalField0149.world == null) {
            return d2 + 0.01;
        }
        BlockPos blockPos = BlockPos.ofFloored((double)d, (double)d2, (double)d3);
        double d4 = d - (double)blockPos.getX();
        double d5 = d3 - (double)blockPos.getZ();
        double d6 = Double.NEGATIVE_INFINITY;
        for (int i = blockPos.getY() + 1; i >= blockPos.getY() - 2; --i) {
            BlockPos blockPos2 = new BlockPos(blockPos.getX(), i, blockPos.getZ());
            BlockState blockState = JumpCircleEspFeature.internalField0149.world.getBlockState(blockPos2);
            VoxelShape voxelShape = blockState.getOutlineShape((BlockView)JumpCircleEspFeature.internalField0149.world, blockPos2);
            for (Box box : voxelShape.getBoundingBoxes()) {
                boolean bl;
                if (d4 < box.minX || d4 > box.maxX || d5 < box.minZ || d5 > box.maxZ) continue;
                double d7 = (double)blockPos2.getY() + box.maxY;
                boolean bl2 = Math.abs(d7 - d2) <= 0.01;
                boolean bl3 = bl = blockState.isOf(Blocks.SNOW) && d7 >= d2 - 0.01 && d7 <= d2 + 0.13;
                if (!bl2 && !bl || !(d7 > d6)) continue;
                d6 = d7;
            }
        }
        return (d6 == Double.NEGATIVE_INFINITY ? d2 : d6) + 0.01;
    }

    private void internalMethod06433() {
        long l = System.currentTimeMillis();
        this.internalField0204.removeIf(nestedValue0071 -> l - nestedValue0071.internalField1059 > nestedValue0071.internalField0229 + nestedValue0071.internalField0230);
    }

    private List<JumpShockwaveRenderer.InternalType0218> internalMethod02814(Camera camera) {
        ArrayList<JumpShockwaveRenderer.InternalType0218> arrayList = new ArrayList<JumpShockwaveRenderer.InternalType0218>();
        long l = System.currentTimeMillis();
        Vec3d vec3d = camera.getCameraPos();
        this.internalField0204.removeIf(nestedValue0071 -> l - nestedValue0071.internalField1059 > nestedValue0071.internalField0229 + nestedValue0071.internalField0230);
        for (InternalType0169 nestedValue0072 : this.internalField0204) {
            float f;
            float f2;
            float f3;
            float f4;
            if (arrayList.size() >= 12) break;
            long l2 = l - nestedValue0072.internalField1059;
            long l3 = nestedValue0072.internalField0229 + nestedValue0072.internalField0230;
            if (l3 <= 0L) continue;
            if (l2 < nestedValue0072.internalField0229) {
                f4 = (float)l2 / (float)nestedValue0072.internalField0229;
                f3 = 1.0f - (1.0f - f4) * (1.0f - f4) * (1.0f - f4);
                f2 = nestedValue0072.internalField0205 * f3;
                f = f3;
            } else {
                f4 = MathHelper.clamp((float)((float)(l2 - nestedValue0072.internalField0229) / (float)nestedValue0072.internalField0230), (float)0.0f, (float)1.0f);
                f2 = nestedValue0072.internalField0205 * (1.0f + f4 * 0.18f);
                f3 = 1.0f - f4;
                f = f3 * f3;
            }
            if (f2 <= 0.001f || f <= 0.001f) continue;
            f4 = MathHelper.clamp((float)((float)l2 / (float)l3), (float)0.0f, (float)1.0f);
            f3 = nestedValue0072.internalField0206 * (1.0f - 0.5f * f4);
            float f5 = nestedValue0072.internalField1456 * f;
            float f6 = nestedValue0072.internalField1048 * f;
            arrayList.add(new JumpShockwaveRenderer.InternalType0218((float)(nestedValue0072.internalField0194 - vec3d.x), (float)(nestedValue0072.internalField0193 - vec3d.y), (float)(nestedValue0072.internalField1045 - vec3d.z), f2, f3, nestedValue0072.internalField1047, nestedValue0072.internalField1049, nestedValue0072.internalField1046, f5, f6));
        }
        return arrayList;
    }

    private static PlayerTargetType internalMethod05051(PlayerEntity playerEntity) {
        if (playerEntity == JumpCircleEspFeature.internalField0149.player) {
            return PlayerTargetType.internalField0026;
        }
        if (RockstarClient.getInstance().internalMethod03375().internalMethod00380(playerEntity.getName().getString())) {
            return PlayerTargetType.internalField0961;
        }
        if (JumpCircleEspFeature.internalMethod01451(playerEntity)) {
            return PlayerTargetType.internalField0962;
        }
        return PlayerTargetType.internalField0025;
    }

    private static boolean internalMethod01451(PlayerEntity playerEntity) {
        String string = playerEntity.getName().getString();
        for (Packets.InternalType0018 nestedValue0006 : Information.getVisiblePlayers()) {
            if (nestedValue0006.gameInfo() == null || !string.equals(nestedValue0006.gameInfo().nickname())) continue;
            return true;
        }
        return false;
    }

    @Generated
    public BooleanSetting internalMethod02115() {
        return this.internalField0650;
    }

    @Generated
    public BooleanSetting internalMethod02741() {
        return this.internalField0651;
    }

    @Generated
    public ColorSetting internalMethod02169() {
        return this.internalField0665;
    }

    @Generated
    public SliderSetting internalMethod04338() {
        return this.internalField0383;
    }

    @Generated
    public SliderSetting internalMethod00539() {
        return this.internalField0382;
    }

    @Generated
    public SliderSetting internalMethod07855() {
        return this.internalField1142;
    }

    @Generated
    public SliderSetting internalMethod07974() {
        return this.internalField1140;
    }

    @Generated
    public SliderSetting internalMethod08800() {
        return this.internalField1141;
    }

    @Generated
    public CopyOnWriteArrayList<InternalType0169> internalMethod07647() {
        return this.internalField0204;
    }

    @Generated
    public Map<UUID, InternalType0170> internalMethod09306() {
        return this.internalField0543;
    }

    @Generated
    public JumpShockwaveRenderer internalMethod00590() {
        return this.internalField0338;
    }

    @Generated
    public EventListener<GameTickEvent> internalMethod04558() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<WorldChangeEvent> internalMethod05855() {
        return this.internalField0158;
    }

    @Generated
    public EventListener<Render3DEvent> internalMethod08215() {
        return this.internalField1028;
    }

    static final class InternalType0169 {
        final double internalField0194;
        final double internalField0193;
        final double internalField1045;
        final float internalField0205;
        final float internalField0206;
        final long internalField0229;
        final long internalField0230;
        final float internalField1048;
        final float internalField1047;
        final float internalField1049;
        final float internalField1046;
        final float internalField1456;
        final long internalField1059;

        InternalType0169(double d, double d2, double d3, float f, float f2, long l, long l2, float f3, float f4, float f5, float f6, float f7) {
            this.internalField0194 = d;
            this.internalField0193 = d2;
            this.internalField1045 = d3;
            this.internalField0205 = f;
            this.internalField0206 = f2;
            this.internalField0229 = l;
            this.internalField0230 = l2;
            this.internalField1048 = f3;
            this.internalField1047 = f4;
            this.internalField1049 = f5;
            this.internalField1046 = f6;
            this.internalField1456 = f7;
            this.internalField1059 = System.currentTimeMillis();
        }
    }

    static final class InternalType0170 {
        boolean internalField0277;
        double internalField0194;
        double internalField0193;
        double internalField1045;
        double internalField1043;

        InternalType0170(boolean bl, double d, double d2, double d3, double d4) {
            this.internalField0277 = bl;
            this.internalField0194 = d2;
            this.internalField0193 = d;
            this.internalField1045 = d4;
            this.internalField1043 = d3;
        }
    }
}
