/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.ui.resource;

import com.freefish.rosmontislib.client.renderer.IRenderer;
import com.freefish.rosmontislib.gui.editor.Icons;
import com.freefish.rosmontislib.gui.editor.configurator.IConfigurable;
import com.freefish.rosmontislib.gui.editor.data.resource.Resource;
import com.freefish.rosmontislib.gui.editor.ui.ConfigPanel;
import com.freefish.rosmontislib.gui.editor.ui.ResourcePanel;
import com.freefish.rosmontislib.gui.widget.Widget;

public class IRendererResourceContainer extends ResourceContainer<IRenderer, Widget> {
    //todo uselessWidget

    public IRendererResourceContainer(Resource<IRenderer> resource, ResourcePanel panel) {
        super(resource, panel);
        //setWidgetSupplier(k -> createPreview(getResource().getResource(k)));
        //setDragging(key -> new UIResourceRenderer(resource, key),
        //        (k, o, p) -> new TextTexture(resource.getResourceName(k)));
        setOnEdit(key -> {
            if (getResource().getResource(key) instanceof IConfigurable configurable) {
                getPanel().getEditor().getConfigPanel().openConfigurator(ConfigPanel.Tab.RESOURCE, configurable);
            } else {
                getPanel().getEditor().getConfigPanel().clearAllConfigurators(ConfigPanel.Tab.RESOURCE);
            }
        });
        setCanEdit(key -> key.left().isEmpty() || !resource.getResourceName(key).equals("empty"));
        setCanGlobalChange(key -> key.left().isEmpty() || !resource.getResourceName(key).equals("empty"));
        setCanRemove(key -> key.left().isEmpty() || !resource.getResourceName(key).equals("empty"));
        setOnMenu((selected, m) -> m.branch(Icons.ADD_FILE, "ldlib.gui.editor.menu.add_renderer", menu -> {
            //for (var entry : AnnotationDetector.REGISTER_RENDERERS.entrySet()) {
            //    menu.leaf("ldlib.renderer.%s".formatted(entry.getKey()), () -> {
                    //var renderer = entry.getValue().creator().get();
                    //renderer.initRenderer();
                    //resource.addBuiltinResource(genNewFileName(), renderer);
                    //reBuild();
            //    });
            //}
        }));
    }

    //protected SceneWidget createPreview(IRenderer renderer) {
    //    var level = new TrackedDummyWorld();
    //    level.addBlock(BlockPos.ZERO, BlockInfo.fromBlock(RendererBlock.BLOCK));
    //    Optional.ofNullable(level.getBlockEntity(BlockPos.ZERO)).ifPresent(blockEntity -> {
    //        if (blockEntity instanceof RendererBlockEntity holder) {
    //            holder.setRenderer(renderer);
    //        }
    //    });
    //    var sceneWidget = new SceneWidget(0, 0, 50, 50, null);
    //    sceneWidget.setRenderFacing(false);
    //    sceneWidget.setRenderSelect(false);
    //    sceneWidget.setScalable(false);
    //    sceneWidget.setDraggable(false);
    //    sceneWidget.setIntractable(false);
    //    sceneWidget.createScene(level);
    //    sceneWidget.getRenderer().setOnLookingAt(null); // better performance
    //    sceneWidget.setRenderedCore(Collections.singleton(BlockPos.ZERO), null);
    //    return sceneWidget;
    //}

}
