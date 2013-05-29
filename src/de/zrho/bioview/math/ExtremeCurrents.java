package de.zrho.bioview.math;

import java.util.logging.Level;

import ch.javasoft.polco.adapter.Options;
import ch.javasoft.polco.adapter.Options.LogFormat;
import ch.javasoft.polco.adapter.PolcoAdapter;
import ch.javasoft.xml.config.XmlConfigException;

/**
 * This class provides a method to calculate the extreme currents of a 
 * stoichiometric matrix.
 * 
 * @author Fabian Thorand
 */
public final class ExtremeCurrents {
	
	public static Matrix calculate(Matrix stoichiometrixMatrix) {
		Matrix id = Matrix.identity(stoichiometrixMatrix.getWidth());
		
	    
		PolcoAdapter polco;
	    Options opts = new Options();
	    opts.setLoglevel(Level.WARNING);
		try {
			polco = new PolcoAdapter(opts);
		} catch (XmlConfigException e) {
			e.printStackTrace();
			throw new RuntimeException("Poorly designed third-party library", e);
		}

	    double[][] rays = polco.getDoubleRays(stoichiometrixMatrix.getData(), id.getData());
	    
	    return new Matrix(rays);
	}
}
