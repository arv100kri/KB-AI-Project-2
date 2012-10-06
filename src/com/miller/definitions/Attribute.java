package com.miller.definitions;

public class Attribute {
	private String fill;
	private int orientation;
	private String framePosition;
	public String getFill() {
		return fill;
	}
	public void setFill(String fill) {
		this.fill = fill;
	}
	public int getOrientation() {
		return orientation;
	}
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	public String getFramePosition() {
		return framePosition;
	}
	public void setFramePosition(String framePosition) {
		this.framePosition = framePosition;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fill == null) ? 0 : fill.hashCode());
		result = prime
				* result
				+ ((framePosition == null) ? 0 : framePosition
						.hashCode());
		result = prime * result + orientation;
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
		Attribute other = (Attribute) obj;
		if (fill == null) {
			if (other.fill != null)
				return false;
		} else if (!fill.equals(other.fill))
			return false;
		if (framePosition == null) {
			if (other.framePosition != null)
				return false;
		} else if (!framePosition.equals(other.framePosition))
			return false;
		if (orientation != other.orientation)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Attribute [Fill=" + fill + ", orientation=" + orientation
				+ ", FramePosition=" + framePosition + "]";
	}
	
}
