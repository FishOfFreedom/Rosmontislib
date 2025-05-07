package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.StringPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * @author KilaBash
 * @date 2022/9/7
 * @implNote BlockStateAccessor
 */
public class ResourceLocationAccessor extends CustomObjectAccessor<ResourceLocation>{

    public ResourceLocationAccessor() {
        super(ResourceLocation.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, ResourceLocation value) {
        return StringPayload.of(value.toString());
    }

    @Override
    public ResourceLocation deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof StringPayload stringPayload) {
            return new ResourceLocation(stringPayload.getPayload());
        }
        return null;
    }
}
