package client;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import client.menu.Chatlog;
import client.utility.Event;
import client.utility.Text;
import de.matthiasmann.twl.utils.PNGDecoder;

public class Screen {

	private final int WIDTH = 960;
	private final int HEIGHT = 720;
	public int displayWidth = WIDTH;
	public int displayHeight = HEIGHT;
	
	private final Rectangle2D fixedCamera = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	public Rectangle2D camera = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	
	private float fadeTotal;
	private float fadeTimer;
	private float fade = 0f;
	
	private String name;
	
	public Screen() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("FreedomCell " + " FPS: " + Theater.fps + " " + Theater.version);
			try {
				Display.setIcon(loadIcon(System.getProperty("resources") + "/ui/Icon.png"));
			} catch (IOException e) {
				System.out.println("Failed to load icon");
			}
			Display.setResizable(true);
			Display.create();
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, displayWidth, displayHeight, 0, -1, 1);
			GL11.glViewport(0, 0, displayWidth, displayHeight);
		} catch (LWJGLException e) {
			System.err.println("Could not create display.");
		}
		
		camera = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);
	}
	
	public static ByteBuffer[] loadIcon(String ref) throws IOException {
        InputStream fis = ResourceLoader.getResourceAsStream(ref);
        try {
            PNGDecoder decoder = new PNGDecoder(fis);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            ByteBuffer[] buffer = new ByteBuffer[1];
            buffer[0] = bb;
            return buffer;
        } finally {
            fis.close();
        }
    }
	
	public void update() {
		Display.update();
		Display.sync(200);
	}
	
	public void updateHeader() {
		Display.setTitle("FreedomCell " + " FPS: " + Theater.fps + " " + Theater.version);
	}
	
	public void draw(Stage stage) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// Draw the backdrop
		if(stage.backdrop != null)
			stage.backdrop.draw();

		// Sort the z-buffer 
		for(int x = 0; x<stage.scenery.size(); x++) {
			for(int i = x; i>=0 && i>x-5; i--) {
				if(stage.scenery.get(x).getBox().getMaxY() < stage.scenery.get(i).getBox().getMaxY()) {
					stage.scenery.add(i, stage.scenery.get(x));
					stage.scenery.remove(x+1);
					x--;
				}
			}
		}
		// Draw the scenery
		for(int x = 0; x<stage.scenery.size(); x++) {
			stage.scenery.get(x).draw();
		}
		
		// Draw the playing field
		if(stage.playingField != null) {
			stage.playingField.draw();
		
			// Draw player names
			if(Theater.get().getClient() != null) {
				for(int x = 0; x<4; x++) {
					if(x == 0)
						name = "Greaser";
					else if(x == 1)
						name = "Cardshark";
					else if(x == 2)
						name = "Patron";
					else if(x == 3)
						name = "Bill";
					
					switch(x - stage.playingField.getPlayer() < 0 ? (x - stage.playingField.getPlayer()) + 4 : x - stage.playingField.getPlayer()) {
					case(0):
						Text.drawString(Theater.get().getClient().getClientName(), 300, 390, Color.white);
						break;
					case(1):
						Text.drawString(Theater.get().getClient().getPlayerName(x).matches(" ") ? name : Theater.get().getClient().getPlayerName(x), 800, 420,
								Theater.get().getClient().getPlayerName(x).matches(" ") ? Color.gray : Color.white);
						break;
					case(2):
						Text.drawString(Theater.get().getClient().getPlayerName(x).matches(" ") ? name : Theater.get().getClient().getPlayerName(x), 485, 185,
								Theater.get().getClient().getPlayerName(x).matches(" ") ? Color.gray : Color.white);
						break;
					case(3):
						Text.drawString(Theater.get().getClient().getPlayerName(x).matches(" ") ? name : Theater.get().getClient().getPlayerName(x), 60, 45,
								Theater.get().getClient().getPlayerName(x).matches(" ") ? Color.gray : Color.white);
						break;
					}
				}
			}
		}
		
		// Draw selected card
		if(stage.getSelectedCard() != null) {
			stage.getSelectedCard().draw();
			stage.getSelectedCard().drawBox();
		}
		
		// Draw hover card
		if(stage.hoverCard != null) {
			stage.hoverCard.draw();
			//stage.hoverCard.drawBox();
		}
		
		Chatlog.get().draw();
		
		if(Theater.get().debug) {
			if(stage.playingField != null) {
				Text.drawString("APM: " + stage.playingField.getAPM().getCurrentAPM(), 15, 15, Color.white);
				Text.drawString("Avg: " + stage.playingField.getAPM().getAverageAPM(), 150, 15, Color.white);
			}
		}
		
		if(stage.scoreScreen != null) {
			stage.scoreScreen.draw();
		}
		
		// Draw the win text
		if(!stage.winList.isEmpty()) {
			stage.winList.get(0).draw();
		}

		// Process fading
		fade();
		
		// Draw any achievement gets
		if(!stage.achievementsToShow.isEmpty()) {
			Text.drawString("Achievement Get!", 50, 15, Color.pink);
			Text.drawString(stage.achievementsToShow.peek(), 15, 40, Color.white);
		}
	}
	
	public boolean resized() {
		if(Display.getWidth() != displayWidth || Display.getHeight() != displayHeight)
			return true;
		
		return false;
	}
	
	public void resize() {
		displayWidth = Display.getWidth();
		displayHeight = Display.getHeight();
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
		
		camera = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);
	}
	
	public float getCameraXScale() {
		return (float) (camera.getWidth() / fixedCamera.getWidth());
	}
	
	public float getCameraYScale() {
		return (float) (camera.getHeight() / fixedCamera.getHeight());
	}
	
	public boolean toBeClosed() {
		if(Display.isCloseRequested()) 
			return true;
		
		return false;
	}
	
	public void close() {
		Display.destroy();
	}
	
	public void setFadeTimer(float fadeTimer) {
		this.fadeTimer = fadeTimer;
		this.fadeTotal = fadeTimer;
		
		if(fadeTimer > 0f)
			fade = 0f;
		else
			fade = 1f;
	}
	
	public void fade() {
		if(fadeTotal > 0f) {
			fade += (1f / fadeTotal) * Theater.getDeltaSpeed(0.025f);
			fadeTimer -= Theater.getDeltaSpeed(0.025f);
		} else if(fadeTotal < 0f) {
			fade -= (1f / Math.abs(fadeTotal)) * Theater.getDeltaSpeed(0.025f);
			fadeTimer += Theater.getDeltaSpeed(0.025f);
		}
		
		if(fadeTotal > 0f ? fadeTimer < 0f : fadeTimer > 0f) {
			fadeTimer = 0f;
			fadeTotal = 0f;
			if(Event.get().inAction())
				Event.get().finishAction();
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glColor4f(0f, 0f, 0f, fade);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(Theater.get().getScreen().camera.getWidth(), 0);
			GL11.glVertex2d(Theater.get().getScreen().camera.getWidth(), Theater.get().getScreen().camera.getHeight());
			GL11.glVertex2d(0, Theater.get().getScreen().camera.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
}
