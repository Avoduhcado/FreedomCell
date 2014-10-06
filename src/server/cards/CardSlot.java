package server.cards;

public class CardSlot {

	private ServerCard card;
	
	public CardSlot(ServerCard card) {
		this.card = card;
	}
	
	public boolean isEmpty() {
		if(card == null)
			return true;
		
		return false;
	}
	
	public ServerCard getCard() {
		return card;
	}
	
	public void setCard(ServerCard card) {
		this.card = card;
	}
	
}
