package com.freefish.rosmontislib.example.gui.screen;

import com.freefish.rosmontislib.gui.guiproxy.RContainerScreen;
import com.freefish.rosmontislib.gui.widget.panel.RLPanel;
import com.freefish.rosmontislib.gui.widget.panel.RPagePanel;
import com.freefish.rosmontislib.gui.widget.scene.RButton;
import com.freefish.rosmontislib.gui.widget.scene.RInsideButton;
import com.freefish.rosmontislib.gui.widget.scene.RLScene;
import com.freefish.rosmontislib.gui.widget.scene.RScrollBar;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class EntitySpawnerScreen extends RContainerScreen<EntitySpawnerMenu> {
    private RPagePanel spawnTypePageList;

    public EntitySpawnerScreen(EntitySpawnerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        createScreen(menu);
    }

    @Override
    public void init() {
        super.init();
    }

    private void createScreen(EntitySpawnerMenu menu){
        //wave list
        RPagePanel rPagePanel = new RPagePanel(4);
        rPagePanel.setPos(100,0,60,60);

        this.spawnTypePageList = new RPagePanel(2);
        spawnTypePageList.setPos(200,0,60,60);

        for(int i = 0;i<10;i++){
            rPagePanel.add(createButton(rPagePanel));
        }
        //control wave list
        RScrollBar rScrollBar = new RScrollBar(4);
        rScrollBar.setPos(0,0,RScrollBar.WIDTH,60);

        rScrollBar.setResponder((i)->{
            rPagePanel.scrollToList(i);
        });
        //add wave
        RButton addWave = new RButton();
        addWave.setPos(20,100,20,20);
        addWave.setName(Component.literal("addWave"));


        addWave.setOnAction((act -> {
            if(act){
                rPagePanel.add(createButton(rPagePanel));
            }
        }));




        RLPanel root = new RLPanel();
        root.addChildren(rScrollBar,rPagePanel,spawnTypePageList);

        RLScene rlScene = new RLScene(root, 427, 240);
        rlScene.setSceneCentre(true);
        setRlScene(rlScene);
    }

    private RInsideButton createButton(RPagePanel rPagePanel){
        RInsideButton rButton = new RInsideButton();
        RPagePanel spawnTypelist = new RPagePanel(4);
        spawnTypelist.setPos(100,100,60,60);
        spawnTypePageList.add(spawnTypelist);


        rButton.setOnAction((b)->{
            if(b){
                int i = spawnTypePageList.getPageList().indexOf(spawnTypelist);
                spawnTypePageList.scrollToList(i-1);
            }
        });
        rButton.setOnAction1((b)->{
            if(b){
                rPagePanel.remove(rButton);
                spawnTypePageList.remove(spawnTypelist);
            }
        });

        rButton.setPos(0,0,60,20);

        return  rButton;
    }
}