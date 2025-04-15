package com.freefish.rosmontislib.gui.widget.scene;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RImageView extends RLNode{
    private ResourceLocation resourceLocation;
    private boolean isCenter;
    private Supplier<ResourceLocation> imageEvent;

    public RImageView(ResourceLocation resourceLocation){
        this.resourceLocation = resourceLocation;
    }

    public void setCenter(boolean center) {
        isCenter = center;
    }

    public void setImageEvent(Supplier<ResourceLocation> imageEvent) {
        this.imageEvent = imageEvent;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int layoutX = getLayoutX();
        int layoutY = getLayoutY();
        int width = getWidth();
        int height = getHeight();
        graphics.setColor(1,1,1,1f);
        if(imageEvent!=null)
            graphics.blit(imageEvent.get(), layoutX, layoutY, 0, 0, width, height, width, height);
        else
            graphics.blit(resourceLocation, layoutX, layoutY, 0, 0, width, height, width, height);
    }
}
