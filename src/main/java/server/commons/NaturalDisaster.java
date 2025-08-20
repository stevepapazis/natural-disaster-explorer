package server.commons;


import java.util.List;

import org.apache.commons.math3.util.Pair;

import dom2app.IMeasurementVector;

import server.analysis.RegressionEngine;
import server.analysis.StatisticsEngine;


/**
 * An immutable class that models a natural disaster
 */
public final class NaturalDisaster implements IMeasurementVector {
    private final Integer id;
    private final Country country;
    private final DisasterType type;
    private final TimeSeries measurements;
    private final StatisticsEngine statistics;
    private final RegressionEngine regression;
    
    /**
     * Constructs a natural disaster
     * @param id            the numerical id of the natural disaster
     * @param country       the country where the natural disaster occurred
     * @param type          the type of the natural disaster
     * @param measurements  the occurrences of the natural disasters of this type per year
     */
    protected NaturalDisaster(Integer id, Country country, DisasterType type, TimeSeries measurements) {
        this.id           = id;
        this.country      = country;
        this.type         = type;
        this.measurements = measurements;
        this.statistics   = new StatisticsEngine(measurements);
        this.regression   = new RegressionEngine(measurements);
    }
    
    /** 
     * Returns the numerical id of the natural disaster
     * @return the numerical id of the natural disaster
     */
    final public Integer getId() {
        return id;
    }

    /**
     * Returns the country where the natural disaster occurred
     * @return the country where the natural disaster occurred
     */
    final public Country getCountry() {
        return country;
    }
    
    /**
     * Returns the type of the natural disaster
     * @return the type of the natural disaster
     */
    final public DisasterType getType() {
        return type;
    }

    @Override
    final public String getCountryName() {
        return country.getLongName();
    }

    @Override
    final public String getIndicatorString() {
        return type.toString();
    }

    @Override
    final public List<Pair<Integer, Integer>> getMeasurements() {
        return measurements.getOccurrences();
    }

    @Override
    final public String getDescriptiveStatsAsString() {
        return statistics.getDescriptiveStatsString();
    }

    @Override
    final public String getRegressionResultAsString() {
        return regression.getRegressionResultString();
    }

    /**
     * Parses a list of {@code String} values into a {@code NaturalDisaster} object
     * 
     * @param fields                        at least five {@code Strings} containing the id, longCountryName, 
     *                                      countryIso2, countryIso3, disasterType, numberOfDisastersInThatYear
     * @param startYear                     the year of the first event
     * @return                              a {@code NaturalDisaster} object with the provided information
     * @throws IllegalArgumentException     if less than five {@code Strings} are provided
     * @throws NumberFormatException        if the id or one of the numberOfDisastersInThatYear is not an positive integer
     */
    final public static NaturalDisaster parse(final List<String> fields, final int startYear) 
    throws IllegalArgumentException, NumberFormatException {
        if (fields.size() < 5) throw new IllegalArgumentException("Not enough fields to initialize a NaturalDisaster");
        
        final int id             = Integer.parseUnsignedInt(fields.get(0));

        final String countryName = fields.get(1);
        final String iso2        = fields.get(2);
        final String iso3        = fields.get(3);
        final Country country    = new Country(countryName, iso2, iso3);

        final DisasterType type  = new DisasterType(fields.get(4));

        final List<String> leftoverFields = fields.subList(5, fields.size());
        final TimeSeries measurements = TimeSeries.parse(leftoverFields, startYear);
        
        return new NaturalDisaster(id, country, type, measurements);
    }

    /**
     * Creates a new NaturalDisaster instance that contains only the disasters between lowYear and highYear inclusively.
     * @param lowYear      a low year number
     * @param highYear     a high year number; it must be greater than or equal to lowYear
     * @return             a new NaturalDisaster with disasters between lowYear and highYear
     * @throws IllegalArgumentException if highYear is less than lowYear
     */
    final public NaturalDisaster filter(final int lowYear, final int highYear) throws IllegalArgumentException {
        return new NaturalDisaster(id, country, type, measurements.filter(lowYear, highYear));
    }

    @Override
    final public String toString() {
        return String.join(", ", id.toString(), country.toString(), type.toString());
    }

    /**
     * Returns a more verbose representation of the natural disaster showing information that it is available
     * @return a more verbose representation of the natural disaster showing information that it is available
     */
    final public String toFullDescription(){
        return String.join(", ", id.toString(), country.toFullDescription(), type.toString(), measurements.toString());
    }
}