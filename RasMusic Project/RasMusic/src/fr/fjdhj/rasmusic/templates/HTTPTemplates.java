package fr.fjdhj.rasmusic.templates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import fr.fjdhj.rasmusic.utils.HTTPUtil;
import fr.fjdhj.rasmusic.webserv.HTTPMimeType;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;
import fr.fjdhj.rasmusic.webserv.HTTPStatusCode;

public class HTTPTemplates {
	
	/**
	 * The 404 (Not Found) HTTP request state can be used when :</br>
	 * <i>"The requested resource could not be found but may be available in the future. Subsequent requests by the client are permissible."</i></br>
	 * Source : Wikipedia
	 * @return The HTTPResponse corresponding to the 404 HTTP request code
	 */
	public static HTTPResponse error404() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code404, null, null);
		return response;
	}
	
	/**
	 * The 400 (Bad Request) HTTP request state can be used when :</br>
	 * <i>"The server cannot or will not process the request due to an apparent client error (e.g., malformed request syntax, size too large, invalid request message framing, or deceptive request routing)."</i></br>
	 * Source : Wikipedia
	 * @return The HTTPResponse corresponding to the 400 HTTP request code
	 */
	public static HTTPResponse error400() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code400, null, null);
		return response;
	}
	
	/**
	 * Create a HTTPResponse with code 200 (OK) for sending a text (Mime type : text/plain)
	 * @param text The text to send
	 * @return The HTTPResponse with the text and the 200 HTTP code
	 */
	public static HTTPResponse plainText(String text) {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code200, null, null);
		response.addHeader("Date", new Date().toString());
		response.addHeader("Content-Type", HTTPMimeType.text.MIMEType);
		try {
			response.setBody(text.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * Create a HTTPResponse with code 200 (OK) for a file (Mime type depends on the extension file).
	 * If the file can't be found or can't be read, send a 404 (Not Found) HTTP request state
	 * @param text The text to send
	 * @return The HTTPResponse with the text and the 200 HTTP code
	 */
	public static HTTPResponse uploadFile(File file) {
		HTTPResponse response;
		try {
			response = new HTTPResponse(HTTPStatusCode.code200,null,null);
			response.setBody(HTTPUtil.readFileData(file)); //sets the content length
			response.addHeader("Date", new Date().toString());
			response.addHeader("Content-Type", HTTPUtil.MIMEtype(file.getName()));
			
		} catch (FileNotFoundException e) {
			response = error404();
			e.printStackTrace();
		} catch (IOException e) {
			response = error404();
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * The 200 (OK) HTTP request state can be used when :</br>
	 * <i>"Standard response for successful HTTP requests. The actual response will depend on the request method used. In a GET request, the response will contain an entity corresponding to the requested resource. In a POST request, the response will contain an entity describing or containing the result of the action."</i></br>
	 * Source : Wikipedia
	 * @return The HTTPResponse corresponding to the 200 HTTP request code
	 */
	public static HTTPResponse ok200() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code200, null, null);
		return response;
	}

	/**
	 * The 500 (Internal Server Error) HTTP request state can be used when :</br>
	 * <i>"A generic error message, given when an unexpected condition was encountered and no more specific message is suitable."</i></br>
	 * Source : Wikipedia
	 * @return The HTTPResponse corresponding to the 400 HTTP request code
	 */
	public static HTTPResponse error500() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code500, null, null);
		return response;
	}
}
