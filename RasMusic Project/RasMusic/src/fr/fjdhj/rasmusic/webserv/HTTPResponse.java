package fr.fjdhj.rasmusic.webserv;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class HTTPResponse extends HTTPMessage{
	private HTTPStatusCode code;
	
	public HTTPResponse(HTTPStatusCode code, HashMap<String, String> headers, byte[] body) {
		this.code=code; 
		if(headers == null) {
			headers = new HashMap<String, String>();
		}
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
	/*
	 * @return The message as a byte array
	 * */
	public byte[] getRequestAsByteArray() {
		String statusName = code.message;
		final StringBuilder headersString = new StringBuilder();
		headers.entrySet().forEach(new Consumer<Map.Entry<String,String>>() {
			@Override
			public void accept(Entry<String, String> arg0) {
				headersString.append(arg0.getKey() +": "+arg0.getValue() + "\r\n");
				
			}
		});
		String message = "HTTP/1.1 "+statusName + "\r\n"
					   + headersString + "\r\n";
		byte[] head = message.getBytes(); 
		if(body == null || body.length==0) {
			return head;
		}else {
			int bodylen = body.length;
			int headlen = head.length;
			byte[] sum = new byte[bodylen + headlen];
			
			System.arraycopy(head, 0, sum, 0, headlen);
			System.arraycopy(body, 0, sum, headlen, bodylen);
			return sum;
		}

	}
}
