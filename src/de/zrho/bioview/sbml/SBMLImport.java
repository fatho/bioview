package de.zrho.bioview.sbml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

import de.zrho.bioview.model.Complex;
import de.zrho.bioview.model.Network;
import de.zrho.bioview.model.Reaction;

public class SBMLImport {

	public static Network<String, Double> importNetwork(File file) throws Exception {
		return importNetwork(new SBMLReader().readSBML(file).getModel());
	}
	
	public static Network<String, Double> importNetwork(Model model) {
		// Import the list of species
		List<String> species = new ArrayList<String>(model.getListOfSpecies().size());
		for (Species s : model.getListOfSpecies()) species.add(importSpecies(s));
		
		// Import the reactions and collect complexes on the run
		List<Reaction<String, Double>> reactions = new ArrayList<Reaction<String, Double>>(model.getListOfReactions().size());
		Set<Complex<String>> complexes = new HashSet<Complex<String>>();
		
		for (org.sbml.jsbml.Reaction sourceReaction : model.getListOfReactions()) {
			Complex<String> reactants = importComplex(sourceReaction.getListOfReactants());
			Complex<String> products = importComplex(sourceReaction.getListOfProducts());
			// TODO Find rates
			complexes.add(reactants);
			complexes.add(products);
			reactions.add(new Reaction<String, Double>(reactants, products, 1.0));
		}
		
		// Create network
		List<Complex<String>> complexesList = new ArrayList<Complex<String>>(complexes.size());
		complexesList.addAll(complexes);
		
		return new Network<String, Double>(species, complexesList, reactions);
	}
	
	private static Complex<String> importComplex(ListOf<SpeciesReference> source) {
		Map<String, Integer> complex = new HashMap<String, Integer>();
		
		for (SpeciesReference ref : source) {
			complex.put(ref.getSpecies(), new Double(ref.getStoichiometry()).intValue());
		}
		
		return new Complex<String>(complex);
	}
	
	private static String importSpecies(Species source) {
		return source.getId();
	}
	
	//public static Complex<String> importComplex()
	
}
