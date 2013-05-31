package de.zrho.bioview.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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

public class BioViewMainFrame extends JFrame {

	private static final long serialVersionUID = -4548800584629649126L;
	
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnfile;
	private JMenu mnView;
	private JMenuItem mntmOpenSbml;
	private JSeparator menuItem;
	private JMenuItem mntmExit;
	private JToolBar toolBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					System.err.println("Unable to set look and feel");
					e1.printStackTrace();
				}
				try {
					BioViewMainFrame frame = new BioViewMainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		
		mntmOpenSbml = new JMenuItem("Open SBML");
		mntmOpenSbml.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnfile.add(mntmOpenSbml);
		
		menuItem = new JSeparator();
		mnfile.add(menuItem);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				onmntmExitActionPerformed(e);
			}
		});
		mnfile.add(mntmExit);
		
		mnView = new JMenu("View");
		menuBar.add(mnView);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
	}

	protected void onmntmExitActionPerformed(final ActionEvent e) {
		this.dispose();
	}
}
