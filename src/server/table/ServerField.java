package server.table;

import server.cards.CardSlot;
import server.cards.ServerCard;
import server.cards.ServerDeck;

public class ServerField {

	private ServerDeck deck;
	private CardSlot[] cells = new CardSlot[4];
	private CardSlot[] foundations = new CardSlot[16];
	private ServerCascade[] cascades = new ServerCascade[32];
	
	public ServerField() {
		deck = new ServerDeck(true);
		deck.shuffle();
		
		deal();
		
		for(int x = 0; x<cells.length; x++) {
			cells[x] = new CardSlot(null);
		}
		for(int x = 0; x<foundations.length; x++) {
			foundations[x] = new CardSlot(null);
		}
	}
	
	public void update() {
		
	}
	
	public void deal() {
		for(int a = 0; a<4; a++) {
			for(int b = 0; b<8; b++) {
				cascades[b + (a * 8)] = new ServerCascade(b);
			}
		}
		
		int j = 0;
		for(int x = 0; x<4; x++) {
			int i = x * 8;
			do {
				cascades[i].addCard(deck.getCards()[j]);
				j++;
				i++;
				if(i % 8 == 0)
					i = x * 8;
			} while(cascades[3 + (x * 8)].getCards().size() < 7);
		}
	}
	
	/*public void printCards() {
	for(int x = 0; x<cells.length; x++) {
		if(cells[x] == null)
			System.out.print("[ ]");
		else
			System.out.print("[" + cells[x].parseCard() + "]");
	}
	
	System.out.print("  ");
	
	for(int x = 0; x<foundations.length; x++) {
		if(foundations[x] == null)
			System.out.print("[ ]");
		else
			System.out.print("[" + foundations[x].parseCard() + "]");
	}
	
	System.out.println();
			
	for(int x = 0; x<cascades[getLongestCascade()].getCards().size(); x++) {
		System.out.print(" ");
		for(int y = 0; y<cascades.length; y++) {
			if(cascades[y].getCards().size() <= x)
				System.out.print("[ ]");
			else {
				System.out.print("[" + cascades[y].getCard(x).parseCard() + "]");
			}
		}
		System.out.println();
	}
}*/
	
	public int getLongestCascade() {
		int longest = 0;
		for(int x = 1; x<cascades.length; x++) {
			if(cascades[x].getCards().size() > cascades[longest].getCards().size())
				longest = x;
		}

		return longest;
	}
	
	public ServerCascade getCascade(String id) {
		for(ServerCascade cascade : cascades) {
			if(cascade.getTopCard() != null && cascade.getTopCard().getID().matches(id))
				return cascade;
		}
		
		return null;
	}
	
	public void placeCardCell(String cardID, int cell) {
		cells[cell].setCard(getCard(cardID));
		
		if(getCascade(cardID) != null) {
			getCascade(cardID).removeTopCard();
		} else {
			for(int i = 0; i<cells.length; i++) {
				if(cells[i].getCard() != null && cells[i].getCard().getID().matches(cardID) && i != cell) {
					cells[i].setCard(null);
					break;
				}
			}
		}
	}
	
	public void placeCardFoundation(String cardID, int foundation) {
		foundations[foundation].setCard(getCard(cardID));
		
		if(getCascade(cardID) != null) {
			getCascade(cardID).removeTopCard();
		} else {
			for(int i = 0; i<cells.length; i++) {
				if(cells[i].getCard() != null && cells[i].getCard().getID().matches(cardID)) {
					cells[i].setCard(null);
					break;
				}
			}
		}
	}
	
	public void placeCard(String cardID, int cascade) {
		if(getCascade(cardID) != null)
			getCascade(cardID).removeTopCard();
		else {
			for(int i = 0; i<cells.length; i++) {
				if(cells[i].getCard() != null && cells[i].getCard().getID().matches(cardID)) {
					cells[i].setCard(null);
					break;
				}
			}
		}
		
		cascades[cascade].addCard(getCard(cardID));
	}
	
	public ServerCard getCard(String id) {
		for(int x = 0; x<deck.getCards().length; x++) {
			if(deck.getCards()[x].getID().matches(id))
				return deck.getCards()[x];
		}
		
		return null;
	}
	
	public ServerCard[] getCards() {
		return deck.getCards();
	}
	
	public String toString() {
		String field = "Field;";
		for(int x = 0; x<cells.length; x++) {
			if(cells[x].getCard() != null)
				field += cells[x].getCard().getID() + "-";
			else
				field += "NULL-";
		}
		for(int x = 0; x<foundations.length; x++) {
			if(foundations[x].getCard() != null)
				field += foundations[x].getCard().getID() + "-";
			else
				field += "NULL-";
		}
		for(int x = 0; x<cascades.length; x++) {
			for(int y = 0; y<cascades[x].getCards().size(); y++) {
				if(cascades[x].getCard(y) != null)
					field += cascades[x].getCard(y).getID() + "-";
				else
					field += "NULL-";
			}
			field += "NEW-";
		}
		
		return field;
	}
	
}
