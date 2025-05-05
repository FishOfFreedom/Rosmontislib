package com.freefish.rosmontislib.event;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.event.packet.toclient.CameraShakeMessage;
import com.freefish.rosmontislib.event.packet.toclient.GUIOpenMessage;
import com.freefish.rosmontislib.event.packet.toclient.SPacketUIWidgetUpdate;
import com.freefish.rosmontislib.event.packet.toserver.CPacketUIClientAction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServerNetwork {
    private static int nextMessageId = 0;

    public static void initNetwork() {
        final String VERSION = "1";
        RosmontisLib.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(RosmontisLib.MOD_ID, "net"))
                .networkProtocolVersion(() -> VERSION)
                .clientAcceptedVersions(VERSION::equals)
                .serverAcceptedVersions(VERSION::equals)
                .simpleChannel();

        registerMessage(CameraShakeMessage.class, CameraShakeMessage::serialize, CameraShakeMessage::deserialize, new CameraShakeMessage.Handler());
        registerMessage(GUIOpenMessage.class, GUIOpenMessage::serialize, GUIOpenMessage::deserialize, new GUIOpenMessage.Handler());
        registerMessage(SPacketUIWidgetUpdate.class, SPacketUIWidgetUpdate::serialize, SPacketUIWidgetUpdate::deserialize, new SPacketUIWidgetUpdate.Handler());
        registerMessage(CPacketUIClientAction.class, CPacketUIClientAction::serialize, CPacketUIClientAction::deserialize, new CPacketUIClientAction.Handler());
    }

    private static  <MSG> void registerMessage(final Class<MSG> clazz, final BiConsumer<MSG, FriendlyByteBuf> encoder, final Function<FriendlyByteBuf, MSG> decoder, final BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
        RosmontisLib.NETWORK.messageBuilder(clazz, nextMessageId++)
                .encoder(encoder).decoder(decoder)
                .consumerNetworkThread(consumer)
                .add();

    }

    public static <MSG> void toClientMessage(LivingEntity entity, MSG message){
        RosmontisLib.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    public static <MSG> void toServerMessage(MSG message){
        RosmontisLib.NETWORK.sendToServer(message);
    }
}
