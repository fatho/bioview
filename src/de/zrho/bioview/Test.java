package de.zrho.bioview;

import java.io.File;

import de.zrho.bioview.math.ExtremeCurrents;
import de.zrho.bioview.math.Matrix;
import de.zrho.bioview.model.MassActionNetwork;
import de.zrho.bioview.model.Network;
import de.zrho.bioview.sbml.SBMLImport;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Read network
		Network<String, Double> network;
		
		try {
			File inp = new File("sampledata/example.xml");
			network = SBMLImport.importNetwork(inp, new Network.Factory<String, Double>());
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		Matrix st = network.getStoichiometry();
		//System.out.println(st);
		//System.out.println(network.getReactants());
		
		Matrix extr = ExtremeCurrents.calculate(network.getStoichiometry());
		System.out.println(extr);
		
		Matrix test = new Matrix(extr.getHeight(), st.getHeight());
		
		for(int i = 0; i < extr.getHeight(); i++) {
			for(int k = 0; k < st.getHeight(); k++) {
				double sum = 0;
				for(int j = 0; j < extr.getWidth(); j++) {
					sum += extr.get(i, j) * st.get(k, j);
				}
				test.set(i, k, sum);
			}
		}
		System.out.print(test);
	}

}
