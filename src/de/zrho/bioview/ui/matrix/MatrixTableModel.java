package de.zrho.bioview.ui.matrix;

import javax.swing.table.AbstractTableModel;

import de.zrho.bioview.math.Matrix;

public class MatrixTableModel extends AbstractTableModel implements RowHeaderTableModel {

	private static final long serialVersionUID = -5897374052835309276L;
	
	private Matrix matrix;
	private String[] rowLabels, columnLabels;
	
	public MatrixTableModel(Matrix matrix, String[] rowLabels, String[] columnLabels) {
		if(matrix == null)
			throw new IllegalArgumentException("matrix must not be null");
		this.matrix = matrix;
		if(rowLabels != null && rowLabels.length != matrix.getHeight())
			throw new IllegalArgumentException("Number of row labels does not match height of matrix.");
		this.rowLabels = rowLabels;
		if(columnLabels != null && columnLabels.length != matrix.getWidth())
			throw new IllegalArgumentException("Number of column labels does not match width of matrix.");
		this.columnLabels = columnLabels;
	}

	public Matrix getMatrix() {
		return matrix;
	}
	
	public String[] getRowLabels() {
		return rowLabels;
	}
	
	public String[] getColumnLabels() {
		return columnLabels;
	}
	
	@Override
	public int getRowCount() {
		return matrix.getHeight();
	}

	@Override
	public int getColumnCount() {
		return matrix.getWidth();
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Double.class;
	}
	
	@Override
	public String getColumnName(int column) {
		if(columnLabels == null)
			return "C_" + column;
		else
			return columnLabels[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return matrix.get(rowIndex, columnIndex);
	}

	@Override
	public String getRowName(int row) {
		if(rowLabels == null)
			return "R_" + row;
		else
			return rowLabels[row];
	}

}
