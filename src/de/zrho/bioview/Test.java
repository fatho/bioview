package de.zrho.bioview;

import java.io.File;

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
			File inp = new File("sampledata/MBO.xml");
			network = SBMLImport.importNetwork(inp);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		System.out.println(network.getStoichiometry());
	}

}
