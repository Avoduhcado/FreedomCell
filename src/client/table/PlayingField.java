package client.table;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import client.Theater;
import client.entity.cards.Card;
import client.entity.cards.Suit;
import client.scores.Actions;
import client.scores.Army;

public class PlayingField {

	private int player = 0;
	private final Rectangle cardSize = new Rectangle(0,0,71,96);
	private Actions apm;
	
	private Deck deck;
	private Card[] cells = new Card[4];
	private Rectangle2D[] cellRects = new Rectangle2D[4];
	private Card[] foundations = new Card[16];
	private Rectangle2D[] foundationRects = new Rectangle2D[16];
	private Cascade[] cascades = new Cascade[32];
	
	public PlayingField() {		
		deck = new Deck();
		deck.shuffle();
		for(int x = 0; x<cellRects.length; x++) {
			cellRects[x] = new Rectangle2D.Double(258 + (x * cardSize.width), 100, cardSize.width, cardSize.height);
		}
		for(int x = 0; x<foundationRects.length; x++) {
			foundationRects[x] = new Rectangle2D.Double(258 + (x * cardSize.width), 4, cardSize.width, cardSize.height);
		}
		
		apm = new Actions();
	}

	public PlayingField(String field) {
		Army.init();
		ArrayList<Card> cards = new ArrayList<Card>();
		String[] temp = field.split("-");
		player = Integer.parseInt(temp[temp.length - 1]);
		
		for(int x = 0; x<cellRects.length; x++) {
			cellRects[x] = new Rectangle2D.Double(338 + (x * cardSize.width), 250, cardSize.width, cardSize.height);
		}

		for(int x = 0; x<foundationRects.length; x++) {
			switch((int) (x / 4) - player < 0 ? ((int) (x / 4) - player) + 4 : (int) (x / 4) - player) {
			case(0):
				foundationRects[x] = new Rectangle2D.Double(551 + ((x % 4) * 42.6f), 356.2f, 42.6f, 58.8f);
				break;
			case(1):
				foundationRects[x] = new Rectangle2D.Double(770, 202.8f - ((x % 4) * 42.6f), 57.6f, 42.6f);
				break;
			case(2):
				foundationRects[x] = new Rectangle2D.Double(480 - ((x % 4) * 42.6f), 245.2f, 42.6f, 58.8f);
				break;
			case(3):
				foundationRects[x] = new Rectangle2D.Double(188.8f, 202.8 + ((x % 4) * 42.6f), 42.6f, 58.8f);
				break;
			}
		}
		
		int i = 0;
		for(int x = 0; x<cells.length; x++) {
			if(!temp[i].matches("NULL")) {
				cells[x] = new Card(temp[i]);
				cells[x].setX((float) cellRects[x].getX());
				cells[x].setY((float) cellRects[x].getY());
				cards.add(cells[x]);
			}
			i++;
		}
		
		for(int x = 0; x<foundations.length; x++) {
			if(!temp[i].matches("NULL")) {
				foundations[x] = new Card(temp[i]);
				foundations[x].setX((float) foundationRects[x].getX());
				foundations[x].setY((float) foundationRects[x].getY());
				foundations[x].getSprite().setScale(0.3f);
				foundations[x].getSprite().setRotation((int) (x / 4) - player < 0 ? 
						(((int) (x / 4) - player) + 4)  * 90 : ((int) (x / 4) - player) * 90);
				cards.add(foundations[x]);
			}
			i++;
		}
		
		for(int x = 0; x<cascades.length; x++) {
			cascades[x] = new Cascade(x % 8, (int) (x / 8) - player);
			while(!temp[i].matches("NEW")) {
				if(!temp[i].matches("NULL")) {
					cascades[x].addCard(new Card(temp[i]));
					cascades[x].alignCard();
					cascades[x].compress();
					cards.add(cascades[x].getTopCard());
					if((int) (x / 8) - player == 0)
						cascades[x].getTopCard().setOwned(true);
				}
				i++;
			}
			i++;
		}
		
		deck = new Deck(cards.toArray());
		
		apm = new Actions();
	}
	
