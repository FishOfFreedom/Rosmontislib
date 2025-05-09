package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.levelentity.LevelEntity;
import com.freefish.rosmontislib.levelentity.LevelEntityHandle;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CRClientLevelEntityMessage {
    private boolean isCreate;

    private int id;
    private LevelEntityType<?> levelEntityType;

    public CRClientLevelEntityMessage() {

    }

    public CRClientLevelEntityMessage(boolean isCreate, LevelEntity levelEntity) {
        this.isCreate = isCreate;
        this.id = levelEntity.id;
        this.levelEntityType = levelEntity.getLevelEntityType();
    }

    public static void serialize(final CRClientLevelEntityMessage message, final FriendlyByteBuf buf) {
        buf.writeBoolean(message.isCreate);
        if(message.isCreate){
            buf.writeVarInt(message.id);
            buf.writeResourceLocation(LevelEntityHandle.getKey(message.levelEntityType));
        }else {
            buf.writeVarInt(message.id);
        }
    }

    public static CRClientLevelEntityMessage deserialize(final FriendlyByteBuf buf) {
        final CRClientLevelEntityMessage message = new CRClientLevelEntityMessage();
        message.isCreate = buf.readBoolean();
        if(message.isCreate){
            message.id = buf.readVarInt();
            message.levelEntityType = LevelEntityHandle.getLevelEntityType(buf.readResourceLocation());
        }else {
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
                    if(message.isCreate){
                        LevelEntity levelEntity = message.levelEntityType.createLevelEntity();
                        levelEntity.id = message.id;
                        levelEntity.setLevel(level);
                        instance.addLevelEntity(levelEntity);
                    }else {
                        LevelEntity entityByID = instance.getEntityByID(message.id);
                        if(entityByID != null){
                            instance.removeLevelEntity(entityByID);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
