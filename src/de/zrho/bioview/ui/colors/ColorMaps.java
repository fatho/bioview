package de.zrho.bioview.ui.colors;

import java.awt.Color;

import de.zrho.bioview.math.LinearInterpolator;
import de.zrho.bioview.math.NearestNeighbourInterpolator;

/**
 * Some predefined, nice looking color maps.
 * 
 * @author Fabian Thorand
 *
 */
public final class ColorMaps {
	/**
	 * A monochrome unit color map, that maps every value below 0.5 to black and every
	 * value above to white.
	 */
	public static final ColorMap BlackAndWhite = new ColorMap(new double[] { 0, 1 },
			new Color[] { Color.black, Color.white }, 
			new NearestNeighbourInterpolator<Double,ColorVector>(0.5));
	
	/**
	 * A gradient unit color map, that maps the values from 0 to 1 to the colors red, yellow and green.
	 */
	public static final ColorMap RedYellowGreen = new ColorMap(new double[] { 0, 0.5, 1 },
			new Color[] { Color.red, Color.yellow, Color.green}, 
			new LinearInterpolator<Double, ColorVector>());
}
