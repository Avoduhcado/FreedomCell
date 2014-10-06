package client;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import client.entity.cards.Card;
import client.menu.Chatlog;
import client.utility.keyboard.Keybinds;

public class Input {
	
	public static boolean mousePressed;
	public static boolean mouseHeld;
	public static boolean cardRelease;
	public static Point mouseClick = new Point();
	public static Point mouseRelease = new Point();
	
	public static void checkInput(Stage stage) {
		Keybinds.update();
		
		if(Keybinds.CONFIRM.clicked() && stage.playingField != null && Chatlog.get().closed()) {
			Chatlog.get().open();
		} else if(Keybinds.CANCEL.clicked() && stage.playingField != null && !Chatlog.get().closed()) {
			Chatlog.get().close();
		} else if(Keybinds.CANCEL.clicked() && stage.playingField != null && Chatlog.get().closed()) {
			Theater.get().quitGame(0);
		}
		
		if(Keybinds.DEBUG.clicked()) {
			Theater.get().debug = !Theater.get().debug;
			/*if(stage.scoreScreen == null && stage.playingField != null) {
				stage.playingField.win();
			} else if(stage.scoreScreen != null)
				stage.scoreScreen = null;*/
		}
		
		if(Mouse.isButtonDown(0)) {
			mousePress(stage);
		} else {
			mouseRelease(stage);
		}
		
		if(stage.scoreScreen == null) {
			if(mousePressed) {
				if(stage.getSelectedCard() == null && !cardRelease && stage.playingField != null) {
					for(Card card : stage.playingField.getSelectableCards()) {
						if(card.getBox().contains(getMouseX(), getMouseY())) {
							stage.setSelectedCard(card);
							break;
						}
					}
				} else {
					if(stage.getSelectedCard() != null && !mouseHeld) {
						if(!stage.getSelectedCard().getBox().contains(getMouseX(), getMouseY())) {
							stage.getSelectedCard().setPosition(getMouseX(), getMouseY());
							stage.playingField.placeCard(stage.getSelectedCard());
						}
						stage.setSelectedCard(null);
						cardRelease = true;
					}
				}
			} else {
				if(stage.getSelectedCard() != null && mouseClick.distance(mouseRelease) > 3) {
					stage.playingField.placeCard(stage.getSelectedCard());
					stage.setSelectedCard(null);
				}
				
				if(Mouse.isInsideWindow() && stage.playingField != null) {
					if(stage.hoverCard == null) {
						for(int x = 0; x<stage.playingField.getCascades().length; x++) {
							for(int y = stage.playingField.getCascades()[x].getCards().size() - 1; y >= 0; y--) {
								if(stage.playingField.getCascades()[x].getCard(y).getVisibleBox().contains(getMouseX(), getMouseY())) {
									stage.hoverCard = stage.playingField.getCascades()[x].getCard(y);
									stage.playingField.highlightOptions(stage.hoverCard);
									break;
								}
							}
						}
					} else {
						if(!stage.hoverCard.getVisibleBox().contains(getMouseX(), getMouseY())) {
							stage.hoverCard = null;
							stage.playingField.highlightOptions(null);
							if(stage.getSelectedCard() != null) {
								stage.playingField.highlightOptions(stage.getSelectedCard());
								stage.getSelectedCard().setHighlight(new Vector3f(0f, 1f, 1f));
							}
						}
					}
				}
			}
		}
	}
	
	public static float getMouseX() {
		return Mouse.getX() / Theater.get().getScreen().getCameraXScale();
	}
	
	public static float getMouseY() {
		return -(Mouse.getY() - Theater.get().getScreen().displayHeight) / Theater.get().getScreen().getCameraYScale();
	}
	
	public static Point2D getMouse() {
		return new Point2D.Double(getMouseX(), getMouseY());
	}
	
	public static void mousePress(Stage stage) {
		if(!mousePressed) {
			mousePressed = true;
			mouseClick = new Point(Mouse.getX(), Mouse.getY());
			if(stage.getSelectedCard() == null)
				mouseHeld = true;
		}
	}
	
	public static void mouseRelease(Stage stage) {
		if(mousePressed) {
			mousePressed = false;
			cardRelease = false;
			mouseRelease = new Point(Mouse.getX(), Mouse.getY());
			if(stage.getSelectedCard() != null)
				mouseHeld = false;
		}
	}
	
}
