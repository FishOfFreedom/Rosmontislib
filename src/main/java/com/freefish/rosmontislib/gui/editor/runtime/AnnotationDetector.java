/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.runtime;


import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.gui.editor.accessors.IConfiguratorAccessor;
import com.freefish.rosmontislib.gui.editor.annotation.ConfigAccessor;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegisterClient;
import com.freefish.rosmontislib.gui.editor.configurator.IConfigurableWidget;
import com.freefish.rosmontislib.gui.editor.data.IProject;
import com.freefish.rosmontislib.gui.editor.data.resource.Resource;
import com.freefish.rosmontislib.gui.editor.ui.menu.MenuTab;
import com.freefish.rosmontislib.gui.editor.ui.view.FloatViewWidget;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.utils.ReflectionUtils;
import org.openjdk.nashorn.internal.ir.BaseNode;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author KilaBash
 * @date 2022/12/3
 * @implNote AnnotationDetector
 */
@SuppressWarnings("unchecked")
public class AnnotationDetector {

    public record Wrapper<A extends Annotation, T>(A annotation, Class<? extends T> clazz, Supplier<T> creator) { }

    public static final List<IConfiguratorAccessor<?>> CONFIGURATOR_ACCESSORS = scanClasses(ConfigAccessor.class, IConfiguratorAccessor.class, AnnotationDetector::checkNoArgsConstructor, AnnotationDetector::createNoArgsInstance, (a, b) -> 0, l -> {});
    public static final List<Wrapper<LDLRegister, IGuiTexture>> REGISTER_TEXTURES = scanClasses(LDLRegister.class, IGuiTexture.class, AnnotationDetector::checkNoArgsConstructor, AnnotationDetector::toUINoArgsBuilder, AnnotationDetector::UIWrapperSorter, l -> {});
    public static final List<Wrapper<LDLRegister, Resource>> REGISTER_RESOURCES = scanClasses(LDLRegister.class, Resource.class, AnnotationDetector::checkNoArgsConstructor, AnnotationDetector::toUINoArgsBuilder, AnnotationDetector::UIWrapperSorter, l -> {});
    public static final List<Wrapper<LDLRegister, IConfigurableWidget>> REGISTER_WIDGETS = scanClasses(LDLRegister.class, IConfigurableWidget.class, AnnotationDetector::checkNoArgsConstructor, AnnotationDetector::toUINoArgsBuilder, AnnotationDetector::UIWrapperSorter, l -> {});
    public static final List<Wrapper<LDLRegister, FloatViewWidget>> REGISTER_FLOAT_VIEWS = scanClasses(LDLRegister.class, FloatViewWidget.class, AnnotationDetector::checkNoArgsConstructor, AnnotationDetector::toUINoArgsBuilder, AnnotationDetector::UIWrapperSorter, l -> {});
    public static final List<Wrapper<LDLRegister, MenuTab>> REGISTER_MENU_TABS = scanClasses(LDLRegister.class, MenuTab.class, AnnotationDetector::checkNoArgsConstructor, AnnotationDetector::toUINoArgsBuilder, AnnotationDetector::UIWrapperSorter, l -> {});
    public static final List<Wrapper<LDLRegister, IProject>> REGISTER_PROJECTS = scanClasses(LDLRegister.class, IProject.class, AnnotationDetector::checkNoArgsConstructor, AnnotationDetector::toUINoArgsBuilder, AnnotationDetector::UIWrapperSorter, l -> {});
    //public static final Map<String, Wrapper<LDLRegisterClient, ? extends ISerializableRenderer>> REGISTER_RENDERERS = new HashMap<>();
    //public static final List<Wrapper<LDLRegisterClient, IUITest>> REGISTER_UI_TESTS = new ArrayList<>();
    //public static final List<TypeAdapter.ITypeAdapter> REGISTER_TYPE_ADAPTERS = scanClasses(LDLRegister.class, TypeAdapter.ITypeAdapter.class, AnnotationDetector::checkNoArgsConstructor, AnnotationDetector::createNoArgsInstance, (a, b) -> 0, l -> {});
    public static final Map<String, Wrapper<LDLRegister, ? extends BaseNode>> REGISTER_GP_NODES = new HashMap<>();

