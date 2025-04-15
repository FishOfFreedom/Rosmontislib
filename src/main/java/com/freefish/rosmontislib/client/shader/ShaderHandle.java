package com.freefish.rosmontislib.client.shader;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.client.shader.management.Shader;
import com.freefish.rosmontislib.client.utils.RenderUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShaderHandle {
    private static ShaderInstance particleShader;

    public static ShaderInstance getParticleShader() {
        return particleShader;
    }

    public static void setParticleShader(ShaderInstance particleShader) {
        ShaderHandle.particleShader = particleShader;
    }

    public static void registerShaders(final RegisterShadersEvent e) {
        init();
        try {
            e.registerShader(new ShaderInstance(e.getResourceProvider(), new ResourceLocation(RosmontisLib.MOD_ID,"particle"), DefaultVertexFormat.POSITION), ShaderHandle::setParticleShader);

            ResourceProvider resourceProvider = e.getResourceProvider();
            e.registerShader(ReloadShaderManager.backupNewShaderInstance(resourceProvider, new ResourceLocation(RosmontisLib.MOD_ID, "fast_blit"), DefaultVertexFormat.POSITION), shaderInstance -> RenderUtils.blitShader = shaderInstance);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static final List<Runnable> reloadListeners = new ArrayList<>();
    public static Shader IMAGE_V;

    public static void init() {
        IMAGE_V = load(Shader.ShaderType.VERTEX, new ResourceLocation(RosmontisLib.MOD_ID, "image"));
    }

    public static Map<ResourceLocation, Shader> CACHE = new HashMap<>();

    public static void addReloadListener(Runnable runnable) {
        reloadListeners.add(runnable);
    }

    public static void reload() {
        for (Shader shader : CACHE.values()) {
            if (shader != null) {
                shader.deleteShader();
            }
        }
        CACHE.clear();
        init();
        reloadListeners.forEach(Runnable::run);
    }

    public static Shader load(Shader.ShaderType shaderType, ResourceLocation resourceLocation) {
        return CACHE.computeIfAbsent(new ResourceLocation(resourceLocation.getNamespace(), "shaders/" + resourceLocation.getPath() + shaderType.shaderExtension), key -> {
            try {
                Shader shader = Shader.loadShader(shaderType, key);
                RosmontisLib.LOGGER.debug("load shader {} resource {} success", shaderType, resourceLocation);
                return shader;
            } catch (IOException e) {
                RosmontisLib.LOGGER.error("load shader {} resource {} failed", shaderType, resourceLocation);
                RosmontisLib.LOGGER.error("caused by ", e);
                return IMAGE_V;
            }
        });
    }
}
