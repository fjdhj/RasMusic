package fr.fjdhj.rasmusic.nativemodule.exception;

public class PlayerModuleInterfaceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4501145686067344234L;

	private String message;
	
	public PlayerModuleInterfaceException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
