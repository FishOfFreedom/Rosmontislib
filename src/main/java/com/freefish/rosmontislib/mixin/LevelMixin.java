package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.levelentity.ILevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public class LevelMixin implements ILevelEntityManager {
    private LevelEntityManager levelEntityManager;

    @Override
    public LevelEntityManager getLevelEntityManager() {
        return levelEntityManager;
    }

    @Override
    public void setLevelEntityManager(LevelEntityManager levelEntityManager) {
        this.levelEntityManager = levelEntityManager;
    }
}
