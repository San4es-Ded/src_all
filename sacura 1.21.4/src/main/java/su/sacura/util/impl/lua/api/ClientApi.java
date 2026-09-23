package su.sacura.util.impl.lua.api;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import su.sacura.util.impl.system.AudioUtil;

public class ClientApi implements LuaApi {
    public void register(Globals globals) {
        LuaTable luaTable = LuaValue.tableOf();

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("getPing", new ZeroArgFunction() {
            public LuaValue call() {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.player == null)
                    return LuaValue.valueOf(0);
                try {
                    return LuaValue.valueOf(mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency());
                } catch (Exception e) {
                    return LuaValue.valueOf(0);
                }
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("getName", new ZeroArgFunction() {
            public LuaValue call() {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.player == null)
                    return LuaValue.valueOf("User");
                return LuaValue.valueOf(mc.player.getName().getString());
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("getFps", new ZeroArgFunction() {
            public LuaValue call() {
                return LuaValue.valueOf(MinecraftClient.getInstance().getCurrentFps());
            }
        });

        // Исправлено: new TwoArgFunction(this) -> new TwoArgFunction()
        luaTable.set("playFile", new TwoArgFunction() {
            public LuaValue call(LuaValue fileName, LuaValue volume) {
                String name = fileName.checkjstring();
                float vol = (float)volume.optdouble(1.0D);
                File soundsDir = new File((MinecraftClient.getInstance()).runDirectory, "sacura/lua/sounds");
                if (!soundsDir.exists())
                    soundsDir.mkdirs();
                File soundFile = new File(soundsDir, name);
                AudioUtil.playSoundFromFile(soundFile, vol);
                return LuaValue.NIL;
            }
        });

        // Исправлено: new ThreeArgFunction(this) -> new ThreeArgFunction()
        luaTable.set("playSound", new ThreeArgFunction() {
            public LuaValue call(LuaValue soundName, LuaValue volume, LuaValue pitch) {
                String sound = soundName.checkjstring();
                float vol = (float)volume.optdouble(1.0D);
                float pit = (float)pitch.optdouble(1.0D);
                MinecraftClient.getInstance().execute(() -> {
                    Identifier id = Identifier.of(sound);
                    PositionedSoundInstance soundInstance = PositionedSoundInstance.master(SoundEvent.of(id), pit, vol);
                    MinecraftClient.getInstance().getSoundManager().play((SoundInstance)soundInstance);
                });
                return LuaValue.NIL;
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("getTime", new ZeroArgFunction() {
            public LuaValue call() {
                return LuaValue.valueOf((new SimpleDateFormat("HH:mm:ss")).format(new Date()));
            }
        });

        globals.set("client", luaTable);
    }
}