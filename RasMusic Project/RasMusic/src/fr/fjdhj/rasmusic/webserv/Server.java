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
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;


import fr.fjdhj.rasmusic.Core;
import fr.fjdhj.rasmusic.RasMusic;
import fr.fjdhj.rasmusic.webserv.exception.Error404;

public class Server{
	
	private ServerSocket server;
	
	private BufferedReader in = null;
	private PrintWriter out = null;
	private BufferedOutputStream dataOut = null;
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
							System.out.println(line);
						}

						
						StringTokenizer parse = new StringTokenizer(headers.get(0));
						String methods = parse.nextToken().toUpperCase(); //On récupère la méthode HTTP du client
						//On récupère le fichier demandé
						fileRequested= parse.nextToken().toLowerCase();
						
						//Le serveur ne supporte que les methods GET et HEAD, on verifie :
						if(methods.equals("GET") || methods.equals("HEAD")) {
							
							File file;
							String MIMEtype;
							int fileLength;
							
							//Une requetes a l'API
							if(fileRequested.contains("/api/")) {
								//On envoie la commande
								String resp = core.execRequest(fileRequested.substring(fileRequested.lastIndexOf("/")), null);
								byte[] data = resp.getBytes();
								GET_HEADmethod("200 OK", "text/plain", data.length, true, data);
							}else{
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
									GET_HEADmethod("200 OK", file);

								}else {
									GET_HEADmethod(new Error404(fileRequested).getErrorCode(), "", 0, false, null);
									System.err.println("404 Error " + fileRequested);
								}
							}
							
						}else if(methods.equals("POST")) {
							int len = 1;
							for(String l : headers) 
								if(l.contains("Content-Length:"))
									len = Integer.parseInt(l.substring(l.lastIndexOf(":")+2));
							
							BufferedInputStream dataIn = new BufferedInputStream(client.getInputStream());
							System.out.println(len);
							byte[] buf = new byte[len];
							System.out.println("ok");
							System.out.println(dataIn.available());
							dataIn.read(buf);
							System.out.println("...");
							System.out.println(buf);
							System.out.print(new String(buf));
							
							System.out.println("ok2");
						}
						
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
			
			String MIMEtype = MIMEtype(file.toString());
			int len = (int) file.length();
			
			byte[] data = readFileData(file);
			
			return GET_HEADmethod(HTTPstatus, MIMEtype, len, true, data);
	}
	
	private byte[]readFileData(File file) throws IOException, FileNotFoundException{
		FileInputStream fileIn = null;
		byte[] data = new byte[(int) file.length()];
			fileIn = new FileInputStream(file);
			fileIn.read(data);
			if(fileIn != null)
				try {
					fileIn.close();
				} catch (IOException e) {e.printStackTrace();}
		
		
		return data;
		
	}
	
	//Renvoie le type MIME du fichier (text, vidéo, image, ...)
	private String MIMEtype(String fileRequested) {
		switch(fileRequested.substring(fileRequested.lastIndexOf("."))) {
		case ".html":
			return "text/html";
		case ".css":
			return "text/css";
		case ".js":
			return "text/javascript";
		case ".svg":
			return "image/svg+xml";
		default:
			return "text/plain";
		}
	}
}
