package fr.fjdhj.rasmusic.webserv;

import java.util.HashMap;

public abstract class HTTPMessage {
	
	protected HashMap<String,String> headers = new HashMap<String,String>();
	protected byte[] body;
	
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
