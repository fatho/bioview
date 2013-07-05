package de.zrho.bioview.view;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTabbedPane;

import de.zrho.bioview.controller.Controller;
import de.zrho.bioview.math.ExtremeCurrents;
import de.zrho.bioview.math.Matrix;
import de.zrho.bioview.model.Network;
import de.zrho.bioview.model.Reaction;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;

public class ReactionPane extends JPanel implements VisualizationPanel {
	private static final long serialVersionUID = 1690011024166678537L;

	private Controller controller;
	private DefaultListModel extremeCurrentListModel;
	private ReactionTableModel reactionTableModel;
	private Matrix extremeCurrents;
	
	private JSplitPane splitPane;
	private JPanel leftPanel;
	private JLabel lblExtremeCurrents;
	private JScrollPane extremeCurrentScrollPane;
	private JPanel rightPanel;
	private JScrollPane reactionScrollPane;
	private JLabel lblReactions;
	private JList extremeCurrentList;
	private JTable reactionTable;

	/**
	 * Create the panel.
	 */
	public ReactionPane() {

		initComponents();
		reactionTable.setDefaultRenderer(Object.class, new ExtremeCurrentTableRenderer());
	}
	
	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		splitPane = new JSplitPane();
		add(splitPane);
		
		leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		
		lblExtremeCurrents = new JLabel("Extreme Currents:");
		
		extremeCurrentScrollPane = new JScrollPane();
		GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
		gl_leftPanel.setHorizontalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(extremeCurrentScrollPane, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
						.addComponent(lblExtremeCurrents))
					.addContainerGap())
		);
		gl_leftPanel.setVerticalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblExtremeCurrents)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(extremeCurrentScrollPane, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		extremeCurrentList = new JList();
		extremeCurrentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		extremeCurrentList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent e) {
				onextremeCurrentListValueChanged(e);
			}
		});
		extremeCurrentScrollPane.setViewportView(extremeCurrentList);
		leftPanel.setLayout(gl_leftPanel);
		
		rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		
		reactionScrollPane = new JScrollPane();
		
		lblReactions = new JLabel("Reactions:");
		GroupLayout gl_rightPanel = new GroupLayout(rightPanel);
		gl_rightPanel.setHorizontalGroup(
			gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 393, Short.MAX_VALUE)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(reactionScrollPane, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(lblReactions))
					.addContainerGap())
		);
		gl_rightPanel.setVerticalGroup(
			gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 443, Short.MAX_VALUE)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblReactions)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(reactionScrollPane, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		reactionTable = new JTable();
		reactionScrollPane.setViewportView(reactionTable);
		rightPanel.setLayout(gl_rightPanel);
		splitPane.setDividerLocation(200);
	}

	@Override
	public void connectController(Controller controller) {
		this.controller = controller;
		controller.addListener(this);
		
		extremeCurrentListModel = new DefaultListModel<>();
		extremeCurrentList.setModel(extremeCurrentListModel);
	}
	
	@Override
	public void modelChanged(EventObject event) {
		Network<String, Double> network = controller.getNetwork();
		if(network == null) {
			extremeCurrentListModel.clear();
			reactionTable.setModel(new DefaultTableModel());
		} else {
			reactionTable.setModel(new ReactionTableModel(network));
			
			extremeCurrents = ExtremeCurrents.calculate(network.getStoichiometry());
			if(extremeCurrents.getHeight() == 0) {
				extremeCurrentList.setEnabled(false);
				extremeCurrentListModel.addElement("No extreme currents");
			} else {
				extremeCurrentList.setEnabled(true);
				for(int i = 0; i < extremeCurrents.getHeight(); i++) {
					extremeCurrentListModel.addElement("Current " + (i + 1));
				}
			}
		}
	}
	
	public void focusReaction(Reaction<String, Double> reaction) {
		int i = 0;
		for (Reaction<String, Double> r : controller.getNetwork().getReactions()) {
			if (r.equals(reaction)) {
				reactionTable.setColumnSelectionInterval(0, 2);
				reactionTable.setRowSelectionInterval(i, i);
				reactionTable.scrollRectToVisible(reactionTable.getCellRect(i, 0, true));
				break;
			} else ++i;
		}
	}
	
	protected void onextremeCurrentListValueChanged(final ListSelectionEvent e) {
		reactionTable.repaint();
	}
	
	class ExtremeCurrentTableRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -3223720247161047286L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			if(!isSelected && !hasFocus)
				comp.setBackground(table.getBackground());
			
			if(extremeCurrents != null) {
				int curIdx = extremeCurrentList.getSelectedIndex();
				if(curIdx >= 0 && extremeCurrents.get(curIdx, row) != 0) {
					float[] rgb = comp.getBackground().getRGBColorComponents(null);
					rgb[0] = 0.5f * (rgb[0] + 1);
					rgb[1] = 0.5f * (rgb[1] + 0);
					rgb[2] = 0.5f * (rgb[2] + 0);
					comp.setBackground(new Color(rgb[0], rgb[1], rgb[2]));
				}
			}
			
			return comp;
		}
	}
}
