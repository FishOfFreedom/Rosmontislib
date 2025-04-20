package com.freefish.rosmontislib.sync;

/**
 * @author KilaBash
 * @date 2023/2/17
 * @implNote Subscription
 */
@FunctionalInterface
public
interface ISubscription {
    void unsubscribe();
}