package com.freefish.rosmontislib.example.block;

import com.freefish.rosmontislib.example.block.blockentity.EntitySpawnerBlockEntity;
import com.freefish.rosmontislib.example.init.BlockEntityHandle;
import com.freefish.rosmontislib.example.syncdata.SyncData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class EntitySpawnerBlock extends BaseEntityBlock {
    public EntitySpawnerBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EntitySpawnerBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityHandle.ENTITY_SPAWNER_ENTITY.get(), EntitySpawnerBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof EntitySpawnerBlockEntity) {
                SyncData syncData = ((EntitySpawnerBlockEntity)entity).getSyncData();
                syncData.a+=1;
                System.out.println(syncData);
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (EntitySpawnerBlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }else {
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
}