package fr.fjdhj.rasmusic.webserv;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

public abstract class HTTPMessage {
	
	protected HashMap<String,String> headers = new HashMap<String,String>();
	protected byte[] body;
	
	public void addHeader(String key, String value) {
		if(key.isEmpty() && key!=null && value.isEmpty() && value!=null) {
			headers.put(key, value);
		}
	}
	/*
	 * Sets the message body and updates the Content-Length header
	 * */
	public void setBody(byte[] body) {
		this.body = body;
		if(body.length != 0) {
			addHeader("Content-Length",String.valueOf(body.length));
		}
	}
	
	/*
	 * Get the header value associated with the given key
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
	/*
	 * Get the content length
	 * Return 0 if there is no content length
	 * */
	public int getContentLength() {
		String length = headers.get("Content-Length");
		int len = 0;
		try {
			len= Integer.parseInt(length);
		}catch(NumberFormatException ex) {
			ex.printStackTrace();
		}
		return len;
	}
}
