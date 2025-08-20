package server.database;


/**
 * A database factory
 * <p>
 * It currently only supports a simple database
 */
final public class DatabaseFactory {

	// hide the constructor
	private DatabaseFactory() {}

	/**
	 * Regulates which of all the possible implementations of IDatabase to use
	 */
	public enum DatabaseType {SimpleDatabase, }; // add more options if new versions appear
	
	/**
	 * Returns a new concrete implementation of IDatabase 
	 * 
	 * @param 	databaseType regulates which concrete class to use
	 * @return  a concrete object which is an implementation of IDatabase
	 */
	final public static IDatabase create(final DatabaseType databaseType) {
		switch (databaseType) {
			case SimpleDatabase:
				return new SimpleDatabase();
			default:
				throw new AssertionError("This code ought to be unreachable");
		}
	}
}