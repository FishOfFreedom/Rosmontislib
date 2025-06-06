/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.data;

import com.freefish.rosmontislib.gui.editor.ILDLRegister;
import com.freefish.rosmontislib.gui.editor.ui.Editor;
import com.freefish.rosmontislib.gui.util.TreeBuilder;
import com.freefish.rosmontislib.sync.ITagSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * @author KilaBash
 * @date 2022/12/9
 * @implNote IProject
 */
public interface IProject extends ILDLRegister, ITagSerializable<CompoundTag> {

    Resources getResources();

    /**
     * Save project
     */
    default void saveProject(File file) {
        try {
            NbtIo.write(serializeNBT(), file);
        } catch (IOException ignored) {
            // TODO
        }
    }

    /**
     * Load project from file. return null if loading failed
     */
    @Nullable
    default IProject loadProject(File file) {
        try {
            var tag = NbtIo.read(file);
            if (tag != null) {
                deserializeNBT(tag);
            }
        } catch (IOException ignored) {}
        return this;
    }

    IProject newEmptyProject();

    /**
     * Get project work space
     */
    default File getProjectWorkSpace(Editor editor) {
        return new File(editor.getWorkSpace(), "projects/" + name());
    }

    /**
     * Suffix name of this project
     */
    default String getSuffix() {
        return name();
    }

    /**
     * Fired when project is closed
     */
    default void onClosed(Editor editor) {
    }

    /**
     * Fired when project is opened
     */
    default void onLoad(Editor editor) {
        editor.getResourcePanel().loadResource(getResources(), false);
    }

    /**
     * Attach menu
     * @param name menu name
     * @param menu current menu
     */
    default void attachMenu(Editor editor, String name, TreeBuilder.Menu menu) {

    }

    /**
     * Load resource from nbt data
     */
    default Resources loadResources(CompoundTag tag) {
        return Resources.fromNBT(tag);
    }

}
