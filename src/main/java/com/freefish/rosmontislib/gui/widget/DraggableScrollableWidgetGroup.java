/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.widget;

import com.freefish.rosmontislib.client.utils.Position;
import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.editor.annotation.ConfigSetter;
import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.annotation.NumberRange;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4f;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

@LDLRegister(name = "draggable_scrollable_group", group = "widget.group")
@Accessors(chain = true)
public class DraggableScrollableWidgetGroup extends WidgetGroup {
    public enum ScrollWheelDirection {
        VERTICAL,
        HORIZONTAL,
    }
    @Getter
    protected int scrollXOffset;
    @Getter
    protected int scrollYOffset;
    @Configurable(name = "ldlib.gui.editor.name.x_bar_height")
    @NumberRange(range = {0, Integer.MAX_VALUE})
    protected int xBarHeight;
    @Configurable(name = "ldlib.gui.editor.name.y_bar_width")
    @NumberRange(range = {0, Integer.MAX_VALUE})
    protected int yBarWidth;
    @Configurable(name = "ldlib.gui.editor.name.draggable")
    @Setter @Getter
    protected boolean draggable;
    @Configurable(name = "ldlib.gui.editor.name.scrollable")
    @Setter @Getter
    protected boolean scrollable = true;
    @Getter @Setter
    @Configurable(name = "ldlib.gui.editor.name.scroll_wheel_direction")
    protected ScrollWheelDirection scrollWheelDirection = ScrollWheelDirection.VERTICAL;
    @Getter @Setter
    @Configurable(name = "ldlib.gui.editor.name.use_scissor")
    protected boolean useScissor;
    protected int maxHeight;
    protected int maxWidth;
    @Configurable(name = "ldlib.gui.editor.name.x_bar_background")
    protected IGuiTexture xBarB;
    @Configurable(name = "ldlib.gui.editor.name.x_bar_foreground")
    protected IGuiTexture xBarF;
    @Configurable(name = "ldlib.gui.editor.name.y_bar_background")
    protected IGuiTexture yBarB;
    @Configurable(name = "ldlib.gui.editor.name.y_bar_foreground")
    protected IGuiTexture yBarF;
    protected Widget draggedWidget;
    protected Widget selectedWidget;
    protected boolean isComputingMax;
    private boolean draggedPanel;
    private boolean draggedOnXScrollBar;
    private boolean draggedOnYScrollBar;
    private double lastDeltaX, lastDeltaY;
    @Getter
    private final Set<BiConsumer<Integer, Integer>> moveCallbacks = new HashSet<>();

    public DraggableScrollableWidgetGroup() {
        this(0, 0,50, 50);
    }

    @Override
    public void initTemplate() {
        setBackground(ColorPattern.RED.rectTexture());
        setYScrollBarWidth(4).setYBarStyle(ColorPattern.RED.rectTexture(), ColorPattern.WHITE.rectTexture().setRadius(2));
    }

    public DraggableScrollableWidgetGroup(int x, int y, int width, int height) {
        super(new Position(x, y), new Size(width, height));
        maxHeight = height;
        maxWidth = width;
        useScissor = true;
    }

    @ConfigSetter(field = "xBarHeight")
    public DraggableScrollableWidgetGroup setXScrollBarHeight(int xBar) {
        this.xBarHeight = xBar;
        computeMax();
        return this;
    }

    @ConfigSetter(field = "yBarWidth")
    public DraggableScrollableWidgetGroup setYScrollBarWidth(int yBar) {
        this.yBarWidth = yBar;
        computeMax();
        return this;
    }

    public DraggableScrollableWidgetGroup setBackground(IGuiTexture background) {
        super.setBackground(background);
        return this;
    }

    public DraggableScrollableWidgetGroup setXBarStyle(IGuiTexture background, IGuiTexture bar) {
        this.xBarB = background;
        this.xBarF = bar;
        return this;
    }

    public DraggableScrollableWidgetGroup setYBarStyle(IGuiTexture background, IGuiTexture bar) {
        this.yBarB = background;
        this.yBarF = bar;
        return this;
    }

    @Override
    public WidgetGroup addWidget(int index, Widget widget) {
        maxHeight = Math.max(maxHeight - xBarHeight, widget.getSize().height + widget.getSelfPosition().y);
        maxWidth = Math.max(maxWidth - yBarWidth, widget.getSize().width + widget.getSelfPosition().x);
        Position newPos = widget.addSelfPosition(- scrollXOffset, - scrollYOffset);
        widget.setVisible(newPos.x < getSize().width - yBarWidth && newPos.x + widget.getSize().width > 0);
        widget.setVisible(newPos.y < getSize().height - xBarHeight && newPos.y + widget.getSize().height > 0);
        return super.addWidget(index, widget);
    }

