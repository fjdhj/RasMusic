package fr.fjdhj.rasmusic;

import java.util.ArrayList;

import com.goxr3plus.streamplayer.stream.StreamPlayerException;

import fr.fjdhj.rasmusic.module.radio.Radio;
import fr.fjdhj.rasmusic.module.radio.SongManager;
import fr.fjdhj.rasmusic.utils.XMLUtil;

public class Core {

	private SongManager webRadio;
	private PlayerModule player;
	
	public Core() {
		ArrayList<Radio> liste = new ArrayList<Radio>();
		XMLUtil.loadRadioList(liste);
		webRadio = new SongManager(liste);
		player = new PlayerModule(webRadio.getURL());
	}
	
	
	public String execRequest(String request, String[] args) {
		String reponse = "";
		System.out.println("REQUETE :" + request);
		switch(request) {
		case "/radiolist":// 		/radiolist
			reponse += XMLUtil.loadRadioListAsXML();
			break;
		case "/play" ://	 /play
			try {
				if(player.isPaused()) {
					player.resume();
				}else {
					player.play();
				}
			} catch (StreamPlayerException e) {
				e.printStackTrace();
			}
			break;
		case "/pause" ://		/pause
			player.pause();
			break;
			
		case "/selectRadio":// 		/selectRadio X
			selectRadio(args);
			break;
		}
			
		
		return reponse;
	}
	
	private String selectRadio(String[] args) {
		String rep = "";
		if(args.length>0) {
			try{
				int ID = Integer.parseInt(args[0]);
				webRadio.selectByID(ID);
			}catch(NumberFormatException ex) {
				System.err.println("[ERROR] Bad request : Radio selection invalid argument");
			}
		}else {
			System.err.println("[ERROR] Bad request : Radio selection without argument");
		}
		return rep;
	}
}
