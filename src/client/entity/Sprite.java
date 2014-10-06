package client.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import client.Theater;
import client.utility.TextureLoader;

public class Sprite {
	
	protected Texture texture;
	protected int maxDir = 1;
	protected int maxFrame = 1;
	protected int dir;
	protected int frame;
	private float animStep;

	protected float width;
	protected float height;
	protected float textureX;
	protected float textureY;
	protected float textureXWidth;
	protected float textureYHeight;
	
	public Sprite(String ref, int maxDir, int maxFrame) {
		setTexture(ref);
		this.maxDir = maxDir;
		this.maxFrame = maxFrame;
	}
	
	public Sprite(String ref) {
		this.texture = TextureLoader.get().getSlickTexture(System.getProperty("resources") + ref + ".png");
	}
	
	public static Sprite loadSprite(String ref) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/sprites"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ref)) {
	    			Sprite sprite = new Sprite(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
	    			
	    			reader.close();
	    			return sprite;
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The sprite database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Sprite database failed to load!");
	    	e.printStackTrace();
	    }
		
		System.out.println("Failed to load sprite: " + ref);
		return new Sprite(null, 0, 0);
	}
	
	public void draw(float x, float y) {
		texture.bind();

		width = texture.getWidth() / maxFrame;
		height = texture.getHeight() / maxDir;

		textureX = 0;
		textureY = 0;
		textureXWidth = width;
		textureYHeight = height;
		textureX = width * frame;
		textureXWidth = (width * frame) + width;
		if(maxDir > 1) {
			textureY = height * dir;
			textureYHeight = (height * dir) + height;
		}
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, 0f);
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glScalef(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(textureX, textureY);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(textureXWidth, textureY);
			GL11.glVertex2f(getWidth(), 0);
			GL11.glTexCoord2f(textureXWidth, textureYHeight);
			GL11.glVertex2f(getWidth(), getHeight());
			GL11.glTexCoord2f(textureX, textureYHeight);
			GL11.glVertex2f(0, getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public void animate() {
		if(maxFrame > 1) {
			animStep += Theater.getDeltaSpeed(0.025f);
			if (animStep >= 0.16f) {
				animStep = 0f;
				frame++;
				if (frame >= maxFrame) {
					frame = 0;
				}
			}
		}
	}
	
	public void setTexture(String ref) {
		String temp = System.getProperty("resources") + "/images/" + ref + ".png";
		this.texture = TextureLoader.get().getSlickTexture(temp);
	}

	public int getFrame() {
		return frame;
	}
	
	public void setFrame(int frame) {
		this.frame = frame;
	}
	
	public int getDir() {
		return dir;
	}
	
	public void setDir(int dir) {
		this.dir = dir;
	}
	
	public int getMaxFrame() {
		return maxFrame;
	}
	
	public float getWidth() {
		return texture.getImageWidth() / maxFrame;
	}
	
	public float getHeight() {
		return texture.getImageHeight() / maxDir;
	}
	
}
