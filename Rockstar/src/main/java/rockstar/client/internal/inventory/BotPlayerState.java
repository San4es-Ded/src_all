package rockstar.client.internal.inventory;


import rockstar.client.*;
import java.util.List;
import lombok.Generated;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BotPlayerState {
    private double internalField0194;
    private double internalField0193;
    private double internalField1045;
    private float internalField0205;
    private float internalField0206;
    private float internalField1048 = 20.0f;
    private float internalField1047 = 20.0f;
    private int internalField0227 = 20;
    private int internalField0228 = 0;
    private boolean internalField0277 = false;
    private boolean internalField0276 = false;
    private boolean internalField1099 = true;
    private int internalField1053 = -1;
    private ItemStack[] internalField0299 = new ItemStack[46];
    private ItemStack[] internalField0300 = new ItemStack[4];
    private ItemStack internalField0878 = ItemStack.EMPTY;
    private ItemStack internalField0879 = ItemStack.EMPTY;
    private ItemStack[] internalField1116 = new ItemStack[128];
    private int internalField1055 = -1;
    private int internalField1056;
    private String internalField0248 = "";
    private float internalField1049;
    private int internalField1054;
    private int internalField1464;
    private boolean internalField1100;
    private boolean internalField1102;
    private boolean internalField1101;
    private boolean internalField1516;
    private float internalField1046 = 0.05f;
    private float internalField1456 = 0.1f;
    private int internalField1470 = -1;
    private long internalField0229 = 0L;
    private double internalField1043;
    private double internalField1042;
    private double internalField1044;
    private float internalField1457;
    private float internalField1458;

    public BotPlayerState() {
        int n;
        for (n = 0; n < this.internalField0299.length; ++n) {
            this.internalField0299[n] = ItemStack.EMPTY;
        }
        for (n = 0; n < this.internalField0300.length; ++n) {
            this.internalField0300[n] = ItemStack.EMPTY;
        }
        this.internalMethod08593();
    }

    public Vec3d internalMethod06297() {
        return new Vec3d(this.internalField0194, this.internalField0193, this.internalField1045);
    }

    public void internalMethod07202(double d, double d2, double d3) {
        this.internalField0194 = d;
        this.internalField0193 = d2;
        this.internalField1045 = d3;
        this.internalField0229 = System.currentTimeMillis();
    }

    public void internalMethod06850(float f, float f2) {
        this.internalField0205 = MathHelper.wrapDegrees((float)f);
        this.internalField0206 = MathHelper.clamp((float)f2, (float)-90.0f, (float)90.0f);
    }

    public ItemStack internalMethod07411() {
        if (this.internalField0228 >= 0 && this.internalField0228 < 9 && this.internalField0228 < this.internalField0299.length) {
            return this.internalField0299[this.internalField0228];
        }
        return ItemStack.EMPTY;
    }

    public void internalMethod02596(int n, ItemStack itemStack) {
        if (n >= 0 && n < this.internalField0299.length) {
            this.internalField0299[n] = itemStack != null ? itemStack : ItemStack.EMPTY;
        }
    }

    public void internalMethod01460(int n, ItemStack itemStack) {
        if (n >= 36 && n <= 44) {
            this.internalMethod02596(n - 36, itemStack);
            return;
        }
        if (n >= 9 && n <= 35) {
            this.internalMethod02596(n, itemStack);
            return;
        }
        if (n >= 5 && n <= 8) {
            this.internalMethod08841(8 - n, itemStack);
            return;
        }
        if (n == 45) {
            this.internalField0878 = itemStack == null ? ItemStack.EMPTY : itemStack;
        }
    }

    public void internalMethod05107(List<ItemStack> list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            this.internalMethod01460(i, list.get(i));
        }
    }

    public void internalMethod08841(int n, ItemStack itemStack) {
        if (n >= 0 && n < this.internalField0300.length) {
            this.internalField0300[n] = itemStack != null ? itemStack : ItemStack.EMPTY;
        }
    }

    public void internalMethod04174(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        if (equipmentSlot == null) {
            return;
        }
        ItemStack itemStack2 = itemStack == null ? ItemStack.EMPTY : itemStack;
        switch (equipmentSlot) {
            case MAINHAND: {
                this.internalMethod02596(this.internalField0228, itemStack2);
                break;
            }
            case OFFHAND: {
                this.internalField0878 = itemStack2;
                break;
            }
            case FEET: {
                this.internalMethod08841(0, itemStack2);
                break;
            }
            case LEGS: {
                this.internalMethod08841(1, itemStack2);
                break;
            }
            case CHEST: {
                this.internalMethod08841(2, itemStack2);
                break;
            }
            case HEAD: {
                this.internalMethod08841(3, itemStack2);
                break;
            }
        }
    }

    public void internalMethod07204(float f, int n, int n2) {
        this.internalField1049 = MathHelper.clamp((float)f, (float)0.0f, (float)1.0f);
        this.internalField1054 = Math.max(0, n);
        this.internalField1464 = Math.max(0, n2);
    }

    public void internalMethod03425(boolean bl, boolean bl2, boolean bl3, boolean bl4, float f, float f2) {
        this.internalField1100 = bl;
        this.internalField1102 = bl2;
        this.internalField1101 = bl3;
        this.internalField1516 = bl4;
        this.internalField1046 = Math.max(0.0f, f);
        this.internalField1456 = Math.max(0.0f, f2);
    }

    public void internalMethod04044(int n, String string) {
        this.internalField1055 = n;
        this.internalField1056 = 0;
        this.internalField0248 = string == null ? "" : string;
        this.internalMethod08598();
    }

    public void internalMethod04821(int n) {
        if (this.internalField1055 == n || n < 0) {
            this.internalMethod08593();
        }
    }

    public void internalMethod04427(int n, int n2, List<ItemStack> list) {
        if (n == 0) {
            return;
        }
        if (this.internalField1055 != n) {
            this.internalMethod04044(n, this.internalField0248);
        }
        this.internalField1056 = n2;
        this.internalMethod08598();
        if (list == null) {
            return;
        }
        this.internalMethod09432(list.size());
        for (int i = 0; i < list.size(); ++i) {
            this.internalField1116[i] = list.get(i) == null ? ItemStack.EMPTY : list.get(i);
        }
    }

    public void internalMethod03129(int n, int n2, int n3, ItemStack itemStack) {
        if (n == 0) {
            this.internalMethod01460(n3, itemStack);
            return;
        }
        if (n3 < 0) {
            return;
        }
        if (this.internalField1055 != n) {
            this.internalMethod04044(n, this.internalField0248);
        }
        this.internalField1056 = n2;
        this.internalMethod09432(n3 + 1);
        this.internalField1116[n3] = itemStack == null ? ItemStack.EMPTY : itemStack;
    }

    public ItemStack internalMethod01305(int n) {
        if (n < 0 || n >= this.internalField1116.length) {
            return ItemStack.EMPTY;
        }
        return this.internalField1116[n] == null ? ItemStack.EMPTY : this.internalField1116[n];
    }

    public boolean internalMethod02812() {
        return this.internalField1055 > 0;
    }

    public boolean internalMethod02820() {
        return this.internalField1048 <= 0.0f;
    }

    public boolean internalMethod08594() {
        return this.internalField0194 != this.internalField1043 || this.internalField0193 != this.internalField1042 || this.internalField1045 != this.internalField1044;
    }

    public boolean internalMethod08599() {
        return this.internalField0205 != this.internalField1457 || this.internalField0206 != this.internalField1458;
    }

    public void internalMethod02811() {
        this.internalField1043 = this.internalField0194;
        this.internalField1042 = this.internalField0193;
        this.internalField1044 = this.internalField1045;
        this.internalField1457 = this.internalField0205;
        this.internalField1458 = this.internalField0206;
    }

    public void internalMethod02819() {
        int n;
        this.internalField1045 = 0.0;
        this.internalField0193 = 0.0;
        this.internalField0194 = 0.0;
        this.internalField0206 = 0.0f;
        this.internalField0205 = 0.0f;
        this.internalField1044 = 0.0;
        this.internalField1042 = 0.0;
        this.internalField1043 = 0.0;
        this.internalField1458 = 0.0f;
        this.internalField1457 = 0.0f;
        this.internalField1048 = 20.0f;
        this.internalField1047 = 20.0f;
        this.internalField0227 = 20;
        this.internalField0228 = 0;
        this.internalField0277 = false;
        this.internalField0276 = false;
        this.internalField1099 = true;
        this.internalField1053 = -1;
        this.internalField1049 = 0.0f;
        this.internalField1054 = 0;
        this.internalField1464 = 0;
        this.internalField1100 = false;
        this.internalField1102 = false;
        this.internalField1101 = false;
        this.internalField1516 = false;
        this.internalField1046 = 0.05f;
        this.internalField1456 = 0.1f;
        this.internalField1470 = -1;
        for (n = 0; n < this.internalField0299.length; ++n) {
            this.internalField0299[n] = ItemStack.EMPTY;
        }
        for (n = 0; n < this.internalField0300.length; ++n) {
            this.internalField0300[n] = ItemStack.EMPTY;
        }
        this.internalField0878 = ItemStack.EMPTY;
        this.internalField0879 = ItemStack.EMPTY;
        this.internalMethod08593();
    }

    private void internalMethod08593() {
        this.internalField1055 = -1;
        this.internalField1056 = 0;
        this.internalField0248 = "";
        this.internalMethod08598();
    }

    private void internalMethod08598() {
        for (int i = 0; i < this.internalField1116.length; ++i) {
            this.internalField1116[i] = ItemStack.EMPTY;
        }
    }

    private void internalMethod09432(int n) {
        if (n <= this.internalField1116.length) {
            return;
        }
        ItemStack[] itemStackArray = new ItemStack[n];
        System.arraycopy(this.internalField1116, 0, itemStackArray, 0, this.internalField1116.length);
        for (int i = this.internalField1116.length; i < itemStackArray.length; ++i) {
            itemStackArray[i] = ItemStack.EMPTY;
        }
        this.internalField1116 = itemStackArray;
    }

    @Generated
    public double internalMethod02807() {
        return this.internalField0194;
    }

    @Generated
    public double internalMethod02816() {
        return this.internalField0193;
    }

    @Generated
    public double internalMethod08590() {
        return this.internalField1045;
    }

    @Generated
    public float internalMethod02808() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod02817() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod08591() {
        return this.internalField1048;
    }

    @Generated
    public float internalMethod08596() {
        return this.internalField1047;
    }

    @Generated
    public int internalMethod02809() {
        return this.internalField0227;
    }

    @Generated
    public int internalMethod02818() {
        return this.internalField0228;
    }

    @Generated
    public boolean internalMethod08608() {
        return this.internalField0277;
    }

    @Generated
    public boolean internalMethod08612() {
        return this.internalField0276;
    }

    @Generated
    public boolean internalMethod09849() {
        return this.internalField1099;
    }

    @Generated
    public int internalMethod08592() {
        return this.internalField1053;
    }

    @Generated
    public ItemStack[] internalMethod00106() {
        return this.internalField0299;
    }

    @Generated
    public ItemStack[] internalMethod05401() {
        return this.internalField0300;
    }

    @Generated
    public ItemStack internalMethod05298() {
        return this.internalField0878;
    }

    @Generated
    public ItemStack internalMethod08661() {
        return this.internalField0879;
    }

    @Generated
    public ItemStack[] internalMethod07683() {
        return this.internalField1116;
    }

    @Generated
    public int internalMethod08597() {
        return this.internalField1055;
    }

    @Generated
    public int internalMethod08607() {
        return this.internalField1056;
    }

    @Generated
    public String internalMethod04767() {
        return this.internalField0248;
    }

    @Generated
    public float internalMethod08606() {
        return this.internalField1049;
    }

    @Generated
    public int internalMethod08611() {
        return this.internalField1054;
    }

    @Generated
    public int internalMethod09848() {
        return this.internalField1464;
    }

    @Generated
    public boolean internalMethod09852() {
        return this.internalField1100;
    }

    @Generated
    public boolean internalMethod09866() {
        return this.internalField1102;
    }

    @Generated
    public boolean internalMethod09867() {
        return this.internalField1101;
    }

    @Generated
    public boolean internalMethod09199() {
        return this.internalField1516;
    }

    @Generated
    public float internalMethod08610() {
        return this.internalField1046;
    }

    @Generated
    public float internalMethod09847() {
        return this.internalField1456;
    }

    @Generated
    public int internalMethod09851() {
        return this.internalField1470;
    }

    @Generated
    public long internalMethod02810() {
        return this.internalField0229;
    }

    @Generated
    public double internalMethod08595() {
        return this.internalField1043;
    }

    @Generated
    public double internalMethod08605() {
        return this.internalField1042;
    }

    @Generated
    public double internalMethod08609() {
        return this.internalField1044;
    }

    @Generated
    public float internalMethod09850() {
        return this.internalField1457;
    }

    @Generated
    public float internalMethod09865() {
        return this.internalField1458;
    }

    @Generated
    public void internalMethod04819(double d) {
        this.internalField0194 = d;
    }

    @Generated
    public void internalMethod04879(double d) {
        this.internalField0193 = d;
    }

    @Generated
    public void internalMethod09019(double d) {
        this.internalField1045 = d;
    }

    @Generated
    public void internalMethod04820(float f) {
        this.internalField0205 = f;
    }

    @Generated
    public void internalMethod04880(float f) {
        this.internalField0206 = f;
    }

    @Generated
    public void internalMethod09020(float f) {
        this.internalField1048 = f;
    }

    @Generated
    public void internalMethod09037(float f) {
        this.internalField1047 = f;
    }

    @Generated
    public void internalMethod04881(int n) {
        this.internalField0227 = n;
    }

    @Generated
    public void internalMethod09021(int n) {
        this.internalField0228 = n;
    }

    @Generated
    public void internalMethod04823(boolean bl) {
        this.internalField0277 = bl;
    }

    @Generated
    public void internalMethod04882(boolean bl) {
        this.internalField0276 = bl;
    }

    @Generated
    public void internalMethod09022(boolean bl) {
        this.internalField1099 = bl;
    }

    @Generated
    public void internalMethod09038(int n) {
        this.internalField1053 = n;
    }

    @Generated
    public void internalMethod01404(ItemStack[] itemStackArray) {
        this.internalField0299 = itemStackArray;
    }

    @Generated
    public void internalMethod00165(ItemStack[] itemStackArray) {
        this.internalField0300 = itemStackArray;
    }

    @Generated
    public void internalMethod02015(ItemStack itemStack) {
        this.internalField0878 = itemStack;
    }

    @Generated
    public void internalMethod04965(ItemStack itemStack) {
        this.internalField0879 = itemStack;
    }

    @Generated
    public void internalMethod08578(ItemStack[] itemStackArray) {
        this.internalField1116 = itemStackArray;
    }

    @Generated
    public void internalMethod07793(int n) {
        this.internalField1055 = n;
    }

    @Generated
    public void internalMethod07818(int n) {
        this.internalField1056 = n;
    }

    @Generated
    public void internalMethod02414(String string) {
        this.internalField0248 = string;
    }

    @Generated
    public void internalMethod07792(float f) {
        this.internalField1049 = f;
    }

    @Generated
    public void internalMethod09252(int n) {
        this.internalField1054 = n;
    }

    @Generated
    public void internalMethod09262(int n) {
        this.internalField1464 = n;
    }

    @Generated
    public void internalMethod09039(boolean bl) {
        this.internalField1100 = bl;
    }

    @Generated
    public void internalMethod07794(boolean bl) {
        this.internalField1102 = bl;
    }

    @Generated
    public void internalMethod07819(boolean bl) {
        this.internalField1101 = bl;
    }

    @Generated
    public void internalMethod09253(boolean bl) {
        this.internalField1516 = bl;
    }

    @Generated
    public void internalMethod07817(float f) {
        this.internalField1046 = f;
    }

    @Generated
    public void internalMethod09251(float f) {
        this.internalField1456 = f;
    }

    @Generated
    public void internalMethod09425(int n) {
        this.internalField1470 = n;
    }

    @Generated
    public void internalMethod04822(long l) {
        this.internalField0229 = l;
    }

    @Generated
    public void internalMethod09036(double d) {
        this.internalField1043 = d;
    }

    @Generated
    public void internalMethod07791(double d) {
        this.internalField1042 = d;
    }

    @Generated
    public void internalMethod07816(double d) {
        this.internalField1044 = d;
    }

    @Generated
    public void internalMethod09261(float f) {
        this.internalField1457 = f;
    }

    @Generated
    public void internalMethod09424(float f) {
        this.internalField1458 = f;
    }
}

