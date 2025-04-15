package com.freefish.rosmontislib.gui.guiproxy;

import com.freefish.rosmontislib.gui.widget.scene.RLScene;
import com.freefish.rosmontislib.gui.widget.scene.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public RContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    private RLScene state;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == MouseButton.BACK.id) {
            //state.onBack();
            return true;
        } else {
            return state.mouseClicked(x,y,MouseButton.get(button));
        }
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return state.mouseReleased( x, y, MouseButton.get(button));
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        return state.mouseScrolled( x, y, scroll);
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double dragX, double dragY) {
        return state.mouseDragged( x , y , MouseButton.get(button) , dragX , dragY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean charTyped(char keyChar, int modifiers) {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        state.updateMouseOver((int) mouseX, (int) mouseY);
        state.updateGui(mouseX, mouseY, partialTicks);

        renderBackground(graphics);

        state.render(graphics,mouseX,mouseY,partialTicks);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        state.renderBackGround(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
    }

    @Override
    public void renderBackground(GuiGraphics matrixStack) {
        if(state.isRenderBackGround()) {
            super.renderBackground(matrixStack);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        state.tick();
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void removed() {
        super.removed();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    public RLScene getRlScene() {
        return state;
    }

    public void setRlScene(RLScene rlScene) {
        this.state = rlScene;
    }

}
