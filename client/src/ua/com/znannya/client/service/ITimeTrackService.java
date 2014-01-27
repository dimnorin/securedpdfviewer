package ua.com.znannya.client.service;

import org.jivesoftware.smack.znannya.dao.EntryType;

public interface ITimeTrackService {
	String startTrack(long fileId, EntryType entryType);
	
	String stopTrack();
	
	void stopShowingDocument(String reason);
}
