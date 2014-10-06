package client.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import client.Theater;

public class AdjustableSprite extends Sprite {

	private float scale = 1f;
	private float rotation = 0f;
	private float fade = -1f;
	private boolean fader;
	
	public AdjustableSprite(String ref, int maxDir, int maxFrame) {
		super(ref, maxDir, maxFrame);
	}
	
	public AdjustableSprite(String ref) {
		super(ref);
	}

	public static AdjustableSprite loadSprite(String ref) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/sprites"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ref)) {
	    			AdjustableSprite sprite = new AdjustableSprite(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
	    			
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
		return new AdjustableSprite(null, 0, 0);
	}
	
	@Override
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
		GL11.glScalef(scale, scale, 0);
		GL11.glRotatef(rotation, 0f, 0f, 1f);
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
	
	public void drawBox(float x, float y, Vector3f color) {
		if(color == null && Theater.get().debug) {
			color = new Vector3f(1f,1f,1f);
			fade = 1f;
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, 0);
		GL11.glColor4f(color.x, color.y, color.z, fade);
		GL11.glLineWidth(2.5f);
		GL11.glScalef(scale, scale, 1f);
		GL11.glRotatef(rotation, 0, 0, 1f);

		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(getWidth(), 0);
			GL11.glVertex2f(getWidth(), getHeight());
			GL11.glVertex2f(0, getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void animate() {
		if(fade != -1f) {
			if(fader) {
				fade -= Theater.getDeltaSpeed(0.075f);
				if(fade <= 0f) {
					fade = 0f;
					fader = false;
				}
			} else {
				fade += Theater.getDeltaSpeed(0.075f);
				if(fade >= 1f) {
					fade = 1f;
					fader = true;
				}
			}
		}
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void increaseScale(float amount) {
		this.scale += amount;
		if(this.scale > 1f)
			scale = 0f;
		else if(this.scale < 0f)
			scale = 1f;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public void increaseRotation(float amount) {
		this.rotation += amount;
		if(this.rotation > 360)
			rotation = 0;
		else if(this.rotation < 0)
			rotation = 360;
	}
	
	public void setFade(float fade) {
		this.fade = fade;
	}
	
}
