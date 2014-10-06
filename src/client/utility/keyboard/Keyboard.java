package client.utility.keyboard;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	private static boolean[] keys = new boolean[1024];
	
	public static boolean isPressed(int key) {
		//return keys[key];
		return org.lwjgl.input.Keyboard.isKeyDown(key);
	}

	public static void init(Component c) {
		c.addKeyListener(new Keyboard());
	}
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
	}

}
