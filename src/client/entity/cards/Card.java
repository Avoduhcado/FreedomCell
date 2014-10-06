package client.entity.cards;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import client.entity.Entity;

public class Card extends Entity {

	private Suit suit;
	private int number;
	private Rectangle2D visibleBox;
	private boolean owned = false;
	
	public Card(int number, Suit suit) {
		super("cardsheet", 0, 0);
		this.number = number;
		this.suit = suit;
		buildCard();
	}
	
	public Card(String data) {
		super("cardsheet", 0, 0);
		
		String[] temp = data.split(",");
		this.number = Integer.parseInt(temp[0]);
		this.suit = Suit.valueOf(temp[1]);
		this.id = data;
		buildCard();
	}
	
	public void update() {
		sprite.animate();
		move();
	}
	
	public void buildCard() {
		this.sprite.setDir(suit.getRow());
		this.sprite.setFrame(number - 1);
		this.sprite.setScale(0.5f);
		visibleBox = new Rectangle2D.Double(this.getBox().getX(), this.getBox().getY(), this.getBox().getWidth(), this.getBox().getHeight());
	}
	
	public boolean canBePlaced(Card card) {
		if(card == null)
			return true;
		if(this.isOppositeSuit(card) && card.getNumber() == this.getNumber() + 1)
			return true;
		
		return false;
	}
	
	public boolean canBePlacedOnFoundation(Card card) {
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
	
	public boolean isOppositeSuit(Card card) {
		if((this.isBlackSuit() && !card.isBlackSuit()) || (!this.isBlackSuit() && card.isBlackSuit()))
			return true;
		
		return false;
	}
	
	public boolean matches(Card card) {
		if(this.getSuit() == card.getSuit() && this.getNumber() == card.getNumber())
			return true;
		
		return false;
	}
	
	public int getNumber() {
		return number;
	}
	
	public Rectangle2D getVisibleBox() {
		return visibleBox;
	}
	
	public void setVisibleBox(Rectangle2D visibleBox) {
		if(sprite.getRotation() == 0) {
			this.visibleBox = visibleBox;
		} else {
			AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(sprite.getRotation()),  
					visibleBox.getX(), visibleBox.getY());
			this.visibleBox = at.createTransformedShape(visibleBox).getBounds2D();
		}
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
	
	public boolean isOwned() {
		return owned;
	}
	
	public void setOwned(boolean owned) {
		this.owned = owned;
	}
	
}
