package fr.fjdhj.rasmusic.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import fr.fjdhj.rasmusic.webserv.HTTPMethod;
import fr.fjdhj.rasmusic.webserv.HTTPRequest;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;
import fr.fjdhj.rasmusic.webserv.HTTPStatusCode;

public class HTTPUtil {
	/*
	 * Parse the request to build a HTTPRequest object.
	 * Returns null if the message can't be parsed
	 * */
	public static HTTPRequest parseRequest(ArrayList<String> lines, byte[] body) {
		System.out.println("[DEBUG]: On parse la requete.");
		String requestURI = "";
		String method = "" ;
		HashMap<String,String> headers = new HashMap<String,String>();
		
		for(int i=0;i<lines.size();i++) {
			String line = lines.get(i);
			if(i==0) {
				StringTokenizer parse = new StringTokenizer(line);
				method = parse.nextToken().toUpperCase(); //On récupère la méthode HTTP du client
				//On récupère le fichier demandé
				requestURI= parse.nextToken().toLowerCase();
			}else {
				try {
					String key = line.substring(0, line.lastIndexOf(":"));
					String value = line.substring(line.lastIndexOf(":")+2);
					headers.put(key, value);
				}catch(IndexOutOfBoundsException ex) {
					ex.printStackTrace();
				}
			}
		}

		return new HTTPRequest(parseMethod(method), requestURI, headers, body);
	}
	
	/*
	 * Parse the given code to build a HTTPStatusCode object
	 * */
	public static HTTPMethod parseMethod(String code) {
		HTTPMethod[] codes = HTTPMethod.values();
		HTTPMethod statusCode = codes[0];
		int i = 0;
		while(i <codes.length && statusCode.message != code) {
			i++;
		}
		if(i >= codes.length) statusCode =null;
		return statusCode;

	}
	
	/*
	 * Parse the given code to build a HTTPStatusCode object
	 * */
	public static HTTPStatusCode parseStatusCode(String code) {
		HTTPStatusCode[] codes = HTTPStatusCode.values();
		HTTPStatusCode statusCode = codes[0];
		int i = 0;
		while(i <codes.length && statusCode.message != code) {
			i++;
		}
		if(i >= codes.length) statusCode =null;
		return statusCode;
	}
	
	/*
	 * Serialize the HTTPRequest object through the given outputStream 
	 * */
	public static void sendHTTPRequest(HTTPRequest request, OutputStream out) throws IOException {
		BufferedOutputStream dataout = new BufferedOutputStream(out);
		dataout.write(request.getRequestAsByteArray());
		dataout.flush();
	}
	
	/*
	 * Serialize the HTTPResponse object through the given outputStream 
	 * */
	public static void sendHTTPResponse(HTTPResponse response, OutputStream out) throws IOException {
		BufferedOutputStream dataout = new BufferedOutputStream(out);
		dataout.write(response.getRequestAsByteArray());
		dataout.flush();
	}
	
}