	public void update() {
		apm.update();
		
		for(int x = 0; x<deck.getCards().length; x++) {
			deck.getCards()[x].update();
			if(deck.getCards()[x].isMoving() && (deck.getCards()[x].getDistance() == 0f || deck.getCards()[x].getMoveTimer() == 0f)) {
				snapCard(deck.getCards()[x]);
			}
		}
	}
	
	public void draw() {
		for(int x = 0; x<foundations.length; x++) {
			if(x / 4 == player && foundations[x] == null)
				drawBox(foundationRects[x]);
			if(foundations[x] != null)
				foundations[x].draw();
		}
		
		for(int x = 0; x<cells.length; x++) {
			if(cells[x] == null)
				drawBox(cellRects[x]);
			if(cells[x] != null)
				cells[x].draw();
		}

		for(int x = 0; x<cascades[getLongestCascade()].getCards().size(); x++) {
			for(int y = 0; y<cascades.length; y++) {
				if(cascades[y].getCards().size() <= x) {
					if(y / 8 == player && cascades[y].getCards().isEmpty())
						drawBox(cascades[y].getTopBox());
				} else {
					if(Theater.get().getStage().getSelectedCard() != cascades[y].getCard(x)) {
						cascades[y].getCard(x).draw();
					}
				}
			}
		}
	}
	
