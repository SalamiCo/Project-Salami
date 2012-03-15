package org.salami;

import java.nio.file.Path;
import java.util.HashMap;

public class FileDictionary {

	private final HashMap <Path, FileRecord> dictionary = new HashMap<>();
	private final Path root;
	//ID ??
	
	public FileDictionary(Path root){
		this.root = root;
	}
	
	public void saveToFile(Path path){
		
	}
	
	//quiz‡s a–adir un constructor que directamente cargue de un fichero
	public static FileDictionary loadFromFile(Path path){
		FileDictionary newDictionary = new FileDictionary( path );
		
		return newDictionary;
	}
	
	public Path getRoot(){
		return root;
	}
	
}
