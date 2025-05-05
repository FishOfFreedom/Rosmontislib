package com.freefish.rosmontislib.utils;

import java.util.function.Consumer;

public interface ISearch<T> {
    void search(String word, Consumer<T> find);
}
