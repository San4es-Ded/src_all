package su.sacura.util.impl.lua.api;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import su.sacura.Sacura;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleManager;

public class ModulesApi implements LuaApi {
    public void register(Globals globals) {
        LuaTable luaTable = LuaValue.tableOf();

        // Исправлено: new OneArgFunction(this) -> new OneArgFunction()
        luaTable.set("isEnabled", new OneArgFunction() {
            public LuaValue call(LuaValue name) {
                String moduleName = name.checkjstring();
                ModuleManager manager = Sacura.getInstance().getModuleManager();
                if (manager == null)
                    return LuaValue.FALSE;
                for (Module m : manager.module) {
                    if (m.name.equalsIgnoreCase(moduleName))
                        return LuaValue.valueOf(m.enable);
                }
                return LuaValue.FALSE;
            }
        });

        // Исправлено: new ZeroArgFunction(this) -> new ZeroArgFunction()
        luaTable.set("getList", new ZeroArgFunction() {
            public LuaValue call() {
                LuaTable resultTable = LuaValue.tableOf();
                ModuleManager manager = Sacura.getInstance().getModuleManager();
                if (manager == null)
                    return resultTable;
                int i = 1;
                for (Module m : manager.module) {
                    if (m.enable && m.bind.getKey() != -1)
                        resultTable.set(i++, LuaValue.valueOf(m.name));
                }
                return resultTable;
            }
        });

        globals.set("modules", luaTable);
    }
}