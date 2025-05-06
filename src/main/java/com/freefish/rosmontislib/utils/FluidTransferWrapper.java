package com.freefish.rosmontislib.utils;

import lombok.Getter;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public record FluidTransferWrapper(@Getter IFluidHandler handler) implements IFluidTransfer {

    @Override
    public int getTanks() {
        return handler.getTanks();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return FluidHelper.toFluidStack(handler.getFluidInTank(tank));
    }

    @Override
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
        throw new NotImplementedException("try to set fluid for a FluidTransfer wrapper");
    }

    @Override
    public long getTankCapacity(int tank) {
        return handler.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return handler.isFluidValid(tank, FluidHelper.toFluidStack(stack));
    }

    @Override
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        throw new NotImplementedException("try to fill fluid for a FluidTransfer wrapper with a specific tank");
    }

    @Override
    public long fill(FluidStack resource, boolean simulate, boolean notifyChange) {
        return handler.fill(FluidHelper.toFluidStack(resource), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, boolean simulate, boolean notifyChange) {
        return FluidHelper.toFluidStack(handler.drain(FluidHelper.toFluidStack(resource), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @NotNull
    @Override
    public Object createSnapshot() {
        return new Object();
    }

    @Override
    public void restoreFromSnapshot(Object snapshot) {

    }

    @Override
    public boolean supportsFill(int tank) {
        // IFluidHandler doesn't support this check natively.
        return true;
    }

    @NotNull
    @Override
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        throw new NotImplementedException("try to drain fluid for a FluidTransfer wrapper with a specific tank");
    }

    @Override
    public boolean supportsDrain(int tank) {
        // IFluidHandler doesn't support this check natively.
        return true;
    }
}