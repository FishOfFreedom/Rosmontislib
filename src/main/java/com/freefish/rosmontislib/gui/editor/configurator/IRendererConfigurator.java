package com.freefish.rosmontislib.gui.editor.configurator;

import com.freefish.rosmontislib.client.renderer.IRenderer;
import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.widget.ImageWidget;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IRendererConfigurator extends ValueConfigurator<IRenderer>{
    //todo uselessWidget
    private ImageWidget frame;
    //private RendererBlockEntity holder;

    public IRendererConfigurator(String name, Supplier<IRenderer> supplier, Consumer<IRenderer> onUpdate, IRenderer defaultRenderer, boolean forceUpdate) {
        super(name, supplier, onUpdate, defaultRenderer, forceUpdate);
        if (value == null) {
            value = defaultValue;
        }
    }

    @Override
    protected void onValueUpdate(IRenderer newValue) {
        if (Objects.equals(newValue, value)) return;
        super.onValueUpdate(newValue);
        //holder.setRenderer(newValue);
    }

    @Override
    public void computeHeight() {
        super.computeHeight();
        setSize(new Size(getSize().width, 15 + frame.getSize().height + 6));
    }

    @Override
    public void init(int width) {
        super.init(width);
        int w = Math.min(width - 6, 100);
        int x = (width - w) / 2;

        addWidget(frame = new ImageWidget(x, 17, w, w, IGuiTexture.EMPTY).setBorder(2, ColorPattern.T_WHITE.color));

        //var level = new TrackedDummyWorld();
        //level.addBlock(BlockPos.ZERO, BlockInfo.fromBlock(RendererBlock.BLOCK));
        //holder = (RendererBlockEntity) level.getBlockEntity(BlockPos.ZERO);
        //assert holder != null;
        //holder.setRenderer(value);

        //var sceneWidget = new SceneWidget(x, 17, w, w, level);
        //sceneWidget.setRenderFacing(false);
        //sceneWidget.setRenderSelect(false);
        //sceneWidget.createScene(level);
        //sceneWidget.getRenderer().setOnLookingAt(null); // better performance
        //sceneWidget.setRenderedCore(Collections.singleton(BlockPos.ZERO), null);

        //addWidget(sceneWidget);
//
        //sceneWidget.setDraggingConsumer(
        //        o -> o instanceof IRenderer,
        //        o -> frame.setBorder(2, ColorPattern.GREEN.color),
        //        o -> frame.setBorder(2, ColorPattern.T_WHITE.color),
        //        o -> {
        //            if (o instanceof IRenderer renderer) {
        //                onValueUpdate(renderer);
        //                updateValue();
        //            }
        //            frame.setBorder(2, ColorPattern.T_WHITE.color);
        //        });
    }

}
