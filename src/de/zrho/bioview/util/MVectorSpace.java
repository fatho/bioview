package de.zrho.bioview.util;

public interface MVectorSpace<S, V> extends VectorSpace<S, V> {

	/** Adds a vector to this one in-place. */
	void addHere(V that);
	
	/** Multiplies this vector with a scalar in-place. */
	void multScalarHere(S s);
	
}
