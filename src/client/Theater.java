package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import server.Server;
import client.menu.Chatlog;
import client.menu.TitleMenu;
import client.sounds.Ensemble;
import client.utility.Achievements;
import client.utility.Config;
import client.utility.SplashScreen;
import client.utility.keyboard.Keybinds;

public class Theater {

	// TODO Occasional card can't be selected?
	// Card grab priority?
	// Cards don't always rotate/resize
	
	private volatile Stage stage;
	private Screen screen;
	private SplashScreen splashScreen;
	private volatile Client client;
	private volatile Server server;

	private String hostIP = "127.0.0.1";
	
	private boolean playing;
	public boolean debug;
	
	private float delta;
	private float deltaMax = 25f;
	private long currentTime;
	private long lastLoopTime;
	public static int fps = 0;
	public static String version = "v1.0";
	
	private static Theater theater;
	
	public static void init() {
		theater = new Theater();
	}
	
	public static Theater get() {
		return theater;
	}
	
	public Theater() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); //you get the IP as a String
			hostIP = ip;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Config.loadConfig();
		Chatlog.init();
		
		screen = new Screen();
		stage = new Stage();
		if(Ensemble.get().getBackground() != null)
			Ensemble.get().getBackground().pause();
		splashScreen = new SplashScreen();
	}
	
	public void update() {
		getFps();
		
		if(splashScreen == null)
			Ensemble.get().update();
		
		if(splashScreen == null) {
			if(TitleMenu.get().isOpen()) {
				TitleMenu.get().draw();
				TitleMenu.get().update();
			} else
				screen.draw(stage);
		} else {
			splashScreen.draw();
			splashScreen.update();
		}
		screen.update();
		if(screen.resized())
			screen.resize();
		
		if(splashScreen == null)
			stage.act();
		
		Input.checkInput(stage);
				
		if(screen.toBeClosed()) {
			close();
		}
	}
	
	public void play() {
		//pause();
		currentTime = getTime();
		if(splashScreen != null) {
			while(splashScreen.isSplashing()) {
				update();
			}
			splashScreen = null;
		}
		
		if(Ensemble.get().getBackground() != null)
			Ensemble.get().getBackground().resume();
		
		TitleMenu.init();
		TitleMenu.get().open();
		while(TitleMenu.get().isOpen()) {
			update();
		}
		
		while(!client.isConnected()) {
			update();
			if(!client.isAlive()) {
				quitGame(1);
			}
		}
		
		playing = true;
		
		while(playing) {
			update();
		}
	}

	public void pause() {
		while(!Keybinds.X.clicked()) {
			getFps();
	
			screen.update();
	
			Input.checkInput(stage);
			
			if(screen.toBeClosed()) {
				if(!Achievements.QUITGAME.getGot() && splashScreen == null) {
					Achievements.QUITGAME.get();
					Achievements.QUITGAME.showGet(stage);
					float x = 0.5f;
					while(x > 0f && !Keybinds.X.clicked()) {
						screen.draw(stage);
						screen.update();
						stage.act();
						Input.checkInput(stage);
						x -= getDeltaSpeed(0.025f);
					}
				}
				close();
			}
		}
		Input.checkInput(stage);
	}
	
	public void join() {
		client = new Client(Config.joinIP, Config.joinPort, Config.joinName);
		client.start();
	}
	
	public void host() {
		server = new Server(Config.hostPort);
		server.start();
		
		client = new Client("localhost", Config.hostPort, Config.joinName);
		client.start();
	}

	public void quitGame(int arg) {
		if(arg == 2) {
			if(server != null)
				server.reset();
			
			if(client.isAlive())
				client.sendData("Kill");
		} else {
			if(client.isAlive())
				client.sendData("Kill");
			
			if(server != null && arg == 1) {
				server.terminate();
				try {
					server.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		client = null;
		server = null;
		stage = new Stage();
		//splashScreen = new SplashScreen();
		
		play();
	}
	
	public void close() {
		try {
			Config.saveConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		screen.close();
		System.exit(0);
	}
	
	public void getFps() {
		delta = getTime() - currentTime;
		currentTime = getTime();
		lastLoopTime += delta;
		fps++;
		if(lastLoopTime >= 1000) {
			screen.updateHeader();
			fps = 0;
			lastLoopTime = 0;
		}
	}
	
	public Screen getScreen() {
		return screen;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public Client getClient() {
		return client;
	}

	public String getHostIP() {
		return hostIP;
	}
	
	public static long getTime() {
		return System.nanoTime() / 1000000;
	}
	
	public static float getDeltaSpeed(float speed) {
		return (Theater.get().delta * speed) / Theater.get().deltaMax;
	}
	
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/natives");
		System.setProperty("resources", System.getProperty("user.dir") + "/resources");
		
		Theater.init();
		theater.play();
	}

}
