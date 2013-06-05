package de.zrho.bioview.view.matrix;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

/**
 * The renderer that is used by RowHeaderList to display row headers.
 * 
 * @author Fabian Thorand
 */
class RowHeaderRenderer extends JLabel implements ListCellRenderer<String> {

	private static final long serialVersionUID = 3072333451605749211L;
	
	private JTable table;

    RowHeaderRenderer(JTable table) {
        this.table = table;
        JTableHeader header = this.table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
        setDoubleBuffered(true);
    }


	@Override
	public Component getListCellRendererComponent(JList<? extends String> list,
			String value, int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value);
        setPreferredSize(null);
        setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), table.getRowHeight(index)));
        //trick to force repaint on JList (set updateLayoutStateNeeded = true) on BasicListUI
        list.firePropertyChange("cellRenderer", 0, 1);
        return this;
	}
	
}