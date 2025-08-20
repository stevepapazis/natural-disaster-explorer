package server.commons;


/**
 * An immutable helper class that models a disaster type
 * <p>
 * Currently it is just a thin wrapper around String
 */
final public class DisasterType {
    private final String type;
    
    /** 
     * Constructs a new instance of disasterType
     * <p>
     * There is no check if the type is valid
     * 
     * @param disaster the type of the disaster
     */
    protected DisasterType(final String disaster) {
        type = disaster;
    }
    
    @Override
    final public String toString() {
        return type;
    }
}
