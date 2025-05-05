package com.freefish.rosmontislib.utils;

import com.freefish.rosmontislib.RosmontisLib;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;

public class ReflectionUtils {
    public static Class<?> getRawType(Type type, Class<?> fallback) {
        var rawType = getRawType(type);
        return rawType != null ? rawType : fallback;
    }
    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof GenericArrayType) {
            return getRawType(((GenericArrayType) type).getGenericComponentType());
        } else if (type instanceof ParameterizedType) {
            return getRawType(((ParameterizedType) type).getRawType());
        } else {
            return null;
        }
    }

    public static <A extends Annotation> void findAnnotationClasses(Class<A> annotationClass, Consumer<Class<?>> consumer, Runnable onFinished) {
        org.objectweb.asm.Type annotationType = org.objectweb.asm.Type.getType(annotationClass);
        for (ModFileScanData data : ModList.get().getAllScanData()) {
            for (ModFileScanData.AnnotationData annotation : data.getAnnotations()) {
                if (annotationType.equals(annotation.annotationType())) {
                    if (annotation.annotationData().containsKey("modID") && annotation.annotationData().get("modID") instanceof String modID) {
                        if (!modID.isEmpty() && !ModList.get().isLoaded(modID)) {
                            continue;
                        }
                    }
                    try {
                        consumer.accept(Class.forName(annotation.memberName(), false, ReflectionUtils.class.getClassLoader()));
                    } catch (Throwable throwable) {
                        RosmontisLib.LOGGER.warn("Failed to load class for notation: " + annotation.memberName());
                    }
                }
            }
        }
        onFinished.run();
    }
}