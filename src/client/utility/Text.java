package client.utility;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import client.Theater;

public class Text {

	private static Font font = new Font("Times New Roman", Font.PLAIN, 16);
	private static String fontName = "MODENINE.ttf";
	protected static UnicodeFont unifont;
	
	public static void drawString(String text, float x, float y, Color col) {
		unifont.drawString((x + 2), y + 2, text, Color.black);
		unifont.drawString(x, y, text, col);
	}
	
	public static void drawCenteredString(String text, float x, float y, Color col) {
		unifont.drawString(((x + 2) - (unifont.getWidth(text) / 2f)) / Theater.get().getScreen().getCameraXScale(), y + 2, text, Color.black);
		unifont.drawString((x - (unifont.getWidth(text) / 2f)) / Theater.get().getScreen().getCameraXScale(), y, text, col);
	}
	
	@SuppressWarnings("unchecked")
	public static void setFont() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		try {
			font = Font.createFont(Font.PLAIN, new FileInputStream(System.getProperty("resources") + "/fonts/" + fontName));
			font = font.deriveFont(24.0f);
		} catch (FontFormatException e) {
			System.err.println("Invalid font format");
		} catch (IOException e) {
			System.err.println("Could not find font");
		}
		
		unifont = new UnicodeFont(font);
		unifont.setPaddingBottom(2);
		unifont.setPaddingTop(2);
		unifont.getEffects().add(new ColorEffect());
		unifont.addAsciiGlyphs();
		try {
			unifont.loadGlyphs();
		} catch(SlickException e) {
			System.err.println("Could not load ASCII Glyphs");
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static UnicodeFont getUnifont() {
		return unifont;
	}
	
}
