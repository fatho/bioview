package de.zrho.bioview.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import de.zrho.bioview.math.Matrix;
import de.zrho.bioview.ui.matrix.JMatrixView;
import de.zrho.bioview.ui.matrix.MatrixTableModel;

public class MatrixViewTestFrame extends JFrame {

	private static final long serialVersionUID = -7193222361535779431L;
	
	private JPanel contentPane;
	private JMatrixView matView;

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
					MatrixViewTestFrame frame = new MatrixViewTestFrame();
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
	public MatrixViewTestFrame() {
		initComponents();
		
		MatrixTableModel model = new MatrixTableModel(Matrix.identity(30).multScalarHere(123.345), null, null);
		matView.setModel(model);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		matView = new JMatrixView();
		contentPane.add(matView, BorderLayout.CENTER);
	}

}
