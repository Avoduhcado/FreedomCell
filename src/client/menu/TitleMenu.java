package client.menu;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.Color;

import client.Input;
import client.Theater;
import client.entity.Prop;
import client.utility.Config;
import client.utility.Text;

public class TitleMenu {

	private Prop logo;
	
	private Rectangle2D joinBox;
	private Rectangle2D joinIPBox;
	private InputBox joinIP;
	private String newIP;
	private Rectangle2D joinPortBox;
	private InputBox joinPort;
	private String newPort;
	private Rectangle2D nameBox;
	private InputBox name;
	private String newName;
	private Rectangle2D hostBox;
	private Rectangle2D hostIPBox;
	private boolean hostIP = Config.showHostIP;
	private Rectangle2D hostPortBox;
	private InputBox hostPort;
	private String newHostPort;
	private Rectangle2D exitBox;
	private int boxHover = 0;
	private boolean mousePress;
	
	private boolean fader = true;
	private float fadeWait;
	private Vector4f bgColor = new Vector4f(1f, 1f, 1f, 0f);
	private boolean open;
	
	private static TitleMenu menu;
	
	public static void init() {
		Text.setFont();
		menu = new TitleMenu();
	}
	
	public static TitleMenu get() {
		return menu;
	}
	
	public TitleMenu() {
		logo = new Prop("logo", 47, 100);
		
		joinBox = new Rectangle2D.Double(((Theater.get().getScreen().displayWidth / 2) - (Text.getUnifont().getWidth("Join Game") / 2)) / Theater.get().getScreen().getCameraXScale(),
				(Theater.get().getScreen().displayHeight / 3) / Theater.get().getScreen().getCameraYScale(), 
				Text.getUnifont().getWidth("Join Game") + Text.getUnifont().getWidth("__"),
				Text.getUnifont().getHeight("Join Game") + 5);
		joinIPBox = new Rectangle2D.Double(((Theater.get().getScreen().displayWidth / 2) - joinBox.getWidth()) / Theater.get().getScreen().getCameraXScale(),
				(joinBox.getY() + joinBox.getHeight()), 
				joinBox.getWidth() * 2,
				joinBox.getHeight() + 5);
		joinPortBox = new Rectangle2D.Double(((Theater.get().getScreen().displayWidth / 2) - joinBox.getWidth()) / Theater.get().getScreen().getCameraXScale(),
				(joinBox.getY() + (joinBox.getHeight() * 2)), 
				joinBox.getWidth() * 2,
				joinBox.getHeight() + 5);
		nameBox = new Rectangle2D.Double(((Theater.get().getScreen().displayWidth / 2) - joinBox.getWidth()) / Theater.get().getScreen().getCameraXScale(),
				(joinBox.getY() + (joinBox.getHeight() * 3)), 
				joinBox.getWidth() * 2,
				joinBox.getHeight() + 5);
		hostBox = new Rectangle2D.Double(((Theater.get().getScreen().displayWidth / 2) - (Text.getUnifont().getWidth("Host Game") / 2)) / Theater.get().getScreen().getCameraXScale(),
				(joinBox.getY() + (joinBox.getHeight() * 4)), 
				Text.getUnifont().getWidth("Host Game") + Text.getUnifont().getWidth("__"), 
				Text.getUnifont().getHeight("Host Game") + 5);
		hostIPBox = new Rectangle2D.Double(((Theater.get().getScreen().displayWidth / 2) - hostBox.getWidth()) / Theater.get().getScreen().getCameraXScale(),
				(hostBox.getY() + hostBox.getHeight()), 
				hostBox.getWidth() * 2,
				hostBox.getHeight() + 5);
		hostPortBox = new Rectangle2D.Double(((Theater.get().getScreen().displayWidth / 2) - hostBox.getWidth()) / Theater.get().getScreen().getCameraXScale(),
				(hostBox.getY() + (hostBox.getHeight() * 2)), 
				hostBox.getWidth() * 2,
				hostBox.getHeight() + 5);
		exitBox = new Rectangle2D.Double(((Theater.get().getScreen().displayWidth / 2) - (Text.getUnifont().getWidth("Exit") / 2)) / Theater.get().getScreen().getCameraXScale(),
				(hostBox.getY() + (hostBox.getHeight() * 3)), 
				Text.getUnifont().getWidth("Exit") + Text.getUnifont().getWidth("__"), 
				Text.getUnifont().getHeight("Exit") + 5);
	}
	
