package it.reply.challenge.enumeration;

public enum CellTypeEnum {
	
	MOUNTAIN('#', Integer.MAX_VALUE),
	WATER('~', 800),
	TRAFFIC_JAM('*', 200),
	DIRT('+', 150),
	RAILWAY_CROSS('X', 120),
	STANDARD('_', 100),
	HIGHWAY('H', 70),
	RAILWAY('T', 50);
	
	private Character character;
	private int cost;
	
	private CellTypeEnum(Character character, int cost) {
		this.character = character;
		this.cost = cost;
	}

	public Character getCharacter() {
		return character;
	}

	public int getCost() {
		return cost;
	}
	
	public static CellTypeEnum decode(char c) {
		for(CellTypeEnum en : CellTypeEnum.values()) {
			if(en.getCharacter() == c) {
				return en;
			}
		}
		return null;
	}
}
