package client.table;

import java.util.LinkedList;
import java.util.List;

import client.entity.cards.Card;
import client.entity.cards.Suit;

public class Deck {

	private Card[] cards = new Card[52];
	
	public Deck() {
		for(int x = 0; x<4; x++) {
			for(int y = 0; y<cards.length / 4; y++) {
				cards[y + (x * (cards.length / 4))] = new Card(y + 1, Suit.parseSuit(x));
			}
		}
	}
	
	public Deck(Object[] cards) {
		this.cards = new Card[cards.length];
		int x = 0;
		for(Object card : cards) {
			this.cards[x] = (Card) card;
			x++;
		}
	}
	
	public Deck(int size) {
		cards = new Card[52 * size];
		int i = 0;
		do {
			for(int x = 0; x<4; x++) {
				for(int y = 0; y<cards.length / 4; y++) {
					cards[y + (x * (cards.length / 4))] = new Card(y + 1, Suit.parseSuit(x));
				}
			}
			i++;
		} while(i < size);
	}
	
	public void draw() {
		for(int x = 0; x<cards.length; x++) {
			cards[x].draw();
		}
	}
	
	public void shuffle() {
		Card[] newCards = new Card[52];
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
	
	public Card[] getCards() {
		return cards;
	}
	
	public void setCards(Card[] cards) {
		this.cards = cards;
	}
	
}
