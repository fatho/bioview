package de.zrho.bioview.view.colors;

import java.awt.Color;

public interface ImmutableColorMap {
	Color getColor(double value);
	
	ImmutableColorMap scale(double scale);
	ImmutableColorMap translate(double translate);
	ImmutableColorMap reverse();
	
	MutableColorMap mutableClone();
}
