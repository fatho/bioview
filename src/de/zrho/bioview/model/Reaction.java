package de.zrho.bioview.model;

public class Reaction<S, R> {

	private Complex<S> product;
	private Complex<S> reactant;
	private R rate;

	public Reaction(Complex<S> reactant, Complex<S> product, R rate) {
		if (reactant == null)
			throw new IllegalArgumentException("reactant must not be null");
		if (product == null)
			throw new IllegalArgumentException("product must not be null");
		if (rate == null)
			throw new IllegalArgumentException("rate must not be null");

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + product.hashCode();
		result = prime * result + rate.hashCode();
		result = prime * result + reactant.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Reaction))
			return false;
		Reaction<?, ?> other = (Reaction<?, ?>) obj;
		return reactant.equals(other.reactant) && product.equals(other.product)
				&& rate.equals(other.rate);
	}

}