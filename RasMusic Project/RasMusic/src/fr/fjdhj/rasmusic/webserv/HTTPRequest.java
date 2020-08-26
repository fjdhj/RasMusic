package fr.fjdhj.rasmusic.webserv;

import java.util.HashMap;

public class HTTPRequest extends HTTPMessage{
	private HTTPMethod method;
	
	public HTTPRequest(HTTPMethod method, HashMap<String, String> headers, byte[] body) {
		this.method=method; 
		this.headers=headers; 
		this.body=body; 
	}

	/*
	 * Get the request method
	 * */
	public HTTPMethod getMethod(){
		return method;
	}
	
}
