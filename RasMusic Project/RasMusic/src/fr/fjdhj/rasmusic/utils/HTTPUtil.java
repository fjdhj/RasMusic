package fr.fjdhj.rasmusic.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.google.api.client.util.Charsets;

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
		String requestURI = null;
		String method = null;
		HashMap<String,String> headers = new HashMap<String,String>();
		
		for(int i=0;i<lines.size();i++) {
			String line = lines.get(i);
			if(i==0) {
				StringTokenizer parse = new StringTokenizer(line);
				method = parse.nextToken().toUpperCase(); 
				try {
					requestURI= URLDecoder.decode(parse.nextToken().toLowerCase(),Charsets.UTF_8.name());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
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
		System.out.println("[DEBUG] : method: " + method + " requestURI: " + requestURI + "");
		HTTPMethod methodObj = parseMethod(method);
		if(methodObj == null || requestURI==null) {
			return null;
		}else {
			return new HTTPRequest(methodObj, requestURI, headers, body);
		}
	}
	
	/*
	 * Parse the given code to build a HTTPStatusCode object
	 * */
	public static HTTPMethod parseMethod(String code) {
		HTTPMethod[] codes = HTTPMethod.values();

		HTTPMethod statusCode = codes[0];
		for(int i=1;i<codes.length;i++) {
			if(codes[i].message.equals(code)) {
				statusCode = codes[i];
			}
		}

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
			statusCode = codes[i];
			i++;
		}
		if(i >= codes.length) statusCode =null;
		return statusCode;
	}
	
	/*
	 * Serialize the HTTPRequest object through the given outputStream 
	 * */
	public static void sendHTTPRequest(HTTPRequest request, OutputStream out) throws IOException {
		if(request != null && out != null) {
			BufferedOutputStream dataout = new BufferedOutputStream(out);
			dataout.write(request.getRequestAsByteArray());
			dataout.flush();
		}
	}
	
	/*
	 * Serialize the HTTPResponse object through the given outputStream 
	 * */
	public static void sendHTTPResponse(HTTPResponse response, OutputStream out) throws IOException {
		if(response != null && out != null) {
			BufferedOutputStream dataout = new BufferedOutputStream(out);
			dataout.write(response.getRequestAsByteArray());
			dataout.flush();
		}
	}
	
	//Copié depuis Server.java from fjdhj
	
	/*
	 * Get the data from the file to upload
	 * */
	public static byte[]readFileData(File file) throws IOException, FileNotFoundException{
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
	public static String MIMEtype(String fileRequested) {
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
