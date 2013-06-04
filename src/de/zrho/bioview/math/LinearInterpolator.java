package de.zrho.bioview.math;


public class LinearInterpolator<S extends Number, V extends VectorSpace<Number, V>> implements Interpolator<S,V> {

	@Override
	public V interpolate(V c1, V c2, S fraction) {
		Number inv = 1 - fraction.doubleValue();
		return c1.multScalar(inv).add(c2.multScalar(fraction));
	}

}
