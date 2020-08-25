package fr.fjdhj.rasmusic;

import fr.fjdhj.rasmusic.webserv.Server;

public class RasMusic {

	public static final int PORT_USE = 1844;
	public static final String ROOT_FOLDER = ".";
	
	public static void main(String[] args) {
		//Creating the serveur :
		Core core = new Core();
		Server serv = new Server(core);  		
		//Test API Apple music
		/*AppleMusicUser user = new AppleMusicUser();
		user.login();*/
		
		
	}

}