	public void drawBox(Rectangle2D rect) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) rect.getX(), (float) rect.getY(), 0);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glLineWidth(2.0f);

		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f((float) rect.getWidth(), 0);
			GL11.glVertex2f((float) rect.getWidth(), (float) rect.getHeight());
			GL11.glVertex2f(0, (float) rect.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void deal() {
		for(int a = 0; a<cascades.length; a++)
			cascades[a] = new Cascade(a % 8, (int) (a / 8));
		
		int x = 0;
		while(x < deck.getCards().length) {
			for(int i = 0; i<8; i++) {
				if(x >= deck.getCards().length)
					break;
				cascades[i].addCard(deck.getCards()[x]);
				cascades[i].alignCard();
				x++;
			}
		}
	}
	
	public boolean hasWon() {
		for(int x = player * 4; x < (player * 4) + 4; x++) {
			if(foundations[x].getNumber() != 13)
				return false;
		}
		
		return true;
	}
	
	public void win() {
		Theater.get().getClient().sendData("Winner");
		calculateScore();
	}
	
	public void calculateScore() {
		int score = 0;
		for(int x = 0; x<foundations.length; x++) {
			if((int) (x / 4) - player == 0 && foundations[x] != null) {
				for(int y = foundations[x].getNumber() + 1; y > 0; y--)
					score += y;
			}
		}
		for(int x = 0; x<cascades.length; x++) {
			if((int) (x / 8) - player == 0)
				score += cascades[x].getCards().size();
		}
		Army.get().tallyArmyValue(score);
	}
	
	public void highlightOptions(Card selection) {
		if(selection != null) {
			for(int x = 0; x<cascades.length; x++) {
				if(cascades[x].getTopCard() != null) {
					if(cascades[x].getTopCard().isOppositeSuit(selection) && 
							cascades[x].getTopCard().getNumber() == selection.getNumber() + 1) {
						cascades[x].getTopCard().setHighlight(new Vector3f(0.15f, 1f, 0.2f));
					} else if(cascades[x].getTopCard().isOppositeSuit(selection) &&
							cascades[x].getTopCard().getNumber() == selection.getNumber() - 1) {
						cascades[x].getTopCard().setHighlight(new Vector3f(0.85f, 0.5f, 0f));
					}
				}
			}
			
			for(int x = 0; x<deck.getCards().length; x++) {
				if(deck.getCards()[x].matches(selection) && deck.getCards()[x].getHighlight() == null) {
					deck.getCards()[x].setHighlight(new Vector3f(1f, 1f, 1f));
				}
			}
		} else {
			for(int x = 0; x<deck.getCards().length; x++) {
				if(deck.getCards()[x].getHighlight() != null) {
					deck.getCards()[x].setHighlight(null);
				}
			}
		}
	}
	
	public int getLongestCascade() {
		int longest = 0;
		for(int x = 1; x<cascades.length; x++) {
			if(cascades[x].getCards().size() > cascades[longest].getCards().size())
				longest = x;
		}

		return longest;
	}
	
	public ArrayList<Card> getSelectableCards() {
		ArrayList<Card> cards = new ArrayList<Card>();
		for(int x = 0; x<cells.length; x++) {
			if(cells[x] != null)
				cards.add(cells[x]);
		}
		for(int x = player * 8; x < (player * 8) + 8; x++) {
			if(cascades[x].getTopCard() != null)
				cards.add(cascades[x].getTopCard());
		}
		
		return cards;
	}
	
	public Cascade[] getCascades() {
		return cascades;
	}
	
	public Cascade getCascade(Card card) {
		for(Cascade cascade : cascades) {
			if(cascade.getCards().contains(card))
				return cascade;
		}
		
		return null;
	}
	
	public boolean openFoundationSuit(Suit suit, int foundation) {
		for(int x = player * 4; x < (player * 4) + 4; x++) {
			if(x != foundation && foundations[x] != null && foundations[x].getSuit() == suit)
				return false;
		}
		
		return true;
	}
	
	public void placeCard(Card card) {
		for(int x = 0; x < cells.length; x++) {
			if(cells[x] == null && card.getBox().intersects(cellRects[x])) {
				card.setX((float) cellRects[x].getX());
				card.setY((float) cellRects[x].getY());
				card.getSprite().setScale(0.5f);
				card.getSprite().setRotation(0);
				
				Theater.get().getClient().sendData("MoveCell;" + card.getID() + ";" + x);
				
				if(getCascade(card) != null)
					getCascade(card).removeCard(card);
				else {
					for(int i = 0; i<cells.length; i++) {
						if(cells[i] == card)
							cells[i] = null;
					}
				}
				cells[x] = card;
				apm.addAction();
				return;
			}
		}
		
		for(int x = player * 4; x < (player * 4) + 4; x++) {
			if(card.getBox().intersects(foundationRects[x]) && card.canBePlacedOnFoundation(foundations[x])
					&& openFoundationSuit(card.getSuit(), x)) {
				card.setX((float) foundationRects[x].getX());
				card.setY((float) foundationRects[x].getY());
				card.getSprite().setScale(0.3f);
				
				Theater.get().getClient().sendData("MoveFoundation;" + card.getID() + ";" + x);
				
				if(getCascade(card) != null)
					getCascade(card).removeCard(card);
				else {
					for(int i = 0; i<cells.length; i++) {
						if(cells[i] == card)
							cells[i] = null;
					}
				}
				
				if(!card.isOwned()) {
					card.setOwned(true);
					Army.get().captureUnit();
				}
				foundations[x] = card;
				apm.addAction();
				
				if(hasWon()) {
					win();
				}
				return;
			}
		}
		
		for(int x = player * 8; x < (player * 8) + 8; x++) {
			if(cascades[x].getTopCard() != card && card.getBox().intersects(cascades[x].getTopBox()) && card.canBePlaced(cascades[x].getTopCard())) {
				Theater.get().getClient().sendData("MoveCascade;" + card.getID() + ";" + x);
				
				if(getCascade(card) != null)
					getCascade(card).removeCard(card);
				else {
					for(int i = 0; i<cells.length; i++) {
						if(cells[i] == card)
							cells[i] = null;
					}
				}
				
				if(!card.isOwned())
					card.setOwned(true);
				cascades[x].addCard(card);
				cascades[x].alignCard();
				cascades[x].compress();
				apm.addAction();
				return;
			}
		}
		
		if(getCascade(card) != null)
			getCascade(card).alignCard();
		else {
			for(int x = 0; x<cells.length; x++) {
				if(cells[x] == card) {
					card.setX((float) cellRects[x].getX());
					card.setY((float) cellRects[x].getY());
				}
			}
		}
	}
	
	public void moveCardCell(Card card, int cell) {
		calculateMovement(card, cellRects[cell]);
		
		if(getCascade(card) != null)
			getCascade(card).removeCard(card);
		else {
			for(int x = 0; x<cells.length; x++) {
				if(cells[x] == card) {
					cells[x] = null;
				}
			}
		}
		
		cells[cell] = card;
	}
	
	public void moveCardFoundation(Card card, int foundation) {
		if(card.isOwned()) {
			card.setOwned(false);
			Army.get().loseUnit();
		}
		
		calculateMovement(card, foundationRects[foundation]);
		
		if(getCascade(card) != null)
			getCascade(card).removeCard(card);
		else {
			for(int x = 0; x<cells.length; x++) {
				if(cells[x] == card) {
					cells[x] = null;
				}
			}
		}
		
		foundations[foundation] = card;
	}
	
	public void moveCardCascade(Card card, int cascade) {
		if(card.isOwned())
			card.setOwned(false);
		calculateMovement(card, cascades[cascade].getTopBox());
		
		if(getCascade(card) != null)
			getCascade(card).removeCard(card);
		else {
			for(int x = 0; x<cells.length; x++) {
				if(cells[x] == card) {
					cells[x] = null;
				}
			}
		}
		
		cascades[cascade].addCard(card);
	}
	
	public void calculateMovement(Card card, Rectangle2D rect) {
		if(rect.getX() == card.getX()) {
			float theta = (float) Math.atan2(rect.getX() - card.getX(), rect.getY() - card.getY());
			card.setMovement(0f, (float) Math.cos(theta) * 10f, 
					(float) Math.sqrt(Math.pow(card.getX() - rect.getX(), 2) + Math.pow(card.getY() - rect.getY(), 2)));
		} else if(rect.getY() == card.getY()) {
			float theta = (float) Math.atan2(rect.getX() - card.getX(), rect.getY() - card.getY());
			card.setMovement((float) Math.sin(theta) * 10f, 0f, 
					(float) Math.sqrt(Math.pow(card.getX() - rect.getX(), 2) + Math.pow(card.getY() - rect.getY(), 2)));
		} else {
			float theta = (float) Math.atan2(rect.getX() - card.getX(), rect.getY() - card.getY());
			card.setMovement((float) Math.sin(theta) * 10f, (float) Math.cos(theta) * 10f, 
					(float) Math.sqrt(Math.pow(card.getX() - rect.getX(), 2) + Math.pow(card.getY() - rect.getY(), 2)));
		}
	}
	
	public void snapCard(Card card) {
		card.stopMoving();
		for(int x = 0; x<cells.length; x++) {
			if(cells[x] == card) {
				cells[x].getSprite().setScale(0.5f);
				cells[x].getSprite().setRotation(0);
				cells[x].setPosition((float) cellRects[x].getX(), (float) cellRects[x].getY());
				return;
			}
		}
		
		for(int x = 0; x<foundations.length; x++) {
			if(foundations[x] == card) {
				foundations[x].setPosition((float) foundationRects[x].getX(), (float) foundationRects[x].getY());
				foundations[x].getSprite().setScale(0.3f);
				foundations[x].getSprite().setRotation((int) (x / 4) - player < 0 ? 
						(((int) (x / 4) - player) + 4)  * 90 : ((int) (x / 4) - player) * 90);
				return;
			}
		}
		
		if(getCascade(card) != null) {
			getCascade(card).alignCard();
			getCascade(card).compress();
		}
	}
		
	public Card getCard(String id) {
		for(int x = 0; x<deck.getCards().length; x++) {
			if(deck.getCards()[x].getID().matches(id))
				return deck.getCards()[x];
		}
		
		return null;
	}
	
	public Card[] getCards() {
		return deck.getCards();
	}
	
	public int getPlayer() {
		return player;
	}
	
	public Actions getAPM() {
		return apm;
	}
	
}
