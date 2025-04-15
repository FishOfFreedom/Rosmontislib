package com.freefish.rosmontislib.client.shader.rendertarget;

public interface IMainTarget {
    int getColorBloomTextureId();
    void clearBloomTexture(boolean error);
    void destroyBloomTextureBuffers();
    void setBloomFilterMode(int pFilterMode);
    void createBuffersHeads(int pWidth, int pHeight, boolean pClearError);
    void createBuffersTail(int pWidth, int pHeight, boolean pClearError);
}
