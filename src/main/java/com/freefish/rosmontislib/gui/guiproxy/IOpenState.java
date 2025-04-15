package com.freefish.rosmontislib.gui.guiproxy;

public interface IOpenState {

    void openGui();

    void closeGui();

    void closeGui(boolean openPrevScreen);

    default void run(){
        openGui();
    };
}
