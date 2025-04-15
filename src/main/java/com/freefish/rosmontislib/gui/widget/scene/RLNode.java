package com.freefish.rosmontislib.gui.widget.scene;

import com.freefish.rosmontislib.gui.widget.property.RIntProperty;
import com.freefish.rosmontislib.gui.widget.scene.input.MouseButton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.function.Consumer;

import static net.minecraft.client.gui.components.AbstractWidget.WIDGETS_LOCATION;

public abstract class RLNode {
    private int width = 100;
    private int height = 100;
    private int layoutX = 0;
    private int layoutY = 0;
    private int interval = 0;
    private float r = 1;
    private float g = 1;
    private float b = 1;
    private float a = 1;
    private boolean isOver = false;
    private RLNode parent;
    private RBackGround backGround;
    private Consumer<Boolean> mouseActionEvent;
    private Runnable tickEvent;
    private boolean isVisible = true;
    private Component component = Component.literal("");
    private final static Font font = Minecraft.getInstance().font;

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }


    public RLNode(float xSize,float ySize){
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public RBackGround getBackGround() {
        return backGround;
    }

    public void setBackGround(RBackGround backGround) {
        this.backGround = backGround;
    }

    public void tick(){
        if(tickEvent!=null) tickEvent.run();
    }

    public void setTickEvent(Runnable tickEvent){
        this.tickEvent=tickEvent;
    }


    public RLNode(){}

    public RLNode getParent() {
        return parent;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setParent(RLNode parent) {
        this.parent = parent;
    }

    public int getWidth() {
        return width- getInterval()*2;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height- getInterval()*2;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLayoutX() {
        return layoutX+ getInterval();
    }

    public int getTranslateX() {
        return (nodeTransformation == null)
                ? DEFAULT_TRANSLATE
                : nodeTransformation.getTranslateX();
    }

    public void setLayoutX(int layoutX) {
        this.layoutX = layoutX;
    }

    public int getLayoutY() {
        return layoutY+ getInterval();
    }

    public int getTranslateY() {
        return (nodeTransformation == null)
                ? DEFAULT_TRANSLATE
                : nodeTransformation.getTranslateY();
    }

    public void setLayoutY(int layoutY) {
        this.layoutY = layoutY;
    }

    public void setOver(boolean over) {
        if(isOver==over) return;
        if(mouseActionEvent !=null) mouseActionEvent.accept(over);
        isOver = over;
    }

    public void setColor(int color) {
        r = FastColor.ARGB32.red(color)/255f;
        g = FastColor.ARGB32.green(color)/255f;
        b = FastColor.ARGB32.blue(color)/255f;
        a = FastColor.ARGB32.alpha(color)/255f;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackGround(graphics,mouseX,mouseY,partialTicks);
    }

    public void renderBackGround(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RBackGround backGround1 = getBackGround();
        int layoutX = getLayoutX();
        int layoutY = getLayoutY();
        int width = getWidth();
        int height = getHeight();

        if(backGround1!=null){

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            graphics.setColor(r, g, b, a);
            graphics.blit(backGround1.getResourceLocation(), layoutX, layoutY, 0, 0, width, height, width, height);
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }else {
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0f);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            graphics.blitNineSliced(WIDGETS_LOCATION, layoutX, layoutY, width, height, 20, 4, 200, 20, 0, 66);
            graphics.drawString(font,getComponent().getString(),layoutX, layoutY,0XFFFFFFFF);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            if(isMouseOver()) {

            }
        }
    }

    public boolean mouseScrolled(double x, double y, double scroll) {
        return false;
    }

    public boolean mouseDragged(double x, double y, MouseButton button, double dragX, double dragY) {
        return false;
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, MouseButton pButton) {
        return false;
    }

    public boolean mouseReleased(double pMouseX, double pMouseY, MouseButton pButton) {
        return false;
    }

    public final boolean isMouseOver() {
        return isOver;
    }

    public boolean checkMouseOver(int mouseX, int mouseY) {
        return mouseX >= layoutX && mouseY >= layoutY && mouseX < layoutX + width && mouseY < layoutY + height;
    }

    public void updateMouseOver(int mouseX, int mouseY) {
        setOver(checkMouseOver(mouseX, mouseY));
    }

    public void updateGui(int mouseX, int mouseY, float partialTicks) {
    }

    public void copyPos(RLNode rlNode){
        setHeight(rlNode.getHeight());
        setWidth(rlNode.getWidth());
        setLayoutX(rlNode.getLayoutX());
        setLayoutY(rlNode.getLayoutY());
    }

    public void setPos(int layoutX,int layoutY,int width,int height){
        setHeight(height);
        setWidth(width);
        setLayoutX(layoutX);
        setLayoutY(layoutY);
    }

    public void setOnMouseEnter(Consumer<Boolean> mouseActionEvent){
        this.mouseActionEvent = mouseActionEvent;
    }

    public float getxSize() {
        return xSize;
    }

    public void setxSize(float xSize) {
        this.xSize = xSize;
    }

    public float getySize() {
        return ySize;
    }

    public void setySize(float ySize) {
        this.ySize = ySize;
    }

    float xSize;
    float ySize;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Component getComponent() {
        return component;
    }

    public void setName(Component component) {
        this.component = component;
    }

    public void getName(Component component) {
        this.component = component;
    }

    //animation
    private NodeTransformation nodeTransformation;

    private NodeTransformation getNodeTransformation() {
        if (nodeTransformation == null) {
            nodeTransformation = new NodeTransformation();
        }

        return nodeTransformation;
    }

    private static final int DEFAULT_TRANSLATE = 0;

    private class NodeTransformation{
        private RIntProperty translateX;
        private RIntProperty translateY;

        public int getTranslateX() {
            return (translateX == null) ? DEFAULT_TRANSLATE
                    : translateX.get();
        }

        public RIntProperty translateXProperty() {
            if (translateX == null) {
                translateX = new RIntProperty();
            }
            return translateX;
        }

        public int getTranslateY() {
            return (translateY == null) ? DEFAULT_TRANSLATE
                    : translateY.get();
        }

        public RIntProperty translateYProperty() {
            if (translateY == null) {
                translateY = new RIntProperty();
            }
            return translateY;
        }
    }
}
