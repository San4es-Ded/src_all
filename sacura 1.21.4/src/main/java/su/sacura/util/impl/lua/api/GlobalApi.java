package su.sacura.util.impl.lua.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class GlobalApi implements LuaApi {
    public void register(Globals globals) {
        // Исправлено: new OneArgFunction(this) -> new OneArgFunction()
        globals.set("chat", new OneArgFunction() {
            public LuaValue call(LuaValue msg) {
                if (MinecraftClient.getInstance().player != null)
                    MinecraftClient.getInstance().player.sendMessage(Text.of(msg.tojstring()), false);
                return LuaValue.NIL;
            }
        });

        // Исправлено: new OneArgFunction(this) -> new OneArgFunction()
        globals.set("print", new OneArgFunction() {
            public LuaValue call(LuaValue msg) {
                System.out.println("[Lua] " + msg.tojstring());
                return LuaValue.NIL;
            }
        });
    }
}