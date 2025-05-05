package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.gui.modular.ModularUIGuiContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SPacketUIWidgetUpdate {

    public int windowId;

    public FriendlyByteBuf updateData;

    public SPacketUIWidgetUpdate() {
    }

    public SPacketUIWidgetUpdate(int windowId, FriendlyByteBuf updateData) {
        this.windowId = windowId;
        this.updateData = updateData;
    }

    public static void serialize(final SPacketUIWidgetUpdate message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.updateData.readableBytes());
        buf.writeBytes(message.updateData);

        buf.writeVarInt(message.windowId);
    }

    public static SPacketUIWidgetUpdate deserialize(final FriendlyByteBuf buf) {
        SPacketUIWidgetUpdate message = new SPacketUIWidgetUpdate();

        ByteBuf directSliceBuffer = buf.readBytes(buf.readVarInt());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        message.updateData = new FriendlyByteBuf(copiedDataBuffer);

        message.windowId = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<SPacketUIWidgetUpdate, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final SPacketUIWidgetUpdate message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Screen currentScreen = Minecraft.getInstance().screen;
                if (currentScreen instanceof ModularUIGuiContainer rContainerScreen) {
                    rContainerScreen.handleWidgetUpdate(message);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
