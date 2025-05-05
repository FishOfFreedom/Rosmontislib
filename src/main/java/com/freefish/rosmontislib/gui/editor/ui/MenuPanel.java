package com.freefish.rosmontislib.gui.editor.ui;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.runtime.AnnotationDetector;
import com.freefish.rosmontislib.gui.editor.ui.menu.MenuTab;
import com.freefish.rosmontislib.gui.texture.ResourceTexture;
import com.freefish.rosmontislib.gui.widget.ImageWidget;
import com.freefish.rosmontislib.gui.widget.WidgetGroup;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author KilaBash
 * @date 2022/12/6
 * @implNote MenuPanel
 */
public class MenuPanel extends WidgetGroup {
    public static final int HEIGHT = 16;

    @Getter
    protected final Editor editor;
    @Getter
    protected final Map<String, MenuTab> tabs = new LinkedHashMap<>();

    public MenuPanel(Editor editor) {
        super(0, 0, editor.getSize().getWidth() - ConfigPanel.WIDTH, HEIGHT);
        setClientSideWidget();
        this.editor = editor;
    }

    @Override
    public void initWidget() {
        this.setBackground(ColorPattern.T_RED.rectTexture());
        this.addWidget(new ImageWidget(2, 2, 12, 12, new ResourceTexture()));
        if (isRemote()) {
            initTabs();
        }
        super.initWidget();
    }

    public <T extends MenuTab> T getTab(String name) {
        return (T) tabs.get(name);
    }

    protected void initTabs() {
        int x = 20;
        var tag = new CompoundTag();
        try {
            tag = NbtIo.read(new File(editor.getWorkSpace(), "settings/menu.cfg"));
            if (tag == null) {
                tag = new CompoundTag();
            }
        } catch (IOException e) {
            RosmontisLib.LOGGER.error(e.getMessage());
        }
        for (AnnotationDetector.Wrapper<LDLRegister, MenuTab> wrapper : AnnotationDetector.REGISTER_MENU_TABS) {
            if (editor.name().startsWith(wrapper.annotation().group())) {
                var tab = wrapper.creator().get();
                tabs.put(wrapper.annotation().name(), tab);
                var button = tab.createTabWidget();
                button.addSelfPosition(x, 0);
                x += button.getSize().getWidth();
                addWidget(button);
                if (tag.contains(tab.name())) {
                    tab.deserializeNBT(tag.getCompound(tab.name()));
                }
            }
        }
    }

    public void saveMenuData() {
        var tag = new CompoundTag();
        for (MenuTab tab : tabs.values()) {
            var nbt = tab.serializeNBT();
            if (!nbt.isEmpty()) {
                tag.put(tab.name(), nbt);
            }
        }
        try {
            NbtIo.write(tag, new File(editor.getWorkSpace(), "settings/menu.cfg"));
        } catch (IOException e) {
            RosmontisLib.LOGGER.error(e.getMessage());
        }
    }

}
