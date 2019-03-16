package it.reply.challenge.model;

import it.reply.challenge.enumeration.CellTypeEnum;

public class TerrainMap {
	
	public final int rows;
	public final int cols;
	private int[][] map;
	
	public TerrainMap(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		map = new int[rows][cols];
	}
	
	public void setCost(int row, int col, int cost) {
		map[row][col] = cost;
	}
	
	public int getCost(int row, int col) {
		try {
			return map[row][col];
		} catch (ArrayIndexOutOfBoundsException e) {
			return CellTypeEnum.MOUNTAIN.getCost();
		}
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}
}
