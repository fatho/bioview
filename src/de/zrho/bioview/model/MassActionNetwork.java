package de.zrho.bioview.model;

import java.util.List;

import de.zrho.bioview.math.Matrix;

public class MassActionNetwork<S, R extends Number> extends Network<S, R> {

	public static class Factory<S, R extends Number> implements NetworkFactory<S, R> {

		@Override
		public MassActionNetwork<S,R> createNetwork(List<S> species,
				List<Complex<S>> complexes, List<Reaction<S,R>> reactions) {
			return new MassActionNetwork<S,R>(species, complexes, reactions);
		}
		
	}
	
	public MassActionNetwork(List<S> species, List<Complex<S>> complexes, List<Reaction<S, R>> reactions) {
		super(species, complexes, reactions);
		deriveRates();
		deriveKinetic();
	}
	
	private Matrix rates;
	private Matrix kinetic;
	
	/**
	 * Rate constant matrix k(i, j) in M(|C| x |C|).
	 * 
	 * k(i, j) > 0 is the rate constant corresponding to the reaction (C_i, C_j) in R,
	 * if (C_i, C_j) in R. Otherwise k(i, j) = 0.
	 * 
	 * @see http://reaction-networks.net/wiki/Mass-action_kinetics
	 */
	public Matrix getRates() {
		return rates;
	}

	/**
	 * Kinetic (Kirchhoff) matrix A_k(i, j) in M(|C| x |C|).
	 * 
	 * @see http://reaction-networks.net/wiki/Kinetic_matrix
	 */
	public Matrix getKinetic() {
		return kinetic;
	}

	/** Derives the rate matrix k. */
	private void deriveRates() {
		rates = new Matrix(getComplexes().size(), getComplexes().size());
		
		for (Reaction<S, R> reaction : getReactions()) {
			int i = getComplexes().indexOf(reaction.getReactant());
			int j = getComplexes().indexOf(reaction.getProduct());
			
			rates.set(i, j, reaction.getRate().doubleValue());
		}
	}
	
	/** Derives the kinetic matrix A_k. Requires k (rates). */
	private void deriveKinetic() {
		kinetic = new Matrix(getComplexes().size(), getComplexes().size());
		
		for (int i = 0; i < getComplexes().size(); ++i) {
			for (int j = 0; j < getComplexes().size(); ++j) {
				double value = 0;
				
				// Is diagonal entry?
				if (i == j) {
					for (int k = 0; k < getComplexes().size(); ++k) {
						value -= rates.get(j, k);
					}
				} else {
					value = rates.get(j, i);
				}
				
				kinetic.set(i, j, value);
			}
		}
	}
	
}
