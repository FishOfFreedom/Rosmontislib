/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.widget;

import com.freefish.rosmontislib.client.utils.Position;
import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.configurator.ConfiguratorGroup;
import com.freefish.rosmontislib.gui.editor.configurator.IConfigurableWidget;
import com.freefish.rosmontislib.gui.editor.configurator.WrapperConfigurator;
import com.freefish.rosmontislib.gui.ingredient.IRecipeIngredientSlot;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.texture.ProgressTexture;
import com.freefish.rosmontislib.gui.texture.ResourceBorderTexture;
import com.freefish.rosmontislib.gui.util.DrawerHelper;
import com.freefish.rosmontislib.gui.util.TextFormattingUtil;
import com.freefish.rosmontislib.utils.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidActionResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
@LDLRegister(name = "fluid_slot", group = "widget.container")
@Accessors(chain = true)
public class TankWidget extends Widget implements IRecipeIngredientSlot, IConfigurableWidget {
    public final static ResourceBorderTexture FLUID_SLOT_TEXTURE = new ResourceBorderTexture("rosmontislib:textures/gui/fluid_slot.png", 18, 18, 1, 1);

    @Nullable
    @Getter
    protected IFluidTransfer fluidTank;
    @Getter
    protected int tank;
    @Configurable(name = "ldlib.gui.editor.name.showAmount")
    @Setter
    protected boolean showAmount;
    @Configurable(name = "ldlib.gui.editor.name.allowClickFilled")
    @Setter
    protected boolean allowClickFilled;
    @Configurable(name = "ldlib.gui.editor.name.allowClickDrained")
    @Setter
    protected boolean allowClickDrained;
    @Configurable(name = "ldlib.gui.editor.name.drawHoverOverlay")
    @Setter
    public boolean drawHoverOverlay = true;
    @Configurable(name = "ldlib.gui.editor.name.drawHoverTips")
    @Setter
    protected boolean drawHoverTips;
    @Configurable(name = "ldlib.gui.editor.name.fillDirection")
    @Setter
    protected ProgressTexture.FillDirection fillDirection = ProgressTexture.FillDirection.ALWAYS_FULL;
    @Setter
    protected BiConsumer<TankWidget, List<Component>> onAddedTooltips;
    //@Setter @Getter
    //protected IngredientIO ingredientIO = IngredientIO.RENDER_ONLY;
    @Setter @Getter
    protected float XEIChance = 1f;
    @Getter
    protected FluidStack lastFluidInTank;
    @Getter
    protected long lastTankCapacity;
    @Setter
    protected Runnable changeListener;

    public TankWidget() {
        this(null, 0, 0, 18, 18, true, true);
    }

