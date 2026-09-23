package wtf.wyvern.client.modules.impl.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.game.player.rotation.RotationUtil;

@ModuleAnnotation(
        name = "ItemAim",
        category = Category.PLAYER,
        description = "Автоматически наводится на нужный предмет"
)
public final class ItemAim extends Module {
    public static final ItemAim INSTANCE = new ItemAim();

    private final MultiBooleanSetting items = new MultiBooleanSetting(
            "Лутать",
            new MultiBooleanSetting.Value("Шары", true),
            new MultiBooleanSetting.Value("Элитры", true)
    );

    private ItemAim() {
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) return;

        ItemEntity targetItem = findTargetItem();
        if (targetItem == null) return;

        Vec2f rotation = RotationUtil.calculate(targetItem.getBoundingBox().getCenter());
        RotationComponent.update(
                new Rotation(rotation.x, rotation.y),
                360.0F, 360.0F, 360.0F, 360.0F,
                0, 10, false
        );
    }

    private ItemEntity findTargetItem() {
        ItemEntity bestItem = null;
        double bestDistance = Double.MAX_VALUE;
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof ItemEntity itemEntity) || !isWantedItem(itemEntity)) continue;

            double distance = mc.player.squaredDistanceTo(itemEntity);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestItem = itemEntity;
            }
        }
        return bestItem;
    }

    private boolean isWantedItem(ItemEntity itemEntity) {
        return items.isEnable("Шары") && itemEntity.getStack().isOf(Items.PLAYER_HEAD)
                || items.isEnable("Элитры") && itemEntity.getStack().isOf(Items.ELYTRA);
    }
}
