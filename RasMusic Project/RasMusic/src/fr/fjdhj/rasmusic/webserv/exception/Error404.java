package fr.fjdhj.rasmusic.webserv.exception;

public class Error404 extends HTTPRequestException {
	
	
	
	private String file;
	
	public Error404(String filename) {
		file = filename;
	}
	
	@Override
	public String getErrorCode() {
		// TODO Auto-generated method stub
		return "404 Not Found";
	}

}
