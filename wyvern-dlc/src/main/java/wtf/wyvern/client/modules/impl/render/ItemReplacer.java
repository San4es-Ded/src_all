package wtf.wyvern.client.modules.impl.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.item.SwordItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "ItemReplacer", category = Category.RENDER, description = "Заменяет модели мечей")
public final class ItemReplacer extends Module {
    private static final Variant[] VARIANTS = createVariants();
    private static final Map<String, Variant> BY_LABEL = createMap();
    private static final RuntimeSwordModel COSMETIC_KATANA = new RuntimeSwordModel("katana");
    private static final ItemStack COSMETIC_KATANA_STACK = new ItemStack(Items.IRON_SWORD);
    public static final ItemReplacer INSTANCE = new ItemReplacer();

    public final ModeSetting swordModel = new ModeSetting("Модель меча", labels());
    private Variant cached = VARIANTS[0];

    private ItemReplacer() {
    }

    public static boolean renderReplacement(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices,
                                            VertexConsumerProvider consumers, int light, int overlay) {
        if (!INSTANCE.isEnabled() || stack == null || stack.isEmpty() || !(stack.getItem() instanceof SwordItem)) {
            return false;
        }
        return INSTANCE.selected().model.render(stack, mode, matrices, consumers, light, overlay);
    }

    public static boolean renderCosmeticKatana(MatrixStack matrices, VertexConsumerProvider consumers, int light) {
        return COSMETIC_KATANA.renderRaw(COSMETIC_KATANA_STACK, matrices, consumers, light, 0);
    }

    @FastNative
    private Variant selected() {
        cached = BY_LABEL.getOrDefault(swordModel.get().toLowerCase(Locale.ROOT), cached);
        return cached;
    }

