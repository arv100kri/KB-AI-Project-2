package com.miller.definitions;

import java.util.ArrayList;
import java.util.List;

public class ProblemFormulation {
	private List<Matrix> matrices = new ArrayList<Matrix>();
	private int dimensions;
	public List<Matrix> getMatrices() {
		return matrices;
	}
	public void setMatrices(List<Matrix> matrices) {
		this.matrices = matrices;
	}
	public int getDimensions() {
		return dimensions;
	}
	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}
	@Override
	public String toString() {
		String returner = "";
		returner+="Dimensions: "+dimensions+"\n";
		for(Matrix matrix: matrices)
		{
			returner+=matrix;
		}
		
		return returner;
	}
	public void addMatrixList(List<Matrix> matrices2) {
		matrices.addAll(matrices2);
	}
}
