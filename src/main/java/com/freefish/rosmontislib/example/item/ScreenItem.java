package com.freefish.rosmontislib.example.item;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.client.particle.advance.base.particle.RLParticle;
import com.freefish.rosmontislib.client.particle.advance.base.particle.RLParticleConfig;
import com.freefish.rosmontislib.gui.RGuiHandle;
import com.freefish.rosmontislib.gui.widget.panel.RHBox;
import com.freefish.rosmontislib.gui.widget.panel.RVBox;
import com.freefish.rosmontislib.gui.widget.scene.RButton;
import com.freefish.rosmontislib.gui.widget.scene.RImageView;
import com.freefish.rosmontislib.gui.widget.scene.RLScene;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class ScreenItem extends Item {
    private static ResourceLocation RING1 = new ResourceLocation(RosmontisLib.MOD_ID,"textures/gui/skill_outline.png");
    private static ResourceLocation RING2= new ResourceLocation(RosmontisLib.MOD_ID, "textures/gui/skill_radial_mask.png");

    public ScreenItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(world.isClientSide) {
            if(false){
                RHBox hBox = new RHBox();
                RVBox vBox = new RVBox();

                RButton button = new RButton();

                RImageView imageView1 = new RImageView(RING1);
                RImageView imageView2 = new RImageView(RING2);
                RImageView imageView3 = new RImageView(RING2);

                imageView2.setInterval(3);
                imageView3.setInterval(13);

                vBox.addChildren(imageView2);
                vBox.addChildren(button);
                hBox.addChildren(imageView3);
                hBox.addChildren(vBox);

                button.setOnAction((isPress -> {
                    if (isPress) {
                        System.out.println(12);
                    }
                }));

                RLScene rlScene = new RLScene(hBox, 384, 240);
                rlScene.setSceneCentre(true);
                rlScene.setRenderBackGround(false);
                RGuiHandle.INSTANCE.openGui(rlScene);
            }
            RLParticle rlParticle = new RLParticle();
            rlParticle.config.getRenderer().setBloomEffect(true);
            rlParticle.emmit(null);
        }
        return super.use(world, player, hand);
    }
}
