package fr.fjdhj.rasmusic.webserv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import fr.fjdhj.rasmusic.Core;
import fr.fjdhj.rasmusic.RasMusic;
import fr.fjdhj.rasmusic.utils.HTTPUtil;
import fr.fjdhj.rasmusic.webserv.exception.Error404;

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
						
					} catch (IOException e) {
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
	/*
	private boolean GET_HEADmethod(String HTTPstatus, String MIMEtype, int len, boolean body, byte[] bodyContent) throws IOException {
		try {
			//On ouvres les flux d'ecriture
			out = new PrintWriter(client.getOutputStream());
			dataOut = new BufferedOutputStream(client.getOutputStream());
		
			out.println("HTTP/1.1 "+HTTPstatus);
			out.println("Server: Java HTTP server for RasMus");
			out.println("Date: " + new Date());
			//On regarde si on a un fichier a envoyer
			if(body) {
				out.println("Content-type: "+MIMEtype);
				out.println("Content-lenght: "+len);
			}
			
			out.println();
			out.flush();
		
			if(body) {
				dataOut.write(bodyContent, 0, len);
				dataOut.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			out.close();
			dataOut.close();
		}
		return true;
		
	}
	
	private boolean GET_HEADmethod(String HTTPstatus, File file) throws IOException {
			
			System.out.println(client.getInetAddress() +" need "+ file);
			System.out.println("Methods : GET_HEAD\n");
			
			String MIMEtype = HTTPUtil.MIMEtype(file.toString());
			int len = (int) file.length();
			
			byte[] data = HTTPUtil.readFileData(file);
			
			return GET_HEADmethod(HTTPstatus, MIMEtype, len, true, data);
	}*/
}
