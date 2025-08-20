package server.commons;


import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.commons.math3.util.Pair;


/**
 * An immutable helper class that stores (timeValue,intValue) pairs
 */
final public class TimeSeries {
    final private List<Pair<Integer, Integer>> occurrences;
    
    /** 
     * Constructs an empty TimeSeries
     */
    protected TimeSeries() {
        occurrences = new ArrayList<Pair<Integer, Integer>>();
    }

    /**
     * Constructs a TimeSeries from a list of (timeValue,intValue)
     * @param occurrences pairs of (timeValue,intValue) 
     */
    protected TimeSeries(final List<Pair<Integer, Integer>> occurrences) {
        this.occurrences = new ArrayList<Pair<Integer, Integer>>(occurrences.size());
        this.setOccurrences(occurrences);
    } 
    
    /**
     * Returns the list of (timeValue,intValue) stored in the timeSeries
     * @return the list of (timeValue,intValue) stored in the timeSeries
     */
    final public List<Pair<Integer, Integer>> getOccurrences() {
        return occurrences;
    }
    
    /**
     * Sets the occurrences from a list
     * <p>
     * The list is not assumed to be sorted nor it is assumed that it contains unique elements.
     * @param occurrences   a list of occurrences
     */
    final private void setOccurrences(final List<Pair<Integer, Integer>> occurrences) {
        this.occurrences.clear();
        
        if (occurrences.size() == 0) return;
        
        occurrences.sort( Comparator.comparing(Pair::getFirst) );
        
        this.add(occurrences.get(0));
        
        for (int i=1; i<occurrences.size(); i++) {  
            final Pair<Integer, Integer> pair = occurrences.get(i);
            
            int lastIndex = this.size()-1;
            Pair<Integer, Integer> lastPair = this.get(lastIndex);
            
            if (lastPair.getFirst() != pair.getFirst() ) {
                this.add(pair);
            } else {
                lastPair = Pair.create(pair.getFirst(), pair.getSecond()+lastPair.getSecond());
                this.occurrences.set(lastIndex, lastPair);
            }
        }
    }

    /**
     * Appends a pair to the timeSeries
     * @param pair      a (timeValue, intValue) pair to be appended
     * @return          <tt>true</tt> if the timeSeries changed as a result of the call
     */
    final private boolean add(final Pair<Integer, Integer> pair) {
        return this.occurrences.add(pair);
    }

    /**
     * Returns the pair at the given index
     * @param index     index of the pair to return
     * @return          the pair at the given index
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    final public Pair<Integer, Integer> get(final int index) throws IndexOutOfBoundsException {
        return this.occurrences.get(index);
    }

    /** 
     * Returns the number of pairs in the timeSeries
     * @return the number of pairs in the timeSeries
     */
    final public int size() {
        return this.occurrences.size();
    }

    /**
     * Construct a new timeSeries for the window (lowTimeValue, highTimeValue)
     * 
     * @param lowTimeValue      the low time value
     * @param highTimeValue     the high time value; it must be greater or equal to lowTimeValue
     * @return                  a new timeSeries containing the result
     * @throws IllegalArgumentException if the low time value is greater than the highTimeValue
     */
    final TimeSeries filter(final int lowTimeValue, final int highTimeValue) throws IllegalArgumentException {
        if (lowTimeValue > highTimeValue) throw new IllegalArgumentException("lowTimeValue must be smaller than highTimeValue");

        final TimeSeries result = new TimeSeries();

        for ( Pair<Integer, Integer> pair : this.occurrences ) {
            if (pair.getFirst() <= highTimeValue) {
                if (pair.getFirst() >= lowTimeValue) 
                    result.add(pair);
            } else 
                break;
        }

        return result;
    }
    
    /**
     * Generates a timeSeries from a list of {@Strings} containing numbers.
     * <p>
     * An empty string is treated as a zero value.
     * 
     * @param occurrences       a list of {@Strings} containing numbers
     * @param offsetTime        all the timeValues are offset by this much
     * @throws NumberFormatException if one of the values is not a positive number or an empty string
     */
    final static TimeSeries parse(final List<String> occurrences, final int offsetTime) throws NumberFormatException {
        final List<Pair<Integer,Integer>> nonZeroOccurrences = new ArrayList<>();

        for ( int i = 0; i < occurrences.size(); i++) {
            final String value = occurrences.get(i);
            if (!value.isEmpty()) 
                nonZeroOccurrences.add( Pair.create(offsetTime+i, Integer.parseUnsignedInt(value)) );
        }
        
        return new TimeSeries(nonZeroOccurrences);
    }

    @Override
    final public String toString() {
        return occurrences.toString();
    }
}
