package com.freefish.rosmontislib.gui.factory;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.gui.editor.ui.UIEditor;
import com.freefish.rosmontislib.gui.modular.IUIHolder;
import com.freefish.rosmontislib.gui.modular.ModularUI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class UIEditorFactory extends UIFactory<UIEditorFactory> implements IUIHolder {

	public static final UIEditorFactory INSTANCE = new UIEditorFactory();

	private UIEditorFactory(){
		super(RosmontisLib.location("ui_editor"));
	}

	@Override
	protected ModularUI createUITemplate(UIEditorFactory holder, Player entityPlayer) {
		return createUI(entityPlayer);
	}

	@Override
	protected UIEditorFactory readHolderFromSyncData(FriendlyByteBuf syncData) {
		return this;
	}

	@Override
	protected void writeHolderToSyncData(FriendlyByteBuf syncData, UIEditorFactory holder) {

	}

	@Override
	public ModularUI createUI(Player entityPlayer) {
		return new ModularUI(this, entityPlayer)
				.widget(new UIEditor(RosmontisLib.MOD_ID));
	}

	@Override
	public boolean isInvalid() {
		return false;
	}

	@Override
	public boolean isRemote() {
		return RosmontisLib.isRemote();
	}

	@Override
	public void markAsDirty() {

	}
}
