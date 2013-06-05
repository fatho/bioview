package de.zrho.bioview.view.matrix;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;

/**
 * A subclass of JList that adorns a JTable with row headers.
 * 
 * @author Fabian Thorand
 *
 */
public class RowHeaderList extends JList<String> {
	private static final long serialVersionUID = -7046274660554110934L;
	
	private JTable table;
	
	private static final int MIN_ROW_HEIGHT = 12;
	
	public RowHeaderList(JTable table) {
		this.table = table;

	    // register model changed
	    table.addPropertyChangeListener("model", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				onTableModelChanged(evt);
			}
		});
		

	    this.setOpaque(false);
	    this.setFixedCellWidth(50);

	    // TODO: Can be integrated later for row-resizing support.
		MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
	        Cursor oldCursor;
	        Cursor RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
	        int index = -1;
	        int oldY = -1;

	        @Override
	        public void mousePressed(MouseEvent e) {
	            super.mousePressed(e);
	        }

	        @Override
	        public void mouseMoved(MouseEvent e) {
	            super.mouseMoved(e);
	            int previ = getLocationToIndex(new Point(e.getX(), e.getY() - 3));
	            int nexti = getLocationToIndex(new Point(e.getX(), e.getY() + 3));
	            if (previ != -1 && previ != nexti) {
	                if (!isResizeCursor()) {
	                    oldCursor = getCursor();
	                    setCursor(RESIZE_CURSOR);
	                    index = previ;
	                }
	            } else if (isResizeCursor()) {
	                setCursor(oldCursor);
	            }
	        }

	        private int getLocationToIndex(Point point) {
	            int i = locationToIndex(point);
	            if (!getCellBounds(i, i).contains(point)) {
	                i = -1;
	            }
	            return i;
	        }

	        @Override
	        public void mouseReleased(MouseEvent e) {
	            super.mouseReleased(e);
	            if (isResizeCursor()) {
	                setCursor(oldCursor);
	                index = -1;
	                oldY = -1;
	            }
	        }

	        @Override
	        public void mouseDragged(MouseEvent e) {
	            super.mouseDragged(e);
	            if (isResizeCursor() && index != -1) {
	                int y = e.getY();
	                if (oldY != -1) {
	                    int inc = y - oldY;
	                    int oldRowHeight = RowHeaderList.this.table.getRowHeight(index);
	                    if (oldRowHeight > 12 || inc > 0) {
	                        int rowHeight = Math.max(MIN_ROW_HEIGHT, oldRowHeight + inc);
	                        RowHeaderList.this.table.setRowHeight(index, rowHeight);
	                        if (RowHeaderList.this.getModel().getSize() > index + 1) {
	                            int rowHeight1 = RowHeaderList.this.table.getRowHeight(index + 1) - inc;
	                            rowHeight1 = Math.max(12, rowHeight1);
	                            RowHeaderList.this.table.setRowHeight(index + 1, rowHeight1);
	                        }
	                    }
	                }
	                oldY = y;
	            }
	        }

	        private boolean isResizeCursor() {
	            return getCursor() == RESIZE_CURSOR;
	        }
	    };
	    //this.addMouseListener(mouseAdapter);
	    //this.addMouseMotionListener(mouseAdapter);
	    //this.addMouseWheelListener(mouseAdapter);

	    this.setCellRenderer(new RowHeaderRenderer(table));
	    this.setBackground(table.getBackground());
	    this.setForeground(table.getForeground());
	    
	    onTableModelChanged(null);
	}
	
	private void onTableModelChanged(PropertyChangeEvent event) {
		if(table.getModel() instanceof RowHeaderTableModel) {
			setModel(new RowHeaderListModel((RowHeaderTableModel)table.getModel()));
		} else {
			setModel(new RowHeaderListModel(null));
		}
	}
	
}

class RowHeaderListModel extends AbstractListModel<String> {
	private static final long serialVersionUID = -4897836588791184846L;

	RowHeaderTableModel model;
	
	public RowHeaderListModel(RowHeaderTableModel model) {
		this.model = model;
	}
	
	@Override
	public int getSize() {
		return model == null ? 0 : model.getRowCount();
	}

	@Override
	public String getElementAt(int index) {
		return model == null ? "" : model.getRowName(index);
	}
	
}
