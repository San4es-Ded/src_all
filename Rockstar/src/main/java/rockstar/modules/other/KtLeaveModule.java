package rockstar.modules.other;




import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;

@ModuleInfo(name="KT Leave", category=ModuleCategory.OTHER)
public class KtLeaveModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private KeybindSetting internalField0648;
    private ServerSocket internalField0946;
    private ExecutorService internalField0124 = Executors.newSingleThreadExecutor();
    boolean internalField0277;
    private final EventListener<WorldChangeEvent> internalField0157 = worldChangeEvent -> {
        if (this.internalField0237.isSelected()) {
            this.toggle();
        }
    };
    private final EventListener<KeyPressEvent> internalField0158 = keyPressEvent -> this.internalMethod06750(keyPressEvent.getKey(), keyPressEvent.getAction());
    private final EventListener<MouseEvent> internalField1028 = mouseEvent -> this.internalMethod06750(mouseEvent.getButton(), mouseEvent.getAction());

    public KtLeaveModule() {
        this.internalMethod09830();
    }

    private void internalMethod09830() {
        this.internalField0668 = new ModeSetting(this, "\u0420\u0435\u0436\u0438\u043c");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "HW Classic");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "\u041e\u0441\u043d\u043e\u0432\u043d\u043e\u0439");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "\u0414\u043e\u043f\u043e\u043b\u043d\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u0439");
        this.internalField0648 = new KeybindSetting(this, "\u041a\u043d\u043e\u043f\u043a\u0430 \u043b\u0438\u0432\u0430", () -> this.internalField1066.isSelected() || this.internalField0237.isSelected());
    }

    @Override
    public void internalMethod08229() {
        if (this.internalField0237.isSelected()) {
            if (KtLeaveModule.internalField0149.player == null || KtLeaveModule.internalField0149.world == null || KtLeaveModule.internalField0149.player.networkHandler == null) {
                return;
            }
            for (int i = 0; i < 41; ++i) {
                KtLeaveModule.internalField0149.player.setSneaking(true);
                Vec3d vec3d = KtLeaveModule.internalField0149.player.getEntityPos().add((double)i, 0.0, (double)i);
                KtLeaveModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(vec3d.x, vec3d.y, vec3d.z, Math.random() > 0.5, KtLeaveModule.internalField0149.player.horizontalCollision));
                KtLeaveModule.internalField0149.player.networkHandler.sendPacket((Packet)new KeepAliveC2SPacket((long)((int)(Math.random() * 8.0))));
            }
        }
        if (!this.internalField1066.isSelected()) {
            return;
        }
        if (this.internalField0277) {
            this.internalMethod09829();
            this.internalField0277 = false;
        }
        this.internalField0124.submit(() -> {
            try {
                Socket socket = this.internalField0946.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String string = bufferedReader.readLine();
                if (string != null) {
                    this.internalField0277 = true;
                }
                socket.close();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        });
    }

    private void internalMethod06750(int n, int n2) {
        if (KtLeaveModule.internalField0149.currentScreen == null && n2 == 1 && this.internalField0238.isSelected() && this.internalField0648.internalMethod02165(n)) {
            try {
                Socket socket = new Socket("localhost", 1524);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println("SIGNAL");
                socket.close();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    public void internalMethod09829() {
        if (KtLeaveModule.internalField0149.player == null || KtLeaveModule.internalField0149.world == null) {
            return;
        }
        internalField0149.doItemUse();
    }

    @Override
    public void onDisable() {
        if (this.internalField1066.isSelected() && this.internalField0946 != null) {
            try {
                this.internalField0946.close();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {
        if (this.internalField1066.isSelected()) {
            try {
                this.internalField0946 = new ServerSocket(1524);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }
}
