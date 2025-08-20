package server.parsing;


import java.io.IOException;
import java.nio.file.Path;

import java.util.List;

import server.commons.NaturalDisaster;


/**
 * A simple parser interface
 */
public interface IParser {
   /**
    * Parses the given input file and returns a list of {@code Entry} instances
    *
    * @param filePath       a file to parse
    * @param linesToSkip    a number of lines to skip in the beginning of the file; for instance if there is a header
    * @param initialYear    the year when the measurements started
    * @return               a list of {@code Entry} instances
    * @throws IOException   when the input file is malformed
    */
    public List<NaturalDisaster> parse(final Path filePath, final int linesToSkip, final int initialYear) throws IOException;
}