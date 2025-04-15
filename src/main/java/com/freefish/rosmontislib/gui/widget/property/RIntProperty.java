package com.freefish.rosmontislib.gui.widget.property;

public class RIntProperty implements Property<Integer> {
    private int f;

    public Integer get() {
        return f;
    }

    public void set(Integer f) {
        this.f = f;
    }
}
