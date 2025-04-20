package BTO;

public class InvalidInput extends Exception {
	public InvalidInput() {
		super("Invalid input!");
	}
	
	public InvalidInput(String input) {
		super("Invalid " + input + "!");
	}
}
