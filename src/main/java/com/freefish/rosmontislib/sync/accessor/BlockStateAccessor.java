package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author KilaBash
 * @date 2022/9/7
 * @implNote BlockStateAccessor
 */
public class BlockStateAccessor extends CustomObjectAccessor<BlockState>{

    public BlockStateAccessor() {
        super(BlockState.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, BlockState value) {
        return NbtTagPayload.of(NbtUtils.writeBlockState(value));
    }

    @Override
    public BlockState deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload nbtTagPayload) {
            return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), (CompoundTag)nbtTagPayload.getPayload());
        }
        return null;
    }
}
