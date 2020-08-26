package fr.fjdhj.rasmusic.webserv;

public enum HTTPMethod {
	POST("POST"),
	PUT("PUT"),
	GET("GET"),
	HEAD("HEAD"),
;

private String message;
HTTPMethod(String string) {
	message = string;
}
}
