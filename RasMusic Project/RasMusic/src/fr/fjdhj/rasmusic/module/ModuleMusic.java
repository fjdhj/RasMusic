package fr.fjdhj.rasmusic.module;

import fr.fjdhj.rasmusic.nativemodule.PlayerModuleInterface;

/**
 * Music interface for the app module
 * It's allow the module to interact with the playerModule give in parameter with the init function
 * @author Fjdhj
 *
 */
public interface ModuleMusic extends ModuleBase{

	/**
	 * Use for check if the module interface is updated
	 */
	public static final int MODULE_MUSIC_VERSION = 0;
	
	/**
	 * The function call at the module loading
	 * @param player the PlayerModuleInterface object give by the ModuleLoader
	 * @return true if the module has load correctly
	 */
	public boolean init(PlayerModuleInterface player);
}
