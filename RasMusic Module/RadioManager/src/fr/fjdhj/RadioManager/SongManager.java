package fr.fjdhj.RadioManager;

import java.net.URL;
import java.util.HashMap;

public class SongManager{
	
	private HashMap<String,Radio> radioList = new HashMap<String,Radio>();
	
	private Radio currentRadio;
	
	public SongManager(HashMap<String,Radio> liste) {
		radioList = liste;
		currentRadio = radioList.get((String) radioList.keySet().toArray()[0]);
	}

	public URL getURL() {return currentRadio.getUrl();}
	public URL getImageURL() {return currentRadio.getIcon();}
	public String getName() {return currentRadio.getName();}
	
	public void setRadioList(HashMap<String,Radio> liste) {radioList = liste;}
	/**
	 * @param ID L'id de la radio
	 * Selectionne une radio. Pour la jouer il est necessaire d'appeler play()
	 */
	public void selectByID(String ID) {
		if(radioList.containsKey(ID)) {
			System.out.println(ID+" selectionné");
			currentRadio = radioList.get(ID);
		}else {
			System.out.println("La clé : " +ID+" n'existe pas");
		}
	}
}
