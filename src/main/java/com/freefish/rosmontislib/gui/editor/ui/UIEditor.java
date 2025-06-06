/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.ui;


import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;

import java.io.File;

/**
 * @author KilaBash
 * @date 2023/6/3
 * @implNote UIEditor
 */
@LDLRegister(name = "editor.ui", group = "editor")
public class UIEditor extends Editor {
    public UIEditor(File workSpace) {
        super(workSpace);
    }

    public UIEditor(String modID) {
        super(modID);
    }
}
