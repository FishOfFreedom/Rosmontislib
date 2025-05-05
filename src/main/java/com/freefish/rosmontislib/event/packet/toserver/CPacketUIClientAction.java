package com.freefish.rosmontislib.event.packet.toserver;

import com.freefish.rosmontislib.gui.modular.ModularUIContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CPacketUIClientAction {

    public int windowId;
    public FriendlyByteBuf updateData;

    public CPacketUIClientAction() {
    }

    public CPacketUIClientAction(int windowId, FriendlyByteBuf updateData) {
        this.windowId = windowId;
        this.updateData = updateData;
    }

    public static void serialize(final CPacketUIClientAction message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.updateData.readableBytes());
        buf.writeBytes(message.updateData);

        buf.writeVarInt(message.windowId);
    }

    public static CPacketUIClientAction deserialize(final FriendlyByteBuf buf) {
        CPacketUIClientAction message = new CPacketUIClientAction();
        ByteBuf directSliceBuffer = buf.readBytes(buf.readVarInt());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);

        directSliceBuffer.release();
        message.updateData = new FriendlyByteBuf(copiedDataBuffer);

        message.windowId = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<CPacketUIClientAction, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final CPacketUIClientAction message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                if(player!=null){
                    AbstractContainerMenu openContainer = player.containerMenu;
                    if (openContainer instanceof ModularUIContainer) {
                        ((ModularUIContainer)openContainer).handleClientAction(message);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
