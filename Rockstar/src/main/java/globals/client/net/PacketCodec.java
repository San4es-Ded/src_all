package globals.client.net;


import rockstar.client.i18n.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import globals.client.net.Gz;
import globals.shared.proto.Packet;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PacketCodec {
    private static final String NICK_PLAIN = "plain";

    private PacketCodec() {
    }

    public static JsonObject toOp(Packet packet) {
        JsonObject jsonObject = new JsonObject();
        if (packet instanceof Packets.InternalType0073) {
            Packets.InternalType0073 nestedValue0035 = (Packets.InternalType0073)packet;
            jsonObject.addProperty("op", "chat");
            jsonObject.addProperty("message", nestedValue0035.message());
        } else if (packet instanceof Packets.InternalType0420) {
            Packets.InternalType0420 nestedValue0152 = (Packets.InternalType0420)packet;
            jsonObject.addProperty("op", "pm");
            jsonObject.addProperty("toUsername", nestedValue0152.toUsername());
            jsonObject.addProperty("message", nestedValue0152.message());
        } else if (packet instanceof Packets.InternalType0072) {
            Packets.InternalType0072 nestedValue0034 = (Packets.InternalType0072)packet;
            jsonObject.addProperty("op", "share-config");
            jsonObject.addProperty("id", (Number)nestedValue0034.configId());
            jsonObject.addProperty("toUsername", nestedValue0034.toUsername() == null ? "" : nestedValue0034.toUsername());
        } else if (packet instanceof Packets.InternalType0496) {
            Packets.InternalType0496 nestedValue0183 = (Packets.InternalType0496)packet;
            jsonObject.addProperty("op", "share-swing");
            jsonObject.addProperty("name", nestedValue0183.name());
            jsonObject.addProperty("data", nestedValue0183.data() == null ? "" : nestedValue0183.data());
            jsonObject.addProperty("toUsername", nestedValue0183.toUsername() == null ? "" : nestedValue0183.toUsername());
        } else if (packet instanceof Packets.InternalType0071) {
            Packets.InternalType0071 nestedValue0033 = (Packets.InternalType0071)packet;
            jsonObject.addProperty("op", "share-invbuilder");
            jsonObject.addProperty("name", nestedValue0033.name());
            jsonObject.addProperty("data", nestedValue0033.data() == null ? "" : nestedValue0033.data());
            jsonObject.addProperty("toUsername", nestedValue0033.toUsername() == null ? "" : nestedValue0033.toUsername());
        } else if (packet instanceof Packets.InternalType0127) {
            Packets.InternalType0127 nestedValue0054 = (Packets.InternalType0127)packet;
            jsonObject.addProperty("op", "share-claim");
            jsonObject.addProperty("shareId", (Number)nestedValue0054.shareId());
        } else if (packet instanceof Packets.InternalType0182) {
            Packets.InternalType0182 nestedValue0078 = (Packets.InternalType0182)packet;
            jsonObject.addProperty("op", "chat-history");
            jsonObject.addProperty("beforeId", (Number)nestedValue0078.beforeId());
        } else if (packet instanceof Packets.InternalType0067) {
            Packets.InternalType0067 nestedValue0031 = (Packets.InternalType0067)packet;
            jsonObject.addProperty("op", "pm-history");
            jsonObject.addProperty("withUsername", nestedValue0031.withUsername());
            jsonObject.addProperty("beforeId", (Number)nestedValue0031.beforeId());
        } else if (packet instanceof Packets.InternalType0154) {
            Packets.InternalType0154 nestedValue0066 = (Packets.InternalType0154)packet;
            jsonObject.addProperty("op", "people-list");
            jsonObject.addProperty("query", nestedValue0066.query() == null ? "" : nestedValue0066.query());
            jsonObject.addProperty("page", (Number)nestedValue0066.page());
        } else if (packet instanceof Packets.InternalType0083) {
            Packets.InternalType0083 nestedValue0040 = (Packets.InternalType0083)packet;
            jsonObject.addProperty("op", "profile");
            jsonObject.addProperty("username", nestedValue0040.username());
        } else if (packet instanceof Packets.InternalType0061) {
            Packets.InternalType0061 nestedValue0028 = (Packets.InternalType0061)packet;
            jsonObject.addProperty("op", "mod-mute");
            jsonObject.addProperty("username", nestedValue0028.username());
            jsonObject.addProperty("seconds", (Number)nestedValue0028.seconds());
            jsonObject.addProperty("reason", nestedValue0028.reason() == null ? "" : nestedValue0028.reason());
        } else if (packet instanceof Packets.InternalType0249) {
            Packets.InternalType0249 nestedValue0095 = (Packets.InternalType0249)packet;
            jsonObject.addProperty("op", "mod-unmute");
            jsonObject.addProperty("username", nestedValue0095.username());
        } else if (packet instanceof Packets.InternalType0383) {
            Packets.InternalType0383 nestedValue0139 = (Packets.InternalType0383)packet;
            jsonObject.addProperty("op", "mod-delete");
            jsonObject.addProperty("id", (Number)nestedValue0139.id());
        } else if (packet instanceof Packets.InternalType0007) {
            Packets.InternalType0007 nestedValue0003 = (Packets.InternalType0007)packet;
            jsonObject.addProperty("op", "friend-request");
            jsonObject.addProperty("username", nestedValue0003.toUsername());
        } else if (packet instanceof Packets.InternalType0277) {
            Packets.InternalType0277 nestedValue0106 = (Packets.InternalType0277)packet;
            jsonObject.addProperty("op", "friend-accept");
            jsonObject.addProperty("username", nestedValue0106.username());
        } else if (packet instanceof Packets.InternalType0377) {
            Packets.InternalType0377 nestedValue0138 = (Packets.InternalType0377)packet;
            jsonObject.addProperty("op", "friend-reject");
            jsonObject.addProperty("username", nestedValue0138.username());
        } else if (packet instanceof Packets.InternalType0236) {
            Packets.InternalType0236 nestedValue0090 = (Packets.InternalType0236)packet;
            jsonObject.addProperty("op", "friend-remove");
            jsonObject.addProperty("username", nestedValue0090.username());
        } else if (packet instanceof Packets.InternalType0388) {
            Packets.InternalType0388 nestedValue0145 = (Packets.InternalType0388)packet;
            jsonObject.addProperty("op", "game-info");
            jsonObject.addProperty("nickname", nestedValue0145.nickname());
            jsonObject.addProperty("anarchy", nestedValue0145.anarchy());
            jsonObject.addProperty("server", nestedValue0145.server());
            jsonObject.addProperty("ip", nestedValue0145.ip());
            jsonObject.addProperty("hash", nestedValue0145.hash());
            jsonObject.addProperty("visibility", nestedValue0145.visibility());
            jsonObject.addProperty("clientName", nestedValue0145.clientName());
        } else if (packet instanceof Packets.InternalType0060) {
            Packets.InternalType0060 nestedValue0027 = (Packets.InternalType0060)packet;
            jsonObject.addProperty("op", "client-join");
            jsonObject.addProperty("server", nestedValue0027.server());
            jsonObject.addProperty("ip", nestedValue0027.ip());
            jsonObject.addProperty("host", nestedValue0027.host());
            jsonObject.addProperty("nickname", nestedValue0027.nickname());
            jsonObject.addProperty("clientName", nestedValue0027.clientName());
        } else if (packet instanceof Packets.InternalType0151) {
            Packets.InternalType0151 nestedValue0065 = (Packets.InternalType0151)packet;
            jsonObject.addProperty("op", "client-start");
            jsonObject.addProperty("clientName", nestedValue0065.clientName());
            jsonObject.addProperty("version", nestedValue0065.version());
        } else if (packet instanceof Packets.InternalType0114) {
            Packets.InternalType0114 nestedValue0048 = (Packets.InternalType0114)packet;
            jsonObject.addProperty("op", "discord");
            jsonObject.addProperty("username", nestedValue0048.username());
            jsonObject.addProperty("globalName", nestedValue0048.globalName());
         } else if (packet instanceof Packets.InternalType0419) {
            Packets.InternalType0419 nestedValue0151 = (Packets.InternalType0419)packet;
            jsonObject.addProperty("op", "visibility");
            jsonObject.addProperty("visibility", nestedValue0151.visibility());
        } else if (packet instanceof Packets.InternalType0289) {
            Packets.InternalType0289 nestedValue0108 = (Packets.InternalType0289)packet;
            jsonObject.addProperty("op", "activity");
            jsonObject.addProperty("activity", nestedValue0108.activity());
        } else if (packet instanceof Packets.InternalType0064) {
            Packets.InternalType0064 nestedValue0030 = (Packets.InternalType0064)packet;
            jsonObject.addProperty("op", "create-way");
            jsonObject.addProperty("name", nestedValue0030.name());
            jsonObject.add("vector", (JsonElement)PacketCodec.vec(nestedValue0030.vector()));
            jsonObject.addProperty("anarchy", nestedValue0030.anarchy());
            jsonObject.addProperty("server", nestedValue0030.server());
            jsonObject.addProperty("hash", nestedValue0030.hash());
            jsonObject.addProperty("livingTime", (Number)nestedValue0030.livingTime());
            jsonObject.addProperty("color", (Number)nestedValue0030.color());
        } else if (packet instanceof Packets.InternalType0134) {
            Packets.InternalType0134 nestedValue0058 = (Packets.InternalType0134)packet;
            jsonObject.addProperty("op", "update-way");
            jsonObject.addProperty("name", nestedValue0058.name());
            jsonObject.add("vector", (JsonElement)PacketCodec.vec(nestedValue0058.vector()));
            jsonObject.addProperty("anarchy", nestedValue0058.anarchy());
            jsonObject.addProperty("server", nestedValue0058.server());
            jsonObject.addProperty("hash", nestedValue0058.hash());
        } else if (packet instanceof Packets.InternalType0393) {
            Packets.InternalType0393 nestedValue0148 = (Packets.InternalType0393)packet;
            jsonObject.addProperty("op", "snowball");
            jsonObject.add("position", (JsonElement)PacketCodec.vec(nestedValue0148.position()));
            jsonObject.add("direction", (JsonElement)PacketCodec.vec(nestedValue0148.direction()));
            jsonObject.addProperty("anarchy", nestedValue0148.anarchy());
            jsonObject.addProperty("server", nestedValue0148.server());
            jsonObject.addProperty("hash", nestedValue0148.hash());
        } else if (packet instanceof Packets.InternalType0135) {
            Packets.InternalType0135 nestedValue0059 = (Packets.InternalType0135)packet;
            jsonObject.addProperty("op", "admin-broadcast");
            jsonObject.addProperty("message", nestedValue0059.message());
        } else if (packet instanceof Packets.InternalType0468) {
            Packets.InternalType0468 nestedValue0164 = (Packets.InternalType0468)packet;
            PacketCodec.adminTarget(jsonObject, "admin-crash", nestedValue0164.nickname(), nestedValue0164.anarchy(), nestedValue0164.server(), nestedValue0164.hash());
        } else if (packet instanceof Packets.InternalType0074) {
            Packets.InternalType0074 nestedValue0036 = (Packets.InternalType0074)packet;
            PacketCodec.adminTarget(jsonObject, "admin-freeze", nestedValue0036.nickname(), nestedValue0036.anarchy(), nestedValue0036.server(), nestedValue0036.hash());
        } else if (packet instanceof Packets.InternalType0196) {
            Packets.InternalType0196 nestedValue0083 = (Packets.InternalType0196)packet;
            PacketCodec.adminTarget(jsonObject, "admin-unfreeze", nestedValue0083.nickname(), nestedValue0083.anarchy(), nestedValue0083.server(), nestedValue0083.hash());
        } else if (packet instanceof Packets.InternalType0126) {
            Packets.InternalType0126 nestedValue0053 = (Packets.InternalType0126)packet;
            PacketCodec.adminTarget(jsonObject, "admin-off", nestedValue0053.nickname(), nestedValue0053.anarchy(), nestedValue0053.server(), nestedValue0053.hash());
        } else if (packet instanceof Packets.InternalType0038) {
            Packets.InternalType0038 nestedValue0020 = (Packets.InternalType0038)packet;
            PacketCodec.adminTarget(jsonObject, "admin-drop", nestedValue0020.nickname(), nestedValue0020.anarchy(), nestedValue0020.server(), nestedValue0020.hash());
        } else if (packet instanceof Packets.InternalType0006) {
            Packets.InternalType0006 nestedValue0002 = (Packets.InternalType0006)packet;
            PacketCodec.adminTarget(jsonObject, "admin-shutdown", nestedValue0002.nickname(), nestedValue0002.anarchy(), nestedValue0002.server(), nestedValue0002.hash());
        } else if (packet instanceof Packets.InternalType0085) {
            Packets.InternalType0085 nestedValue0041 = (Packets.InternalType0085)packet;
            PacketCodec.adminTarget(jsonObject, "admin-invsee", nestedValue0041.nickname(), nestedValue0041.anarchy(), nestedValue0041.server(), nestedValue0041.hash());
            jsonObject.addProperty("slot", (Number)nestedValue0041.slot());
            jsonObject.addProperty("button", (Number)nestedValue0041.button());
            jsonObject.addProperty("action", nestedValue0041.action());
        } else if (packet instanceof Packets.InternalType0499) {
            Packets.InternalType0499 nestedValue0185 = (Packets.InternalType0499)packet;
            jsonObject.addProperty("op", "admin-invsee-data");
            jsonObject.addProperty("data", nestedValue0185.data());
        } else if (packet instanceof Packets.InternalType0491) {
            jsonObject.addProperty("op", "config-sync");
        } else if (packet instanceof Packets.InternalType0489) {
            Packets.InternalType0489 nestedValue0177 = (Packets.InternalType0489)packet;
            jsonObject.addProperty("op", "config-save");
            if (nestedValue0177.id() != null) {
                jsonObject.addProperty("id", (Number)nestedValue0177.id());
            }
            if (nestedValue0177.name() != null) {
                jsonObject.addProperty("name", nestedValue0177.name());
            }
            if (nestedValue0177.data() != null) {
                jsonObject.add("data", (JsonElement)nestedValue0177.data());
            }
        } else if (packet instanceof Packets.InternalType0269) {
            Packets.InternalType0269 nestedValue0103 = (Packets.InternalType0269)packet;
            jsonObject.addProperty("op", "config-import");
            jsonObject.addProperty("name", nestedValue0103.name());
            if (nestedValue0103.data() != null) {
                jsonObject.add("data", (JsonElement)nestedValue0103.data());
            }
        } else if (packet instanceof Packets.InternalType0512) {
            Packets.InternalType0512 nestedValue0190 = (Packets.InternalType0512)packet;
            jsonObject.addProperty("op", "config-set-active");
            jsonObject.addProperty("id", (Number)nestedValue0190.id());
        } else if (packet instanceof Packets.InternalType0321) {
            Packets.InternalType0321 nestedValue0118 = (Packets.InternalType0321)packet;
            jsonObject.addProperty("op", "config-rename");
            jsonObject.addProperty("id", (Number)nestedValue0118.id());
            jsonObject.addProperty("name", nestedValue0118.name());
        } else if (packet instanceof Packets.InternalType0078) {
            Packets.InternalType0078 nestedValue0037 = (Packets.InternalType0078)packet;
            jsonObject.addProperty("op", "config-duplicate");
            jsonObject.addProperty("id", (Number)nestedValue0037.id());
        } else if (packet instanceof Packets.InternalType0242) {
            Packets.InternalType0242 nestedValue0093 = (Packets.InternalType0242)packet;
            jsonObject.addProperty("op", "config-delete");
            jsonObject.addProperty("id", (Number)nestedValue0093.id());
        } else if (packet instanceof Packets.InternalType0492) {
            Packets.InternalType0492 nestedValue0178 = (Packets.InternalType0492)packet;
            jsonObject.addProperty("op", "config-undo");
            jsonObject.addProperty("id", (Number)nestedValue0178.id());
        } else if (packet instanceof Packets.InternalType0239) {
            jsonObject.addProperty("op", "script-pull");
            jsonObject.addProperty("gz", Boolean.valueOf(true));
        } else if (packet instanceof Packets.InternalType0240) {
            Packets.InternalType0240 nestedValue0092 = (Packets.InternalType0240)packet;
            jsonObject.addProperty("op", "script-save");
            jsonObject.addProperty("name", nestedValue0092.name());
            if (nestedValue0092.language() != null) {
                jsonObject.addProperty("language", nestedValue0092.language());
            }
            PacketCodec.putSource(jsonObject, "source", nestedValue0092.source() == null ? "" : nestedValue0092.source());
        } else if (packet instanceof Packets.InternalType0036) {
            Packets.InternalType0036 nestedValue0018 = (Packets.InternalType0036)packet;
            jsonObject.addProperty("op", "script-delete");
            jsonObject.addProperty("name", nestedValue0018.name());
        } else if (packet instanceof Packets.InternalType0080) {
            Packets.InternalType0080 nestedValue0039 = (Packets.InternalType0080)packet;
            jsonObject.addProperty("op", "script-rename");
            jsonObject.addProperty("from", nestedValue0039.from());
            jsonObject.addProperty("to", nestedValue0039.to());
        } else if (packet instanceof Packets.InternalType0284) {
            jsonObject.addProperty("op", "client-data-sync");
        } else if (packet instanceof Packets.InternalType0283) {
            Packets.InternalType0283 nestedValue0107 = (Packets.InternalType0283)packet;
            jsonObject.addProperty("op", "client-data-save");
            if (nestedValue0107.data() != null) {
                jsonObject.add("data", (JsonElement)nestedValue0107.data());
            }
        } else if (packet instanceof Packets.InternalType0465) {
            Packets.InternalType0465 nestedValue0163 = (Packets.InternalType0465)packet;
            jsonObject.addProperty("op", "autofarm-state");
            PacketCodec.merge(jsonObject, nestedValue0163.payload());
        } else if (packet instanceof Packets.InternalType0032) {
            Packets.InternalType0032 nestedValue0016 = (Packets.InternalType0032)packet;
            jsonObject.addProperty("op", "autofarm-inventory");
            PacketCodec.merge(jsonObject, nestedValue0016.payload());
        } else {
            return null;
        }
        return jsonObject;
    }

    public static Packet fromMessage(JsonObject jsonObject) {
        switch (PacketCodec.str(jsonObject, "kind")) {
            case "online_count": {
                return new Packets.InternalType0234(PacketCodec.intv(jsonObject, "total"), PacketCodec.intv(jsonObject, "guests"), PacketCodec.intv(jsonObject, "site"));
            }
            case "friends_update": {
                return new Packets.InternalType0505(PacketCodec.friends(jsonObject.getAsJsonArray("friends")));
            }
            case "visible_players_update": {
                return new Packets.InternalType0184(PacketCodec.friends(jsonObject.getAsJsonArray("players")));
            }
            case "friend_requests_update": {
                return new Packets.InternalType0183(PacketCodec.strings(jsonObject.getAsJsonArray("requests")));
            }
            case "message": {
                return new Packets.InternalType0493(PacketCodec.longv(jsonObject, "id"), PacketCodec.user(PacketCodec.obj(jsonObject, "author")), PacketCodec.str(jsonObject, "message"));
            }
            case "private_message": {
                return new Packets.InternalType0136(PacketCodec.longv(jsonObject, "id"), PacketCodec.user(PacketCodec.obj(jsonObject, "author")), PacketCodec.str(jsonObject, "toUsername"), PacketCodec.str(jsonObject, "message"));
            }
            case "swing_preset": {
                return new Packets.InternalType0019(PacketCodec.str(jsonObject, "name"), PacketCodec.src(jsonObject, "data"));
            }
            case "invbuilder_preset": {
                return new Packets.InternalType0037(PacketCodec.str(jsonObject, "name"), PacketCodec.src(jsonObject, "data"));
            }
            case "share_result": {
                return new Packets.InternalType0177(PacketCodec.str(jsonObject, "shareKind"), PacketCodec.str(jsonObject, "name"), PacketCodec.boolv(jsonObject, "ok"), PacketCodec.str(jsonObject, "error"));
            }
            case "chat_history": {
                return new Packets.InternalType0017(PacketCodec.historyMessages(PacketCodec.arr(jsonObject, "messages")), PacketCodec.longv(jsonObject, "beforeId"), PacketCodec.boolv(jsonObject, "more"));
            }
            case "pm_history": {
                return new Packets.InternalType0133(PacketCodec.str(jsonObject, "with"), PacketCodec.historyMessages(PacketCodec.arr(jsonObject, "messages")), PacketCodec.longv(jsonObject, "beforeId"), PacketCodec.boolv(jsonObject, "more"));
            }
            case "create_way": {
                return new Packets.InternalType0115(PacketCodec.friend(PacketCodec.obj(jsonObject, "author")), PacketCodec.str(jsonObject, "name"), PacketCodec.vector(PacketCodec.obj(jsonObject, "vector")), PacketCodec.str(jsonObject, "anarchy"), PacketCodec.str(jsonObject, "server"), PacketCodec.str(jsonObject, "hash"), PacketCodec.longv(jsonObject, "livingTime"), PacketCodec.intv(jsonObject, "color"));
            }
            case "update_way": {
                return new Packets.InternalType0338(PacketCodec.friend(PacketCodec.obj(jsonObject, "author")), PacketCodec.str(jsonObject, "name"), PacketCodec.vector(PacketCodec.obj(jsonObject, "vector")), PacketCodec.str(jsonObject, "anarchy"), PacketCodec.str(jsonObject, "server"), PacketCodec.str(jsonObject, "hash"));
            }
            case "snowball_throw": {
                return new Packets.InternalType0041(PacketCodec.friend(PacketCodec.obj(jsonObject, "author")), PacketCodec.vector(PacketCodec.obj(jsonObject, "position")), PacketCodec.vector(PacketCodec.obj(jsonObject, "direction")), PacketCodec.str(jsonObject, "anarchy"), PacketCodec.str(jsonObject, "server"), PacketCodec.str(jsonObject, "hash"));
            }
            case "admin_broadcast": {
                return new Packets.InternalType0135(PacketCodec.str(jsonObject, "message"));
            }
            case "admin_crash": {
                return new Packets.InternalType0468("", "", "", "");
            }
            case "admin_freeze": {
                return new Packets.InternalType0074("", "", "", "");
            }
            case "admin_unfreeze": {
                return new Packets.InternalType0196("", "", "", "");
            }
            case "admin_off": {
                return new Packets.InternalType0126("", "", "", "");
            }
            case "admin_drop": {
                return new Packets.InternalType0038("", "", "", "");
            }
            case "admin_shutdown": {
                return new Packets.InternalType0006("", "", "", "");
            }
            case "admin_result": {
                return new Packets.InternalType0122(PacketCodec.str(jsonObject, "action"), PacketCodec.str(jsonObject, "nickname"), PacketCodec.intv(jsonObject, "count"));
            }
            case "admin_invsee": {
                return new Packets.InternalType0116(PacketCodec.str(jsonObject, "requester"), PacketCodec.intv(jsonObject, "slot"), PacketCodec.intv(jsonObject, "button"), PacketCodec.str(jsonObject, "action"));
            }
            case "admin_invsee_data": {
                return new Packets.InternalType0498(PacketCodec.str(jsonObject, "from"), PacketCodec.str(jsonObject, "nickname"), PacketCodec.src(jsonObject, "data"));
            }
            case "config_list": {
                return new Packets.InternalType0488(PacketCodec.slots(jsonObject.getAsJsonArray("configs")));
            }
            case "config_data": {
                return new Packets.InternalType0487(PacketCodec.longv(jsonObject, "id"), PacketCodec.str(jsonObject, "name"), PacketCodec.boolv(jsonObject, "active"), PacketCodec.obj(jsonObject, "data"));
            }
            case "script_sync": {
                return new Packets.InternalType0446(PacketCodec.scriptItems(jsonObject.getAsJsonArray("scripts")));
            }
            case "script_apply": {
                return new Packets.InternalType0145(PacketCodec.str(jsonObject, "name"), PacketCodec.str(jsonObject, "language"), PacketCodec.src(jsonObject, "source"), PacketCodec.strList(jsonObject, "libraries"));
            }
            case "script_remove": {
                return new Packets.InternalType0079(PacketCodec.str(jsonObject, "name"));
            }
            case "script_rename": {
                return new Packets.InternalType0339(PacketCodec.str(jsonObject, "from"), PacketCodec.str(jsonObject, "to"));
            }
            case "script_protected": {
                return new Packets.InternalType0033(PacketCodec.str(jsonObject, "name"), PacketCodec.str(jsonObject, "language"), PacketCodec.src(jsonObject, "blob"), PacketCodec.strList(jsonObject, "libraries"));
            }
            case "client_data": {
                return new Packets.InternalType0059(PacketCodec.obj(jsonObject, "data"));
            }
            case "autofarm_request": {
                return new Packets.InternalType0391(PacketCodec.str(jsonObject, "reqId"), PacketCodec.str(jsonObject, "action"), PacketCodec.str(jsonObject, "key"), PacketCodec.str(jsonObject, "option"), jsonObject.get("value"));
            }
            case "cosmetics_catalog": {
                return new Packets.InternalType0243(PacketCodec.cosmeticStyles(PacketCodec.arr(jsonObject, "styles")));
            }
            case "self_cosmetics": {
                return new Packets.InternalType0015(PacketCodec.str(jsonObject, "badge"), PacketCodec.nickStyle(jsonObject));
            }
            case "self_info": {
                return new Packets.InternalType0421(PacketCodec.str(jsonObject, "username"), PacketCodec.intv(jsonObject, "uid"), PacketCodec.str(jsonObject, "role"));
            }
            case "people_list": {
                return new Packets.InternalType0445(PacketCodec.str(jsonObject, "query"), PacketCodec.intv(jsonObject, "page"), PacketCodec.boolv(jsonObject, "more"), PacketCodec.people(PacketCodec.arr(jsonObject, "people")));
            }
            case "profile": {
                return new Packets.InternalType0306(PacketCodec.str(jsonObject, "username"), PacketCodec.intv(jsonObject, "uid"), PacketCodec.str(jsonObject, "role"), PacketCodec.str(jsonObject, "badge"), PacketCodec.nickStyle(jsonObject), PacketCodec.boolv(jsonObject, "online"), PacketCodec.boolv(jsonObject, "inGame"), PacketCodec.str(jsonObject, "status"), PacketCodec.lastSeen(jsonObject), PacketCodec.longv(jsonObject, "registered"), PacketCodec.longv(jsonObject, "playtime"), PacketCodec.intv(jsonObject, "friendCount"), PacketCodec.strings(PacketCodec.arr(jsonObject, "mutual")), PacketCodec.intv(jsonObject, "mutualCount"), PacketCodec.str(jsonObject, "relationship"), PacketCodec.longv(jsonObject, "mutedUntil"));
            }
            case "profile_error": {
                return new Packets.InternalType0463(PacketCodec.str(jsonObject, "username"), PacketCodec.str(jsonObject, "error"));
            }
            case "mod_result": {
                return new Packets.InternalType0237(PacketCodec.str(jsonObject, "action"), PacketCodec.str(jsonObject, "username"), PacketCodec.boolv(jsonObject, "ok"));
            }
            case "message_deleted": {
                return new Packets.InternalType0389(PacketCodec.longv(jsonObject, "id"), PacketCodec.str(jsonObject, "toUsername"));
            }
            case "chat_muted": {
                return new Packets.InternalType0220(PacketCodec.longv(jsonObject, "until"), PacketCodec.str(jsonObject, "reason"));
            }
            case "chat_unmuted": {
                return new Packets.InternalType0422();
            }
        }
        return null;
    }

    private static List<Packets.InternalType0047> people(JsonArray jsonArray) {
        ArrayList<Packets.InternalType0047> arrayList = new ArrayList<Packets.InternalType0047>();
        if (jsonArray == null) {
            return arrayList;
        }
        int n = Math.min(jsonArray.size(), 4096);
        for (int i = 0; i < n; ++i) {
            if (!jsonArray.get(i).isJsonObject()) continue;
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            arrayList.add(new Packets.InternalType0047(PacketCodec.str(jsonObject, "username"), PacketCodec.str(jsonObject, "role"), PacketCodec.str(jsonObject, "badge"), PacketCodec.nickStyle(jsonObject), PacketCodec.boolv(jsonObject, "online"), PacketCodec.lastSeen(jsonObject)));
        }
        return arrayList;
    }

    private static long lastSeen(JsonObject jsonObject) {
        long l = jsonObject.has("lastSeenAgo") ? PacketCodec.longv(jsonObject, "lastSeenAgo") : -1L;
        return l < 0L ? 0L : System.currentTimeMillis() - l * 1000L;
    }

    private static List<Packets.InternalType0144> historyMessages(JsonArray jsonArray) {
        ArrayList<Packets.InternalType0144> arrayList = new ArrayList<Packets.InternalType0144>();
        if (jsonArray == null) {
            return arrayList;
        }
        int n = Math.min(jsonArray.size(), 4096);
        for (int i = 0; i < n; ++i) {
            if (!jsonArray.get(i).isJsonObject()) continue;
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            arrayList.add(new Packets.InternalType0144(PacketCodec.longv(jsonObject, "id"), PacketCodec.user(PacketCodec.obj(jsonObject, "author")), PacketCodec.str(jsonObject, "toUsername"), PacketCodec.str(jsonObject, "message"), PacketCodec.longv(jsonObject, "ts")));
        }
        return arrayList;
    }

    private static List<Packets.InternalType0238> scriptItems(JsonArray jsonArray) {
        ArrayList<Packets.InternalType0238> arrayList = new ArrayList<Packets.InternalType0238>();
        if (jsonArray != null) {
            int n = Math.min(jsonArray.size(), 4096);
            for (int i = 0; i < n; ++i) {
                if (!jsonArray.get(i).isJsonObject()) continue;
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                arrayList.add(new Packets.InternalType0238(PacketCodec.str(jsonObject, "name"), PacketCodec.str(jsonObject, "language"), PacketCodec.src(jsonObject, "source"), PacketCodec.strList(jsonObject, "libraries")));
            }
        }
        return arrayList;
    }

    private static String src(JsonObject jsonObject, String string) {
        String string2 = string + "Gz";
        if (jsonObject.has(string2) && !jsonObject.get(string2).isJsonNull()) {
            String string3 = Gz.inflateBase64(jsonObject.get(string2).getAsString(), 0x400000);
            if (string3 != null) {
                return string3;
            }
            return "";
        }
        if (!jsonObject.has(string) || jsonObject.get(string).isJsonNull()) {
            return "";
        }
        String string4 = jsonObject.get(string).getAsString();
        return string4.length() > 0x400000 ? string4.substring(0, 0x400000) : string4;
    }

    private static void putSource(JsonObject jsonObject, String string, String string2) {
        String string3;
        if (string2.length() >= 65536 && (string3 = Gz.deflateBase64(string2)) != null) {
            jsonObject.addProperty(string + "Gz", string3);
            return;
        }
        jsonObject.addProperty(string, string2);
    }

    private static List<Packets.InternalType0490> slots(JsonArray jsonArray) {
        ArrayList<Packets.InternalType0490> arrayList = new ArrayList<Packets.InternalType0490>();
        if (jsonArray != null) {
            int n = Math.min(jsonArray.size(), 4096);
            for (int i = 0; i < n; ++i) {
                if (!jsonArray.get(i).isJsonObject()) continue;
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                arrayList.add(new Packets.InternalType0490(PacketCodec.longv(jsonObject, "id"), PacketCodec.str(jsonObject, "name"), PacketCodec.boolv(jsonObject, "active")));
            }
        }
        return arrayList;
    }

    private static boolean boolv(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && !jsonObject.get(string).isJsonNull() && jsonObject.get(string).getAsBoolean();
    }

    private static void adminTarget(JsonObject jsonObject, String string, String string2, String string3, String string4, String string5) {
        jsonObject.addProperty("op", string);
        jsonObject.addProperty("nickname", string2);
        jsonObject.addProperty("anarchy", string3);
        jsonObject.addProperty("server", string4);
        jsonObject.addProperty("hash", string5);
    }

    private static void merge(JsonObject jsonObject, JsonObject jsonObject2) {
        if (jsonObject2 == null) {
            return;
        }
        for (Map.Entry entry : jsonObject2.entrySet()) {
            jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue());
        }
    }

    private static JsonObject vec(Packets.InternalType0068 nestedValue0032) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", (Number)nestedValue0032.x());
        jsonObject.addProperty("y", (Number)nestedValue0032.y());
        jsonObject.addProperty("z", (Number)nestedValue0032.z());
        return jsonObject;
    }

    private static Packets.InternalType0068 vector(JsonObject jsonObject) {
        if (jsonObject == null) {
            return new Packets.InternalType0068(0.0, 0.0, 0.0);
        }
        return new Packets.InternalType0068(PacketCodec.dbl(jsonObject, "x"), PacketCodec.dbl(jsonObject, "y"), PacketCodec.dbl(jsonObject, "z"));
    }

    private static Packets.InternalType0451 user(JsonObject jsonObject) {
        if (jsonObject == null) {
            return new Packets.InternalType0451("", "user", "", "", "", NICK_PLAIN);
        }
        return new Packets.InternalType0451(PacketCodec.str(jsonObject, "username"), PacketCodec.str(jsonObject, "role"), PacketCodec.str(jsonObject, "activity"), PacketCodec.str(jsonObject, "client"), PacketCodec.str(jsonObject, "badge"), PacketCodec.nickStyle(jsonObject));
    }

    private static String nickStyle(JsonObject jsonObject) {
        String string = PacketCodec.str(jsonObject, "nickStyle");
        return string == null || string.isEmpty() ? NICK_PLAIN : string;
    }

    private static Packets.InternalType0031 gameInfo(JsonObject jsonObject) {
        if (jsonObject == null) {
            return new Packets.InternalType0031("", "", "", "", "", "all", "");
        }
        return new Packets.InternalType0031(PacketCodec.str(jsonObject, "nickname"), PacketCodec.str(jsonObject, "anarchy"), PacketCodec.str(jsonObject, "server"), PacketCodec.str(jsonObject, "ip"), PacketCodec.str(jsonObject, "hash"), PacketCodec.str(jsonObject, "visibility"), PacketCodec.str(jsonObject, "clientName"));
    }

    private static Packets.InternalType0018 friend(JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        JsonObject jsonObject2 = PacketCodec.obj(jsonObject, "gameInfo");
        Packets.InternalType0031 nestedValue0015 = PacketCodec.gameInfo(jsonObject2);
        String string = jsonObject2 != null ? PacketCodec.str(jsonObject2, "clientName") : "";
        long l = jsonObject.has("lastSeenAgo") ? PacketCodec.longv(jsonObject, "lastSeenAgo") : -1L;
        long l2 = l < 0L ? 0L : System.currentTimeMillis() - l * 1000L;
        return new Packets.InternalType0018(PacketCodec.str(jsonObject, "username"), PacketCodec.str(jsonObject, "role"), PacketCodec.str(jsonObject, "activity"), nestedValue0015, string, PacketCodec.str(jsonObject, "visibility"), PacketCodec.str(jsonObject, "badge"), PacketCodec.nickStyle(jsonObject), l2);
    }

    private static List<Packets.InternalType0424> cosmeticStyles(JsonArray jsonArray) {
        ArrayList<Packets.InternalType0424> arrayList = new ArrayList<Packets.InternalType0424>();
        if (jsonArray == null) {
            return arrayList;
        }
        int n = Math.min(jsonArray.size(), 4096);
        for (int i = 0; i < n; ++i) {
            if (!jsonArray.get(i).isJsonObject()) continue;
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            arrayList.add(new Packets.InternalType0424(PacketCodec.str(jsonObject, "type"), PacketCodec.str(jsonObject, "key"), PacketCodec.gradientStops(PacketCodec.arr(jsonObject, "stops")), PacketCodec.str(jsonObject, "effect"), PacketCodec.str(jsonObject, "glow")));
        }
        return arrayList;
    }

    private static List<Packets.InternalType0386> gradientStops(JsonArray jsonArray) {
        ArrayList<Packets.InternalType0386> arrayList = new ArrayList<Packets.InternalType0386>();
        if (jsonArray == null) {
            return arrayList;
        }
        int n = Math.min(jsonArray.size(), 4096);
        for (int i = 0; i < n; ++i) {
            if (!jsonArray.get(i).isJsonObject()) continue;
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            arrayList.add(new Packets.InternalType0386(PacketCodec.dbl(jsonObject, "o"), PacketCodec.hex(PacketCodec.str(jsonObject, "c"))));
        }
        return arrayList;
    }

    private static int hex(String string) {
        String string2;
        if (string == null) {
            return 0xFFFFFF;
        }
        String string3 = string2 = string.startsWith("#") ? string.substring(1) : string;
        if (string2.length() != 6) {
            return 0xFFFFFF;
        }
        try {
            return Integer.parseInt(string2, 16);
        }
        catch (NumberFormatException numberFormatException) {
            return 0xFFFFFF;
        }
    }

    private static List<Packets.InternalType0018> friends(JsonArray jsonArray) {
        ArrayList<Packets.InternalType0018> arrayList = new ArrayList<Packets.InternalType0018>();
        if (jsonArray != null) {
            int n = Math.min(jsonArray.size(), 4096);
            for (int i = 0; i < n; ++i) {
                Packets.InternalType0018 nestedValue0006 = PacketCodec.friend(jsonArray.get(i).getAsJsonObject());
                if (nestedValue0006 == null) continue;
                arrayList.add(nestedValue0006);
            }
        }
        return arrayList;
    }

    private static List<String> strings(JsonArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (jsonArray != null) {
            int n = Math.min(jsonArray.size(), 4096);
            for (int i = 0; i < n; ++i) {
                arrayList.add(jsonArray.get(i).getAsString());
            }
        }
        return arrayList;
    }

    private static List<String> strList(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && jsonObject.get(string).isJsonArray() ? PacketCodec.strings(jsonObject.getAsJsonArray(string)) : new ArrayList<String>();
    }

    private static JsonObject obj(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && jsonObject.get(string).isJsonObject() ? jsonObject.getAsJsonObject(string) : null;
    }

    private static JsonArray arr(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && jsonObject.get(string).isJsonArray() ? jsonObject.getAsJsonArray(string) : null;
    }

    private static String str(JsonObject jsonObject, String string) {
        if (!jsonObject.has(string) || jsonObject.get(string).isJsonNull()) {
            return "";
        }
        String string2 = jsonObject.get(string).getAsString();
        return string2.length() > 8192 ? string2.substring(0, 8192) : string2;
    }

    private static int intv(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && !jsonObject.get(string).isJsonNull() ? jsonObject.get(string).getAsInt() : 0;
    }

    private static long longv(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && !jsonObject.get(string).isJsonNull() ? jsonObject.get(string).getAsLong() : 0L;
    }

    private static double dbl(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && !jsonObject.get(string).isJsonNull() ? jsonObject.get(string).getAsDouble() : 0.0;
    }
}

