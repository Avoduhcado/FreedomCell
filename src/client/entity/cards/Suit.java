package client.entity.cards;

public enum Suit {

	DIAMOND (0),
	HEART (1),
	CLUB (2),
	SPADE (3);
	
	private int row;
	
	Suit(int row) {
		this.row = row;
	}
	
	public int getRow() {
		return row;
	}
	
	public static Suit parseSuit(int row) {
		switch(row) {
		case(0):
			return DIAMOND;
		case(1):
			return HEART;
		case(2):
			return CLUB;
		case(3):
			return SPADE;
		default:
			return DIAMOND;
		}
	}
	
}
