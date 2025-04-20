package com.freefish.rosmontislib.client.particle.advance.data;


/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote ToggleGroup
 */
public class ToggleGroup{
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void open() {
        this.enable = true;
    }

    protected boolean enable;

}
