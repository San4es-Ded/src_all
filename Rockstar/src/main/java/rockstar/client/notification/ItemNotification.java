package rockstar.client.notification;



import rockstar.client.render.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.render.UiBatchRenderer;
import rockstar.client.notification.IconNotification;

public class ItemNotification
extends IconNotification {
    private final ItemStack internalField0878;

    public ItemNotification(String string, ItemStack itemStack) {
        super(string, null, null);
        this.internalField0878 = itemStack.copy();
    }

    public ItemNotification(String string, Item item) {
        super(string, null, null);
        this.internalField0878 = item.getDefaultStack();
    }

    public ItemNotification internalMethod03390(String string) {
        this.internalField0247 = string;
        if (this.internalField0777 == null) {
            Integer n = this.internalField0878.getRarity().getFormatting().getColorValue();
            this.internalField0777 = n != null ? ColorRGBA.fromInt(n) : new ColorRGBA(255.0f, 85.0f, 85.0f);
        }
        return this;
    }

    public ItemNotification internalMethod05942(ColorRGBA colorRGBA) {
        this.internalField0777 = colorRGBA;
        return this;
    }

    @Override
    protected void internalMethod01476(CustomDrawContext customDrawContext, float f, float f2, float f3) {
        float[] fArray = (float[])RenderSystem.getShaderColor().clone();
        boolean bl = GL11.glIsEnabled((int)3042);
        UiBatchRenderer.internalMethod02576();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f3);
        customDrawContext.drawItem(this.internalField0878, f, f2, 0.625f);
        RenderSystem.setShaderColor((float)fArray[0], (float)fArray[1], (float)fArray[2], (float)fArray[3]);
        if (!bl) {
            RenderSystem.disableBlend();
        }
    }
}

