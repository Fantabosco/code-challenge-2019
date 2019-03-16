package it.reply.challenge.model;

import java.util.List;

import it.reply.challenge.solver.ShortestPath;

@SuppressWarnings("unchecked")
/**
 * Directed Graph Test - Dijkstra Shortest Path
 * 
 * @author /u/Philboyd_Studge
 */
public class DiGraphTest
{
	public static void main(String[] args)
	{

		// create graph
		DiGraph graph = new DiGraph();

		// add a bunch of edges
		graph.add("SACRAMENTO", "PHOENIX", 150);
		graph.add("PHOENIX", "SACRAMENTO", 135);
		graph.add("PHOENIX", "SLC", 120);
		graph.add("SLC", "SACRAMENTO", 175);
		graph.add("SACRAMENTO", "PHOENIX", 160); // edge already exists!!!
		graph.add("SACRAMENTO", "PORTLAND", 90);
		graph.add("PORTLAND", "SLC", 185);
		graph.add("OAKLAND", "SACRAMENTO", 45);
		graph.add("PORTLAND", "OAKLAND", 100);
		graph.add("SLC", "OAKLAND", 150);
		graph.add("LAX","OAKLAND", 75);
		graph.add("SLC", "LAS VEGAS", 100);
		graph.add("LAS VEGAS", "CHICAGO", 250);

		System.out.println("Graph is connected: " + graph.DepthFirstSearch());
		System.out.println("Connected from LAX:" + graph.BreadthFirstSearch("LAX"));
		System.out.println();

		System.out.println(graph);
		System.out.println(graph.edgesToString());

		System.out.println("LAX to PORTLAND");
		List<String> path = graph.getPath("LAX", "PORTLAND");
		for (String each : path) 
			System.out.println(each);

		System.out.println("\nSLC to PHOENIX");
		path = graph.getPath("SLC", "PHOENIX");
		for (String each : path) 
			System.out.println(each);

		System.out.println("Adding Edge Las Vegas to Phoenix at cost $120");
		graph.add("LAS VEGAS", "PHOENIX", 120);

		System.out.println("\nSLC to PHOENIX");
		path = graph.getPath("SLC", "PHOENIX");
		for (String each : path) 
			System.out.println(each);

		System.out.println("\nSACRAMENTO to LAX");
		path = graph.getPath("SACRAMENTO", "LAX");
		for (String each : path) 
			System.out.println(each);

		System.out.println("\nSACRAMENTO to CHICAGO");
		path = graph.getPath("SACRAMENTO", "CHICAGO");
		for (String each : path) 
			System.out.println(each);
	}
	
	public static int solve(List<CustomerHeadquarter> model, TerrainMap modelTerrain, List<ReplyOffice> solution, int sorgente) {
		//num riga * lungh riga + colonna
		ShortestPath t = new ShortestPath(); 
		int rows = modelTerrain.getMap().length;
		int cols = modelTerrain.getMap()[0].length;
		DiGraph graph = new DiGraph();
		int[][] matrix = modelTerrain.getMap();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				int nodo = (i * matrix.length) + j;
				if (i != 0) { // non prima riga, se lo fosse salto le celle sopra
					if (modelTerrain.getCost(i - 1, j) > 5000) {
						graph.add(nodo, ((i - 1) * matrix.length) + j, modelTerrain.getCost(i - 1, j));
					}
				}
				if (j != 0) { // non prima colonna, se lo fosse salto le celle a sinistra
					if (modelTerrain.getCost(i, j - 1) > 5000) {
						graph.add(nodo, (i * matrix.length) + (j - 1), modelTerrain.getCost(i, j - 1));
					}
				}
				if (i < (matrix.length - 1)) { // non ultima riga, se lo fosse salto le celle sotto
					if (modelTerrain.getCost(i + 1, j) > 5000) {
						graph.add(nodo, ((i + 1) * matrix.length) + j, modelTerrain.getCost(i + 1, j));

					}
				}
				if (j < (matrix[i].length - 1)) { // non ultima colonna, se lo fosse salto le celle a destra
					if (modelTerrain.getCost(i, j + 1) > 5000) {
						graph.add(nodo, (i * matrix.length) + (j + 1), modelTerrain.getCost(i, j + 1));
					}

				}
			}
		}
		System.out.println("Graph is connected: " + graph.DepthFirstSearch());
		List<String> path = graph.getPath(82, 84);
		for (String each : path) {
			System.out.println(each);			
		}
		return 0;
	}
}