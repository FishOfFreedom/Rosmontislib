package com.freefish.rosmontislib.example.item.help;

import com.freefish.rosmontislib.client.RLClientUseUtils;
import com.freefish.rosmontislib.commom.init.DamageSourceHandle;
import com.freefish.rosmontislib.gui.factory.UIEditorFactory;
import com.freefish.rosmontislib.levelentity.LevelEntityExample1;
import com.freefish.rosmontislib.levelentity.LevelEntityHandle;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class TestItem extends Item {
    private Mode mode = Mode.MOVE;

    public TestItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if (!level.isClientSide) {
            if(player.isShiftKeyDown()) {
                mode = mode.changeMode();
                player.sendSystemMessage(Component.translatable(mode.getName()));
            }
            else {
                if(mode==Mode.MOVE) {
                }
                else if(mode==Mode.CLEAN_ENTITY){
                    List<LivingEntity> entitiesOfClass = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5));
                    for(LivingEntity living:entitiesOfClass){
                        if(living==player) continue;
                        living.hurt(DamageSourceHandle.realDamage(player),5);
                    }
                }else if(mode==Mode.START_DIALOGUE){
                    RLClientUseUtils.StartCameraShake(level,player.position().add(100,0,0),200,0.1f,10,10);
                }
                else if(mode==Mode.PLAY_ANIMATION){
                    UIEditorFactory.INSTANCE.openUI(UIEditorFactory.INSTANCE,
                            (ServerPlayer) player);
                }
            }
        }
        if(mode==Mode.PARTICLE){
            if(level.isClientSide){
            }
        }

        if(mode==Mode.GUI &&!level.isClientSide){

        }
        if(mode==Mode.DIALOGUE){
            LevelEntityManager instance = LevelEntityManager.getInstance(level);
            if(instance!=null){
                LevelEntityExample1 instanceLevelEntity = instance.getInstanceLevelEntity(LevelEntityHandle.EXAMPLE_INSTANCE);
                if(instanceLevelEntity!=null){
                    instanceLevelEntity.testint++;
                }
            }
        }
        
        return super.use(level, player, pUsedHand);
    }


    enum Mode{
        MOVE("move"),CLEAN_ENTITY("clean_entity"),PLAY_ANIMATION("play_animation"),PARTICLE("particle"),START_DIALOGUE("start_dialogue"), GUI("gui"),DIALOGUE("dialogue");

        Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        final String name;

        public Mode changeMode(){
            return switch (this){
                default -> MOVE;
                case MOVE -> PARTICLE;
                case PARTICLE -> PLAY_ANIMATION;
                case PLAY_ANIMATION -> CLEAN_ENTITY;
                case CLEAN_ENTITY -> START_DIALOGUE;
                case START_DIALOGUE -> GUI;
                case GUI -> DIALOGUE;
            };
        }
    }
}
