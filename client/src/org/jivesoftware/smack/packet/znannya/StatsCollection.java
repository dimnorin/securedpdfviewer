package org.jivesoftware.smack.packet.znannya;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.znannya.dao.FullStatistics;

public class StatsCollection extends Statistic {
	
	private List<FullStatistics> statsList = new ArrayList<FullStatistics>();

	public void add(FullStatistics stat){
		statsList.add(stat);
	}
	
	public List<FullStatistics> getFullStatistics(){
		return statsList;
	}

	@Override
	public String toString() {
		return "StatsCollection [statsList=" + statsList + "]";
	}
}
