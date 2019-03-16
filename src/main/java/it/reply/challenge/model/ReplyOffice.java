package it.reply.challenge.model;

import java.util.List;

public class ReplyOffice {
	public int x;
	public int y;
	public List<String> path;
	
	public ReplyOffice() {
		
	}
	
	public ReplyOffice(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
}
