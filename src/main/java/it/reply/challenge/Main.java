package it.reply.challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.reply.challenge.enumeration.CellTypeEnum;
import it.reply.challenge.model.CustomerHeadquarter;
import it.reply.challenge.model.ReplyOffice;
import it.reply.challenge.model.TerrainMap;
//import it.reply.challenge.solver.AndreaSolver;
import it.reply.challenge.solver.GiovaSolver;
import it.reply.challenge.utils.FileUtils;

public class Main {

	// Constants
	private static final String DATASET_0 = "0_example.txt";
	private static final String DATASET_1 = "0_example2.txt";
	private static final String DATASET_A = "1_victoria_lake.txt";
	private static final String DATASET_B = "2_himalayas.txt";
	private static final String DATASET_C = "3_budapest.txt";
	private static final String DATASET_D = "4_manhattan.txt";
	private static final String DATASET_E = "5_oceania.txt";

	// Data
	// mappa del mondo con caratteristiche del terreno, e posizione dei clienti
	private static List<CustomerHeadquarter> modelCustomers = new ArrayList<>();
	private static TerrainMap modelTerrain;

	// Ufficio costruito sulla mappa, e connessione con i clienti
	private static List<ReplyOffice> solution;

	public static void main(String[] args) {

		// Reader
		String dataset = DATASET_E;
		List<String> file = FileUtils.readFile("in/" + dataset);
		
		
		// Parser
		String[] firstLine = file.get(0).split(" ");
		int mapWidth = Integer.parseInt(firstLine[0]);
		int mapHeight = Integer.parseInt(firstLine[1]);
		int numCustomerHeadquarters = Integer.parseInt(firstLine[2]);
		int maxReplyOffices = Integer.parseInt(firstLine[3]);
		int i;
		for(i=1; (i - 1) < numCustomerHeadquarters; i++) {
			String[] line = file.get(i).split(" ");
			// three integers describing the X coordinate, the Y coordinate, and the reward
			//associated with Customer Headquarter.
			CustomerHeadquarter e = new CustomerHeadquarter();
			e.x = Integer.parseInt(line[0]);
			e.y = Integer.parseInt(line[1]);
			e.reward = Integer.parseInt(line[2]);
			modelCustomers.add(e);
			
		}
		modelTerrain = new TerrainMap(mapHeight, mapWidth);
		for(int j = 0; j < mapHeight; j++) {
			char[] line = file.get(j + i).toCharArray();
			for(int k = 0; k < mapWidth; k++) {
				//a row of N terrain cells.
				// The top-left cell is the origin of the map and is thus assigned “(0, 0)” coordinates.
				// In the world map columns are represented by the X coordinate, while rows by the Y coordinate.
				modelTerrain.setCost(j, k, CellTypeEnum.decode(line[k]).getCost());
			}
		}
		// Mappa di appoggio con i customer
		Map<Integer,Set<Integer>> mapCustomers = new HashMap<>();
		for(CustomerHeadquarter c: modelCustomers) {
			if(!mapCustomers.containsKey(c.x)) {
				mapCustomers.put(c.x, new HashSet<>());
			}
			mapCustomers.get(c.x).add(c.y);
		}

		
		// Solver
		solution = new ArrayList<>(maxReplyOffices);
		int score = GiovaSolver.solve(Collections.unmodifiableList(modelCustomers), modelTerrain, maxReplyOffices, solution, mapCustomers);
//		int score = AndreaSolver.solve(modelCustomers, modelTerrain, solution, maxReplyOffices);
		
	
		// Validator
		if(solution.size() == 0) {
			System.err.println("Empty solution.");
			System.exit(1);
		}
		if(solution.size() > maxReplyOffices) {
			System.err.println("You can build a number of Reply Offices less or equal the value given in the input file.");
			System.exit(1);
		}
		// Mappa di appoggio con le sedi
		Map<Integer,Set<Integer>> mapReplyes = new HashMap<>();
		for(ReplyOffice ro: solution) {
			if(!mapReplyes.containsKey(ro.x)) {
				mapReplyes.put(ro.x, new HashSet<>());
			}
			mapReplyes.get(ro.x).add(ro.y);
		}
		for(ReplyOffice ro : solution) {
			if(ro.path.isEmpty()) {
				System.err.println("For each Office you build there must be a path to at least one Customer Headquarter.");
				System.exit(1);
			}
			if(mapCustomers.containsKey(ro.x) &&  mapCustomers.get(ro.x).contains(ro.y)) {
				System.err.println("Reply Offices cannot be over a Customer Headquater");
			}
			// Validità path
			for(String path: ro.path) {
				int deltax = 0;
				int deltay = 0;
				for (int idc = 0; idc < path.toCharArray().length ; idc++) {
					char c = path.charAt(idc);
					switch (c) {
					case 'U':
						deltay--;
						break;
					case 'D':
						deltay++;
						break;
					case 'L':
						deltax--;
						break;
					case 'R':
						deltax++;
						break;
					default:
						System.err.println("The sequence of steps is a string composed by the ASCII characters U, R, D, L (UP, RIGHT, DOWN, LEFT).");
						System.exit(1);
					}
					if(modelTerrain.getCost(ro.y + deltay, ro.x + deltax) == CellTypeEnum.MOUNTAIN.getCost()) {
						System.err.println("Mountains are Non-walkable cell");
					}
					if(idc != path.toCharArray().length - 1) {
						if(mapCustomers.containsKey(ro.x + deltax) && mapCustomers.get(ro.x + deltax).contains(ro.y + deltay)) {
							System.err.println("Path passa su un Customer Headquater");
						}
						if(mapReplyes.containsKey(ro.x + deltax) && mapReplyes.get(ro.x + deltax).contains(ro.y + deltay)) {
							System.err.println("Path passa su una sede Reply");
						}
					}
				}
				if(!mapCustomers.containsKey(ro.x + deltax)
						|| !mapCustomers.get(ro.x + deltax).contains(ro.y + deltay)) {
					System.err.println("Path does not lead to a Customer Headquater");
				}
			}
		}
		
		// Serializer
		StringBuilder solutionText = new StringBuilder();
		for(ReplyOffice s : solution) {
			for(String path : s.path) {
				solutionText.append(s.x);
				solutionText.append(" ");
				solutionText.append(s.y);
				solutionText.append(" ");
				solutionText.append(path);
				solutionText.append("\n");
			}
		}
		
		// Writer
		FileUtils.writeFile(dataset, solutionText.substring(0, solutionText.length() - 1), score);
	}
}
