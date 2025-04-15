package com.freefish.rosmontislib.gui.widget.scene;

import com.freefish.rosmontislib.gui.widget.scene.input.MouseButton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Consumer;

import static net.minecraft.client.gui.components.AbstractWidget.WIDGETS_LOCATION;

public class RInsideButton extends RLControl{
    private Consumer<Boolean> action;
    private Consumer<Boolean> action1;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, MouseButton pButton) {
        if(isMouseOver()&&action!=null) {
            int layoutX = getLayoutX()+5;
            int layoutY = getLayoutY()+5;
            int width =   getWidth()-10;
            int height = getHeight()-10;

            if(mouseX >= layoutX && mouseY >= layoutY && mouseX < layoutX + width && mouseY < layoutY + height){
                action1.accept(true);
            }else {
                action.accept(true);
            }

            return true;
        }else
            return false;
    }

    public final void setOnAction(Consumer<Boolean> action) {
        this.action = action;
    }
    public final void setOnAction1(Consumer<Boolean> action) {
        this.action1 = action;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackGround(graphics,mouseX,mouseY,partialTicks);
        int layoutX = getLayoutX()+5;
        int layoutY = getLayoutY()+5;
        int width =   getWidth()-10;
        int height = getHeight()-10;

        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blitNineSliced(WIDGETS_LOCATION, layoutX, layoutY, width, height, 20, 4, 200, 20, 0, 66);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
