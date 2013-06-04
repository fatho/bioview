package de.zrho.bioview.util;

import de.zrho.bioview.math.VectorSpace;

public class NearestNeighbourInterpolator<S extends Comparable<S>, V>
		implements Interpolator<S, V> {

	private S threshold;

	public NearestNeighbourInterpolator(S threshold) {
		this.threshold = threshold;
	}

	@Override
	public V interpolate(V c1, V c2, S fraction) {
		return fraction.compareTo(threshold) <= 0 ? c1 : c2;
	}

}
