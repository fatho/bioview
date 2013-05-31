package de.zrho.bioview.model;

import java.util.List;

import de.zrho.bioview.math.Matrix;

public class MassActionNetwork<S, R extends Number> {
	
	public MassActionNetwork(Network<S, R> network) {
		this.network = network;
		deriveRates();
		deriveKinetic();
	}
	
	private Network<S, R> network;
	private Matrix rates;
	private Matrix kinetic;
	
	public Network<S, R> getNetwork() {
		return network;
	}
	
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
		List<Complex<S>> complexes = getNetwork().getComplexes();
		List<Reaction<S, R>> reactions = getNetwork().getReactions();
		rates = new Matrix(complexes.size(), complexes.size());
		
		for (Reaction<S, R> reaction : reactions) {
			int i = complexes.indexOf(reaction.getReactant());
			int j = complexes.indexOf(reaction.getProduct());
			
			rates.set(i, j, reaction.getRate().doubleValue());
		}
	}
	
	/** Derives the kinetic matrix A_k. Requires k (rates). */
	private void deriveKinetic() {
		List<Complex<S>> complexes = getNetwork().getComplexes();
		kinetic = new Matrix(complexes.size(), complexes.size());
		
		for (int i = 0; i < complexes.size(); ++i) {
			for (int j = 0; j < complexes.size(); ++j) {
				double value = 0;
				
				// Is diagonal entry?
				if (i == j) {
					for (int k = 0; k < complexes.size(); ++k) {
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
