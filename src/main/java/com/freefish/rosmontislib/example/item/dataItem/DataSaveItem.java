package com.freefish.rosmontislib.example.item.dataItem;

import com.freefish.rosmontislib.item.ItemAdditionData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class DataSaveItem extends Item implements ItemAdditionData<ItemData> {

    public DataSaveItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if (!level.isClientSide) {
            if(player.isShiftKeyDown()) {
                if(pUsedHand == InteractionHand.MAIN_HAND){
                    ItemStack mainHandItem = player.getMainHandItem();
                    ItemData itemAdditionData = getItemAdditionData(mainHandItem, ItemData::new);
                    player.sendSystemMessage(Component.literal(String.valueOf(itemAdditionData.a)));

                    itemAdditionData.a+=1;
                    setItemAdditionData(mainHandItem,itemAdditionData);
                }else {

                }
            }
            else {
            }
        }

        return super.use(level, player, pUsedHand);
    }

    @Override
    public String getSaveDataName() {
        return "rltestitemdata";
    }

    @Override
    public void addAdditionTextTool(ItemTooltipEvent event) {
        List<Component> toolTip = event.getToolTip();
        toolTip.add(Component.literal(String.valueOf(getItemAdditionData(event.getItemStack(),ItemData::new).a)));
    }
}
