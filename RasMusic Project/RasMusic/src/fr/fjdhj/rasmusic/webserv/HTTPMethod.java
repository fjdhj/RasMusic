package fr.fjdhj.rasmusic.webserv;

public enum HTTPMethod {
	POST("POST"),
	PUT("PUT"),
	GET("GET"),
	HEAD("HEAD"),
;

public String message;
HTTPMethod(String string) {
	message = string;
}


}