    @FastNative
    private static Variant[] createVariants() {
        String[][] data = new String[][]{
                {"Abominable Blade", "abominableblade"}, {"Abominable Greatsaber", "abominablegreatsaber"},
                {"Abominable Scythe", "abominablescythe"}, {"Acid Demon", "aciddemon"},
                {"Amethyst Shuriken", "amethyst_shuriken"}, {"Ancient Royal Greatsword", "ancient_royal_great_sword"},
                {"Aquantic Sacred Blade", "aquantic_sacred_blade"}, {"Aquantic Trident", "aquantictrident"},
                {"Arcanethyst", "arcanethyst"}, {"Ashura Blade", "ashura_blade"},
                {"Awakened Lichblade", "awakened_lichblade"}, {"Blood Edge", "bloodedge"},
                {"Bloody Death", "bloodydeath"}, {"Bramblethorn", "bramblethorn"},
                {"Brimstone Claymore", "brimstone_claymore"}, {"Carian Sword", "cariansword"},
                {"Chrono Blade", "chrono_blade"}, {"Corrupted Mythic Blade", "corruptedmythicblade"},
                {"Creation Splitter", "creationsplitter"}, {"Crescent Rose", "crescentrose"},
                {"Cyber Katana", "cyberkatana"}, {"Cyber Mantis Blade", "cybermantisblade"},
                {"Cybernetic Katana", "cybernetickatana"}, {"Cybernetic Knife", "cyberneticknife"},
                {"Cybernetic Sawblade", "cyberneticsawblade"}, {"Cyber Sword", "cybersword"},
                {"Dainsleif", "dainsleif"}, {"Dark Blade", "dark_blade"}, {"Dark Cleaver", "dark_cleaver"},
                {"Death Knight Dagger", "death_knight_dagger"}, {"Death Knight Sword", "death_knight_sword"},
                {"Demigod's Unholy Blade", "demigodsunholyblade"}, {"Demigod's Unholy Halberd", "demigodsunholyhalberd"},
                {"Demonic Blade", "demonicblade"}, {"Demonic Cleaver", "demoniccleaver"},
                {"Demon Lord's Great Axe", "demonlordsgreataxe"}, {"Demon Lord's Sword", "demonlordsword"},
                {"Divine Justice", "divine_justice"}, {"Divine Reaper", "divine_reaper"},
                {"Divine Axe Rhitta", "divineaxerhitta"}, {"Divine Punisher", "divinepunisher"},
                {"Dragon Slaying Blade", "dragonslayingblade"}, {"Edge of the Astral Plane", "edgeoftheastralplane"},
                {"Emberblade", "emberblade"}, {"Enigma", "enigma"}, {"Epic Sword", "epicsword"},
                {"Estoc", "estoc"}, {"Excalibur", "excalibur"}, {"Fallen God Spear", "fallengodspear"},
                {"Fallen God Sword", "fallengodsword"}, {"Floral Longsword", "floral_longsword"},
                {"Floral Sabre", "floral_sabre"}, {"Forest Guardian Glaive", "forest_guardian_glaive"},
                {"Frost Axe", "frostaxe"}, {"Frostblade", "frostblade"}, {"Frost Scythe", "frostscythe"},
                {"Green Scythe", "greenscythe"}, {"Hearthflame", "hearthflame"}, {"Hero Sword", "herosword"},
                {"Holy Moonlight Sword", "holymoonlightsword"}, {"Hornet's Needle", "hornetsneedle"},
                {"Ice Whisper", "icewhisper"}, {"Jade Halberd", "jadehalberd"}, {"Katana", "katana"},
                {"Legendary Sword", "legendarysword"}, {"Longsword", "longsword"}, {"Magi Scythe", "magiscythe"},
                {"Masamune", "masamune"}, {"Mjolnir", "mjolnir"}, {"Molten Blade", "moltenblade"},
                {"Molten Sword", "moltensword"}, {"Muramasa", "muramasa"}, {"Mystical Spellblade", "mysticalspellblade"},
                {"Mythic Blade", "mythicblade"}, {"Partisan", "partisan"}, {"Pharaoh's Treasure", "pharaohs_treasure"},
                {"Phoenix Grace", "pheonixgrace"}, {"Powerfuse Hammer", "powerfusehammer"},
                {"Powerfuse Sword", "powerfusesword"}, {"Requiem of Hell", "requiem_of_hell"},
                {"Ribbon Cleaver", "ribboncleaver"}, {"Righteous Relic", "righteous_relic"},
                {"Rivers of Blood", "riversofblood"}, {"Royal Chakram", "royalchakram"},
                {"Royal Rapier", "royalrapier"}, {"Sabre", "sabre"}, {"Scissor Blade", "scissorblade"},
                {"Sculk Cleaver", "sculkcleaver"}, {"Sculk Scythe", "sculkscythe"}, {"Sculk Sword", "sculksword"},
                {"Sentinel's Will", "sentinels_will"}, {"Silverine Blade", "silverine_blade"},
                {"Soul Claws", "soulclaws"}, {"Soul Edge", "souledge"}, {"Soul Harvester", "soulharvester"},
                {"Soul Render", "soulrender"}, {"Soul Stealer", "soulstealer"}, {"Soul Collector", "soul_collector"},
                {"Soul Devourer", "soul_devourer"}, {"Star's Edge", "stars_edge"}, {"Steel Sword", "steelsword"},
                {"Stop Sign", "stop_sign"}, {"Stormbringer", "stormbringer"}, {"Storm's Edge", "storms_edge"},
                {"Sunbreak", "sunbreak"}, {"Tengen's Blade", "tengensblade"}, {"Terrablade", "terrablade"},
                {"Thousand Demon Daggers", "thousanddemondaggers"}, {"Thunderbrand", "thunderbrand"},
                {"Thunderbringer", "thunderbringer"}, {"Toxic Longsword", "toxic_longsword"},
                {"Vampiric Needle", "vampiricneedle"}, {"Wakizashi", "wakizashi"},
                {"Watcher Claymore", "watcher_claymore"}, {"Watching Warglaive", "watching_warglaive"},
                {"Waxweaver", "waxweaver"}, {"Whisperwind", "whisperwind"}, {"Wickpiercer", "wickpiercer"}, {"Yoru", "yoru"}
        };
        Variant[] variants = new Variant[data.length];
        for (int i = 0; i < data.length; i++) {
            variants[i] = new Variant(data[i][0], data[i][1]);
        }
        return variants;
    }

    @FastNative
    private static Map<String, Variant> createMap() {
        Map<String, Variant> map = new HashMap<>();
        for (Variant variant : VARIANTS) map.put(variant.label.toLowerCase(Locale.ROOT), variant);
        return map;
    }

    @FastNative
    private static String[] labels() {
        String[] labels = new String[VARIANTS.length];
        for (int i = 0; i < VARIANTS.length; i++) labels[i] = VARIANTS[i].label;
        return labels;
    }

    private record Variant(String label, String modelName, RuntimeSwordModel model) {
        private Variant(String label, String modelName) {
            this(label, modelName, new RuntimeSwordModel(modelName));
        }
    }

