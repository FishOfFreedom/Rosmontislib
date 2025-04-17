package com.freefish.rosmontislib;

import com.freefish.rosmontislib.client.shader.ShaderHandle;
import com.freefish.rosmontislib.compat.oculus.ForgeOculusHandle;
import com.freefish.rosmontislib.event.ServerNetwork;
import com.freefish.rosmontislib.example.init.BlockEntityHandle;
import com.freefish.rosmontislib.example.init.BlockHandle;
import com.freefish.rosmontislib.example.init.ItemHandle;
import com.freefish.rosmontislib.example.init.MenuHandle;
import com.mojang.logging.LogUtils;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import java.util.Random;

@Mod(RosmontisLib.MOD_ID)
public class RosmontisLib
{
    public static final String MOD_ID = "rosmontislib";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static SimpleChannel NETWORK;
    public static final Random random = new Random();

    public RosmontisLib()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        if(isDevEnv()){
            ItemHandle.ITEMS.register(bus);
            ItemHandle.TABS.register(bus);
            MenuHandle.MENUS.register(bus);
            BlockEntityHandle.TILES.register(bus);
            BlockHandle.BLOCKS.register(bus);
        }
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()->{
            ServerNetwork.initNetwork();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        event.enqueueWork(()->{
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            bus.addListener(ShaderHandle::registerShaders);
        });
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }

    public static boolean isUsingShaderPack() {
        return ForgeOculusHandle.INSTANCE!=null && ForgeOculusHandle.INSTANCE.underShaderPack();
    }

    public static boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }
}
