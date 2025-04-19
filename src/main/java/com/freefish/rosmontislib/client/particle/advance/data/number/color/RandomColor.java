package com.freefish.rosmontislib.client.particle.advance.data.number.color;

import com.freefish.rosmontislib.client.particle.advance.data.number.RandomConstant;
import com.freefish.rosmontislib.client.utils.ColorUtils;
import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

import static com.freefish.rosmontislib.client.utils.ColorUtils.*;

/**
 * @author KilaBash
 * @date 2023/5/27
 * @implNote RandomColor
 */
public class RandomColor extends RandomConstant {
    public RandomColor() {
        this(0xff000000, 0xffffffff);
    }

    public RandomColor(Number a, Number b) {
        super(a, b, false);
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        int colorA = getA().intValue();
        int colorB = getB().intValue();
        return ColorUtils.blendColor(colorA, colorB, lerp.get());
    }

    private int randomColor(RandomSource randomSource, int minA, int maxA, int minR, int maxR, int minG, int maxG, int minB, int maxB) {
        return  ((minR + randomSource.nextInt(maxA + 1 - minA)) << 24) |
                ((minR + randomSource.nextInt(maxR + 1 - minR)) << 16) |
                ((minG + randomSource.nextInt(maxG + 1 - minG)) << 8) |
                ((minB + randomSource.nextInt(maxB + 1 - minB))) ;
    }

    private int randomColor(RandomSource randomSource, int colorA, int colorB) {
        return randomColor(randomSource, Math.min(alphaI(colorA), alphaI(colorB)), Math.max(alphaI(colorA), alphaI(colorB)),
                Math.min(redI(colorA), redI(colorB)), Math.max(redI(colorA), redI(colorB)),
                Math.min(greenI(colorA), greenI(colorB)), Math.max(greenI(colorA), greenI(colorB)),
                Math.min(blueI(colorA), blueI(colorB)), Math.max(blueI(colorA), blueI(colorB)));
    }
}
