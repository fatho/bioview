package de.zrho.bioview.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import de.zrho.bioview.view.colors.DiscreteInterpolatedColorMap;
import de.zrho.bioview.view.colors.ColorMaps;
import de.zrho.bioview.view.colors.ImmutableColorMap;
import de.zrho.bioview.view.colors.MutableColorMap;
import de.zrho.bioview.view.matrix.ColoredMatrixCellRenderer;
import de.zrho.bioview.view.matrix.JMatrixView;

public class MatrixFrame extends JFrame {
	private static final long serialVersionUID = -6452762121233713000L;
	
	private JPanel contentPane;
	private JMatrixView matView;
	
	/**
	 * Create the frame.
	 */
	public MatrixFrame() {
		initComponents();
	}

	public void setColorMap(ImmutableColorMap colorMap) {
		ColoredMatrixCellRenderer renderer = 
			new ColoredMatrixCellRenderer(colorMap, "%.2f");
		// set reasonable default values
		matView.getTable().setDefaultRenderer(Double.class, renderer);
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
	
	public JMatrixView getMatrixView() {
		return matView;
	}
}
