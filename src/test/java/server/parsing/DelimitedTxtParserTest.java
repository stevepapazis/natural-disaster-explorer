package server.parsing;

import org.junit.Test;
import org.junit.Assert;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import server.parsing.ParserFactory.ParserType;

import server.commons.NaturalDisaster;

public class DelimitedTxtParserTest {

    /**
     * Parses a tsv and checks the correctness of the obtained data
     * @throws IOException
     */
    @Test
    public void testTSVParser() throws IOException {
        Path filePath = Paths.get("./src/test/resources/input/gre.tsv");

        ParserType guess = ParserFactory.guessDelimiterFromFileExtension(filePath).orElse(null);
        Assert.assertNotNull(guess);
        Assert.assertEquals(ParserType.TSV, guess);

        IParser parser = ParserFactory.create(guess);
        List<NaturalDisaster> entries = parser.parse(filePath,1,1980);
        
        List<String> disasterTypes = Arrays.asList(new String[] {
                                                                    "TOTAL",
                                                                    "Flood",
                                                                    "Drought",
                                                                    "Extreme temperature",
                                                                    "Storm",
                                                                    "Wildfire"
                                                                });

        Assert.assertEquals(9, entries.size());
        
        for (NaturalDisaster disaster: entries) {
            Assert.assertTrue(disaster.getId()>=344 && disaster.getId()<=352);
            Assert.assertTrue(disaster.getCountryName().equals("Greece") || disaster.getCountryName().equals("Grenada"));
            Assert.assertTrue(disaster.getCountry().getIso2().equals("GR") || disaster.getCountry().getIso2().equals("GD"));
            Assert.assertTrue(disaster.getCountry().getIso3().equals("GRC") || disaster.getCountry().getIso3().equals("GRD"));
            Assert.assertTrue(disasterTypes.contains(disaster.getType().toString()));
        }
    }

    /**
     * Parses a csv file and checks its correctness
     * @throws IOException
     */
    @Test
    public void testCSVParser() throws IOException {
        Path filePath = Paths.get("./src/test/resources/input/gre.csv");

        ParserType guess = ParserFactory.guessDelimiterFromFileExtension(filePath).orElse(null);
        Assert.assertNotNull(guess);
        Assert.assertEquals(ParserType.CSV, guess);

        IParser parser = ParserFactory.create(guess);
        List<NaturalDisaster> entries = parser.parse(filePath, 1, 1980);
        
        List<String> disasterTypes = Arrays.asList(new String[] {
                                                                    "TOTAL",
                                                                    "Flood",
                                                                    "Drought",
                                                                    "Extreme temperature",
                                                                    "Storm",
                                                                    "Wildfire"
                                                                });

        Assert.assertEquals(9, entries.size());
        
        for (NaturalDisaster disaster: entries) {
            Assert.assertTrue(disaster.getId()>=344 && disaster.getId()<=352);
            Assert.assertTrue(disaster.getCountryName().equals("Greece") || disaster.getCountryName().equals("Grenada"));
            Assert.assertTrue(disaster.getCountry().getIso2().equals("GR") || disaster.getCountry().getIso2().equals("GD"));
            Assert.assertTrue(disaster.getCountry().getIso3().equals("GRC") || disaster.getCountry().getIso3().equals("GRD"));
            Assert.assertTrue(disasterTypes.contains(disaster.getType().toString()));
        }
    }

    /**
     * Tests whether the output from CSV and TSV parsers is the same for equivalent data
     */
    @Test
    public void compareOutputs() throws IOException {
        final Path csvFile = Paths.get("./src/test/resources/input/gre.csv");
        final IParser csvParser = ParserFactory.create(ParserType.CSV);
        final List<NaturalDisaster> csvOutput = csvParser.parse(csvFile, 1, 1980); 

        final Path tsvFile = Paths.get("./src/test/resources/input/gre.tsv");
        final IParser tsvParser = ParserFactory.create(ParserType.TSV);
        final List<NaturalDisaster> tsvOutput = tsvParser.parse(tsvFile, 1, 1980); 

        for (int i = 0; i < csvOutput.size(); i++) {
            NaturalDisaster csvDisaster = csvOutput.get(i); 
            NaturalDisaster tsvDisaster = tsvOutput.get(i);
            Assert.assertEquals(csvDisaster.toFullDescription(), tsvDisaster.toFullDescription());
        }
    }

    /**
     * Parses a file that is not delimited
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void parseFileWrongFormat() throws IOException {
        Path filePath = Paths.get("./src/test/resources/output/GR-TOT.txt");
        IParser parser = ParserFactory.create(ParserType.TSV);
        List<NaturalDisaster> results = parser.parse(filePath, 1, 1980);
    }

    /**
     * Parses a path that doesn't exist
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void parseFileDoesNotExist() throws IOException {
        Path filePath = Paths.get("this/is/a/file/that/doesnt/exist.csv");
        IParser parser = ParserFactory.create(
            ParserFactory.guessDelimiterFromFileExtension(filePath).get()
        );
        List<NaturalDisaster> results = parser.parse(filePath, 1, 1980);
    }

    /**
     * Parses a TSV file with a CSV parser
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void parseWithWrongParserType() throws IOException {
        Path filePath = Paths.get("./src/test/resources/input/gre.tsv");
        IParser parser = ParserFactory.create(ParserType.CSV);
        List<NaturalDisaster> results = parser.parse(filePath, 1, 1980);
    }

}
