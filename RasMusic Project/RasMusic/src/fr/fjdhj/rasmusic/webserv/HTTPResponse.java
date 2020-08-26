package fr.fjdhj.rasmusic.webserv;

import java.util.HashMap;

public class HTTPResponse extends HTTPMessage{
	private HTTPStatusCode code;
	
	public HTTPResponse(HTTPStatusCode code, HashMap<String, String> headers, byte[] body) {
		this.code=code; 
		this.headers=headers; 
		this.body=body; 
	}
	/*
	 * Get the header value
	 * */
	public String getHeaderValue(String key) {
		if(headers.containsKey(key)) {
			return headers.get(key);
		}else {
			return "null";
		}
	}
	
	/*
	 * Get the request payload
	 * */
	public byte[] getBody() {
		return body;
	}
}
