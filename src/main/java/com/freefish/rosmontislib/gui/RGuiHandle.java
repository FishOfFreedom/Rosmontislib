package com.freefish.rosmontislib.gui;

import com.freefish.rosmontislib.gui.guiproxy.RLScene;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum RGuiHandle {
    INSTANCE;

    private HashBiMap<String,RLScene> HUD = HashBiMap.create();

    public void openGui(RLScene rlScene){
        rlScene.openGui();
    }

    public void putHud(RLScene rlScene){
        this.putHud(rlScene.getName().getString(),rlScene);
    }

    public void putHud(String name,RLScene rlScene){
        HUD.put(name,rlScene);
    }

    public void removeHud(RLScene rlScene){
        this.removeHud(rlScene.getName().getString());
    }

    public void removeHud(String name){
        HUD.remove(name);
    }

    public void renderHUD(GuiGraphics graphics,float partialTicks){
        for(RLScene rlScene:HUD.values()){
            rlScene.render(graphics,-1,-1,partialTicks);
        }
    }
}
