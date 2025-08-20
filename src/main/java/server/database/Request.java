package server.database;


import java.util.Optional;

import dom2app.IMeasurementVector;
import dom2app.ISingleMeasureRequest;

import server.commons.NaturalDisaster;


/**
 * An immutable class modeling database requests that implements ISingleMeasureRequest
 */
final class Request implements ISingleMeasureRequest {
    final private String name;
    final private String country;
    final private String type;
    final private Optional<NaturalDisaster> answer; 

    /**
     * Constructs a new request with the specified information
     * @param name          the name of the request
     * @param country       the name of the country in the request
     * @param type          the type of the disaster in the request
     */
    private Request(final String name, final String country, final String type, final Optional<NaturalDisaster> disaster) {
        this.name = name;
        this.country = country;
        this.type = type;
        this.answer = disaster;
    }

    /**
     * Creates a new instance of a Request
     * @param name
     * @param country
     * @param type
     * @param disaster
     * @return the new Request instance
     */
    protected static Request create(final String name, final String country, final String type, final Optional<NaturalDisaster> disaster) {
        return new Request(name, country, type, disaster);
    }

    @Override
    public String getRequestName() {
        return name;
    }

    /**
     * @return "Country ~ %s | Indicator : %s"
     */
    @Override
    public String getRequestFilter() {
        return String.format("Country ~ %s | Indicator : %s", country, type);
    }

    @Override
    public boolean isAnsweredFlag() {
        return answer.isPresent();
    }

    @Override
    public IMeasurementVector getAnswer() {
        return answer.get();
    }

    @Override
    public String getDescriptiveStatsString() {
        return answer.map(NaturalDisaster::getDescriptiveStatsAsString)
                     .orElse(null);
    }

    @Override
    public String getRegressionResultString() {
        return answer.map(NaturalDisaster::getRegressionResultAsString)
                     .orElse(null);
    }

    @Override
    public String toString() {
        return String.join(", ", name, country, type, answer.toString());
    }
}
