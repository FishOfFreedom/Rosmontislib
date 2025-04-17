package com.freefish.rosmontislib.gui.guiproxy;

import com.freefish.rosmontislib.gui.widget.panel.RLPanel;
import com.freefish.rosmontislib.gui.widget.scene.RLNode;
import com.freefish.rosmontislib.gui.widget.scene.input.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RLScene extends RLNode {
    private boolean isCentre = false;
    private boolean isFill = false;
    private List<RLPanel> panels = new ArrayList<>();

    public RLNode lastFocus;
    public boolean focused;

    private Minecraft minecraft = Minecraft.getInstance();

    public void updateMouseOver(int mouseX, int mouseY) {
        setOver(checkMouseOver(mouseX, mouseY));
        panels.forEach(p -> p.updateMouseOver(mouseX, mouseY));
    }

    public void setRenderBackGround(boolean renderBackGround) {
        isRenderBackGround = renderBackGround;
    }

    public boolean isRenderBackGround() {
        return isRenderBackGround;
    }

    private boolean isRenderBackGround = true;

    public RLScene(RLPanel rlPanel,int weigh,int height) {
        rlPanel.setParent(this);
        setWidth(weigh);
        setHeight(height);
        panels.add(rlPanel);
        rlPanel.setGui(this);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        focused = false;
        for (RLNode child : panels) {
            if (child.mouseScrolled(x, y, scroll))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double x, double y, MouseButton button, double dragX, double dragY) {
        focused = false;
        for (RLNode child : panels) {
            if (child.mouseDragged(x, y, button, dragX, dragY))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, MouseButton pButton) {
        focused = false;
        for (RLNode child : panels) {
            if (child.mouseClicked(pMouseX, pMouseY, pButton))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, MouseButton pButton) {
        focused = false;
        for (RLNode child : panels) {
            if (child.mouseReleased(pMouseX, pMouseY, pButton))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        focused = false;
        for (RLNode child : panels) {
            if (child.keyPressed(keyCode, scanCode, modifiers))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        focused = false;
        for (RLNode child : panels) {
            if (child.keyReleased(keyCode, scanCode, modifiers))
                return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        focused = false;
        for (RLNode child : panels) {
            if (child.charTyped(codePoint, modifiers))
                return true;
        }
        return false;
    }

    public void openGui() {
        Minecraft.getInstance().setScreen(new StateProxy(this));
    }

    public void closeGui() {
    }

    public Component getName() {
        return Component.translatable("");
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if(!isRenderBackGround())
            graphics.fillGradient(getLayoutX(), getLayoutY(),getLayoutX()+ getWidth(), getLayoutY()+getHeight(), -1072689136, -804253680);
        for(RLPanel rlPanel:panels){
            rlPanel.render(graphics,mouseX,mouseY,partialTicks);
        }
    }

    public void setSceneCentre(boolean isCentre){
        this.isCentre = isCentre;
    }

    public boolean isCentre(){
        return this.isCentre;
    }

    public void updateGui(int mouseX, int mouseY, float partialTicks) {
        if(isCentre()){
            setLayoutX(minecraft.getWindow().getGuiScaledWidth()/2- getWidth()/2);
            setLayoutY(minecraft.getWindow().getGuiScaledHeight()/2-getHeight()/2);
        }
        if(isFill){
            setLayoutX(0);
            setLayoutY(0);
            setWidth(minecraft.getWindow().getGuiScaledWidth());
            setHeight(minecraft.getWindow().getGuiScaledHeight());
        }
        panels.forEach(rlPanel -> {
            rlPanel.copyPos(this);
            rlPanel.updateGui(mouseX, mouseY, partialTicks);
        });
    }

    @Override
    public void tick() {
        super.tick();
        panels.forEach(RLNode::tick);
    }

    public boolean switchFocus(@Nonnull RLNode rlNode) {
        if (focused) return false;
        focused = true;
        if (lastFocus == rlNode) return false;
        RLNode l = lastFocus;
        lastFocus = rlNode;
        if (l != null) l.setFocus(false);
        return true;
    }

    public void setFillScene(boolean isFill) {
        this.isFill = isFill;
    }

    public boolean getFillScene() {
        return isFill;
    }
}
