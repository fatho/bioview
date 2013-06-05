package de.zrho.bioview.view.matrix;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * A visual component that consists of a JScrollPane containing a JTable and a RowHeaderList.
 * It is used to display instances of classes implementing RowHeaderTableModel.
 * 
 * @author Fabian Thorand
 */
public class JMatrixView extends JComponent {
	private static final long serialVersionUID = 8175807245269442429L;

	private AutoResizeTable table;
	private JScrollPane scrollPane;
	private RowHeaderList rowHeaders;

	public JMatrixView() {
		table = new AutoResizeTable();
		rowHeaders = new RowHeaderList(table);
		scrollPane = new JScrollPane(table);
		scrollPane.setRowHeaderView(rowHeaders);
		
		// react to changes of the table model
		table.addPropertyChangeListener("model", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				table.resizeToHeaders();
			}
		});
		// propagate font changes to children
		this.addPropertyChangeListener("font", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				table.setFont(getFont());
				scrollPane.setFont(getFont());
				rowHeaders.setFont(getFont());
			}
		});
		
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, table.getFont()
				.getSize()));

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public JTable getTable() {
		return table;
	}

	/**
	 * A subclass of JTable that resizes all columns to fit its contents, but is using
	 * all available horizontal space, if the columns don't fill up the entire width.
	 * 
	 * @author Fabian Thorand
	 *
	 */
	class AutoResizeTable extends JTable {

		private static final long serialVersionUID = -1885889141856998846L;

		public boolean getScrollableTracksViewportWidth() {
			return getPreferredSize().width < getParent().getWidth();
		}
		
		public AutoResizeTable() {
		}
		
		/**
		 * Adjusts the preferred width of all columns to their respective caption.
		 */
		void resizeToHeaders() {
			final TableCellRenderer renderer = table.getTableHeader()
					.getDefaultRenderer();
			for (int i = 0; i < table.getColumnCount(); ++i)
				table.getColumnModel()
						.getColumn(i)
						.setPreferredWidth(
								renderer.getTableCellRendererComponent(table,
										table.getModel().getColumnName(i), false,
										false, 0, i).getPreferredSize().width);
		}
		
		/**
		 * Adjusts the width of the column to the width of the widest cell.
		 * This method is called automatically in the JTable rendering logic. 
		 */
		public Component prepareRenderer(final TableCellRenderer renderer,
		        final int row, final int column) {
		    final Component prepareRenderer = super
		            .prepareRenderer(renderer, row, column);
		    final TableColumn tableColumn = getColumnModel().getColumn(column);

		    tableColumn.setPreferredWidth(Math.max(
		            prepareRenderer.getPreferredSize().width + 5,
		            tableColumn.getPreferredWidth()));

		    return prepareRenderer;
		}
	}
}
