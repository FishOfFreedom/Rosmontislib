package com.freefish.rosmontislib.example.syncdata;

import com.freefish.rosmontislib.sync.IPersistedSerializable;
import com.freefish.rosmontislib.sync.annotation.Persisted;

public class SyncData implements IPersistedSerializable {
    @Persisted
    public float a;

    @Persisted
    public boolean b;

    @Override
    public String toString() {
        return a + "  " + b;
    }
}
