package server.reporting;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;
import java.util.ArrayList;

import dom2app.ISingleMeasureRequest;


/**
 * An implementation of the {@code IReportWriter} that produces markdown reports 
 */
final class MarkdownWriter implements IReportWriter {
    
    final private ISingleMeasureRequest request;

    protected MarkdownWriter(ISingleMeasureRequest request) {
        this.request = request;
    }

    @Override
    public ISingleMeasureRequest getRequest() {
        return request;
    }

    @Override
    public int write(final Path filePath) {

        final List<String> lines = new ArrayList<String>(); 

        lines.add( generateTitleLine("**%s**\n") );
        lines.add( generateFilterLine("_%s_") );
        lines.add( generateYearValueHeader( "| *%s* | *%s* |\n|----|----|" ) );
        lines.addAll( generateDataLines("| %s | %s |") );
        lines.addAll( generateStatLines("\n%s") );

        
        try {
            Files.write(filePath, lines, utf8);
        } catch (final IOException e) {
            return -1;
        }

        return String.join("\n", lines).split("\n").length;
    }
    
}
