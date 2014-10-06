package client.table;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import client.entity.cards.Card;

public class Cascade {

	private ArrayList<Card> cards = new ArrayList<Card>();
	private Rectangle2D position;
	private int orientation;
	
	public Cascade(int column, int orientation) {
		if(orientation < 0)
			orientation += 4;
		switch(orientation) {
		case(0):
			this.orientation = 0;
			position = new Rectangle2D.Float(196 + (column * 71), 420, 71, 96);
			break;
		case(1):
			this.orientation = 90;
			position = new Rectangle2D.Float(832.6f, 373.2f - (column * 42.6f), 57.6f, 42.6f);
			break;
		case(2):
			this.orientation = 180;
			position = new Rectangle2D.Float(650.4f - (column * 42.6f), 182.6f, 42.6f, 57.6f);
			break;
		case(3):
			this.orientation = 270;
			position = new Rectangle2D.Float(125, 117.6f + (column * 42.6f), 57.6f, 42.6f);
			break;
		}
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public Card getCard(int x) {
		return cards.get(x);
	}
	
	public Card getTopCard() {
		if(cards.isEmpty())
			return null;
		
		return cards.get(cards.size() - 1);
	}
	
	public Rectangle2D getTopBox() {
		if(cards.isEmpty())
			return position;
		
		return cards.get(cards.size() - 1).getBox();
	}
	
	public void alignCard() {
		getTopCard().getSprite().setRotation(orientation);
		if(orientation == 0)
			getTopCard().getSprite().setScale(0.5f);
		else
			getTopCard().getSprite().setScale(0.3f);
		
		if(cards.size() == 1) {
			getTopCard().setX((float) position.getX());
			getTopCard().setY((float) position.getY());
			getTopCard().setVisibleBox(getTopBox());
		} else {
			switch(orientation) {
			case(0):
				getTopCard().setPosition(cards.get(0).getX(),
						(float) (cards.get(0).getY() + (position.getHeight() / getCompression() * (cards.size() - 1))));
				break;
			case(90):
				getTopCard().setPosition((float) (cards.get(0).getX() + (position.getWidth() / getCompression() * (cards.size() - 1))),
						cards.get(0).getY());
				break;
			case(180):
				getTopCard().setPosition(cards.get(0).getX(),
						(float) (cards.get(0).getY() - (position.getHeight() / getCompression() * (cards.size() - 1))));
				break;
			case(270):
				getTopCard().setPosition((float) (cards.get(0).getX() - (position.getWidth() / getCompression() * (cards.size() - 1))),
						cards.get(0).getY());
				break;
			}
			getTopCard().setVisibleBox(getTopBox());
		}
	}
	
	public void alignCard(int card) {
		cards.get(card).getSprite().setRotation(orientation);
		if(orientation == 0)
			cards.get(card).getSprite().setScale(0.5f);
		else
			cards.get(card).getSprite().setScale(0.3f);
		
		switch(orientation) {
		case(0):
			cards.get(card).setPosition(cards.get(0).getX(),
					(float) (cards.get(0).getY() + (position.getHeight() / getCompression() * card)));
			break;
		case(90):
			cards.get(card).setPosition((float) (cards.get(0).getX() + (position.getWidth() / getCompression() * card)),
					cards.get(0).getY());
			break;
		case(180):
			cards.get(card).setPosition(cards.get(0).getX(),
					(float) (cards.get(0).getY() - (position.getHeight() / getCompression() * card)));
			break;
		case(270):
			cards.get(card).setPosition((float) (cards.get(0).getX() - (position.getWidth() / getCompression() * card)),
					cards.get(0).getY());
			break;
		}
		
		cards.get(card).setVisibleBox(cards.get(card).getBox());
	}
	
	public float getCompression() {
		float compression = 4;
		if(cards.size() > 8)
			compression += (cards.size() % 8) / 2;
		
		return compression;
	}
	
	public void compress() {
		if(cards.size() > 8) {
			for(int x = 0; x<cards.size(); x++) {
				alignCard(x);
			}
		}
	}
	
	public void addCard(Card card) {
		cards.add(card);
		/*if(cards.size() > 8) {
			for(int x = 0; x<cards.size(); x++) {
				alignCard(x);
			}
		}*/
	}
	
	public void removeCard(Card card) {
		cards.remove(card);
		if(cards.size() >= 8) {
			for(int x = 0; x<cards.size(); x++) {
				alignCard(x);
			}
		}
	}
	
	public int getOrientation() {
		return orientation;
	}
	
}
