package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CRClientLevelEntityMessage {
    private boolean isCreate;

    private int id;

    public CRClientLevelEntityMessage() {

    }

    public CRClientLevelEntityMessage(boolean isCreate,int id) {
        this.isCreate = isCreate;
        this.id = id;
    }

    public CRClientLevelEntityMessage(boolean isCreate) {
        this.isCreate = isCreate;
    }

    public static void serialize(final CRClientLevelEntityMessage message, final FriendlyByteBuf buf) {
        buf.writeBoolean(message.isCreate);
        if(message.isCreate){
            buf.writeVarInt(message.id);
        }
    }

    public static CRClientLevelEntityMessage deserialize(final FriendlyByteBuf buf) {
        final CRClientLevelEntityMessage message = new CRClientLevelEntityMessage();
        message.isCreate = buf.readBoolean();
        if(message.isCreate){
            message.id = buf.readVarInt();
        }
        return message;
    }

    public static class Handler implements BiConsumer<CRClientLevelEntityMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final CRClientLevelEntityMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if(level!=null){
                    LevelEntityManager instance = LevelEntityManager.getInstance(level);
                    instance
                }
            });
            context.setPacketHandled(true);
        }
    }
}
