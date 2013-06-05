package de.zrho.bioview.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.stream.XMLStreamException;

import de.zrho.bioview.model.Network;
import de.zrho.bioview.sbml.SBMLImport;
import de.zrho.bioview.view.BioViewMainFrame;

/**
 * The controller class in the MVC pattern.
 * 
 * @author Fabian Thorand
 * 
 */
public class Controller {
	private boolean initialized;
	private BioViewMainFrame mainFrame;

	private Network<String, Double> network;

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
		mainFrame.setLoadAction(loadFileAction);
		mainFrame.setExitAction(exitFileAction);
	}

	/**
	 * Loads the specified SBML file in the current application.
	 * 
	 * @param filename
	 */
	public void loadSBMLFile(File file) {
		try {
			network = SBMLImport.importNetwork(file);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Exits the application. This operation closes all open views.
	 */
	public void exit() {
		mainFrame.dispose();
		// TODO: add other exit-tasks
		initialized = false;
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
	
	class LoadFileAction extends EasyInitAction {
		private static final long serialVersionUID = 5590547176947731901L;

		public LoadFileAction(String text, ImageIcon icon) {
			super(text, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO: initialize JFileChooser with last used directory
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new GenericFileFilter("SBML files", ".xml",
					".sbml"));
			if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
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
}
