package de.zrho.bioview.ui.matrix;

import javax.swing.table.AbstractTableModel;

import de.zrho.bioview.math.Matrix;

/**
 * Provides TableModel interface for matrices, in such a way as to enable them
 * to be displayed by JTable-like components.
 *  
 * @author Fabian Thorand
 *
 */
public class MatrixTableModel extends AbstractTableModel implements RowHeaderTableModel {

	private static final long serialVersionUID = -5897374052835309276L;
	
	private Matrix matrix;
	private String[] rowLabels, columnLabels;
	
	/**
	 * Initializes the MatrixTableModel.
	 * @param matrix The matrix to be displayed. This parameter must not be null.
	 * 
	 * @param rowLabels An optional array of row labels. If the parameter is not null, 
	 * the array must contain exactly one element for each matrix row.
	 * 
	 * @param columnLabels An optional array of column headers. If the parameter is not null, 
	 * the array must contain exactly one element for each matrix column.
	 */
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
			return toSpreadSheetName(column); // provide a default naming
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
			return Integer.toString(row); // provide a default naming
		else
			return rowLabels[row];
	}

	private String toSpreadSheetName(int num) {
		String result = "";
		do {
			int b = num % 26;
			char ch = (char)(b + 65);
			num = (num / 26) - 1;
			result = ch + result;
		} while(num >= 0);
		return result;
	}
}
