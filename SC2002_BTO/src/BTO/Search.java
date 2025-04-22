package BTO;

public abstract interface Search<T> {
	boolean getById(T id);
	default boolean getByBTO(T id) { return false; };
	default boolean getByUser(T id) { return false; };
}
