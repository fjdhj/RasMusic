package fr.fjdhj.rasmusic.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Directory {
	
	/**
	 * This function return all the files in a folder and sub folder who end with a specific sting
	 * @param folder The folder
	 * @param end The end of the file (like a file extension)
	 * @return The list who contains all files in the folder and sub-folders
	 */
	public static List<File> getAllFiles(File folder, String end) {
		List<File> out = new ArrayList<>();
		
		//If it's not a directory we don't need to execute the rest of the code
		if(!folder.isDirectory())
			return out;
		
		ArrayList<File> content = new ArrayList<>();
		content.addAll(Arrays.asList(folder.listFiles()));
		File currentFile;
		while(!content.isEmpty()) {
			currentFile = content.get(0);
			if(currentFile.isDirectory())
				content.addAll(Arrays.asList(content.remove(0).listFiles()));
			else if(currentFile.getName().substring(currentFile.getName().lastIndexOf('.')).equals(end))
				out.add(content.remove(0));
			else
				content.remove(0);
		}
		
		return out;
	}
}
