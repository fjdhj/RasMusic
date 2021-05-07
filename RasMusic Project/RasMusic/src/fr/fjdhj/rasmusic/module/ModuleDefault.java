package fr.fjdhj.rasmusic.module;

/**
 * Basic interface for the app module
 * @author Fjdhj
 *
 */
public interface ModuleDefault extends ModuleBase {

	/**
	 * Use for check if the module interface is updated
	 */
	public static final int MODULE_DEFAULT_VERSION = 0;
	
	/**
	 * The function call at the module loading
	 * @return true if the module has load correctly 
	 */
	public boolean init();
}
