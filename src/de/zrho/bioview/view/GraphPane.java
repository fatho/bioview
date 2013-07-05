package de.zrho.bioview.view;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;

import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import de.zrho.bioview.controller.Controller;
import de.zrho.bioview.graph.GraphFactory;
import de.zrho.bioview.model.Complex;
import de.zrho.bioview.model.Reaction;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;

public class GraphPane extends JPanel implements VisualizationPanel {
	private static final long serialVersionUID = 292030325113028921L;

	private Controller controller;
	
	private Layout<Complex<String>, Reaction<String, Double>> layout;
	private Graph<Complex<String>, Reaction<String, Double>> graph;
	private DefaultModalGraphMouse<Complex<String>, Reaction<String, Double>> mouse;
	private VisualizationViewer<Complex<String>, Reaction<String, Double>> graphView;
	
	/**
	 * Create the panel.
	 */
	public GraphPane() {
		createComponents();
	}
	
	private void createComponents() {
		setLayout(new BorderLayout());
		
		// Default empty graph and layout
		graph = new SparseMultigraph<>();
		layout = new KKLayout<>(graph);
		
		// Create graph view
		graphView = new VisualizationViewer<>(layout);
		add(graphView, BorderLayout.CENTER);
		
		// Interaction
		mouse = new DefaultModalGraphMouse<>();
		mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		mouse.add(new PickingGraphMousePlugin<>());
		graphView.setGraphMouse(mouse);
		
		// Listeners
		graphView.getPickedEdgeState().addItemListener(new EdgePickListener());
		
		// Transformers
		RenderContext<Complex<String>, Reaction<String, Double>> ctx;
		ctx = graphView.getRenderContext();
		ctx.setVertexLabelTransformer(new VertexLabeller());
	}

	@Override
	public void modelChanged(EventObject event) {
		// Network loaded?
		if (null == controller.getNetwork())
			return;
		
		// Build graph and layout
		graph = GraphFactory.createGraph(controller.getNetwork());
		layout = new ISOMLayout<>(graph);
		
		// Update graph view
		graphView.setModel(new DefaultVisualizationModel<>(layout));
	}

	@Override
	public void connectController(Controller controller) {
		this.controller = controller;
		controller.addListener(this);
	}
	
	private class VertexLabeller implements Transformer<Complex<String>, String> {
		
		@Override
		public String transform(Complex<String> c) {
			return c.toString();
		}
		
	}
	
	private class EdgePickListener implements ItemListener {
		
		@SuppressWarnings("unchecked")
		@Override
		public void itemStateChanged(ItemEvent e) {
			controller.focusReaction((Reaction<String, Double>) e.getItem());
		}
		
	}
	
}
