package engine;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dom2app.IMeasurementVector;
import dom2app.ISingleMeasureRequest;

import server.commons.NaturalDisaster;

import server.parsing.ParserFactory;
import server.parsing.IParser;

import server.database.IDatabase;
import server.database.DatabaseFactory;
import server.database.DatabaseFactory.DatabaseType;

import server.reporting.IReportWriter;
import server.reporting.ReportWriterFactory;


final public class MainController implements IMainController {
    final private IDatabase database = DatabaseFactory.create(DatabaseType.SimpleDatabase);
    final private List<ISingleMeasureRequest> requests = new ArrayList<>();
    
    @Override
    public List<IMeasurementVector> load(final String fileName, final String delimiter) throws FileNotFoundException, IOException {
        final IParser parser = ParserFactory.createCustomDelimitedTxtParser(delimiter);
        final Path filePath = Paths.get(fileName);

        final List<NaturalDisaster> disasters = parser.parse(filePath, 1, 1980);
        
        final List<IMeasurementVector> result = new ArrayList<>();

        disasters.forEach(disaster -> {
            result.add(disaster);
            database.add(disaster);
        });

        return result;
    }

    @Override
    public ISingleMeasureRequest findSingleCountryIndicator(
        final String requestName, 
        final String countryName, 
        final String indicatorString
    ) throws IllegalArgumentException {
        ISingleMeasureRequest request = database.request(requestName, countryName, indicatorString);
        if (request.isAnsweredFlag()) requests.add(request);
        return request;
    }

    @Override
    public ISingleMeasureRequest findSingleCountryIndicatorYearRange(
        final String requestName, 
        final String countryName,
        final String indicatorString, 
        final int startYear, 
        final int endYear
    ) throws IllegalArgumentException {
        ISingleMeasureRequest request = database.request(requestName, countryName, indicatorString, startYear, endYear);
        if (request.isAnsweredFlag()) requests.add(request);
        return request;
    }

    @Override
    public Set<String> getAllRequestNames() {
        return requests.stream()
                       .map(req->req.getRequestName())
                       .collect(Collectors.toSet());
    }

    @Override
    public ISingleMeasureRequest getRequestByName(final String requestName) {
        return requests.stream()
                       .filter(req->requestName==req.getRequestName())
                       .findFirst()
                       .get(); // every request in the list is guaranteed to be non-empty
    }

    @Override
    public ISingleMeasureRequest getDescriptiveStats(String requestName) {
        // the answer of the request has its stats created when it's created
        return getRequestByName(requestName); 
    }

    @Override
    public ISingleMeasureRequest getRegression(String requestName) {
        // the answer of the request has its stats created when it's created
        return getRequestByName(requestName);
    }

    @Override
    public int reportToFile(String outputFilePath, String requestName, String reportType) throws IOException {
        final ISingleMeasureRequest request = getRequestByName(requestName);
        final IReportWriter writer = ReportWriterFactory.createFromString(reportType, request);
        final Path filePath = Paths.get(outputFilePath);
        return writer.write(filePath);
    }
}
