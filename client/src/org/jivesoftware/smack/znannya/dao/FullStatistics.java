package org.jivesoftware.smack.znannya.dao;

public class FullStatistics {
	private long time;
	private String ip;
	private String action;
	private float quantity;
	private String unit; //can be null
	private float costPerUnit; //can be null
	private float amount;
	private String description; //can be null

	public FullStatistics(){}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getCostPerUnit() {
		return costPerUnit;
	}

	public void setCostPerUnit(float costPerUnit) {
		this.costPerUnit = costPerUnit;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "FullStatistics [action=" + action + ", amount=" + amount
				+ ", costPerUnit=" + costPerUnit + ", description="
				+ description + ", ip=" + ip + ", quantity=" + quantity
				+ ", time=" + time + ", unit=" + unit + "]";
	}
}
