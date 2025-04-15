package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.AdvancedRLParticleBase;
import com.freefish.rosmontislib.client.particle.advance.base.particle.TileParticle;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.function.TriFunction;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class RendererSetting {
    protected Layer layer = Layer.Translucent;
    protected boolean bloomEffect = false;
    protected final Cull cull = new Cull();

    public Cull getCull() {
        return cull;
    }

    public boolean isBloomEffect() {
        return bloomEffect;
    }

    public void setBloomEffect(boolean bloomEffect) {
        this.bloomEffect = bloomEffect;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public enum Layer {
        Opaque,
        Translucent
    }


    public static class Cull extends ToggleGroup {
        protected Vector3f from = new Vector3f(-0.5f, -0.5f, -0.5f);

        public Vector3f getTo() {
            return to;
        }

        public void setTo(Vector3f to) {
            this.to = to;
        }

        public Vector3f getFrom() {
            return from;
        }

        public void setFrom(Vector3f from) {
            this.from = from;
        }

        protected Vector3f to = new Vector3f(0.5f, 0.5f, 0.5f);

        public AABB getCullAABB(AdvancedRLParticleBase particle, float partialTicks) {
            var pos = particle.transform().position();
            return new AABB(from.x, from.y, from.z, to.x, to.y, to.z).move(pos.x, pos.y, pos.z);
        }
    }

    public static class Particle extends RendererSetting {
        public enum Mode {
            Billboard((p, c, t) -> c.rotation()),
            Horizontal(0, 90),
            Vertical(0, 0),
            VerticalBillboard((p, c, t) -> {
                var quaternion = new Quaternionf();
                quaternion.rotateY((float) Math.toRadians(-c.getYRot()));
                return quaternion;
            }),
            Model((p, c, t) -> new Quaternionf());

            public final TriFunction<TileParticle, Camera, Float, Quaternionf> quaternion;

            Mode(TriFunction<TileParticle, Camera, Float, Quaternionf> quaternion) {
                this.quaternion = quaternion;
            }

            Mode(Quaternionf quaternion) {
                this.quaternion = (p, c, t) -> quaternion;
            }

            Mode(float yRot, float xRot) {
                var quaternion = new Quaternionf();
                quaternion.rotateY((float) Math.toRadians(-yRot));
                quaternion.rotateX((float) Math.toRadians(xRot));
                this.quaternion = (p, c, t) -> quaternion;
            }
        }

        public Mode getRenderMode() {
            return renderMode;
        }

        public void setRenderMode(Mode renderMode) {
            this.renderMode = renderMode;
        }

        public boolean isShade() {
            return shade;
        }

        public void setShade(boolean shade) {
            this.shade = shade;
        }

        public boolean isUseBlockUV() {
            return useBlockUV;
        }

        public void setUseBlockUV(boolean useBlockUV) {
            this.useBlockUV = useBlockUV;
        }

        protected Mode renderMode = Mode.Billboard;
        protected boolean shade = true;

        protected boolean useBlockUV = true;
    }
}