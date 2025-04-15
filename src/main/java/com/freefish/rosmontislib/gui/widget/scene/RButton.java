package com.freefish.rosmontislib.gui.widget.scene;

import com.freefish.rosmontislib.gui.widget.scene.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Consumer;

public class RButton extends RLControl{
    private Consumer<Boolean> action;

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, MouseButton pButton) {
        if(isMouseOver()&&action!=null) {
            action.accept(true);
            return true;
        }else
            return false;
    }

    public final void setOnAction(Consumer<Boolean> action) {
        this.action = action;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackGround(graphics,mouseX,mouseY,partialTicks);
        int layoutX = getLayoutX();
        int layoutY = getLayoutY();
        int width = getWidth();
        int height = getHeight();
    }
}
