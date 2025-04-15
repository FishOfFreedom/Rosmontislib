package com.freefish.rosmontislib.example.gui.screen;

import com.freefish.rosmontislib.example.block.blockentity.EntitySpawnerBlockEntity;
import com.freefish.rosmontislib.example.init.BlockHandle;
import com.freefish.rosmontislib.example.init.MenuHandle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EntitySpawnerMenu extends AbstractContainerMenu {

    public final EntitySpawnerBlockEntity blockEntity;
    private final ContainerLevelAccess containerAccess;

    public EntitySpawnerMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public EntitySpawnerMenu(int windowId, Inventory playerInventory, BlockEntity blockEntity) {
        super(MenuHandle.ENTITY_SPAWNER_MENU.get(), windowId);
        checkContainerSize(playerInventory, 0);
        this.blockEntity = (EntitySpawnerBlockEntity) blockEntity;
        this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level(), blockEntity.getBlockPos());
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.containerAccess, player, BlockHandle.ENTITY_SPAWNER_BLOCK.get());
    }
}