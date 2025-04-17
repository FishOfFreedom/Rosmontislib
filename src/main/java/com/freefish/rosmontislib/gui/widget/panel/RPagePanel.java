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
        int size = list.size();

        if(size <=maxLen) {
            count = 0;
        }else {
            count = Math.min(count,size-maxLen);
        }

        if(!list.isEmpty()) {
            for(int i = 0; i+count< size &&i<maxLen; i++){
                RLNode rlNode = list.get(i + count);
                rlNode.setName(Component.literal(String.valueOf(i + count)));
                addChildren(rlNode);
            }
        }
    }

    public void scrollToList(float count1){
        getChildren().clear();
        int size = list.size();

        int count;
        if(size <=maxLen) {
            count = 0;
        }else {
            count = (int)((size -maxLen)*count1);
        }

        if(!list.isEmpty()) {
            for(int i = 0; i+count< size &&i<maxLen; i++){
                RLNode rlNode = list.get(i + count);
                rlNode.setName(Component.literal(String.valueOf(i + count)));
                addChildren(rlNode);
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
        scrollToList(list.size()-maxLen);
    }

    public void remove(RLNode button){
        int i = list.indexOf(button)+1;

        list.remove(button);
        scrollToList(Math.max(0,i-1));
    }

    public List<RLNode> getPageList() {
        return list;
    }
}