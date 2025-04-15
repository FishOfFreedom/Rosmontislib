package com.freefish.rosmontislib.gui.widget.property;

public class RFloatProperty implements Property<Float>{
    private float f;

    public Float get() {
        return f;
    }

    public void set(Float f) {
        this.f = f;
    }
}
