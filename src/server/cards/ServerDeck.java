package server.cards;

import java.util.LinkedList;
import java.util.List;

import client.entity.cards.Suit;

public class ServerDeck {

	private ServerCard[] cards = new ServerCard[52];
	
	public ServerDeck(boolean multi) {
		if(!multi) {
			for(int x = 0; x<4; x++) {
				for(int y = 0; y<cards.length / 4; y++) {
					cards[y + (x * (cards.length / 4))] = new ServerCard(y + 1, Suit.parseSuit(x));
				}
			}
		} else {
			cards = new ServerCard[52 * 4];
			
			for(int z = 0; z<4; z++) { 
				for(int x = 0; x<4; x++) {
					for(int y = 0; y<cards.length / 16; y++) {
						cards[(y + (x * (cards.length / 16))) + (z * 52)] = new ServerCard(y + 1, Suit.parseSuit(x));
					}
				}
			}
		}
	}
	
	public void shuffle() {
		ServerCard[] newCards = new ServerCard[cards.length];
		int randomCard;
		List<Integer> usedInts = new LinkedList<Integer>();
		int x = 0;
		
		while(x < cards.length) {
			randomCard = (int)(Math.random() * cards.length);
			if(!usedInts.contains(randomCard)) {
				usedInts.add(randomCard);
				newCards[x] = cards[randomCard];
				x++;
			}
		}
		
		cards = newCards;
	}
	
	public ServerCard[] getCards() {
		return cards;
	}
	
}
