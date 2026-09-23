package rockstar.client.internal.core;




import rockstar.client.util.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.util.Identifier;
import rockstar.client.internal.core.AnimationAtlas;
import rockstar.client.internal.config.GifAnimationData;
import rockstar.client.internal.core.TextureRegion;
import rockstar.client.util.Stopwatch;

public class SpriteAnimationPlayer {
    private final AnimationAtlas.InternalType0109 internalField0536;
    private final Stopwatch internalField0519;
    private int internalField0227 = 0;
    private boolean internalField0277 = true;
    private boolean internalField0276 = false;
    private boolean internalField1099 = false;

    public SpriteAnimationPlayer(Identifier identifier) {
        this.internalField0536 = AnimationAtlas.internalMethod07453(identifier);
        if (this.internalField0536 == null) {
            throw new RuntimeException("\u0410\u043d\u0438\u043c\u0430\u0446\u0438\u044f \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430 \u0432 \u0433\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u043e\u043c \u0430\u0442\u043b\u0430\u0441\u0435: " + String.valueOf(identifier));
        }
        this.internalField0519 = new Stopwatch();
    }

    public void internalMethod03142() {
        this.internalField0227 = 0;
        this.internalField0277 = true;
        this.internalField0276 = false;
        this.internalField1099 = true;
        this.internalField0519.internalMethod00701();
    }

    public TextureRegion internalMethod00560() {
        if (!this.internalField0277 || this.internalField0276) {
            return this.internalField0536.internalMethod00899(this.internalField0227);
        }
        this.internalMethod03146();
        return this.internalField0536.internalMethod00899(this.internalField0227);
    }

    public void internalMethod03146() {
        if (!this.internalField0277 || this.internalField0276) {
            return;
        }
        long l = this.internalField0536.internalField0943.internalMethod01507();
        if (this.internalField0519.internalMethod02365(l)) {
            this.internalMethod09080();
            this.internalField0519.internalMethod00701();
        }
    }

    private void internalMethod09080() {
        ++this.internalField0227;
        if (this.internalField0227 >= this.internalField0536.internalField0228) {
            if (this.internalField1099) {
                this.internalField0227 = this.internalField0536.internalField0228 - 1;
                this.internalField0276 = true;
                this.internalField1099 = false;
            } else if (this.internalField0536.internalField0943.internalMethod01508()) {
                this.internalField0227 = 0;
            } else {
                this.internalField0227 = this.internalField0536.internalField0228 - 1;
                this.internalField0276 = true;
            }
        }
    }

    public void internalMethod09060() {
        this.internalField0277 = true;
        this.internalField0276 = false;
        this.internalField1099 = false;
    }

    public void internalMethod09061() {
        this.internalField0277 = false;
    }

    public void internalMethod09078() {
        this.internalField0277 = false;
        this.internalField0227 = 0;
        this.internalField0276 = false;
        this.internalField1099 = false;
        this.internalField0519.internalMethod00701();
    }

    public void internalMethod07104(int n) {
        if (n >= 0 && n < this.internalField0536.internalField0228) {
            this.internalField0227 = n;
            this.internalField0276 = false;
        }
    }

    public boolean internalMethod03143() {
        return this.internalField0276;
    }

    public GifAnimationData internalMethod00559() {
        return this.internalField0536.internalField0943;
    }

    public Identifier internalMethod06010() {
        return this.internalField0536.internalField0355;
    }

    @Generated
    public int internalMethod03141() {
        return this.internalField0227;
    }

    @Generated
    public boolean internalMethod03147() {
        return this.internalField0277;
    }
}

