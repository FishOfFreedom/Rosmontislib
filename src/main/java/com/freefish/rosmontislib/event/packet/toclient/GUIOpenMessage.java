package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.event.IHandlerContext;
import com.freefish.rosmontislib.event.IPacket;
import com.freefish.rosmontislib.gui.factory.UIFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@NoArgsConstructor
public class GUIOpenMessage implements IPacket {
    private ResourceLocation uiFactoryId;
    private FriendlyByteBuf serializedHolder;
    private int windowId;

    public GUIOpenMessage(ResourceLocation uiFactoryId, FriendlyByteBuf serializedHolder, int windowId) {
        this.uiFactoryId = uiFactoryId;
        this.serializedHolder = serializedHolder;
        this.windowId = windowId;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(serializedHolder.readableBytes());
        buf.writeBytes(serializedHolder);

        buf.writeResourceLocation(uiFactoryId);
        buf.writeVarInt(windowId);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        ByteBuf directSliceBuffer = buf.readBytes(buf.readVarInt());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        this.serializedHolder = new FriendlyByteBuf(copiedDataBuffer);

        this.uiFactoryId = buf.readResourceLocation();
        this.windowId = buf.readVarInt();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        if (handler.isClient()) {
            UIFactory<?> uiFactory = UIFactory.FACTORIES.get(uiFactoryId);
            if (uiFactory != null) {
                uiFactory.initClientUI(serializedHolder, windowId);
            }
        }
    }
}
