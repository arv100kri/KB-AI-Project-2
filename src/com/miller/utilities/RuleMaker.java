package com.miller.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.miller.definitions.Frame;
import com.miller.definitions.Shape;

public class RuleMaker {

	public static final RuleMaker s_instance = new RuleMaker();
	private RuleMaker()
	{
		//Singleton
	}
	public static RuleMaker getInstance()
	{
		return s_instance;
	}
	/*
	 * If dimensions are 2x2 ==> Number of frames in reference =2x1=2, Number of frames in incomplete =1
	 * Else if 3x3 ==> Number of frames in reference=2x3=6 and number of frames in incomplete = 2
	 */
	private int estimateNumberOfShapesInAnswerFrame(int dimensions, List<Frame>referenceFrames, List<Frame>incompleteFrames)
	{
		int estimate = -1;
		if(dimensions == 2)
		{
			int diffInReference = referenceFrames.get(0).getNoOfShapes() - referenceFrames.get(1).getNoOfShapes();
			estimate = incompleteFrames.get(0).getNoOfShapes() - diffInReference;
		}
		else
		{
			int sumRow1 = referenceFrames.get(0).getNoOfShapes()+referenceFrames.get(1).getNoOfShapes()+referenceFrames.get(2).getNoOfShapes();
			int sumRow2 = referenceFrames.get(3).getNoOfShapes()+referenceFrames.get(4).getNoOfShapes()+referenceFrames.get(5).getNoOfShapes();
			if(sumRow1 == sumRow2)
			{
				int sumPartialRow = incompleteFrames.get(0).getNoOfShapes()+ incompleteFrames.get(1).getNoOfShapes();
				estimate = sumRow1 - sumPartialRow;
			}
		}
		return estimate;
	}
	
	private List<String> estimatedListOfShapesInAnswerFrame(int dimensions, List<Frame>referenceFrames, List<Frame>incompleteFrames)
	{
		List<String> estimateList = new ArrayList<String>();
		if(dimensions == 2)
		{
			List<Shape>shapesA = referenceFrames.get(0).getListOfShapes();
			List<Shape>shapesB = referenceFrames.get(1).getListOfShapes();
			Set<String> intersection = new HashSet<String>();
			Map<String,String> mapA = buildCanonicalMap(shapesA);
			for(Shape s: shapesB)
			{
				if(mapA.containsKey(s.getShapeName()))
				{
					intersection.add(mapA.get(s.getShapeName()));
				}
			}
			List<Shape> incompleteFrameShapes = incompleteFrames.get(0).getListOfShapes();
			Map<String,String> mapI = buildCanonicalMap(incompleteFrameShapes);
			for(Map.Entry<String, String> entry : mapI.entrySet())
			{
				if(intersection.contains(entry.getValue()))
				{
					estimateList.add(entry.getKey());
				}
			}
		}	
		else if(dimensions == 3)
		{
			List<Shape>shapesA = referenceFrames.get(0).getListOfShapes();
			List<Shape>shapesB = referenceFrames.get(1).getListOfShapes();
			List<Shape>shapesC = referenceFrames.get(2).getListOfShapes();
			List<Shape> intersection1 = new ArrayList<Shape>(); //A ^ B
			Map<String,String> mapA = buildCanonicalMap(shapesA);
			for(Shape s: shapesB)
			{
				if(mapA.containsKey(s.getShapeName()))
				{
					intersection1.add(s);
				}
			}
			
			Map<String, String> incompleteMap = buildCanonicalMap(intersection1);
			Set<String> intersection2 = new HashSet<String>();
			for(Shape s: shapesC)
			{
				if(incompleteMap.containsKey(s.getShapeName()))
				{
					intersection2.add(mapA.get(s.getShapeName()));
				}
			}
			List<Shape> incompleteFrameShapes = incompleteFrames.get(0).getListOfShapes();
			Map<String,String> mapI = buildCanonicalMap(incompleteFrameShapes);
			for(Map.Entry<String, String> entry : mapI.entrySet())
			{
				if(intersection2.contains(entry.getValue()))
				{
					estimateList.add(entry.getKey());
				}
			}
		}
		return estimateList;
	}
	
