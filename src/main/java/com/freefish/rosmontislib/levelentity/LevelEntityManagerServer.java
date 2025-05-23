package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.event.RLNetworking;
import com.freefish.rosmontislib.event.packet.toclient.CRClientLevelEntityMessage;
import com.freefish.rosmontislib.levelentity.sync.SavedLevelEntityData;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public class LevelEntityManagerServer extends LevelEntityManager{
    @Getter
    private List<LevelEntity> levelEntityList = new ArrayList<>();
    private final SavedLevelEntityData savedData;
    private final ServerLevel serverLevel;
    public final Map<InstanceLevelEntityType<?>,LevelEntity> instanceLevelEntityTypeLevelEntityMap = new HashMap<>();

    private int index;

    public LevelEntityManagerServer(ServerLevel serverLevel) {
        savedData = SavedLevelEntityData.get(serverLevel);
        this.serverLevel = serverLevel;

        loadInstanceLevelEntity();

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
        levelEntity.id = index++;
        levelEntity.setLevel(serverLevel);
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

    @Override
    public <T extends LevelEntity> T getInstanceLevelEntity(InstanceLevelEntityType<T> instanceLevelEntityType) {
        return (T) instanceLevelEntityTypeLevelEntityMap.computeIfAbsent(instanceLevelEntityType,(instanceLevelEntityTypeMap -> null));
    }

    public void deserializeNBT(CompoundTag tag) {
        ListTag listtag = tag.getList("LevelEntities", 10);

        ListTag instancelisttag = tag.getList("INLevelEntities", 10);

        for (int i = 0; i < listtag.size(); i++) {
            CompoundTag compound = listtag.getCompound(i);
            addLevelEntity(LevelEntityType.loadEntityRecursive(compound,serverLevel));
        }

        for (int i = 0; i < instancelisttag.size(); i++) {
            CompoundTag compound = instancelisttag.getCompound(i);
            String id = compound.getString("id");
            String[] parts = id.split(":");
            if(parts.length==2){
                ResourceLocation resourceLocation = new ResourceLocation(parts[0], parts[1]);
                LevelEntityType<?> levelEntityType = LevelEntityHandle.getLevelEntityType(resourceLocation);
                if (levelEntityType instanceof InstanceLevelEntityType<?>) {
                    LevelEntity levelEntity = instanceLevelEntityTypeLevelEntityMap.get(levelEntityType);
                    levelEntity.load(compound);
                }
            }
            else {
                RosmontisLib.LOGGER.warn("when deserializeNBT levelentity resourceLocation no Find :" + id);
            }
        }
    }

    public void serializeNBT() {
        ListTag listtag = new ListTag();

        ListTag instancelisttag = new ListTag();

        levelEntityList.forEach((levelEntity) -> {
            if(!levelEntity.isInstance()){
                CompoundTag compoundtag1 = new CompoundTag();
                try {
                    if (levelEntity.save(compoundtag1)) {
                        listtag.add(compoundtag1);
                    }
                } catch (Exception e) {
                    RosmontisLib.LOGGER.error("An Entity type {} has thrown an exception trying to write state. It will not persist. Report this to the mod author", levelEntity.getLevelEntityType(), e);
                }
            }else {
                CompoundTag compoundtag1 = new CompoundTag();
                try {
                    if (levelEntity.save(compoundtag1)) {
                        instancelisttag.add(compoundtag1);
                    }
                } catch (Exception e) {
                    RosmontisLib.LOGGER.error("An Entity type {} has thrown an exception trying to write state. It will not persist. Report this to the mod author", levelEntity.getLevelEntityType(), e);
                }
            }
        });

        CompoundTag compoundtag = NbtUtils.addCurrentDataVersion(new CompoundTag());
        compoundtag.put("LevelEntities", listtag);
        compoundtag.put("INLevelEntities", instancelisttag);

        savedData.setLevelEntityManager(compoundtag);
    }

    private void loadInstanceLevelEntity(){
        List<InstanceLevelEntityType<?>> instanceList = InstanceLevelEntityType.instanceList;

        for(InstanceLevelEntityType<?> instanceLevelEntityType:instanceList){
            LevelEntity levelEntity = instanceLevelEntityType.createLevelEntity();
            levelEntity.setInstance(true);
            addLevelEntity(levelEntity);
            instanceLevelEntityTypeLevelEntityMap.put(instanceLevelEntityType,levelEntity);
        }
    };
}
