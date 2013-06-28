package de.zrho.bioview.view.colors;

public interface MutableColorMap extends ImmutableColorMap {
	
	MutableColorMap scaleHere(double scale);
	MutableColorMap translateHere(double translate);
	MutableColorMap reverseHere();
	
	ImmutableColorMap immutableClone();
}
