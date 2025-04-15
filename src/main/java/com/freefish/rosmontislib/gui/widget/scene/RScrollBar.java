package com.freefish.rosmontislib.gui.widget.scene;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.gui.widget.scene.input.MouseButton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RScrollBar extends RLControl {
    private static ResourceLocation SCROLL = new ResourceLocation(RosmontisLib.MOD_ID,"textures/gui/scroll.png");
    public final static int WIDTH = 9;

    private boolean scrolling;
    public int len;
    public int maxLen;

    @Nullable
    private Consumer<Integer> responder;

    public RScrollBar(int maxLen) {
        this.maxLen = maxLen;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int i = false ? -1 : -6250336;
        int layoutX = getLayoutX();
        int layoutY = getLayoutY();
        int height = getHeight();
        int width = getWidth();

        graphics.fill(layoutX - 1, layoutY - 1, layoutX + width + 1, layoutY + height + 1, i);
        graphics.fill(layoutX, layoutY, layoutX + width, layoutY + height, -16777216);
        
        float gray = scrolling ? 1 : 0.8f;
        RenderSystem.setShaderColor(gray,gray,gray,1);
        graphics.blit(SCROLL,layoutX,layoutY+(int)((len/(float)maxLen)*(getHeight()-9)),9,0,0,9,9,9,9);
        RenderSystem.setShaderColor(1,1,1,1);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, MouseButton pButton, double pDragX, double pDragY) {
        if (this.scrolling) {
            scrollTo((float) Mth.clamp((pMouseY-getLayoutY())/(getHeight()-9),0,1));
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, MouseButton pButton) {
        if (this.insideScrollbar(pMouseX, pMouseY)&&pButton == MouseButton.LEFT) {
            this.scrolling = this.canScroll();
            return true;
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, MouseButton pButton) {
        if (pButton == MouseButton.LEFT) {
            this.scrolling = false;
        }
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    public void scrollTo(float pos) {
        if (pos < 0.0F || pos > 1.0F) throw new IllegalArgumentException("pos must be of interval 0 to 1");
        if (this.canScroll()) {
             int len1 = (int) Mth.lerp(pos,0f,(float) maxLen);
             if(len1!=len){
                 len = len1;
                 this.onValueChange(len);
             }
        } else {
            this.len = 0;
        }
    }

    private void onValueChange(int pNewLen) {
        if (this.responder != null) {
            this.responder.accept(pNewLen);
        }
    }

    public void setResponder(Consumer<Integer> pResponder) {
        this.responder = pResponder;
    }

    protected boolean insideScrollbar(double pMouseX, double pMouseY) {
        int i = this.getLayoutX();
        int j = this.getLayoutY()+(int)((len/(float)maxLen)*(getHeight()-9));
        int k = i + 9;
        int l = j + 9;
        return pMouseX >= (double)i && pMouseY >= (double)j && pMouseX < (double)k && pMouseY < (double)l;
    }

    public boolean canScroll(){
        return maxLen>=len;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
    }

}