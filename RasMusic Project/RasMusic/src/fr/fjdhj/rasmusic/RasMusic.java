package fr.fjdhj.rasmusic;

import java.io.File;
import java.util.ArrayList;

import fr.fjdhj.rasmusic.module.ModuleBase;
import fr.fjdhj.rasmusic.webserv.Server;

public class RasMusic {
	

	public static final int PORT_USE = 1886;
	public static final String ROOT_FOLDER = ".";
	public static final String DATA_FOLDER_PATH = "data/";
	public static final String MODULE_FOLDER_PATH = "module/";
	public static final String SERVER_ROOT = "ressources/";
	public static final File HTML_MAIN_BLANC_FILE = new File(DATA_FOLDER_PATH+"HTMLMainDefault.html");
	
	public static void main(String[] args) {

		//Creating the serveur core:
		Core core = new Core();
		core.mainPageReload();
		Server serv = new Server(core);		
		
	}

}
