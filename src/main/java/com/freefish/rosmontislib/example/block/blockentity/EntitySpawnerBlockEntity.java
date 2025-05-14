package com.freefish.rosmontislib.example.block.blockentity;

import com.freefish.rosmontislib.example.gui.screen.EntitySpawnerMenu;
import com.freefish.rosmontislib.example.init.BlockEntityHandle;
import com.freefish.rosmontislib.example.syncdata.SyncData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntitySpawnerBlockEntity extends BlockEntity implements MenuProvider {
    public List<UUID> spawnedEntityUUIDs = new ArrayList<>();
    public List<Wave> waveList = new ArrayList<>();
    public Wave currentWave;
    public int tickCount = -1;
    public boolean isStart;
    public SyncData syncData= new SyncData();

    public EntitySpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityHandle.ENTITY_SPAWNER_ENTITY.get(), pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag uuidListTag = new CompoundTag();
        for (int i = 0; i < waveList.size(); i++) {
            uuidListTag.put("wavedata" + i, waveList.get(i).save());
        }
        tag.put("wavedata", uuidListTag);

        tag.put("syyy",getSyncData().serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag wavedata = tag.getCompound("wavedata");
        for (String key : wavedata.getAllKeys()) {
            waveList.add(Wave.load(wavedata.getCompound(key)));
        }
        SyncData syncData1 = new SyncData();
        CompoundTag sy = tag.getCompound("syyy");
        syncData1.deserializeNBT(sy);
        setSyncData(syncData1);
    }

    public SyncData getSyncData() {
        return syncData;
    }

    public void setSyncData(SyncData syncData) {
        this.syncData=syncData;
    }

    public void startSpawning(SummonType summonType) {
        if (level instanceof ServerLevel serverLevel) {
            RegistryAccess registryAccess = serverLevel.registryAccess();
            EntityType<?> entityType = registryAccess.registryOrThrow(Registries.ENTITY_TYPE).getOptional(new ResourceLocation(summonType.entity_type)).orElse(null);
            if (entityType == null) {
                return;
            }

            for (int i = 0; i < summonType.summonCount; i++) {
                Entity entity = entityType.create(serverLevel);
                if (entity != null) {
                    entity.moveTo(worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, level.random.nextFloat() * 360F, 0F);
                    serverLevel.addFreshEntity(entity);
                    spawnedEntityUUIDs.add(entity.getUUID());
                }
            }
        }
    }

    public static void tick(Level level, BlockPos pPos, BlockState pState, EntitySpawnerBlockEntity blockEntity) {
        if(blockEntity.isStart) {
            blockEntity.tickCount++;
        }
        if (level.isClientSide) {
            return;
        }

        if(blockEntity.currentWave==null){
            if(!blockEntity.waveList.isEmpty()){
                blockEntity.currentWave = blockEntity.waveList.get(0);
            }
        }else {
            for(SummonType summonType:blockEntity.currentWave.summonTypeList){
                if(blockEntity.tickCount == summonType.summonTime) {
                    blockEntity.startSpawning(summonType);
                }
            }
        }

        if (!blockEntity.spawnedEntityUUIDs.isEmpty()) {
            blockEntity.spawnedEntityUUIDs.removeIf(uuid -> {
                Entity entity = ((ServerLevel) level).getEntity(uuid);
                return entity == null || !entity.isAlive();
            });

            if (blockEntity.spawnedEntityUUIDs.isEmpty()) {
                blockEntity.onAllEntitiesDead();
            }
        }
    }

    private void onAllEntitiesDead() {
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.tutorialmod.gem_polishing_station");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new EntitySpawnerMenu(pContainerId, pPlayerInventory, this);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public static class Wave{
        List<SummonType> summonTypeList = new ArrayList<>();

        public CompoundTag save(){
            CompoundTag tag = new CompoundTag();
            for (int i = 0; i < summonTypeList.size(); i++) {
                tag.put("wavedata" + i, summonTypeList.get(i).save());
            }
            return tag;
        }

        public static Wave load(CompoundTag tag){
            Wave wave = new Wave();
            for (String key : tag.getAllKeys()) {
                wave.summonTypeList.add(SummonType.load(tag.getCompound(key)));
            }
            return wave;
        }

        public List<SummonType> getSummonTypeList() {
            return summonTypeList;
        }

    }

    public static class SummonType{
        String entity_type;
        int summonTime;
        int summonCount;

        public CompoundTag save(){
            CompoundTag tag = new CompoundTag();
            tag.putString("name",entity_type);
            tag.putInt("time",summonTime);
            tag.putInt("count",summonCount);
            return tag;
        }

        public static SummonType load(CompoundTag tag){
            SummonType summonType = new SummonType();
            summonType.entity_type = tag.getString("name");
            summonType.summonTime = tag.getInt("time");
            summonType.summonCount = tag.getInt("count");
            return  summonType;
        }
    }
}