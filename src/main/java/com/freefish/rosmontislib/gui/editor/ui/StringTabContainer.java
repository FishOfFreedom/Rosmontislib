/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.ui;

import com.freefish.rosmontislib.client.utils.Position;
import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.editor.Icons;
import com.freefish.rosmontislib.gui.texture.GuiTextureGroup;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.texture.TextTexture;
import com.freefish.rosmontislib.gui.widget.TabButton;
import com.freefish.rosmontislib.gui.widget.TabContainer;
import com.freefish.rosmontislib.gui.widget.WidgetGroup;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

@Getter
public class StringTabContainer extends TabContainer {
    public final static int TAB_HEIGHT = 16;
    protected Editor editor;

    protected final Map<WidgetGroup, Runnable> onSelected;
    protected final Map<WidgetGroup, Runnable> onDeselected;
    public BiConsumer<WidgetGroup, WidgetGroup> onChanged;
    private final List<WidgetGroup> tabGroups;


    public StringTabContainer(Editor editor) {
        super(0, 0, editor.getSize().width - editor.getConfigPanel().getSize().width, editor.getSize().height);
        this.editor = editor;
        this.onSelected = new HashMap<>();
        this.onDeselected = new HashMap<>();
        this.tabGroups = new ArrayList<>();
        super.setOnChanged(this::onTabChanged);
    }

    public int getTabIndex() {
        if (focus == null) return -1;
        return tabGroups.indexOf(focus);
    }

    public int getTabIndex(WidgetGroup group) {
        return tabGroups.indexOf(group);
    }

    public void switchTabIndex(int index) {
        if (tabGroups.size() > index && index >= 0) {
            switchTag(tabGroups.get(index));
        }
    }

    @Override
    public void clearAllWidgets() {
        super.clearAllWidgets();
        this.onSelected.clear();
        this.onDeselected.clear();
        this.tabGroups.clear();
    }

    public TabContainer setOnChanged(BiConsumer<WidgetGroup, WidgetGroup> onChanged) {
        this.onChanged = onChanged;
        return this;
    }

    protected void onTabChanged(WidgetGroup oldGroup, WidgetGroup newGroup) {
        Optional.ofNullable(onDeselected.get(oldGroup)).ifPresent(Runnable::run);
        Optional.ofNullable(onSelected.get(newGroup)).ifPresent(Runnable::run);
        if (onChanged != null) {
            onChanged.accept(oldGroup, newGroup);
        }
    }

    @Override
    public final void addTab(TabButton tabButton, WidgetGroup tabWidget) {
        super.addTab(tabButton, tabWidget);
        tabGroups.add(tabWidget);
    }

    @Override
    public void removeTab(TabButton tabButton) {
        var group = tabs.get(tabButton);
        var lastIndex = getTabIndex(group);
        super.removeTab(tabButton);
        tabGroups.remove(group);
        if (focus == group) {
            Optional.ofNullable(onDeselected.get(group)).ifPresent(Runnable::run);
            // find a new tabs
            focus = null;
            if (lastIndex > 0) {
                switchTabIndex(lastIndex - 1);
            }
        }
        onSelected.remove(group);
        onDeselected.remove(group);
        calculateTabSize();
    }

    public void addTab(@Nullable IGuiTexture icon, String name, WidgetGroup group, @Nullable Runnable onSelected, @Nullable Runnable onDeselected, @Nullable Runnable onRemoved) {
        var nameTexture = new TextTexture(name).setType(TextTexture.TextType.ROLL);
        var tabButton = new TabButton(0, 0, 60, TAB_HEIGHT - 2) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
                super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
                var position = getPosition();
                var size = getSize();
                if (icon != null) {
                    icon.draw(graphics, mouseX, mouseY, position.x + 2, position.y + 2, 12, 12);
                }
                var textWidth = size.width - (icon == null ? 0 : 16) - (onRemoved == null ? 0 : 16);
                if (textWidth != nameTexture.width) {
                    nameTexture.setWidth(textWidth);
                }
                nameTexture.draw(graphics, mouseX, mouseY, position.x + (icon == null ? 0 : 16), position.y, textWidth, size.height);
                if (onRemoved != null) {
                    if (isMouseOver(position.x + size.width - 16, position.y, 12, 12, mouseX, mouseY)) {
                        Icons.REMOVE.copy().setColor(ColorPattern.GREEN.color).draw(graphics, mouseX, mouseY, position.x + size.width - 16, position.y, 12, 12);
                    } else {
                        Icons.REMOVE.draw(graphics, mouseX, mouseY, position.x + size.width - 16, position.y, 12, 12);
                    }
                }
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (onRemoved != null) {
                    var position = getPosition();
                    var size = getSize();
                    if (isMouseOver(position.x + size.width - 16, position.y, 12, 12, mouseX, mouseY)) {
                        onRemoved.run();
                        removeTab(this);
                        calculateTabSize();
                        return true;
                    }
                }
                return super.mouseClicked(mouseX, mouseY, button);
            }
        };
        tabButton.setTexture(
                new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture()),
                new GuiTextureGroup(ColorPattern.T_RED.rectTexture()));
        if (onSelected != null) {
            this.onSelected.put(group, onSelected);
        }
        if (onDeselected != null) {
            this.onDeselected.put(group, onDeselected);
        }
        addTab(tabButton, group);
        if (this.focus == group && onSelected != null) {
            onSelected.run();
        }
        calculateTabSize();
    }

    public void addTab(String name, WidgetGroup group, @Nullable Runnable onSelected, @Nullable Runnable onDeselected) {
        this.addTab(null, name, group, onSelected, onDeselected, null);
    }

    public void addTab(String name, WidgetGroup group, Runnable onSelected) {
        this.addTab(name, group, onSelected, null);
    }

    public void addTab(String name, WidgetGroup group) {
        this.addTab(name, group, null, null);
    }

    public void calculateTabSize() {
        int tabWidth = (getSize().getWidth() - 1 - tabs.size()) / this.tabs.size();
        int x = 1;
        int y = editor.getMenuPanel().getSize().height + 1;
        for (var tabButton : this.tabs.keySet()) {
            tabButton.setSelfPosition(new Position(x, y));
            tabButton.setSize(new Size(tabWidth, TAB_HEIGHT - 2));
            x += tabWidth + 1;
        }
    }
}
