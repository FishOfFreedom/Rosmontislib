package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.particle.RLParticle;
import com.freefish.rosmontislib.client.particle.advance.base.particle.TileParticle;
import com.freefish.rosmontislib.client.particle.advance.base.trail.RLTrailConfig;
import com.freefish.rosmontislib.client.particle.advance.base.trail.TrailParticle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.color.Gradient;
import com.freefish.rosmontislib.client.utils.ColorUtils;
import com.freefish.rosmontislib.client.utils.Vector3fHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

@OnlyIn(Dist.CLIENT)
public class TrailsSetting extends ToggleGroup{

    protected float ratio = 1f;

    protected NumberFunction lifetime = NumberFunction.constant(1);

    protected boolean dieWithParticles = false;

    protected boolean sizeAffectsWidth = true;

    protected boolean sizeAffectsLifetime = false;

    protected boolean inheritParticleColor = true;

    protected NumberFunction colorOverLifetime = new Gradient();

    public final RLTrailConfig config = new RLTrailConfig();

    public TrailsSetting() {
        config.setWidthOverTrail(NumberFunction.constant(0.5f));
        config.setParallelRendering(true);
    }

    public void setup(RLParticle emitter, TileParticle particle) {
        var random = emitter.getRandomSource();
        if (random.nextFloat() < ratio) { // has tail
            var trail = new TrailParticle(emitter, config, emitter.getThreadSafeRandomSource());
            trail.setDelay(particle.getDelay() + trail.getDelay());
            trail.setHeadPositionSupplier(particle::getWorldPos);
            trail.setDieWhenAllTailsRemoved(!dieWithParticles);
            trail.setOnUpdate(() -> {
                if (particle.isRemoved()) {
                    trail.setRemoved(true);
                }
            });
            trail.setLifetimeSupplier(() -> {
                var time = lifetime.get(particle.getT(), () -> particle.getMemRandom("trails-lifetime")).floatValue() * particle.getLifetime();
                if (sizeAffectsLifetime) {
                    time *= Vector3fHelper.max(particle.getRealSize(0));
                }
                return time;
            });
            trail.setWidthMultiplier(() -> {
                if (sizeAffectsWidth) {
                    return Vector3fHelper.max(particle.getRealSize(0));
                }
                return 1f;
            });
            trail.setColorMultiplier(t -> {
                var color = new Vector4f(1);
                if (inheritParticleColor) {
                    color.mul(particle.getRealColor(t));
                }
                if (colorOverLifetime != null) {
                    var c = colorOverLifetime.get(particle.getT(t), () -> particle.getMemRandom("trails-color")).intValue();
                    color.mul(ColorUtils.red(c), ColorUtils.green(c), ColorUtils.blue(c), ColorUtils.alpha(c));
                }
                return color;
            });

            emitter.emitParticle(trail);
        }
    }
}
