package fr.fjdhj.rasmusic.webserv;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class HTTPRequest extends HTTPMessage{
	private HTTPMethod method;
	private String requestURI;
	public HTTPRequest(HTTPMethod method,String requestURI, HashMap<String, String> headers, byte[] body) {
		this.method=method; 
		this.headers=headers; 
		this.body=body; 
		this.requestURI=requestURI; 
	}
	
	/*
	 * Get the request URI
	 * */
	public String getRequestUri() {
		return requestURI;
	}

	/*
	 * Get the request method
	 * */
	public HTTPMethod getMethod(){
		return method;
	}
	/*
	 * @return The message as a byte array
	 * */
	public byte[] getRequestAsByteArray() {
		String methodString = method.name();
		final StringBuilder headersString = new StringBuilder();
		headers.entrySet().forEach(new Consumer<Map.Entry<String,String>>() {
			@Override
			public void accept(Entry<String, String> arg0) {
				headersString.append(arg0.getKey() +": "+arg0.getValue() + "\r\n");
				
			}
		});
		String message = methodString +" HTTP/1.1" + "\r\n"
					   + headersString + "\r\n\r\n";
		byte[] head = message.getBytes(); 
		int bodylen = body.length;
		int headlen = head.length;
		byte[] sum = new byte[bodylen + headlen];
		
		System.arraycopy(head, 0, sum, 0, headlen);
		System.arraycopy(bodylen, 0, sum, headlen, bodylen);
		return sum;
	}
}
