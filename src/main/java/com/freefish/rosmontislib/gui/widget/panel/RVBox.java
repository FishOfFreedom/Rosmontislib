package com.freefish.rosmontislib.gui.widget.panel;

import com.freefish.rosmontislib.gui.widget.scene.RLNode;

import java.util.List;

public class RVBox extends RLPanel{
    private int in;
    public RVBox(float xSize, float ySize) {
        super(xSize, ySize);
    }

    public RVBox() {
        this.in = 0;
    }

    public RVBox(int in) {
        this.in = in;
    }

    @Override
    public void updateGui(int mouseX, int mouseY, float partialTicks) {
        List<RLNode> children = getChildren();
        int width = getWidth();
        int layoutX =getLayoutX();
        int layoutY =getLayoutY();
        int size = children.size();
        int amount = 0;

        for (int i = 0; i < size; i++) {
            RLNode rlNode = children.get(i);
            int nodeWidth = rlNode.getWidth();
            int nodeHeight = rlNode.getHeight();
            amount += in;

            rlNode.setPos(layoutX+width/2-nodeWidth/2,layoutY+amount,nodeWidth,nodeHeight);
            rlNode.updateGui(mouseX,mouseY,partialTicks);

            amount+=nodeHeight;
        }
    }
}
