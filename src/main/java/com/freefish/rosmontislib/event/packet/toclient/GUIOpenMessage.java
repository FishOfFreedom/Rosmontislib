package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.gui.factory.UIFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class GUIOpenMessage {
    private ResourceLocation uiFactoryId;
    private FriendlyByteBuf serializedHolder;
    private int windowId;

    public GUIOpenMessage() {

    }

    public GUIOpenMessage(ResourceLocation uiFactoryId, FriendlyByteBuf serializedHolder, int windowId) {
        this.uiFactoryId = uiFactoryId;
        this.serializedHolder = serializedHolder;
        this.windowId = windowId;
    }

    public static void serialize(final GUIOpenMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.serializedHolder.readableBytes());
        buf.writeBytes(message.serializedHolder);

        buf.writeResourceLocation(message.uiFactoryId);
        buf.writeVarInt(message.windowId);
    }

    public static GUIOpenMessage deserialize(final FriendlyByteBuf buf) {
        final GUIOpenMessage message = new GUIOpenMessage();
        ByteBuf directSliceBuffer = buf.readBytes(buf.readVarInt());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        message.serializedHolder = new FriendlyByteBuf(copiedDataBuffer);

        message.uiFactoryId = buf.readResourceLocation();
        message.windowId = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<GUIOpenMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final GUIOpenMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                UIFactory<?> uiFactory = UIFactory.FACTORIES.get(message.uiFactoryId);
                if (uiFactory != null) {
                    uiFactory.initClientUI(message.serializedHolder, message.windowId);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
