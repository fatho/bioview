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
	
	/**
	 * This method calculates the extreme current Matrix <tt>E(i, j)</tt> in <tt>M(n x |R|)</tt>,
	 * whereupon <tt>n >= 0</tt> is the number of extreme current paths <tt>P</tt>.
	 * 
	 * <pre>
	 * P_i = { R_j | e(i, j) = 1 }
	 * </pre>
	 *           
	 * @param stoichiometrixMatrix
	 * Stoichiometic coefficient matrix <br>
	 * <tt>s(i, j) = y'(i, j) - y(i, j)</tt> in <tt>M(|S| x |R|)</tt>.
	 * 
	 * @return The extreme current matrix.
	 * <tt>E(i, j) = 1</tt> when the path <tt>P_i</tt> contains the reaction <tt>R_j</tt>,
	 * otherwise <tt>E(i, j) = 0</tt>  
	 */
	public static Matrix calculate(Matrix stoichiometrixMatrix) {
		Matrix id = Matrix.identity(stoichiometrixMatrix.getWidth());
		
		PolcoAdapter polco;
	    Options opts = new Options();
	    opts.setLoglevel(Level.WARNING);
		try {
			polco = new PolcoAdapter(opts);
		} catch (XmlConfigException e) {
			e.printStackTrace();
			throw new RuntimeException("An error occured while initializing polco", e);
		}

	    double[][] rays = polco.getDoubleRays(stoichiometrixMatrix.getData(), id.getData());
	    
	    return new Matrix(rays);
	}
}
