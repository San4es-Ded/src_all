package su.sacura.util.impl.lua.api;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.features.modules.settings.impl.ColorSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.impl.lua.LuaScript;
import su.sacura.util.impl.lua.api.LuaApi;

public class SettingsApi
        implements LuaApi {
    private final LuaScript script;

    public SettingsApi(LuaScript script) {
        this.script = script;
    }

    @Override
    public void register(Globals globals) {
        LuaTable settings = LuaValue.tableOf();
        settings.set("addBoolean", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue name, LuaValue def) {
                final BooleanSetting setting = new BooleanSetting(name.checkjstring(), def.checkboolean());
                SettingsApi.this.script.addSettings(setting);
                LuaTable ret = LuaValue.tableOf();
                // Исправлено: new OneArgFunction(this) -> new OneArgFunction()
                ret.set("get", new OneArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg) {
                        return LuaValue.valueOf((Boolean)setting.get());
                    }
                });
                // Исправлено: new TwoArgFunction(this) -> new TwoArgFunction()
                ret.set("set", new TwoArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg, LuaValue val) {
                        setting.set(val.checkboolean());
                        return LuaValue.NIL;
                    }
                });
                return ret;
            }
        });
        settings.set("addSlider", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                String name = args.checkjstring(1);
                float min = (float)args.checkdouble(2);
                float max = (float)args.checkdouble(3);
                float def = (float)args.checkdouble(4);
                float inc = args.narg() > 4 ? (float)args.checkdouble(5) : 0.1f;
                final SliderSetting setting = new SliderSetting(name, def, min, max, inc);
                SettingsApi.this.script.addSettings(setting);
                LuaTable ret = LuaValue.tableOf();
                // Исправлено: new OneArgFunction(this) -> new OneArgFunction()
                ret.set("get", new OneArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg) {
                        return LuaValue.valueOf(((Float)setting.get()).floatValue());
                    }
                });
                return ret;
            }
        });
        settings.set("addColor", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue name, LuaValue def) {
                final ColorSetting setting = new ColorSetting(name.checkjstring(), def.checkint());
                SettingsApi.this.script.addSettings(setting);
                LuaTable ret = LuaValue.tableOf();
                // Исправлено: new OneArgFunction(this) -> new OneArgFunction()
                ret.set("get", new OneArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg) {
                        return LuaValue.valueOf((Integer)setting.get());
                    }
                });
                return ret;
            }
        });
        globals.set("settings", settings);
    }
}