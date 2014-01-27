package test;

public abstract class ParentMechanism {
	private String str;
	
	public ParentMechanism(String str) {
		this.str = str;
	}
	
	public abstract String getName();

}
