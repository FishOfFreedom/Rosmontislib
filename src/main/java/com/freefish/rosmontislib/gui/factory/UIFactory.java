/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.factory;

import com.freefish.rosmontislib.event.RLNetworking;
import com.freefish.rosmontislib.event.packet.toclient.GUIOpenMessage;
import com.freefish.rosmontislib.gui.modular.ModularUI;
import com.freefish.rosmontislib.gui.modular.ModularUIContainer;
import com.freefish.rosmontislib.gui.modular.ModularUIGuiContainer;
import com.freefish.rosmontislib.mixin.accessor.ServerPlayerAccessor;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

public abstract class UIFactory<T> {
    public final ResourceLocation uiFactoryId;
    public static final Map<ResourceLocation, UIFactory<?>> FACTORIES = new HashMap<>();

    public UIFactory(ResourceLocation uiFactoryId){
        this.uiFactoryId = uiFactoryId;
    }
    
    public static void register(UIFactory<?> factory) {
        FACTORIES.put(factory.uiFactoryId, factory);
    }

    public final boolean openUI(T holder, ServerPlayer player) {
        ModularUI uiTemplate = createUITemplate(holder, player);
        if (uiTemplate == null) return false;
        uiTemplate.initWidgets();

        if (player.containerMenu != player.inventoryMenu) {
            player.closeContainer();
        }
        ((ServerPlayerAccessor) player).callNextContainerCounter();
        int currentWindowId = ((ServerPlayerAccessor) player).getContainerCounter();

        FriendlyByteBuf serializedHolder = new FriendlyByteBuf(Unpooled.buffer());
        writeHolderToSyncData(serializedHolder, holder);
        ModularUIContainer container = new ModularUIContainer(uiTemplate, currentWindowId);

        //accumulate all initial updates of widgets in open packet
        uiTemplate.mainGroup.writeInitialData(serializedHolder);

        RLNetworking.NETWORK.sendToPlayer(new GUIOpenMessage(uiFactoryId, serializedHolder, currentWindowId), player);

        ((ServerPlayerAccessor) player).callInitMenu(container);
        player.containerMenu = container;

        //and fire forge event only in the end
        //if (Platform.isForge()) {
        //    ForgeEventHooks.postPlayerContainerEvent(player, container);
        //}
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public final void initClientUI(FriendlyByteBuf serializedHolder, int windowId) {
        T holder = readHolderFromSyncData(serializedHolder);
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer entityPlayer = minecraft.player;

        ModularUI uiTemplate = createUITemplate(holder, entityPlayer);
        if (uiTemplate == null) return;
        uiTemplate.initWidgets();
        ModularUIGuiContainer ModularUIGuiContainer = new ModularUIGuiContainer(uiTemplate, windowId);
        uiTemplate.mainGroup.readInitialData(serializedHolder);
        minecraft.setScreen(ModularUIGuiContainer);
        minecraft.player.containerMenu = ModularUIGuiContainer.getMenu();

    }

    protected abstract ModularUI createUITemplate(T holder, Player entityPlayer);

    @OnlyIn(Dist.CLIENT)
    protected abstract T readHolderFromSyncData(FriendlyByteBuf syncData);

    protected abstract void writeHolderToSyncData(FriendlyByteBuf syncData, T holder);

    public static void init(){
        register(UIEditorFactory.INSTANCE);
    }

}
