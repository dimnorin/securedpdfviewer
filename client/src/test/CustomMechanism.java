package test;

public class CustomMechanism extends ParentMechanism {

	public CustomMechanism(String str) {
		super(str);
	}

	@Override
	public String getName() {
		return "TEST_TEST";
	}

}
