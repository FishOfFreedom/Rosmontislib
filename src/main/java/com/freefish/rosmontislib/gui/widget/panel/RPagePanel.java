package com.freefish.rosmontislib.gui.widget.panel;


import com.freefish.rosmontislib.gui.widget.scene.RLNode;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class RPagePanel extends RLPanel {
    private List<RLNode> list;
    private int maxLen;

    public RPagePanel(int maxLen) {
        this.list = new ArrayList<>();
        this.maxLen = maxLen;
        scrollToList(0);
    }

    public void scrollToList(int count){
        getChildren().clear();
        if(!list.isEmpty()) {
            for(int i = 0;i+count<list.size()&&i<maxLen;i++){
                addChildren(list.get(i+count));
            }
            for(int i = 0;i+count<list.size();i++){
                list.get(i).setName(Component.literal(String.valueOf(i)));
            }
        }
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

            rlNode.setPos(layoutX+width/2-nodeWidth/2,layoutY+amount,nodeWidth,nodeHeight);
            rlNode.updateGui(mouseX,mouseY,partialTicks);

            amount+=nodeHeight;
        }
    }

    public void add(RLNode button){
        list.add(button);
        scrollToList(0);
    }

    public void remove(RLNode button){
        list.remove(button);
        scrollToList(0);
    }

    public List<RLNode> getPageList() {
        return list;
    }
}