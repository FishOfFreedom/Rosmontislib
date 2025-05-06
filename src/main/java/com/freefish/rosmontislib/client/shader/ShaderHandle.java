package com.freefish.rosmontislib.client.shader;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.client.shader.management.Shader;
import com.freefish.rosmontislib.client.utils.RenderUtils;
import com.freefish.rosmontislib.gui.util.DrawerHelper;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import lombok.Getter;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.ELEMENT_POSITION;

public class ShaderHandle {
    @Getter
    private static ShaderInstance particleShader;
    @Getter
    private static ShaderInstance hsbShader;

    public static void setParticleShader(ShaderInstance particleShader) {
        ShaderHandle.particleShader = particleShader;
    }

    public static void setHSBAlphaShader(ShaderInstance particleShader) {
        ShaderHandle.hsbShader = particleShader;
    }

    private static final VertexFormatElement HSB_Alpha = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.COLOR, 4);

    public static final VertexFormat HSB_VERTEX_FORMAT = new VertexFormat(
            ImmutableMap.<String, VertexFormatElement>builder()
                    .put("Position", ELEMENT_POSITION)
                    .put("HSB_ALPHA", HSB_Alpha)
                    .build());

    public static void registerShaders(final RegisterShadersEvent e) {
        try {
            e.registerShader(new ShaderInstance(e.getResourceProvider(), new ResourceLocation(RosmontisLib.MOD_ID,"particle"), DefaultVertexFormat.POSITION), ShaderHandle::setParticleShader);
            e.registerShader(new ShaderInstance(e.getResourceProvider(), new ResourceLocation(RosmontisLib.MOD_ID,"hsb_block"), HSB_VERTEX_FORMAT), ShaderHandle::setHSBAlphaShader);

            ResourceProvider resourceProvider = e.getResourceProvider();
            e.registerShader(ReloadShaderManager.backupNewShaderInstance(resourceProvider, new ResourceLocation(RosmontisLib.MOD_ID, "fast_blit"), DefaultVertexFormat.POSITION), shaderInstance -> RenderUtils.blitShader = shaderInstance);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static final List<Runnable> reloadListeners = new ArrayList<>();
    public static Shader IMAGE_V;
    public static Shader SCREEN_V;
    public static Shader GUI_IMAGE_V;
    public static Shader ROUND_F;
    public static Shader PANEL_BG_F;
    public static Shader ROUND_BOX_F;
    public static Shader PROGRESS_ROUND_BOX_F;
    public static Shader FRAME_ROUND_BOX_F;
    public static Shader ROUND_LINE_F;

    public static void init() {
        IMAGE_V = load(Shader.ShaderType.VERTEX, new ResourceLocation(RosmontisLib.MOD_ID, "image"));
        SCREEN_V = load(Shader.ShaderType.VERTEX, new ResourceLocation(RosmontisLib.MOD_ID, "screen"));
        GUI_IMAGE_V = load(Shader.ShaderType.VERTEX, new ResourceLocation(RosmontisLib.MOD_ID, "gui_image"));
        ROUND_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation(RosmontisLib.MOD_ID, "round"));
        PANEL_BG_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation(RosmontisLib.MOD_ID, "panel_bg"));
        ROUND_BOX_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation(RosmontisLib.MOD_ID, "round_box"));
        PROGRESS_ROUND_BOX_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation(RosmontisLib.MOD_ID, "progress_round_box"));
        FRAME_ROUND_BOX_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation(RosmontisLib.MOD_ID, "frame_round_box"));
        ROUND_LINE_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation(RosmontisLib.MOD_ID, "round_line"));
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
        DrawerHelper.init();
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
