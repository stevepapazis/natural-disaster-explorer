package server.reporting;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;
import java.util.ArrayList;

import dom2app.ISingleMeasureRequest;


/**
 * An implementation of the {@code IReportWriter} that produces html reports 
 */
final class HtmlWriter implements IReportWriter {
    
    final private ISingleMeasureRequest request;

    protected HtmlWriter(ISingleMeasureRequest request) {
        this.request = request;
    }

    @Override
    public ISingleMeasureRequest getRequest() {
        return request;
    }

    @Override
    public int write(final Path filePath) {

        final List<String> lines = new ArrayList<String>(); 

        lines.add( "<!doctype html>" );
        lines.add( "<html>" );
        lines.add( "<head>" );
        lines.add( "<meta http-equiv=\"Content-Type\" content\"text/html; charset=windows-1253\"> ");
        lines.add( "<title>Natural Disaster Data</title>" );
        lines.add( "</head>" );

        lines.add( "\n<body>" );

        lines.add( generateTitleLine( "<p><b>%s</b></p>" ) );
        lines.add( generateFilterLine("<p><i>%s</i></p>") );

        lines.add( "\n<table>" );
        lines.add( generateYearValueHeader( "<tr> <td>%s</td> <td>%s</td> </tr>\n") );
        lines.addAll( generateDataLines( "<tr> <td>%s</td> <td>%s</td> </tr>") );
        lines.add( "</table>\n" );

        lines.addAll( generateStatLines("<p>%s</p>") );

        lines.add( "</body>" );
        lines.add( "</html>" );


        try {
            Files.write(filePath, lines, utf8);
        } catch (final IOException e) {
            return -1;
        }

        return String.join("\n", lines).split("\n").length;
    }
    
    
}
