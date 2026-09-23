package globals.client.ui;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.internal.script.*;
import globals.client.GlobalsUser;
import globals.client.Information;
import globals.client.ui.RocknetMenu;
import globals.shared.proto.Packets;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.Rect;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.util.Stopwatch;

public class AuthForm
extends Rect {
    private boolean login;
    private final CustomTextField loginField = new CustomTextField(Fonts.internalField1154.internalMethod01432(8.0f));
    private final CustomTextField emailField;
    private final CustomTextField passwordField;
    public static final Stopwatch REGISTER_TIMER = new Stopwatch();

    public AuthForm() {
        this.loginField.internalMethod09000(LanguageManager.internalMethod07214("rocknet.auth.login_placeholder"));
        this.emailField = new CustomTextField(Fonts.internalField1154.internalMethod01432(8.0f));
        this.emailField.internalMethod09000(LanguageManager.internalMethod07214("rocknet.auth.email_placeholder"));
        this.passwordField = new CustomTextField(Fonts.internalField1154.internalMethod01432(8.0f)).internalMethod01541();
        this.passwordField.internalMethod09000(LanguageManager.internalMethod07214("rocknet.auth.password_placeholder"));
    }

    public void render(UiRenderContext iII) {
        String string;
        iII.drawRoundedRect(this.x, this.y, this.width, this.height, CornerRadii.internalMethod03908(10.0f), RocknetMenu.card());
        float f = 10.0f;
        boolean bl = this.login;
        iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(10.0f), LanguageManager.internalMethod07214(bl ? "rocknet.auth.login_title" : "rocknet.auth.register_title"), this.x + this.width / 2.0f, this.y + f, RocknetMenu.text());
        f += 14.0f;
        this.loginField.internalMethod07506(16);
        f = this.field(iII, this.loginField, f);
        if (bl) {
            this.emailField.internalMethod05191(0.0f, 0.0f, 0.0f, 0.0f);
        } else {
            this.emailField.internalMethod07506(100);
            f = this.field(iII, this.emailField, f);
        }
        this.passwordField.internalMethod07506(100);
        f = this.field(iII, this.passwordField, f);
        boolean bl2 = UiUtils.internalMethod06450(this.x + 10.0f, this.y + f, this.width - 20.0f, 18.0, iII);
        iII.drawRoundedRect(this.x + 10.0f, this.y + f, this.width - 20.0f, 18.0f, CornerRadii.internalMethod03908(6.0f), RocknetMenu.accent().mix(ColorRGBA.WHITE, bl2 ? 0.12f : 0.0f));
        iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(8.0f), LanguageManager.internalMethod07214(bl ? "rocknet.auth.login_button" : "rocknet.auth.register_button"), this.x + this.width / 2.0f, this.y + f + 6.0f, RocknetMenu.onAccent());
        if (bl2) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        boolean bl3 = UiUtils.internalMethod06450(this.x + 10.0f, this.y + (f += 24.0f), this.width - 20.0f, 18.0, iII);
        String string2 = LanguageManager.internalMethod07214("rocknet.auth.have_account") + " ";
        String string3 = LanguageManager.internalMethod07214(bl ? "rocknet.auth.register" : "rocknet.auth.login_action");
        float f2 = Fonts.internalField1154.internalMethod01432(8.0f).internalMethod00965(string2 + string3);
        float f3 = Fonts.internalField1154.internalMethod01432(8.0f).internalMethod00965(string2);
        iII.drawText(Fonts.internalField1154.internalMethod01432(8.0f), string2, this.x + this.width / 2.0f - f2 / 2.0f, this.y + f, RocknetMenu.second());
        iII.drawText(Fonts.internalField1154.internalMethod01432(8.0f), string3, this.x + this.width / 2.0f - f2 / 2.0f + f3, this.y + f, RocknetMenu.accent().mulAlpha(bl3 ? 0.7f : 1.0f));
        if (bl3) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        if ((string = Information.getResult()) == null || string.isBlank()) {
            string = LanguageManager.internalMethod07214("rocknet.auth.independent_account");
        }
        List<String> list = RocknetMenu.wrapText(string, 150.0f);
        float f4 = 0.0f;
        for (String string4 : list) {
            iII.drawCenteredText(Fonts.internalField1154.internalMethod01432(8.0f), string4, this.x + this.width / 2.0f, this.y + this.height + 8.0f + f4, RocknetMenu.second());
            f4 += Fonts.internalField1154.internalMethod01432(8.0f).internalMethod04890() + 2.0f;
        }
    }

    private float field(UiRenderContext iII, CustomTextField typedValue194, float f) {
        iII.drawRoundedRect(this.x + 10.0f, this.y + f, this.width - 20.0f, 18.0f, CornerRadii.internalMethod03908(6.0f), RocknetMenu.inset());
        typedValue194.internalMethod05191(this.x + 10.0f, this.y + f, this.width - 20.0f, 18.0f);
        typedValue194.internalMethod00143(RocknetMenu.text());
        typedValue194.internalMethod03398(iII);
        return f + 23.0f;
    }

    public void onMouseClicked(double d, double d2, MouseButton typedParameter016) {
        if (typedParameter016 != MouseButton.internalField0990) {
            this.loginField.internalMethod01643(d, d2, typedParameter016);
            this.emailField.internalMethod01643(d, d2, typedParameter016);
            this.passwordField.internalMethod01643(d, d2, typedParameter016);
        }
        if (this.login) {
            boolean bl;
            boolean bl2 = UiUtils.internalMethod05785(this.x + 10.0f, this.y + 70.0f, this.width - 20.0f, 18.0, d, d2);
            String string = this.loginField.internalMethod06202().trim();
            String string2 = this.passwordField.internalMethod06202().trim();
            if (bl2 && !string.isBlank() && !string2.isBlank()) {
                RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0235(string, string2, "Rockstar".toLowerCase()));
                Information.setPreferUser(new GlobalsUser(string, string2, "Rockstar".toLowerCase()));
            }
            if (bl = UiUtils.internalMethod05785(this.x + 10.0f, this.y + 94.0f, this.width - 20.0f, 18.0, d, d2)) {
                this.login = false;
            }
        } else {
            boolean bl;
            boolean bl3 = UiUtils.internalMethod05785(this.x + 10.0f, this.y + 93.0f, this.width - 20.0f, 18.0, d, d2);
            String string = this.loginField.internalMethod06202().trim();
            String string3 = this.emailField.internalMethod06202().trim();
            String string4 = this.passwordField.internalMethod06202().trim();
            if (bl3 && !string.isBlank() && !string3.isBlank() && !string4.isBlank()) {
                RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0150(string, string3, string4, "Rockstar".toLowerCase()));
                Information.setPreferUser(new GlobalsUser(string, string4, "Rockstar".toLowerCase()));
                REGISTER_TIMER.internalMethod00701();
            }
            if (bl = UiUtils.internalMethod05785(this.x + 10.0f, this.y + 117.0f, this.width - 20.0f, 18.0, d, d2)) {
                this.login = true;
            }
        }
    }

    public void onMouseReleased(double d, double d2, MouseButton typedParameter016) {
        if (this.loginField.internalMethod00342()) {
            this.loginField.internalMethod02863(d, d2, typedParameter016);
        }
        if (this.emailField.internalMethod00342()) {
            this.emailField.internalMethod02863(d, d2, typedParameter016);
        }
        if (this.passwordField.internalMethod00342()) {
            this.passwordField.internalMethod02863(d, d2, typedParameter016);
        }
    }

    public void keyPressed(int n, int n2, int n3) {
        this.loginField.internalMethod05727(n, n2, n3);
        this.emailField.internalMethod05727(n, n2, n3);
        this.passwordField.internalMethod05727(n, n2, n3);
    }

    public void charTyped(char c, int n) {
        if (this.loginField.internalMethod00342()) {
            this.loginField.internalMethod05413(c, n);
        }
        if (this.emailField.internalMethod00342()) {
            this.emailField.internalMethod05413(c, n);
        }
        if (this.passwordField.internalMethod00342()) {
            this.passwordField.internalMethod05413(c, n);
        }
    }

    @Generated
    public boolean isLogin() {
        return this.login;
    }
}