	public void update() {
		fade();
		
		if(joinIP != null) {
			joinIP.update();
			newIP = joinIP.input();
			if(newIP != null) {
				Config.joinIP = newIP;
				disableJoinIP();
			}
		} else if(joinPort != null) {
			joinPort.update();
			newPort = joinPort.input();
			if(newPort != null) {
				Config.joinPort = Integer.parseInt(newPort);
				disableJoinPort();
			}
		} else if(name != null) {
			name.update();
			newName = name.input();
			if(newName != null) {
				Config.joinName = newName;
				disableName();
			}
		} else if(hostPort != null) {
			hostPort.update();
			newHostPort = hostPort.input();
			if(newHostPort != null) {
				Config.hostPort = Integer.parseInt(newHostPort);
				disableHostPort();
			}
		}
		
		if(Mouse.isInsideWindow()) {
			if(joinBox.contains(Input.getMouse()))
				boxHover = 1;
			else if(joinIPBox.contains(Input.getMouse()))
				boxHover = 2;
			else if(joinPortBox.contains(Input.getMouse()))
				boxHover = 3;
			else if(nameBox.contains(Input.getMouse()))
				boxHover = 4;
			else if(hostBox.contains(Input.getMouse()))
				boxHover = 5;
			else if(hostIPBox.contains(Input.getMouse()))
				boxHover = 6;
			else if(hostPortBox.contains(Input.getMouse()))
				boxHover = 7;
			else if(exitBox.contains(Input.getMouse()))
				boxHover = 8;
			else
				boxHover = 0;
		} else if(boxHover != 0)
			boxHover = 0;
		
		if(Mouse.isButtonDown(0)) {
			if(joinBox.contains(Input.getMouse())) {
				if(!optionsClosed())
					finalizeOptions();
				close();
				Theater.get().join();
			} else if(joinIPBox.contains(Input.getMouse())) {
				if(joinIP == null && !mousePress) {
					finalizeOptions();
					mousePress = true;
					enableJoinIP();
				} else if(joinIP != null && !mousePress) {
					mousePress = true;
					disableJoinIP();
				}
			} else if(joinPortBox.contains(Input.getMouse())) {
				if(joinPort == null && !mousePress) {
					finalizeOptions();
					mousePress = true;
					enableJoinPort();
				} else if(joinPort != null && !mousePress) {
					mousePress = true;
					disableJoinPort();
				}
			} else if(nameBox.contains(Input.getMouse())) {
				if(name == null && !mousePress) {
					finalizeOptions();
					mousePress = true;
					enableName();
				} else if(name != null && !mousePress) {
					mousePress = true;
					disableName();
				}
			} else if(hostBox.contains(Input.getMouse())) {
				if(!optionsClosed())
					finalizeOptions();
				close();
				Theater.get().host();
			} else if(hostIPBox.contains(Input.getMouse())) {
				if(!mousePress) {
					finalizeOptions();
					mousePress = true;
					toggleHostIP();
				}
			} else if(hostPortBox.contains(Input.getMouse())) {
				if(hostPort == null && !mousePress) {
					finalizeOptions();
					mousePress = true;
					enableHostPort();
				} else if(hostPort != null && !mousePress) {
					mousePress = true;
					disableHostPort();
				}
			} else if(exitBox.contains(Input.getMouse())) {
				close();
				Theater.get().close();
			}
		} else
			mousePress = false;
	}
	
	public void draw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glColor3f(0f, 0f, 0f);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(Theater.get().getScreen().camera.getWidth(), 0);
			GL11.glVertex2d(Theater.get().getScreen().camera.getWidth(), Theater.get().getScreen().camera.getHeight());
			GL11.glVertex2d(0, Theater.get().getScreen().camera.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glColor4f(bgColor.x, bgColor.y, bgColor.z, bgColor.w);
		
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
		
		logo.draw();
		
		Text.drawCenteredString("Join Game", (Theater.get().getScreen().displayWidth / 2),
				(float) joinBox.getY() + 2.5f, boxHover == 1 ? Color.white : Color.gray);
		if(joinIP != null) {
			joinIP.draw(Theater.get().getScreen().displayWidth / 2, 
					(float) (joinBox.getY() + 2.5f + joinBox.getHeight()));
		} else {
			Text.drawCenteredString(Config.joinIP, Theater.get().getScreen().displayWidth / 2,
					(float) (joinBox.getY() + 2.5f + joinBox.getHeight()), boxHover == 2 ? Color.white : Color.darkGray);
		}
		if(joinPort != null) {
			joinPort.draw(Theater.get().getScreen().displayWidth / 2, 
					(float) (joinBox.getY() + 2.5f + (joinBox.getHeight() * 2)));
		} else {
			Text.drawCenteredString(Config.joinPort + "", Theater.get().getScreen().displayWidth / 2,
					(float) (joinBox.getY() + 2.5f + (joinBox.getHeight() * 2)), boxHover == 3 ? Color.white : Color.darkGray);	
		}
		if(name != null) {
			name.draw(Theater.get().getScreen().displayWidth / 2, 
					(float) (joinBox.getY() + 2.5f + (joinBox.getHeight() * 3)));
		} else {
			Text.drawCenteredString(Config.joinName, Theater.get().getScreen().displayWidth / 2,
					(float) (joinBox.getY() + 2.5f + (joinBox.getHeight() * 3)), boxHover == 4 ? Color.white : Color.darkGray);	
		}
		Text.drawCenteredString("Host Game", Theater.get().getScreen().displayWidth / 2,
				(float) hostBox.getY() + 2.5f, boxHover == 5 ? Color.white : Color.gray);
		Text.drawCenteredString(hostIP ? "___._._.___ (Show)" : Theater.get().getHostIP() + " (Hide)", 
				Theater.get().getScreen().displayWidth / 2,
				(float) (hostBox.getY() + 2.5f + hostBox.getHeight()), boxHover == 6 ? Color.white : Color.darkGray);
		if(hostPort != null) {
			hostPort.draw(Theater.get().getScreen().displayWidth / 2, 
					(float) (hostBox.getY() + 2.5f + (hostBox.getHeight() * 2)));
		} else {
			Text.drawCenteredString(Config.hostPort + "", Theater.get().getScreen().displayWidth / 2,
					(float) (hostBox.getY() + 2.5f + (hostBox.getHeight() * 2)), boxHover == 7 ? Color.white : Color.darkGray);
		}
		Text.drawCenteredString("Exit", Theater.get().getScreen().displayWidth / 2,
				(float) exitBox.getY() + 2.5f, boxHover == 8 ? Color.white : Color.gray);
	}
	
