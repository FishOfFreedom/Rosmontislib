package com.freefish.rosmontislib;

import com.freefish.rosmontislib.client.particle.advance.data.material.MaterialHandle;
import com.freefish.rosmontislib.client.shader.ShaderHandle;
import com.freefish.rosmontislib.compat.oculus.ForgeOculusHandle;
import com.freefish.rosmontislib.event.RLNetworking;
import com.freefish.rosmontislib.event.listener.CameraShakeEvent;
import com.freefish.rosmontislib.event.listener.CommonEvent;
import com.freefish.rosmontislib.example.init.BlockEntityHandle;
import com.freefish.rosmontislib.example.init.BlockHandle;
import com.freefish.rosmontislib.example.init.ItemHandle;
import com.freefish.rosmontislib.example.init.MenuHandle;
import com.freefish.rosmontislib.gui.editor.runtime.AnnotationDetector;
import com.freefish.rosmontislib.gui.factory.UIFactory;
import com.freefish.rosmontislib.gui.util.DrawerHelper;
import com.freefish.rosmontislib.levelentity.LevelEntityHandle;
import com.freefish.rosmontislib.sync.TypedPayloadRegistries;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.File;
import java.util.Random;

@Mod(RosmontisLib.MOD_ID)
public class RosmontisLib
{
    public static final String MOD_ID = "rosmontislib";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random random = new Random();
    public static File location;

    public RosmontisLib()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        getRLLibDir();

        //DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        if(isDevEnv()){
            ItemHandle.ITEMS.register(bus);
            ItemHandle.TABS.register(bus);
            MenuHandle.MENUS.register(bus);
            BlockEntityHandle.TILES.register(bus);
            BlockHandle.BLOCKS.register(bus);
        }
        UIFactory.init();
        AnnotationDetector.init();

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CommonEvent());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()->{
            RLNetworking.init();
        });
        LevelEntityHandle.init();
        TypedPayloadRegistries.init();
        TypedPayloadRegistries.postInit();
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        event.enqueueWork(()->{
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            bus.addListener(ShaderHandle::registerShaders);
            ShaderHandle.init();
            DrawerHelper.init();
            MaterialHandle.init();
            MinecraftForge.EVENT_BUS.register(CameraShakeEvent.INSTANCE);
        });
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static File getRLLibDir() {
        if (location == null) {
            location = new File(FMLPaths.GAMEDIR.get().toFile(), "rosmontislib");
            if (location.mkdir()) {
                LOGGER.info("create rosmontislib config folder");
            }
        }
        return location;
    }

    public static boolean isUsingShaderPack() {
        return ForgeOculusHandle.INSTANCE!=null && ForgeOculusHandle.INSTANCE.underShaderPack();
    }

    public static boolean isModLoaded(String mod) {
        return ModList.get().isLoaded(mod);
    }

    public static boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }

    public static boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    public static boolean isRemote() {
        if (isClient()) {
            return Minecraft.getInstance().isSameThread();
        }
        return false;
    }

}
