package com.freefish.rosmontislib.gui.guiproxy;

import com.freefish.rosmontislib.gui.widget.scene.RLScene;

public interface IStateProxy extends IOpenState{
    RLScene getState();

    @Override
    default void openGui(){
        getState().openGui();
    }

    @Override
    default void closeGui(){
        getState().closeGui();
    }

    @Override
    default void closeGui(boolean openPrevScreen){
        getState().closeGui();
    };
}
