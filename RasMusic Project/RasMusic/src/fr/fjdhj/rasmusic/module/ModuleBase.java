package fr.fjdhj.rasmusic.module;

import fr.fjdhj.rasmusic.webserv.HTTPResponse;

/**
 * Base interface for the app module
 * @author Fjdhj
 *
 */
public interface ModuleBase {
	
	/**
	 * Use for check if the module interface is updated
	 */
	public static final int MODULE_BASE_VERSION = 0;
	
	/**
	 * Give the name of the module
	 * @return the name of the module
	 */
	public String getModuleName();
	
	/**
	 * Give the module version	
	 * @return the module version
	 */
	public int getModuleVersion();
	
	/**
	 * Call when the server receive a GET request to this API and give in parameter the request
	 * The first value of args is the command, the other value is for parameter
	 * @param args the arguments give by the request
	 * @return true if the request succeeds, false if the request do not exist or a problem appeared 
	 */
	public HTTPResponse GETRequest(String[] args);
	
}
