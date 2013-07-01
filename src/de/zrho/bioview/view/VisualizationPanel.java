package de.zrho.bioview.view;

import de.zrho.bioview.controller.Controller;
import de.zrho.bioview.controller.ControllerListener;

/**
 * An interface for views that can visualize Networks.
 * @author Fabian Thorand
 *
 */
public interface VisualizationPanel extends ControllerListener {
	public void connectController(Controller controller);
}
