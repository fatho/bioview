package de.zrho.bioview.controller;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.EventListenerList;
import javax.xml.stream.XMLStreamException;

import de.zrho.bioview.math.Matrix;
import de.zrho.bioview.model.Network;
import de.zrho.bioview.model.Reaction;
import de.zrho.bioview.sbml.SBMLImport;
import de.zrho.bioview.view.BioViewMainFrame;
import de.zrho.bioview.view.MatrixFrame;
import de.zrho.bioview.view.colors.ColorMaps;
import de.zrho.bioview.view.colors.ImmutableColorMap;
import de.zrho.bioview.view.matrix.MatrixTableModel;

/**
 * The controller class in the MVC pattern.
 * 
 * @author Fabian Thorand
 * 
 */
public class Controller {
	private boolean initialized;
	
	private BioViewMainFrame mainFrame;
	private MatrixFrame stoichiometrixFrame;
	private MatrixFrame kineticFrame;
	private File currentFile;
	private Network<String, Double> network;
	private EventListenerList listeners;
	
	private Preferences userPrefs = Preferences.userNodeForPackage(de.zrho.bioview.controller.Controller.class);

	public Controller() {
		listeners = new EventListenerList();
	}
	
	/**
	 * Performs one-time initialization tasks of the application, including the
	 * creation of a visible frame.
	 * 
	 * @param args
	 */
	public synchronized void init(String[] args) {
		if (initialized)
			throw new IllegalStateException("Controller already initialized");
		initialized = true;
		// TODO: parse command-line
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					System.err.println("Unable to set look and feel");
					e1.printStackTrace();
				}
				try {
					setupMainView();
					mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setupMainView() {
		mainFrame = new BioViewMainFrame();
		mainFrame.connectController(this);
	}
	
	/**
	 * Unloads the currently opened document. 
	 */
	public void unloadDocument() {
		network = null;
		currentFile = null;
		if(stoichiometrixFrame != null)
			stoichiometrixFrame.dispose();
		for(Action act : actionsRequiringOpenFile) act.setEnabled(false);
		fireModelChanged();
	}

	/**
	 * Loads the specified SBML file in the current application.
	 * 
	 * @param filename
	 */
	public void loadSBMLFile(File file) {
		if(file.equals(currentFile))
			return;
		unloadDocument();
		try {
			network = SBMLImport.importNetwork(file);
			currentFile = file;
			fireModelChanged();
			// enable actions
			for(Action act : actionsRequiringOpenFile) act.setEnabled(true);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showStoichiometricMatrixFrame() {
		if(network == null)
			throw new IllegalStateException("No network loaded!");
		if(stoichiometrixFrame == null) {
			stoichiometrixFrame = new MatrixFrame();
			stoichiometrixFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		if(stoichiometrixFrame.isVisible()) {
			stoichiometrixFrame.setVisible(false);
		} else {
			String[] rowHeaders = new String[network.getSpecies().size()];
			String[] colHeaders = new String[network.getReactions().size()];
			for (int i = 0; i < rowHeaders.length; i++)
				rowHeaders[i] = network.getSpecies().get(i).toString();
			for (int i = 0; i < colHeaders.length; i++) {
				String n = network.getReactions().get(i).getName();
				colHeaders[i] = n == null ? "R_" + i : n;
			}
			Matrix st = network.getStoichiometry();
			stoichiometrixFrame.getMatrixView().getTable()
				.setModel(new MatrixTableModel(st, rowHeaders, colHeaders));
			
			ImmutableColorMap cmap = ColorMaps.linearMap(new double[] {st.min(), 0, st.max() },
					new Color[] {Color.red, Color.white, Color.green });
			stoichiometrixFrame.setColorMap(cmap);
			stoichiometrixFrame.setTitle("Stoichiometrix Matrix of " + currentFile.getName());
		}
		
		stoichiometrixFrame.setVisible(true);
	}

	public void showKineticMatrixFrame() {
		if(network == null)
			throw new IllegalStateException("No network loaded!");
		if(kineticFrame == null) {
			kineticFrame = new MatrixFrame();
			kineticFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		if(kineticFrame.isVisible()) {
			kineticFrame.setVisible(false);
		} else {
			String[] rowHeaders = new String[network.getSpecies().size()];
			String[] colHeaders = new String[network.getReactions().size()];
			for (int i = 0; i < rowHeaders.length; i++)
				rowHeaders[i] = network.getSpecies().get(i).toString();
			for (int i = 0; i < colHeaders.length; i++) {
				String n = network.getReactions().get(i).getName();
				colHeaders[i] = n == null ? "R_" + i : n;
			}
			Matrix st = network.getReactants();
			kineticFrame.getMatrixView().getTable()
				.setModel(new MatrixTableModel(st, rowHeaders, colHeaders));
			
			ImmutableColorMap cmap = ColorMaps.uniformLinearMap(0, st.max(), Color.white, Color.green);
			kineticFrame.setColorMap(cmap);
			kineticFrame.setTitle("Kinetic (Reactant) Matrix of " + currentFile.getName());
		}
		
		kineticFrame.setVisible(true);
	}
	
	/**
	 * Switches the to reaction pane and focuses the reaction.
	 * @param reaction
	 */
	public void focusReaction(Reaction<String, Double> reaction) {
		mainFrame.switchToReactionPane();
		mainFrame.getReactionPane().focusReaction(reaction);
	}
	
	/**
	 * Exits the application. This operation closes all open views.
	 */
	public void exit() {
		mainFrame.dispose();
		// TODO: add other exit-tasks
		initialized = false;
	}
	
	/** 
	 * @return the internal network model.
	 */
	public Network<String, Double> getNetwork() {
		return network;
	}
	
	/**
	 * @return the currently opened file (can be null, if no file was opened)
	 */
	public File getCurrentFile() {
		return currentFile;
	}
	
	// ======================= EVENTS =============================

	public void addListener(ControllerListener listener) {
		listeners.add(ControllerListener.class, listener);
	}
	
	public void removeListener(ControllerListener listener) {
		listeners.remove(ControllerListener.class, listener);
	}
	
	/**
	 * Fire modelChanged event on registered listeners.
	 */
	 protected void fireModelChanged() {
		 ControllerListener[] arr = listeners.getListeners(ControllerListener.class);
		 EventObject event = new EventObject(this);
		 for(ControllerListener l : arr) {
			 l.modelChanged(event);
		 }
	 }
	

	// ==================== ACTIONS ==================================
	public final Action loadFileAction = new LoadFileAction("Open SBML", null)
			.withDescription("Show a dialog for selecting and opening an SBML file")
			.withMnemonic(KeyEvent.VK_O)
			.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
	
	public final Action exitFileAction = new ExitAction("Exit", null)
			.withDescription("Exits the application.")
			.withMnemonic(KeyEvent.VK_E)
			.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
	
	public final Action showStoichiometricMatrixAction = new ShowStoichiometricMatrixAction("Show stoichiometric Matrix", null)
		.withDescription("Show the stoichiometric matrix in a new window.")
		.withMnemonic(KeyEvent.VK_S)
		.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK))
		.initiallyDisabled();
	
	public final Action showKineticMatrixAction = new ShowKineticMatrixAction("Show kinetic Matrix", null)
		.withDescription("Show the kinetic matrix in a new window.")
		.withMnemonic(KeyEvent.VK_K)
		.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK))
		.initiallyDisabled();
	
	private final Action[] actionsRequiringOpenFile = { showStoichiometricMatrixAction, showKineticMatrixAction };
	
	class LoadFileAction extends EasyInitAction {
		private static final long serialVersionUID = 5590547176947731901L;

		public LoadFileAction(String text, ImageIcon icon) {
			super(text, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO: initialize JFileChooser with last used directory
			File curDir = new File(userPrefs.get("fileopendir", "."));
			if(!curDir.exists())
				curDir = null;
			JFileChooser fc = new JFileChooser(curDir);
			fc.setFileFilter(new GenericFileFilter("SBML files", ".xml",
					".sbml"));
			if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
				userPrefs.put("fileopendir", fc.getSelectedFile().getParent());
				loadSBMLFile(fc.getSelectedFile());
			}
		}
	}

	class ExitAction extends EasyInitAction {
		private static final long serialVersionUID = -4532754281382190421L;

		public ExitAction(String text, ImageIcon icon) {
			super(text, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			exit();
		}

	}
	
	class ShowStoichiometricMatrixAction extends EasyInitAction {
		private static final long serialVersionUID = 5644675899459152445L;

		public ShowStoichiometricMatrixAction(String text, ImageIcon icon) {
			super(text, icon);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			showStoichiometricMatrixFrame();
		}
	}
	
	class ShowKineticMatrixAction extends EasyInitAction {
		private static final long serialVersionUID = 1203434711949602314L;

		public ShowKineticMatrixAction(String text, ImageIcon icon) {
			super(text, icon);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			showKineticMatrixFrame();
		}
	}
}
