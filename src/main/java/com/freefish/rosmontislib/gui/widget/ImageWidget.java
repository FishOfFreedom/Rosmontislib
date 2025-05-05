package com.freefish.rosmontislib.gui.widget;


import com.freefish.rosmontislib.client.utils.Position;
import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.annotation.NumberColor;
import com.freefish.rosmontislib.gui.editor.annotation.NumberRange;
import com.freefish.rosmontislib.gui.editor.configurator.IConfigurableWidget;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.texture.ResourceTexture;
import com.freefish.rosmontislib.gui.util.DrawerHelper;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@Configurable(name = "ldlib.gui.editor.register.widget.image", collapse = false)
@LDLRegister(name = "image", group = "widget.basic")
public class ImageWidget extends Widget implements IConfigurableWidget {

    @Configurable(name = "ldlib.gui.editor.name.border")
    @NumberRange(range = {-100, 100})
    @Getter
    private int border;
    @Configurable(name = "ldlib.gui.editor.name.border_color")
    @NumberColor
    @Getter
    private int borderColor = -1;

    private Supplier<IGuiTexture> textureSupplier;

    public ImageWidget() {
        this(0, 0, 50, 50, new ResourceTexture());
    }


    public ImageWidget(int xPosition, int yPosition, int width, int height, IGuiTexture area) {
        super(xPosition, yPosition, width, height);
        setImage(area);
    }

    public ImageWidget(int xPosition, int yPosition, int width, int height, Supplier<IGuiTexture> textureSupplier) {
        super(xPosition, yPosition, width, height);
        setImage(textureSupplier);
    }

    public ImageWidget setImage(IGuiTexture area) {
        setBackground(area);
        return this;
    }

    public ImageWidget setImage(Supplier<IGuiTexture> textureSupplier) {
        this.textureSupplier = textureSupplier;
        if (textureSupplier != null) {
            setBackground(textureSupplier.get());
        }
        return this;
    }

    public IGuiTexture getImage() {
        return backgroundTexture;
    }

    public ImageWidget setBorder(int border, int color) {
        this.border = border;
        this.borderColor = color;
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateScreen() {
        super.updateScreen();
        if (textureSupplier != null) {
            setBackground(textureSupplier.get());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        Position position = getPosition();
        Size size = getSize();
        if (border > 0) {
            DrawerHelper.drawBorder(graphics, position.x, position.y, size.width, size.height, borderColor, border);
        }
        drawOverlay(graphics, mouseX, mouseY, partialTicks);
    }
}

