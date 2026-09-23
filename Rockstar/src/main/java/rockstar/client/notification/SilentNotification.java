package rockstar.client.notification;


import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.notification.NotificationView;
import rockstar.client.notification.NotificationType;

public class SilentNotification
extends NotificationView {
    private final NotificationType internalField0704;
    private final String internalField0248;

    public SilentNotification(NotificationType typedValue048, String string) {
        super(1000L);
        this.internalField0704 = typedValue048;
        this.internalField0248 = string;
    }

    @Override
    public void internalMethod04213(CustomDrawContext customDrawContext, float f) {
    }

    @Generated
    public NotificationType internalMethod05376() {
        return this.internalField0704;
    }

    @Generated
    public String internalMethod03176() {
        return this.internalField0248;
    }
}

