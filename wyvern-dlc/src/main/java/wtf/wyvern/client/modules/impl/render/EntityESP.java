package wtf.wyvern.client.modules.impl.render;

import java.util.Iterator;
import java.util.Optional;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4d;
import org.joml.Vector4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.impl.misc.NameProtect;
import wtf.wyvern.client.modules.impl.misc.ScoreboardHealth;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.render.EventRender2D;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.utility.game.other.ReplaceUtil;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.wyvern.utility.math.ProjectionUtil;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;


@ModuleAnnotation(
        name = "NameTags",
        category = Category.RENDER,
        description = "Показывает информацию о игроке"
)
public final class EntityESP extends Module {
    public static final EntityESP INSTANCE = new EntityESP();
    private static final float PLAYER_TAG_HEIGHT = 10.0F;
    private static final float PLAYER_HEAD_SIZE = 7.0F;
    private static final float EQUIPMENT_ICON_STEP = 9.0F;
    private static final float EQUIPMENT_GAP = 2.0F;
    private static final BorderRadius PLAYER_HEAD_RADIUS = BorderRadius.all(0.5F);
    private final MultiBooleanSetting display = new MultiBooleanSetting("Отображать",
            new MultiBooleanSetting.Value("Игроки", true),
            new MultiBooleanSetting.Value("Невидимки", true),
            new MultiBooleanSetting.Value("Предметы", true),
            new MultiBooleanSetting.Value("Броня", true),
            new MultiBooleanSetting.Value("Предметы в руках", true));

    @EventTarget
    private void onRender(EventRender2D e) {
        if (mc.world != null && mc.player != null) {
            float tickDelta = e.getTickDelta();
            this.renderPlayerTags(tickDelta, e);
            if (this.display.isEnable("Предметы")) {
                this.renderItemTags(tickDelta, e);
            }
        }
    }

