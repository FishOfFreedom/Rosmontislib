package com.freefish.rosmontislib.command;

import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.freefish.rosmontislib.client.particle.advance.base.FXObject;
import com.freefish.rosmontislib.mixin.accessor.ParticleEngineAccessor;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RLCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("rosmontislib").requires((require) -> require.hasPermission(2))
                .then(Commands.literal("clear_particle")
                        .executes(context -> {
                            if (Minecraft.getInstance().particleEngine instanceof ParticleEngineAccessor accessor) {
                                accessor.getParticles().entrySet().removeIf(entry ->
                                        entry.getKey() instanceof RLParticleRenderType ||
                                                entry.getKey() == FXObject.NO_RENDER_RENDER_TYPE);
                            }
                            return 1;
                        })));
    }
}