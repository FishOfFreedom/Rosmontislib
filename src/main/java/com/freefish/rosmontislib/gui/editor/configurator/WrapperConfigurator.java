/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.configurator;

import com.freefish.rosmontislib.client.utils.Position;
import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.widget.Widget;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Function;

/**
 * @author KilaBash
 * @date 2022/12/3
 * @implNote WrapperConfigurator
 */
@Accessors(chain = true)
public class WrapperConfigurator extends Configurator {
    public final Widget inner;
    @Setter
    public boolean removeTitleBar;

    public WrapperConfigurator(Widget widget) {
        this("", widget, true);
    }

    public WrapperConfigurator(Function<WrapperConfigurator, Widget> widgetSupplier) {
        this("", widgetSupplier, true);
    }

    public WrapperConfigurator(String name, Widget widget) {
        this(name, widget, false);
    }

    public WrapperConfigurator(String name, Function<WrapperConfigurator, Widget> widgetSupplier) {
        this(name, widgetSupplier, false);
    }

    public WrapperConfigurator(String name, Widget widget, boolean removeTitleBar) {
        this(name, configurator -> widget, removeTitleBar);
    }

    public WrapperConfigurator(String name, Function<WrapperConfigurator, Widget> widgetSupplier, boolean removeTitleBar) {
        super(name);
        this.inner = widgetSupplier.apply(this);
        this.removeTitleBar = removeTitleBar;
    }

    @Override
    public void computeHeight() {
        super.computeHeight();
        if (removeTitleBar) {
            setSize(new Size(getSize().width, inner.getSize().height + 4));
        } else {
            setSize(new Size(getSize().width, inner.getSize().height + 19));
        }
    }

    @Override
    public void init(int width) {
        super.init(width);
        Size size = inner.getSize();
        inner.setSelfPosition(new Position((width - size.width) / 2, removeTitleBar ? 2 : 17));
        addWidget(inner);
    }
}
