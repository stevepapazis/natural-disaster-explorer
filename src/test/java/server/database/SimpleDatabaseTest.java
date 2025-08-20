package server.database;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import dom2app.ISingleMeasureRequest;

import org.junit.Assert;
import org.junit.Before;

import server.commons.NaturalDisaster;
import server.database.DatabaseFactory.DatabaseType;

import server.parsing.IParser;
import server.parsing.ParserFactory;
import server.parsing.ParserFactory.ParserType;

public class SimpleDatabaseTest {

    private static IParser parser;
    private static Path path;

    @Before
    public void loadDatabase() throws Exception {
        path = Paths.get("./src/test/resources/input/ClimateRelatedDisasters.tsv");
        parser = ParserFactory.create(ParserType.TSV);
    }

    /**
     * Tests the addition of an element to the database and its retrieval
     * @throws IOException
     */
    @Test
    public final void testAddAndRetrieve() throws IOException {
        
        NaturalDisaster entry = parser.parse(path, 1, 1980).get(0);

        IDatabase database = DatabaseFactory.create(DatabaseType.SimpleDatabase);

        database.add(entry);

        ISingleMeasureRequest request = database.request("request-name", entry.getCountryName(), entry.getIndicatorString());
        
        Assert.assertNotNull(request.getAnswer());
        Assert.assertEquals(entry, request.getAnswer());
        Assert.assertEquals(entry.getCountryName(), request.getAnswer().getCountryName());
        Assert.assertEquals(entry.getType().toString(), request.getAnswer().getIndicatorString());
        Assert.assertEquals(entry.getDescriptiveStatsAsString(), request.getAnswer().getDescriptiveStatsAsString());
        Assert.assertEquals(entry.getRegressionResultAsString(), request.getAnswer().getRegressionResultAsString());
    }

    /** 
     * Tests the addition of some elements to the database and their retrieval
     * @throws IOException
    */
    @Test
    public final void testAddAndRetrieveAll() throws IOException {
        List<NaturalDisaster> entries = parser.parse(path, 1, 1980);

        IDatabase database = DatabaseFactory.create(DatabaseType.SimpleDatabase);

        database.addAll(entries);

        for (NaturalDisaster entry: entries) {
            ISingleMeasureRequest request = database.request("request-name"+entry.getCountryName()+entry.getIndicatorString(), entry.getCountryName(), entry.getIndicatorString());
            
            Assert.assertNotNull(request.getAnswer());
            Assert.assertEquals(entry, request.getAnswer());
            Assert.assertEquals(entry.getCountryName(), request.getAnswer().getCountryName());
            Assert.assertEquals(entry.getType().toString(), request.getAnswer().getIndicatorString());
            Assert.assertEquals(entry.getDescriptiveStatsAsString(), request.getAnswer().getDescriptiveStatsAsString());
            Assert.assertEquals(entry.getRegressionResultAsString(), request.getAnswer().getRegressionResultAsString());
        }
    }
    
    /** 
     * Tests the addition of some elements to the database and their retrieval
     * @throws IOException
    */
    @Test
    public final void testAddAndRetrieveAllFiltered() throws IOException {
        List<NaturalDisaster> entries = parser.parse(path, 1, 1980);

        IDatabase database = DatabaseFactory.create(DatabaseType.SimpleDatabase);

        database.addAll(entries);

        for (NaturalDisaster entry: entries) {
            ISingleMeasureRequest request1 = database.request(
                "request-name"+entry.getCountryName()+entry.getIndicatorString(), 
                entry.getCountryName(), 
                entry.getIndicatorString()
            );

            ISingleMeasureRequest request2 = database.request(
                "request-name"+entry.getCountryName()+entry.getIndicatorString(), 
                entry.getCountryName(), 
                entry.getIndicatorString(),
                1995,
                2015
            );

            Assert.assertNotNull(request1.getAnswer());
            Assert.assertNotNull(request2.getAnswer());

            Assert.assertEquals(entry, request1.getAnswer());
            Assert.assertNotEquals(entry, request2.getAnswer());
            Assert.assertNotEquals(request1.getAnswer(), request2.getAnswer());

            Assert.assertEquals(request1.getAnswer().getCountryName(), request2.getAnswer().getCountryName());
            Assert.assertEquals(request1.getAnswer().getIndicatorString(), request2.getAnswer().getIndicatorString());

        }
        
    }
    
}
