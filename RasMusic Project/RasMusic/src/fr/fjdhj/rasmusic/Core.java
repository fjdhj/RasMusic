package fr.fjdhj.rasmusic;

import java.io.File;

import com.goxr3plus.streamplayer.stream.StreamPlayerException;

import fr.fjdhj.rasmusic.module.radio.SongManager;
import fr.fjdhj.rasmusic.templates.HTTPTemplates;
import fr.fjdhj.rasmusic.utils.XMLUtil;
import fr.fjdhj.rasmusic.webserv.HTTPMethod;
import fr.fjdhj.rasmusic.webserv.HTTPRequest;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;
import fr.fjdhj.rasmusic.webserv.HTTPStatusCode;
import fr.fjdhj.rasmusic.webserv.exception.Error404;

public class Core {

	private SongManager webRadio;
	private PlayerModule player;
	private static final String root = "ressource/";
	
	public Core() {
		webRadio = new SongManager(XMLUtil.loadRadioList());
		player = new PlayerModule(webRadio.getURL());
	}
	
	public HTTPResponse handleRequest(HTTPRequest request) {
		//By default, if no supported method is supplied : error 400 Bad Request
		HTTPResponse response;
		switch(request.getMethod()) {
		case GET:
		case HEAD:
			//Une requetes a l'API
			String fileRequested = request.getRequestUri();
			if(fileRequested.contains("/api/")) {
				//On envoie la commande
				String commande = fileRequested.substring(fileRequested.lastIndexOf("/"));
				String[] args = commande.split("-");
				response = execRequest(args);
			}else{
				File file;
				//Une demande de fichier
				if(fileRequested.equals("/")) {	
					//On envoie l'index
					file = new File(root + "index.html");
				}else {
					if(fileRequested.startsWith("/"))
						file = new File(root + fileRequested.substring(1));
					else
						file = new File(root + fileRequested);
				}
				
				if(file.exists()) {
					response = HTTPTemplates.uploadFile(file);

				}else {
					response = HTTPTemplates.error404();
					System.err.println("404 Error " + fileRequested);
				}
			}
			break;
		case POST:
			response = HTTPTemplates.error404();
			break;
		case PUT:
			response = HTTPTemplates.error404();
			break;
		default:
			response = HTTPTemplates.error400();
			break;
		}
		return response;
	}
	
	public HTTPResponse execRequest(String[] args) {
		HTTPResponse reponse = null;
		String debug = "";
		String request = args[0];
		for(String arg : args) {
			debug+= "  ||  "+arg;
		}
		System.out.println("REQUETE :" +  debug);
		switch(request) {
		case "/radiolist":// 		/radiolist
			reponse = HTTPTemplates.plainText(XMLUtil.loadRadioListAsXML());
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
			reponse = HTTPTemplates.plainText(webRadio.getImageURL().toString());

			break;
		case "/getname":
			reponse = HTTPTemplates.plainText(webRadio.getName());
			break;
		default:
			reponse = HTTPTemplates.error400();
			break;
		}
			
		
		return reponse;
	}
}
