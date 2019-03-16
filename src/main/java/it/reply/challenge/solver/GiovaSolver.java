package it.reply.challenge.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.reply.challenge.enumeration.CellTypeEnum;
import it.reply.challenge.model.CustomerHeadquarter;
import it.reply.challenge.model.ReplyOffice;
import it.reply.challenge.model.TerrainMap;
import it.reply.challenge.utils.ScoreCalculator;

public class GiovaSolver {

	private static final int MAX_DISTANCE = 8;

	private GiovaSolver() {
	}

	/**
	 * @param model           Dati di input
	 * @param modelTerrain
	 * @param maxReplyOffices
	 * @param solution        Soluzione
	 * @param mapCustomers2 
	 * @return score della soluzione
	 */
	public static int solve(final List<CustomerHeadquarter> modelCustomers, TerrainMap modelTerrain,
			int maxReplyOffices, List<ReplyOffice> solution, Map<Integer, Set<Integer>> mapCustomers2) {
		List<CustomerHeadquarter> customers = new ArrayList<>();
		customers.addAll(modelCustomers);
		// Considera per primi i clienti migliori
		Collections.sort(customers, new Comparator<CustomerHeadquarter>() {
		    public int compare(CustomerHeadquarter obj1, CustomerHeadquarter obj2) {
		        return (obj2.reward - modelTerrain.getCost(obj2.y, obj2.x)) - (obj1.reward - modelTerrain.getCost(obj1.y, obj1.x));
		    }
		});
		// Mappa di appoggio con i customer
		Map<Integer,Set<Integer>> mapCustomers = new HashMap<>();
		for(CustomerHeadquarter c: modelCustomers) {
			if(!mapCustomers.containsKey(c.x)) {
				mapCustomers.put(c.x, new HashSet<>());
			}
			mapCustomers.get(c.x).add(c.y);
		}
		for (int i = 0; i < maxReplyOffices; i++) {
			ReplyOffice ro = null;
			Iterator<CustomerHeadquarter> it = customers.iterator();
			while (it.hasNext()) {
				boolean removeCustomer = false;
				CustomerHeadquarter c = it.next();
				if (modelTerrain.getCost(c.y, c.x) == CellTypeEnum.MOUNTAIN.getCost()) {
					removeCustomer = true;
				}

				ro = solver1(c, modelTerrain, mapCustomers);

				// Ogni volta che raggiungi un customer, toglilo dalla lista, dato che:
				// If each Customer Headquarter has a path to at least one Reply Office, you
				// will get a bonus score.
				// If all Customer Headquarters have a path to at least one Reply Office, add to
				// the score a bonus value equal to the sum of the rewards for all clients.
				// Rimuovi anche se non valido (su montagna)
				if (removeCustomer) {
					it.remove();
					continue;
				}
				if(ro != null) {
					solution.add(ro);
					it.remove();
					break;
				}
			}
		}
		
		
		System.out.println("Base score:" + ScoreCalculator.calculate(modelTerrain, solution, modelCustomers));
		
		
		// Cerca di migliorare
		for(ReplyOffice ro : solution) {
//			if(true) continue;
			Iterator<CustomerHeadquarter> it = customers.iterator();
			while (it.hasNext()) {
				CustomerHeadquarter c = it.next();
				
				int deltax = c.x - ro.x;
				int deltay = c.y - ro.y;
				
				int newPosx = ro.x;
				int newPosy = ro.y;
				
				int cost = 0;

				if(Math.abs(deltax) < MAX_DISTANCE && Math.abs(deltay) < MAX_DISTANCE) {
					String path = "";
					while(Math.abs(deltax) > 0 || Math.abs(deltay) > 0) {
						if(Math.abs(deltax) > Math.abs(deltay)) {
							if(deltax > 0 && modelTerrain.getCost(newPosy, newPosx + 1) != CellTypeEnum.MOUNTAIN.getCost() // Le sedi Repy non possono stare sule montagne
									&& !((Math.abs(deltax) > 1) && mapCustomers.containsKey(newPosx + 1) &&  mapCustomers.get(newPosx + 1).contains(newPosy))) { // Se non sono all'ultimo step, non andare su Customers
								path += "R";
								deltax--;
								newPosx++;
								cost += modelTerrain.getCost(newPosy, newPosx);
								continue;
							} else if(modelTerrain.getCost(newPosy, newPosx - 1) != CellTypeEnum.MOUNTAIN.getCost()
									&& !((Math.abs(deltax) > 1) && mapCustomers.containsKey(newPosx - 1) &&  mapCustomers.get(newPosx - 1).contains(newPosy))) {
								path += "L";
								deltax++;	
								newPosx--;
								cost += modelTerrain.getCost(newPosy, newPosx);
								continue;
							}
						} else if(modelTerrain.getCost(newPosy + 1, newPosx) != CellTypeEnum.MOUNTAIN.getCost()
								&& !((Math.abs(deltay) > 1) && mapCustomers.containsKey(newPosx) &&  mapCustomers.get(newPosx).contains(newPosy + 1))) {
							if(deltay > 0) {
								path += "D";
								deltay--;
								newPosy++;
								cost += modelTerrain.getCost(newPosy, newPosx);
								continue;
							} else if(modelTerrain.getCost(newPosy - 1, newPosx) != CellTypeEnum.MOUNTAIN.getCost()
									&& !((Math.abs(deltay) > 1) && mapCustomers.containsKey(newPosx) &&  mapCustomers.get(newPosx).contains(newPosy - 1))) {
								path += "U";
								deltay++;
								newPosy--;
								cost += modelTerrain.getCost(newPosy, newPosx);
								continue;
							}
						}
						if(deltax != 0 && deltay != 0) {
							 // Pathfinding base ha fallito
							path = "";
						}
						break;
					}
					// Se ho un path e mi fa guadagnare
					if(!path.isEmpty() && cost < c.reward) {
						ro.path.add(path);
						it.remove();
					}
				}
			}
		}

		/*
		 * The score for a client is computed as the reward of the client minus the cost
		 * to reach it. The cost to reach a client is the sum of the traversal cost of
		 * all the symbols traversed on the path from Reply Office to Customer
		 * Headquarter The total score for an input is computed as the sum of the scores
		 * for each connected client. If the resulting score is negative than the score
		 * is automatically assigned as 0. The higher the score, the better.
		 */
		return ScoreCalculator.calculate(modelTerrain, solution, modelCustomers);
	}

