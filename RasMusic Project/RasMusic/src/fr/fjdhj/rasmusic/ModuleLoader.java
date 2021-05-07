package fr.fjdhj.rasmusic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import fr.fjdhj.rasmusic.module.ModuleBase;
import fr.fjdhj.rasmusic.module.ModuleDefault;
import fr.fjdhj.rasmusic.module.ModuleMusic;
import fr.fjdhj.rasmusic.nativemodule.PlayerModuleInterface;

class ModuleLoader {

	static ArrayList<ModuleBase> loadModule(PlayerModuleInterface player){
		ArrayList<ModuleBase> moduleList = new ArrayList<>();
		File moduleFolder = new File(RasMusic.MODULE_FOLDER_PATH);
		if(moduleFolder.listFiles().length == 0)
			System.out.println("Aucun module a charger");
		
		//For load the .jar in the memory
		URLClassLoader loader;
		
		//For test the file in the .jar file
		String tmp = "";
		
		//Contain the content of the .jar
		Enumeration<JarEntry> enumeration;
		
		//Use for detect the interface use by the class
		@SuppressWarnings("rawtypes")
		Class tmpClass;
		
		ModuleBase tmpModule;
		
		for(File module : moduleFolder.listFiles()) {
			if(!module.isFile() || !module.getName().substring(module.getName().lastIndexOf('.')).equals(".jar") ) {
				System.out.println(module.getName() + " is not a valide file");
			}else {
				System.out.println("Looking for : " + module.getName());
				
            	try {
    				URL u = new URL("file:///"+module.getAbsoluteFile().toString());
    				loader = new URLClassLoader(new URL[] {u}); 
    	 
    	            JarFile jar = new JarFile(module.getAbsolutePath());
    	 
    	            //Get the content of the .jar
    	            enumeration = jar.entries();
    	            
    	            while(enumeration.hasMoreElements()){

    	            	tmp = enumeration.nextElement().toString();
    	 
    	                //Check if file is a .class
    	                if(tmp.length() > 6 && tmp.substring(tmp.length()-6).compareTo(".class") == 0) {
    	 
    	                    tmp = tmp.substring(0,tmp.length()-6);
    	                    tmp = tmp.replaceAll("/",".");
    	                    
    	                    tmpClass = Class.forName(tmp ,true,loader);
    	                    
    	                    for(int i = 0 ; i < tmpClass.getInterfaces().length; i ++ ){
    	                        if(tmpClass.getInterfaces()[i].getCanonicalName().equals("fr.fjdhj.rasmusic.module.ModuleDefault") ) {
    	                        	System.out.println("Loading module : " + module.getName());
    								tmpModule = (ModuleBase)tmpClass.getConstructor().newInstance();
    	                        	((ModuleDefault)tmpModule).init();
    	                        	System.out.println("Module name : "+tmpModule.getModuleName());
    	                        	System.out.println("Module version : "+tmpModule.getModuleVersion());
    	                        	moduleList.add(tmpModule);
    	                        	
    	                        }
    	                        else if(tmpClass.getInterfaces()[i].getCanonicalName().equals("fr.fjdhj.rasmusic.module.ModuleMusic")) {
    	                        	System.out.println("Loading module : " + module.getName());
    								tmpModule = (ModuleBase)tmpClass.getConstructor().newInstance();
    	                        	((ModuleMusic)tmpModule).init(player);
    	                        	System.out.println("Module name : "+tmpModule.getModuleName());
    	                        	System.out.println("Module version : "+tmpModule.getModuleVersion());
    	                        	moduleList.add(tmpModule);
    	                        }
    	                    }
    	 
    	                }
    	            }
    	            
            	}catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException e) {
					System.err.println("Error when intenciate the module "+module.getName());
					e.printStackTrace();
				} catch (MalformedURLException e) {
					System.err.println("The URL of the module is incorect :file:///"+module.getAbsoluteFile().toString());
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					System.err.println("Unable to find the class for the module "+module.getName());
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Error loading the jar file");
					e.printStackTrace();
				}

            }
			
		}
		return moduleList;
	}
}
