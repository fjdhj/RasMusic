package fr.fjdhj.rasmusic.webserv;

public enum HTTPStatusCode {
	code200("200 OK"),
	code201("201 Created"),
	code400("400 Bad Request"),
	code404("404 Not Found"),
	code411("411 Length Required"),
	code500("500 Internal Server Error ")
	;


public final String message;
HTTPStatusCode(String string) {
	message = string;
}
}
