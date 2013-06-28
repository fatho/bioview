package de.zrho.bioview;

import java.awt.Color;
import java.io.File;

import javax.swing.JFrame;

import de.zrho.bioview.math.ExtremeCurrents;
import de.zrho.bioview.math.Matrix;
import de.zrho.bioview.model.Network;
import de.zrho.bioview.sbml.SBMLImport;
import de.zrho.bioview.view.MatrixFrame;
import de.zrho.bioview.view.colors.DiscreteInterpolatedColorMap;
import de.zrho.bioview.view.colors.ColorMaps;
import de.zrho.bioview.view.colors.ImmutableColorMap;
import de.zrho.bioview.view.matrix.MatrixTableModel;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Read network
		Network<String, Double> network;

		try {
			File inp = new File("sampledata/example.xml");
			network = SBMLImport.importNetwork(inp);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		Matrix st = network.getStoichiometry();
		// System.out.println(st);
		System.out.println(network.getReactants());

		// IN HASKELL: rowHeaders = map show (network ^. species)
		String[] rowHeaders = new String[network.getSpecies().size()];
		String[] colHeaders = new String[network.getReactions().size()];
		for (int i = 0; i < rowHeaders.length; i++)
			rowHeaders[i] = network.getSpecies().get(i).toString();
		for (int i = 0; i < colHeaders.length; i++) {
			String n = network.getReactions().get(i).getName();
			colHeaders[i] = n == null ? "R_" + i : n;
		}
		showMatrixFrame("Stoichiometric Matrix", st, rowHeaders, colHeaders,
				ColorMaps.positiveNegativeMap(Color.red, Color.white,
						Color.green, Double.MIN_NORMAL));
		

		Matrix extr = ExtremeCurrents.calculate(network.getStoichiometry());
		if(extr.getWidth() != 0 && extr.getHeight() != 0) {
			showMatrixFrame("Extreme Currents", extr, null, colHeaders, ColorMaps.BlackAndWhite);
			// Fun Fact: S * E^T = 0 € Mat
			System.out.println(st.mult(extr.transpose()));
		} else {
			System.out.println("No extreme currents");
		}
		
		
	}

	private static void showMatrixFrame(String title, Matrix st,
			String[] rowHeaders, String[] columnHeaders, ImmutableColorMap cmap) {
		MatrixFrame mf = new MatrixFrame();
		mf.getMatrixView().getTable()
				.setModel(new MatrixTableModel(st, rowHeaders, columnHeaders));
		if (cmap != null) {
			mf.setColorMap(cmap);
		}
		mf.setVisible(true);
		mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mf.setTitle(title);
	}

	private static Matrix randomMat(int n, int m) {
		Matrix mat = new Matrix(n, m);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				mat.set(i, j, Math.random());
			}
		}
		return mat;
	}
}
