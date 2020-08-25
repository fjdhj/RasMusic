package fr.fjdhj.rasmusic;

import java.net.URL;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class PlayerModule {

	private AdvancedPlayer player;
	private Thread playerWorker;
	private URL streamURL;
	
	private boolean songStarted;
	
	public PlayerModule(URL defaultURL) {
		songStarted = false;
		streamURL = defaultURL;
	}
	
	public void play(){
	
	}
	
	public void pause() {
		
	}
	
	public void setURL() {
		
	}
	
	class PlayerWorkerThread implements Runnable{
		@Override
		public void run() {
			
		}
	}
}