    private static final class RuntimeSwordModel {
        private static final float PIXEL = 1f / 16f;
        private static final int STRIDE = 8;
        private final String modelName;
        private final ArrayList<Cube> cubes = new ArrayList<>();
        private final DisplayTransform[] transforms = new DisplayTransform[8];
        private Identifier texture;
        private float[] mesh = new float[0];
        private int vertices;
        private boolean loaded;

        private RuntimeSwordModel(String modelName) {
            this.modelName = modelName;
        }

        private boolean render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices,
                               VertexConsumerProvider consumers, int light, int overlay) {
            load();
            if (vertices == 0 || texture == null) return false;
            RenderLayer layer = RenderLayer.getEntityTranslucent(texture);
            VertexConsumer vc = ItemRenderer.getItemGlintConsumer(consumers, layer, true, stack.hasGlint());
            matrices.push();
            applyTransform(mode, matrices);
            MatrixStack.Entry entry = matrices.peek();
            for (int i = 0; i < vertices * STRIDE; i += STRIDE) {
                vc.vertex(entry, mesh[i], mesh[i + 1], mesh[i + 2])
                        .color(255, 255, 255, 255)
                        .texture(mesh[i + 3], mesh[i + 4])
                        .overlay(overlay)
                        .light(light)
                        .normal(entry, mesh[i + 5], mesh[i + 6], mesh[i + 7]);
            }
            matrices.pop();
            return true;
        }

        private boolean renderRaw(ItemStack stack, MatrixStack matrices,
                                  VertexConsumerProvider consumers, int light, int overlay) {
            load();
            if (vertices == 0 || texture == null) return false;
            RenderLayer layer = RenderLayer.getEntityTranslucent(texture);
            VertexConsumer vc = ItemRenderer.getItemGlintConsumer(consumers, layer, true, false);
            MatrixStack.Entry entry = matrices.peek();
            for (int i = 0; i < vertices * STRIDE; i += STRIDE) {
                vc.vertex(entry, mesh[i], mesh[i + 1], mesh[i + 2])
                        .color(255, 255, 255, 255)
                        .texture(mesh[i + 3], mesh[i + 4])
                        .overlay(overlay)
                        .light(light)
                        .normal(entry, mesh[i + 5], mesh[i + 6], mesh[i + 7]);
            }
            return true;
        }

        private void load() {
            if (loaded) return;
            loaded = true;
            try {
                Identifier id = Identifier.of("wyvern", "item_replacer/sword/models/" + modelName + ".json");
                try (InputStreamReader reader = new InputStreamReader(
                        MinecraftClient.getInstance().getResourceManager().getResourceOrThrow(id).getInputStream(),
                        StandardCharsets.UTF_8)) {
                    read(JsonParser.parseReader(reader).getAsJsonObject());
                }
            } catch (Exception e) {
                System.err.println("[ItemReplacer] Failed to load " + modelName + ": " + e.getMessage());
            }
        }

        private void read(JsonObject root) {
            JsonObject textures = root.getAsJsonObject("textures");
            if (textures == null || textures.size() == 0) return;
            String primaryTextureKey = textures.has("0") ? "0" : textures.entrySet().iterator().next().getKey();
            String primaryTextureRef = "#" + primaryTextureKey;
            String texturePath = textures.get(primaryTextureKey).getAsString();
            String textureName = texturePath.substring(texturePath.lastIndexOf('/') + 1);
            texture = Identifier.of("wyvern", "item_replacer/sword/textures/" + textureName + ".png");

            JsonArray elements = root.getAsJsonArray("elements");
            if (elements != null) {
                for (JsonElement element : elements) readCube(element.getAsJsonObject(), primaryTextureRef);
            }
            bake();
            readTransforms(root.getAsJsonObject("display"));
        }

        private void readCube(JsonObject obj, String primaryTextureRef) {
            JsonArray from = obj.getAsJsonArray("from");
            JsonArray to = obj.getAsJsonArray("to");
            JsonObject faces = obj.getAsJsonObject("faces");
            if (from == null || to == null || faces == null) return;
            Cube c = new Cube(from.get(0).getAsFloat() * PIXEL, from.get(1).getAsFloat() * PIXEL, from.get(2).getAsFloat() * PIXEL,
                    to.get(0).getAsFloat() * PIXEL, to.get(1).getAsFloat() * PIXEL, to.get(2).getAsFloat() * PIXEL);
            c.north = face(faces, "north", primaryTextureRef);
            c.south = face(faces, "south", primaryTextureRef);
            c.east = face(faces, "east", primaryTextureRef);
            c.west = face(faces, "west", primaryTextureRef);
            c.up = face(faces, "up", primaryTextureRef);
            c.down = face(faces, "down", primaryTextureRef);
            if (obj.has("rotation")) c.rotation(obj.getAsJsonObject("rotation"));
            if (c.count() > 0) cubes.add(c);
        }

