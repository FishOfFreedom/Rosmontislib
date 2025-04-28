package com.freefish.rosmontislib.client;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.client.screeneffect.CameraShake;
import com.freefish.rosmontislib.event.packet.toclient.CameraShakeMessage;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RLClientUseUtils {
    public static void StartCameraShake(Level world, Vec3 position, float radius, float magnitude, int duration, int fadeDuration){
        if (!world.isClientSide) {
            CameraShake cameraShake = new CameraShake(position, radius, magnitude, duration, fadeDuration);
            RosmontisLib.sendMSGToAll(new CameraShakeMessage(cameraShake));
        }
    }
}
