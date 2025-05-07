package com.freefish.rosmontislib.sync.accessor;


import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.core.Registry;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class BuiltinRegistryAccessor<T> extends CustomObjectAccessor<T>{

    Registry<T> registry;

    public BuiltinRegistryAccessor(Class<T> clazz, Registry<T> registry) {
        super(clazz, true);
        this.registry = registry;
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, T value) {
        return NbtTagPayload.of(StringTag.valueOf(Optional.ofNullable(registry.getKey(value))
                .map(ResourceLocation::toString).orElse("")));
    }

    @Override
    public T deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload nbtTagPayload) {
            var key = nbtTagPayload.getPayload().getAsString();
            return key.isEmpty() ? null : registry.get(new ResourceLocation(key));
        }
        return null;
    }
}
