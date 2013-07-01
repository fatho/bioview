package de.zrho.bioview.controller;

import java.util.EventListener;
import java.util.EventObject;

public interface ControllerListener extends EventListener {
	
	public void modelChanged(EventObject event);
	
}
