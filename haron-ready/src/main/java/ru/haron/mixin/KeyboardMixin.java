package ru.haron.mixin;

import haron.client.MinecraftClientAccess;
import haron.events.KeyInputEvent;
import haron.events.EventDispatcher;
import haron.gui.core.ClickGuiKeybind;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.ClickGuiScreen;
import haron.gui.events.CommandBindsPanel;
import haron.markers.MarkerIcon;
import haron.markers.QuickMarkerPreferences;
import haron.markers.MarkerRegistry;
import haron.markers.Waypoint;
import haron.module.ModuleManager;
import haron.module.HaronModule;
import haron.theme.pryrvd;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.haron.Haron;

@Mixin(value={Keyboard.class})
public class KeyboardMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private static final double MAX_RAYCAST_DISTANCE = 500.0;

    @Inject(method={"onKey"}, at={@At(value="HEAD")})
    private void onKey(long j, int i, int i2, int i3, int i4, CallbackInfo callbackInfo) {
        if (j == this.client.getWindow().getHandle()) {
            EventDispatcher.EVENT_BUS.post((Object)new KeyInputEvent(i, i2, i3, i4));
            if (MinecraftClientAccess.c.player == null || MinecraftClientAccess.c.world == null || i3 != 1) {
                return;
            }
            if (MinecraftClientAccess.c.currentScreen != null) {
                if (ClickGuiKeybind.OPEN_KEY.matchesKey(i, i2)) {
                    Screen ScreenVar = MinecraftClientAccess.c.currentScreen;
                    if (ScreenVar instanceof ClickGuiScreen) {
                        ClickGuiScreen haronClickGuiScreen = (ClickGuiScreen)ScreenVar;
                        if (InteractionOverlayController.a().b()) {
                            haronClickGuiScreen.c();
                            return;
                        }
                        haronClickGuiScreen.close();
                        return;
                    }
                    return;
                }
                return;
            }
            if (ClickGuiKeybind.OPEN_KEY.matchesKey(i, i2)) {
                MinecraftClientAccess.c.setScreen(Haron.getInstance().getClickGui());
                return;
            }
            CommandBindsPanel.onKeyPress(i);
            int iB = QuickMarkerPreferences.b();
            if (QuickMarkerPreferences.a() && iB != 0 && iB == i) {
                this.createQuickMarker();
                return;
            }
            for (HaronModule clientModule : ModuleManager.all()) {
                if (clientModule.j() != i) continue;
                clientModule.d();
            }
        }
    }

    @Unique
    private void createQuickMarker() {
        if (MinecraftClientAccess.c.player == null || MinecraftClientAccess.c.world == null) {
            return;
        }
        Vec3d Vec3dVarGetCameraPosVec = MinecraftClientAccess.c.player.getCameraPosVec(1.0f);
        BlockHitResult BlockHitResultVarRaycast = MinecraftClientAccess.c.world.raycast(new RaycastContext(Vec3dVarGetCameraPosVec, Vec3dVarGetCameraPosVec.add(MinecraftClientAccess.c.player.getRotationVec(1.0f).multiply(500.0)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)MinecraftClientAccess.c.player));
        if (BlockHitResultVarRaycast.getType() == HitResult.Type.BLOCK) {
            BlockPos BlockPosVarGetBlockPos = BlockHitResultVarRaycast.getBlockPos();
            MarkerRegistry.a(new Waypoint("Быстрая метка", BlockPosVarGetBlockPos.getX(), BlockPosVarGetBlockPos.getY(), BlockPosVarGetBlockPos.getZ(), pryrvd.X, MarkerIcon.FAST));
        }
    }
}

