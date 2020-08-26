package fr.fjdhj.rasmusic;

import java.util.ArrayList;

import com.goxr3plus.streamplayer.stream.StreamPlayerException;

import fr.fjdhj.rasmusic.module.radio.Radio;
import fr.fjdhj.rasmusic.module.radio.WebRadio;
import fr.fjdhj.rasmusic.utils.XMLUtil;

public class Core {

	private WebRadio webRadio;
	private PlayerModule player;
	
	public Core() {
		ArrayList<Radio> liste = new ArrayList<Radio>();
		XMLUtil.loadRadioList(liste);
		webRadio = new WebRadio(liste);
		player = new PlayerModule(webRadio.getURL());
	}
	
	
	public String execRequest(String request, String[] args) {
		String reponse = "";
		System.out.println("REQUETE :" + request);
		switch(request) {
		case "/radiolist":// 		/radiolist
			reponse += XMLUtil.loadRadioListAsXML();
			break;
		case "/selectRadio":// 		/selectRadio X
			if(args.length>0) {
				int ID = Integer.parseInt(args[0]);
				webRadio.selectByID(ID);
			}
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
		case "/getimage":
			reponse+=webRadio.getImageURL();
			break;
		case "/getname":
			reponse+=webRadio.getName();
			break;
		}
			
		
		return reponse;
	}
}
