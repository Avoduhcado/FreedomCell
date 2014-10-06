package client.menu;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import client.Theater;
import client.utility.Text;
import client.utility.keyboard.Keybinds;

public class InputBox {

	private boolean hasInputNumbers;
	private String text;
	private String backupText;
	private float flash = 0.0f;
	private boolean centered = true;
	
	public InputBox(boolean hasInputNumbers, String text) {
		Keybinds.clear();
		this.hasInputNumbers = hasInputNumbers;
		Keyboard.enableRepeatEvents(true);
		this.text = text != null ? text : "";
		this.backupText = this.text;
	}
	
	public void update() {
		if(flash < 1.0f)
			flash += Theater.getDeltaSpeed(0.025f);
		else
			flash = 0;
	}
	
	public void draw(float x, float y) {
		if(centered)
			Text.drawCenteredString(flash > 0.5f ? text + "|" : text, x, y, Color.white);
		else
			Text.drawString(flash > 0.5f ? text + "|" : text, x, y, Color.white);
	}
	
	public String input() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() != Keyboard.KEY_LSHIFT && Keyboard.getEventKey() != Keyboard.KEY_RSHIFT && Keyboard.getEventKey() != Keyboard.KEY_RIGHT
						 && Keyboard.getEventKey() != Keyboard.KEY_LEFT && Keyboard.getEventKey() != Keyboard.KEY_DOWN && Keyboard.getEventKey() != Keyboard.KEY_UP
						 && Keyboard.getEventKey() != Keyboard.KEY_LCONTROL && Keyboard.getEventKey() != Keyboard.KEY_RCONTROL) {
					if(Keyboard.getEventKey() == Keyboard.KEY_BACK && text.length() > 0) {
						text = (String)text.subSequence(0, text.length() - 1);
					} else if(Keyboard.getEventKey() != Keyboard.KEY_BACK && text.length() <= 100) {
						if(hasInputNumbers && text.length() < 20) {
							if(Keyboard.getEventKey() == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
								try {
									text += Integer.parseInt((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
								} catch (NumberFormatException e) {
									;
								} catch (HeadlessException e) {
									e.printStackTrace();
								} catch (UnsupportedFlavorException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								if(Keyboard.getEventKey() == Keyboard.KEY_0 || Keyboard.getEventKey() == Keyboard.KEY_1 || Keyboard.getEventKey() == Keyboard.KEY_2 || 
										Keyboard.getEventKey() == Keyboard.KEY_3 || Keyboard.getEventKey() == Keyboard.KEY_4 || Keyboard.getEventKey() == Keyboard.KEY_5 || 
										Keyboard.getEventKey() == Keyboard.KEY_6 || Keyboard.getEventKey() == Keyboard.KEY_7 || Keyboard.getEventKey() == Keyboard.KEY_8 || 
										Keyboard.getEventKey() == Keyboard.KEY_9 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD1 || 
										Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD3 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4 || 
										Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD7 || 
										Keyboard.getEventKey() == Keyboard.KEY_NUMPAD8 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD9 || Keyboard.getEventKey() == Keyboard.KEY_PERIOD ||
										Keyboard.getEventKey() == Keyboard.KEY_NUMPADCOMMA) {
									text = text + Keyboard.getEventCharacter();
								}
							}
						} else if(!hasInputNumbers) {
							if(Keyboard.getEventKey() == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
								try {
									text += (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								} catch (HeadlessException e) {
									e.printStackTrace();
								} catch (UnsupportedFlavorException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								if(Keyboard.getEventCharacter() != Keyboard.CHAR_NONE)
									text = text + Keyboard.getEventCharacter();
							}
						}
					}
				}
			}
		}
		if(Keybinds.CANCEL.clicked()) {
			Keyboard.enableRepeatEvents(false);
			return backupText;
		}
		if(Keybinds.CONFIRM.clicked()) {
			Keyboard.enableRepeatEvents(false);
			return text;
		}
		
		return null;
	}
	
	public void setCentered(boolean centered) {
		this.centered = centered;
	}
	
	public String getText() {
		return text;
	}
	
}
