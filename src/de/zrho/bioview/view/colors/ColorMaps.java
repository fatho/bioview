package de.zrho.bioview.view.colors;

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
	 * A monochrome unit color map, that maps every value below 0.5 to black and
	 * every value above to white.
	 */
	public static final ImmutableColorMap BlackAndWhite = thresholdMap(
			Color.black, Color.white, 0.5).immutableClone();

	/**
	 * A gradient unit color map, that maps the values from 0 to 1 to the colors
	 * red, yellow and green.
	 */
	public static final ImmutableColorMap RedYellowGreen = uniformLinearMap(0,
			1, Color.red, Color.yellow, Color.green).immutableClone();
	
	public static MutableColorMap linearMap(double[] values, Color[] colors) {
		return new DiscreteInterpolatedColorMap(values, colors,
				new LinearInterpolator<Double, ColorVector>());
	}
	
	public static MutableColorMap uniformLinearMap(double low, double high,
			Color... colors) {
		if (colors.length == 0)
			throw new IllegalArgumentException("No colors!");
		if (low > high) {
			throw new IllegalArgumentException(
					"Lower bound is larger than upper bound");
		}

		double interval = colors.length == 1 ? 0 : (high - low)
				/ (colors.length - 1);
		double[] steps = new double[colors.length];
		for (int i = 0; i < steps.length; i++) {
			steps[i] = low + i * interval;
		}
		return new DiscreteInterpolatedColorMap(steps, colors,
				new LinearInterpolator<Double, ColorVector>());
	}

	public static MutableColorMap thresholdMap(Color lowColor, Color highColor,
			double threshold) {
		return new DiscreteInterpolatedColorMap(new double[] { threshold - 1, threshold + 1 },
				new Color[] { lowColor, highColor },
				new NearestNeighbourInterpolator<Double, ColorVector>(0.5));
	}

	/**
	 * Returns color map that contains only one color.
	 * 
	 * @param color
	 *            The uniform color of this color map.
	 * @return A single-colored color map.
	 */
	public static MutableColorMap constantColorMap(Color color) {
		return new DiscreteInterpolatedColorMap(new double[] { 0 }, new Color[] { color },
				new NearestNeighbourInterpolator<Double, ColorVector>(0.5));
	}

	public static MutableColorMap positiveNegativeMap(Color negative,
			Color zero, Color positive, double epsilon) {
		return new DiscreteInterpolatedColorMap(new double[] { -epsilon, 0, epsilon }, new Color[] {
				negative, zero, positive },
				new LinearInterpolator<Double, ColorVector>());
	}
}
