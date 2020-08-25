package fr.fjdhj.rasmusic;

import java.util.ArrayList;

import fr.fjdhj.rasmusic.module.radio.Radio;
import fr.fjdhj.rasmusic.module.radio.WebRadio;
import fr.fjdhj.rasmusic.utils.XMLUtil;

public class Core {

	private WebRadio webRadioPlayer;
	
	public Core() {
		ArrayList<Radio> liste = new ArrayList<Radio>();
		XMLUtil.loadRadioList(liste);
		webRadioPlayer = new WebRadio(liste);
	}
	
	
	public String execRequest(String request, String[] args) {
		String reponse = "";
		System.out.println("REQUETE :" + request);
		switch(request) {
		case "/radiolist":
			reponse += XMLUtil.loadRadioListAsXML();
			break;
		case "/selectRadio":
			break;
		case "/play" :
			break;
		case "/pause" :
			break;
		}
			
		
		return reponse;
	}
}
