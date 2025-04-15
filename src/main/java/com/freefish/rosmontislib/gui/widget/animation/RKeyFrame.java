package com.freefish.rosmontislib.gui.widget.animation;

import java.util.Arrays;
import java.util.List;

public class RKeyFrame {
    private final int duration;    // 时间点
    private final List<RKeyValue> values; // 关键值列表

    public RKeyFrame(int duration, RKeyValue... values) {
        this.duration = duration;
        this.values = Arrays.asList(values);
    }

    public int getDuration() {
        return duration;
    }

    public List<RKeyValue> getValues() {
        return values;
    }
}