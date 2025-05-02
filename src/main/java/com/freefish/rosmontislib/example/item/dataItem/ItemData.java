package com.freefish.rosmontislib.example.item.dataItem;

import com.freefish.rosmontislib.sync.IPersistedSerializable;
import com.freefish.rosmontislib.sync.annotation.Persisted;

public class ItemData implements IPersistedSerializable {
    @Persisted
    int a = 0;
}
