package fr.fjdhj.rasmusic.webserv;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import fr.fjdhj.rasmusic.Core;
import fr.fjdhj.rasmusic.RasMusic;
import fr.fjdhj.rasmusic.templates.HTTPTemplates;
import fr.fjdhj.rasmusic.utils.HTTPUtil;

public class Server{
	
	private ServerSocket server;
	
	BufferedInputStream in;
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
				while(servRun){
					try {
						
						
						client = server.accept();
						System.out.println("Nouvelle connexion");
						
						//Quelqu'un s'est connecté, on ouvre les flux d'écriture et de lecture
				
						in = new BufferedInputStream(client.getInputStream());
						HTTPRequest request = HTTPUtil.parseRequest(in);
						//On traite la requete
						HTTPResponse reponse;
						if(request != null) {
							reponse = core.handleRequest(request);
						}else {
							reponse = HTTPTemplates.error400();
						}
						//On corrige la réponse si besoin
						if(reponse ==null) {
							reponse = HTTPTemplates.error500();
						}
						HTTPUtil.sendHTTPResponse(reponse, client.getOutputStream());
						
					} catch (Exception e) {
							e.printStackTrace();
						}finally {
						try {
							System.out.println("Fermeture des flux");
							System.out.println("-----------------------------------");
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