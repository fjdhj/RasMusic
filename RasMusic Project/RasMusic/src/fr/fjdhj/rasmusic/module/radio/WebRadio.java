package fr.fjdhj.rasmusic.module.radio;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class WebRadio{
	
	private ArrayList<Radio> radioList = new ArrayList<Radio>();
	
	private URL URL;
	private Socket soc;
	private AdvancedPlayer player;
	private Thread soundThread;
	private Radio currentRadio;
	
	public WebRadio(ArrayList<Radio> liste) {
		radioList = liste;
		URL = liste.get(0).getUrl();
		create();
	}
	public WebRadio(URL url) {
		URL= url;
		create();
	}
	
	private void create() {
		try {
			URLConnection uc = URL.openConnection();
			DataInputStream in = new DataInputStream(uc.getInputStream());
			player = new AdvancedPlayer(in);
		} catch (IOException e) {e.printStackTrace();} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		try {
			System.out.println("On joue !");
			player.play();
		} catch (JavaLayerException e) {e.printStackTrace();}}
	public void pause() {player.stop();}
	public URL getURL() {return URL;}
	
	public void setRadioList(ArrayList<Radio> liste) {radioList = liste;}
	/**
	 * @param ID L'id de la radio
	 * Selectionne une radio. Pour la jouer il est necessaire d'appeler play()
	 */
	public void selectByID(int ID) {
		if(0 <= ID && ID < radioList.size()) {
			currentRadio = radioList.get(ID);
		}
		
	}
	
	class SoundThread implements Runnable{
		@Override
		public void run() {
			
		}
	}
	
}
