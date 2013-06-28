package de.zrho.bioview.view.colors;

import java.awt.Color;

import de.zrho.bioview.math.Interpolator;

/**
 * A continuous mapping between values and colors.
 * 
 * @author Fabian Thorand
 *
 */
public class DiscreteInterpolatedColorMap implements MutableColorMap {

	protected double[] values;
	protected Color [] colors; 
	
	protected Interpolator<Double, ColorVector> interpolator;
	
	private static class ImmutableWrap implements ImmutableColorMap {
		ImmutableColorMap inner;
		
		public ImmutableWrap(ImmutableColorMap inner) {
			this.inner = inner;
		}
		
		@Override
		public Color getColor(double value) {
			return inner.getColor(value);
		}

		@Override
		public ImmutableColorMap scale(double scale) {
			return inner.scale(scale);
		}

		@Override
		public ImmutableColorMap translate(double translate) {
			return inner.translate(translate);
		}

		@Override
		public ImmutableColorMap reverse() {
			return inner.reverse();
		}

		@Override
		public MutableColorMap mutableClone() {
			return inner.mutableClone();
		}
		
	}
	
	/**
	 * Initializes the color map. This class will copy the given arrays.
	 * 
	 * @param values
	 * @param colors
	 * @param interpolator
	 */
	public DiscreteInterpolatedColorMap(double[] values, Color[] colors, Interpolator<Double, ColorVector> interpolator) {
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
	public MutableColorMap translateHere(double offset) {
		for(int i = 0; i < values.length; i++) {
			values[i] += offset;
		}
		return this;
	}

	/**
	 * Scales the value scale by the given amount.
	 * @param offset the offset that should be added to every entry in the color map.
	 */
	public MutableColorMap scaleHere(double scale) {
		for(int i = 0; i < values.length; i++) {
			values[i] *= scale;
		}
		return this;
	}
	
	/**
	 * Reverses the list of colors.
	 */
	public MutableColorMap reverseHere() {
		for(int i = 0; i < values.length / 2; i++) {
			double tmp = values[i];
			values[i] = values[values.length - i - 1];
			values[values.length - i - 1] = tmp;
		}
		return this;
	}
	
	public ImmutableColorMap scale(double scale) {
		return new ImmutableWrap(mutableClone().scaleHere(scale));
	}

	@Override
	public ImmutableColorMap translate(double translate) {;
		return new ImmutableWrap(mutableClone().translateHere(translate));
	}

	@Override
	public ImmutableColorMap reverse() {
		return new ImmutableWrap(mutableClone().reverseHere());
	}

	@Override
	public MutableColorMap mutableClone() {
		return clone();
	}
	
	/**
	 * Creates a new color map with a copy of the underlying arrays
	 */
	public DiscreteInterpolatedColorMap clone() {
		return new DiscreteInterpolatedColorMap(values, colors, interpolator);
	}

	@Override
	public ImmutableColorMap immutableClone() {
		return new ImmutableWrap(clone());
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
