package it.reply.challenge.utils;

import java.util.ArrayList;
import java.util.List;

import it.reply.challenge.model.CustomerHeadquarter;
import it.reply.challenge.model.PathScore;
import it.reply.challenge.model.ReplyOffice;
import it.reply.challenge.model.TerrainMap;

public class ScoreCalculator {
	
	public static int calculate(TerrainMap map, List<ReplyOffice> officeList, List<CustomerHeadquarter> customerList) {
		int costSum = 0;
		List<PathScore> scoreList = new ArrayList<>();
		for (ReplyOffice office : officeList) {
			List<PathScore> list = calculateSingleOffice(map, office, customerList);
			scoreList.addAll(list);
			for(PathScore p : list) {
				costSum += p.getCost();
			}
		}
		
		boolean allCustomersVisited = true;
		for (CustomerHeadquarter customer : customerList) {
			boolean found = false;
			for (PathScore score : scoreList) {
				if (score.getCustomer().equals(customer)) {
					found = true;
					break;
				}
			}
			if (!found) {
				allCustomersVisited = false;
				break;
			}
		}
		
		if (allCustomersVisited) {
			for (CustomerHeadquarter customer : customerList) {
				costSum += customer.reward;
			}
		}
		
		return costSum;
	}
	
	private static List<PathScore> calculateSingleOffice(TerrainMap map, ReplyOffice office, List<CustomerHeadquarter> customerList) {
		List<PathScore> list = new ArrayList<>();
		for (String direction : office.path) {
			int x = office.x;
			int y = office.y;
			int pathCost = 0;
			PathScore score = new PathScore();
			for(char c : direction.toCharArray()) {
				if ('R' == c) {
					x += 1;
				}
				else if ('D' == c) {
					y += 1;
				}
				else if ('L' == c) {
					x -= 1;
				}
				else if ('U' == c) {
					y -= 1;
				}
				pathCost += map.getCost(y, x);
			}
			for (CustomerHeadquarter customer : customerList) {
				if (customer.x == x && customer.y == y) {
					score.setCost(customer.reward - pathCost);
					score.setCustomer(customer);
					break;
				}
			}
			if(score.getCustomer() == null) {
				System.err.println("Customer non trovato, soluzione non valida");
				System.exit(1);
			}
			list.add(score);
		}
		return list;
	}
}
