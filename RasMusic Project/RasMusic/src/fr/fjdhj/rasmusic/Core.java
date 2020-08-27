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
		webRadio = new SongManager(XMLUtil.loadRadioList());
		player = new PlayerModule(webRadio.getURL());
	}
	
	
	public String execRequest(String[] args) {
		String reponse = "";
		String debug = "";
		String request = args[0];
		for(String arg : args) {
			debug+= "  ||  "+arg;
		}
		System.out.println("REQUETE :" +  debug);
		switch(request) {
		case "/radiolist":// 		/radiolist
			reponse += XMLUtil.loadRadioListAsXML();
			break;
		case "/selectRadio":// 		/selectRadio X
			webRadio.selectByID(args[1]);
			player.stop();
			player = new PlayerModule(webRadio.getURL());
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

		case "/stop":
			player.stop();
			break;
		case "/getimage":
			reponse+=webRadio.getImageURL();
			break;
		case "/getname":
			reponse+=webRadio.getName();
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
