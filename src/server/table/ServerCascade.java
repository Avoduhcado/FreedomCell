package server.table;

import java.util.ArrayList;

import server.cards.CardSlot;
import server.cards.ServerCard;

public class ServerCascade {

	private ArrayList<CardSlot> cards = new ArrayList<CardSlot>();
	@SuppressWarnings("unused")
	private int index;
	
	public ServerCascade(int index) {
		this.index = index;
	}
	
	public ArrayList<CardSlot> getCards() {
		return cards;
	}
	
	public ServerCard getCard(int x) {
		return cards.get(x).getCard();
	}
	
	public CardSlot getTopSlot() {
		return cards.get(cards.size() - 1);
	}
	
	public ServerCard getTopCard() {
		if(cards.isEmpty())
			return null;
		
		return cards.get(cards.size() - 1).getCard();
	}
	
	public void addCard(ServerCard card) {
		cards.add(new CardSlot(card));
	}
	
	public void removeTopCard() {
		cards.remove(cards.size() - 1);
		if(cards.isEmpty())
			cards.add(new CardSlot(null));
	}
	
	public void removeCard(ServerCard card) {
		cards.remove(card);
	}
	
}
