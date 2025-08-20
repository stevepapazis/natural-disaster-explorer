package server.reporting;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dom2app.ISingleMeasureRequest;


/**
 * A interface for ReportWriters
 */
public interface IReportWriter {

    static Charset utf8 = StandardCharsets.UTF_8;

    ISingleMeasureRequest getRequest();
    
    default String generateTitleLine(String format) { 
        return String.format(format, getRequest().getRequestName());
    }

    default String generateFilterLine(String format) { 
        return String.format(format, getRequest().getRequestFilter());
    }

    default String generateYearValueHeader(String format) { 
        return String.format(format, "Year", "Value");
    }

    default List<String> generateDataLines(String format) { 
        return getRequest().getAnswer()
                           .getMeasurements()
                           .stream()
                           .map( pair -> String.format(format, pair.getFirst(), pair.getSecond()) )
                           .collect(Collectors.toCollection(ArrayList<String>::new));
    }
    
    default List<String> generateStatLines(String format) {
        return Arrays.asList(
            new String[]{ 
                String.format(format, getRequest().getDescriptiveStatsString()),
                String.format(format, getRequest().getRegressionResultString())
            }
        );
    }

    /** 
     * Write a report to the specified path
     * @param filePath 
     * @return an integer with the number of lines written; -1 if sth goes wrong
    */
    public int write(Path filePath);
}
