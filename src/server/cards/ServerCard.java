package server.cards;

import client.entity.cards.Suit;

public class ServerCard {

	private static int count = 0;
	
	private Suit suit;
	private int number;
	private String id;
	
	public ServerCard(int number, Suit suit) {
		this.number = number;
		this.suit = suit;
		this.id = number + "," + suit.toString() + "," + (count++);
	}
	
	public boolean canBePlaced(ServerCard card) {
		if(card == null)
			return true;
		if(this.isOppositeSuit(card) && card.getNumber() == this.getNumber() + 1)
			return true;
		
		return false;
	}
	
	public boolean canBePlacedOnFoundation(ServerCard card) {
		if(card == null) {
			if(this.getNumber() == 1)
				return true;
			
			return false;
		}
		if(this.suit == card.getSuit() && card.getNumber() == this.getNumber() - 1)
			return true;
		
		return false;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public boolean isBlackSuit() {
		if(suit == Suit.CLUB || suit == Suit.SPADE)
			return true;
		
		return false;
	}
	
	public boolean isOppositeSuit(ServerCard card) {
		if((this.isBlackSuit() && !card.isBlackSuit()) || (!this.isBlackSuit() && card.isBlackSuit()))
			return true;
		
		return false;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getID() {
		return id;
	}
	
	public String parseCard() {
		switch(number) {
		case(1):
			return "A";
		case(10):
			return "T";
		case(11):
			return "J";
		case(12):
			return "Q";
		case(13):
			return "K";
		default:
			return "" + number;
		}
	}
	
}
