package fr.fjdhj.rasmusic;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class PlayerModule {

	private AdvancedPlayer player;
	private volatile Thread playerWorker;
	private URL streamURL;
	
	private int pausedOnFrame = 0;
	
	public PlayerModule(URL defaultURL) {
		streamURL = defaultURL;
		playerWorker = new PlayerWorkerThread("PlayerWorker");
		reset();
	}
	
	public void play(){
		if(playerWorker==null || !playerWorker.isAlive()) {
			reset();
			playerWorker = new PlayerWorkerThread("PlayerWorker"); 
			playerWorker.start();
		}
	}
	
	public void pause() {
		player.stop();
	}
	
	public void setURL(URL url) {
		streamURL = url;
		reset();
	}
	
	public void reset() {
		try {
			URLConnection uc = streamURL.openConnection();
			DataInputStream in = new DataInputStream(uc.getInputStream());
			player = new AdvancedPlayer(in);
			player.setPlayBackListener(new PlaybackListener() {
				@Override
				public void playbackFinished(PlaybackEvent event) {
					pausedOnFrame = event.getFrame();
				}
			});
			pausedOnFrame = 0;
		} catch (JavaLayerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class PlayerWorkerThread extends Thread{
		public PlayerWorkerThread(String string) {
			super(string);
		}

		@Override
		public void run() {
			try {
				if(pausedOnFrame>0) {
					player.play(pausedOnFrame,Integer.MAX_VALUE);
				}else {
					player.play();
				}
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}
	}
}
