/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.server.ServerLifecycleHooks;

public class RecipeAccessor extends CustomObjectAccessor<Recipe> {

    public RecipeAccessor() {
        super(Recipe.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, Recipe value) {
        var type = BuiltInRegistries.RECIPE_TYPE.getKey(value.getType());
        var id = value.getId();
        CompoundTag tag = new CompoundTag();
        tag.putString("type", type == null ? "null" : type.toString());
        tag.putString("id", id.toString());
        return NbtTagPayload.of(tag);
    }

    @Override
    public Recipe deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload nbtTagPayload && nbtTagPayload.getPayload() instanceof CompoundTag tag) {
            var type = BuiltInRegistries.RECIPE_TYPE.get(new ResourceLocation(tag.getString("type")));
            var id = new ResourceLocation(tag.getString("id"));
            if (type != null) {
                RecipeManager recipeManager;
                if (RosmontisLib.isRemote()) {
                    recipeManager = Minecraft.getInstance().getConnection().getRecipeManager();
                } else {
                    recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
                }
                for (Recipe<?> recipe : recipeManager.getRecipes()) {
                    if (recipe.getType() == type && recipe.getId().equals(id)) {
                        return recipe;
                    }
                }
            }
        }
        return null;
    }
}
