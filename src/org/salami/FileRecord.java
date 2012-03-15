package org.salami;

import hirondelle.date4j.DateTime;

import java.nio.file.Path;

public class FileRecord {

	private final Path path;
	private final DateTime modificationTime;
	private final DateTime syncTime;
	
	public FileRecord(Path path, DateTime modificationTime, DateTime syncTime){
		this.path = path;
		this.modificationTime = modificationTime;
		this.syncTime = syncTime;
	}
	
	public Path getPath(){
		return path;
	}
	
	public DateTime getModificationTime() {
		return modificationTime;
	}

	public DateTime getSyncTime() {
		return syncTime;
	}
	
}
