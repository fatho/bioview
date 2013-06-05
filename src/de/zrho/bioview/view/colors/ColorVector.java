package de.zrho.bioview.view.colors;

import java.awt.Color;

import de.zrho.bioview.math.VectorSpace;

/**
 * An implementation of vector spaces for colors.
 * 
 * @author Fabian Thorand
 *
 */
public class ColorVector implements VectorSpace<Number, ColorVector> {
	private Color color;
	
	public ColorVector(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public ColorVector multScalar(Number s) {
		float[] f = color.getComponents(null);
		for(int i = 0; i < f.length; i++) {
			f[i] = s.floatValue() * f[i];
		}
		return new ColorVector(new Color(color.getColorSpace(), f, f[f.length-1]));
	}

	@Override
	public ColorVector add(ColorVector that) {
		float[] f = color.getComponents(null);
		float[] f2 = that.color.getComponents(null);
		for(int i = 0; i < f.length; i++) {
			f[i] = f[i] + f2[i];
		}
		return new ColorVector(new Color(color.getColorSpace(), f, f[f.length-1]));
	}
	
}
