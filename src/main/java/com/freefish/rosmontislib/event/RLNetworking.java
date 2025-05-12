package com.freefish.rosmontislib.event;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.event.packet.toclient.*;
import com.freefish.rosmontislib.event.packet.toserver.CPacketUIClientAction;
import net.minecraft.resources.ResourceLocation;

public class RLNetworking {
    public static Networking NETWORK;

    public static void init() {
        NETWORK = new Networking(new ResourceLocation(RosmontisLib.MOD_ID, "networking"), "1.0.0");

        NETWORK.registerS2C(CameraShakeMessage.class);
        NETWORK.registerS2C(SPacketUIWidgetUpdate.class);
        NETWORK.registerS2C(CRClientLevelEntityMessage.class);
        NETWORK.registerS2C(GUIOpenMessage.class);
        NETWORK.registerS2C(SetLevelEntityDataMessage.class);

        NETWORK.registerC2S(CPacketUIClientAction.class);
    }

}
