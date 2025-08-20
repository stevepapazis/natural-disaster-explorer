package server.analysis;


import java.util.Arrays;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import server.commons.TimeSeries;


/**
 * A class that computes regression statistics for a {@code TimeSeries}
 */
final public class RegressionEngine {
    final private SimpleRegression regression = new SimpleRegression();

    public RegressionEngine(final TimeSeries timeSeries) {
        timeSeries.getOccurrences().forEach( 
            pair -> {
                regression.addData(pair.getFirst(), pair.getSecond());
            }
        );
    }

    public double getRegressionIntercept() {
        return regression.getIntercept();
    }

    public double getRegressingSlope() {
        return regression.getSlope();
    }

    public double getRegressingSlopeError() {
        return regression.getSlopeStdErr();
    }

    public String getTendency() {
        double slope = getRegressingSlope();
        if (Double.isNaN(slope))
            return "Undefined Tendency";
        else if(slope > 0.1)
            return "Increased Tendency";
        else if(slope < -0.1)
            return "Decreased Tendency";
        return "Stable Tendency";
    }

    public String getRegressionResultString() {
        String[] stats = {
            String.format("intercept: %s",  getRegressionIntercept()),
            String.format("slope: %s",  getRegressingSlope()),
            String.format("slopeError: %s",  getRegressingSlopeError()),
            getTendency()
        };

        return "RegressionResult: " + Arrays.toString(stats);
    }
    
    @Override
    public String toString() {
        return getRegressionResultString();
    }
}
