package com.freefish.rosmontislib.client.particle.advance;

import com.freefish.rosmontislib.client.particle.advance.base.FXObject;
import com.freefish.rosmontislib.client.particle.advance.base.IParticleEmitter;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public abstract class AdvancedRLParticleBase extends FXObject implements IParticleEmitter {
    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    // runtime
    protected int delay;
    @Nullable
    protected Vector3f previousPosition;
    protected Vector3f velocity = new Vector3f();

    protected float t;

    public RandomSource getThreadSafeRandomSource() {
        return threadSafeRandomSource;
    }

    private final RandomSource threadSafeRandomSource = RandomSource.createThreadSafe();

    protected ConcurrentHashMap<Object, Float> memRandom = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BlockPos, Integer> lightCache = new ConcurrentHashMap<>();

    protected AdvancedRLParticleBase() {
        this.friction = 1;
    }

    public RandomSource getRandomSource() {
        return random;
    }

    @Override
    public final void tick() {
        super.tick();
        if (!isAlive()) {
            return;
        }

        if (delay > 0) {
            delay--;
            return;
        }

        lightCache.clear();
        updateOrigin();
        update();
    }

    @Override
    public void setPos(double x, double y, double z) {
    }

    @Override
    public Vector3f getVelocity() {
        return new Vector3f(velocity);
    }

    protected void update() {
        if (this.age >= getLifetime() && !isLooping()) {
            this.remove(false);
        }
        this.age++;
        if (getLifetime() > 0) {
            t = (this.age % getLifetime()) * 1f / getLifetime();
        }
    }

    protected void updateOrigin() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;
    }

    protected int getLightColor(float partialTick) {
        BlockPos blockPos = new BlockPos((int) this.x, (int) this.y, (int) this.z);
        if (level != null && (level.hasChunkAt(blockPos) )) {
            return LevelRenderer.getLightColor(level, blockPos);
        }
        return 0;
    }

    public float getT(float partialTicks) {
        if (this.lifetime > 0 ){
            return t + partialTicks / this.lifetime;
        }
        return 0;
    }

    public float getMemRandom(Object object) {
        return getMemRandom(object, RandomSource::nextFloat);
    }

    public float getMemRandom(Object object, Function<RandomSource, Float> randomFunc) {
        var value = memRandom.get(object);
        if (value == null) return memRandom.computeIfAbsent(object, o -> randomFunc.apply(random));
        return value;
    }

    public void reset() {
        this.age = 0;
        this.memRandom.clear();
        this.removed = false;
        this.onGround = false;
        this.previousPosition = null;
        this.velocity.zero();
        this.t = 0;
    }

    @Nonnull
    public final RLParticleRenderType getRenderType() {
        return RLParticleQueueRenderType.INSTANCE;
    }

    @Override
    public boolean isAlive() {
        if (!removed || getParticleAmount() != 0) return true;
        return super.isAlive();
    }

    @Override
    public int getLightColor(BlockPos pos) {
        return lightCache.computeIfAbsent(pos, p -> {
            if (level != null && (level.hasChunkAt(p))) {
                return LevelRenderer.getLightColor(level, p);
            }
            return 0;
        });
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isLooping() {
        return false;
    }

    public void setRGBAColor(Vector4f color) {
        this.rCol = color.x;
        this.gCol = color.y;
        this.bCol = color.z;
        this.alpha = color.w;
    }

    public Vector4f getRGBAColor() {
        return new Vector4f(rCol, gCol, bCol, alpha);
    }
}
