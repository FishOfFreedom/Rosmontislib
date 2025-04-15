package com.freefish.rosmontislib.gui.widget.scene;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Supplier;

public class RText extends RLNode{
    private static Font font = Minecraft.getInstance().font;
    private String context;
    private Supplier<String> action;
    private int locateType;
    public final static int UP = 0;
    public final static int DOWN = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;

    public RText(String context){
        this.context = context;
    }

    public int getLocateType() {
        return locateType;
    }

    public void setLocateType(int locateType) {
        this.locateType = locateType;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int layoutX = getLayoutX();
        int layoutY = getLayoutY();
        int width = getWidth();
        int height = getHeight();
//todo button
        float scale = (float)height/font.lineHeight;
        graphics.pose().pushPose();

        String s = action!=null?action.get():context;
        int yOffset = 0;
        int xOffset = 0;
        if(locateType==LEFT) {
            graphics.pose().translate(layoutX, layoutY+height/2f, 0);
            graphics.pose().scale(scale, scale, 1);
            yOffset =  -(int)((height/scale)/2);
        }
        if(locateType==RIGHT) {
            graphics.pose().translate(layoutX+width, layoutY+height/2f, 0);
            graphics.pose().scale(scale, scale, 1);
            yOffset =  -(int)((height/scale)/2);
            xOffset =  -(int)(font.width(s));
        }
        graphics.setColor(1,1,1,1);
        graphics.drawString(font,s,xOffset,yOffset,0XFFFFFF);
        graphics.pose().popPose();
    }

    public final void setText(Supplier<String> action) {
        this.action = action;
    }
}
