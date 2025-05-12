package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.event.IHandlerContext;
import com.freefish.rosmontislib.event.IPacket;
import com.freefish.rosmontislib.levelentity.LevelEntity;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import com.freefish.rosmontislib.levelentity.sync.SynchedLevelEntityData;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class SetLevelEntityDataMessage implements IPacket {
    private List<SynchedLevelEntityData.DataValue<?>> pValues;
    private int id;

    public SetLevelEntityDataMessage(List<SynchedLevelEntityData.DataValue<?>> pValues,int id) {
        this.pValues = pValues;
        this.id = id;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(id);
        for(SynchedLevelEntityData.DataValue<?> datavalue : pValues) {
            datavalue.write(buf);
        }
        buf.writeByte(255);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        id = buf.readVarInt();
        List<SynchedLevelEntityData.DataValue<?>> list = new ArrayList<>();
        int i;
        while((i = buf.readUnsignedByte()) != 255) {
            list.add(SynchedLevelEntityData.DataValue.read(buf, i));
        }
        pValues = list;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        if (handler.isClient()) {
            var level = handler.getLevel();
            if (level != null) {
                LevelEntityManager instance = LevelEntityManager.getInstance(level);
                LevelEntity entity = instance.getEntityByID(id);
                if (entity != null) {
                    entity.levelEntityData.assignValues(pValues);
                }
            }
        }
    }
}
