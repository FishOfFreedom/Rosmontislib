package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.IParticle;
import com.freefish.rosmontislib.client.particle.advance.base.particle.TileParticle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction3;
import com.freefish.rosmontislib.client.utils.Vector3fHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote VelocityOverLifetimeSetting
 */
@OnlyIn(Dist.CLIENT)
public class VelocityOverLifetimeSetting  extends ToggleGroup{

    public enum OrbitalMode {
        AngularVelocity,
        LinearVelocity,
        FixedVelocity
    }
    protected NumberFunction3 linear = new NumberFunction3(0, 0, 0);

    protected OrbitalMode orbitalMode = OrbitalMode.AngularVelocity;

    protected NumberFunction3 orbital = new NumberFunction3(0, 0, 0);

    protected NumberFunction3 offset = new NumberFunction3(0, 0, 0);

    protected NumberFunction speedModifier = NumberFunction.constant(1);

    public NumberFunction getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(NumberFunction speedModifier) {
        this.speedModifier = speedModifier;
    }

    public NumberFunction3 getOffset() {
        return offset;
    }

    public void setOffset(NumberFunction3 offset) {
        this.offset = offset;
    }

    public NumberFunction3 getOrbital() {
        return orbital;
    }

    public void setOrbital(NumberFunction3 orbital) {
        this.orbital = orbital;
    }

    public OrbitalMode getOrbitalMode() {
        return orbitalMode;
    }

    public void setOrbitalMode(OrbitalMode orbitalMode) {
        this.orbitalMode = orbitalMode;
    }

    public NumberFunction3 getLinear() {
        return linear;
    }

    public void setLinear(NumberFunction3 linear) {
        this.linear = linear;
    }

    public Vector3f getVelocityAddition(TileParticle particle) {
        var lifetime = particle.getT();
        var addition = linear.get(lifetime, () -> particle.getMemRandom("vol0")).mul(0.05f);
        var orbitalVec = orbital.get(lifetime, () -> particle.getMemRandom("vol1"));
        var center = offset.get(lifetime, () -> particle.getMemRandom("vol2"));
        if (!Vector3fHelper.isZero(orbitalVec)) {
            if (orbitalMode == OrbitalMode.AngularVelocity) {
                var toPoint = new Vector3f(particle.getLocalPos()).sub(center);
                if (orbitalVec.x != 0) {
                    var radiusVec = new Vector3f(toPoint).sub(Vector3fHelper.project(new Vector3f(toPoint), new Vector3f(1, 0, 0)));
                    addition.add(new Vector3f(radiusVec).rotateX(orbitalVec.x * 0.05f).sub(radiusVec));
                }
                if (orbitalVec.y != 0) {
                    var radiusVec = new Vector3f(toPoint).sub(Vector3fHelper.project(new Vector3f(toPoint), new Vector3f(0, 1, 0)));
                    addition.add(new Vector3f(radiusVec).rotateY(orbitalVec.y * 0.05f).sub(radiusVec));
                }
                if (orbitalVec.z != 0) {
                    var radiusVec = new Vector3f(toPoint).sub(Vector3fHelper.project(new Vector3f(toPoint), new Vector3f(0, 0, 1)));
                    addition.add(new Vector3f(radiusVec).rotateZ(orbitalVec.z * 0.05f).sub(radiusVec));
                }
            } else if (orbitalMode == OrbitalMode.LinearVelocity) {
                var toPoint = particle.getLocalPos().sub(center);
                if (orbitalVec.x != 0) {
                    var radiusVec = new Vector3f(toPoint).sub(Vector3fHelper.project(new Vector3f(toPoint), new Vector3f(1, 0, 0)));
                    var r = radiusVec.length();
                    addition.add(new Vector3f(radiusVec).rotateX(orbitalVec.x * 0.05f / r).sub(radiusVec));
                }
                if (orbitalVec.y != 0) {
                    var radiusVec = new Vector3f(toPoint).sub(Vector3fHelper.project(new Vector3f(toPoint), new Vector3f(0, 1, 0)));
                    var r = radiusVec.length();
                    addition.add(new Vector3f(radiusVec).rotateY(orbitalVec.y * 0.05f / r).sub(radiusVec));
                }
                if (orbitalVec.z != 0) {
                    var radiusVec = new Vector3f(toPoint).sub(Vector3fHelper.project(new Vector3f(toPoint), new Vector3f(0, 0, 1)));
                    var r = radiusVec.length();
                    addition.add(new Vector3f(radiusVec).rotateZ(orbitalVec.z * 0.05f / r).sub(radiusVec));
                }
            } else if (orbitalMode == OrbitalMode.FixedVelocity) {
                var toCenter = center.sub(particle.getLocalPos());
                if (orbitalVec.x != 0) {
                    addition.add(new Vector3f(toCenter).cross(new Vector3f(1, 0, 0)).normalize().mul(orbitalVec.x * 0.05f));
                }
                if (orbitalVec.y != 0) {
                    addition.add(new Vector3f(toCenter).cross(new Vector3f(0, 1, 0)).normalize().mul(orbitalVec.y * 0.05f));
                }
                if (orbitalVec.z != 0) {
                    addition.add(new Vector3f(toCenter).cross(new Vector3f(0, 0, 1)).normalize().mul(orbitalVec.z * 0.05f));
                }
            }
        }
        return addition;
    }

    public float getVelocityMultiplier(IParticle particle) {
        var lifetime = particle.getT();
        return speedModifier.get(lifetime, () -> particle.getMemRandom(this)).floatValue();
    }

}
