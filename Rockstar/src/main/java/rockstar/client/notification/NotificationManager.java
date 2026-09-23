package rockstar.client.notification;



import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import globals.shared.proto.Packets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import pyrock.events.client.NotificationEvent;
import pyrock.events.render.HudRenderEvent;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.core.ClientSoundEvents;
import rockstar.client.notification.NotificationView;
import rockstar.client.notification.NotificationType;
import rockstar.client.notification.DetailedNotification;
import rockstar.client.notification.SilentNotification;
import rockstar.client.notification.RocknetNotification;
import rockstar.client.notification.IconNotification;

public class NotificationManager {
    private final List<NotificationView> internalField0416 = new CopyOnWriteArrayList<NotificationView>();
    private final EventListener<HudRenderEvent> internalField0157 = hudRenderEvent -> {
        float f = 0.0f;
        float f2 = 0.0f;
        for (NotificationView typedValue047 : this.internalField0416) {
            typedValue047.internalMethod06600();
            if (typedValue047 instanceof SilentNotification) {
                typedValue047.internalMethod04213(hudRenderEvent.getContext(), 0.0f);
                continue;
            }
            if (typedValue047 instanceof RocknetNotification) {
                typedValue047.internalMethod04213(hudRenderEvent.getContext(), f);
                if (!(typedValue047.internalField0808.internalMethod02881() >= 0.5f)) continue;
                f += 30.0f;
                continue;
            }
            if (!(typedValue047 instanceof IconNotification) && !(typedValue047 instanceof DetailedNotification)) continue;
            typedValue047.internalMethod04213(hudRenderEvent.getContext(), f2);
            if (!(typedValue047.internalField0808.internalMethod02881() >= 0.5f)) continue;
            f2 += typedValue047.internalMethod06598();
        }
        this.internalField0416.removeIf(NotificationView::internalMethod06601);
    };

    public NotificationManager() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public void internalMethod02784(NotificationView typedValue047) {
        this.internalMethod07490(() -> {
            this.internalField0416.add(typedValue047);
            this.internalMethod04165(typedValue047);
        });
    }

    public void internalMethod04075(NotificationType typedValue048, String string) {
        this.internalMethod02784(new SilentNotification(typedValue048, string));
    }

    public void internalMethod00599(NotificationType typedValue048, String string, String string2) {
        this.internalMethod02784(new DetailedNotification(typedValue048, string, string2));
    }

    public void internalMethod00433(Packets.InternalType0451 nestedValue2055, String string) {
        this.internalMethod07490(() -> {
            if (MinecraftClientAccess.internalField0149.player == null) {
                return;
            }
            this.internalMethod02784(new RocknetNotification(nestedValue2055, string));
            ClientSoundEvents.internalField1017.internalMethod03864(1.0f);
        });
    }

    public void internalMethod06070(Packets.InternalType0451 nestedValue2055, String string) {
        this.internalMethod07490(() -> {
            if (MinecraftClientAccess.internalField0149.player == null) {
                return;
            }
            this.internalMethod02784(new RocknetNotification(nestedValue2055, string, true));
            ClientSoundEvents.internalField1017.internalMethod03864(1.0f);
        });
    }

    private void internalMethod07490(Runnable runnable) {
        if (MinecraftClientAccess.internalField0149.isOnThread()) {
            runnable.run();
            return;
        }
        MinecraftClientAccess.internalField0149.execute(runnable);
    }

    private void internalMethod04165(NotificationView typedValue047) {
        String string;
        String string2 = "info";
        String string3 = "";
        String string4 = "";
        if (typedValue047 instanceof SilentNotification) {
            SilentNotification typedValue050 = (SilentNotification)typedValue047;
            string = "island";
            string2 = typedValue050.internalMethod05376().internalMethod07259();
            string4 = typedValue050.internalMethod03176();
        } else if (typedValue047 instanceof DetailedNotification) {
            DetailedNotification typedValue049 = (DetailedNotification)typedValue047;
            string = "crosshair";
            string2 = typedValue049.internalMethod01027().internalMethod07259();
            string3 = typedValue049.internalMethod01429();
            string4 = typedValue049.internalMethod06047();
        } else if (typedValue047 instanceof RocknetNotification) {
            RocknetNotification typedValue051 = (RocknetNotification)typedValue047;
            string = "irc";
            string3 = typedValue051.internalMethod05514();
            string4 = typedValue051.internalMethod02017();
        } else if (typedValue047 instanceof IconNotification) {
            IconNotification typedValue053 = (IconNotification)typedValue047;
            string = "mini";
            string4 = typedValue053.internalMethod07467();
            string3 = typedValue053.internalMethod04018() == null ? "" : typedValue053.internalMethod04018();
        } else {
            string = "other";
        }
        try {
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new NotificationEvent(string, string2, string3 == null ? "" : string3, string4 == null ? "" : string4));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Generated
    public List<NotificationView> internalMethod02336() {
        return this.internalField0416;
    }

    @Generated
    public EventListener<HudRenderEvent> internalMethod00410() {
        return this.internalField0157;
    }
}

