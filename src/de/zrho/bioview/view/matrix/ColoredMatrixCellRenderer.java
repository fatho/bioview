package de.zrho.bioview.view.matrix;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.zrho.bioview.math.Interpolator;
import de.zrho.bioview.math.LinearInterpolator;
import de.zrho.bioview.view.colors.DiscreteInterpolatedColorMap;
import de.zrho.bioview.view.colors.ColorVector;
import de.zrho.bioview.view.colors.ImmutableColorMap;

public class ColoredMatrixCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 5534866495705995402L;
	
	private ImmutableColorMap colorMap;
	private String cellFormat;
	private Interpolator<Number, ColorVector> linear;

	public ColoredMatrixCellRenderer(ImmutableColorMap colorMap) {
		this(colorMap, "%f");
	}
	
	public ColoredMatrixCellRenderer(ImmutableColorMap colorMap, String cellFormat) {
		this.colorMap = colorMap;
		this.linear = new LinearInterpolator<>();
		this.cellFormat = cellFormat;
	}
	
	private float getBrightness(Color c) {
		float[] rgb = c.getRGBComponents(null);
		return 0.299f * rgb[0] + 0.587f * rgb[1] + 0.114f * rgb[2];
	}
	
	private Color getTextColor(Color background) {
		if(getBrightness(background) > 0.5)
			return Color.BLACK;
		else
			return Color.WHITE;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		
		setValue(String.format(cellFormat, value));
		
		if(colorMap!= null && value != null && value instanceof Number) {
			double d = ((Number) value).doubleValue();
			Color bg = colorMap.getColor(d);
			if(isSelected) {
				// mix with selection color
				bg = linear.interpolate(new ColorVector(c.getBackground()), new ColorVector(bg), 0.5).getColor();
			}
			c.setBackground(bg);
			c.setForeground(getTextColor(bg));
		}
		
		return c;
	}
	
}
