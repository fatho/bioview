package de.zrho.bioview.math;

public interface MVectorSpace<S, V> extends VectorSpace<S, V> {

	/** Adds a vector to this one in-place.
	 *
	 * @return The instance on which the method was called. This can
	 * be used for chaining multiple in-place operations together.
	 */
	V addHere(V that);
	
	/** Multiplies this vector with a scalar in-place.
	 * 
	 * @return The instance on which the method was called. This can
	 * be used for chaining multiple in-place operations together. */
	V multScalarHere(S s);
	
}
