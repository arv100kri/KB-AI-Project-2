package com.miller.utilities;

import java.util.ArrayList;
import java.util.List;

import com.miller.definitions.Frame;
import com.miller.definitions.ProblemFormulation;

public class AnalogySolverHelper 
{
	private static final AnalogySolverHelper s_instance = new AnalogySolverHelper();
	private AnalogySolverHelper()
	{
		//singleton
	}
	public static AnalogySolverHelper getInstance()
	{
		return s_instance;
	}
	
	public Frame returnAnswerToProblem(ProblemFormulation problem)
	{
		Frame answerFrame = new Frame();
		int problemDimension = problem.getDimensions();
		List<Frame> referenceFrames = problem.getMatrices().get(0).getFrames();
		List<Frame> incompleteFrames = new ArrayList<Frame>();
		List<Frame> answerChoices = new ArrayList<Frame>();
		if(problemDimension == 2)
		{
				incompleteFrames.add(problem.getMatrices().get(1).getFrames().get(0));
				int n = problem.getMatrices().get(1).getFrames().size();
				for(int i=1; i<n;i++)
				{
					answerChoices.add(problem.getMatrices().get(1).getFrames().get(i));
				}
		}
		
		else if(problemDimension == 3)
		{
			incompleteFrames.add(problem.getMatrices().get(1).getFrames().get(0));
			incompleteFrames.add(problem.getMatrices().get(1).getFrames().get(1));
			int n = problem.getMatrices().get(1).getFrames().size();
			for(int i=2; i<n;i++)
			{
				answerChoices.add(problem.getMatrices().get(1).getFrames().get(i));
			}
		}
		
		else
		{
			System.out.println("Cannot solve anything other than 2x2 and 3x3");
			return null;
		}
		
		RuleMaker solver = RuleMaker.getInstance();
		for(Frame answerChoice: answerChoices)
		{
			if(solver.isMatching(problemDimension, referenceFrames, incompleteFrames, answerChoice))
			{
				answerFrame = new Frame(answerChoice);
				break;
			}
		}
		
		return answerFrame;
	}
}
