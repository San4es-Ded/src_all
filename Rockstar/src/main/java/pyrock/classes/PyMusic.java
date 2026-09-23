package pyrock.classes;


import rockstar.client.internal.script.*;
import dev.redstones.mediaplayerinfo.IMediaSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.RockstarClient;
import rockstar.client.internal.script.MediaSessionProvider;
import rockstar.client.internal.script.LyricsTrack;

public class PyMusic {
    public boolean active() {
        return PyMusic.snapshot() != null;
    }

    public boolean playing() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        return nestedValue0115 != null && nestedValue0115.internalMethod00392();
    }

    public double position() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        return nestedValue0115 == null ? 0.0 : nestedValue0115.internalMethod00389();
    }

    public long positionMs() {
        return Math.round(this.position() * 1000.0);
    }

    public long duration() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        return nestedValue0115 == null ? 0L : nestedValue0115.internalMethod00391();
    }

    public long durationMs() {
        return this.duration() * 1000L;
    }

    public double progress() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        if (nestedValue0115 == null || nestedValue0115.internalMethod00391() <= 0L) {
            return 0.0;
        }
        return Math.clamp(nestedValue0115.internalMethod00389() / (double)nestedValue0115.internalMethod00391(), 0.0, 1.0);
    }

    public float bpm() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        return nestedValue0115 == null ? 0.0f : nestedValue0115.internalMethod00390();
    }

    public boolean lyricsSynced() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        return nestedValue0115 != null && nestedValue0115.internalMethod01450().internalMethod00823();
    }

    @Nullable
    public ColorRGBA color() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        return nestedValue0115 == null ? null : nestedValue0115.internalMethod02127();
    }

    @Nullable
    public Identifier artwork() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        return nestedValue0115 == null ? null : nestedValue0115.internalMethod03282();
    }

    @Nullable
    public Map<String, Object> current() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        return nestedValue0115 == null ? null : PyMusic.track(nestedValue0115);
    }

    public List<Map<String, Object>> lyrics() {
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        if (nestedValue0115 == null) {
            return List.of();
        }
        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
        for (LyricsTrack.InternalType0316 nestedValue0117 : nestedValue0115.internalMethod01450().internalMethod00129()) {
            arrayList.add(PyMusic.line(nestedValue0117));
        }
        return arrayList;
    }

    @Nullable
    public Map<String, Object> lyricAt(double d) {
        long l;
        MediaSessionProvider.InternalType0314 nestedValue0115 = PyMusic.snapshot();
        if (nestedValue0115 == null || !nestedValue0115.internalMethod01450().internalMethod00823()) {
            return null;
        }
        LyricsTrack typedValue279 = nestedValue0115.internalMethod01450();
        int n = typedValue279.internalMethod06143(l = Math.max(0L, Math.round(d)));
        if (n < 0) {
            return null;
        }
        Map<String, Object> map = PyMusic.line(typedValue279.internalMethod00129().get(n));
        long l2 = nestedValue0115.internalMethod00391() * 1000L;
        map.put("index", n);
        map.put("progress", Float.valueOf(typedValue279.internalMethod05423(n, l, l2)));
        map.put("singing_progress", Float.valueOf(typedValue279.internalMethod02234(n, l, l2)));
        map.put("active", n == typedValue279.internalMethod05414(l, l2, 1000L));
        return map;
    }

    @Nullable
    public Map<String, Object> currentLyric() {
        return this.lyricAt(this.positionMs());
    }

    public boolean play() {
        return PyMusic.control(IMediaSession::play);
    }

    public boolean pause() {
        return PyMusic.control(IMediaSession::pause);
    }

    public boolean toggle() {
        return PyMusic.control(IMediaSession::playPause);
    }

    public boolean next() {
        return PyMusic.control(IMediaSession::next);
    }

    public boolean previous() {
        return PyMusic.control(IMediaSession::previous);
    }

    public boolean stop() {
        return PyMusic.control(IMediaSession::stop);
    }

    private static Map<String, Object> track(MediaSessionProvider.InternalType0314 nestedValue0115) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        linkedHashMap.put("title", nestedValue0115.internalMethod01219());
        linkedHashMap.put("artist", nestedValue0115.internalMethod05846());
        linkedHashMap.put("owner", nestedValue0115.internalMethod08078());
        linkedHashMap.put("playing", nestedValue0115.internalMethod00392());
        linkedHashMap.put("position", nestedValue0115.internalMethod00389());
        linkedHashMap.put("position_ms", Math.round(nestedValue0115.internalMethod00389() * 1000.0));
        linkedHashMap.put("duration", nestedValue0115.internalMethod00391());
        linkedHashMap.put("duration_ms", nestedValue0115.internalMethod00391() * 1000L);
        linkedHashMap.put("progress", nestedValue0115.internalMethod00391() <= 0L ? 0.0 : Math.clamp(nestedValue0115.internalMethod00389() / (double)nestedValue0115.internalMethod00391(), 0.0, 1.0));
        linkedHashMap.put("bpm", Float.valueOf(nestedValue0115.internalMethod00390()));
        linkedHashMap.put("color", nestedValue0115.internalMethod02127());
        linkedHashMap.put("artwork", nestedValue0115.internalMethod03282());
        linkedHashMap.put("lyrics_synced", nestedValue0115.internalMethod01450().internalMethod00823());
        linkedHashMap.put("has_lyrics", !nestedValue0115.internalMethod01450().internalMethod00819());
        return linkedHashMap;
    }

    private static Map<String, Object> line(LyricsTrack.InternalType0316 nestedValue0117) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        ArrayList arrayList = new ArrayList();
        for (LyricsTrack.InternalType0315 nestedValue0116 : nestedValue0117.internalMethod00629()) {
            LinkedHashMap<String, Number> linkedHashMap2 = new LinkedHashMap<String, Number>();
            linkedHashMap2.put("time_ms", nestedValue0116.internalMethod00233());
            linkedHashMap2.put("char_index", nestedValue0116.internalMethod00232());
            arrayList.add(linkedHashMap2);
        }
        linkedHashMap.put("time_ms", nestedValue0117.internalMethod01998());
        linkedHashMap.put("text", nestedValue0117.internalMethod00756());
        linkedHashMap.put("cues", arrayList);
        return linkedHashMap;
    }

    private static boolean control(Consumer<IMediaSession> consumer) {
        IMediaSession iMediaSession;
        MediaSessionProvider typedValue277 = PyMusic.tracker();
        IMediaSession iMediaSession2 = iMediaSession = typedValue277 == null ? null : typedValue277.internalMethod04514();
        if (iMediaSession == null) {
            return false;
        }
        try {
            consumer.accept(iMediaSession);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    @Nullable
    private static MediaSessionProvider tracker() {
        return RockstarClient.getInstance().internalMethod05636();
    }

    @Nullable
    private static MediaSessionProvider.InternalType0314 snapshot() {
        MediaSessionProvider typedValue277 = PyMusic.tracker();
        return typedValue277 == null ? null : typedValue277.internalMethod03535();
    }
}