    private void renderPlayerTags(float tickDelta, EventRender2D e) {
        for (PlayerEntity entity : mc.world.getPlayers()) {
            if (entity.isInvisible() ? !this.display.isEnable("Невидимки") : !this.display.isEnable("Игроки")) {
                continue;
            }
            if (entity == mc.player && !mc.getEntityRenderDispatcher().camera.isThirdPerson()) {
                continue;
            }
            if (!ProjectionUtil.canSee(entity.getBoundingBox().getCenter())) {
                continue;
            }

            double x = MathHelper.lerp((double)tickDelta, entity.lastRenderX, entity.getX());
            double y = MathHelper.lerp((double)tickDelta, entity.lastRenderY, entity.getY()) + (double)entity.getHeight() + 0.2D;
            double z = MathHelper.lerp((double)tickDelta, entity.lastRenderZ, entity.getZ());
            Vec3d screenPosition = ProjectionUtil.worldSpaceToScreenSpace(new Vec3d(x, y, z));
            if (screenPosition.z <= 0.0D || screenPosition.z >= 1.0D) {
                continue;
            }

            Vector4d position = ProjectionUtil.getVector4D(entity);
            if (position == null) {
                continue;
            }

            float posY = (float)(position.y - 11.0D);
            float hp = ScoreboardHealth.INSTANCE.isEnabled() && entity != mc.player
                    ? PlayerIntersectionUtil.getHealth(entity) : entity.getHealth();
            Text name = entity == mc.player && NameProtect.INSTANCE.isEnabled()
                    ? Text.literal(NameProtect.getCustomName())
                    : removeTrailingEmptyTag(ReplaceUtil.replaceSymbols(entity.getDisplayName()));
            Text nameWithHp = name.copy()
                    .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                    .append(Text.literal(String.valueOf((int)hp)).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                    .append(Text.literal("hp").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            float textWidth = Fonts.MEDIUM.getWidth(nameWithHp.getString(), 6.5F);
            int equipmentCount = this.getEquipmentCount(entity);
            float equipmentWidth = equipmentCount == 0
                    ? 0.0F : EQUIPMENT_GAP + equipmentCount * EQUIPMENT_ICON_STEP;
            float totalTagWidth = textWidth + PLAYER_HEAD_SIZE + 6.0F + equipmentWidth;
            float tagX = (float)(position.x + (position.z - position.x) / 2.0D - (double)(totalTagWidth / 2.0F));
            float tagY = posY - 2.5F;
            DrawUtil.drawRoundedRect(e.getContext().getMatrices(), tagX, tagY, totalTagWidth,
                    PLAYER_TAG_HEIGHT, BorderRadius.ZERO,
                    Wyvern.getInstance().getFriendManager().isFriend(entity.getNameForScoreboard())
                            ? new ColorRGBA(0, 166, 0, 123) : new ColorRGBA(0, 0, 0, 123));
            DrawUtil.drawPlayerHeadWithRoundedShader(e.getContext().getMatrices(),
                    entity instanceof AbstractClientPlayerEntity
                            ? ((AbstractClientPlayerEntity)entity).getSkinTextures().texture()
                            : DefaultSkinHelper.getSteve().texture(),
                    tagX + 2.0F, tagY + (PLAYER_TAG_HEIGHT - PLAYER_HEAD_SIZE) / 2.0F,
                    PLAYER_HEAD_SIZE, PLAYER_HEAD_RADIUS, ColorRGBA.WHITE);
            e.getContext().drawText(Fonts.MEDIUM.getFont(6.5F), nameWithHp,
                    tagX + PLAYER_HEAD_SIZE + 4.0F, posY, 255.0F);
            if (equipmentCount > 0) {
                this.renderEquipment(e, entity,
                        tagX + PLAYER_HEAD_SIZE + 4.0F + textWidth + EQUIPMENT_GAP,
                        tagY + 1.0F);
            }
        }
    }

    private int getEquipmentCount(PlayerEntity player) {
        boolean renderHands = this.display.isEnable("Предметы в руках");
        boolean renderArmor = this.display.isEnable("Броня");
        int itemCount = 0;
        if (renderHands) {
            if (!player.getMainHandStack().isEmpty()) itemCount++;
            if (!player.getOffHandStack().isEmpty()) itemCount++;
        }
        if (renderArmor) {
            for (ItemStack stack : player.getInventory().armor) {
                if (!stack.isEmpty()) itemCount++;
            }
        }
        return itemCount;
    }

    private void renderEquipment(EventRender2D event, PlayerEntity player, float x, float y) {
        boolean renderHands = this.display.isEnable("Предметы в руках");
        boolean renderArmor = this.display.isEnable("Броня");
        if (renderHands) {
            x = this.renderEquipmentItem(event, player.getMainHandStack(), x, y);
            x = this.renderEquipmentItem(event, player.getOffHandStack(), x, y);
        }
        if (renderArmor) {
            for (int slot = player.getInventory().armor.size() - 1; slot >= 0; slot--) {
                x = this.renderEquipmentItem(event, player.getInventory().armor.get(slot), x, y);
            }
        }
    }

    private float renderEquipmentItem(EventRender2D event, ItemStack stack, float x, float y) {
        if (stack.isEmpty()) {
            return x;
        }
        event.getContext().getMatrices().push();
        event.getContext().getMatrices().translate(x, y, 1.0F);
        event.getContext().getMatrices().scale(0.5F, 0.5F, 0.5F);
        event.getContext().drawItem(stack, 0, 0);
        event.getContext().getMatrices().pop();
        return x + EQUIPMENT_ICON_STEP;
    }

    private static Text removeTrailingEmptyTag(Text text) {
        String value = text.getString();
        int end = value.length();
        while (end > 0 && Character.isWhitespace(value.charAt(end - 1))) {
            end--;
        }

        if (end < 2 || value.charAt(end - 1) != ']') {
            return text;
        }

        int openingBracket = value.lastIndexOf('[', end - 2);
        if (openingBracket < 0 || !value.substring(openingBracket + 1, end - 1).isBlank()) {
            return text;
        }

        int trimmedEnd = openingBracket;
        while (trimmedEnd > 0 && Character.isWhitespace(value.charAt(trimmedEnd - 1))) {
            trimmedEnd--;
        }
        return sliceStyled(text, 0, trimmedEnd);
    }

    private static Text sliceStyled(Text source, int start, int end) {
        MutableText result = Text.empty();
        int[] cursor = {0};
        source.visit((style, value) -> {
            int segmentStart = cursor[0];
            int segmentEnd = segmentStart + value.length();
            int from = Math.max(start, segmentStart) - segmentStart;
            int to = Math.min(end, segmentEnd) - segmentStart;
            if (from < to) {
                result.append(Text.literal(value.substring(from, to)).setStyle(style));
            }
            cursor[0] = segmentEnd;
            return Optional.empty();
        }, Style.EMPTY);
        return result;
    }

    private void renderItemTags(float tickDelta, EventRender2D e) {
        Iterator var3 = mc.world.getEntities().iterator();

        while(var3.hasNext()) {
            Entity entity = (Entity)var3.next();
            if (entity instanceof ItemEntity) {
                ItemEntity itemEntity = (ItemEntity)entity;
                if (ProjectionUtil.canSee(itemEntity.getBoundingBox().getCenter())) {
                    double x = MathHelper.lerp((double)tickDelta, entity.lastRenderX, entity.getX());
                    double y = MathHelper.lerp((double)tickDelta, entity.lastRenderY, entity.getY()) + (double)entity.getHeight() + 0.1D;
                    double z = MathHelper.lerp((double)tickDelta, entity.lastRenderZ, entity.getZ());
                    Vec3d pos = ProjectionUtil.worldSpaceToScreenSpace(new Vec3d(x, y, z));
                    if (!(pos.z <= 0.0D) && !(pos.z >= 1.0D)) {
                        Vector4d position = ProjectionUtil.getVector4D(entity);
                        if (position == null) {
                            continue;
                        }
                        float posY = (float)(position.y - 11.0D);
                        ItemStack stack = itemEntity.getStack();
                        if (!stack.isEmpty()) {
                            int rarityOrdinal = stack.getRarity().ordinal();
                            Formatting var10000;
                            switch(rarityOrdinal) {
                                case 1:
                                    var10000 = Formatting.YELLOW;
                                    break;
                                case 2:
                                    var10000 = Formatting.AQUA;
                                    break;
                                case 3:
                                    var10000 = Formatting.LIGHT_PURPLE;
                                    break;
                                default:
                                    var10000 = Formatting.WHITE;
                            }

                            Formatting rarityColor = var10000;
                            String itemName = stack.getName().getString();
                            Text nameText = Text.literal(itemName).setStyle(Style.EMPTY.withColor(rarityColor));
                            if (!stack.getName().getSiblings().isEmpty()) {
                                nameText = stack.getName();
                            }

                            Text countComponent = stack.getCount() > 1 ? Text.literal(" х" + stack.getCount()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)) : Text.empty();
                            Text textComponent = ((Text)nameText).copy().append(countComponent);
                            float textWidth = Fonts.MEDIUM.getFont(6.5F).width((Text)textComponent);
                            DrawUtil.drawRoundedRect(e.getContext().getMatrices(), (float)(position.x + (position.z - position.x) / 2.0D - (double)(textWidth / 2.0F) - 3.0D), (float)(position.y - 13.5D), textWidth + 4.0F, 10.0F, BorderRadius.ZERO, new ColorRGBA(0, 0, 0, 123));
                            e.getContext().drawText(Fonts.MEDIUM.getFont(6.5F), textComponent, (float)(position.x + (position.z - position.x) / 2.0D - (double)(textWidth / 2.0F)), (float)position.y - 11.0F, 255.0F);
                        }
                    }
                }
            }
        }

    }

    public static void drawBox(double x, double y, double width, double height, double size, int color, BufferBuilder bufferbuilder) {
        drawRectBuilding(x + size, y, width - size, y + size, color, bufferbuilder);
        drawRectBuilding(x, y, x + size, height, color, bufferbuilder);
        drawRectBuilding(width - size, y, width, height, color, bufferbuilder);
        drawRectBuilding(x + size, height - size, width - size, height, color, bufferbuilder);
    }

    public static void drawBoxTest(double x, double y, double width, double height, double size, Vector4f colors, BufferBuilder bufferbuilder) {
        drawMCHorizontalBuilding(x + size, y, width - size, y + size, (int)colors.x(), (int)colors.y(), bufferbuilder);
        drawMCVerticalBuilding(width - size, y + size, width, height - size, (int)colors.y(), (int)colors.z(), bufferbuilder);
        drawMCHorizontalBuilding(x + size, height - size, width - size, height, (int)colors.w(), (int)colors.z(), bufferbuilder);
        drawMCVerticalBuilding(x, y + size, x + size, height - size, (int)colors.x(), (int)colors.w(), bufferbuilder);
    }

    public static void drawRectBuilding(double left, double top, double right, double bottom, int color, BufferBuilder bufferbuilder) {
        double j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        bufferbuilder.vertex((float)left, (float)bottom, 0.0F).color(f, f1, f2, f3);
        bufferbuilder.vertex((float)right, (float)bottom, 0.0F).color(f, f1, f2, f3);
        bufferbuilder.vertex((float)right, (float)top, 0.0F).color(f, f1, f2, f3);
        bufferbuilder.vertex((float)left, (float)top, 0.0F).color(f, f1, f2, f3);
    }

    public static void drawMCHorizontalBuilding(double x1, double y1, double x2, double y2, int start, int end, BufferBuilder bufferbuilder) {
        float a1 = (float)(start >> 24 & 255) / 255.0F;
        float r1 = (float)(start >> 16 & 255) / 255.0F;
        float g1 = (float)(start >> 8 & 255) / 255.0F;
        float b1 = (float)(start & 255) / 255.0F;
        float a2 = (float)(end >> 24 & 255) / 255.0F;
        float r2 = (float)(end >> 16 & 255) / 255.0F;
        float g2 = (float)(end >> 8 & 255) / 255.0F;
        float b2 = (float)(end & 255) / 255.0F;
        bufferbuilder.vertex((float)x1, (float)y2, 0.0F).color(r1, g1, b1, a1);
        bufferbuilder.vertex((float)x2, (float)y2, 0.0F).color(r2, g2, b2, a2);
        bufferbuilder.vertex((float)x2, (float)y1, 0.0F).color(r2, g2, b2, a2);
        bufferbuilder.vertex((float)x1, (float)y1, 0.0F).color(r1, g1, b1, a1);
    }

    public static void drawMCVerticalBuilding(double x1, double y1, double x2, double y2, int start, int end, BufferBuilder bufferbuilder) {
        float a1 = (float)(start >> 24 & 255) / 255.0F;
        float r1 = (float)(start >> 16 & 255) / 255.0F;
        float g1 = (float)(start >> 8 & 255) / 255.0F;
        float b1 = (float)(start & 255) / 255.0F;
        float a2 = (float)(end >> 24 & 255) / 255.0F;
        float r2 = (float)(end >> 16 & 255) / 255.0F;
        float g2 = (float)(end >> 8 & 255) / 255.0F;
        float b2 = (float)(end & 255) / 255.0F;
        bufferbuilder.vertex((float)x1, (float)y2, 0.0F).color(r2, g2, b2, a2);
        bufferbuilder.vertex((float)x2, (float)y2, 0.0F).color(r2, g2, b2, a2);
        bufferbuilder.vertex((float)x2, (float)y1, 0.0F).color(r1, g1, b1, a1);
        bufferbuilder.vertex((float)x1, (float)y1, 0.0F).color(r1, g1, b1, a1);
    }
}
