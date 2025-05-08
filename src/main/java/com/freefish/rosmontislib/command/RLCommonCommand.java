package com.freefish.rosmontislib.command;

import com.freefish.rosmontislib.levelentity.LevelEntityHandle;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class RLCommonCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("rosmontislib").requires((require) -> require.hasPermission(2))
                        .then(Commands.literal("levelentity")
                                .then(Commands.literal("check")
                                        .executes(context -> {
                                            ServerLevel level = context.getSource().getLevel();
                                            LevelEntityManager instance = LevelEntityManager.getInstance(level);
                                            context.getSource().sendSuccess(() -> Component.literal(String.valueOf(instance.levelEntityList().size())),false);
                                            return 1;
                                        })
                                )
                                .then(Commands.literal("add")
                                        .executes(context -> {
                                            ServerLevel level = context.getSource().getLevel();
                                            LevelEntityManager instance = LevelEntityManager.getInstance(level);
                                            instance.addLevelEntityToWorld(level, LevelEntityHandle.EXAMPLE);
                                            return 1;
                                        })
                                )
                        )
        );
    }
}