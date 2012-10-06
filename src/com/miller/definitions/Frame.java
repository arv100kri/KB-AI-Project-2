package com.miller.definitions;

import java.util.ArrayList;
import java.util.List;

public class Frame {
	private List<Shape> listOfShapes = new ArrayList<Shape>();
	private int noOfShapes;
	private String frameName;

	public Frame(Frame frame) 
	{
		this.setFrameName(frame.getFrameName());
		this.setListOfShapes(frame.getListOfShapes());
		this.setNoOfShapes(frame.getNoOfShapes());
	}
	public Frame() {
		//Default Constructor
	}
	public String getFrameName() {
		return frameName;
	}
	public void setFrameName(String frameName) {
		this.frameName = frameName;
	}
	public List<Shape> getListOfShapes() {
		return listOfShapes;
	}
	public void setListOfShapes(List<Shape> listOfShapes) {
		this.listOfShapes = listOfShapes;
	}
	public int getNoOfShapes() {
		return noOfShapes;
	}
	public void setNoOfShapes(int noOfShapes) {
		this.noOfShapes = noOfShapes;
	}
	@Override
	public String toString() {
		String returner = "";
		returner+="Frame: "+frameName+"\n";
		for(Shape shape: listOfShapes)
		{
			returner+=shape+"\n";
		}
		return returner;
	}
	public void addToShapes(Shape shape) {
		listOfShapes.add(shape);
		
	}
	

}
