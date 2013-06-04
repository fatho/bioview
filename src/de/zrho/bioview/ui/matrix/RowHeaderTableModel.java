package de.zrho.bioview.ui.matrix;

import javax.swing.table.TableModel;

/**
 * An interface for TableModels which also have named rows in addition to named columns.
 *  
 * @author Fabian Thorand
 *
 */
public interface RowHeaderTableModel extends TableModel {
	
	public String getRowName(int row);
}
