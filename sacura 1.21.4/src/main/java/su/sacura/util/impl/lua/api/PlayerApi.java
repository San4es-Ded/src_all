package su.sacura.util.impl.lua.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class PlayerApi implements LuaApi {
    public void register(Globals globals) {
        LuaTable luaTable = LuaValue.tableOf();

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("getX", new ZeroArgFunction() {
            public LuaValue call() {
                if (MinecraftClient.getInstance().player == null)
                    return LuaValue.valueOf(0);
                return LuaValue.valueOf(MinecraftClient.getInstance().player.getX());
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("getY", new ZeroArgFunction() {
            public LuaValue call() {
                if (MinecraftClient.getInstance().player == null)
                    return LuaValue.valueOf(0);
                return LuaValue.valueOf(MinecraftClient.getInstance().player.getY());
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("getZ", new ZeroArgFunction() {
            public LuaValue call() {
                if (MinecraftClient.getInstance().player == null)
                    return LuaValue.valueOf(0);
                return LuaValue.valueOf(MinecraftClient.getInstance().player.getZ());
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("yaw", new ZeroArgFunction() {
            public LuaValue call() {
                if (MinecraftClient.getInstance().player == null)
                    return LuaValue.valueOf(0);
                return LuaValue.valueOf(MinecraftClient.getInstance().player.getYaw());
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("pitch", new ZeroArgFunction() {
            public LuaValue call() {
                if (MinecraftClient.getInstance().player == null)
                    return LuaValue.valueOf(0);
                return LuaValue.valueOf(MinecraftClient.getInstance().player.getPitch());
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        // Исправлено: логика метода isAlive — теперь возвращает boolean, а не Text
        luaTable.set("isAlive", new ZeroArgFunction() {
            public LuaValue call() {
                if (MinecraftClient.getInstance().player == null)
                    return LuaValue.FALSE;
                return LuaValue.valueOf(MinecraftClient.getInstance().player.isAlive());
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        // Исправлено: Identifier -> String через .toString()
        luaTable.set("getWeaponId", new ZeroArgFunction() {
            public LuaValue call() {
                if (MinecraftClient.getInstance().player == null)
                    return LuaValue.valueOf(0);
                return LuaValue.valueOf(Registries.ITEM.getId(MinecraftClient.getInstance().player.getMainHandStack().getItem()).toString());
            }
        });

        globals.set("player", luaTable);
    }
}