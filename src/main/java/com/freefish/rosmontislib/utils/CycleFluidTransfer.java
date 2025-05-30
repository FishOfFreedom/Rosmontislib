/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CycleFluidTransfer implements IFluidTransfer {
    private List<List<FluidStack>> stacks;

    public CycleFluidTransfer(List<List<FluidStack>> stacks) {
        this.updateStacks(stacks);
    }

    public void updateStacks(List<List<FluidStack>> stacks) {
        this.stacks = stacks;
    }

    @Override
    public int getTanks() {
        return this.stacks.size();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        List<FluidStack> stackList = this.stacks.get(tank);
        return stackList != null && !stackList.isEmpty() ? stackList.get(Math.abs((int)(System.currentTimeMillis() / 1000L) % stackList.size())) : FluidStack.empty();
    }

    @Override
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
        if (tank >= 0 && tank < this.stacks.size()) {
            this.stacks.set(tank, List.of(fluidStack));
        }
    }

    @Override
    public long getTankCapacity(int tank) {
        return this.getFluidInTank(tank).getAmount();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        return 0;
    }

    @Override
    public boolean supportsFill(int tank) {
        return false;
    }

    @NotNull
    @Override
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        return FluidStack.empty();
    }

    @Override
    public boolean supportsDrain(int tank) {
        return false;
    }

    public List<FluidStack> getStackList(int i) {
        return this.stacks.get(i);
    }


    @Override
    public @NotNull Object createSnapshot() {
        return new Object();
    }

    @Override
    public void restoreFromSnapshot(Object snapshot) {

    }
}
