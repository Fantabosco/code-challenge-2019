package it.reply.challenge.solver;

import java.util.List;

import it.reply.challenge.model.CustomerHeadquarter;
import it.reply.challenge.model.ReplyOffice;

public class SergiuSolver extends TemplateSolver {
	
	/**
	 * @param model Dati di input
	 * @param solution Soluzione
	 * @return score della soluzione
	 */
	public static int solve(List<CustomerHeadquarter> model, List<ReplyOffice> solution) {
		
		return 0;
	}
	
	/*
	 * A utility function that returns minimum of 3 integers
	 */
	static int min(int x, int y, int z) {
		if (x < y)
			return (x < z) ? x : z;
		else
			return (y < z) ? y : z;
	}
	
	public static int min(int a, int b, int c, int d) {

	    int min = a;

	    if (b < min)
	    	min = b;
	    if (c < min)
	    	min = c;
	    if (d < min)
	    	min = d;

	     return min;
	}

	/*
	 * Returns cost of minimum cost path from (0,0) to (m, n) in mat[R][C]
	 */
	static int minCost(int cost[][], int m, int n) {
		if (n < 0 || m < 0)
			return Integer.MAX_VALUE;
		else if (m == 0 && n == 0)
			return cost[m][n];
		else
			return cost[m][n] 
					+ min(minCost(cost, m - 1, n - 1)
							, minCost(cost, m - 1, n)
							, minCost(cost, m, n - 1));
	}

	// Driver code
	public static void main(String args[]) {

		int cost[][] = { { 1, 2, 3 }, { 4, 8, 2 }, { 1, 5, 3 } };

		System.out.print(minCost(cost, 2, 2));
	}
	
	
}
