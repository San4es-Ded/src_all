package haron.settings;

import haron.settings.Setting;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ModeSetting
extends Setting<Integer> {
    private final String[] values;
    private final boolean multiSelect;
    private final Set<Integer> selectedIndices;

    public boolean isSelected(String string) {
        return this.multiSelect ? this.selectedIndices.contains(ModeSetting.indexOf(this.values, string)) : this.selectedValue().equals(string);
    }

    public boolean isSelected(int n) {
        return this.multiSelect ? this.selectedIndices.contains(n) : (Integer)this.k() == n;
    }

    public void select(int n) {
        if (!this.multiSelect) {
            super.a(ModeSetting.clampIndex(this.values, n));
        } else if (this.selectedIndices.contains(n)) {
            this.selectedIndices.remove(n);
        } else if (ModeSetting.isValidIndex(this.values, n)) {
            this.selectedIndices.add(n);
        }
    }

    public void select(String string) {
        int n = 433;
        this.select(ModeSetting.indexOf(this.values, string));
    }

    public void setSelectedIndices(Set<Integer> set) {
        this.selectedIndices.clear();
        for (int n : set) {
            if (!ModeSetting.isValidIndex(this.values, n)) continue;
            this.selectedIndices.add(n);
        }
    }

    public List<String> valueList() {
        return Arrays.asList(this.values);
    }

    private static int clampIndex(String[] stringArray, int n) {
        if (stringArray.length == 0) {
            return 0;
        }
        return Math.max(0, Math.min(n, stringArray.length - 1));
    }

    @Override
    public Setting<Integer> visibleWhen(Supplier<Boolean> supplier) {
        super.visibleWhen(supplier);
        return this;
    }

    public Setting<Integer> visibleWhen2(Supplier supplier) {
        return this.visibleWhen(supplier);
    }

    public Set<Integer> selectedIndices() {
        HashSet<Integer> hashSet = new HashSet<Integer>(this.selectedIndices);
        if (!this.multiSelect) {
            hashSet.add((Integer)this.k());
        }
        return hashSet;
    }

    private static int firstSelectedIndex(String[] stringArray, int[] nArray) {
        if (nArray.length > 0) {
            return ModeSetting.clampIndex(stringArray, nArray[0]);
        }
        return 0;
    }

    public boolean isMultiSelect() {
        return this.multiSelect;
    }

    private static boolean isValidIndex(String[] stringArray, int n) {
        return n >= 0 && n < stringArray.length;
    }

    public String selectedValue() {
        int n = (Integer)this.k();
        return ModeSetting.isValidIndex(this.values, n) ? this.values[n] : "";
    }

    public ModeSetting(String string, String string2, String[] stringArray, int n) {
        super(string, string2, ModeSetting.clampIndex(stringArray, n));
        this.values = stringArray;
        this.multiSelect = false;
        this.selectedIndices = new HashSet<Integer>();
    }

    public ModeSetting(String string, String[] stringArray, int n) {
        this(string, "", stringArray, n);
    }

    public ModeSetting(String string, String[] stringArray, String string2) {
        this(string, "", stringArray, ModeSetting.indexOf(stringArray, string2));
    }

    public ModeSetting(String string, String[] stringArray, int[] nArray) {
        this(string, "", stringArray, nArray);
    }

    public ModeSetting(String string, String string2, String[] stringArray, int[] nArray) {
        super(string, string2, ModeSetting.firstSelectedIndex(stringArray, nArray));
        this.values = stringArray;
        this.multiSelect = true;
        this.selectedIndices = new HashSet<Integer>();
        for (int n : nArray) {
            if (!ModeSetting.isValidIndex(stringArray, n)) continue;
            this.selectedIndices.add(n);
        }
    }

    public String[] values() {
        int n = 168;
        return this.values;
    }

    private static int indexOf(String[] stringArray, String string) {
        for (int i = 0; i < stringArray.length; ++i) {
            if (!stringArray[i].equals(string)) continue;
            return i;
        }
        return 0;
    }

    public Set<Integer> e() {
        return this.selectedIndices();
    }

    public void b(int n) {
        this.select(n);
    }

    public boolean b(String string) {
        return this.isSelected(string);
    }

    public List<String> b() {
        int n = 711;
        return this.valueList();
    }

    public boolean c(int n) {
        return this.isSelected(n);
    }

    public boolean c() {
        return this.isMultiSelect();
    }

    public boolean c(String string) {
        return this.selectedIndices.contains(ModeSetting.indexOf(this.values, string));
    }

    public String d() {
        return this.selectedValue();
    }

    public boolean a(int n) {
        return this.isSelected(n);
    }

    public ModeSetting a(Supplier<Boolean> supplier) {
        return (ModeSetting)this.visibleWhen(supplier);
    }

    public void a(Set<Integer> set) {
        this.setSelectedIndices(set);
    }

    public void a(String string) {
        this.select(string);
    }

    public String[] a() {
        return this.values();
    }
}
