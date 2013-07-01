package de.zrho.bioview.view;

import java.util.EventObject;

import javax.swing.JPanel;

import de.zrho.bioview.controller.Controller;

public class GraphPane extends JPanel implements VisualizationPanel {
	private static final long serialVersionUID = 292030325113028921L;

	private Controller controller;
	
	/**
	 * Create the panel.
	 */
	public GraphPane() {

	}

	@Override
	public void modelChanged(EventObject event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectController(Controller controller) {
		this.controller = controller;
		controller.addListener(this);
	}

}
