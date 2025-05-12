package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.event.RLNetworking;
import com.freefish.rosmontislib.event.packet.toclient.CRClientLevelEntityMessage;
import com.freefish.rosmontislib.levelentity.sync.SavedLevelEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class LevelEntityManagerServer extends LevelEntityManager{
    private List<LevelEntity> levelEntityList = new ArrayList<>();
    private final SavedLevelEntityData savedData;
    private final ServerLevel serverLevel;

    public LevelEntityManagerServer(ServerLevel serverLevel) {
        savedData = SavedLevelEntityData.get(serverLevel);
        this.serverLevel = serverLevel;
        deserializeNBT(savedData.getLevelEntityManager());
    }

    @Override
    public LevelEntity getEntityByID(int id) {
        Optional<LevelEntity> first = levelEntityList.stream()
                .filter(levelEntity -> levelEntity.id == id)
                .findFirst();
        return first.orElse(null);
    }

    @Override
    public void addLevelEntity(LevelEntity levelEntity) {
        levelEntity.defineSynchedData();
        levelEntityList.add(levelEntity);
        levelEntity.onAddFromWorld();
    }

    @Override
    public void removeLevelEntity(LevelEntity levelEntity) {
        levelEntityList.remove(levelEntity);
    }

    @Override
    public void tick() {
        Iterator<LevelEntity> iterator = levelEntityList.iterator();
        while (iterator.hasNext()){
            LevelEntity next = iterator.next();
            next.tick();
            if (next.isRemove()) {
                next.onRemoveFromWorld();
                RLNetworking.NETWORK.sendToAll(new CRClientLevelEntityMessage(false,next));
                iterator.remove();
            }
        }
    }

    @Override
    public List<LevelEntity> levelEntityList() {
        return levelEntityList;
    }

    public void deserializeNBT(CompoundTag tag) {
        ListTag listtag = tag.getList("LevelEntities", 10);
        for (int i = 0; i < listtag.size(); i++) {
            CompoundTag compound = listtag.getCompound(i);
            addLevelEntity(LevelEntityType.loadEntityRecursive(compound,serverLevel));
        }
    }

    public void serializeNBT() {
        ListTag listtag = new ListTag();
        levelEntityList.forEach((levelEntity) -> {
            CompoundTag compoundtag1 = new CompoundTag();
            try {
                if (levelEntity.save(compoundtag1)) {
                    listtag.add(compoundtag1);
                }
            } catch (Exception e) {
                RosmontisLib.LOGGER.error("An Entity type {} has thrown an exception trying to write state. It will not persist. Report this to the mod author", levelEntity.getLevelEntityType(), e);
            }

        });
        CompoundTag compoundtag = NbtUtils.addCurrentDataVersion(new CompoundTag());
        compoundtag.put("LevelEntities", listtag);

        savedData.setLevelEntityManager(compoundtag);
    }
}
