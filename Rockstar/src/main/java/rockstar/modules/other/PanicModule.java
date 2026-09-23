package rockstar.modules.other;




import rockstar.client.module.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;
import moscow.rockstar.mixin.minecraft.client.IMinecraftClient;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.minecraft.client.util.Icons;
import net.minecraft.resource.ResourcePack;
import rockstar.client.module.ModuleEntry;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.config.LegacyGameDirLocator;
import rockstar.client.internal.ui.TitleBarThemeHelper;
import rockstar.client.module.Module;

@ModuleInfo(name="Panic", category=ModuleCategory.OTHER, internalMethod09633="modules.descriptions.panic")
public class PanicModule
extends Module {
    @Override
    public final void onEnable() {
        TitleBarThemeHelper.internalMethod01739();
        RockstarClient.getInstance().internalMethod00631(true);
        RockstarClient.getInstance().internalMethod03371().internalMethod08923();
        for (ModuleEntry object : RockstarClient.getInstance().getModuleManager().getModules()) {
            object.setKeybind(-1);
            object.disable();
        }
        try {
            internalField0149.getWindow().setIcon((ResourcePack)internalField0149.getDefaultResourcePack(), Icons.RELEASE);
        }
        catch (Exception exception) {
            // empty catch block
        }
        ModContainerImpl modContainerImpl = this.internalMethod02099();
        if (modContainerImpl != null) {
            for (Path path : modContainerImpl.getOrigin().getPaths()) {
                path.toFile().delete();
            }
            FabricLoaderImpl.INSTANCE.getModsInternal().remove(modContainerImpl);
        }
        this.internalMethod09819();
        super.onEnable();
    }

    private void internalMethod09819() {
        Optional<Path> optional = LegacyGameDirLocator.internalMethod02256();
        if (optional.isEmpty()) {
            RockstarClient.internalField0572.warn("Legacy Launcher game directory was not found; Minecraft directory was not changed for Panic");
            return;
        }
        Path path = optional.get().toAbsolutePath().normalize();
        Path path2 = path.resolve("resourcepacks");
        try {
            Files.createDirectories(path2, new FileAttribute[0]);
            IMinecraftClient iMinecraftClient = (IMinecraftClient)internalField0149;
            iMinecraftClient.setRunDirectory(path.toFile());
            iMinecraftClient.setResourcePackDir(path2);
            RockstarClient.internalField0572.info("Panic changed Minecraft directory to Legacy Launcher path: {}", (Object)path);
        }
        catch (IOException iOException) {
            RockstarClient.internalField0572.warn("Failed to prepare Legacy Launcher resourcepacks directory: {}", (Object)path2, (Object)iOException);
        }
        catch (RuntimeException runtimeException) {
            RockstarClient.internalField0572.warn("Failed to change Minecraft directory for Panic: {}", (Object)path, (Object)runtimeException);
        }
    }

    private ModContainerImpl internalMethod02099() {
        return (ModContainerImpl)FabricLoaderImpl.INSTANCE.getAllMods().stream().filter(modContainer -> modContainer.getMetadata().getId().equals(RockstarClient.internalField1077)).findFirst().orElse(null);
    }
}
