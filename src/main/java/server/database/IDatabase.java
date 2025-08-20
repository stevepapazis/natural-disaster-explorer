package server.database;


import java.util.Collection;

import dom2app.ISingleMeasureRequest;

import server.commons.NaturalDisaster;


/** 
 * A simple interface for a database 
 */
public interface IDatabase {
    
    /**
     * Adds a {@code IMeasurementVector} record to the database
     * <p>
     * The database is returned; useful in operation chaining
     * @param entry     an {@code IMeasurementVector}
     * @return          the database after the insertion
     */
    public IDatabase add(final NaturalDisaster entry);

    /**
     * Adds {@code IMeasurementVector} records to the database
     * <p>
     * The database is returned; useful in operation chaining
     * @param entries   a collection of {@code IMeasurementVector} instances
     * @return          the database after the insertion
     */
    public IDatabase addAll(final Collection<NaturalDisaster> entries);

    /**
     * Creates a request to the database with the specified information
     * @param requestName
     * @param countryName
     * @param disasterType
     * @return the request
     */
    public ISingleMeasureRequest request(final String requestName, final String countryName, final String disasterType);

    /**
     * Creates a request to the database with the specified information
     * @param requestName
     * @param countryName
     * @param disasterType
     * @param startYear
     * @param endYear
     * @return the request
     * @throws IllegalArgumentException if the startYear is greater than the endYear
     */
    public ISingleMeasureRequest request(
        final String requestName, 
        final String countryName,
        final String disasterType, 
        final int startYear, 
        final int endYear
    ) throws IllegalArgumentException;
}
