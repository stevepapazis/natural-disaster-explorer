package server.database;


import java.util.Collection;
import java.util.Optional;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;

import dom2app.IMeasurementVector;
import dom2app.ISingleMeasureRequest;

import server.commons.NaturalDisaster;


/**
 * A very simple database
 */
final class SimpleDatabase implements IDatabase {

    final private HashMap<Pair<String, String>, NaturalDisaster> entries = new LinkedHashMap<>();
    
    /**
     * Produces a key for accessing the database 
     * @param country           a countryLongName
     * @param disasterType      a disasterType
     * @return the key
     */
    final private static Pair<String, String> generateKey(String country, String disasterType) {
        return Pair.create(country, disasterType);
    }

    /**
     * Produces a key for accessing the database 
     * @param disaster
     * @return the key
     */
    final private static Pair<String, String> generateKey(IMeasurementVector disaster) {
        return generateKey(disaster.getCountryName(), disaster.getIndicatorString());
    }

    /**
     * @throws UnsupportedOperationException if there exists another object in the database with the same key
     */
    @Override
    final public SimpleDatabase add(final NaturalDisaster entry) throws UnsupportedOperationException {
        final Pair<String, String> key = generateKey(entry);

        if (this.entries.containsKey(key)) 
            throw new UnsupportedOperationException(
                "There is already another entry with the same key in the database. Adding the entry is disallowed"
            );

        this.entries.put(key, entry);

        return this;
    }

    /**
     * @throws UnsupportedOperationException whenever {@link #add} throws an exception
     */
    @Override
    final public SimpleDatabase addAll(final Collection<NaturalDisaster> disasters) throws UnsupportedOperationException {
        disasters.forEach(disaster->this.add(disaster));
        return this;
    }

    /**
     * Gets a {@code IMeasurementVector} record that match the information provided
     * @param countryName       the country long name
     * @param disasterType      the disaster type
     * @return                  an optional of the {@code IMeasurementVector} 
     */
    final private Optional<NaturalDisaster> retrieve(final String countryName, final String disasterType) {
        return Optional.ofNullable(
            entries.get(generateKey(countryName, disasterType))
        );
    }

    @Override
    public ISingleMeasureRequest request(final String requestName, final String countryName, final String disasterType) {
        Optional<NaturalDisaster> disaster = retrieve(countryName, disasterType);
        return Request.create(requestName, countryName, disasterType, disaster);
    }

    /** 
     * Gets a filtered {@code IMeasurementVector} record that match the information provided
     * <p>
     * The records that have been filtered contain the values in the inclusive range (startYear, endYear).
     * 
     * @param countryName   the long name of the country
     * @param disasterType  the type of the disaster
     * @param startYear     the start year
     * @param endYear       the end year
     * @return              the filtered record that was created
     * @throws IllegalArgumentException if startYear is greater than endYear
    */
    final private Optional<NaturalDisaster> retrieveWithFilter(
        final String countryName, 
        final String disasterType, 
        final int startYear, 
        final int endYear
    ) throws IllegalArgumentException {
        return retrieve(countryName,disasterType)
                    .map(disaster->disaster.filter(startYear, endYear));
    }

    @Override
    public ISingleMeasureRequest request(
        String requestName, 
        String countryName, 
        String disasterType, 
        int startYear,
        int endYear
    ) throws IllegalArgumentException {
        Optional<NaturalDisaster> disaster = retrieveWithFilter(countryName, disasterType, startYear, endYear);
        return Request.create(requestName, countryName, disasterType, disaster);
    }

    @Override
    final public String toString() {
        return entries.values().stream()
                      .map(NaturalDisaster::toFullDescription)
                      .collect(Collectors.joining("\n"));
    }
}