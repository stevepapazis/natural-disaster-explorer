package server.reporting;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;
import java.util.ArrayList;

import dom2app.ISingleMeasureRequest;


/**
 * An implementation of the {@code IReportWriter} that produces text reports 
 */
final class TxtWriter implements IReportWriter {

    final private ISingleMeasureRequest request;

    protected TxtWriter(ISingleMeasureRequest request) {
        this.request = request;
    }

    @Override
    public ISingleMeasureRequest getRequest() {
        return request;
    }

    @Override
    public int write(final Path filePath) {

        final List<String> lines = new ArrayList<String>(); 

        lines.add( generateTitleLine("%s") );
        lines.add( generateFilterLine("%s") );
        lines.add( generateYearValueHeader( "%s\t%s" ));
        lines.addAll( generateDataLines("%s\t%s") );
        lines.addAll( generateStatLines("%s") );

        try {
            Files.write(filePath, lines, utf8);
        } catch (final IOException e) {
            return -1;
        }

        return lines.size();
    }
    
}
