package fr.fjdhj.rasmusic.webserv.exception;

public abstract class HTTPRequestException extends Exception {
	public abstract String getErrorCode();
}
