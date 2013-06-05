package de.zrho.bioview.controller;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public abstract class EasyInitAction extends AbstractAction {
	private static final long serialVersionUID = -6727680206112895359L;

	public EasyInitAction(String text, ImageIcon icon) {
		super(text, icon);
	}
	
	public EasyInitAction withMnemonic(int mnemonic) {
		return withProperty(MNEMONIC_KEY, mnemonic);
	}
	
	public EasyInitAction withAccelerator(KeyStroke keyStroke) {
		return withProperty(ACCELERATOR_KEY, keyStroke);
	}
	
	public EasyInitAction withDescription(String description) {
		return withProperty(SHORT_DESCRIPTION, description);
	}
	
	public EasyInitAction withProperty(String key, Object newValue) {
		putValue(key, newValue);
		return this;
	}
}
