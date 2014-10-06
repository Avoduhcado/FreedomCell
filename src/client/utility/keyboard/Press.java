package client.utility.keyboard;


public class Press {

	private int key;
	private boolean pressed;
	private boolean held;
	private boolean released;
	private boolean disabled;
	
	public Press(int k) {
		setKey(k);
		setPressed(Keyboard.isPressed(k));
		setHeld(Keyboard.isPressed(k));
	}
	
	public void update() {
		if(Keyboard.isPressed(getKey())) {
			setPressed(true);
			setReleased(false);
		} else {
			if(isPressed())
				setReleased(true);
			else
				setReleased(false);
			setPressed(false);
			setHeld(false);
		}
			
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isPressed() {
		if(isDisabled())
			return false;
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean isHeld() {
		if(isDisabled())
			return false;
		return held;
	}

	public void setHeld(boolean held) {
		this.held = held;
	}

	public boolean isReleased() {
		if(isDisabled())
			return false;
		return released;
	}

	public void setReleased(boolean released) {
		this.released = released;
	}
	
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