	public void fade() {
		if(fader) {
			if(fadeWait < 5f) {
				fadeWait += Theater.getDeltaSpeed(0.025f);
				if(bgColor.w < 1f) {
					bgColor.w += Theater.getDeltaSpeed(0.025f);
					if(bgColor.w > 1f)
						bgColor.w = 1f;
				}
			} else {
				fadeWait = 2f;
				fader = false;
			}
		} else {
			if(fadeWait > 0f) {
				fadeWait -= Theater.getDeltaSpeed(0.025f);
				if(bgColor.w > 0f) {
					bgColor.w -= Theater.getDeltaSpeed(0.025f) / 2f;
					if(bgColor.w < 0f)
						bgColor.w = 0f;
				}
			} else {
				fadeWait = 0f;
				fader = true;
				switch((int)(Math.random() * 3)) {
				case(0):
					bgColor = new Vector4f(0.95f, 0.95f, 0.95f, 0f);
					break;
				case(1):
					bgColor = new Vector4f(0.85f, 0.1f, 0.25f, 0f);
					break;
				case(2):
					bgColor = new Vector4f(0.2f, 0.15f, 0.85f, 0f);
					break;
				}
			}
		}
	}
	
	public void enableJoinIP() {
		joinIP = new InputBox(false, Config.joinIP);
		disableJoinPort();
		disableName();
		disableHostPort();
	}
	
	public void enableJoinPort() {
		joinPort = new InputBox(true, Config.joinPort + "");
		disableJoinIP();
		disableName();
		disableHostPort();
	}
	
	public void enableName() {
		name = new InputBox(false, Config.joinName);
		disableJoinIP();
		disableJoinPort();
		disableHostPort();
	}
	
	public void toggleHostIP() {
		hostIP = !hostIP;
		Config.showHostIP = hostIP;
		disableJoinIP();
		disableJoinPort();
		disableName();
		disableHostPort();
	}
	
	public void enableHostPort() {
		hostPort = new InputBox(true, Config.hostPort + "");
		disableJoinIP();
		disableJoinPort();
		disableName();
	}
	
	public void disableJoinIP() {
		joinIP = null;
		newIP = null;
	}
	
	public void disableJoinPort() {
		joinPort = null;
		newPort = null;
	}
	
	public void disableName() {
		name = null;
		newName = null;
	}
	
	public void disableHostPort() {
		hostPort = null;
		newHostPort = null;
	}
	
	public boolean optionsClosed() {
		if(joinIP == null && joinPort == null && name == null && hostPort == null)
			return true;
		
		return false;
	}
	
	public void finalizeOptions() {
		if(joinIP != null) {
			Config.joinIP = joinIP.getText();
			disableJoinIP();
		} else if(joinPort != null) {
			Config.joinPort = Integer.parseInt(joinPort.getText());
			disableJoinPort();
		} else if(name != null) {
			Config.joinName = name.getText();
			disableName();
		} else if(hostPort != null) {
			Config.hostPort = Integer.parseInt(hostPort.getText());
			disableHostPort();
		}
	}
	
	public void open() {
		open = true;
	}
	
	public void close() {
		open = false;
	}
	
	public boolean isOpen() {
		return open;
	}
	
}
