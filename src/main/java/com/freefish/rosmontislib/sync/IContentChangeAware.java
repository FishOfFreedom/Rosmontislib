package com.freefish.rosmontislib.sync;

public interface IContentChangeAware {
    void setOnContentsChanged(Runnable onContentChanged);
    Runnable getOnContentsChanged();
}