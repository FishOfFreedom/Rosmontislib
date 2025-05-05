package com.freefish.rosmontislib.gui.editor.configurator;

import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.data.Resources;
import com.freefish.rosmontislib.gui.editor.data.resource.Resource;
import com.freefish.rosmontislib.gui.editor.data.resource.TexturesResource;
import com.freefish.rosmontislib.gui.editor.runtime.AnnotationDetector;
import com.freefish.rosmontislib.gui.texture.ColorRectTexture;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.texture.UIResourceTexture;
import com.freefish.rosmontislib.gui.widget.Widget;
import com.freefish.rosmontislib.sync.PersistedParser;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2022/12/6
 * @implNote IConfigurableWidget
 */
public interface IConfigurableWidget extends IConfigurable {

    Function<String, AnnotationDetector.Wrapper<LDLRegister, IConfigurableWidget>> CACHE = Util.memoize(type -> {
        for (var wrapper : AnnotationDetector.REGISTER_WIDGETS) {
            if (wrapper.annotation().name().equals(type)) {
                return wrapper;
            }
        }
        return null;
    });

    default Widget widget() {
        return (Widget) this;
    }

    default void initTemplate() {

    }

    default boolean canDragIn(Object dragging) {
        if (dragging instanceof IGuiTexture) {
            return true;
        } else if (dragging instanceof String) {
            return true;
        } else if (dragging instanceof IIdProvider) {
            return true;
        } else if (dragging instanceof Integer) {
            return true;
        }
        return false;
    }

    default boolean handleDragging(Object dragging) {
        if (dragging instanceof IGuiTexture guiTexture) {
            widget().setBackground(guiTexture);
            return true;
        } else if (dragging instanceof String string) {
            widget().setHoverTooltips(string);
            return true;
        } else if (dragging instanceof IIdProvider idProvider) {
            widget().setId(idProvider.get());
            return true;
        } else if (dragging instanceof Integer color) {
            widget().setBackground(new ColorRectTexture(color));
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    static CompoundTag serializeNBT(IConfigurableWidget widget, Resources resources, boolean isProject) {
        return serializeNBT(widget, (Resource<IGuiTexture>) resources.resources.get(TexturesResource.RESOURCE_NAME), isProject);
    }

    static CompoundTag serializeNBT(IConfigurableWidget widget, Resource<IGuiTexture> resources, boolean isProject) {
        UIResourceTexture.setCurrentResource(resources, isProject);
        CompoundTag tag = widget.serializeInnerNBT();
        UIResourceTexture.clearCurrentResource();
        return tag;
    }

    @SuppressWarnings("unchecked")
    static void deserializeNBT(IConfigurableWidget widget, CompoundTag tag, Resources resources, boolean isProject) {
        deserializeNBT(widget, tag, (Resource<IGuiTexture>) resources.resources.get(TexturesResource.RESOURCE_NAME), isProject);
    }

    static void deserializeNBT(IConfigurableWidget widget, CompoundTag tag, Resource<IGuiTexture> resources, boolean isProject) {
        UIResourceTexture.setCurrentResource(resources, isProject);
        widget.deserializeInnerNBT(tag);
        UIResourceTexture.clearCurrentResource();
    }

    default CompoundTag serializeInnerNBT() {
        CompoundTag tag = new CompoundTag();
        PersistedParser.serializeNBT(tag, getClass(), this);
        return tag;
    }

    default void deserializeInnerNBT(CompoundTag nbt) {
        PersistedParser.deserializeNBT(nbt, new HashMap<>(), getClass(), this);
    }

    default CompoundTag serializeWrapper() {
        var tag = new CompoundTag();
        tag.putString("type", name());
        tag.put("data", serializeInnerNBT());
        return tag;
    }

    @Nullable
    static IConfigurableWidget deserializeWrapper(CompoundTag tag) {
        String type = tag.getString("type");
        var wrapper = CACHE.apply(type);
        if (wrapper != null) {
            var child = wrapper.creator().get();
            child.deserializeInnerNBT(tag.getCompound("data"));
            return child;
        }
        return null;
    }

    // ******* setter ********//


    @FunctionalInterface
    interface IIdProvider extends Supplier<String> {

    }
}
