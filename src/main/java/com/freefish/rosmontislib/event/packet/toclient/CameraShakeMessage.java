package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.client.ClientHandle;
import com.freefish.rosmontislib.client.screeneffect.CameraShake;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CameraShakeMessage {
    private CameraShake cameraShake;

    public CameraShakeMessage() {

    }

    public CameraShakeMessage(CameraShake cameraShake) {
        this.cameraShake = cameraShake;
    }

    public static void serialize(final CameraShakeMessage message, final FriendlyByteBuf buf) {
        buf.writeFloat(message.cameraShake.getMagnitude());
        buf.writeFloat(message.cameraShake.getRadius());
        buf.writeVarInt(message.cameraShake.getDuration());
        buf.writeVarInt(message.cameraShake.getFadeDuration());
        Vec3 vec3 = message.cameraShake.getVec3();
        buf.writeFloat((float) vec3.x);
        buf.writeFloat((float) vec3.y);
        buf.writeFloat((float) vec3.z);
    }

    public static CameraShakeMessage deserialize(final FriendlyByteBuf buf) {
        final CameraShakeMessage message = new CameraShakeMessage();
        float ma = buf.readFloat();
        float ra = buf.readFloat();
        int du = buf.readVarInt();
        int fa = buf.readVarInt();
        float x = buf.readFloat();
        float y = buf.readFloat();
        float z = buf.readFloat();

        message.cameraShake = new CameraShake(new Vec3(x,y,z),ra,ma,du,fa);
        return message;
    }

    public static class Handler implements BiConsumer<CameraShakeMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final CameraShakeMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ClientHandle.INSTANCE.getCameraShakeList().add(message.cameraShake);
            });
            context.setPacketHandled(true);
        }
    }
}
