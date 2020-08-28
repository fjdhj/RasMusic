package fr.fjdhj.rasmusic.templates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import fr.fjdhj.rasmusic.utils.HTTPUtil;
import fr.fjdhj.rasmusic.webserv.HTTPMimeType;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;
import fr.fjdhj.rasmusic.webserv.HTTPStatusCode;

public class HTTPTemplates {
	
	public static HTTPResponse error404() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code404, null, null);
		return response;
	}
	
	public static HTTPResponse error400() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code400, null, null);
		return response;
	}
	
	public static HTTPResponse plainText(String text) {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code200, null, null);
		response.addHeader("Date", new Date().toString());
		response.addHeader("Content-Type", HTTPMimeType.text.MIMEType);
		response.setBody(text.getBytes());
		return response;
	}
	
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

	public static HTTPResponse ok200() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code200, null, null);
		return response;
	}

	public static HTTPResponse error500() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code500, null, null);
		return response;
	}
}
