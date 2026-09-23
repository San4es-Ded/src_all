package rockstar.client.util;



import rockstar.client.server.*;
import rockstar.client.*;
import java.util.Locale;
import lombok.Generated;
import moscow.rockstar.mixin.accessors.EntityMovementMultiplierAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;

public final class GameUtils
implements MinecraftClientAccess {
    private static float internalField0205 = 1.0f;

    public static void internalMethod00468() {
        internalField0205 = 1.0f;
    }

    public static Block internalMethod01375() {
        return GameUtils.internalMethod02397(0.0, 0.0, 0.0);
    }

    public static Block internalMethod02397(double d, double d2, double d3) {
        return !GameUtils.internalMethod00471() ? Blocks.AIR : GameUtils.internalField0149.world.getBlockState(BlockPos.ofFloored((Position)GameUtils.internalField0149.player.getEntityPos().add(d, d2, d3))).getBlock();
    }

    public static boolean internalMethod03365(double d) {
        return GameUtils.internalMethod02397(0.3, d, 0.3) != Blocks.AIR || GameUtils.internalMethod02397(-0.3, d, 0.3) != Blocks.AIR || GameUtils.internalMethod02397(0.3, d, -0.3) != Blocks.AIR || GameUtils.internalMethod02397(-0.3, d, -0.3) != Blocks.AIR;
    }

    public static boolean internalMethod06131(LivingEntity livingEntity) {
        return GameUtils.internalMethod00611(livingEntity, 0.0f);
    }

    public static boolean internalMethod00611(LivingEntity livingEntity, float f) {
        Box box = GameUtils.internalField0149.player.getBoundingBox().expand((double)f, 0.0, (double)f);
        return livingEntity.getBoundingBox().intersects(box);
    }

    public static boolean internalMethod02992(LivingEntity livingEntity, Vec3d vec3d, float f) {
        return GameUtils.internalMethod00473(livingEntity.getBoundingBox().offset(-livingEntity.getX(), -livingEntity.getY(), -livingEntity.getZ()).offset(vec3d), f);
    }

    public static boolean internalMethod00473(Box box, float f) {
        Box box2 = GameUtils.internalField0149.player.getBoundingBox().expand((double)f, 0.0, (double)f);
        return box.intersects(box2);
    }

    public static StatusEffectInstance internalMethod04749(RegistryEntry<StatusEffect> registryEntry, int n, int n2) {
        return new StatusEffectInstance(registryEntry, n, n2, false, false, false);
    }

    public static boolean internalMethod06278(StatusEffectInstance statusEffectInstance) {
        return statusEffectInstance != null && !statusEffectInstance.shouldShowParticles() && !statusEffectInstance.shouldShowIcon();
    }

    public static void internalMethod05868(double d, boolean bl) {
        double d2 = rockstar.client.compat.InputCompat.forward(GameUtils.internalField0149.player.input);
        double d3 = rockstar.client.compat.InputCompat.sideways(GameUtils.internalField0149.player.input);
        float f = GameUtils.internalField0149.player.getYaw();
        if (!(d2 != 0.0 || d3 != 0.0 || bl && (GameUtils.internalField0149.options.jumpKey.isPressed() || GameUtils.internalField0149.options.sneakKey.isPressed()))) {
            GameUtils.internalField0149.player.setVelocity(0.0, GameUtils.internalField0149.player.getVelocity().y, 0.0);
            return;
        }
        if (d2 != 0.0) {
            if (d3 > 0.0) {
                f += (float)(d2 > 0.0 ? -45 : 45);
            } else if (d3 < 0.0) {
                f += (float)(d2 > 0.0 ? 45 : -45);
            }
            d3 = 0.0;
            d2 = d2 > 0.0 ? 1.0 : -1.0;
        }
        double d4 = Math.sin(Math.toRadians((double)f + 90.0));
        double d5 = Math.cos(Math.toRadians((double)f + 90.0));
        double d6 = d2 * d * d5 + d3 * d * d4;
        double d7 = d2 * d * d4 - d3 * d * d5;
        double d8 = 0.0;
        if (GameUtils.internalField0149.options.jumpKey.isPressed()) {
            d8 += d;
        }
        if (GameUtils.internalField0149.options.sneakKey.isPressed()) {
            d8 -= d;
        }
        GameUtils.internalField0149.player.setVelocity(d6, bl ? d8 / 2.0 : GameUtils.internalField0149.player.getVelocity().y, d7);
    }

    public static boolean internalMethod07128(LivingEntity livingEntity) {
        double d;
        if (GameUtils.internalField0149.player == null) {
            return false;
        }
        double d2 = Math.sqrt(livingEntity.getVelocity().x * livingEntity.getVelocity().x + livingEntity.getVelocity().z * livingEntity.getVelocity().z);
        if (d2 < 0.1) {
            return false;
        }
        double d3 = livingEntity.getX() - GameUtils.internalField0149.player.getX();
        double d4 = Math.sqrt(d3 * d3 + (d = livingEntity.getZ() - GameUtils.internalField0149.player.getZ()) * d);
        if (d4 < 0.1) {
            return false;
        }
        double d5 = d3 / d4;
        double d6 = livingEntity.getVelocity().x;
        double d7 = d / d4;
        double d8 = livingEntity.getVelocity().z;
        double d9 = d5 * d6 + d7 * d8;
        return d9 > 0.15;
    }

    public static boolean internalMethod00469() {
        if (GameUtils.internalField0149.player == null || GameUtils.internalField0149.world == null || GameUtils.internalField0149.player.input == null) {
            return false;
        }
        return (double)GameUtils.internalField0149.player.forwardSpeed != 0.0 || (double)rockstar.client.compat.InputCompat.sideways(GameUtils.internalField0149.player.input) != 0.0;
    }

    public static Block internalMethod05434(Entity entity) {
        if (entity == null) {
            return null;
        }
        BlockPos blockPos = entity.getBlockPos().down();
        return GameUtils.internalMethod01898(blockPos, entity.getEntityWorld());
    }

    public static Block internalMethod06101(Entity entity) {
        if (entity == null) {
            return null;
        }
        BlockPos blockPos = entity.getBlockPos().add(0, Math.round(entity.getHeight()), 0).up();
        return GameUtils.internalMethod01898(blockPos, entity.getEntityWorld());
    }

    public static Block internalMethod02020() {
        if (GameUtils.internalField0149.player == null || GameUtils.internalField0149.world == null) {
            return null;
        }
        BlockPos blockPos = GameUtils.internalField0149.player.getBlockPos().down().up();
        return GameUtils.internalMethod01898(blockPos, (World)GameUtils.internalField0149.world);
    }

    public static Block internalMethod08894() {
        if (GameUtils.internalField0149.player == null || GameUtils.internalField0149.world == null) {
            return null;
        }
        BlockPos blockPos = GameUtils.internalField0149.player.getBlockPos().up();
        return GameUtils.internalMethod01898(blockPos, (World)GameUtils.internalField0149.world);
    }

    public static Block internalMethod08016(Entity entity) {
        if (entity == null) {
            return null;
        }
        BlockPos blockPos = entity.getBlockPos();
        return GameUtils.internalMethod01898(blockPos, entity.getEntityWorld());
    }

    public static double internalMethod00466() {
        return Math.hypot(GameUtils.internalField0149.player.getVelocity().x, GameUtils.internalField0149.player.getVelocity().z);
    }

    public static Block internalMethod09040() {
        if (GameUtils.internalField0149.player == null || GameUtils.internalField0149.world == null) {
            return null;
        }
        BlockPos blockPos = GameUtils.internalField0149.player.getBlockPos();
        return GameUtils.internalMethod01898(blockPos, (World)GameUtils.internalField0149.world);
    }

    public static Block internalMethod01898(BlockPos blockPos, World world) {
        return world.getBlockState(blockPos).getBlock();
    }

    public static double internalMethod01047(float f, double d, double d2) {
        if (d < 0.0) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (d < 0.0) {
            f2 = -0.5f;
        } else if (d > 0.0) {
            f2 = 0.5f;
        }
        if (d2 > 0.0) {
            f -= 90.0f * f2;
        }
        if (d2 < 0.0) {
            f += 90.0f * f2;
        }
        return Math.toRadians(f);
    }

    public static boolean internalMethod00471() {
        return GameUtils.internalField0149.player != null && GameUtils.internalField0149.world != null;
    }

    public static float internalMethod02919(PlayerEntity playerEntity) {
        if (playerEntity == null) {
            return 0.0f;
        }
        ScoreboardObjective scoreboardObjective = playerEntity.getEntityWorld().getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
        if (scoreboardObjective != null) {
            ReadableScoreboardScore readableScoreboardScore = playerEntity.getEntityWorld().getScoreboard().getScore((ScoreHolder)playerEntity, scoreboardObjective);
            String string = ReadableScoreboardScore.getFormattedScore((ReadableScoreboardScore)readableScoreboardScore, (NumberFormat)scoreboardObjective.getNumberFormatOr((NumberFormat)StyledNumberFormat.EMPTY)).getString();
            Float f = GameUtils.internalMethod04627(string);
            if (GameUtils.internalMethod00913(scoreboardObjective, string)) {
                return f != null ? f.floatValue() : (readableScoreboardScore != null ? (float)readableScoreboardScore.getScore() : playerEntity.getHealth());
            }
        }
        return playerEntity.getHealth();
    }

    private static Float internalMethod04627(String string) {
        int n = -1;
        int n2 = -1;
        boolean bl = false;
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (Character.isDigit(c)) {
                if (n == -1) {
                    n = i;
                }
                n2 = i + 1;
                continue;
            }
            if (!(c != '.' && c != ',' || n == -1 || bl)) {
                bl = true;
                n2 = i + 1;
                continue;
            }
            if (n != -1) break;
        }
        if (n == -1) {
            return null;
        }
        try {
            return Float.valueOf(Float.parseFloat(string.substring(n, n2).replace(',', '.')));
        }
        catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    private static boolean internalMethod00913(ScoreboardObjective scoreboardObjective, String string) {
        if (ServerUtils.internalMethod01786(KnownServer.internalField1217)) {
            return true;
        }
        if (ServerUtils.internalMethod07326()) {
            return true;
        }
        if (scoreboardObjective.getRenderType() == ScoreboardCriterion.RenderType.HEARTS) {
            return true;
        }
        String string2 = (scoreboardObjective.getName() + " " + scoreboardObjective.getDisplayName().getString() + " " + string).toLowerCase(Locale.ROOT);
        if (string2.contains("hp") || string2.contains("\u0445\u043f") || string2.contains("\u0437\u0434\u043e\u0440\u043e\u0432") || string2.contains("\u2764") || string2.contains("\u2665")) {
            return true;
        }
        return string2.contains("funtime") || string2.contains("fun time") || string2.contains("\u0444\u0430\u043d\u0442\u0430\u0439\u043c");
    }

    public static boolean internalMethod08354() {
        if (GameUtils.internalField0149.player == null) {
            return false;
        }
        ItemStack itemStack = GameUtils.internalField0149.player.getMainHandStack();
        Item item = itemStack.getItem();
        if (itemStack.isEmpty()) {
            return false;
        }
        return LegacyItemTypes.isSword(item) || item instanceof AxeItem || item instanceof TridentItem || item instanceof MaceItem;
    }

    public static boolean internalMethod08355() {
        return GameUtils.internalMethod01497((Entity)GameUtils.internalField0149.player);
    }

    public static boolean internalMethod01497(Entity entity) {
        if (!(entity instanceof EntityMovementMultiplierAccessor)) {
            return false;
        }
        EntityMovementMultiplierAccessor entityMovementMultiplierAccessor = (EntityMovementMultiplierAccessor)(Object)entity;
        Vec3d vec3d = entityMovementMultiplierAccessor.getMovementMultiplier();
        if (vec3d == null) {
            return false;
        }
        return GameUtils.internalMethod02725(vec3d);
    }

    private static boolean internalMethod02725(Vec3d vec3d) {
        return GameUtils.internalMethod04590(vec3d, 0.25, 0.05, 0.25) || GameUtils.internalMethod04590(vec3d, 0.5, 0.25, 0.5);
    }

    private static boolean internalMethod04590(Vec3d vec3d, double d, double d2, double d3) {
        double d4 = 1.0E-6;
        return Math.abs(vec3d.x - d) < d4 && Math.abs(vec3d.y - d2) < d4 && Math.abs(vec3d.z - d3) < d4;
    }

    @Generated
    private GameUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static void internalMethod03366(float f) {
        internalField0205 = f;
    }

    @Generated
    public static float internalMethod00467() {
        return internalField0205;
    }
}