	private Map<String, String> buildCanonicalMap(List<Shape> shapes)
	{
		Map<String, String> canonicalMap = new HashMap<String, String>();
		int count = 1;
		for(Shape s: shapes)
		{
			String shapeName = s.getShapeName();
			if(!canonicalMap.containsKey(shapeName))
			{
				String canonicalShapeName = "Shape"+count;
				canonicalMap.put(shapeName, canonicalShapeName);
				count++;
			}
		}
		return canonicalMap;
	}
	
	public boolean isMatching(int dimension, List<Frame>referenceFrames, List<Frame> incompleteFrames, Frame potentialAnswerFrame)
	{
		boolean isMatch = false;
		List<String> shapesRequired = estimatedListOfShapesInAnswerFrame(dimension, referenceFrames, incompleteFrames);
		int noOfShapes = estimateNumberOfShapesInAnswerFrame(dimension, referenceFrames, incompleteFrames);
		if(noOfShapes>=0)
			if(potentialAnswerFrame.getNoOfShapes()!= noOfShapes)
				return false;
		if(shapesRequired.size()>0 && shapesRequired.size()<=noOfShapes)		
		{
			List<Shape>shapes = potentialAnswerFrame.getListOfShapes();
			List<String> shapeNames = new ArrayList<String>();
			for(Shape s: shapes)
			{
				shapeNames.add(s.getShapeName());
			}
			if(!shapeNames.containsAll(shapesRequired))
				return false;
			if(dimension == 2)
			{
				for(String name: shapeNames)
				{
					if(!shapesRequired.contains(name))
						return false;
				}

			}
		}
		if(dimension == 2)
		{
			if(noOfShapes == 1)	//1 shape solutions occupy full
			{
				//Fill relationship
				Shape shapeA = referenceFrames.get(0).getListOfShapes().get(0);
				Shape shapeB = referenceFrames.get(1).getListOfShapes().get(0);
				String fillA = shapeA.getAttribute().getFill();
				String fillB = shapeB.getAttribute().getFill();
				Shape shapeC = incompleteFrames.get(0).getListOfShapes().get(0);
				String fillC = shapeC.getAttribute().getFill();
				Shape shapeD = potentialAnswerFrame.getListOfShapes().get(0);
				String fillD = shapeD.getAttribute().getFill();
				if(fillA.equals(fillB) && !fillC.equals(fillD))
					return false;
				else if(!fillA.equals(fillB) && fillC.equals(fillD))
					return false;
				
				//Orientation
				int orientationA = shapeA.getAttribute().getOrientation();
				int orientationB = shapeB.getAttribute().getOrientation();
				int orientationC = shapeC.getAttribute().getOrientation();
				int orientationD = shapeD.getAttribute().getOrientation();
				
				if((orientationA - orientationB)!= (orientationC - orientationD))
					return false;
				
				isMatch = true;
			}
			
			else
			{
				/* Check fill pattern*/
				List<String> fills = new ArrayList<String>();
				List<Shape>shapesA = referenceFrames.get(0).getListOfShapes();
				for(Shape s: shapesA)
				{
					fills.add(s.getAttribute().getFill());
				}
				
				List<Shape>shapesB = referenceFrames.get(1).getListOfShapes();
				for(Shape s: shapesB)
				{
					fills.add(s.getAttribute().getFill());
				}
				
				int [] fillsDiff = getDiff(fills);
				
				List<String>answerFills = new ArrayList<String>();
				List<Shape>shapesC = incompleteFrames.get(0).getListOfShapes();
				for(Shape s: shapesC)
				{
					answerFills.add(s.getAttribute().getFill());
				}
				List<Shape>shapesD = potentialAnswerFrame.getListOfShapes();
				for(Shape s: shapesD)
				{
					answerFills.add(s.getAttribute().getFill());
				}
				int [] answerFillsDiff = getDiff(answerFills);
				
				int i=0;
				for(int x: answerFillsDiff)
				{
					if(fillsDiff[i++]!=x)
						return false;					
				}
				
				/* Fill pattern match has passed */
				/* Check position matching */
				List<String>positionA = new ArrayList<String>();
				for(Shape s: shapesA)
				{
					positionA.add(s.getAttribute().getFramePosition());
				}
				
				List<String>positionB = new ArrayList<String>();
				for(Shape s: shapesB)
				{
					positionB.add(s.getAttribute().getFramePosition());
				}
				
				List<String>positionC = new ArrayList<String>();
				for(Shape s: shapesC)
				{
					positionC.add(s.getAttribute().getFramePosition());
				}
				
				List<String>positionD = new ArrayList<String>();
				for(Shape s: shapesD)
				{
					positionD.add(s.getAttribute().getFramePosition());
				}
				
				if(positionC.containsAll(positionA))
				{
					if(!positionD.containsAll(positionB))
						return false;
				}
				
				/*Position match passed*/
				/* Orientation match passed */
				List<Integer>orientationA = new ArrayList<Integer>();
				for(Shape s: shapesA)
				{
					orientationA.add(s.getAttribute().getOrientation());
				}
						
				List<Integer>orientationB = new ArrayList<Integer>();
				for(Shape s: shapesB)
				{
					orientationB.add(s.getAttribute().getOrientation());
				}
						
				List<Integer>orientationC = new ArrayList<Integer>();
				for(Shape s: shapesC)
				{
					orientationC.add(s.getAttribute().getOrientation());
				}
						
				List<Integer>orientationD = new ArrayList<Integer>();
				for(Shape s: shapesD)
				{
					orientationD.add(s.getAttribute().getOrientation());
				}
						
				if(orientationC.containsAll(orientationA))
				{
					if(!orientationD.containsAll(orientationB))
					return false;
				}
					
				/*All tests have passed(?)*/
				isMatch = true;
			}
		}
		
		else if(dimension == 3)
		{			
			/*Check for orientation, fill and position*/
			
			/*Fill*/
			List<String>fillRow1 = new ArrayList<String>();
			List<Frame> row1 = new ArrayList<Frame>();
			row1.add(referenceFrames.get(0));
			row1.add(referenceFrames.get(1));
			row1.add(referenceFrames.get(2));
			
			for(Frame f: row1)
			{
				List<Shape> shapesinF = f.getListOfShapes();
				for(Shape s: shapesinF)
				{
					fillRow1.add(s.getAttribute().getFill());
				}
			}
			
			List<Frame> row2 = new ArrayList<Frame>();
			row2.add(referenceFrames.get(3));
			row2.add(referenceFrames.get(4));
			row2.add(referenceFrames.get(5));
			
			/*for(Frame f: row2)
			{
				List<Shape> shapesinF = f.getListOfShapes();
				for(Shape s: shapesinF)
				{
					fillRow2.add(s.getAttribute().getFill());
				}
			}
			
			Collections.sort(fillRow1);
			Collections.sort(fillRow2);
			*/
			
			Collections.sort(fillRow1);
			
			List<Frame>row3 = new ArrayList<Frame>();
			row3.add(incompleteFrames.get(0));
			row3.add(incompleteFrames.get(1));
			row3.add(potentialAnswerFrame);
			
			List<String> fillRow3 = new ArrayList<String>();
			for(Frame f: row3)
			{
				List<Shape> shapesinF = f.getListOfShapes();
				for(Shape s: shapesinF)
				{
					fillRow3.add(s.getAttribute().getFill());
				}
			}
			Collections.sort(fillRow3);
			if(!fillRow1.equals(fillRow3))
				return false;
			
			/* Orientation */
			List<Integer> row1Orientations = new ArrayList<Integer>();
			for(Frame f: row1)
			{
				List<Shape> shapesinF = f.getListOfShapes();
				for(Shape s: shapesinF)
				{
					row1Orientations.add(s.getAttribute().getOrientation());
				}
			}
			int [] diffArrayRow1 = new int[row1Orientations.size()-1];
			for(int i=0; i<row1Orientations.size()-1;i++)
			{
				diffArrayRow1[i] = row1Orientations.get(i) - row1Orientations.get(i+1);
			}
			/*
			List<Integer> row2Orientations = new ArrayList<Integer>();
			for(Frame f: row2)
			{
				List<Shape> shapesinF = f.getListOfShapes();
				for(Shape s: shapesinF)
				{
					row2Orientations.add(s.getAttribute().getOrientation());
				}
			}
			int [] diffArrayRow2 = new int[row2Orientations.size()-1];
			for(int i=0; i<row2Orientations.size()-1;i++)
			{
				diffArrayRow2[i] = row2Orientations.get(i) - row2Orientations.get(i+1);
			}
			*/ //Ignoring row2 orientations
			List<Integer> row3Orientations = new ArrayList<Integer>();
			for(Frame f: row3)
			{
				List<Shape> shapesinF = f.getListOfShapes();
				for(Shape s: shapesinF)
				{
					row3Orientations.add(s.getAttribute().getOrientation());
				}
			}
			int [] diffArrayRow3 = new int[row3Orientations.size()-1];
			for(int i=0; i<row3Orientations.size()-1;i++)
			{
				diffArrayRow3[i] = row3Orientations.get(i) - row3Orientations.get(i+1);
			}
			
			if(!isDiffArrayMatch(diffArrayRow1, diffArrayRow3))
				return false;
			
			/*Frame position*/
			
			List<String> row1Positions = new ArrayList<String>();
			for(Frame f: row1)
			{
				for(Shape s: f.getListOfShapes())
				{
					row1Positions.add(s.getAttribute().getFramePosition());
				}
			}
			
			List<String> row3Positions = new ArrayList<String>();
			for(Frame f: row3)
			{
				for(Shape s: f.getListOfShapes())
				{
					row3Positions.add(s.getAttribute().getFramePosition());
				}
			}
				
			Collections.sort(row1Positions);
			Collections.sort(row3Positions);
			
			if(!row1Positions.equals(row3Positions))
				return false;
			/*All tests have passed. Check for ordering of the shapes and best match*/
			isMatch = noOfShapes == 1?true:isSpecialCheckPassed(referenceFrames, incompleteFrames, potentialAnswerFrame);
		}
		return isMatch;
	}
	
