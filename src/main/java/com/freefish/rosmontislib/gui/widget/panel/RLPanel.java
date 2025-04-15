package com.freefish.rosmontislib.gui.widget.panel;

import com.freefish.rosmontislib.gui.widget.scene.RLNode;
import com.freefish.rosmontislib.gui.widget.scene.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class RLPanel extends RLNode {
    private List<RLNode> children = new ArrayList<>();

    public RLPanel(RLNode... children) {
        super();
        for(RLNode node:children) {
            addChildren(node);
        }
    }

    public RLPanel(float xSize, float ySize) {
        super(xSize, ySize);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackGround(graphics,mouseX,mouseY,partialTicks);
        List<RLNode> children = getChildren();
        for (int i = 0, size = children.size(); i < size; i++) {
            RLNode rlNode = children.get(i);
            rlNode.render(graphics,mouseX,mouseY,partialTicks);
        }
    }

    @Override
    public boolean mouseDragged(double x, double y, MouseButton button, double dragX, double dragY) {
        for (RLNode child : getChildren()) {
            if (child.mouseDragged(x, y, button, dragX, dragY))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, MouseButton pButton) {
        for (RLNode child : getChildren()) {
            if (child.mouseClicked(pMouseX, pMouseY, pButton))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, MouseButton pButton) {
        for (RLNode child : getChildren()) {
            if (child.mouseReleased(pMouseX, pMouseY, pButton))
                return true;
        }
        return false;
    }

    @Override
    public void updateMouseOver(int mouseX, int mouseY) {
        getChildren().forEach(rlNode -> rlNode.updateMouseOver(mouseX,mouseY));
        super.updateMouseOver(mouseX, mouseY);
    }

    public List<RLNode> getChildren() {
        return children;
    }

    public void addChildren(RLNode rlNode) {
        getChildren().add(rlNode);
        rlNode.setParent(this);
    }

    @Override
    public void tick() {
        super.tick();
        getChildren().forEach(RLNode::tick);
    }

    public void addChildren(RLNode... rlNode ) {
        for(RLNode node:rlNode) {
            getChildren().add(node);
            node.setParent(this);
        }
    }

    @Override
    public void updateGui(int mouseX, int mouseY, float partialTicks) {
        List<RLNode> children = getChildren();
        for(RLNode rlNode:children){
            rlNode.updateGui(mouseX,mouseY,partialTicks);
        }
    }
}
