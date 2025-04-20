package com.freefish.rosmontislib.sync.managed;

import com.freefish.rosmontislib.sync.IContentChangeAware;
import com.freefish.rosmontislib.sync.IManaged;
import com.freefish.rosmontislib.sync.field.ManagedKey;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class ReadonlyRef implements IRef {

    @Override
    @Nullable
    public String getPersistedPrefixName() {
        return persistedPrefixName;
    }

    @Override
    public void setPersistedPrefixName(String persistedPrefixName) {
        this.persistedPrefixName = persistedPrefixName;
    }

    private String persistedPrefixName;
    protected final boolean lazy;
    private ManagedKey key;

    private boolean isSyncDirty;

    @Override
    public boolean isPersistedDirty() {
        return isPersistedDirty;
    }

    @Override
    public boolean isSyncDirty() {
        return isSyncDirty;
    }

    private boolean isPersistedDirty;

    protected BooleanConsumer onSyncListener = changed -> {
    };

    @Override
    public void setOnPersistedListener(BooleanConsumer onPersistedListener) {
        this.onPersistedListener = onPersistedListener;
    }

    @Override
    public void setOnSyncListener(BooleanConsumer onSyncListener) {
        this.onSyncListener = onSyncListener;
    }

    protected BooleanConsumer onPersistedListener = changed -> {
    };

    protected final WeakReference<?> reference;

    public ReadonlyRef(boolean lazy, Object value) {
        this.lazy = lazy;
        this.reference = new WeakReference<>(value);
        init();
    }

    protected void init() {
        if (!lazy) {
            if (getReference().get() instanceof IContentChangeAware handler) {
                replaceHandler(handler);
            } else if (readRaw() instanceof IManaged) {

            }
            else {
                throw new IllegalArgumentException("complex sync field must be an IContentChangeAware if not lazy!");
            }
        }
    }

    protected void replaceHandler(IContentChangeAware handler) {
        var onContentChanged = handler.getOnContentsChanged();
        if (onContentChanged != null) {
            handler.setOnContentsChanged(() -> {
                markAsDirty();
                onContentChanged.run();
            });
        } else {
            handler.setOnContentsChanged(() -> markAsDirty());
        }
    }

    @Override
    public ManagedKey getKey() {
        return key;
    }

    public IRef setKey(ManagedKey key) {
        this.key = key;
        return this;
    }

    public WeakReference<?> getReference() {
        return reference;
    }

    @Override
    public void clearSyncDirty() {
        isSyncDirty = false;
        if (readRaw() instanceof IManaged managed) {
            for (var field : managed.getSyncStorage().getSyncFields()) {
                field.clearSyncDirty();
            }
        }
        if (key.isDestSync()) {
            onSyncListener.accept(false);
        }
    }

    @Override
    public void clearPersistedDirty() {
        isPersistedDirty = false;
        if (readRaw() instanceof IManaged managed) {
            for (var field : managed.getSyncStorage().getPersistedFields()) {
                field.clearPersistedDirty();
            }
        }
        if (key.isPersist()) {
            onPersistedListener.accept(false);
        }
    }

    @Override
    public void markAsDirty() {
        if (key.isDestSync()) {
            isSyncDirty = true;
            onSyncListener.accept(true);
        }
        if (key.isPersist()) {
            isPersistedDirty = true;
            onPersistedListener.accept(true);
        }
    }

    @Override
    public void update() {
        if (readRaw() instanceof IManaged managed) {
            var storage = managed.getSyncStorage();

            for (IRef field : storage.getNonLazyFields()) {
                field.update();
            }

            if (storage.hasDirtySyncFields()) {
                if (key.isDestSync()) {
                    markAsDirty();
                } else {
                    for (var field : storage.getSyncFields()) {
                        field.clearSyncDirty();
                    }
                }
            }

            if (storage.hasDirtyPersistedFields()) {
                if (key.isPersist()) {
                    markAsDirty();
                } else {
                    for (var field : storage.getPersistedFields()) {
                        field.clearPersistedDirty();
                    }
                }
            }
        }
    }

    @Override
    public boolean isLazy() {
        return lazy;
    }


    @Override
    public <T> T readRaw() {
        //noinspection unchecked
        return (T) this.reference.get();
    }
}