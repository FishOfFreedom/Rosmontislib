package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.event.IHandlerContext;
import com.freefish.rosmontislib.event.IPacket;
import com.freefish.rosmontislib.levelentity.LevelEntity;
import com.freefish.rosmontislib.levelentity.LevelEntityHandle;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityType;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@NoArgsConstructor
public class CRClientLevelEntityMessage implements IPacket {
    private boolean isCreate;

    private int id;
    private LevelEntityType<?> levelEntityType;

    public CRClientLevelEntityMessage(boolean isCreate, LevelEntity levelEntity) {
        this.isCreate = isCreate;
        this.id = levelEntity.id;
        this.levelEntityType = levelEntity.getLevelEntityType();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isCreate);
        if(isCreate){
            buf.writeVarInt(id);
            buf.writeResourceLocation(LevelEntityHandle.getKey(levelEntityType));
        }else {
            buf.writeVarInt(id);
        }
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        isCreate = buf.readBoolean();
        if(isCreate){
            id = buf.readVarInt();
            levelEntityType = LevelEntityHandle.getLevelEntityType(buf.readResourceLocation());
        }else {
            id = buf.readVarInt();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        if(handler.isClient()){
            var level = handler.getLevel();
            if (level != null) {
                LevelEntityManager instance = LevelEntityManager.getInstance(level);
                if (isCreate) {
                    LevelEntity levelEntity = levelEntityType.createLevelEntity();
                    levelEntity.id = id;
                    levelEntity.setLevel(level);
                    instance.addLevelEntity(levelEntity);
                } else {
                    LevelEntity entityByID = instance.getEntityByID(id);
                    if (entityByID != null) {
                        instance.removeLevelEntity(entityByID);
                    }
                }
            }
        }
    }
}
