package fr.fjdhj.rasmusic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.comparator.DirectoryFileComparator;

import com.goxr3plus.streamplayer.stream.StreamPlayerException;

import fr.fjdhj.rasmusic.module.ModuleBase;
import fr.fjdhj.rasmusic.nativemodule.PlayerModuleInterface;
import fr.fjdhj.rasmusic.templates.HTTPTemplates;
import fr.fjdhj.rasmusic.utils.Directory;
import fr.fjdhj.rasmusic.webserv.HTTPRequest;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;

public class Core {

	private PlayerModuleInterface player;
	private static ArrayList<ModuleBase> moduleList;
	
	public Core() {
		//webRadio = new SongManager(XMLUtil.loadRadioList());
		//Loading native Module
		player = new PlayerModuleInterface();
		
		//Loading module in module file
		moduleList = ModuleLoader.loadModule(player);
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
				//Si jamais il n'y a pas /api/ ou / dans le reste de la requetes, il y a une erreur de type java.lang.StringIndexOutOfBoundsException
				String module = fileRequested.substring(fileRequested.lastIndexOf("/api/")+5, fileRequested.lastIndexOf("/"));
				String[] args = fileRequested.substring(fileRequested.lastIndexOf("/")+1).split("-");
				response = execGETRequest(module, args);
			}else{
				File file;
				//Une demande de fichier
				if(fileRequested.equals("/")) {	
					//On envoie l'index
					file = new File(RasMusic.SERVER_ROOT + "index.html");
				}else {
					if(fileRequested.startsWith("/"))
						file = new File(RasMusic.SERVER_ROOT + fileRequested.substring(1));
					else
						file = new File(RasMusic.SERVER_ROOT + fileRequested);
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
	
	public HTTPResponse execGETRequest(String module, String[] args) {
		HTTPResponse reponse = null;
		String debug = "";
		for(String arg : args) {
			debug+= "  ||  "+arg;
		}
		System.out.println("MODULE NAME : "+module);
		System.out.println("REQUETE : " +  debug);
		if(module.equals("nativemodule"))
			return nativeHTTPGETRequest(args);
		else
			for(ModuleBase base : moduleList) {
				if(base.getModuleName().equals(module)) {
					reponse = base.GETRequest(args);
					break;
				}
			}
		return reponse;
	}
	
	private HTTPResponse nativeHTTPGETRequest(String[] args) {
		try {
			return switch(args[0]) {
					case "isplaying" -> HTTPTemplates.plainText(String.valueOf(player.isPlaying()));
					case "getname" -> HTTPTemplates.plainText(player.getName());
					case "getimage" -> HTTPTemplates.plainText(player.getImageURL());
					case "play" -> {
						if(player.isPaused())
							player.resume();
						else
							player.play();
						yield HTTPTemplates.ok200(); 
					}
					case "pause" -> {
						player.pause();
						yield HTTPTemplates.ok200();
					}
					case "stop" -> {
						player.stopCurrentStream();
						yield HTTPTemplates.ok200();
					}
					default -> HTTPTemplates.error400();
			};
		}catch(Exception e) {
			System.err.println(e.toString());
			return HTTPTemplates.error500(); 
		}
	}
	
	/**
	 * This function will generate a new index.html
	 */
	public void mainPageReload() {
		System.out.println("RELOAD MAIN PAGE");
		String mainPage = "";
		BufferedReader readerMain = null;
		BufferedReader readerModule = null;
		File nativeModule = new File(RasMusic.DATA_FOLDER_PATH+"NativeModule");
		
		List<File> jsScripts = Directory.getAllFiles(new File(RasMusic.SERVER_ROOT), ".js");
		List<File> cssScripts = Directory.getAllFiles(new File(RasMusic.SERVER_ROOT), ".css");
		
		try {
			readerMain = new BufferedReader(new FileReader(RasMusic.HTML_MAIN_BLANC_FILE));
			String tampon;
			while(!(tampon = readerMain.readLine()).equals("<!--JSSCRIPTLOAD-->"))
				mainPage += tampon+"\n";
			
			//Adding the js file to the main page
			for(File script : jsScripts)
				mainPage += "<script src=\"" + script.getPath().substring(script.getPath().indexOf(RasMusic.SERVER_ROOT)+12) + "\"></script>\n";
			
			//Adding the css file to the main page
			for(File css : cssScripts)
				mainPage += "<link rel=\"stylesheet\" href=\""+ css.getPath().substring(css.getPath().indexOf(RasMusic.SERVER_ROOT)+12) +"\">\n";
			
			while(!(tampon = readerMain.readLine()).equals("<!--SEPARATOR-->"))
				mainPage += tampon+"\n";
			
			for(File file : Directory.getAllFiles(nativeModule, ".html")) {
				System.out.println(file.getAbsolutePath());
				readerModule = new BufferedReader(new FileReader(file.getAbsolutePath()));
				while((tampon = readerModule.readLine()) != null) {
					mainPage += tampon+"\n";
				}
				readerModule.close();
			}
			
			for(ModuleBase module : moduleList) {
				readerModule = new BufferedReader(new FileReader(RasMusic.DATA_FOLDER_PATH+module.getModuleName()+"/main.html"));
				while((tampon = readerModule.readLine()) != null) {
					mainPage += tampon+"\n";
				}
				readerModule.close();
			}
				
			while((tampon = readerMain.readLine()) != null)
				mainPage += tampon+"\n";	
			
		} catch (FileNotFoundException e1) {
			System.err.println("Unable to generate the main page : the file "+RasMusic.HTML_MAIN_BLANC_FILE.getAbsolutePath()+" wasn't found.");
			e1.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error when read a file");
			e.printStackTrace();
		}finally {
			try {
				if(readerModule != null)
					readerModule.close(); //If already close, nothing append, here only if a error occurred.
				if(readerMain != null)
					readerMain.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(RasMusic.SERVER_ROOT + "index.html"));
			writer.write(mainPage);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
