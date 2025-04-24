package BTO;

/**
 * Search
 * Generic interface for enabling search operations in BTO system entities.
 * 
 * Defines default and abstract methods for identifying items by ID,
 * BTO project, or user association. Used for classes like BTO, Application,
 * Users, and Enquiries to support dynamic filtering and lookup.
 * 
 * @param <T> the data type of the identifier (e.g., Integer, String)
 */
public abstract interface Search<T> {

	/**
	 * getById(T id)
	 * Checks whether the current object matches the provided identifier.
	 * 
	 * @param id the identifier to check
	 * @return true if the object matches the ID, false otherwise
	 */
	boolean getById(T id);
	

	/**
	 * getByBTO(T id)
	 * Checks whether the object is linked to a specific BTO project.
	 * 
	 * @param id the BTO ID
	 * @return true if applicable, false otherwise (default: false)
	 */
	default boolean getByBTO(T id) { return false; };
	
	/**
	 * getByUser(T id)
	 * Checks whether the object is associated with a specific user.
	 * 
	 * @param id the user ID
	 * @return true if applicable, false otherwise (default: false)
	 */
	default boolean getByUser(T id) { return false; };
}
