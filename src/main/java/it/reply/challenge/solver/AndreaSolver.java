package it.reply.challenge.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.reply.challenge.enumeration.CellTypeEnum;
import it.reply.challenge.model.CustomerHeadquarter;
import it.reply.challenge.model.ReplyOffice;
import it.reply.challenge.model.TerrainMap;
import it.reply.challenge.utils.OfficePlacer;

public class AndreaSolver {
	
	private static final String UP = "U";
	private static final String DOWN = "D";
	private static final String RIGHT = "R";
	private static final String LEFT = "L";
	
	private AndreaSolver() {
	}

	/**
	 * Soluzione 1: piazzo tutti gli uffici Reply attaccati ai primi Customer headquarter che trovo
	 * 
	 * @param modelCustomers
	 * @param modelTerrain 
	 * @param solution Soluzione
	 * @param maxReplyOffices 
	 * @return score della soluzione
	 */
	public static int solve(List<CustomerHeadquarter> modelCustomers, TerrainMap modelTerrain, List<ReplyOffice> solution, int maxReplyOffices) {
		// Considera per primi i clienti migliori
        Collections.sort(modelCustomers, new Comparator<CustomerHeadquarter>() {
            public int compare(CustomerHeadquarter obj1, CustomerHeadquarter obj2) {
            	int costTerrain1 = modelTerrain.getCost(obj1.y, obj1.x);
            	int costTerrain2 = modelTerrain.getCost(obj2.y, obj2.x);
                return (obj2.reward-costTerrain2) - (obj1.reward-costTerrain1);
            }
        });
		
        int i;
		for (i = 0; i < modelCustomers.size() && solution.size() < maxReplyOffices; i++) {
			CustomerHeadquarter c = modelCustomers.get(i);
			ReplyOffice office = new ReplyOffice();
			office.path = new ArrayList<>();
			// provo a piazzare il Reply office sopra il Customer
			if (CellTypeEnum.MOUNTAIN.getCost() != modelTerrain.getCost(c.y-1, c.x) && !OfficePlacer.isOfficeInCustomer(new ReplyOffice(c.x, c.y-1), modelCustomers)) {
				office.y = c.y-1;
				office.x = c.x;
				office.path.add(DOWN);
			}
			// provo a piazzare il Reply office a sinistra del Customer
			else if (CellTypeEnum.MOUNTAIN.getCost() != modelTerrain.getCost(c.y, c.x-1) && !OfficePlacer.isOfficeInCustomer(new ReplyOffice(c.x-1, c.y), modelCustomers)) {
				office.y = c.y;
				office.x = c.x-1;
				office.path.add(RIGHT);
			}
			// provo a piazzare il Reply office a destra del Customer
			else if (CellTypeEnum.MOUNTAIN.getCost() != modelTerrain.getCost(c.y, c.x+1) && !OfficePlacer.isOfficeInCustomer(new ReplyOffice(c.x+1, c.y), modelCustomers)) {
				office.y = c.y;
				office.x = c.x+1;
				office.path.add(LEFT);
			}
			// provo a piazzare il Reply office sotto il Customer
			else if (CellTypeEnum.MOUNTAIN.getCost() != modelTerrain.getCost(c.y+1, c.x) && !OfficePlacer.isOfficeInCustomer(new ReplyOffice(c.x, c.y+1), modelCustomers)) {
				office.y = c.y+1;
				office.x = c.x;
				office.path.add(UP);
			}
			solution.add(office);
		}
		
//		for (int j = i; j < modelCustomers.size(); j++) {
//			
//		}

		return 0;
	}
}