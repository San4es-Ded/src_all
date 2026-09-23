package haron.gui.friends;

import haron.gui.friends.FriendsTab;
import net.minecraft.entity.Entity;

public final class FriendUtils {
    public static boolean isFriendName(String string) {
        return FriendsTab.isFriend(string);
    }

    public static boolean canAttack(Entity entity) {
        return FriendsTab.canAttack(entity);
    }

    public static boolean isFriend(Entity entity) {
        return FriendsTab.isFriend(entity);
    }

    private FriendUtils() {
    }

    public static boolean a(String string) {
        return FriendUtils.isFriendName(string);
    }
}

