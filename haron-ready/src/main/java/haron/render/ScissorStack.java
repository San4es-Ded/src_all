package haron.render;

import haron.render.ScissorRect;
import haron.render.ShapeRenderer;
import java.util.Stack;
import net.minecraft.client.util.math.MatrixStack;

public class ScissorStack {
    private final ShapeRenderer a;
    private final Stack<ScissorRect> b = new Stack();

    public ScissorStack(ShapeRenderer s7swsm2) {
        this.a = s7swsm2;
    }

    public void a(MatrixStack matrixStack) {
        if (this.b.isEmpty()) {
            return;
        }
        this.b.pop();
        this.a.a(matrixStack);
        if (this.b.isEmpty()) {
            return;
        }
        ScissorRect ix7z3q2 = this.b.peek();
        this.a.a(ix7z3q2.a(), ix7z3q2.b(), ix7z3q2.c(), ix7z3q2.d(), ix7z3q2.e(), matrixStack);
    }

    public void a(float f, float f2, float f3, float f4, float f5, MatrixStack matrixStack) {
        if (this.b.isEmpty()) {
            this.b.push(new ScissorRect(f, f2, f3, f4, f5));
            this.a.a(f, f2, f3, f4, f5, matrixStack);
            return;
        }
        ScissorRect ix7z3q2 = this.b.peek();
        float f6 = Math.max(ix7z3q2.a(), f);
        float f7 = Math.max(ix7z3q2.b(), f2);
        float f8 = Math.min(ix7z3q2.a() + ix7z3q2.c(), f + f3) - f6;
        float f9 = Math.min(ix7z3q2.b() + ix7z3q2.d(), f2 + f4) - f7;
        if (f8 <= 0.0f || f9 <= 0.0f) {
            this.b.push(new ScissorRect(ix7z3q2.a(), ix7z3q2.b(), 0.0f, 0.0f, f5));
            return;
        }
        this.a.a(matrixStack);
        this.a.a(f6, f7, f8, f9, f5, matrixStack);
        this.b.push(new ScissorRect(f6, f7, f8, f9, f5));
    }

    public void a(float f, float f2, float f3, float f4, MatrixStack matrixStack) {
        this.a(f, f2, f3, f4, 0.0f, matrixStack);
    }
}

