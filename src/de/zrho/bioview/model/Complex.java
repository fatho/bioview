package de.zrho.bioview.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.zrho.bioview.math.VectorSpace;

public class Complex<S> implements VectorSpace<Integer, Complex<S>> {

	private Map<S, Double> coefficients;

	public Complex(Map<S, Double> coefficients) {
		this.coefficients = new HashMap<S, Double>();

		// Filter negative coefficients
		for (Entry<S, Double> e : coefficients.entrySet()) {
			if (e.getValue() > 0)
				this.coefficients.put(e.getKey(), e.getValue());
		}
	}

	public Map<S, Double> getCoefficients() {
		return Collections.unmodifiableMap(coefficients);
	}

	public double getCoefficient(S species) {
		Double coeff = coefficients.get(species);
		return (coeff == null) ? 0 : coeff;
	}

	public Set<S> getSpecies() {
		return coefficients.keySet();
	}

	@Override
	public Complex<S> add(Complex<S> that) {
		Map<S, Double> result = new HashMap<>(coefficients);

		for (Entry<S, Double> entry : that.getCoefficients().entrySet()) {
			S species = entry.getKey();
			double before = result.containsKey(species) ? result.get(species): 0;
			result.put(species, before + entry.getValue());
		}

		return new Complex<S>(result);
	}

	@Override
	public Complex<S> multScalar(Integer s) {
		Map<S, Double> result = new HashMap<>(coefficients);

		for (Entry<S, Double> entry : coefficients.entrySet()) {
			result.put(entry.getKey(), result.get(entry.getKey()) * s);
		}

		return new Complex<S>(result);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		int i = 0;

		for (Entry<S, Double> e : coefficients.entrySet()) {
			b.append(e.getValue());
			b.append(e.getKey());

			// Not last?
			if (++i < coefficients.size())
				b.append(" + ");
		}

		return b.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof Complex))
			return false;
		return ((Complex<?>) obj).coefficients.equals(coefficients);
	}

	@Override
	public int hashCode() {
		return coefficients.hashCode();
	}

}
