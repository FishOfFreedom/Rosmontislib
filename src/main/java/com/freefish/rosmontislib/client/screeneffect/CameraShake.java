package com.freefish.rosmontislib.client.screeneffect;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CameraShake {
    @Getter
    @Setter
    private float radius,magnitude;
    @Getter
    @Setter
    private int duration,fadeDuration;
    @Getter
    @Setter
    private Vec3 vec3;

    private int tickCount;

    public CameraShake(Vec3 position, float radius, float magnitude, int duration, int fadeDuration) {
        setRadius(radius);
        setMagnitude(magnitude);
        setDuration(duration);
        setFadeDuration(fadeDuration);
        setVec3(position);
    }

    public void tick() {
        tickCount ++;
    }

    public boolean shouldRemove(){
        return tickCount > getDuration() + getFadeDuration();
    }

    public float getShakeAmount(Player player, float delta) {
        float ticksDelta = tickCount + delta;
        float timeFrac = 1.0f - (ticksDelta - getDuration()) / (getFadeDuration() + 1.0f);
        float baseAmount = ticksDelta < getDuration() ? getMagnitude() : timeFrac * timeFrac * getMagnitude();
        Vec3 playerPos = player.getEyePosition(delta);
        float distFrac = (float) (1.0f - Mth.clamp(vec3.distanceTo(playerPos) / getRadius(), 0, 1));
        return baseAmount * distFrac * distFrac;
    }
}
