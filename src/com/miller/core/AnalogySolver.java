package com.miller.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.miller.definitions.Frame;
import com.miller.definitions.ProblemFormulation;
import com.miller.utilities.AnalogySolverHelper;
import com.miller.utilities.SimplePropositionalRepresentationParser;

public class AnalogySolver 
{
	public static void main(String [] args)
	{
		System.out.println("**************************************************");
		SimplePropositionalRepresentationParser parser = SimplePropositionalRepresentationParser.getInstance();
		String [] filesList = new String [] {"2-1.txt", "2-2.txt", "2-3.txt", "2-4.txt", "2-5.txt", "2-6.txt", "2-7.txt", "2-8.txt"};
		List<ProblemFormulation> problemList = new ArrayList<ProblemFormulation>();
		List<Frame> answersToProblems = new ArrayList<Frame>();
		try 
		{
			for(String fileName: filesList)
			{
				System.out.println("\n Parsing the file: "+fileName);
				ProblemFormulation problem = parser.parseTextFile(fileName);
				if(problem.getDimensions()==0)
				{
					System.out.println("PlaceHolder file");
				}
				else
				{
					problemList.add(problem);
				}
			}
			AnalogySolverHelper analogySolver = AnalogySolverHelper.getInstance();
			for(ProblemFormulation p: problemList)
			{
				System.out.println("-----------------------------------------");
				System.out.println(p);
				Frame ans = analogySolver.returnAnswerToProblem(p);
				if(ans!=null)
					System.out.println("The answer choice is: "+ ans.getFrameName());
				else
					System.out.println("Cannot find answer");
				answersToProblems.add(ans);
				System.out.println("-----------------------------------------");
			}
			int i = 1;
			for(Frame ans: answersToProblems)
			{
				if(ans!=null)
					System.out.println("The answer choice for Problem("+i+") is: "+ ans.getFrameName());
				else
					System.out.println("Cannot find answer");
				++i;
			}
		}
		catch (IOException e) 
		{
			if(e.getClass().getSimpleName().equalsIgnoreCase(" FileNotFoundException"))
			{
				System.out.println("File not found");
			}
			else
			{
				e.printStackTrace();
			}
		}
		System.out.println("**************************************************");
	}
}
