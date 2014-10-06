package client.entity;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.lwjgl.util.vector.Vector3f;

import client.Theater;
import client.utility.Event;

public class Entity {

	private static int entityCount = 0;
	
	protected AdjustableSprite sprite;
	protected String name;
	protected String id;
	protected float x;
	protected float y;
	protected float dx;
	protected float dy;
	protected float distance;
	protected boolean moving;
	protected float moveTimer;
	private Rectangle2D box;
	protected Vector3f highlight;
	
	public Entity(String ref, float x, float y) {
		this.sprite = AdjustableSprite.loadSprite(ref);
		this.name = ref;
		this.id = name + entityCount;
		entityCount++;
		this.x = x;
		this.y = y;
		this.box = new Rectangle2D.Double(x, y, sprite.getWidth() * sprite.getScale(), sprite.getHeight() * sprite.getScale());
	}
	
	public void update() {
		sprite.animate();
		move();
	}
	
	public void draw() {
		if(Theater.get().getScreen().camera.intersects(this.box)) {
			sprite.draw(x, y);
			if(highlight != null || Theater.get().debug)
				sprite.drawBox(x, y, highlight);
		}
	}
	
	public void drawBox() {
		sprite.drawBox(x, y, highlight);
	}
	
	public void move() {
		if(distance > 0f) {
			moveTimer -= Theater.getDeltaSpeed(0.025f);
			distance -= (float) Math.sqrt(Math.pow(Theater.getDeltaSpeed(dx), 2) + Math.pow(Theater.getDeltaSpeed(dy), 2));
			
			if(distance <= 0f) {
				distance = 0f;
				dx = 0;
				dy = 0;
				
				Event.get().finishAction();
			}
		}
		
		if(dx != 0 || dy != 0) {
			x += Theater.getDeltaSpeed(dx);
			y += Theater.getDeltaSpeed(dy);
			updateBox();
		}
	}
	
	public void setMovement(int direction, float distance) {
		this.distance = distance;
		switch(direction) {
		case(0):
			dy = 3f;
			break;
		case(1):
			dx = 3f;
			break;
		case(2):
			dx = -3f;
			break;
		case(3):
			dy = -3f;
			break;
		}
	}
	
	public void setMovement(float dx, float dy, float distance) {
		this.moving = true;
		this.distance = distance;
		this.dx = dx;
		this.dy = dy;
		this.moveTimer = 2f;
	}
	
	public void setMovementWithSpeed(int direction, float distance, float speed) {
		this.distance = distance;
		switch(direction) {
		case(0):
			dy = speed;
			break;
		case(1):
			dx = speed;
			break;
		case(2):
			dx = -speed;
			break;
		case(3):
			dy = -speed;
			break;
		}
	}
	
	public void stopMoving() {
		this.moving = false;
		this.distance = 0f;
		this.dx = 0f;
		this.dy = 0f;
		this.moveTimer = 0f;
	}
	
	public void setPositionWithMovement(float x, float y, float dx, float dy, float distance) {
		setPosition(x, y);
		setMovement(dx, dy, distance);
	}
	
	public void updateBox() {
		AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(sprite.getRotation()),  
				box.getX(), box.getY());
		box = at.createTransformedShape(box).getBounds2D();
		box.setFrame(x, y, sprite.getWidth() * sprite.getScale(), sprite.getHeight() * sprite.getScale());
	}
	
	public String getName() {
		return name;
	}
	
	public String getID() {
		return id;
	}
	
	public void setPosition(float x, float y) {
		this.distance = 0;
		this.setX(x);
		this.setY(y);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
		updateBox();
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
		updateBox();
	}

	public float getDistance() {
		return distance;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public float getMoveTimer() {
		return moveTimer;
	}
	
	public void setMoveTimer(float moveTimer) {
		this.moveTimer = moveTimer;
	}
	
	public Rectangle2D getBox() {
		return box;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public AdjustableSprite getSprite() {
		return sprite;
	}
	
	public Vector3f getHighlight() {
		return highlight;
	}
	
	public void setHighlight(Vector3f highlight) {
		if(highlight != null)
			sprite.setFade(0f);
		else
			sprite.setFade(-1f);
		this.highlight = highlight;
	}
	
}