        private Face face(JsonObject faces, String name, String primaryTextureRef) {
            if (!faces.has(name)) return null;
            JsonObject face = faces.getAsJsonObject(name);
            if (face.has("texture") && !face.get("texture").getAsString().equals(primaryTextureRef)) return null;
            JsonArray uv = face.getAsJsonArray("uv");
            if (uv == null || uv.size() < 4) return null;
            return new Face(uv.get(0).getAsFloat() * PIXEL, uv.get(1).getAsFloat() * PIXEL,
                    uv.get(2).getAsFloat() * PIXEL, uv.get(3).getAsFloat() * PIXEL);
        }

        private void bake() {
            int faces = 0;
            for (Cube c : cubes) faces += c.count();
            mesh = new float[faces * 4 * STRIDE];
            int off = 0;
            for (Cube c : cubes) off = bakeCube(c, off);
            vertices = off / STRIDE;
            cubes.clear();
        }

        private int bakeCube(Cube c, int o) {
            if (c.north != null) o = quad(o, c, c.x2, c.y1, c.z1, c.x1, c.y1, c.z1, c.x1, c.y2, c.z1, c.x2, c.y2, c.z1, c.north, 0, 0, -1);
            if (c.south != null) o = quad(o, c, c.x1, c.y1, c.z2, c.x2, c.y1, c.z2, c.x2, c.y2, c.z2, c.x1, c.y2, c.z2, c.south, 0, 0, 1);
            if (c.east != null) o = quad(o, c, c.x2, c.y1, c.z2, c.x2, c.y1, c.z1, c.x2, c.y2, c.z1, c.x2, c.y2, c.z2, c.east, 1, 0, 0);
            if (c.west != null) o = quad(o, c, c.x1, c.y1, c.z1, c.x1, c.y1, c.z2, c.x1, c.y2, c.z2, c.x1, c.y2, c.z1, c.west, -1, 0, 0);
            if (c.up != null) o = quad(o, c, c.x1, c.y2, c.z1, c.x1, c.y2, c.z2, c.x2, c.y2, c.z2, c.x2, c.y2, c.z1, c.up, 0, 1, 0);
            if (c.down != null) o = quad(o, c, c.x1, c.y1, c.z2, c.x1, c.y1, c.z1, c.x2, c.y1, c.z1, c.x2, c.y1, c.z2, c.down, 0, -1, 0);
            return o;
        }

        private int quad(int o, Cube c, float x1, float y1, float z1, float x2, float y2, float z2,
                         float x3, float y3, float z3, float x4, float y4, float z4, Face f, float nx, float ny, float nz) {
            o = vertex(o, c, x1, y1, z1, f.u1, f.v2, nx, ny, nz);
            o = vertex(o, c, x2, y2, z2, f.u2, f.v2, nx, ny, nz);
            o = vertex(o, c, x3, y3, z3, f.u2, f.v1, nx, ny, nz);
            return vertex(o, c, x4, y4, z4, f.u1, f.v1, nx, ny, nz);
        }

        private int vertex(int o, Cube c, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
            if (c.axis != 0) {
                float dx = x - c.ox, dy = y - c.oy, dz = z - c.oz;
                float sin = c.sin, cos = c.cos;
                if (c.axis == 1) {
                    y = c.oy + dy * cos - dz * sin;
                    z = c.oz + dy * sin + dz * cos;
                    float rny = ny * cos - nz * sin;
                    float rnz = ny * sin + nz * cos;
                    ny = rny;
                    nz = rnz;
                }
                if (c.axis == 2) {
                    x = c.ox + dx * cos + dz * sin;
                    z = c.oz - dx * sin + dz * cos;
                    float rnx = nx * cos + nz * sin;
                    float rnz = -nx * sin + nz * cos;
                    nx = rnx;
                    nz = rnz;
                }
                if (c.axis == 3) {
                    x = c.ox + dx * cos - dy * sin;
                    y = c.oy + dx * sin + dy * cos;
                    float rnx = nx * cos - ny * sin;
                    float rny = nx * sin + ny * cos;
                    nx = rnx;
                    ny = rny;
                }
            }
            mesh[o++] = x - 0.5f; mesh[o++] = y - 0.5f; mesh[o++] = z - 0.5f;
            mesh[o++] = u; mesh[o++] = v; mesh[o++] = nx; mesh[o++] = ny; mesh[o++] = nz;
            return o;
        }

