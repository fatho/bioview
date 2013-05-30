package de.zrho.bioview.ui.matrix;

import de.zrho.bioview.math.Matrix;

public class MatrixFormatHelper {
	public static String getMinimalCellFormat(Matrix matrix) {
		int maxWhole = 0;
		int maxFrac  = 0;
		
		for (int i = 0; i < matrix.getHeight(); ++i) {
			for (int j = 0; j < matrix.getWidth(); ++j) {
				int whole = String.format("%.0f", matrix.get(i,j)).length();
				int fractional = 0;
				if(matrix.get(i,j) % 1 != 0)
					fractional = String.format("%s", Math.abs(matrix.get(i,j) % 1)).length() - 1;
				maxWhole = Math.max(maxWhole, whole);
				maxFrac = Math.max(maxFrac, fractional);
			}
		}
		
		return String.format("%%%d.%df", maxWhole + maxFrac, Math.max(maxFrac - 1, 0));
	}
}
