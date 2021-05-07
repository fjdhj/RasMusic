package fr.fjdhj.rasmusic.nativemodule;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URL;

import com.goxr3plus.streamplayer.enums.Status;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;

import fr.fjdhj.rasmusic.RasMusic;
import fr.fjdhj.rasmusic.nativemodule.exception.PlayerModuleInterfaceException;

public class PlayerModuleInterface {

	private PlayerModule player;
	private String imageURL;
	private String MediaName;
	
	/**
	 * The default constructor
	 */
	public PlayerModuleInterface() {
		player =  new PlayerModule();
		imageURL = null;
		MediaName = "";
	}
	
	/**
	 * Pause the current song
	 */
	public void pause() {
		player.pause();
	}
	
	/**
	 * Resume the current song
	 */
	public void resume() {
		player.resume();
	}

	/**
	 * Play the current stream
	 * @throws PlayerModuleInterfaceException 
	 */
	public void play() throws PlayerModuleInterfaceException {
		try {
			player.play();
		} catch (StreamPlayerException e) {
			e.printStackTrace();
			throw new PlayerModuleInterfaceException("StreamPlayerException, unable to open the stream");
		}
	}
	/**
	 * Check if the player is playing 
	 * @return true if the player id playing, false if is not
	 */
	public boolean isPlaying() {
		return player.isPlaying();
	}
	
	/**
	 * Check if the player is pausing 
	 * @return true if the player id pausing, false if is not
	 */
	public boolean isPaused() {
		return player.isPaused();
	}
	
	/**
	 * Stops the play back and free Audio resources
	 */
	public void stopCurrentStream() {
		player.stop();
	}
	
	/**
	 * Open a new stream for playback, if a stream is already open, close it.
	 * @param input the stream to be player
	 * @throws PlayerModuleInterfaceException 
	 */
	public void openStream(BufferedInputStream input) throws PlayerModuleInterfaceException {
		if(!player.getStatus().equals(Status.STOPPED))
			stopCurrentStream();
		try {
			player.open(input);
		} catch (StreamPlayerException e) {
			e.printStackTrace();
			throw new PlayerModuleInterfaceException("StreamPlayerException, unable to open the stream");
		}
	}
	
	/**
	 * Open a new stream for playback, if a stream is already open, close it.
	 * @param input the file to be played
	 * @return false if an error occur
	 */
	public boolean openStream(File input) {
		if(!player.getStatus().equals(Status.STOPPED))
			stopCurrentStream();
		try {
			player.open(input);
		} catch (StreamPlayerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Open a new stream for playback, if a stream is already open, close it.
	 * @param input the URL to be played
	 * @return false if an error occur
	 */
	public boolean openStream(URL input) {
		if(!player.getStatus().equals(Status.STOPPED))
			stopCurrentStream();
		try {
			player.open(input);
		} catch (StreamPlayerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Give the image's url of the media. If not image available, return the noimage.svg url.
	 * @return the image's url of the media
	 */
	public String getImageURL() {
		if(imageURL == null)
			return "noimage.svg";
		else
			return imageURL;
	}
	
	/**
	 * Set a new image for the current media play
	 * @param url the new url of the image
	 */
	public void setImageURL(String url) {
		imageURL = url;
	}
	
	/**
	 * Give the name of the media.
	 * @return the name of the media
	 */
	public String getName() {
		return MediaName;
	}
	
	/**
	 * Set a new name for the current media.
	 * @param name the new name of the media
	 */
	public void setName(String name) {
		MediaName = name;		
	}
}
