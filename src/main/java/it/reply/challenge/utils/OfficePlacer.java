package it.reply.challenge.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import it.reply.challenge.model.CustomerHeadquarter;
import it.reply.challenge.model.ReplyOffice;
import it.reply.challenge.model.TerrainMap;

public class OfficePlacer {
	
	public static List<ReplyOffice> placeOfficesRandom(TerrainMap map, List<CustomerHeadquarter> customerList, int numOffices) {
		List<ReplyOffice> officeList = new ArrayList<ReplyOffice>();
		int mountainCount = countMountains(map);
		if (numOffices >= (map.rows * map.cols) - mountainCount) {
			return officeList;
		}
		int i = 0;
		while (i < numOffices) {
			boolean isCoordinateOk = false;
			while (!isCoordinateOk) {
				int x = ThreadLocalRandom.current().nextInt(0, map.cols);
				int y = ThreadLocalRandom.current().nextInt(0, map.rows);
				ReplyOffice office = new ReplyOffice();
				office.x = x;
				office.y = y;
				if (map.getCost(y, x) == Integer.MAX_VALUE || isOfficeInCustomer(office, customerList)) {
					continue; // Se è una montagna o c'è già un customer riprova
				}
				isCoordinateOk = true;
				officeList.add(office);
				i++;
			}
		}
		return officeList;
	}
	
	public static boolean isOfficeInCustomer(ReplyOffice office, List<CustomerHeadquarter> customerList) {
		for (CustomerHeadquarter customer : customerList) {
			if (office.x == customer.x && office.y == customer.y) {
				return true;
			}
		}
		return false;
	}
	
	private static int countMountains(TerrainMap map) {
		int mountainCount = 0;
		for (int i = 0; i < map.rows; i++) {
			for (int j = 0; j < map.cols; j++) {
				if (map.getCost(i, j) == Integer.MAX_VALUE) {
					mountainCount++;
				}
			}
		}
		return mountainCount;
	}
}
