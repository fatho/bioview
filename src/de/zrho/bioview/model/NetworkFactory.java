package de.zrho.bioview.model;

import java.util.List;

public interface NetworkFactory<S,R> {
	public Network<S,R> createNetwork(List<S> species, List<Complex<S>> complexes, List<Reaction<S,R>> reactions);
}
