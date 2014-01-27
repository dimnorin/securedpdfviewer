package org.jivesoftware.smack.znannya.dao;

import org.jivesoftware.smack.packet.znannya.Statistic;

public class BriefStatistics extends Statistic{
	private float fromBalance;
	private float toBalance;
	private float spentBalance;
	private float refillBalance;
	
	public BriefStatistics(){}
	
	public float getFromBalance() {
		return fromBalance;
	}
	public void setFromBalance(float fromBalance) {
		this.fromBalance = fromBalance;
	}
	public float getToBalance() {
		return toBalance;
	}
	public void setToBalance(float toBalance) {
		this.toBalance = toBalance;
	}
	public float getSpentBalance() {
		return spentBalance;
	}
	public void setSpentBalance(float spentBalance) {
		this.spentBalance = spentBalance;
	}
	public float getRefillBalance() {
		return refillBalance;
	}
	public void setRefillBalance(float refillBalance) {
		this.refillBalance = refillBalance;
	}

	@Override
	public String toString() {
		return "BriefStatistics [fromBalance=" + fromBalance + ", toBalance=" + toBalance
				+ ", refillBalance=" + refillBalance + ", spentBalance="
				+ spentBalance + "]";
	}
}
