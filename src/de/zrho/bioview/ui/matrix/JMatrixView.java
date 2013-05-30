package de.zrho.bioview.ui.matrix;

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
		
		table.addPropertyChangeListener("model", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				table.doAutoSize();
			}
		});
		
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, table.getFont()
				.getSize()));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	public void setModel(TableModel model) {
		table.setModel(model);
	}

	public TableModel getModel() {
		return table.getModel();
	}

	class AutoResizeTable extends JTable {

		private static final long serialVersionUID = -1885889141856998846L;

		/*public boolean getScrollableTracksViewportWidth() {
			return getPreferredSize().width < getParent().getWidth();
		}*/
		
		public AutoResizeTable() {
		}
		
		void doAutoSize() {
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
