package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.event.RLNetworking;
import com.freefish.rosmontislib.event.packet.toclient.CRClientLevelEntityMessage;
import com.freefish.rosmontislib.levelentity.LevelEntity;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityManagerServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "sendLevelInfo", at = @At(value = "RETURN"))
    private void sendContext(ServerPlayer playerIn, ServerLevel worldIn, CallbackInfo ci) {
        LevelEntityManager instance = LevelEntityManager.getInstance(worldIn);
        if(instance instanceof LevelEntityManagerServer levelEntityManagerServer){
            for(LevelEntity level: levelEntityManagerServer.getLevelEntityList()){
                RLNetworking.NETWORK.sendToAll(new CRClientLevelEntityMessage(true,level));
            }
        }
    }
}