package fr.fjdhj.rasmusic.templates;

import fr.fjdhj.rasmusic.webserv.HTTPResponse;
import fr.fjdhj.rasmusic.webserv.HTTPStatusCode;

public class HTTPTemplates {
	
	public static HTTPResponse error404() {
		HTTPResponse response = new HTTPResponse(HTTPStatusCode.code404, null, null);
		return response;
	}
}
