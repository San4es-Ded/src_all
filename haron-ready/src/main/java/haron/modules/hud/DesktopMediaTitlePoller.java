package haron.modules.hud;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DesktopMediaTitlePoller {
    private static final Logger LOG = LogManager.getLogger((String)"haron/lyrics");
    private static final DesktopMediaTitlePoller INSTANCE = new DesktopMediaTitlePoller();
    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Haron-LyricsPoller");
        thread.setDaemon(true);
        return thread;
    });
    private final AtomicReference<String> currentTitle = new AtomicReference<String>("");
    private final AtomicReference<String> currentArtist = new AtomicReference<String>("");
    private volatile long lastPollTime = 0L;
    private static final long POLL_INTERVAL_MS = 5000L;
    private volatile File winPs1 = null;
    private int debugCounter = 0;
    private volatile boolean polling = false;

    public String getTitle() {
        this.pollIfNeeded();
        return this.currentTitle.get();
    }

    public String getArtist() {
        this.pollIfNeeded();
        return this.currentArtist.get();
    }

    private void pollIfNeeded() {
        long l = System.currentTimeMillis();
        if (l - this.lastPollTime < 5000L) {
            return;
        }
        if (this.polling) {
            return;
        }
        this.lastPollTime = l;
        this.ensureFiles();
        if (this.winPs1 == null) {
            return;
        }
        this.polling = true;
        this.executor.submit(() -> {
            try {
                String string = "";
                String string2 = "";
                String string3 = this.runPs(this.winPs1, "win_out.txt", 8);
                if (this.debugCounter % 10 == 0) {
                    LOG.info("[Lyrics] PS output: '{}'", (Object)string3.replace("\n", " | "));
                }
                for (String string4 : string3.split("\n")) {
                    String string5 = string4.trim();
                    if (string5.isEmpty()) continue;
                    String string6 = this.parseWindowTitle(string5);
                    if (!string6.isEmpty()) {
                        string = string6;
                        break;
                    }
                    string6 = this.parseSpotifyTitle(string5);
                    if (string6.isEmpty()) continue;
                    String[] stringArray = string6.split(" - ", 2);
                    string = stringArray.length > 1 ? stringArray[1].trim() : stringArray[0].trim();
                    string2 = stringArray.length > 1 ? stringArray[0].trim() : "";
                    break;
                }
                ++this.debugCounter;
                if (!string.isEmpty()) {
                    this.currentTitle.set(string);
                    this.currentArtist.set(string2);
                    if (this.debugCounter % 10 == 0) {
                        LOG.info("[Lyrics] Final: title='{}' artist='{}'", (Object)string, (Object)string2);
                    }
                } else if (this.debugCounter % 10 == 0) {
                    LOG.info("[Lyrics] No media detected");
                }
            }
            catch (Exception exception) {
                LOG.error("[Lyrics] Poll error", (Throwable)exception);
            }
            finally {
                this.polling = false;
            }
        });
    }

    private void ensureFiles() {
        File file = new File(this.getGameDir(), "haron_temp");
        if (!file.exists()) {
            file.mkdirs();
        }
        if (this.winPs1 == null || !this.winPs1.exists()) {
            try {
                this.winPs1 = new File(file, "win.ps1");
                this.winPs1.deleteOnExit();
                String string = "$titles = @()\nGet-Process -Name spotify,chrome,firefox,msedge,opera,brave,yandexbrowser -ErrorAction SilentlyContinue |\n  Where-Object { $_.MainWindowTitle.Length -gt 0 } |\n  ForEach-Object { $titles += $_.MainWindowTitle }\n$r = $titles -join [char]10\n[IO.File]::WriteAllText((Join-Path (Split-Path $MyInvocation.MyCommand.Path) 'win_out.txt'), $r, [Text.Encoding]::UTF8)\n";
                FileOutputStream fileOutputStream = new FileOutputStream(this.winPs1);
                fileOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.close();
            }
            catch (Exception exception) {
                LOG.error("[Lyrics] Failed to create Win PS1", (Throwable)exception);
                this.winPs1 = null;
            }
        }
    }

    private String parseSpotifyTitle(String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }
        if (string.contains(" - ")) {
            return string.trim();
        }
        return "";
    }

    public String getTrackKey() {
        String string = this.getTitle();
        String string2 = this.getArtist();
        if (string.isEmpty()) {
            return "";
        }
        return string2.isEmpty() ? string : DesktopMediaTitlePoller.$sf$0(string2, string);
    }

    private String runPs(File file, String string, int n) {
        try {
            File file2 = new File(file.getParent(), string);
            file2.delete();
            ProcessBuilder processBuilder = new ProcessBuilder("powershell", "-NoProfile", "-NonInteractive", "-ExecutionPolicy", "Bypass", "-File", file.getAbsolutePath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            boolean bl = process.waitFor(n, TimeUnit.SECONDS);
            if (!bl) {
                process.destroyForcibly();
            }
            if (file2.exists()) {
                byte[] byArray = Files.readAllBytes(file2.toPath());
                String string2 = new String(byArray, StandardCharsets.UTF_8).trim();
                file2.delete();
                return string2;
            }
        }
        catch (Exception exception) {
            LOG.error("[Lyrics] PS error: {}", (Object)file.getName(), (Object)exception);
        }
        return "";
    }

    private String parseWindowTitle(String string) {
        String string2;
        int n;
        String string3;
        if (string == null || string.isEmpty()) {
            return "";
        }
        String string4 = string.replaceAll(" - (Google Chrome|Yandex Browser|Microsoft Edge|Opera|Brave|Vivaldi)$", "");
        if (string4.endsWith(" - YouTube") && !(string3 = string4.substring(0, string4.length() - " - YouTube".length()).trim()).isEmpty()) {
            return string3;
        }
        if (string4.endsWith(" - YouTube Music") && !(string3 = string4.substring(0, string4.length() - " - YouTube Music".length()).trim()).isEmpty()) {
            return string3;
        }
        if (string4.endsWith(" - VK Music") && !(string3 = string4.substring(0, string4.length() - " - VK Music".length()).trim()).isEmpty()) {
            return string3;
        }
        if (string4.endsWith(" - Яндекс Музыка") && (n = string4.lastIndexOf(" - ")) > 0 && !(string2 = string4.substring(0, n).trim()).isEmpty()) {
            return string2;
        }
        return "";
    }

    private DesktopMediaTitlePoller() {
    }

    public void clear() {
        this.currentTitle.set("");
        this.currentArtist.set("");
        this.lastPollTime = 0L;
    }

    public static DesktopMediaTitlePoller getInstance() {
        return INSTANCE;
    }

    private static /* synthetic */ String $sf$0(String string, String string2) {
        return string + " - " + string2;
    }

    private File getGameDir() {
        try {
            return MinecraftClient.getInstance().runDirectory;
        }
        catch (Exception exception) {
            return new File(System.getProperty("user.dir"));
        }
    }
}

