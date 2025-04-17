package com.freefish.rosmontislib.gui.widget.scene;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.gui.widget.scene.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class RCheckBox extends RLControl{
    private static final ResourceLocation TRUE_ICON = new ResourceLocation(RosmontisLib.MOD_ID,"textures/gui/true.png");
    private static final ResourceLocation FALSE_ICON = new ResourceLocation(RosmontisLib.MOD_ID,"textures/gui/false.png");
    protected Consumer<Boolean> booleanResponder;
    private boolean check;

    public RCheckBox(){

    }

    public RCheckBox(boolean check){
        this.check = check;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, MouseButton button) {
        if(isMouseOver()){
            check = !check;
            if(booleanResponder!=null){
                booleanResponder.accept(check);
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        int layoutX = getLayoutX();
        int layoutY = getLayoutY();
        int width = getWidth();
        int height = getHeight();
        graphics.setColor(1,1,1,1f);
        if(check)
            graphics.blit(TRUE_ICON, layoutX, layoutY, 0, 0, width, height, width, height);
        else
            graphics.blit(FALSE_ICON, layoutX, layoutY, 0, 0, width, height, width, height);
    }

    public void setBooleanResponder(Consumer<Boolean> booleanResponder) {
        this.booleanResponder = booleanResponder;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
