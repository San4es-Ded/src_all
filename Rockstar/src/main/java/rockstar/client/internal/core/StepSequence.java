package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import rockstar.client.internal.core.SequenceStep;

public class StepSequence {
    private final List<SequenceStep> internalField0416 = new ArrayList<SequenceStep>();
    private int internalField0227 = 0;
    private boolean internalField0277 = false;
    private boolean internalField0276 = false;

    public StepSequence internalMethod01234(SequenceStep typedValue105) {
        this.internalField0416.add(typedValue105);
        return this;
    }

    public void internalMethod05210() {
        if (this.internalField0416.isEmpty()) {
            return;
        }
        this.internalField0277 = true;
        this.internalField0227 = 0;
        this.internalField0276 = false;
        for (SequenceStep typedValue105 : this.internalField0416) {
            typedValue105.internalMethod01928();
        }
        this.internalField0416.get(0).internalMethod01925();
        this.internalField0276 = true;
    }

    public void internalMethod05212() {
        if (!this.internalField0277 || this.internalField0416.isEmpty()) {
            return;
        }
        SequenceStep typedValue105 = this.internalField0416.get(this.internalField0227);
        if (!this.internalField0276) {
            typedValue105.internalMethod01925();
            this.internalField0276 = true;
        }
        if (typedValue105.internalMethod01926()) {
            ++this.internalField0227;
            this.internalField0276 = false;
            if (this.internalField0227 >= this.internalField0416.size()) {
                this.internalMethod08221();
                return;
            }
            this.internalField0416.get(this.internalField0227).internalMethod01925();
            this.internalField0276 = true;
        }
    }

    public void internalMethod08221() {
        this.internalField0277 = false;
        this.internalField0227 = 0;
        this.internalField0276 = false;
        this.internalField0416.clear();
    }

    public boolean internalMethod05211() {
        return this.internalField0277;
    }

    @Generated
    public int internalMethod05209() {
        return this.internalField0227;
    }

    @Generated
    public boolean internalMethod05213() {
        return this.internalField0277;
    }
}

