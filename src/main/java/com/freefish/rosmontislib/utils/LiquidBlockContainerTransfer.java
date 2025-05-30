/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class LiquidBlockContainerTransfer implements IFluidTransfer {
    protected final LiquidBlockContainer liquidContainer;
    protected final Level world;
    protected final BlockPos blockPos;

    public LiquidBlockContainerTransfer(LiquidBlockContainer liquidContainer, Level world, BlockPos blockPos) {
        this.liquidContainer = liquidContainer;
        this.world = world;
        this.blockPos = blockPos;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return FluidStack.empty();
    }

    @Override
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
        fill(0, fluidStack, false, false);
    }

    @Override
    public long getTankCapacity(int tank) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        return FluidStack.empty();
    }

    @Override
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        // NOTE: "Filling" means placement in this context!
        if (resource.getAmount() >= FluidHelper.getBucket()) {
            BlockState state = world.getBlockState(blockPos);
            if (liquidContainer.canPlaceLiquid(world, blockPos, state, resource.getFluid())) {
                //If we are executing try to actually fill the container, if it failed return that we failed
                if (simulate || liquidContainer.placeLiquid(world, blockPos, state, resource.getFluid().defaultFluidState())) {
                    return FluidHelper.getBucket();
                }
            }
        }
        return 0;
    }

    @Override
    public boolean supportsFill(int tank) {
        return true;
    }

    @Override
    public boolean supportsDrain(int tank) {
        return false;
    }

    @NotNull
    @Override
    public Object createSnapshot() {
        return world.getBlockState(blockPos);
    }

    @Override
    public void restoreFromSnapshot(Object snapshot) {
        if (snapshot instanceof BlockState state) {
            world.setBlockAndUpdate(blockPos, state);
        }
    }

    public static class BlockWrapper implements IFluidTransfer {

        protected final BlockState state;
        protected final Level world;
        protected final BlockPos blockPos;

        public BlockWrapper(BlockState state, Level world, BlockPos blockPos) {
            this.state = state;
            this.world = world;
            this.blockPos = blockPos;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return FluidStack.empty();
        }

        @Override
        public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
            fill(0, fluidStack, false, false);
        }

        @Override
        public long getTankCapacity(int tank) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return true;
        }

        @Nonnull
        @Override
        public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
            return FluidStack.empty();
        }

        @Override
        public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
            // NOTE: "Filling" means placement in this context!
            if (resource.getAmount() < FluidHelper.getBucket()) {
                return 0;
            }
            if (!simulate) {
                FluidTransferHelper.destroyBlockOnFluidPlacement(world, blockPos);
                world.setBlock(blockPos, state, Block.UPDATE_ALL_IMMEDIATE);
            }
            return FluidHelper.getBucket();
        }

        @Override
        public boolean supportsFill(int tank) {
            return true;
        }

        @Override
        public boolean supportsDrain(int tank) {
            return false;
        }

        @NotNull
        @Override
        public Object createSnapshot() {
            return state;
        }

        @Override
        public void restoreFromSnapshot(Object snapshot) {
            world.setBlockAndUpdate(blockPos, state);
        }
    }
}
