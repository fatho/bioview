package de.zrho.bioview.view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.EventObject;

import javax.swing.DefaultListModel;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;

import com.sun.org.apache.bcel.internal.generic.LSTORE;

import de.zrho.bioview.controller.Controller;
import de.zrho.bioview.model.Complex;
import de.zrho.bioview.model.Network;
import de.zrho.bioview.model.Reaction;

public class ComplexPane extends JPanel implements VisualizationPanel {
	private static final long serialVersionUID = 8124690289304424552L;

	private Controller controller;
	private DefaultListModel speciesListModel;
	private DefaultListModel complexListModel;
	
	private JSplitPane splitPane;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JLabel lblComplexes;
	private JLabel lblSpecies;
	private JScrollPane complexScrollPane;
	private JScrollPane speciesScrollPane;
	private JList complexList;
	private JList speciesList;

	/**
	 * Create the panel.
	 */
	public ComplexPane() {

		initComponents();
	}
	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		add(splitPane, BorderLayout.CENTER);
		
		leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		
		lblComplexes = new JLabel("Complexes:");
		
		complexScrollPane = new JScrollPane();
		GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
		gl_leftPanel.setHorizontalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(complexScrollPane, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
						.addComponent(lblComplexes))
					.addContainerGap())
		);
		gl_leftPanel.setVerticalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblComplexes)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(complexScrollPane, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		complexList = new JList();
		complexScrollPane.setViewportView(complexList);
		leftPanel.setLayout(gl_leftPanel);
		
		rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		
		lblSpecies = new JLabel("Species:");
		
		speciesScrollPane = new JScrollPane();
		GroupLayout gl_rightPanel = new GroupLayout(rightPanel);
		gl_rightPanel.setHorizontalGroup(
			gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(speciesScrollPane, GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
						.addComponent(lblSpecies))
					.addContainerGap())
		);
		gl_rightPanel.setVerticalGroup(
			gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSpecies)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(speciesScrollPane, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		speciesList = new JList();
		speciesScrollPane.setViewportView(speciesList);
		rightPanel.setLayout(gl_rightPanel);
		splitPane.setDividerLocation(250);
	}

	@Override
	public void connectController(Controller controller) {
		this.controller = controller;
		controller.addListener(this);
		
		speciesListModel = new DefaultListModel<>();
		speciesList.setModel(speciesListModel);
		complexListModel = new DefaultListModel<>();
		complexList.setModel(complexListModel);
	}
	
	@Override
	public void modelChanged(EventObject event) {
		Network<String, Double> network = controller.getNetwork();
		if(network == null) {
			speciesListModel.clear();
			complexListModel.clear();
		} else {
			for(String species : network.getSpecies()) {
				speciesListModel.addElement(species);
			}
			for(Complex<String> complex : network.getComplexes()) {
				complexListModel.addElement(complex);
			}
		}
	}

}
