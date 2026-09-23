package wtf.wyvern.core.discord;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Small Discord IPC client that talks to the desktop client's named pipe
 * directly. This avoids the legacy native discord-rpc DLL and supports the
 * current activity payload, including buttons and external image URLs.
 */
final class DiscordIpcClient implements AutoCloseable {
    private static final int HANDSHAKE = 0;
    private static final int FRAME = 1;
    private static final int CLOSE = 2;

    private RandomAccessFile pipe;

    boolean connect(String applicationId) throws IOException {
        close();
        IOException failure = null;
        for (int index = 0; index < 10; index++) {
            try {
                pipe = new RandomAccessFile("\\\\.\\pipe\\discord-ipc-" + index, "rw");
                JsonObject hello = new JsonObject();
                hello.addProperty("v", 1);
                hello.addProperty("client_id", applicationId);
                write(HANDSHAKE, hello.toString());
                read(); // Discord READY response
                return true;
            } catch (IOException exception) {
                failure = exception;
                close();
            }
        }
        throw failure == null ? new IOException("Discord IPC pipe was not found") : failure;
    }

    synchronized void setActivity(JsonObject activity) throws IOException {
        if (pipe == null) throw new IOException("Discord IPC is not connected");
        JsonObject args = new JsonObject();
        args.addProperty("pid", ProcessHandle.current().pid());
        args.add("activity", activity);

        JsonObject payload = new JsonObject();
        payload.addProperty("cmd", "SET_ACTIVITY");
        payload.add("args", args);
        payload.addProperty("nonce", UUID.randomUUID().toString());
        write(FRAME, payload.toString());
    }

    private void write(int opcode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        pipe.writeInt(Integer.reverseBytes(opcode));
        pipe.writeInt(Integer.reverseBytes(bytes.length));
        pipe.write(bytes);
        pipe.getFD().sync();
    }

    private JsonObject read() throws IOException {
        int opcode = Integer.reverseBytes(pipe.readInt());
        int length = Integer.reverseBytes(pipe.readInt());
        if (length < 0 || length > 1024 * 1024 || opcode == CLOSE) throw new IOException("Discord IPC closed the connection");
        byte[] bytes = new byte[length];
        pipe.readFully(bytes);
        return JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    @Override
    public synchronized void close() {
        if (pipe == null) return;
        try {
            pipe.close();
        } catch (IOException ignored) {
        } finally {
            pipe = null;
        }
    }
}
