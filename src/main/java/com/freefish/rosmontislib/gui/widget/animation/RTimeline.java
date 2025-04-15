package com.freefish.rosmontislib.gui.widget.animation;

import com.freefish.rosmontislib.gui.widget.scene.RLNode;

import java.util.Arrays;
import java.util.List;

public class RTimeline {
    private final List<RKeyFrame> keyFrames;
    private State state = State.RUNNING;
    private int animationTick;

    public RTimeline(RKeyFrame... keyFrames) {
        this.keyFrames = Arrays.asList(keyFrames);
    }

    public void play() {
        switch (state){
            case STOP :
                setState(State.RUNNING);
                break;
            case RUNNING:
                break;
        }
    }

    public void stop() {
        switch (state){
            case STOP :
                break;
            case RUNNING:
                setState(State.STOP);
                break;
        }
    }

    public void update(RLNode node) {
        //if (!isRunning) return;
//
        //double accumulated = 0;
        //RKeyFrame frame = keyFrames;
        //double frameDuration = frame.getDuration().toMillis();
        //if (currentProgress <= (accumulated + frameDuration) / totalDuration) {
        //    double frameProgress = (currentProgress * totalDuration - accumulated) / frameDuration;
        //    for (RKeyValue kv : frame.getValues()) {
        //        kv.update(/* 获取起始值 */, frameProgress); // 需绑定起始值
        //    }
        //}
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isRunning(){
        return state == State.RUNNING;
    }

    public boolean isStop(){
        return state == State.STOP;
    }

    private enum State{
        RUNNING,STOP
    }
}