package de.zrho.bioview.model;

import java.util.Collections;
import java.util.List;

import de.zrho.bioview.util.Matrix;

/**
 * Chemical reaction network N = (S, C, R), with a species set S, a complex set C and a reaction set R.
 * 
 * @author Lukas Heidemann
 * @param <S> Type of the species.
 * @see http://reaction-networks.net/wiki/Chemical_reaction_network
 */
public class Network<S, R> {

	private List<S> species;
	private List<Complex<S>> complexes;
	private List<Reaction<S, R>> reactions;
	
	public Network(List<S> species, List<Complex<S>> complexes, List<Reaction<S, R>> reactions) {
		this.species = species;
		this.complexes = complexes;
		this.reactions = reactions;
		
		deriveStoichiometry();
	}

	private Matrix products;
	private Matrix reactants;
	private Matrix stoichiometry;
	
	public List<S> getSpecies() {
		return Collections.unmodifiableList(species);
	}

	public List<Complex<S>> getComplexes() {
		return Collections.unmodifiableList(complexes);
	}

	public List<Reaction<S, R>> getReactions() {
		return Collections.unmodifiableList(reactions);
	}

	/**
	 * Product coefficient matrix y'(i, j) in M(|S| x |R|).
	 * 
	 * y'(i, j) >= 0 is the coefficient of species S_i in the product complex of R_j.
	 * 
	 * @see http://reaction-networks.net/wiki/Stoichiometry
	 */
	public Matrix getProducts() {
		return products;
	}

	/**
	 * Reactant coefficient matrix y(i, j) in M(|S| x |R|).
	 * 
	 * y(i, j) >= 0 is the coefficient of species S_i in the reactant complex of R_j.
	 * 
	 * @see http://reaction-networks.net/wiki/Stoichiometry
	 */
	public Matrix getReactants() {
		return reactants;
	}

	/**
	 * Stoichiometic coefficient matrix s(i, j) = y'(i, j) - y(i, j) in M(|S| x |R|).
	 * 
	 * @see http://reaction-networks.net/wiki/Stoichiometry
	 */
	public Matrix getStoichiometry() {
		return stoichiometry;
	}
	
	/** Derives the product, reactant and stoichiometric matrices y', y and s. */
	private void deriveStoichiometry() {
		products = new Matrix(species.size(), reactions.size());
		reactants = new Matrix(species.size(), reactions.size());
		
		for (int i = 0; i < reactions.size(); ++i) {
			Reaction<S, R> reaction = reactions.get(i);
			Complex<S> product = reaction.getProduct();
			Complex<S> reactant = reaction.getReactant();
			
			for (int j = 0; j < species.size(); ++j) {
				S species = this.species.get(j);
				
				products.set(j, i, product.getCoefficient(species));
				reactants.set(j, i, reactant.getCoefficient(species));
			}
		}
		
		stoichiometry = reactants.clone();
		stoichiometry.multScalarHere(-1);
		stoichiometry.addHere(products);
	}
	
}
