package globals.client;



import rockstar.client.i18n.*;
import rockstar.client.internal.network.*;
import globals.client.Chat;
import globals.client.GlobalsUser;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;


public class Information {
    public static final String GLOBAL_CHAT = LanguageManager.internalMethod07214("rocknet.chat.global.id");
    private static GlobalsUser preferUser;
    private static GlobalsUser user;
    private static String result;
    private static final Object chatsLock;
    private static final Map<String, Chat> chats;
    private static List<String> requests;
    public static List<Packets.InternalType0018> friends;
    private static volatile Map<String, Packets.InternalType0018> friendIndex;
    private static volatile int friendsVersion;
    public static List<Packets.InternalType0018> visiblePlayers;
    private static Packets.InternalType0421 self;
    private static final List<Packets.InternalType0047> people;
    private static String peopleQuery;
    private static int peoplePage;
    private static boolean peopleMore;
    private static boolean peopleLoading;
    private static long peopleRequestedAt;
    private static long profileRequestedAt;
    private static final Map<String, Packets.InternalType0047> peopleIndex;
    private static Packets.InternalType0306 profile;
    private static String profileLoading;
    private static final Map<String, Packets.InternalType0306> peers;
    private static final Map<String, Long> peersAsked;
    private static long peerAskedAt;
    private static long muteUntil;
    private static String muteReason;


    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List<Chat> chats() {
        Object object = chatsLock;
        synchronized (object) {
            return new ArrayList<Chat>(chats.values());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Chat byName(String string) {
        Object object = chatsLock;
        synchronized (object) {
            Chat chat = chats.get(string);
            if (chat == null) {
                chat = new Chat(string);
                chats.put(string, chat);
            }
            return chat;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void moveToTop(String string) {
        Object object = chatsLock;
        synchronized (object) {
            if (!chats.containsKey(string)) {
                return;
            }
            Chat chat = chats.get(string);
            chats.remove(string);
            LinkedHashMap<String, Chat> linkedHashMap = new LinkedHashMap<String, Chat>();
            linkedHashMap.put(string, chat);
            linkedHashMap.putAll(chats);
            chats.clear();
            chats.putAll(linkedHashMap);
        }
    }

    public static List<String> dialogs() {
        List<Chat> list = Information.chats();
        list.removeIf(chat -> chat.getName().equals(GLOBAL_CHAT) || chat.getLatestMessage() == null);
        list.sort((chat, chat2) -> Long.compare(chat2.getLastMessageTime(), chat.getLastMessageTime()));
        ArrayList<String> arrayList = new ArrayList<String>(list.size());
        for (Chat chat3 : list) {
            arrayList.add(chat3.getName());
        }
        return arrayList;
    }

    public static void setFriends(List<Packets.InternalType0018> arrayList) {
        friends = arrayList == null ? new ArrayList() : arrayList;
        HashMap<String, Packets.InternalType0018> hashMap = new HashMap<String, Packets.InternalType0018>();
        for (Packets.InternalType0018 nestedValue0006 : friends) {
            if (nestedValue0006 == null || nestedValue0006.username() == null) continue;
            hashMap.put(nestedValue0006.username(), nestedValue0006);
        }
        friendIndex = hashMap;
        ++friendsVersion;
    }

    public static Packets.InternalType0018 friend(String string) {
        return string == null ? null : friendIndex.get(string);
    }

    public static List<Packets.InternalType0018> getSortedFriends() {
        ArrayList<Packets.InternalType0018> arrayList = new ArrayList<Packets.InternalType0018>(friends);
        arrayList.sort((nestedValue0006, nestedValue0007) -> {
            Chat chat = Information.byName(nestedValue0006.username());
            Chat chat2 = Information.byName(nestedValue0007.username());
            return Long.compare(chat2.getLastMessageTime(), chat.getLastMessageTime());
        });
        return arrayList;
    }

    public static void setVisiblePlayers(List<Packets.InternalType0018> list) {
        ArrayList<Packets.InternalType0018> arrayList = new ArrayList<Packets.InternalType0018>();
        for (Packets.InternalType0018 nestedValue0006 : list) {
            Packets.InternalType0031 nestedValue0015 = nestedValue0006.gameInfo();
            if (nestedValue0015 != null && (nestedValue0015.nickname() == null || nestedValue0015.nickname().isBlank())) continue;
            arrayList.add(nestedValue0006);
        }
        visiblePlayers = arrayList;
    }

    public static boolean staff() {
        return self != null && ("admin".equals(self.role()) || "moderator".equals(self.role()));
    }

    public static void requestPeople(String string, int n) {
        if (peopleLoading) {
            return;
        }
        peopleLoading = true;
        peopleRequestedAt = System.currentTimeMillis();
        peopleQuery = string == null ? "" : string;
        RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0154(peopleQuery, n));
    }

    public static boolean peopleFailed() {
        return peopleLoading && System.currentTimeMillis() - peopleRequestedAt > 8000L;
    }

    public static boolean profileFailed() {
        return profileLoading != null && System.currentTimeMillis() - profileRequestedAt > 8000L;
    }

    public static void onPeople(Packets.InternalType0445 nestedValue0155) {
        peopleLoading = false;
        if (!nestedValue0155.query().equals(peopleQuery)) {
            return;
        }
        if (nestedValue0155.page() == 0) {
            people.clear();
            peopleIndex.clear();
        }
        people.addAll(nestedValue0155.people());
        for (Packets.InternalType0047 nestedValue0024 : nestedValue0155.people()) {
            peopleIndex.put(nestedValue0024.username(), nestedValue0024);
        }
        peoplePage = nestedValue0155.page();
        peopleMore = nestedValue0155.more();
    }

    public static Packets.InternalType0047 person(String string) {
        return peopleIndex.get(string);
    }

    public static void resetPeople() {
        people.clear();
        peopleIndex.clear();
        peoplePage = 0;
        peopleMore = false;
        peopleLoading = false;
        peopleQuery = "";
    }

    public static void requestProfile(String string) {
        profile = null;
        profileLoading = string;
        profileRequestedAt = System.currentTimeMillis();
        RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0083(string));
    }

    public static void closeProfile() {
        profile = null;
        profileLoading = null;
    }

    public static Packets.InternalType0306 peer(String string) {
        return string == null ? null : peers.get(string);
    }

    public static void cachePeer(Packets.InternalType0306 nestedValue0112) {
        peers.put(nestedValue0112.username(), nestedValue0112);
    }

    public static void refreshPeer(String string) {
        if (string == null || string.isBlank() || string.equals(GLOBAL_CHAT)) {
            return;
        }
        long l = System.currentTimeMillis();
        Long l2 = peersAsked.get(string);
        if (l2 != null && l - l2 < 30000L) {
            return;
        }
        if (l - peerAskedAt < 1500L) {
            return;
        }
        peersAsked.put(string, l);
        peerAskedAt = l;
        RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0083(string));
    }

    public static boolean muted() {
        if (muteUntil == 0L) {
            return false;
        }
        if (muteUntil < 0L) {
            return true;
        }
        if (System.currentTimeMillis() < muteUntil) {
            return true;
        }
        muteUntil = 0L;
        return false;
    }

    @Generated
    public static void setPreferUser(GlobalsUser globalsUser) {
        preferUser = globalsUser;
    }

    @Generated
    public static void setUser(GlobalsUser globalsUser) {
        user = globalsUser;
    }

    @Generated
    public static GlobalsUser getPreferUser() {
        return preferUser;
    }

    @Generated
    public static GlobalsUser getUser() {
        return user;
    }

    @Generated
    public static void setResult(String string) {
        result = string;
    }

    @Generated
    public static String getResult() {
        return result;
    }

    @Generated
    public static void setRequests(List<String> list) {
        requests = list;
    }

    @Generated
    public static List<String> getRequests() {
        return requests;
    }

    @Generated
    public static List<Packets.InternalType0018> getFriends() {
        return friends;
    }

    @Generated
    public static int getFriendsVersion() {
        return friendsVersion;
    }

    @Generated
    public static List<Packets.InternalType0018> getVisiblePlayers() {
        return visiblePlayers;
    }

    @Generated
    public static void setSelf(Packets.InternalType0421 nestedValue0153) {
        self = nestedValue0153;
    }

    @Generated
    public static Packets.InternalType0421 getSelf() {
        return self;
    }

    @Generated
    public static List<Packets.InternalType0047> getPeople() {
        return people;
    }

    @Generated
    public static void setPeopleQuery(String string) {
        peopleQuery = string;
    }

    @Generated
    public static String getPeopleQuery() {
        return peopleQuery;
    }

    @Generated
    public static int getPeoplePage() {
        return peoplePage;
    }

    @Generated
    public static boolean isPeopleMore() {
        return peopleMore;
    }

    @Generated
    public static boolean isPeopleLoading() {
        return peopleLoading;
    }

    @Generated
    public static void setProfile(Packets.InternalType0306 nestedValue0112) {
        profile = nestedValue0112;
    }

    @Generated
    public static Packets.InternalType0306 getProfile() {
        return profile;
    }

    @Generated
    public static void setProfileLoading(String string) {
        profileLoading = string;
    }

    @Generated
    public static String getProfileLoading() {
        return profileLoading;
    }

    @Generated
    public static void setMuteUntil(long l) {
        muteUntil = l;
    }

    @Generated
    public static long getMuteUntil() {
        return muteUntil;
    }

    @Generated
    public static void setMuteReason(String string) {
        muteReason = string;
    }

    @Generated
    public static String getMuteReason() {
        return muteReason;
    }

    static {
        chatsLock = new Object();
        chats = new LinkedHashMap<String, Chat>();
        requests = new ArrayList<String>();
        friends = new ArrayList<Packets.InternalType0018>();
        friendIndex = Map.of();
        visiblePlayers = new ArrayList<Packets.InternalType0018>();
        people = new ArrayList<Packets.InternalType0047>();
        peopleQuery = "";
        peopleIndex = new HashMap<String, Packets.InternalType0047>();
        peers = new ConcurrentHashMap<String, Packets.InternalType0306>();
        peersAsked = new ConcurrentHashMap<String, Long>();
        muteReason = "";
    }
}
