package de.zrho.bioview.ui.matrix;

import javax.swing.table.TableModel;

public interface RowHeaderTableModel extends TableModel {

	public String getRowName(int row);
}
