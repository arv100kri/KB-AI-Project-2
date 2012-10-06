package com.miller.definitions;

public class Shape {
	private String shapeName;
	private Attribute attribute;
	public String getShapeName() {
		return shapeName;
	}
	public void setShapeName(String shapeName) {
		this.shapeName = shapeName;
	}
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	@Override
	public String toString() {
		return "Shape [shapeName=" + shapeName + ", attribute=" + attribute
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result
				+ ((shapeName == null) ? 0 : shapeName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shape other = (Shape) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (shapeName == null) {
			if (other.shapeName != null)
				return false;
		} else if (!shapeName.equals(other.shapeName))
			return false;
		return true;
	}
}
