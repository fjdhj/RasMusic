package fr.fjdhj.rasmusic.module.radio;

import java.net.URL;
import java.util.ArrayList;

public class WebRadio{
	
	private ArrayList<Radio> radioList = new ArrayList<Radio>();
	
	private Radio currentRadio;
	
	public WebRadio(ArrayList<Radio> liste) {
		radioList = liste;
	}

	public URL getURL() {return currentRadio.getUrl();}
	
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
}
