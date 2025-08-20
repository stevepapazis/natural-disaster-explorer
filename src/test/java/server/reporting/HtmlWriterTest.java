package server.reporting;


import org.junit.Test;
import org.junit.Assert;

import java.util.List;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import dom2app.ISingleMeasureRequest;

import server.commons.NaturalDisaster;

import server.database.DatabaseFactory;
import server.database.DatabaseFactory.DatabaseType;
import server.database.IDatabase;

import server.parsing.IParser;
import server.parsing.ParserFactory;
import server.parsing.ParserFactory.ParserType;

import server.reporting.ReportWriterFactory.ReportWriterTypes;


public class HtmlWriterTest {

    /**
     * Tests the HtmlWriter.write method
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {

        Path path = Paths.get("./src/test/resources/input/ClimateRelatedDisasters.tsv");
        IParser parser = ParserFactory.create(ParserType.TSV);

        List<NaturalDisaster> entries = parser.parse(path, 1, 1980);

        IDatabase database = DatabaseFactory.create(DatabaseType.SimpleDatabase);
        database.addAll(entries);

        NaturalDisaster entry = entries.get(123); 

        ISingleMeasureRequest request = database.request("request-name", entry.getCountryName(), entry.getIndicatorString());

        Path outputPath = Paths.get("./src/test/resources/output/test.html");
        IReportWriter writer = ReportWriterFactory.create(ReportWriterTypes.HTML, request);

        Assert.assertEquals(35, writer.write(outputPath));
    }

}
