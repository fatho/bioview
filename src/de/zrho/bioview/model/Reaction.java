package de.zrho.bioview.model;

public class Reaction<S, R> {

	private Complex<S> product;
	private Complex<S> reactant;
	private R rate;
	
	public Reaction(Complex<S> reactant, Complex<S> product, R rate) {
		this.product = product;
		this.reactant = reactant;
		this.rate = rate;
	}

	public Complex<S> getProduct() {
		return product;
	}
	
	public Complex<S> getReactant() {
		return reactant;
	}
	
	public R getRate() {
		return rate;
	}
	
	@Override
	public String toString() {
		return String.format("%s -(%f)-> %s", reactant, rate, product);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Reaction)) return false;
		Reaction<?, ?> react = (Reaction<?, ?>) obj;
		return react.getProduct().equals(product) && react.getReactant().equals(reactant) && react.getRate() == rate;
	}
	
	@Override
	public int hashCode() {
		return product.hashCode() ^ reactant.hashCode() ^ rate.hashCode();
	}
	
}