package de.zrho.bioview.ui.colors;

import java.awt.Color;

import de.zrho.bioview.util.Interpolator;

/**
 * A continuous mapping between values and colors.
 * 
 * @author Fabian Thorand
 *
 */
public class ColorMap {

	protected double[] values;
	protected Color [] colors; 
	
	protected Interpolator<Double, ColorVector> interpolator;
	
	/**
	 * Initializes the color map. This class will copy the given arrays.
	 * 
	 * @param values
	 * @param colors
	 * @param interpolator
	 */
	public ColorMap(double[] values, Color[] colors, Interpolator<Double, ColorVector> interpolator) {
		if(values == null || colors == null || interpolator == null)
			throw new IllegalArgumentException("Arguments must not be null");
		if(values.length != colors.length) 
			throw new IllegalArgumentException("Number of values does not match number of colors");
		this.values = values.clone();
		this.colors = colors.clone();
		this.interpolator = interpolator;
	}
	
	/**
	 * Translates the value scale by the given amount.
	 * @param offset the offset that should be added to every entry in the color map.
	 */
	protected void translateHere(double offset) {
		for(int i = 0; i < values.length; i++) {
			values[i] += offset;
		}
	}

	/**
	 * Scales the value scale by the given amount.
	 * @param offset the offset that should be added to every entry in the color map.
	 */
	protected void scaleHere(double scale) {
		for(int i = 0; i < values.length; i++) {
			values[i] *= scale;
		}
	}
	
	/**
	 * Reverses the list of colors.
	 */
	protected void reverseHere() {
		for(int i = 0; i < values.length / 2; i++) {
			double tmp = values[i];
			values[i] = values[values.length - i - 1];
			values[values.length - i - 1] = tmp;
		}
	}
	
	/**
	 * Transforms a color map by translating it first and then applying the scale.
	 * @param translate the translate transform
	 * @param scale the scale transform
	 * @param reverse if true, the colors will be reversed.
	 * @return
	 */
	public ColorMap transform(double translate, double scale, boolean reverse) {
		ColorMap cpy = clone();
		cpy.translateHere(translate);
		cpy.scaleHere(scale);
		if(reverse)
			cpy.reverseHere();
		return cpy;
	}
	
	public ColorMap transform(double translate, double scale) {
		return transform(translate, scale, false);
	}
	
	/**
	 * Creates a new color map with a copy of the underlying arrays
	 */
	public ColorMap clone() {
		return new ColorMap(values, colors, interpolator);
	}
	
	/**
	 * Returns the color associated with the given value.
	 * @param value
	 * @return The interpolated color.
	 */
	public Color getColor(double value) {
		int low = -1;
		int high;
		for(high = 0; high < values.length; high++) {
			if(values[high] > value)
				break;
			low = high;
		}
		if(low == -1) {
			return colors[0];
		} else if(high >= colors.length) {
			return colors[colors.length - 1];
		} else {
			double dl = value - values[low];
			double dh = values[high] - value;
			double l = dl + dh;
			if(l == 0)
				return colors[low];
			double fraction = dl / l;

			return interpolator.interpolate(new ColorVector(colors[low]), new ColorVector(colors[high]), fraction).getColor(); 
		} 
	}

}
