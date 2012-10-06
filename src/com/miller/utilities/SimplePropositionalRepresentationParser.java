package com.miller.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.miller.definitions.Attribute;
import com.miller.definitions.Frame;
import com.miller.definitions.Matrix;
import com.miller.definitions.ProblemFormulation;
import com.miller.definitions.Shape;


public class SimplePropositionalRepresentationParser {
	
/*-------------Constants--------------------------------*/
	private static final String MATRIX = "<<Matrix>>";
	private static final String path_prefix = "Representations/";
	private static final String PLACEHOLDER = "<<PlaceHolder>>";
	private static final String END_MATRIX = "<<EndMatrix";
	private static final String MATRIX_ID = "<<MatrixId";
	private static final String FRAME = "<<Frame>>";
	private static final String DIMENSION = "<<Dimensions>>";
	private static final String REPRESENTATION = "<<Representation>>";
	private static final String END_FRAME = "<<EndFrame>>";
	private static final String FRAME_NAME = "<<FrameName>>";
	private static final String NOOFSHAPES = "<<NoOfShapes>>";
	private static final String SHAPES = "<<Shapes>>";
	private static final String END_SHAPE = "<<EndShapes>>";
	private static final String SHAPE_NAME = "<<ShapeName>>";
	private static final String ATTRIBUTES = "<<Attributes>>";
	private static final String END_ATTRIBUTES = "<<EndAttributes>>";
	private static final String FILL = "<<Fill>>";
	private static final String ORIENTATION = "<<Orientation>>";
	private static final String FRAMEPOSITION = "<<FramePosition>>";
	private static final String END_REPRESENTATION = "<<EndRepresentation>>";
/*---------------End of Constants----------------------*/
	private static final SimplePropositionalRepresentationParser s_instance = new SimplePropositionalRepresentationParser();
		
	private SimplePropositionalRepresentationParser()
	{
		//singleton
	}
	
	public static SimplePropositionalRepresentationParser getInstance()
	{
		return s_instance;
	}
	
	public ProblemFormulation parseTextFile(String FileName) throws IOException
	{
		ProblemFormulation problem = new ProblemFormulation();
		problem.setDimensions(0);
		List<Matrix> matrices = new ArrayList<Matrix>();
		String actualPath = path_prefix + FileName;
		FileInputStream fileInputStream = new FileInputStream(actualPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(fileInputStream)));
		String line="";
		while((line = br.readLine())!=null)
		{
			if(line.length()>0 && line.contains("/*"))	// Skipping over the comments
			{
				while(true)
				{
					line = br.readLine();
					if(line.contains("*/"))
						break;
				}
			}
			
			else if(line.contains(PLACEHOLDER))
			{
				return problem;
			}
			
			else if(line.contains(REPRESENTATION))
			{
				System.out.println(line);
				line = br.readLine();
				while(!line.contains(END_REPRESENTATION))
				{		
					if(line.contains(DIMENSION))
					{
						String [] splitter = line.split(":");
						String [] dimensions = splitter[1].split(",");
						problem.setDimensions(Integer.parseInt(dimensions[0]));
					}
					else if(line.contains(MATRIX))
					{
						System.out.println(line);
						Matrix matrix = new Matrix();
						line = br.readLine();
						System.out.println(line);
						while(!line.contains(END_MATRIX))
						{
							if(line.contains(MATRIX_ID))
							{
								String [] splitter = line.split(":");
								matrix.setId(Integer.parseInt(splitter[1]));
							}
							else if(line.contains(FRAME))
							{
								line = br.readLine();
								System.out.println(line);
								Frame frame = new Frame();
								while(!line.contains(END_FRAME))
								{
									String [] pair;
									if(line.contains(FRAME_NAME))
									{
										pair = line.split(":");
										frame.setFrameName(pair[1]);
									}
									else if(line.contains(NOOFSHAPES))
									{
										pair = line.split(":");
										frame.setNoOfShapes(Integer.parseInt(pair[1]));
									}
									else if(line.contains(SHAPES))
									{
										System.out.println(line);
										line = br.readLine();
										Shape shape = new Shape();
										while(!line.contains(END_SHAPE))
										{
											if(line.contains(SHAPE_NAME))
											{
												pair = line.split(":");
												shape.setShapeName(pair[1]);
											}
											else if(line.contains(ATTRIBUTES))
											{
												System.out.println(line);
												line = br.readLine();
												Attribute attribute = new Attribute();
												while(!line.contains(END_ATTRIBUTES))
												{
													if(line.contains(FILL))
													{
														pair = line.split(":");
														attribute.setFill(pair[1]);
													}
													else if(line.contains(ORIENTATION))
													{
														pair = line.split(":");
														attribute.setOrientation(Integer.parseInt(pair[1]));
													}
													
													else if(line.contains(FRAMEPOSITION))
													{
														pair = line.split(":");
														attribute.setFramePosition(pair[1]);
													}
													line = br.readLine();
													System.out.println(line);
												}
												shape.setAttribute(attribute);
											}
											line = br.readLine();
											System.out.println(line);
										}
										frame.addToShapes(shape);
									}
									line = br.readLine();
									System.out.println(line);
								}
									System.out.println("Adding the frame \n"+ frame);
									matrix.addFrame(frame);
							}
							line = br.readLine();
							System.out.println(line);
						}
						matrices.add(matrix);
					}
					line = br.readLine();
					System.out.println(line);
				}
			}
			System.out.println(line);
		}
		System.out.println("--------------------------------------");
		problem.addMatrixList(matrices);
		return problem;
	}
}