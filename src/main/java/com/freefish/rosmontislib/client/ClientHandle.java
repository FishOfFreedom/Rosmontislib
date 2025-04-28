package com.freefish.rosmontislib.client;

import com.freefish.rosmontislib.client.screeneffect.CameraShake;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum ClientHandle {
    INSTANCE;

    @Getter
    private List<CameraShake> cameraShakeList = new ArrayList<>();

    public void tick(){
        Iterator<CameraShake> iterator = cameraShakeList.iterator();
        while (iterator.hasNext()) {
            CameraShake shake = iterator.next();
            if (shake.shouldRemove()) {
                iterator.remove();
            }
            shake.tick();
        }
    }

}
