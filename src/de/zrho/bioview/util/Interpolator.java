package de.zrho.bioview.util;

/**
 * Interface for interpolating vectors.
 * 
 * @author Fabian Thorand.
 */
public interface Interpolator<S,V> {
	
	/**
	 * Interpolates between two vectors.
	 * @param c1 the first value
	 * @param c2 the second value
	 * @param fraction a value representing the amount of interpolation.
	 * @return the interpolated value.
	 */
	public V interpolate(V c1, V c2, S fraction);
}
