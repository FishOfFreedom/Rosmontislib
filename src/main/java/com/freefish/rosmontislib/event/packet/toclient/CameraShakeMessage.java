package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.client.ClientHandle;
import com.freefish.rosmontislib.client.screeneffect.CameraShake;
import com.freefish.rosmontislib.event.IHandlerContext;
import com.freefish.rosmontislib.event.IPacket;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@NoArgsConstructor
public class CameraShakeMessage implements IPacket {
    private CameraShake cameraShake;

    public CameraShakeMessage(CameraShake cameraShake) {
        this.cameraShake = cameraShake;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(cameraShake.getMagnitude());
        buf.writeFloat(cameraShake.getRadius());
        buf.writeVarInt(cameraShake.getDuration());
        buf.writeVarInt(cameraShake.getFadeDuration());
        Vec3 vec3 = cameraShake.getVec3();
        buf.writeFloat((float) vec3.x);
        buf.writeFloat((float) vec3.y);
        buf.writeFloat((float) vec3.z);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        float ma = buf.readFloat();
        float ra = buf.readFloat();
        int du = buf.readVarInt();
        int fa = buf.readVarInt();
        float x = buf.readFloat();
        float y = buf.readFloat();
        float z = buf.readFloat();

        cameraShake = new CameraShake(new Vec3(x,y,z),ra,ma,du,fa);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        if (handler.isClient()) {
            ClientHandle.INSTANCE.getCameraShakeList().add(cameraShake);
        }
    }
}
