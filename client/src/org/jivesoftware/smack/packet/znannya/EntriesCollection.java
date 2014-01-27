package org.jivesoftware.smack.packet.znannya;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Entry;

public class EntriesCollection extends IQ {
	private List<Entry> entriesList = new ArrayList<Entry>();
	private int count = 0;

	public void add(Entry diss){
		entriesList.add(diss);
	}
	
	public List<Entry> getEntries(){
		return entriesList;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String getChildElementXML() {
		return null;
	}

	@Override
	public String toString() {
		return "EntriesCollection [count=" + count + ", entriesList="
				+ entriesList + "]";
	}
}
