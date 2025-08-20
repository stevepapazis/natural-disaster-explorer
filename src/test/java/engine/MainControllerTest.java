package engine;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import engine.IMainControllerFactory.ControllerTypeEnum;

import server.commons.NaturalDisaster;
import server.database.DatabaseFactory;
import server.database.DatabaseFactory.DatabaseType;
import server.database.IDatabase;
import server.parsing.IParser;
import server.parsing.ParserFactory;
import server.parsing.ParserFactory.ParserType;
import server.reporting.IReportWriter;
import server.reporting.ReportWriterFactory;
import server.reporting.ReportWriterFactory.ReportWriterTypes;

import dom2app.IMeasurementVector;
import dom2app.ISingleMeasureRequest;


public class MainControllerTest {

    IMainControllerFactory factory = new IMainControllerFactory();

    @Test
    public void testLoad() throws IOException {

        // we need two controllers because try to add the same entries to the database is disallowed and creates classes
        IMainController controller1 = factory.createMainController(ControllerTypeEnum.DEFAULT);
        IMainController controller2 = factory.createMainController(ControllerTypeEnum.DEFAULT);

        List<IMeasurementVector> csvInput = controller1.load("src/test/resources/input/ClimateRelatedDisasters.csv", ",");
        List<IMeasurementVector> tsvInput = controller2.load("src/test/resources/input/ClimateRelatedDisasters.tsv", "\t");

        for (int i=0; i<tsvInput.size(); i++) {
            final IMeasurementVector csvVector = csvInput.get(i);
            final IMeasurementVector tsvVector = tsvInput.get(i);

            Assert.assertEquals(csvVector.getDescriptiveStatsAsString(), tsvVector.getDescriptiveStatsAsString());
            Assert.assertEquals(csvVector.getRegressionResultAsString(), tsvVector.getRegressionResultAsString());
                
        }
    }

    @Test
    public void testFindSingleCountryIndicator() throws IOException {
        IMainController controller = factory.createMainController(ControllerTypeEnum.DEFAULT);
        List<IMeasurementVector> entries = controller.load("src/test/resources/input/ClimateRelatedDisasters.csv", ",");

        IMeasurementVector entry = entries.get(123);

        ISingleMeasureRequest request = controller.findSingleCountryIndicator("test-request", entry.getCountryName(), entry.getIndicatorString());
        
        Assert.assertEquals(entry, request.getAnswer());
        Assert.assertEquals(entry.getDescriptiveStatsAsString(), request.getAnswer().getDescriptiveStatsAsString());
    }

    @Test
    public void testFindSingleCountryIndicatorYearRange() throws IOException {
        
        IMainController controller = factory.createMainController(ControllerTypeEnum.DEFAULT);
        List<IMeasurementVector> entries = controller.load("src/test/resources/input/ClimateRelatedDisasters.csv", ",");

        IMeasurementVector entry = entries.get(123);

        ISingleMeasureRequest request = controller.findSingleCountryIndicatorYearRange(
            "test-request", 
            entry.getCountryName(), 
            entry.getIndicatorString(),
            1995,
            2018
        );
        
        Assert.assertEquals(entry.getCountryName(), request.getAnswer().getCountryName());
        Assert.assertEquals(entry.getIndicatorString(), request.getAnswer().getIndicatorString());

    }

    @Test
    public void testGetRequestByName() throws IOException {
        
        IMainController controller = factory.createMainController(ControllerTypeEnum.DEFAULT);
        List<IMeasurementVector> entries = controller.load("src/test/resources/input/ClimateRelatedDisasters.csv", ",");

        IMeasurementVector entry = entries.get(123);

        ISingleMeasureRequest request = controller.findSingleCountryIndicatorYearRange(
            "test-request", 
            entry.getCountryName(), 
            entry.getIndicatorString(),
            1995,
            2018
        );

        ISingleMeasureRequest request2 = controller.getRequestByName("test-request");

        Assert.assertEquals(request, request2);
    }

    @Test
    public void testGetDescriptiveStats() throws IOException {
        IMainController controller = factory.createMainController(ControllerTypeEnum.DEFAULT);
        List<IMeasurementVector> entries = controller.load("src/test/resources/input/ClimateRelatedDisasters.csv", ",");

        IMeasurementVector entry = entries.get(123);

        ISingleMeasureRequest request = controller.findSingleCountryIndicatorYearRange(
            "test-request", 
            entry.getCountryName(), 
            entry.getIndicatorString(),
            1995,
            2018
        );

        Assert.assertEquals(
            "DescriptiveStats: [count: 8, min: 1.0, max: 1.0, mean: 1.0, median: 1.0, std dev: 0.0, variance: 0.0, skewness: NaN, kurtosis: NaN, total: 8.0]", 
            request.getDescriptiveStatsString()
        );
    }

    @Test
    public void testGetRegression() throws IOException {
        
        IMainController controller = factory.createMainController(ControllerTypeEnum.DEFAULT);
        List<IMeasurementVector> entries = controller.load("src/test/resources/input/ClimateRelatedDisasters.csv", ",");

        IMeasurementVector entry = entries.get(123);

        ISingleMeasureRequest request = controller.findSingleCountryIndicatorYearRange(
            "test-request", 
            entry.getCountryName(), 
            entry.getIndicatorString(),
            1995,
            2018
        );

        Assert.assertEquals(
            "RegressionResult: [intercept: 1.0, slope: 0.0, slopeError: 0.0, Stable Tendency]", 
            request.getRegressionResultString()
        );
    }

    @Test
    public void testReportToFile() throws IOException {
        
        Path path = Paths.get("./src/test/resources/input/ClimateRelatedDisasters.tsv");
        IParser parser = ParserFactory.create(ParserType.TSV);

        List<NaturalDisaster> entries = parser.parse(path, 1, 1980);

        IDatabase database = DatabaseFactory.create(DatabaseType.SimpleDatabase);
        database.addAll(entries);

        NaturalDisaster entry = entries.get(543); 

        ISingleMeasureRequest request = database.request("request-name", entry.getCountryName(), entry.getIndicatorString());

        Path outputPath = Paths.get("./src/test/resources/output/controller-test.md");
        IReportWriter writer = ReportWriterFactory.create(ReportWriterTypes.MD, request);

        Assert.assertEquals(25, writer.write(outputPath));

    }

}
