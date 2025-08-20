package server.commons;


/**
 * An immutable helper class that models a country
 */
final public class Country {
    private final String longName;
    private final String iso2;
    private final String iso3;
    
    /**
     * Constructs a new country with the specified name, iso2, and iso3 values.
     * <p>
     * There is no validation of the values passed to the constructor
     * @param countryName       the usual name of the country
     * @param countryIso2       the iso2 country identifier; a two letter country identifier
     * @param countryIso3       the iso3 country identifier; a three letter country identifier
     */
    protected Country(final String countryName, final String countryIso2, final String countryIso3) {
        longName = countryName;
        iso2     = countryIso2;
        iso3     = countryIso3;
    };
    
    /**
     * Returns the full name of the country
     * @return the full name of the country
     */
    final public String getLongName() {
        return longName;
    }
    
    /**
     * Returns the two letter name of the country
     * @return the two letter name of the country
     */
    final public String getIso2() {
        return iso2;
    }
    
    /**
     * Returns the three letter name of the country
     * @return the three letter name of the country
     */
    final public String getIso3() {
        return iso3;
    }

    @Override
    final public String toString() {
        return longName;
    }

    /** 
     * Returns all the available information for the country
     * @return all the available information for the country
     */
    final public String toFullDescription() {
        return String.join(", ", longName, iso2, iso3);
    }
}