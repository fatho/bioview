package de.zrho.bioview.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import org.sbml.jsbml.Reaction;

import de.zrho.bioview.controller.Controller;
import de.zrho.bioview.controller.ControllerListener;

public class BioViewMainFrame extends JFrame implements ControllerListener {

	private static final long serialVersionUID = -4548800584629649126L;
	
	private Controller controller;
	
	private List<VisualizationPanel> panels;
	
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnfile;
	private JMenu mnView;
	private JToolBar toolBar;
	private JTabbedPane tabbedPane;
	private ComplexPane complexPane;
	private ReactionPane reactionPane;
	private GraphPane graphPane;

	/**
	 * Create the frame.
	 */
	public BioViewMainFrame() {
		initComponents();
	}
	
	private void initComponents() {
		setTitle("BioView");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 674, 482);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnfile = new JMenu("File");
		mnfile.setMnemonic('F');
		mnfile.setActionCommand("File");
		menuBar.add(mnfile);
		
		mnView = new JMenu("View");
		menuBar.add(mnView);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		complexPane = new ComplexPane();
		tabbedPane.add("Complexes", complexPane);
		
		reactionPane = new ReactionPane();
		tabbedPane.add("Reactions", reactionPane);
		
		graphPane = new GraphPane();
		tabbedPane.add("Network Graph", graphPane);
	}
	
	
	public void connectController(Controller controller) {
		this.controller = controller;
		
		controller.addListener(this);
		
		// build file menu
		mnfile.add(controller.loadFileAction);
		mnfile.addSeparator();
		mnfile.add(controller.exitFileAction);
		
		// build view menu
		mnView.add(controller.showStoichiometricMatrixAction);
		mnView.add(controller.showKineticMatrixAction);

		// build toolbar
		toolBar.add(controller.loadFileAction);
		toolBar.addSeparator();
		toolBar.add(controller.showStoichiometricMatrixAction);
		toolBar.add(controller.showKineticMatrixAction);
		
		// initialize tabs
		for(Component com : tabbedPane.getComponents()) {
			if(com instanceof VisualizationPanel) {
				((VisualizationPanel)com).connectController(controller);
			}
		}
		tabbedPane.setEnabled(false);
	}

	@Override
	public void modelChanged(EventObject event) {
		File curFile = controller.getCurrentFile();
		if(curFile == null) {
			setTitle("BioView");
			tabbedPane.setEnabled(false);
		} else {
			setTitle(String.format("BioView (%s)", curFile.getAbsolutePath()));
			tabbedPane.setEnabled(true);
		}
	}
}
