package de.zrho.bioview.view.matrix;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import sun.swing.table.DefaultTableCellHeaderRenderer;

public class ColumnHeaderRenderer extends DefaultTableCellHeaderRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(table.isColumnSelected(column)) {
        	comp.setFont(getFont().deriveFont(Font.BOLD));
        } else {
        	comp.setFont(getFont().deriveFont(Font.PLAIN));
        }
        return comp;
	}

}
