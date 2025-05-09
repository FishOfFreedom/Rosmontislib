package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.levelentity.LevelEntity;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import com.freefish.rosmontislib.levelentity.sync.SynchedLevelEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SetLevelEntityDataMessage {
    private List<SynchedLevelEntityData.DataValue<?>> pValues;
    private int id;

    public SetLevelEntityDataMessage() {

    }

    public SetLevelEntityDataMessage(List<SynchedLevelEntityData.DataValue<?>> pValues,int id) {
        this.pValues = pValues;
        this.id = id;
    }

    public static void serialize(final SetLevelEntityDataMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.id);

        for(SynchedLevelEntityData.DataValue<?> datavalue : message.pValues) {
            datavalue.write(buf);
        }

        buf.writeByte(255);
    }

    public static SetLevelEntityDataMessage deserialize(final FriendlyByteBuf buf) {
        final SetLevelEntityDataMessage message = new SetLevelEntityDataMessage();
        message.id = buf.readVarInt();

        List<SynchedLevelEntityData.DataValue<?>> list = new ArrayList<>();

        int i;
        while((i = buf.readUnsignedByte()) != 255) {
            list.add(SynchedLevelEntityData.DataValue.read(buf, i));
        }

        message.pValues = list;

        return message;
    }

    public static class Handler implements BiConsumer<SetLevelEntityDataMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final SetLevelEntityDataMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                LevelEntityManager instance = LevelEntityManager.getInstance(level);

                LevelEntity entity = instance.getEntityByID(message.id);
                if (entity != null) {
                    entity.levelEntityData.assignValues(message.pValues);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
