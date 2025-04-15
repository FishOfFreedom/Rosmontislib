package com.freefish.rosmontislib.gui.widget.panel;

import com.freefish.rosmontislib.gui.widget.scene.RLNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RAnchorPane extends RLPanel{
    private static Map<RLNode,PosType[]> map = new HashMap<>();
    public final static int UP = 0;
    public final static int DOWN = 1;
    public final static int RIGHT = 2;
    public final static int LEFT = 3;

    public RAnchorPane(float xSize, float ySize) {
        super(xSize, ySize);
    }

    public RAnchorPane() {
    }

    @Override
    public void updateGui(int mouseX, int mouseY, float partialTicks) {
        List<RLNode> children = getChildren();
        int width = getWidth();
        int height = getHeight();
        int layoutX =getLayoutX();
        int layoutY =getLayoutY();
        int size = children.size();

        for (int i = 0; i < size; i++) {
            RLNode rlNode = children.get(i);
            PosType[] orDefault = map.get(rlNode);
            for(PosType posType:orDefault) {
                switch (posType.getType()) {
                    case UP: {
                        rlNode.setLayoutY(layoutY+posType.getOffset());
                        break;
                    }
                    case DOWN: {
                        rlNode.setLayoutY(layoutY+height-rlNode.getHeight()-posType.getOffset());
                        break;
                    }
                    case RIGHT: {
                        rlNode.setLayoutX(layoutX+width-rlNode.getWidth()-posType.getOffset());
                        break;
                    }
                    case LEFT: {
                        rlNode.setLayoutX(layoutX+posType.getOffset());
                        break;
                    }
                }
            }
            rlNode.updateGui(mouseX,mouseY,partialTicks);
        }
    }

    public static void setConstantPos(RLNode rlNode,PosType ... posType){
        map.put(rlNode,posType);
    }

    public static class PosType{
        public int getType() {
            return type;
        }

        public int getOffset() {
            return offset;
        }

        int type;

        public PosType(int type, int offset) {
            this.type = type;
            this.offset = offset;
        }

        int offset;
    }
}
