package com.freefish.rosmontislib.gui.widget.scene;

import net.minecraft.resources.ResourceLocation;

public class RBackGround {
    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    private ResourceLocation resourceLocation;

    public RBackGround(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
}
