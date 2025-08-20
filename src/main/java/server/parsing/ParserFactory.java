package server.parsing;


import java.nio.file.Path;
import java.util.Optional;


/**
 * A parser factory
 * <p>
 * It currently supports CSV, TSV parsers and parsers for delimited text with custom delimiters 
 */
final public class ParserFactory {

	// hide the constructor
	private ParserFactory() {}

	/**
	 * Regulates which of all the possible implementations of {@code IParser} to use
	 * <p>
	 * It currently supports csv and tsv files.
	 */
	public static enum ParserType{
		CSV (",", ".csv"), 
		TSV ("\t", ".tsv");

		final protected String delimiter;
		final protected String fileExtension;

		private ParserType(String delimiter, String extension) {
			this.delimiter = delimiter;
			this.fileExtension = extension;
		}
	}
	
	/**
	 * Returns a new concrete implementation of {@code IParser} 
	 * @param 	parserType regulates which concrete class to use
	 * @return  a concrete object which is an implementation of {@code IParser}
	 */
	final public static IParser create(final ParserType parserType) {
		switch (parserType) {
			case CSV:
				return new DelimitedTxtParser(ParserType.CSV.delimiter);
			case TSV:
				return new DelimitedTxtParser(ParserType.TSV.delimiter);
			default:
				throw new AssertionError("This code is supposed to be unreachable");
		}
	}

	/** 
	 * Constructs a DelimitedTxtParser with a user-specified delimiter
	 * @param 	delimiter the delimiter to use
	 * @return 	a new {@code DelimitedTxtParser} with the specified delimiter
	 */
	final public static IParser createCustomDelimitedTxtParser(final String delimiter) {
		return new DelimitedTxtParser(delimiter);
	}

	/**
     * Guesses the parser type needed based on the extension of a file
     * <p>
     * Only "*.csv" and "*.tsv" files are currently supported.
     * @param file			the file to guess
     * @return          	the delimiter
     */
    final public static Optional<ParserType> guessDelimiterFromFileExtension(Path file) {
		String path = file.getFileName().toString();
        String extension = path.substring(path.lastIndexOf("."));
        
		if (extension.equals(ParserType.CSV.fileExtension)) return Optional.of(ParserType.CSV);
		if (extension.equals(ParserType.TSV.fileExtension)) return Optional.of(ParserType.TSV);
		
		return Optional.empty();
    }
}