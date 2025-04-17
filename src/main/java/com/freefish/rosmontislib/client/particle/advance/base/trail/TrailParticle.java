package com.freefish.rosmontislib.client.particle.advance.base.trail;

import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.freefish.rosmontislib.client.particle.advance.base.IParticle;
import com.freefish.rosmontislib.client.particle.advance.base.IParticleEmitter;
import com.freefish.rosmontislib.client.utils.ColorUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class TrailParticle implements IParticle {
    private static Integer atomicCounterBuffer;
    public enum UVMode {
        Stretch,
        Tile
    }
    /**
     * Basic data
     */
    protected float r = 1, g = 1, b = 1, a = 1; // color
    protected float ro = 1, go = 1, bo = 1, ao = 1;
    protected int light = -1;

    public boolean isDieWhenAllTailsRemoved() {
        return dieWhenAllTailsRemoved;
    }

    public void setDieWhenAllTailsRemoved(boolean dieWhenAllTailsRemoved) {
        this.dieWhenAllTailsRemoved = dieWhenAllTailsRemoved;
    }

    protected boolean dieWhenAllTailsRemoved = true;

    /**
     * Life cycle
     */
    protected int delay;

    protected boolean isRemoved;

    protected Runnable onUpdate;

    protected Float2ObjectFunction<Vector3f> headPositionSupplier;

    protected Supplier<Float> lifetimeSupplier;

    protected Supplier<Float> widthMultiplier;

    public Float2ObjectFunction<Vector4f> getColorMultiplier() {
        return colorMultiplier;
    }

    public void setColorMultiplier(Float2ObjectFunction<Vector4f> colorMultiplier) {
        this.colorMultiplier = colorMultiplier;
    }

    public Supplier<Float> getWidthMultiplier() {
        return widthMultiplier;
    }

    public void setWidthMultiplier(Supplier<Float> widthMultiplier) {
        this.widthMultiplier = widthMultiplier;
    }

    public Supplier<Float> getLifetimeSupplier() {
        return lifetimeSupplier;
    }

    public void setLifetimeSupplier(Supplier<Float> lifetimeSupplier) {
        this.lifetimeSupplier = lifetimeSupplier;
    }

    public Float2ObjectFunction<Vector3f> getHeadPositionSupplier() {
        return headPositionSupplier;
    }

    public void setHeadPositionSupplier(Float2ObjectFunction<Vector3f> headPositionSupplier) {
        this.headPositionSupplier = headPositionSupplier;
    }

    public Runnable getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    @Override
    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    protected Float2ObjectFunction<Vector4f> colorMultiplier;
    //runtime

    public TailArray getRawTails() {
        return rawTails;
    }

    public RLTrailConfig getConfig() {
        return config;
    }

    public IParticleEmitter getEmitter() {
        return emitter;
    }

    public ConcurrentHashMap<Object, Float> getMemRandom() {
        return memRandom;
    }

    protected TailArray rawTails = new TailArray();

    public TailArray getTails() {
        return tails;
    }

    protected TailArray tails = new TailArray();
    protected RLTrailConfig config;

    protected IParticleEmitter emitter;

    protected ConcurrentHashMap<Object, Float> memRandom = new ConcurrentHashMap<>();

    public RandomSource randomSource;

    public TrailParticle(IParticleEmitter emitter, RLTrailConfig config, RandomSource randomSource) {
        this.emitter = emitter;
        this.config = config;
        this.randomSource = randomSource;
        this.headPositionSupplier = (t) -> emitter.transform().position();
        this.setup();
    }

    public void setup() {
        this.setDelay(config.getStartDelay());
        this.lifetimeSupplier = () -> (float) config.getTime();
        update();
        updateOrigin();
        tails.clear();
        rawTails.clear();
    }

    @Override
    public RLParticleRenderType getRenderType() {
        return config.particleRenderType;
    }

    @Override
    public RandomSource getRandomSource() {
        return randomSource;
    }

    @Override
    public boolean isAlive() {
        if (isRemoved) {
            return dieWhenAllTailsRemoved && !tails.isEmpty();
        }
        return true;
    }

    @Override
    public float getT() {
        return emitter.getT();
    }

    @Override
    public float getT(float partialTicks) {
        return emitter.getT(partialTicks);
    }

    @Override
    public float getMemRandom(Object object) {
        return getMemRandom(object, RandomSource::nextFloat);
    }

    @Override
    public float getMemRandom(Object object, Function<RandomSource, Float> randomFunc) {
        var value = memRandom.get(object);
        if (value == null) return memRandom.computeIfAbsent(object, o -> randomFunc.apply(randomSource));
        return value;
    }

    public Vector4f getRealColor(float partialTicks) {
        var emitterColor = emitter.getRGBAColor();
        var a = Mth.lerp(partialTicks, this.ao, this.a);
        var r = Mth.lerp(partialTicks, this.ro, this.r);
        var g = Mth.lerp(partialTicks, this.go, this.g);
        var b = Mth.lerp(partialTicks, this.bo, this.b);
        if (colorMultiplier != null) {
            var color = colorMultiplier.get(partialTicks);
            r *= color.x();
            g *= color.y();
            b *= color.z();
            a *= color.w();
        }
        return emitterColor.mul(r, g, b, a);
    }

    public int getRealLight(float partialTicks) {
        if (config.renderer.isBloomEffect()) {
            return LightTexture.FULL_BRIGHT;
        }
        if (config.lights.isEnable()) {
            return config.lights.getLight(this, partialTicks);
        }
        return light;
    }

    public int getLightColor() {
        var pos = getHeadPosition();
        var blockPos = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);
        return emitter.getLightColor(blockPos);
    }

    public Vector3f getHeadPosition() {
        return getHeadPosition(0);
    }

    public Vector3f getHeadPosition(float partialTicks) {
        return headPositionSupplier.get(partialTicks);
    }

    @Override
    public void tick() {
        if (delay > 0) {
            delay--;
            return;
        }

        updateOrigin();

        update();
    }

    protected void updateOrigin() {
        this.ro = this.r;
        this.go = this.g;
        this.bo = this.b;
        this.ao = this.a;
    }

    protected void update() {
        updateChanges();
        if (onUpdate != null) {
            onUpdate.run();
        }
    }

    protected void updateChanges() {
        var changed = updateTails();
        if (changed) {
            updateRawTailsProperties();
            if (config.isSmoothInterpolation()) {
                this.tails = generateSmoothPath(rawTails, config.getMinVertexDistance());
            } else {
                this.tails = rawTails;
            }
        }
        this.updateLight();
    }

    protected boolean updateTails() {
        var tailChanged = false;

        for (int i = 0; i < rawTails.size(); i++) {
            rawTails.lifeTime[i] -= 1;
        }

        TailArray newRawTails = new TailArray();
        for (int i = 0; i < rawTails.size(); i++) {
            if (rawTails.lifeTime[i] > 0) {
                rawTails.copyTailTo(newRawTails, i);
            } else {
                tailChanged = true;
            }
        }
        rawTails = newRawTails;

        if (!isRemoved()) {
            var headPos = getHeadPosition();
            var shouldAdd = true;
            if (!rawTails.isEmpty()) {
                Vector3f last = rawTails.getPosition(rawTails.size() - 1);
                if (headPos.distanceSquared(last) < config.getMinVertexDistance() * config.getMinVertexDistance()) {
                    shouldAdd = false;
                }
            }
            if (shouldAdd) {
                Tail newTail = new Tail(headPos, lifetimeSupplier.get());
                rawTails.add(newTail);
                tailChanged = true;
            }
        }
        return tailChanged;
    }

    public TailArray generateSmoothPath(TailArray vertices, float distance) {
        if (vertices.size() <= 2) {
            return vertices;
        }

        TailArray smoothPath = new TailArray();
        float minDistanceSq = Math.max(distance, 0.05f) * Math.max(distance, 0.05f);
        Vector3f prevDir = null;

        for (int i = 0; i < vertices.size() - 1; i++) {
            Vector3f curr = vertices.getPosition(i);
            Vector3f next = vertices.getPosition(i + 1);
            Vector3f dir = new Vector3f(next).sub(curr).normalize();
            smoothPath.add(vertices.copyAsTail(i));

            if (prevDir != null && dir.dot(prevDir) > 0.99f) {
                // skip interpolation if angle is small
                prevDir = dir;
                continue;
            }

            float distSq = curr.distanceSquared(next);
            if (distSq > minDistanceSq) {
                int steps = (int) (distSq / minDistanceSq);
                for (int j = 1; j < steps; j++) {
                    float t = j / (float) steps;
                    Vector3f interpPos = catmullRomInterpolate(
                            vertices.getPosition(Math.max(i - 1, 0)),
                            curr, next,
                            vertices.getPosition(Math.min(i + 2, vertices.size() - 1)), t);
                    // build interpolated tail
                    var interp = new Tail(interpPos, vertices.lifeTime[i]);
                    interp.color = vertices.getColor(i);
                    interp.width = vertices.getWidth(i);
                    smoothPath.add(interp);
                }
            }
            prevDir = dir;
        }
        smoothPath.add(vertices.copyAsTail(vertices.size() - 1));
        return smoothPath;
    }

    public static Vector3f catmullRomInterpolate(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, float t) {
        float t2 = t * t;
        float t3 = t2 * t;
        Vector3f result = new Vector3f();
        result.x = 0.5f * (2 * p1.x + (-p0.x + p2.x) * t + (2 * p0.x - 5 * p1.x + 4 * p2.x - p3.x) * t2 + (-p0.x + 3 * p1.x - 3 * p2.x + p3.x) * t3);
        result.y = 0.5f * (2 * p1.y + (-p0.y + p2.y) * t + (2 * p0.y - 5 * p1.y + 4 * p2.y - p3.y) * t2 + (-p0.y + 3 * p1.y - 3 * p2.y + p3.y) * t3);
        result.z = 0.5f * (2 * p1.z + (-p0.z + p2.z) * t + (2 * p0.z - 5 * p1.z + 4 * p2.z - p3.z) * t2 + (-p0.z + 3 * p1.z - 3 * p2.z + p3.z) * t3);
        return result;
    }

    protected void updateRawTailsProperties() {
        if (rawTails.size() > 1) {
            for (int i = 0; i < rawTails.size(); i++) {
                updateRawTrailProperties(i, rawTails.size());
            }
        } else if (rawTails.size() == 1) {
            updateRawTrailProperties(0, 2);
        }
    }

    protected void updateRawTrailProperties(int tailIndex, int tailsSize) {
        float t = ((float) tailIndex) / (tailsSize - 1);

        int colorInt = config.getColorOverTrail().get(t, () -> getMemRandom("trails-colorOverTrail")).intValue();
        rawTails.colorR[tailIndex] = ColorUtils.red(colorInt);
        rawTails.colorG[tailIndex] = ColorUtils.green(colorInt);
        rawTails.colorB[tailIndex] = ColorUtils.blue(colorInt);
        rawTails.colorA[tailIndex] = ColorUtils.alpha(colorInt);

        float widthValue = config.getWidthOverTrail().get(t, () -> getMemRandom("trails-widthOverTrail")).floatValue();
        if (widthMultiplier != null) {
            widthValue *= widthMultiplier.get();
        }
        rawTails.width[tailIndex] = widthValue;
    }

    protected void updateLight() {
        if (config.lights.isEnable() || config.renderer.isBloomEffect()) return;
        light = getLightColor();
    }

    public void render(@Nonnull VertexConsumer pBuffer, Camera pRenderInfo, float partialTicks) {
        if (delay <= 0 && this.emitter.isVisible()) {
            tails.renderInternal(pBuffer, partialTicks, pRenderInfo.getPosition().toVector3f(), getRealColor(partialTicks), getRealLight(partialTicks));
        }
    }

    public Vector4f getUVs(int tailIndex, int size, float partialTicks) {
        float u0, u1, v0, v1;
        var uvMode = config.getUvMode();
        if (uvMode == UVMode.Stretch) {
            u0 = tailIndex / (size - 1f);
            u1 = (tailIndex + 1f) / (size - 1f);
            v0 = 0;
            v1 = 1;

            if (config.uvAnimation.isEnable()) {
                var uvs = config.uvAnimation.getUVs(this, partialTicks);
                var x = uvs.x;
                var y = uvs.y;
                var w = uvs.z - uvs.x;
                var h = uvs.w - uvs.y;
                u0 = x + w * u0;
                v0 = y + h * v0;
                u1 = x + w * u1;
                v1 = y + h * v1;
            }
        } else {
            if (config.uvAnimation.isEnable()) {
                var uvs = config.uvAnimation.getUVs(this, partialTicks);
                u0 = uvs.x();
                v0 = uvs.y();
                u1 = uvs.z();
                v1 = uvs.w();
            } else {
                u0 = 0;
                v0 = 0;
                u1 = 1;
                v1 = 1;
            }
        }
        return new Vector4f(u0, v0, u1, v1);
    }

    public class TailArray {
        private int size = 0;
        private int capacity = 16;

        // SoA布局
        private float[] posX = new float[capacity];
        private float[] posY = new float[capacity];
        private float[] posZ = new float[capacity];

        private float[] colorR = new float[capacity];
        private float[] colorG = new float[capacity];
        private float[] colorB = new float[capacity];
        private float[] colorA = new float[capacity];

        private float[] width = new float[capacity];
        private float[] lifeTime = new float[capacity];

        public void clear() {
            size = 0;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void add(Tail tail) {
            ensureCapacity();
            posX[size] = tail.position.x;
            posY[size] = tail.position.y;
            posZ[size] = tail.position.z;

            colorR[size] = tail.color.x;
            colorG[size] = tail.color.y;
            colorB[size] = tail.color.z;
            colorA[size] = tail.color.w;

            width[size] = tail.width;
            lifeTime[size] = tail.lifeTime;
            size++;
        }

        private void removeLast() {
            if (size > 0) {
                size--;
            }
        }

        public Vector3f getPosition(int index) {
            return new Vector3f(posX[index], posY[index], posZ[index]);
        }

        public Vector4f getColor(int index) {
            return new Vector4f(colorR[index], colorG[index], colorB[index], colorA[index]);
        }

        public float getWidth(int index) {
            return width[index];
        }

        public float getLifeTime(int index) {
            return lifeTime[index];
        }

        private void ensureCapacity() {
            if (size >= capacity) {
                int newCapacity = capacity * 2;
                posX = Arrays.copyOf(posX, newCapacity);
                posY = Arrays.copyOf(posY, newCapacity);
                posZ = Arrays.copyOf(posZ, newCapacity);

                colorR = Arrays.copyOf(colorR, newCapacity);
                colorG = Arrays.copyOf(colorG, newCapacity);
                colorB = Arrays.copyOf(colorB, newCapacity);
                colorA = Arrays.copyOf(colorA, newCapacity);

                width = Arrays.copyOf(width, newCapacity);
                lifeTime = Arrays.copyOf(lifeTime, newCapacity);

                capacity = newCapacity;
            }
        }

        private void copyTailTo(TailArray dest, int index) {
            dest.ensureCapacity();
            dest.posX[dest.size()] = posX[index];
            dest.posY[dest.size()] = posY[index];
            dest.posZ[dest.size()] = posZ[index];

            dest.colorR[dest.size()] = colorR[index];
            dest.colorG[dest.size()] = colorG[index];
            dest.colorB[dest.size()] = colorB[index];
            dest.colorA[dest.size()] = colorA[index];

            dest.width[dest.size()] = width[index];
            dest.lifeTime[dest.size()] = lifeTime[index];
            dest.size++;
        }

        private Tail copyAsTail(int index) {
            Vector3f pos = getPosition(index);
            Tail tail = new Tail(pos, lifeTime[index]);
            tail.color = getColor(index);
            tail.width = width[index];
            return tail;
        }

        public void renderInternal(VertexConsumer buffer, float partialTicks, Vector3f cameraPos, Vector4f color, int light) {
            Vector3f lastNormal = null;
            Vector3f lastUp = null;

            Vector3f headPos = getHeadPosition(partialTicks);
            boolean pushHead = true;
            int tailSize = tails.size();
            if (tailSize > 0) {
                Vector3f lastPos = tails.getPosition(tailSize - 1);
                if (lastPos.equals(headPos)) {
                    pushHead = false;
                }
            }

            if (pushHead) {
                Tail headTail = new Tail(headPos, 100);
                headTail.previous = tailSize > 1 ? copyAsTail(tailSize - 1) : null;
                headTail.updateData();
                tails.add(headTail);
            }

            for (int i = 0; i < size - 1; i++) {
                // skip dead tails
                if (lifeTime[i] - partialTicks <= 0 || lifeTime[i + 1] - partialTicks <= 0) {
                    continue;
                }

                // 基础参数
                Vector3f tailPos = new Vector3f(posX[i], posY[i], posZ[i]);
                Vector3f curr = new Vector3f(posX[i], posY[i], posZ[i]);
                Vector3f next = new Vector3f(posX[i + 1], posY[i + 1], posZ[i + 1]);
                Vector3f vec = new Vector3f(next).sub(curr);
                Vector3f toTail = new Vector3f(curr).sub(cameraPos);
                Vector3f normal = vec.cross(toTail).normalize();

                if (lastNormal == null) lastNormal = normal;

                Vector3f avgNormal = new Vector3f(lastNormal).add(normal).div(2);
                Vector3f up = new Vector3f(tailPos).add(new Vector3f(avgNormal).mul(width[i])).sub(cameraPos);
                Vector3f down = new Vector3f(tailPos).add(new Vector3f(avgNormal).mul(-width[i])).sub(cameraPos);

                float ta = color.w() * colorA[i];
                float tr = color.x() * colorR[i];
                float tg = color.y() * colorG[i];
                float tb = color.z() * colorB[i];

                Vector4f uvs = getUVs(i, size, partialTicks);
                float u0 = uvs.x(), u1 = uvs.z(), v0 = uvs.y(), v1 = uvs.w();

                // 1. push first strip segment
                if (lastUp == null) {
                    pushVertex(buffer, light, up, tr, tg, tb, ta, u0, v0);
                    pushVertex(buffer, light, up, tr, tg, tb, ta, u0, v0);
                }

                // 2. push next segment
                pushVertex(buffer, light, up, tr, tg, tb, ta, u0, v0);
                pushVertex(buffer, light, down, tr, tg, tb, ta, u0, v1);

                // 保留 last
                lastUp = up;
                lastNormal = normal;
            }

            // handle head segment
            int headIndex = size - 1;
            // 修改 head segment 处理
            if (headIndex > 0 && lastNormal != null) {
                Vector3f head = getPosition(headIndex);

                // 注意这里，直接用 lastNormal

                Vector3f up = new Vector3f(head).add(new Vector3f(lastNormal).mul(width[headIndex])).sub(cameraPos);
                Vector3f down = new Vector3f(head).add(new Vector3f(lastNormal).mul(-width[headIndex])).sub(cameraPos);

                float ta = color.w() * colorA[headIndex];
                float tr = color.x() * colorR[headIndex];
                float tg = color.y() * colorG[headIndex];
                float tb = color.z() * colorB[headIndex];

                Vector4f uvs = getUVs(headIndex - 1, size, partialTicks);
                float u0 = uvs.x(), u1 = uvs.z(), v0 = uvs.y(), v1 = uvs.w();

                // 继续用之前的 u1、v0、v1
                pushVertex(buffer, light, up, tr, tg, tb, ta, u1, v0);
                pushVertex(buffer, light, down, tr, tg, tb, ta, u1, v1);
                lastUp = up;
            }


            // 3. **Degenerate triangle 插入**
            if (lastUp != null) {
                pushVertex(buffer, light, lastUp, 0, 0, 0, 0, 0, 0); // 重复点1
                pushVertex(buffer, light, lastUp, 0, 0, 0, 0, 0, 0); // 重复点2
            }

            if (pushHead) {
                tails.removeLast(); // 新增函数，见下
            }
        }

        private void pushVertex(VertexConsumer buffer, int light, Vector3f pos, float r, float g, float b, float a, float u, float v) {
            buffer.vertex(pos.x, pos.y, pos.z).uv(u, v).color(r, g, b, a).uv2(light).endVertex();
        }

    }

    public static class Tail {
        public float lifeTime;
        public Vector3f position;
        public Vector4f color;
        public float width;
        public Tail previous;
        public Tail next;
        public float t;

        public Tail(Vector3f position, float lifeTime) {
            this.position = position;
            this.lifeTime = lifeTime;
            this.color = new Vector4f(1, 1, 1, 1);
            this.width = 0.2f;
        }

        public Tail(Vector3f position, Tail previous, Tail next, float t) {
            this.position = position;
            this.previous = previous;
            this.next = next;
            this.t = t;
            updateData();
        }

        public void updateData() {
            if (previous == null) {
                return;
            }
            if (next == null ) {
                this.color = new Vector4f(previous.color);
                this.width = previous.width;
                this.lifeTime = previous.lifeTime;
            } else {
                this.color = new Vector4f(previous.color).lerp(next.color, t);
                this.width = Mth.lerp(t, previous.width, next.width);
                this.lifeTime = Mth.lerp(t, previous.lifeTime, next.lifeTime);
            }
        }

        public float distanceSquared(Tail next) {
            return position.distanceSquared(next.position);
        }
    }
}