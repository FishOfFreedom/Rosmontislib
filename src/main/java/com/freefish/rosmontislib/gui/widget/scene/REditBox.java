package com.freefish.rosmontislib.gui.widget.scene;

import com.freefish.rosemarylib.gui.scene.input.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class REditBox extends RLControl{
    protected EditBox textField;

    protected int maxStringLength = Integer.MAX_VALUE;
    protected Predicate<String> textValidator = (s)->true;
    protected Consumer<String> textResponder;

    protected String currentString;

    protected boolean isBordered;

    protected int textColor = -1;

    protected float wheelDur;
    protected NumberFormat numberInstance;
    private boolean isDragging;

    public REditBox() {
        this(0, 0, 60, 15);
    }

    public REditBox(int xPosition, int yPosition, int width, int height) {
        Font fontRenderer = Minecraft.getInstance().font;
        this.textField = new EditBox(fontRenderer, xPosition, yPosition, width, height, Component.literal("text field"));
        this.textField.setBordered(true);
        isBordered = true;
        this.textField.setMaxLength(this.maxStringLength);
        this.textField.setResponder(this::onTextChanged);
    }

    public REditBox setTextResponder(Consumer<String> textResponder) {
        this.textResponder = textResponder;
        return this;
    }

    public REditBox setTextValidator(Predicate<String> textValidator) {
        this.textValidator = textValidator;
        this.textField.setFilter(textValidator);
        return this;
    }

    public REditBox setCurrentString(Object currentString) {
        this.currentString = currentString.toString();

        if (!this.textField.getValue().equals(currentString)) {
            this.textField.setValue(currentString.toString());
        }

        return this;
    }

    public String getCurrentString() {
        return this.currentString == null ? "" : this.currentString;
    }

    public String getRawCurrentString() {
        return textField.getValue();
    }

    @Override
    public void onFocusChanged(@Nullable RLNode lastFocus, RLNode focus) {
        this.textField.setFocused(isFocus());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.textField.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, MouseButton pButton) {
        boolean mouseOver = isMouseOver();
        if (mouseOver) {
            isDragging = true;
        }
        setFocus(mouseOver);
        this.textField.setFocused(mouseOver);
        return this.textField.mouseClicked(pMouseX,pMouseY,pButton.id);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, MouseButton button) {
        isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return keyCode != 256 && (this.textField.keyPressed(keyCode, scanCode, modifiers) || isFocus());
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.textField.charTyped(codePoint, modifiers);
    }

    protected void onTextChanged(String newTextString) {
        String lastText = currentString;
        boolean newText = textValidator.test(newTextString);
        if (newText) {
            this.textField.setTextColor(textColor);
            setCurrentString(newTextString);
            if (textResponder != null) {
                textResponder.accept(newTextString);
            }
        }
        //else if (!newTextString.equals(newText)){
        //    this.textField.setTextColor(0xffdf0000);
        //} else {
        //    this.textField.setTextColor(textColor);
        //}
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        if (wheelDur > 0 && numberInstance != null && isMouseOver() && isFocus()) {
            try {
                onTextChanged(numberInstance.format(Float.parseFloat(getCurrentString()) + (scroll > 0 ? 1 : -1) * wheelDur));
            } catch (Exception ignored) {
            }
            setFocus(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double x, double y, MouseButton button, double dragX, double dragY) {
        if (isDragging && numberInstance != null && isFocus()) {
            try {
                onTextChanged(numberInstance.format(Float.parseFloat(getCurrentString()) + dragX * wheelDur));
            } catch (Exception ignored) {
            }
            setFocus(true);
            return true;
        }
        return super.mouseDragged(x, y, button, dragX, dragY);
    }
}
