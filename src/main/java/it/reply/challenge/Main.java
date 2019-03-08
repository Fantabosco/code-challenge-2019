package it.reply.challenge;

import java.util.ArrayList;
import java.util.List;

import it.reply.challenge.solver.TemplateSolver;
import it.reply.challenge.utils.FileUtils;

public class Main {

	// Constants
	private static final String DATASET_A = "A.txt";
	private static final String DATASET_B = "B";
	private static final String DATASET_C = "C";
	private static final String DATASET_D = "D";
	private static final String DATASET_E = "E";

	// Data
	//TODO
	private static List<Object> model;
	private static List<Object> solution;

	public static void main(String[] args) {

		// Reader
		String dataset = DATASET_A;
		List<String> file = FileUtils.readFile("in/" + dataset);
		
		
		// Parser
		//TODO
		
		
		// Solver
		solution = new ArrayList<>();
		//TODO
		int score = TemplateSolver.solve(model, solution);
		
	
		// Serializer & validator
		//TODO
		StringBuilder solutionText = new StringBuilder();
		solutionText.append(solution.size());
		solutionText.append("\n");
		for(Object s : solution) {
			solutionText.append(s);
			solutionText.append("\n");
		}
		
		
		// Writer
		FileUtils.writeFile(dataset, solutionText.substring(0, solutionText.length() - 1), score);
	}
}
