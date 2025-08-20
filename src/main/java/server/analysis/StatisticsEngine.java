package server.analysis;


import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import server.commons.TimeSeries;


/**
 * A class that computes descriptive statistics for a {@code TimeSeries}
 */
final public class StatisticsEngine {
    final private DescriptiveStatistics statistics = new DescriptiveStatistics();

    public StatisticsEngine(final TimeSeries timeSeries) {
        timeSeries.getOccurrences().forEach( 
            pair -> {
                statistics.addValue(pair.getSecond());
            }
        );
    }

    public long getNumberOfValues() {
        return statistics.getN();
    }
    
    public double getMin() {
        return statistics.getMin();
    }

    public double getMax() {
        return statistics.getMax();
    }

    public double getMean() {
        return statistics.getMean();
    }

    public double getMedian() {
        return statistics.getPercentile(50);
    }

    public double getStandardDeviation() {
        return statistics.getStandardDeviation();
    }

    public double getVariance() {
        return statistics.getVariance();
    }

    public double getSkewness() {
        return statistics.getSkewness();
    }

    public double getKurtosis() {
        return statistics.getKurtosis();
    }
    
    public double getTotalOccurences() {
        return statistics.getSum();
    }

    public String getDescriptiveStatsString() {
        Object[] stats = {
            "count",    getNumberOfValues(),
            "min",      getMin(),
            "max",      getMax(),
            "mean",     getMean(),
            "median",   getMedian(),
            "std dev",  getStandardDeviation(),
            "variance", getVariance(),
            "skewness", getSkewness(),
            "kurtosis", getKurtosis(),
            "total",    getTotalOccurences()
        };

        String[] statsAStrings = new String[stats.length/2];
        for (int i = 0; i < statsAStrings.length; i++)
            statsAStrings[i] = String.format("%s: %s", stats[2*i], stats[2*i+1]);
        
        return "DescriptiveStats: " + Arrays.toString(statsAStrings);
    }

    @Override
    public String toString() {
        return getDescriptiveStatsString();
    }
}