    public static void init() {
        if (RosmontisLib.isClient()) {
            //AnnotationDetector.scanClasses(
            //        LDLRegisterClient.class,
            //        ISerializableRenderer.class,
            //        AnnotationDetector::checkNoArgsConstructor,
            //        AnnotationDetector::toClientUINoArgsBuilder,
            //        AnnotationDetector::clientUIWrapperSorter,
            //        l -> REGISTER_RENDERERS.putAll(l.stream().collect(Collectors.toMap(w -> w.annotation().name(), w -> w)))
            //);
            //if (RosmontisLib.isDevEnv()) {
            //    AnnotationDetector.scanClasses(
            //            LDLRegisterClient.class,
            //            IUITest.class,
            //            AnnotationDetector::checkNoArgsConstructor,
            //            AnnotationDetector::toClientUINoArgsBuilder,
            //            AnnotationDetector::clientUIWrapperSorter,
            //            l -> {
            //                for (Wrapper<LDLRegisterClient, ? extends IUITest> wrapper : l) {
            //                    REGISTER_UI_TESTS.add((Wrapper<LDLRegisterClient, IUITest>) wrapper);
            //                }
            //            });
            //}
        }
        AnnotationDetector.scanClasses(
                LDLRegister.class,
                BaseNode.class,
                AnnotationDetector::checkNoArgsConstructor,
                AnnotationDetector::toUINoArgsBuilder,
                AnnotationDetector::UIWrapperSorter,
                l -> REGISTER_GP_NODES.putAll(l.stream().collect(Collectors.toMap(w -> w.annotation().name(), w -> w))));
    }

    public static <A extends Annotation, T, C> List<C> scanClasses(Class<A> annotationClass, Class<T> baseClazz, BiPredicate<A, Class<? extends T>> predicate, Function<Class<? extends T>, C> mapping, Comparator<C> sorter, Consumer<List<C>> onFinished) {
        ArrayList<C> result = new ArrayList<>();
        ReflectionUtils.findAnnotationClasses(annotationClass, clazz -> {
            if (baseClazz.isAssignableFrom(clazz)) {
                try {
                    Class<? extends T> realClass =  (Class<? extends T>) clazz;
                    if (predicate.test(clazz.getAnnotation(annotationClass), realClass)) {
                        result.add(mapping.apply(realClass));
                    }
                } catch (Throwable e) {
                    RosmontisLib.LOGGER.error("failed to scan annotation {} + base class {} while handling class {} ", annotationClass, baseClazz, clazz, e);
                }
            }
        }, () -> {
            result.sort(sorter);
            onFinished.accept(result);
        });
        return result;
    }

    public static <A, T> boolean checkNoArgsConstructor(A annotation, Class<? extends T> clazz) {
        if (annotation instanceof LDLRegister LDLRegister) {
            if (!LDLRegister.modID().isEmpty() && !RosmontisLib.isModLoaded(LDLRegister.modID())) {
                return false;
            }
        } else if (annotation instanceof LDLRegisterClient LDLRegisterClient) {
            if (!LDLRegisterClient.modID().isEmpty() && !RosmontisLib.isModLoaded(LDLRegisterClient.modID())) {
                return false;
            }
        }
        try {
            clazz.getDeclaredConstructor();
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static <T> T createNoArgsInstance(Class<? extends T> clazz) {
        try {
            var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Wrapper<LDLRegister, T> toUINoArgsBuilder(Class<? extends T> clazz) {
        return new Wrapper<>(clazz.getAnnotation(LDLRegister.class), clazz, () -> createNoArgsInstance(clazz));
    }

    public static int UIWrapperSorter(Wrapper<LDLRegister, ?> a, Wrapper<LDLRegister, ?> b) {
        return b.annotation.priority() - a.annotation.priority();
    }

    public static <T> Wrapper<LDLRegisterClient, T> toClientUINoArgsBuilder(Class<? extends T> clazz) {
        return new Wrapper<>(clazz.getAnnotation(LDLRegisterClient.class), clazz, () -> AnnotationDetector.createNoArgsInstance(clazz));
    }

    public static int clientUIWrapperSorter(Wrapper<LDLRegisterClient, ?> a, Wrapper<LDLRegisterClient, ?> b) {
        return b.annotation().priority() - a.annotation().priority();
    }

}
