package client.scores;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import client.Theater;
import client.utility.Text;

public class ScoreScreen {

	private Rectangle2D[] playerBoxes = new Rectangle2D[4];
	private int boxHover = 0;
	
	private Rectangle2D restartBox;
	
	private float[] armyScores = new float[4];
	private String[] extendedScores = new String[4];
		
	public ScoreScreen(String scores) {
		String[] temp = scores.split("-");
		for(int x = 0; x<temp.length; x++) {
			armyScores[x] = Float.parseFloat(temp[x].split(",")[0]);
			String[] stats = temp[x].split(",")[1].split(" ");
			extendedScores[x] = "Cap: " + stats[0] + " Lost: " + stats[1] + " Base: " + stats[2];
		}
		
		for(int x = 0; x<playerBoxes.length; x++) {
			playerBoxes[x] = new Rectangle2D.Double(300, 250 + (25 * x), 300, 25);
		}
		
		restartBox = new Rectangle2D.Double(300, 650, 300, 30);
	}
	
	public void update() {
		if(Mouse.isInsideWindow()) {
			for(int x = 0; x<playerBoxes.length; x++) {
				if(playerBoxes[x].contains(Mouse.getX(), -(Mouse.getY() - Theater.get().getScreen().displayHeight))) {
					boxHover = x + 1;
					break;
				} else
					boxHover = 0;
			}
			
			if(restartBox.contains(Mouse.getX(), -(Mouse.getY() - Theater.get().getScreen().displayHeight)))
				boxHover = 5;
		} else if(boxHover != 0)
			boxHover = 0;
		
		if(Mouse.isButtonDown(0)) {
			if(restartBox.contains(Mouse.getX(), -(Mouse.getY() - Theater.get().getScreen().displayHeight))) {
				Theater.get().quitGame(2);
			}
		}
	}

	public void draw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		
		GL11.glTranslatef(0, 0, 0);
		GL11.glColor4f(0f, 0f, 0f, 0.45f);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(960, 0);
			GL11.glVertex2f(960, 720);
			GL11.glVertex2f(0, 720);
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Text.drawCenteredString("Game over!", Theater.get().getScreen().displayWidth / 2, 200, Color.cyan);
		for(int x = 0; x<armyScores.length; x++) {
			if(boxHover == x + 1) {
				Text.drawCenteredString(Theater.get().getClient().getPlayerName(x) + " Summary: " + armyScores[x] +
						" " + extendedScores[x],
						Theater.get().getScreen().displayWidth / 2, 250 + (25 * x), Color.white);
			} else {
				Text.drawCenteredString(Theater.get().getClient().getPlayerName(x) + " Summary: " + armyScores[x],
						Theater.get().getScreen().displayWidth / 2, 250 + (25 * x), Color.white);
			}
		}
		
		Text.drawCenteredString("Restart", Theater.get().getScreen().displayWidth / 2, 650,
				boxHover == 5 ? Color.white : Color.gray);
	}
	
}
