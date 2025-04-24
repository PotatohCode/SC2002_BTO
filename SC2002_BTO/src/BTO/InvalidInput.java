package BTO;

/**
 * A custom exception used to handle invalid user inputs in the BTO system.
 * 
 * This exception is typically thrown during menu navigation, form submissions,
 * or any scenario where the user provides unexpected or disallowed input.
 * 
 * It supports both a default message ("Invalid input!") and a custom message
 * indicating which field or option was invalid.
 * 
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 */
public class InvalidInput extends Exception {
	
	/**
	 * Constructs an InvalidInput exception with a default error message.
	 */
	public InvalidInput() {
		super("Invalid input!");
	}
	

	/**
	 * Constructs an InvalidInput exception with a specific field mentioned.
	 *
	 * @param input the name of the field or context that caused the error
	 */
	public InvalidInput(String input) {
		super("Invalid " + input + "!");
	}
}
