package fr.fjdhj.rasmusic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.google.common.io.Files;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;

import fr.fjdhj.rasmusic.module.radio.Radio;
import fr.fjdhj.rasmusic.module.radio.SongManager;
import fr.fjdhj.rasmusic.templates.HTTPTemplates;
import fr.fjdhj.rasmusic.utils.HTTPUtil;
import fr.fjdhj.rasmusic.utils.XMLUtil;
import fr.fjdhj.rasmusic.webserv.HTTPMimeType;
import fr.fjdhj.rasmusic.webserv.HTTPRequest;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;

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
				response = execRequest(request);
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
			response = post(request);
			break;
		case PUT:
			response = put(request);
			break;
		default:
			response = HTTPTemplates.error400();
			break;
		}
		return response;
	}
	
	private HTTPResponse post(HTTPRequest request) {
		String URI = request.getRequestUri();
		File file = new File(root + URI);
		String rawMIME = request.getHeaderValue("Content-Type");
		if(rawMIME == null) {return HTTPTemplates.error400();}
		HTTPMimeType mime = HTTPUtil.parseMimeType(rawMIME);
			switch(mime) {
			case jar:
				break;
			case text:
				break;
			case xml:
				/*TODO Refactor pour pouvoir supporter d'autre opérations (et nettoyer-optimiser tout ça)
				*Pour la liste des radio, à voir si ajouter la radio dans le SongManager
				*puis d'écrire la liste à partir du SongManager est plus pratique. On ne 
				*devrait pas gerer d'IO ici. De plus cela empecherait tout doublon.
				*Pour l'instant c'est sale mais ça fonctionne.
				*Le serveur ne vérifie pas les données reçues donc si le client envoie n'importe 
				*quoi, la base de donnée des radio est corrompue. 
				*/
				
				System.out.println(file.getAbsolutePath() + " exists= " + file.exists());
				System.out.println("Request body length : " + request.getContentLength());
				if(file.exists()) {
					//On insere les données xml à l'avant dernière ligne.
					File temp = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf("."))+".temp");
					PrintWriter writer = null;
					BufferedReader reader = null;
					try {
						writer = new PrintWriter(temp);
						reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
						String current="";
						String lastline=reader.readLine();
						while(lastline!=null) {
							current=reader.readLine();
							if(current == null && lastline !=null) {
								writer.println(new String(request.getBody()));
							}
							writer.println(lastline);
							lastline = current;
						}
						Files.move(temp, file);
					} catch (IOException e) {
						e.printStackTrace();
						writer.close();
						try {
							reader.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						return HTTPTemplates.error400();
					}finally {
						writer.close();
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					webRadio.setRadioList(XMLUtil.loadRadioList());
					return HTTPTemplates.ok200();
				}else {
					return HTTPTemplates.error400();
				}
			}
		return null;
	}
	
	private HTTPResponse put(HTTPRequest request) {
		
		return null;
	}

	public HTTPResponse execRequest(HTTPRequest request) {
		HTTPResponse reponse = null;
		String requestURI = request.getRequestUri();
		String[] args = requestURI.substring(requestURI.lastIndexOf("/")).split("-");
		String debug = "";
		String endpoint = args[0];
		for(String arg : args) {
			debug+= "  ||  "+arg;
		}
		System.out.println("REQUETE :" +  debug);
		switch(endpoint) {
		case "/isplaying":
			reponse = HTTPTemplates.plainText(String.valueOf(player.isPlaying()));
			break;
		case "/radiolist":// 		/radiolist
			reponse = HTTPTemplates.plainText(XMLUtil.loadRadioListAsXML());
			reponse.addHeader("Content-Type", HTTPMimeType.xml.MIMEType);

			break;
		case "/selectRadio":// 		/selectRadio X
			webRadio.selectByID(args[1]);
			boolean wasplaying = player.isPlaying();
			player.stop();
			player = new PlayerModule(webRadio.getURL());
			if(wasplaying) {
				try {
					player.play();
				} catch (StreamPlayerException e) {
					reponse = HTTPTemplates.error500();
					e.printStackTrace();
				}
			}
			reponse = HTTPTemplates.ok200();
			break;
		case "/play" ://	 /play
			try {
				if(player.isPaused()) {
					player.resume();
				}else {
					player.play();
				}
				reponse = HTTPTemplates.ok200();
			} catch (StreamPlayerException e) {
				reponse = HTTPTemplates.error500();
				e.printStackTrace();
			}
			break;
		case "/pause" ://		/pause
			player.pause();
			reponse = HTTPTemplates.ok200();
			break;

		case "/stop":
			player.stop();
			reponse = HTTPTemplates.ok200();
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