        private void readTransforms(JsonObject display) {
            if (display == null) return;
            transforms[0] = transform(display, "thirdperson_righthand");
            transforms[1] = transform(display, "thirdperson_lefthand");
            transforms[2] = transform(display, "firstperson_righthand");
            transforms[3] = transform(display, "firstperson_lefthand");
            transforms[4] = transform(display, "ground");
            transforms[5] = transform(display, "gui");
            transforms[6] = transform(display, "head");
            transforms[7] = transform(display, "fixed");
        }

        private DisplayTransform transform(JsonObject display, String key) {
            if (!display.has(key)) return null;
            JsonObject o = display.getAsJsonObject(key);
            return new DisplayTransform(arr(o, "rotation", 0), arr(o, "translation", 0), arr(o, "scale", 1));
        }

        private float[] arr(JsonObject o, String key, float def) {
            float[] out = {def, def, def};
            if (o.has(key)) {
                JsonArray a = o.getAsJsonArray(key);
                for (int i = 0; i < 3 && i < a.size(); i++) out[i] = a.get(i).getAsFloat();
            }
            return out;
        }

        private void applyTransform(ModelTransformationMode mode, MatrixStack matrices) {
            DisplayTransform t = switch (mode) {
                case THIRD_PERSON_RIGHT_HAND -> transforms[0];
                case THIRD_PERSON_LEFT_HAND -> transforms[1];
                case FIRST_PERSON_RIGHT_HAND -> transforms[2];
                case FIRST_PERSON_LEFT_HAND -> transforms[3];
                case GROUND -> transforms[4];
                case GUI -> transforms[5];
                case HEAD -> transforms[6];
                case FIXED -> transforms[7];
                default -> null;
            };
            if (t != null) t.apply(matrices, mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND);
        }
    }

    private static final class DisplayTransform {
        private final float[] r, t, s;
        private DisplayTransform(float[] r, float[] t, float[] s) { this.r = r; this.t = t; this.s = s; }
        private void apply(MatrixStack m, boolean left) {
            m.translate((left ? -t[0] : t[0]) / 16f, t[1] / 16f, t[2] / 16f);
            float ry = left ? -r[1] : r[1];
            float rz = left ? -r[2] : r[2];
            m.multiply(new Quaternionf().rotationXYZ(
                    (float) Math.toRadians(r[0]),
                    (float) Math.toRadians(ry),
                    (float) Math.toRadians(rz)
            ));
            m.scale(s[0], s[1], s[2]);
        }
    }

    private static final class Cube {
        final float x1, y1, z1, x2, y2, z2;
        Face north, south, east, west, up, down;
        float ox, oy, oz, sin, cos = 1f;
        int axis;
        Cube(float x1, float y1, float z1, float x2, float y2, float z2) {
            this.x1 = x1; this.y1 = y1; this.z1 = z1; this.x2 = x2; this.y2 = y2; this.z2 = z2;
        }
        int count() {
            return (north != null ? 1 : 0) + (south != null ? 1 : 0) + (east != null ? 1 : 0)
                    + (west != null ? 1 : 0) + (up != null ? 1 : 0) + (down != null ? 1 : 0);
        }
        void rotation(JsonObject r) {
            JsonArray o = r.getAsJsonArray("origin");
            ox = o.get(0).getAsFloat() * RuntimeSwordModel.PIXEL;
            oy = o.get(1).getAsFloat() * RuntimeSwordModel.PIXEL;
            oz = o.get(2).getAsFloat() * RuntimeSwordModel.PIXEL;
            axis = switch (r.get("axis").getAsString()) { case "x" -> 1; case "y" -> 2; case "z" -> 3; default -> 0; };
            float rad = (float) Math.toRadians(r.get("angle").getAsFloat());
            sin = (float) Math.sin(rad); cos = (float) Math.cos(rad);
        }
    }

    private record Face(float u1, float v1, float u2, float v2) {}
}