    @Override
    public void removeWidget(Widget widget) {
        super.removeWidget(widget);
        computeMax();
        if (widget == draggedWidget) draggedWidget = null;
        if (widget == selectedWidget) selectedWidget = null;
    }

    @Override
    public void clearAllWidgets() {
        super.clearAllWidgets();
        maxHeight = getSize().height - xBarHeight;
        maxWidth = getSize().width - yBarWidth;
        scrollXOffset = 0;
        scrollYOffset = 0;
        draggedWidget = null;
        selectedWidget = null;
    }

    @Override
    @ConfigSetter(field = "size")
    public void setSize(Size size) {
        super.setSize(size);
        maxHeight = Math.max(size.height - xBarHeight, maxHeight);
        maxWidth = Math.max(size.width - yBarWidth, maxWidth);
//        computeMax();
        for (Widget widget : widgets) {
            Position newPos = widget.getSelfPosition();
            widget.setVisible(newPos.x < getSize().width - yBarWidth && newPos.x + widget.getSize().width > 0);
            widget.setVisible(newPos.y < getSize().height - xBarHeight && newPos.y + widget.getSize().height > 0);
        }
    }

    @Override
    protected void onChildSelfPositionUpdate(Widget child) {
        super.onChildSelfPositionUpdate(child);
        if (!isComputingMax && isInitialized()) {
            computeMax();
        }
    }

    @Override
    protected void onChildSizeUpdate(Widget child) {
        super.onChildSizeUpdate(child);
        if (!isComputingMax && isInitialized()) {
            computeMax();
        }
    }

    public void computeMax() {
        if (isComputingMax) return;
        isComputingMax = true;
        var lastScrollXOffset = scrollXOffset;
        var lastScrollYOffset = scrollYOffset;
        int mh = 0;
        int mw = 0;
        for (Widget widget : widgets) {
            mh = Math.max(mh, widget.getSize().height + widget.getSelfPosition().y + scrollYOffset);
            mw = Math.max(mw, widget.getSize().width + widget.getSelfPosition().x + scrollXOffset);
        }
        int offsetY = 0;
        int offsetX = 0;
        if (mh > getSize().height - xBarHeight) {
            offsetY = maxHeight - mh;
            maxHeight = mh;
            if (scrollYOffset - offsetY < 0) {
                offsetY = scrollYOffset;
            }
            scrollYOffset -= offsetY;
        } else if (mh < getSize().height - xBarHeight) {
            offsetY = maxHeight - (getSize().height - xBarHeight);
            maxHeight = getSize().height - xBarHeight;
            if (scrollYOffset - offsetY < 0) {
                offsetY = scrollYOffset;
            }
            scrollYOffset -= offsetY;
        }
        if (mw > getSize().width - yBarWidth) {
            offsetX = maxWidth - mw;
            maxWidth = mw;
            if (scrollXOffset - offsetX < 0) {
                offsetX = scrollXOffset;
            }
            scrollXOffset -= offsetX;
        }else if (mw < getSize().width - yBarWidth) {
            offsetX = maxWidth - (getSize().width - yBarWidth);
            maxWidth = getSize().width - yBarWidth;
            if (scrollXOffset - offsetX < 0) {
                offsetX = scrollXOffset;
            }
            scrollXOffset -= offsetX;
        }
        offsetX += scrollXOffset - Math.min(scrollXOffset, lastScrollXOffset);
        offsetY += scrollYOffset - Math.min(scrollYOffset, lastScrollYOffset);
        scrollXOffset = Math.min(scrollXOffset, lastScrollXOffset);
        scrollYOffset = Math.min(scrollYOffset, lastScrollYOffset);
        for (Widget widget : widgets) {
            Position newPos = widget.addSelfPosition(offsetX, offsetY);
            widget.setVisible(newPos.x < getSize().width - yBarWidth && newPos.x + widget.getSize().width > 0);
            widget.setVisible(newPos.y < getSize().height - xBarHeight && newPos.y + widget.getSize().height > 0);
        }
        isComputingMax = false;
    }

    protected int getMaxHeight() {
        return maxHeight + xBarHeight;
    }

    protected int getMaxWidth() {
        return maxWidth + yBarWidth;
    }