	private static ReplyOffice solver1(CustomerHeadquarter c, TerrainMap modelTerrain, Map<Integer, Set<Integer>> mapCustomers) {
		ReplyOffice ro = null;
		if(modelTerrain.rows > c.x + 1  // Sta nella mappa 
				&& (!mapCustomers.containsKey(c.x + 1) || !mapCustomers.get(c.x + 1).contains(c.y))) { // Non è sopra un altro cliente
			ro = new ReplyOffice();
			ro.x = c.x + 1;
			ro.y = c.y; 
			ro.path = new ArrayList<>();
			ro.path.add("L");
		} else if(0 <= c.x - 1  // Sta nella mappa 
				&& (!mapCustomers.containsKey(c.x - 1) || !mapCustomers.get(c.x - 1).contains(c.y))) { // Non è sopra un altro cliente
			ro = new ReplyOffice();
			ro.x = c.x - 1;
			ro.y = c.y; 
			ro.path = new ArrayList<>();
			ro.path.add("R");
			
		} else if(modelTerrain.cols > c.y + 1  // Sta nella mappa 
				&& (!mapCustomers.containsKey(c.x) || !mapCustomers.get(c.x).contains(c.y + 1))) { // Non è sopra un altro cliente
			ro = new ReplyOffice();
			ro.x = c.x;
			ro.y = c.y + 1; 
			ro.path = new ArrayList<>();
			ro.path.add("U");
			
		} else if(0 <= c.y - 1  // Sta nella mappa 
				&& (!mapCustomers.containsKey(c.x) || !mapCustomers.get(c.x).contains(c.y - 1))) { // Non è sopra un altro cliente
			ro = new ReplyOffice();
			ro.x = c.x;
			ro.y = c.y - 1; 
			ro.path = new ArrayList<>();
			ro.path.add("D");
		} 
		return ro;
	}
}