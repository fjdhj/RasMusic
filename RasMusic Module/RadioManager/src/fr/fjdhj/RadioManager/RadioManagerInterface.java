package fr.fjdhj.RadioManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;

import fr.fjdhj.rasmusic.module.ModuleMusic;
import fr.fjdhj.rasmusic.nativemodule.PlayerModuleInterface;
import fr.fjdhj.rasmusic.nativemodule.exception.PlayerModuleInterfaceException;
import fr.fjdhj.rasmusic.templates.HTTPTemplates;
import fr.fjdhj.rasmusic.webserv.HTTPMimeType;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;

public class RadioManagerInterface implements ModuleMusic {

	private PlayerModuleInterface player;
	private SongManager songManager;
	public static final String MODULE_NAME = "RadioManager";
	
	@Override
	public String getModuleName() {
		return MODULE_NAME;
	}

	@Override
	public int getModuleVersion() {
		return 0;
	}

	@Override
	public boolean init(PlayerModuleInterface arg0) {
		player = arg0;
		songManager = new SongManager(XMLUtil.loadRadioList());
		return true;
	}

	@Override
	public HTTPResponse GETRequest(String[] arg0) {
		String request = arg0[0];
		HTTPResponse reponse;
		switch(request) {
		case "radiolist":
			reponse = HTTPTemplates.plainText(XMLUtil.loadRadioListAsXML());
			reponse.addHeader("Content-Type", HTTPMimeType.xml.MIMEType);
			break;
			
		case "selectRadio":
			//The first arg is the id of the radio
			songManager.selectByID(arg0[1]);
			boolean wasplaying = player.isPlaying();
			try {
				BufferedInputStream in = new BufferedInputStream(songManager.getURL().openStream());
				player.openStream(in);			
				player.play();
				player.setImageURL(songManager.getImageURL().toString());
				player.setName(songManager.getName());				
				reponse = HTTPTemplates.ok200();
			}catch (PlayerModuleInterfaceException e) {
				reponse = HTTPTemplates.error500();
				System.err.println(e.getMessage());
			} catch (IOException e) {
				reponse = HTTPTemplates.error500();
				e.printStackTrace();
			}
			break;

		default:
			reponse = HTTPTemplates.error404();
			break;
			
		}
		return reponse;

	}
	




}