    public int getWidgetBottomHeight() {
        int y = 0;
        for (Widget widget : widgets) {
            y = Math.max(y, widget.getSize().height + widget.getSelfPosition().y);
        }
        return y;
    }

    public void setScrollXOffset(int scrollXOffset) {
        if (scrollXOffset == this.scrollXOffset) return;
        if (scrollXOffset < 0) scrollXOffset = 0;
        int offset = scrollXOffset - this.scrollXOffset;
        this.scrollXOffset = scrollXOffset;
        isComputingMax = true;
        for (Widget widget : widgets) {
            Position newPos = widget.addSelfPosition(-offset, 0);
            widget.setVisible(newPos.x < getSize().width - yBarWidth && newPos.x + widget.getSize().width > 0);
        }
        for (var callback : moveCallbacks) {
            callback.accept(-offset, 0);
        }
        isComputingMax = false;
    }

    public void setScrollYOffset(int scrollYOffset) {
        if (scrollYOffset == this.scrollYOffset) return;
        if (scrollYOffset < 0) scrollYOffset = 0;
        int offset = scrollYOffset - this.scrollYOffset;
        this.scrollYOffset = scrollYOffset;
        isComputingMax = true;
        for (Widget widget : widgets) {
            Position newPos = widget.addSelfPosition(0, -offset);
            widget.setVisible(newPos.y < getSize().height - xBarHeight && newPos.y + widget.getSize().height > 0);
        }
        for (var callback : moveCallbacks) {
            callback.accept(0, -offset);
        }
        isComputingMax = false;
    }

    private boolean isOnXScrollPane(double mouseX, double mouseY) {
        Position pos = getPosition();
        Size size = getSize();
        return isMouseOver(pos.x, pos.y + size.height - xBarHeight, size.width, xBarHeight, mouseX, mouseY);
    }

    private boolean isOnYScrollPane(double mouseX, double mouseY) {
        Position pos = getPosition();
        Size size = getSize();
        return isMouseOver(pos.x + size.width - yBarWidth, pos.y, yBarWidth, size.height, mouseX, mouseY);
    }

    @OnlyIn(Dist.CLIENT)
    protected boolean hookDrawInBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (isMouseOverElement(mouseX, mouseY)) {
            super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawBackgroundTexture(graphics, mouseX, mouseY);
        int x = getPosition().x;
        int y = getPosition().y;
        int width = getSize().width;
        int height = getSize().height;
        if (useScissor) {
            var trans = graphics.pose().last().pose();
            var realPos = trans.transform(new Vector4f(x, y, 0, 1));
            var realPos2 = trans.transform(new Vector4f(x + width, y + height, 0, 1));
            graphics.enableScissor((int) realPos.x, (int) realPos.y, (int) realPos2.x, (int) realPos2.y);
            if(!hookDrawInBackground(graphics, mouseX, mouseY, partialTicks)) {
                drawWidgetsBackground(graphics, mouseX, mouseY, partialTicks);
            }
            graphics.disableScissor();
        } else {
            if(!hookDrawInBackground(graphics, mouseX, mouseY, partialTicks)) {
                drawWidgetsBackground(graphics, mouseX, mouseY, partialTicks);
            }
        }

        if (xBarHeight > 0) {
            if (xBarB != null) {
                xBarB.draw(graphics, mouseX, mouseY, x, y + height - xBarHeight, width, xBarHeight);
            }
            if (xBarF != null) {
                int barWidth = (int) (width * 1.0f / getMaxWidth() * width);
                xBarF.draw(graphics, mouseX, mouseY, x + scrollXOffset * width * 1.0f / getMaxWidth(), y + height - xBarHeight, barWidth, xBarHeight);
            }
        }
        if (yBarWidth > 0) {
            if (yBarB != null) {
                yBarB.draw(graphics, mouseX, mouseY, x + width  - yBarWidth, y, yBarWidth, height);
            }
            if (yBarF != null) {
                int barHeight = (int) (height * 1.0f / getMaxHeight() * height);
                yBarF.draw(graphics, mouseX, mouseY, x + width  - yBarWidth, y + scrollYOffset * height * 1.0f / getMaxHeight(), yBarWidth, barHeight);
            }
        }
    }

