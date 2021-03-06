package fr.fjdhj.rasmusic.webserv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import fr.fjdhj.rasmusic.Core;
import fr.fjdhj.rasmusic.RasMusic;
import fr.fjdhj.rasmusic.utils.HTTPUtil;

public class Server{
	
	private ServerSocket server;
	
	private BufferedReader in = null;
	private Socket client;
	
	private final Core core;
	
	private boolean servRun = true;
	
	private String fileRequested;
	
	private String root = "ressource/";
	
	public Server(final Core core) {
		this.core = core;
		System.out.println("Démarage du serveur ...");
		Thread servMain = new Thread(new Runnable(){
			public void run(){
				//Ouverture du serv
				try {
					server = new ServerSocket(RasMusic.PORT_USE);
				} catch (IOException e1) {e1.printStackTrace();}
				System.out.println("Server start on port "+RasMusic.PORT_USE);
				while(servRun){
					try {
						client = server.accept();
						System.out.println("Nouvelle connexion");
						
						//Quelq'un c'est connecté, on ouvre les flux d'écriture et de lecture
				
						//On ouvre le flux de lecture
						in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						
						//On lit le flux et on parse la requetes
						String line = "";
						ArrayList<String> headers = new ArrayList<String>();
						while(!(line=in.readLine()).isEmpty()) {
							headers.add(line);
						}
						
						HTTPRequest request = HTTPUtil.parseRequest(headers, null);
						HTTPResponse reponse = core.handleRequest(request);
						HTTPUtil.sendHTTPResponse(reponse, client.getOutputStream());
						
					} catch (Exception e) {
							e.printStackTrace();
						}finally {
						try {
							System.out.println("Fermeture des flux");
							System.out.println("-----------------------------------");
							in.close();
							client.close();
						} catch (IOException e) {e.printStackTrace();}
						
					}
					
					
				}
				
			}
				
		});
		servMain.setName("servMain");
		servMain.start();
	}
}