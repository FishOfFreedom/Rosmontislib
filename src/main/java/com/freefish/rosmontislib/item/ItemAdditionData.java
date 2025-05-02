package com.freefish.rosmontislib.item;

import com.freefish.rosmontislib.sync.IPersistedSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.function.Supplier;

public interface ItemAdditionData <T extends IPersistedSerializable>{

    String getSaveDataName();

    default T getItemAdditionData(ItemStack itemStack, Supplier<T> factory){
        CompoundTag orCreateTag = itemStack.getOrCreateTag();
        T t = factory.get();
        if(orCreateTag.contains(getSaveDataName())){
            t.deserializeNBT(orCreateTag.getCompound(getSaveDataName()));
        }
        return t;
    }

    default void setItemAdditionData(ItemStack itemStack, T t){
        CompoundTag orCreateTag = itemStack.getOrCreateTag();
        if(orCreateTag.contains(getSaveDataName())){
            orCreateTag.remove(getSaveDataName());
        }
        orCreateTag.put(getSaveDataName(),t.serializeNBT());
    }

    void addAdditionTextTool(ItemTooltipEvent event);

}
