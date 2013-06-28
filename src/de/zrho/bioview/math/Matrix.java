package de.zrho.bioview.math;

import java.util.Arrays;

public class Matrix implements MVectorSpace<Double, Matrix> {

	private int width;
	private int height;
	private double[][] data;
	
	public Matrix(int height, int width) {
		this.width = width;
		this.height = height;
		this.data = new double[height][width];
	}
	
	public Matrix(double[][] data) {
		this(data.length, data.length == 0 ? 0 : data[0].length);
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				this.data[i][j] = data[i][j];
			}
		}
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	/** Data of the matrix. Returns pointer, does not copy. */
	public double[][] getData() {
		return data;
	}
	
	public double get(int row, int col) {
		return data[row][col];
	}
	
	public void set(int row, int col, double val) {
		data[row][col] = val;
	}
	
	/** Multiplies this matrix with the given one, creating a new result matrix. */
	public Matrix mult(Matrix m) {
		if (m.getHeight() != width)
			throw new IllegalArgumentException("Dimension mismatch multiplying matrices.");
		
		Matrix result = new Matrix(height, m.getWidth());
		
		for (int i = 0; i < result.getHeight(); ++i) {
			for (int j = 0; j < result.getWidth(); ++j) {
				for (int k = 0; k < width; ++k) {
					result.data[i][j] += data[i][k] * m.data[k][j];
				}
			}
		}
		
		return result;
	}
	
	public Matrix clone() {
		return new Matrix(getData());
	}
	
	@Override
	public Matrix addHere(Matrix m) {
		if (m.getWidth() != width || m.getHeight() != height)
			throw new IllegalArgumentException("Dimension mismatch adding matrices.");
		
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				data[i][j] += m.getData()[i][j];
			}
		}
		return this;
	}
	
	@Override
	public Matrix add(Matrix m) {
		Matrix result = new Matrix(width, height);
		result.addHere(m);
		return result;
	}
	
	@Override
	public Matrix multScalar(Double s) {
		return multScalar((double) s);
	}
	
	@Override
	public Matrix multScalarHere(Double s) {
		return multScalarHere((double) s);
	}
	
	public Matrix multScalarHere(double s) {
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				data[i][j] = s * data[i][j];
			}
		}
		return this;
	}
	
	public Matrix multScalar(double s) {
		Matrix result = clone();
		result.multScalar(s);
		return result;
	}
	
	public Matrix transpose() {
		// TODO: O(nm) time and O(nm) space complexity 
		Matrix transposed = new Matrix(width, height);
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				transposed.set(j, i, get(i,j));
			}
		}
		return transposed;
	}
	
	@Override
	public String toString() {
		// Calculate dimensions of output
		int maxLength = 0;
		
		int maxWhole = 0;
		int maxFrac  = 0;
		
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				int whole = String.format("%.0f", data[i][j]).length();
				int fractional = 0;
				if(data[i][j] % 1 != 0)
					fractional = String.format("%s", data[i][j] % 1).length() - 1;
				maxWhole = Math.max(maxWhole, whole);
				maxFrac = Math.max(maxFrac, fractional);
			}
		}
		
		String format = String.format("%%%d.%df", maxWhole + maxFrac, Math.max(maxFrac - 1, 0));
		
		int outWidth = 3 + (maxLength + 1) * width;
		int outHeight = height;
		
		// Allocate buffer
		StringBuilder b = new StringBuilder(outWidth * outHeight);
		
		// Write matrix
		for (int i = 0; i < height; ++i) {
			b.append("| ");
			
			for (int j = 0; j < width; ++j) {
				b.append(String.format(format, data[i][j]));
				b.append(" ");
			}
			
			b.append("|\n");
		}
		
		return b.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.deepHashCode(data);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Matrix))
			return false;
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(data, other.data))
			return false;
		return true;
	}
	
	/**
	 * Creates an identity matrix.
	 * 
	 * @param size the width and height of the matrix
	 * @return the identity n x n matrix.
	 */
	public static Matrix identity(int size) {
		Matrix mat = new Matrix(size, size);
		for(int i = 0; i < size; i++) {
			mat.set(i, i, 1);
		}
		return mat;
	}
	
	/**
	 * @return the largest element in this matrix.
	 */
	public double max() {
		double max = Double.NEGATIVE_INFINITY;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				max = Math.max(data[i][j], max);
			}
		}
		return max;
	}
	
	/**
	 * @return the smallest element in this matrix.
	 */
	public double min() {
		double max = Double.POSITIVE_INFINITY;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				max = Math.min(data[i][j], max);
			}
		}
		return max;
	}
}
