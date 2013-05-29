package de.zrho.bioview.math;

public interface VectorSpace<S, V> {

	/** Multiplies the vector with a scalar. */
	V multScalar(S s);
	
	/** Adds a vector to this one and returns the resulting vector. */
	V add(V that);
	
}
