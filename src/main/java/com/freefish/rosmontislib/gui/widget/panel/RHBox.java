package com.freefish.rosmontislib.gui.widget.panel;

import com.freefish.rosmontislib.gui.widget.scene.RLNode;

import java.util.List;

public class RHBox extends RLPanel{
    private int in;

    public RHBox(float xSize, float ySize) {
        super(xSize, ySize);
    }

    public RHBox() {
        this.in = 0;
    }

    public RHBox(int in) {
        this.in = in;
    }

    @Override
    public void updateGui(int mouseX, int mouseY, float partialTicks) {
        List<RLNode> children = getChildren();
        int height = getHeight();
        int layoutX =getLayoutX();
        int layoutY =getLayoutY();
        int size =  children.size();
        int amount = 0;

        for (int i = 0; i < size; i++) {
            RLNode rlNode = children.get(i);
            int nodeWidth = rlNode.getWidth();
            int nodeHeight = rlNode.getHeight();
            amount += in;

            rlNode.setPos(layoutX+amount,layoutY+height/2-nodeHeight/2,nodeWidth,nodeHeight);
            rlNode.updateGui(mouseX,mouseY,partialTicks);

            amount+=nodeWidth;
        }
    }
}
