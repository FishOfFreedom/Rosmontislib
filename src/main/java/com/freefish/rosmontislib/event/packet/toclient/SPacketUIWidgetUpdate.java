package com.freefish.rosmontislib.event.packet.toclient;

import com.freefish.rosmontislib.event.IHandlerContext;
import com.freefish.rosmontislib.event.IPacket;
import com.freefish.rosmontislib.gui.modular.ModularUIGuiContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@NoArgsConstructor
public class SPacketUIWidgetUpdate implements IPacket {
    public int windowId;
    public FriendlyByteBuf updateData;

    public SPacketUIWidgetUpdate(int windowId, FriendlyByteBuf updateData) {
        this.windowId = windowId;
        this.updateData = updateData;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(updateData.readableBytes());
        buf.writeBytes(updateData);

        buf.writeVarInt(windowId);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        ByteBuf directSliceBuffer = buf.readBytes(buf.readVarInt());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        updateData = new FriendlyByteBuf(copiedDataBuffer);

        windowId = buf.readVarInt();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        if (handler.isClient()) {
            Screen currentScreen = Minecraft.getInstance().screen;
            if (currentScreen instanceof ModularUIGuiContainer rContainerScreen) {
                rContainerScreen.handleWidgetUpdate(this);
            }
        }
    }
}
