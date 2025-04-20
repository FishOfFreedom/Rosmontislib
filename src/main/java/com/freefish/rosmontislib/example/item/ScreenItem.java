package com.freefish.rosmontislib.example.item;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.client.particle.advance.base.particle.RLParticle;
import com.freefish.rosmontislib.client.particle.advance.data.EmissionSetting;
import com.freefish.rosmontislib.client.particle.advance.data.UVAnimationSetting;
import com.freefish.rosmontislib.client.particle.advance.data.VelocityOverLifetimeSetting;
import com.freefish.rosmontislib.client.particle.advance.data.material.CustomShaderMaterial;
import com.freefish.rosmontislib.client.particle.advance.data.material.MaterialHandle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction3;
import com.freefish.rosmontislib.client.particle.advance.data.number.RandomConstant;
import com.freefish.rosmontislib.client.particle.advance.data.number.color.Gradient;
import com.freefish.rosmontislib.client.particle.advance.data.number.color.GradientHandle;
import com.freefish.rosmontislib.client.particle.advance.data.shape.Circle;
import com.freefish.rosmontislib.client.particle.advance.effect.BlockEffect;
import com.freefish.rosmontislib.client.particle.advance.effect.EntityEffect;
import com.freefish.rosmontislib.client.utils.GradientColor;
import com.freefish.rosmontislib.client.utils.Range;
import com.freefish.rosmontislib.gui.RGuiHandle;
import com.freefish.rosmontislib.gui.widget.panel.RHBox;
import com.freefish.rosmontislib.gui.widget.panel.RVBox;
import com.freefish.rosmontislib.gui.widget.scene.RButton;
import com.freefish.rosmontislib.gui.widget.scene.RImageView;
import com.freefish.rosmontislib.gui.guiproxy.RLScene;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;


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
                    }
                }));

                RLScene rlScene = new RLScene(hBox, 384, 240);
                rlScene.setSceneCentre(true);
                rlScene.setRenderBackGround(false);
                RGuiHandle.INSTANCE.openGui(rlScene);
            }
            //RLParticle rlParticle = new RLParticle();
            //rlParticle.config.getShape().setShape(new Circle());
            //rlParticle.config.getMaterial().setMaterial(MaterialHandle.GLOW);
            //rlParticle.config.getColorOverLifetime().setEnable(true);
            //GradientColor gradientColor = new GradientColor(0X000000FF, 0XFF0000FF, 0X000000FF);
            //rlParticle.config.getColorOverLifetime().setColor(new Gradient(gradientColor));
            //rlParticle.config.getVelocityOverLifetime().setEnable(true);
            //rlParticle.config.trails.setEnable(true);
            //rlParticle.config.trails.config.material.setMaterial(MaterialHandle.GLOW);
            //rlParticle.config.getVelocityOverLifetime().setOrbitalMode(VelocityOverLifetimeSetting.OrbitalMode.AngularVelocity);
            //rlParticle.config.getVelocityOverLifetime().setOrbital(new NumberFunction3(
            //        NumberFunction.constant(1),
            //        NumberFunction.constant(1),
            //        NumberFunction.constant(1)
            //));
            //rlParticle.emmit(new EntityEffect(world,player));
            BlockEffect blockEffect = new BlockEffect(world,player.getOnPos());

            RLParticle rlParticle1 = new RLParticle();
            rlParticle1.config.setDuration(50);
            rlParticle1.config.setStartLifetime(NumberFunction.constant(30));
            rlParticle1.config.setStartSpeed(NumberFunction.constant(-8));
            rlParticle1.config.setStartSize(new NumberFunction3(1.5f));

            rlParticle1.config.getEmission().setEmissionRate(NumberFunction.constant(3));
            EmissionSetting.Burst burst = new EmissionSetting.Burst();burst.setCount(NumberFunction.constant(60));
            rlParticle1.config.getEmission().addBursts(burst);

            rlParticle1.config.getMaterial().setMaterial(MaterialHandle.SMOKE);

            Circle circle = new Circle();circle.setRadius(16);circle.setRadiusThickness(0.2f);
            rlParticle1.config.getShape().setShape(circle);

            rlParticle1.config.getVelocityOverLifetime().open();
            rlParticle1.config.getVelocityOverLifetime().setOrbitalMode(VelocityOverLifetimeSetting.OrbitalMode.LinearVelocity);
            rlParticle1.config.getVelocityOverLifetime().setOrbital(new NumberFunction3(0,-4,0));

            rlParticle1.config.getColorOverLifetime().open();
            rlParticle1.config.getColorOverLifetime().setColor(new Gradient(GradientHandle.CENTER_OPAQUE));

            rlParticle1.config.getUvAnimation().open();
            rlParticle1.config.getUvAnimation().setTiles(new Range(2,2));
            rlParticle1.config.getUvAnimation().setAnimation(UVAnimationSetting.Animation.SingleRow);

            rlParticle1.config.trails.open();
            rlParticle1.config.trails.config.getMaterial().setMaterial(MaterialHandle.SMOKE);

            RLParticle rlParticle2 = new RLParticle();
            rlParticle2.config.setDuration(50);
            rlParticle2.transform.position(new Vector3f(0,7,0));
            rlParticle2.config.setStartLifetime(NumberFunction.constant(30));
            rlParticle2.config.setStartSpeed(NumberFunction.constant(-8));
            rlParticle2.config.setStartSize(new NumberFunction3(new RandomConstant(0.5,0.1,true)));

            rlParticle2.config.getEmission().setEmissionRate(NumberFunction.constant(0.5));

            rlParticle2.config.getMaterial().setMaterial(MaterialHandle.VOID);

            Circle circle2 = new Circle();circle2.setRadius(16);circle2.setRadiusThickness(0.2f);
            rlParticle2.config.getShape().setShape(circle2);

            rlParticle2.config.getVelocityOverLifetime().open();
            rlParticle2.config.getVelocityOverLifetime().setOrbitalMode(VelocityOverLifetimeSetting.OrbitalMode.LinearVelocity);
            rlParticle2.config.getVelocityOverLifetime().setOrbital(new NumberFunction3(0,-4,0));
            rlParticle2.config.getVelocityOverLifetime().setLinear(new NumberFunction3(0,-5,0));

            rlParticle2.config.getColorOverLifetime().open();
            rlParticle2.config.getColorOverLifetime().setColor(new Gradient(GradientHandle.CENTER_OPAQUE));

            rlParticle2.config.trails.open();
            rlParticle2.config.trails.config.getMaterial().setMaterial(MaterialHandle.CIRCLE);

            RLParticle rlParticle3 = new RLParticle();
            rlParticle3.config.setDuration(40);
            rlParticle3.config.setStartLifetime(NumberFunction.constant(20));
            rlParticle3.config.setStartSpeed(NumberFunction.constant(-8));
            rlParticle3.config.setStartSize(new NumberFunction3(NumberFunction.constant(1)));

            rlParticle3.config.getEmission().setEmissionRate(NumberFunction.constant(0));
            EmissionSetting.Burst burst3 = new EmissionSetting.Burst();burst3.time = 10;burst3.setCount(NumberFunction.constant(2));
            burst3.cycles = 0;
            rlParticle3.config.getEmission().addBursts(burst3);

            rlParticle3.config.getMaterial().setMaterial(MaterialHandle.SMOKE);

            Circle circle3 = new Circle();circle3.setRadius(8);circle3.setRadiusThickness(0.2f);
            rlParticle3.config.getShape().setShape(circle3);

            rlParticle3.config.getVelocityOverLifetime().open();
            rlParticle3.config.getVelocityOverLifetime().setOrbitalMode(VelocityOverLifetimeSetting.OrbitalMode.LinearVelocity);
            rlParticle3.config.getVelocityOverLifetime().setOrbital(new NumberFunction3(0,-4,0));

            rlParticle3.config.getColorOverLifetime().open();
            rlParticle3.config.getColorOverLifetime().setColor(new Gradient(GradientHandle.CENTER_OPAQUE));

            rlParticle3.config.getUvAnimation().open();
            rlParticle3.config.getUvAnimation().setTiles(new Range(2,2));
            rlParticle3.config.getUvAnimation().setAnimation(UVAnimationSetting.Animation.SingleRow);

            rlParticle3.config.trails.open();
            rlParticle3.config.trails.config.getMaterial().setMaterial(MaterialHandle.SMOKE);


            rlParticle1.emmit(blockEffect);
            rlParticle2.emmit(new BlockEffect(world,player.getOnPos().above(7)));
            rlParticle3.emmit(blockEffect);
        }
        return super.use(world, player, hand);
    }
}