	private boolean isSpecialCheckPassed(List<Frame> referenceFrames, List<Frame> incompleteFrames,
			Frame potentialAnswerFrame) {
		
		boolean returner=false;
		List<Frame> row1 = new ArrayList<Frame>(); 
		row1.add(referenceFrames.get(0));
		row1.add(referenceFrames.get(1));
		row1.add(referenceFrames.get(2));
		Map<String, String> cannonicalMappingReference = new HashMap<String, String>();
		for(Frame f : row1)
		{
			 cannonicalMappingReference = buildCanonicalMap(f.getListOfShapes());
		}
		Set<String>setOfShapesInReference = new HashSet<String>();
		for(Map.Entry<String, String> entry: cannonicalMappingReference.entrySet())
		{
			setOfShapesInReference.add(entry.getValue());
		}
		List<Frame> row3 = new ArrayList<Frame>();
		row3.add(incompleteFrames.get(0));
		row3.add(incompleteFrames.get(1));
		row3.add(potentialAnswerFrame);
		
		Map<String, String> cannonicalMappingIncomplete = new HashMap<String, String>();
		for(Frame f : row3)
		{
			 cannonicalMappingIncomplete = buildCanonicalMap(f.getListOfShapes());
		}
		Set<String>setOfShapesInIncomplete = new HashSet<String>();
		for(Map.Entry<String, String> entry: cannonicalMappingIncomplete.entrySet())
		{
			setOfShapesInIncomplete.add(entry.getValue());
		}
		
		if(setOfShapesInReference.size()!=setOfShapesInIncomplete.size())
			returner = false;
		else if(setOfShapesInIncomplete.equals(setOfShapesInReference))
		{
			returner = true;
		}
		else
		{
			returner = false;
		}
		
		return returner;
	}
	
	private boolean isDiffArrayMatch(int[] diffArray1, int[] diffArray2) {
		for(int i=0; i<diffArray1.length;i++)
		{
			if(diffArray1[i] != diffArray2[i])
			{
				System.out.println("Diff Values do not match. Pruning choice");
				return false;
			}
		}
		return true;
	}
	private int[] getDiff(List<String> fills) {
		int [] returnerDiff = new int[fills.size()-1];
		for(int i = 0; i<fills.size()-1;i++)
		{
			if(fills.get(i).equals(fills.get(i+1)))
			{
				returnerDiff[i] = 0;
			}
			else
			{
				returnerDiff[i] = 1;
			}
		}
			
		
		return returnerDiff;
				
	}
	
}