    @Override
    public void initTemplate() {
        setBackground(FLUID_SLOT_TEXTURE);
        setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP);
    }

    public TankWidget(IFluidStorage fluidTank, int x, int y, boolean allowClickContainerFilling, boolean allowClickContainerEmptying) {
        this(fluidTank, x, y, 18, 18, allowClickContainerFilling, allowClickContainerEmptying);
    }

    public TankWidget(@Nullable IFluidStorage fluidTank, int x, int y, int width, int height, boolean allowClickContainerFilling, boolean allowClickContainerEmptying) {
        super(new Position(x, y), new Size(width, height));
        setBackground(FLUID_SLOT_TEXTURE);
        this.fluidTank = fluidTank;
        this.tank = 0;
        this.showAmount = true;
        this.allowClickFilled = allowClickContainerFilling;
        this.allowClickDrained = allowClickContainerEmptying;
        this.drawHoverTips = true;
    }

    public TankWidget(IFluidTransfer fluidTank, int tank, int x, int y, boolean allowClickContainerFilling, boolean allowClickContainerEmptying) {
        this(fluidTank, tank, x, y, 18, 18, allowClickContainerFilling, allowClickContainerEmptying);
    }

    public TankWidget(@Nullable IFluidTransfer fluidTank, int tank, int x, int y, int width, int height, boolean allowClickContainerFilling, boolean allowClickContainerEmptying) {
        super(new Position(x, y), new Size(width, height));
        setBackground(FLUID_SLOT_TEXTURE);
        this.fluidTank = fluidTank;
        this.tank = tank;
        this.showAmount = true;
        this.allowClickFilled = allowClickContainerFilling;
        this.allowClickDrained = allowClickContainerEmptying;
        this.drawHoverTips = true;
    }

    public TankWidget setFluidTank(IFluidStorage fluidTank) {
        this.fluidTank = fluidTank;
        this.tank = 0;
        if (isClientSideWidget) {
            setClientSideWidget();
        }
        return this;
    }

    public TankWidget setFluidTank(IFluidTransfer fluidTank, int tank) {
        this.fluidTank = fluidTank;
        this.tank = tank;
        if (isClientSideWidget) {
            setClientSideWidget();
        }
        return this;
    }

    public FluidStack getFluid() {
        if (isClientSideWidget || isRemote()) {
            return lastFluidInTank == null ? FluidStack.empty() : lastFluidInTank;
        }
        return fluidTank != null ? fluidTank.getFluidInTank(tank) : FluidStack.empty();
    }

    public TankWidget setFluid(FluidStack fluidStack) {
        return setFluid(fluidStack, true);
    }

    public TankWidget setFluid(FluidStack fluidStack, boolean notify) {
        if (fluidTank != null) {
            fluidTank.setFluidInTank(tank, fluidStack);
            if (notify) {
                detectAndSendChanges();
            }
        }
        return this;
    }

    @Override
    public TankWidget setClientSideWidget() {
        super.setClientSideWidget();
        if (fluidTank != null) {
            this.lastFluidInTank = fluidTank.getFluidInTank(tank).copy();
        } else {
            this.lastFluidInTank = null;
        }
        this.lastTankCapacity = fluidTank != null ? fluidTank.getTankCapacity(tank) : 0;
        return this;
    }

    public TankWidget setBackground(IGuiTexture background) {
        super.setBackground(background);
        return this;
    }

    @Nullable
    @Override
    public Object getXEIIngredientOverMouse(double mouseX, double mouseY) {
        if (self().isMouseOverElement(mouseX, mouseY)) {
            if (lastFluidInTank == null || lastFluidInTank.isEmpty()) return null;

            if (this.fluidTank instanceof CycleFluidTransfer cycleItemStackHandler) {
                return getXEIIngredientsFromCycleTransferClickable(cycleItemStackHandler, tank).get(0);
            } else if (this.fluidTank instanceof TagOrCycleFluidTransfer transfer) {
                return getXEIIngredientsFromTagOrCycleTransferClickable(transfer, tank).get(0);
            }

            //if (LDLib.isJeiLoaded()) {
            //    return JEICallWrapper.getPlatformFluidTypeForJEIClickable(lastFluidInTank, getPosition(), getSize());
            //}
            //if (LDLib.isReiLoaded()) {
            //    return EntryStacks.of(dev.architectury.fluid.FluidStack.create(lastFluidInTank.getFluid(), lastFluidInTank.getAmount(), lastFluidInTank.getTag()));
            //}
            //if (LDLib.isEmiLoaded()) {
            //    return EmiStack.of(lastFluidInTank.getFluid(), lastFluidInTank.getTag(), lastFluidInTank.getAmount()).setChance(XEIChance);
            //}
        }
        return null;
    }

    @Override
    public List<Object> getXEIIngredients() {
        if (lastFluidInTank == null || lastFluidInTank.isEmpty()) return Collections.emptyList();

        if (this.fluidTank instanceof CycleFluidTransfer cycleItemStackHandler) {
            return getXEIIngredientsFromCycleTransferClickable(cycleItemStackHandler, tank);
        } else if (this.fluidTank instanceof TagOrCycleFluidTransfer transfer) {
            return getXEIIngredientsFromTagOrCycleTransferClickable(transfer, tank);
        }

        //if (LDLib.isJeiLoaded()) {
        //    return List.of(JEICallWrapper.getPlatformFluidTypeForJEIClickable(FluidStack.create(lastFluidInTank.getFluid(), lastFluidInTank.getAmount()), getPosition(), getSize()));
        //}
        //if (LDLib.isReiLoaded()) {
        //    return List.of(EntryStacks.of(dev.architectury.fluid.FluidStack.create(lastFluidInTank.getFluid(), lastFluidInTank.getAmount(), lastFluidInTank.getTag())));
        //}
        //if (LDLib.isEmiLoaded()) {
        //    return List.of(EmiStack.of(lastFluidInTank.getFluid(), lastFluidInTank.getTag(), lastFluidInTank.getAmount()).setChance(XEIChance));
        //}
        return List.of(FluidHelper.toRealFluidStack(lastFluidInTank));
    }

    @Override
    public Object getXEICurrentIngredient() {
        if (lastFluidInTank == null || lastFluidInTank.isEmpty()) return null;
        //if (LDLib.isJeiLoaded()) {
        //    return JEICallWrapper.getPlatformFluidTypeForJEIClickable(FluidStack.create(lastFluidInTank.getFluid(), lastFluidInTank.getAmount()), getPosition(), getSize());
        //}
        return null;
    }

    private List<Object> getXEIIngredientsFromCycleTransfer(CycleFluidTransfer transfer, int index) {
        var stream = transfer.getStackList(index).stream();
        //if (LDLib.isJeiLoaded()) {
        //    return stream.filter(fluid -> !fluid.isEmpty()).map(JEICallWrapper::getPlatformFluidTypeForJEI).toList();
        //} else if (LDLib.isReiLoaded()) {
        //    return REICallWrapper.getReiIngredients(stream);
        //} else if (LDLib.isEmiLoaded()) {
        //    return EMICallWrapper.getEmiIngredients(stream, getXEIChance());
        //}
        return Collections.emptyList();
    }

    private List<Object> getXEIIngredientsFromCycleTransferClickable(CycleFluidTransfer transfer, int index) {
        var stream = transfer.getStackList(index).stream();
        //if (LDLib.isJeiLoaded()) {
        //    return stream
        //            .filter(fluid -> !fluid.isEmpty())
        //            .map(fluid -> JEICallWrapper.getPlatformFluidTypeForJEIClickable(fluid, getPosition(), getSize()))
        //            .toList();
        //} else if (LDLib.isReiLoaded()) {
        //    return REICallWrapper.getReiIngredients(stream);
        //} else if (LDLib.isEmiLoaded()) {
        //    return EMICallWrapper.getEmiIngredients(stream, getXEIChance());
        //}
        return Collections.emptyList();
    }

    private List<Object> getXEIIngredientsFromTagOrCycleTransfer(TagOrCycleFluidTransfer transfer, int index) {
        Either<List<Pair<TagKey<Fluid>, Long>>, List<FluidStack>> either = transfer
                .getStacks()
                .get(index);
        var ref = new Object() {
            List<Object> returnValue = Collections.emptyList();
        };
        either.ifLeft(list -> {
            //if (LDLib.isJeiLoaded()) {
            //    ref.returnValue = list.stream()
            //            .flatMap(pair -> BuiltInRegistries.FLUID
            //                    .getTag(pair.getFirst())
            //                    .stream()
            //                    .flatMap(HolderSet.ListBacked::stream)
            //                    .map(fluid -> JEICallWrapper.getPlatformFluidTypeForJEI(FluidStack.create(fluid.value(), pair.getSecond()))))
            //            .collect(Collectors.toList());
            //} else if (LDLib.isReiLoaded()) {
            //    ref.returnValue = REICallWrapper.getReiIngredients(list);
            //} else if (LDLib.isEmiLoaded()) {
            //    ref.returnValue = EMICallWrapper.getEmiIngredients(list, getXEIChance());
            //}
        }).ifRight(fluids -> {
            var stream = fluids.stream();
            //if (LDLib.isJeiLoaded()) {
            //    ref.returnValue = stream.filter(fluid -> !fluid.isEmpty()).map(JEICallWrapper::getPlatformFluidTypeForJEI).toList();
            //} else if (LDLib.isReiLoaded()) {
            //    ref.returnValue = REICallWrapper.getReiIngredients(stream);
            //} else if (LDLib.isEmiLoaded()) {
            //    ref.returnValue = EMICallWrapper.getEmiIngredients(stream, getXEIChance());
            //}
        });
        return ref.returnValue;
    }

    private List<Object> getXEIIngredientsFromTagOrCycleTransferClickable(TagOrCycleFluidTransfer transfer, int index) {
        Either<List<Pair<TagKey<Fluid>, Long>>, List<FluidStack>> either = transfer
                .getStacks()
                .get(index);
        var ref = new Object() {
            List<Object> returnValue = Collections.emptyList();
        };
        either.ifLeft(list -> {
            //if (LDLib.isJeiLoaded()) {
            //    ref.returnValue = list.stream()
            //            .flatMap(pair -> BuiltInRegistries.FLUID
            //                    .getTag(pair.getFirst())
            //                    .stream()
            //                    .flatMap(HolderSet.ListBacked::stream)
            //                    .map(fluid -> JEICallWrapper.getPlatformFluidTypeForJEIClickable(FluidStack.create(fluid.value(), pair.getSecond()), getPosition(), getSize())))
            //            .collect(Collectors.toList());
            //} else if (LDLib.isReiLoaded()) {
            //    ref.returnValue = REICallWrapper.getReiIngredients(list);
            //} else if (LDLib.isEmiLoaded()) {
            //    ref.returnValue = EMICallWrapper.getEmiIngredients(list, getXEIChance());
            //}
        }).ifRight(fluids -> {
            var stream = fluids.stream();
            //if (LDLib.isJeiLoaded()) {
            //    ref.returnValue = stream
            //            .filter(fluid -> !fluid.isEmpty())
            //            .map(fluid -> JEICallWrapper.getPlatformFluidTypeForJEIClickable(fluid, getPosition(), getSize()))
            //            .toList();
            //} else if (LDLib.isReiLoaded()) {
            //    ref.returnValue = REICallWrapper.getReiIngredients(stream);
            //} else if (LDLib.isEmiLoaded()) {
            //    ref.returnValue = EMICallWrapper.getEmiIngredients(stream, getXEIChance());
            //}
        });
        return ref.returnValue;
    }

    @Override
    public List<Component> getTooltipTexts() {
        List<Component> tooltips = getAdditionalToolTips(new ArrayList<>());
        tooltips.addAll(tooltipTexts);
        return tooltips;
    }

    public List<Component> getAdditionalToolTips(List<Component> list) {
        if (this.onAddedTooltips != null) {
            this.onAddedTooltips.accept(this, list);
        }
        return list;
    }

    @Override
    public List<Component> getFullTooltipTexts() {
        var tooltips = new ArrayList<Component>();
        var fluidStack = this.lastFluidInTank;
        if (fluidStack != null && !fluidStack.isEmpty()) {
            tooltips.add(FluidHelper.getDisplayName(fluidStack));
            tooltips.add(Component.translatable("ldlib.fluid.amount", fluidStack.getAmount(), lastTankCapacity).append(" " + FluidHelper.getUnit()));
            tooltips.add(Component.translatable("ldlib.fluid.temperature", FluidHelper.getTemperature(fluidStack)));
            tooltips.add(Component.translatable(FluidHelper.isLighterThanAir(fluidStack) ? "ldlib.fluid.state_gas" : "ldlib.fluid.state_liquid"));
        } else {
            tooltips.add(Component.translatable("ldlib.fluid.empty"));
            tooltips.add(Component.translatable("ldlib.fluid.amount", 0, lastTankCapacity).append(" " + FluidHelper.getUnit()));
        }
        tooltips.addAll(getTooltipTexts());
        return tooltips;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        if (isClientSideWidget && fluidTank != null) {
            FluidStack fluidStack = fluidTank.getFluidInTank(tank);
            long capacity = fluidTank.getTankCapacity(tank);
            if (capacity != lastTankCapacity) {
                this.lastTankCapacity = capacity;
            }
            if (!fluidStack.isFluidEqual(lastFluidInTank)) {
                this.lastFluidInTank = fluidStack.copy();
            } else if (fluidStack.getAmount() != lastFluidInTank.getAmount()) {
                this.lastFluidInTank.setAmount(fluidStack.getAmount());
            }
        }
        Position pos = getPosition();
        Size size = getSize();
        var renderedFluid = lastFluidInTank;
        if (renderedFluid != null) {
            RenderSystem.disableBlend();
            if (!renderedFluid.isEmpty()) {
                double progress = renderedFluid.getAmount() * 1.0 / Math.max(Math.max(renderedFluid.getAmount(), lastTankCapacity), 1);
                float drawnU = (float) fillDirection.getDrawnU(progress);
                float drawnV = (float) fillDirection.getDrawnV(progress);
                float drawnWidth = (float) fillDirection.getDrawnWidth(progress);
                float drawnHeight = (float) fillDirection.getDrawnHeight(progress);
                int width = size.width - 2;
                int height = size.height - 2;
                int x = pos.x + 1;
                int y = pos.y + 1;
                DrawerHelper.drawFluidForGui(graphics, renderedFluid, x + drawnU * width, y + drawnV * height, width * drawnWidth, height * drawnHeight);
            }

            if (showAmount && !renderedFluid.isEmpty()) {
                graphics.pose().pushPose();
                graphics.pose().scale(0.5F, 0.5F, 1);
                String s = TextFormattingUtil.formatLongToCompactStringBuckets(renderedFluid.getAmount(), 3) + "B";
                Font fontRenderer = Minecraft.getInstance().font;
                graphics.drawString(fontRenderer, s, (int) ((pos.x + (size.width / 3f)) * 2 - fontRenderer.width(s) + 21), (int) ((pos.y + (size.height / 3f) + 6) * 2), 0xFFFFFF, true);
                graphics.pose().popPose();
            }

            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
        drawOverlay(graphics, mouseX, mouseY, partialTicks);
        if (drawHoverOverlay && isMouseOverElement(mouseX, mouseY) && getHoverElement(mouseX, mouseY) == this) {
            RenderSystem.colorMask(true, true, true, false);
            DrawerHelper.drawSolidRect(graphics, getPosition().x + 1, getPosition().y + 1, getSize().width - 2, getSize().height - 2, 0x80FFFFFF);
            RenderSystem.colorMask(true, true, true, true);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (drawHoverTips && isMouseOverElement(mouseX, mouseY) && getHoverElement(mouseX, mouseY) == this) {
            if (gui != null) {
                gui.getModularUIGui().setHoverTooltip(getFullTooltipTexts(), ItemStack.EMPTY, null, null);
            }
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1f);
        } else {
            super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void detectAndSendChanges() {
        if (fluidTank != null) {
            FluidStack fluidStack = fluidTank.getFluidInTank(tank);
            long capacity = fluidTank.getTankCapacity(tank);
            if (capacity != lastTankCapacity) {
                this.lastTankCapacity = capacity;
                writeUpdateInfo(0, buffer -> buffer.writeVarLong(lastTankCapacity));
            }
            if (!fluidStack.isFluidEqual(lastFluidInTank)) {
                this.lastFluidInTank = fluidStack.copy();
                CompoundTag fluidStackTag = fluidStack.saveToTag(new CompoundTag());
                writeUpdateInfo(2, buffer -> buffer.writeNbt(fluidStackTag));
            } else if (fluidStack.getAmount() != lastFluidInTank.getAmount()) {
                this.lastFluidInTank.setAmount(fluidStack.getAmount());
                writeUpdateInfo(3, buffer -> buffer.writeVarLong(lastFluidInTank.getAmount()));
            } else {
                super.detectAndSendChanges();
                return;
            }
            if (changeListener != null) {
                changeListener.run();
            }
        }
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        buffer.writeBoolean(fluidTank != null);
        if (fluidTank != null) {
            this.lastTankCapacity = fluidTank.getTankCapacity(tank);
            buffer.writeVarLong(lastTankCapacity);
            FluidStack fluidStack = fluidTank.getFluidInTank(tank);
            this.lastFluidInTank = fluidStack.copy();
            buffer.writeNbt(fluidStack.saveToTag(new CompoundTag()));
        }
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            this.lastTankCapacity = buffer.readVarLong();
            readUpdateInfo(2, buffer);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 0) {
            this.lastTankCapacity = buffer.readVarLong();
        } else if (id == 1) {
            this.lastFluidInTank = null;
        } else if (id == 2) {
            this.lastFluidInTank = FluidStack.loadFromTag(buffer.readNbt());
        } else if (id == 3 && lastFluidInTank != null) {
            this.lastFluidInTank.setAmount(buffer.readVarLong());
        } else if (id == 4) {
            ItemStack currentStack = gui.getModularUIContainer().getCarried();
            int newStackSize = buffer.readVarInt();
            currentStack.setCount(newStackSize);
            gui.getModularUIContainer().setCarried(currentStack);
        } else {
            super.readUpdateInfo(id, buffer);
            return;
        }
        if (changeListener != null) {
            changeListener.run();
        }
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            boolean isShiftKeyDown = buffer.readBoolean();
            int clickResult = tryClickContainer(isShiftKeyDown);
            if (clickResult >= 0) {
                writeUpdateInfo(4, buf -> buf.writeVarInt(clickResult));
            }
        }
    }

    private int tryClickContainer(boolean isShiftKeyDown) {
        if (fluidTank == null) return -1;
        Player player = gui.entityPlayer;
        ItemStack currentStack = gui.getModularUIContainer().getCarried();
        var handler = FluidTransferHelper.getFluidTransfer(gui.entityPlayer, gui.getModularUIContainer());
        if (handler == null) return -1;
        int maxAttempts = isShiftKeyDown ? currentStack.getCount() : 1;
        FluidStack initialFluid = fluidTank.getFluidInTank(tank);
        if (allowClickFilled && initialFluid.getAmount() > 0) {
            boolean performedFill = false;
            for (int i = 0; i < maxAttempts; i++) {
                FluidActionResult result = FluidTransferHelper.tryFillContainer(currentStack, fluidTank, Integer.MAX_VALUE, null, false);
                if (!result.isSuccess()) break;
                ItemStack remainingStack = FluidTransferHelper.tryFillContainer(currentStack, fluidTank, Integer.MAX_VALUE, null, true).getResult();
                currentStack.shrink(1);
                performedFill = true;
                if (!remainingStack.isEmpty() && !player.addItem(remainingStack)) {
                    Block.popResource(player.level(), player.getOnPos(), remainingStack);
                    break;
                }
            }
            if (performedFill) {
                SoundEvent soundevent = FluidHelper.getFillSound(initialFluid);
                if (soundevent != null) {
                    player.level().playSound(null, player.position().x, player.position().y + 0.5, player.position().z, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                gui.getModularUIContainer().setCarried(currentStack);
                return currentStack.getCount();
            }
        }

        if (allowClickDrained) {
            boolean performedEmptying = false;
            for (int i = 0; i < maxAttempts; i++) {
                FluidActionResult result = FluidTransferHelper.tryEmptyContainer(currentStack, fluidTank, Integer.MAX_VALUE, null, false);
                if (!result.isSuccess()) break;
                ItemStack remainingStack = FluidTransferHelper.tryEmptyContainer(currentStack, fluidTank, Integer.MAX_VALUE, null, true).getResult();
                currentStack.shrink(1);
                performedEmptying = true;
                if (!remainingStack.isEmpty() && !player.getInventory().add(remainingStack)) {
                    Block.popResource(player.level(), player.getOnPos(), remainingStack);
                    break;
                }
            }
            var filledFluid = fluidTank.getFluidInTank(tank);
            if (performedEmptying) {
                SoundEvent soundevent = FluidHelper.getEmptySound(filledFluid);
                if (soundevent != null) {
                    player.level().playSound(null, player.position().x, player.position().y + 0.5, player.position().z, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                gui.getModularUIContainer().setCarried(currentStack);
                return currentStack.getCount();
            }
        }

        return -1;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if ((allowClickDrained || allowClickFilled) && isMouseOverElement(mouseX, mouseY)) {
            if (button == 0) {
                if (FluidTransferHelper.getFluidTransfer(gui.entityPlayer, gui.getModularUIContainer()) != null) {
                    boolean isShiftKeyDown = isShiftDown();
                    writeClientAction(1, writer -> writer.writeBoolean(isShiftKeyDown));
                    playButtonClickSound();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void buildConfigurator(ConfiguratorGroup father) {
        var handler = new FluidStorage(5000);
        handler.fill(FluidStack.create(Fluids.WATER, 3000), false);
        father.addConfigurators(new WrapperConfigurator("ldlib.gui.editor.group.preview", new TankWidget() {
            @Override
            public void updateScreen() {
                super.updateScreen();
                setHoverTooltips(TankWidget.this.tooltipTexts);
                this.backgroundTexture = TankWidget.this.backgroundTexture;
                this.hoverTexture = TankWidget.this.hoverTexture;
                this.showAmount = TankWidget.this.showAmount;
                this.drawHoverTips = TankWidget.this.drawHoverTips;
                this.fillDirection = TankWidget.this.fillDirection;
                this.overlay = TankWidget.this.overlay;
            }
        }.setAllowClickDrained(false).setAllowClickFilled(false).setFluidTank(handler)));

        IConfigurableWidget.super.buildConfigurator(father);
    }
}
