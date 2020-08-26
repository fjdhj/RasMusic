package fr.fjdhj.rasmusic.utils;

import java.io.OutputStream;

import fr.fjdhj.rasmusic.webserv.HTTPRequest;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;

public class HTTPUtil {
	
	/*
	 * Parse the request to build a HTTPRequest object.
	 * Returns null if the message can't be parsed
	 * */
	public static HTTPRequest parseRequest(String head, byte[] body) {
		
		return null;
	}
	
	/*
	 * Serialize the HTTPRequest object through the given outputStream 
	 * */
	public static void sendHTTPRequest(HTTPRequest request, OutputStream out) {
		
	}
	
	/*
	 * Serialize the HTTPResponse object through the given outputStream 
	 * */
	public static void sendHTTPResponse(HTTPResponse response, OutputStream out) {
		
	}
	
}