    @Override
    public void drawOverlay(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = getPosition().x;
        int y = getPosition().y;
        int width = getSize().width;
        int height = getSize().height;
        if (useScissor) {
            var trans = graphics.pose().last().pose();
            var realPos = trans.transform(new Vector4f(x, y, 0, 1));
            var realPos2 = trans.transform(new Vector4f(x + width, y + height, 0, 1));
            graphics.enableScissor((int) realPos.x, (int) realPos.y, (int) realPos2.x, (int) realPos2.y);
            super.drawOverlay(graphics, mouseX, mouseY, partialTicks);
            graphics.disableScissor();
        } else {
            super.drawOverlay(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        lastDeltaX = 0;
        lastDeltaY = 0;
        if (xBarHeight > 0 && isOnXScrollPane(mouseX, mouseY)) {
            this.draggedOnXScrollBar = true;
            setFocus(true);
            return true;
        }
        else if (yBarWidth > 0 && isOnYScrollPane(mouseX, mouseY)) {
            this.draggedOnYScrollBar = true;
            setFocus(true);
            return true;
        } else if(isMouseOverElement(mouseX, mouseY)){
            if (checkClickedDragged(mouseX, mouseY, button)) {
                setFocus(true);
                return true;
            }
            setFocus(true);
            if (draggable) {
                this.draggedPanel = true;
                return true;
            }
            return false;
        }
        setFocus(false);
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    protected boolean checkClickedDragged(double mouseX, double mouseY, int button) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if(widget.isVisible()) {
                boolean result = widget.mouseClicked(mouseX, mouseY, button);
                if (waitToRemoved == null || !waitToRemoved.contains(widget))  {
                    if (widget instanceof IDraggable && ((IDraggable) widget).allowDrag(mouseX, mouseY, button)) {
                        draggedWidget = widget;
                        ((IDraggable) widget).startDrag(mouseX, mouseY);
                        if (selectedWidget != null && selectedWidget != widget) {
                            ((ISelected) selectedWidget).onUnSelected();
                        }
                        selectedWidget = widget;
                        ((ISelected) selectedWidget).onSelected();
                        return true;
                    }
                    if (widget instanceof ISelected && ((ISelected) widget).allowSelected(mouseX, mouseY, button)) {
                        if (selectedWidget != null && selectedWidget != widget) {
                            ((ISelected) selectedWidget).onUnSelected();
                        }
                        selectedWidget = widget;
                        ((ISelected) selectedWidget).onSelected();
                        return true;
                    }
                }
                if (result) return true;
            }
        }
        draggedWidget = null;
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        if (this.isMouseOverElement(mouseX, mouseY)) {
            if (super.mouseWheelMove(mouseX, mouseY, wheelDelta)) {
                setFocus(true);
                return true;
            }
            if (scrollable) {
                setFocus(true);
                if (isFocus()) {
                    int moveDelta = (int) (-Mth.clamp(wheelDelta, -1, 1) * 13);
                    if (scrollWheelDirection == ScrollWheelDirection.VERTICAL) {
                        if (getMaxHeight() - getSize().height > 0 || scrollYOffset > getMaxHeight() - getSize().height) {
                            setScrollYOffset(Mth.clamp(scrollYOffset + moveDelta, 0, getMaxHeight() - getSize().height));
                        }
                    } else {
                        if (getMaxWidth() - getSize().width > 0 || scrollXOffset > getMaxWidth() - getSize().width) {
                            setScrollXOffset(Mth.clamp(scrollXOffset + moveDelta, 0, getMaxWidth() - getSize().width));
                        }
                    }
                }
            }
            return true;
        }
        setFocus(false);
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        double dx = deltaX + lastDeltaX;
        double dy = deltaY + lastDeltaY;
        deltaX = (int) dx;
        deltaY = (int) dy;
        lastDeltaX = dx- deltaX;
        lastDeltaY = dy - deltaY;
        if (draggedOnXScrollBar && (getMaxWidth() - getSize().width > 0 || scrollYOffset > getMaxWidth() - getSize().width)) {
            setScrollXOffset((int) Mth.clamp(scrollXOffset + deltaX * getMaxWidth() / getSize().width, 0, getMaxWidth() - getSize().width));
            return true;
        } else if (draggedOnYScrollBar && (getMaxHeight() - getSize().height > 0 || scrollYOffset > getMaxHeight() - getSize().height)) {
            setScrollYOffset((int) Mth.clamp(scrollYOffset + deltaY * getMaxHeight() / getSize().height, 0, getMaxHeight() - getSize().height));
            return true;
        } else if (draggedWidget instanceof IDraggable draggableWidget) {
            if (draggableWidget.dragging(mouseX, mouseY, deltaX, deltaY)) {
                if (!draggableWidget.canDragOutRange()) {
                    if (draggedWidget.getPosition().x < getPosition().x) {
                        deltaX = getPosition().x - draggedWidget.getPosition().x;
                    } else if (draggedWidget.getPosition().x + draggedWidget.getSize().width + scrollXOffset > getPosition().x + getSize().width) {
                        deltaX = (getPosition().x + getSize().width) - (draggedWidget.getPosition().x + draggedWidget.getSize().width + scrollXOffset);
                    }
                    if (draggedWidget.getPosition().y < getPosition().y) {
                        deltaY = getPosition().y - draggedWidget.getPosition().y;
                    } else if (draggedWidget.getPosition().y + draggedWidget.getSize().height + scrollYOffset > getPosition().y + getSize().height) {
                        deltaY = (getPosition().y + getSize().height) - (draggedWidget.getPosition().y + draggedWidget.getSize().height + scrollYOffset);
                    }
                    isComputingMax = true;
                    draggedWidget.addSelfPosition((int) deltaX, (int) deltaY);
                    isComputingMax = false;
                } else {
                    draggedWidget.addSelfPosition((int) deltaX, (int) deltaY);
                }
            }
            computeMax();
            return true;
        } else if (draggedPanel) {
            setScrollXOffset((int) Mth.clamp(scrollXOffset - deltaX, 0, Math.max(getMaxWidth() - yBarWidth - getSize().width, 0)));
            setScrollYOffset((int) Mth.clamp(scrollYOffset - deltaY, 0, Math.max(getMaxHeight() - xBarHeight - getSize().height, 0)));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggedOnXScrollBar) {
            draggedOnXScrollBar = false;
        } else if (draggedOnYScrollBar) {
            draggedOnYScrollBar = false;
        } else if (draggedWidget != null) {
            ((IDraggable)draggedWidget).endDrag(mouseX, mouseY);
            draggedWidget = null;
        } else if (draggedPanel) {
            draggedPanel = false;
        } else {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public List<Rect2i> getGuiExtraAreas(Rect2i guiRect, List<Rect2i> list) {
        Rect2i rect2i = toRectangleBox();
        if (rect2i.getX() < guiRect.getX()
                || rect2i.getX() + rect2i.getWidth() >  guiRect.getX() + guiRect.getWidth()
                || rect2i.getY() < guiRect.getY()
                || rect2i.getY() + rect2i.getHeight() >  guiRect.getY() + guiRect.getHeight()){
            list.add(toRectangleBox());
        }
        return list;
    }

    public void setSelected(Widget widget) {
        if (widget instanceof ISelected) {
            if (selectedWidget != null && selectedWidget != widget) {
                ((ISelected) selectedWidget).onUnSelected();
            }
            selectedWidget = widget;
            ((ISelected) selectedWidget).onSelected();
        } else if (widget == null) {
            if (selectedWidget != null) {
                ((ISelected) selectedWidget).onUnSelected();
            }
            selectedWidget = null;
        }
    }

    @Override
    public CompoundTag serializeInnerNBT() {
        CompoundTag tag = super.serializeInnerNBT();
        tag.putInt("scrollXOffset", scrollXOffset);
        tag.putInt("scrollYOffset", scrollYOffset);
        tag.putInt("maxHeight", maxHeight);
        tag.putInt("maxWidth", maxWidth);
        return tag;
    }

    @Override
    public void deserializeInnerNBT(CompoundTag nbt) {
        super.deserializeInnerNBT(nbt);
        this.scrollXOffset= nbt.getInt("scrollXOffset");
        this.scrollYOffset= nbt.getInt("scrollYOffset");
        this.maxHeight= nbt.getInt("maxHeight");
        this.maxWidth= nbt.getInt("maxWidth");
        isComputingMax = true;
        for (Widget widget : widgets) {
            widget.addSelfPosition(-scrollXOffset, -scrollYOffset);
        }
        isComputingMax = false;
    }

    public interface IDraggable extends ISelected {
        default boolean allowDrag(double mouseX, double mouseY, int button) {
            return allowSelected(mouseX, mouseY, button);
        }
        default void startDrag(double mouseX, double mouseY) {}
        default boolean dragging(double mouseX, double mouseY, double deltaX, double deltaY) {return true;}
        default void endDrag(double mouseX, double mouseY) {}

        /**
         * @return if false, can not be dragged out of container size
         */
        default boolean canDragOutRange() {
            return false;
        }
    }

    public interface ISelected {
        boolean allowSelected(double mouseX, double mouseY, int button);
        default void onSelected() {}
        default void onUnSelected() {}
    }
}
