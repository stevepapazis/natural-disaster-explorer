package server.reporting;


import dom2app.ISingleMeasureRequest;


/**
 * A ReportWriter factory
 */
public final class ReportWriterFactory {

	// hide the constructor
	private ReportWriterFactory() {}

	/**
	 * Regulates which of all the possible implementations of IReportWriter to use
	 */
	public enum ReportWriterTypes {
		TXT  ("text"), 
		MD 	 ("md"),
		HTML ("html");
		
		final protected String identifier;

		private ReportWriterTypes(final String identifier) {
			this.identifier = identifier;
		}
		
	} // add more options if new versions appear
	
	/**
	 * Returns a new concrete implementation of IReportWriter 
	 * 
	 * @param 	writerType regulates which concrete class to use
	 * @return  a concrete object which is an implementation of IReportWriter
	 */
	final public static IReportWriter create(final ReportWriterTypes writerType, ISingleMeasureRequest request) {
		switch (writerType) {
			case TXT:
				return new TxtWriter(request);
			case MD:
				return new MarkdownWriter(request);
			case HTML:
				return new HtmlWriter(request);
			default:
				throw new AssertionError("This code ought to be unreachable");
		}
	}

	final public static IReportWriter createFromString(final String type, ISingleMeasureRequest request) {
		if (type == ReportWriterTypes.TXT.identifier)  return new TxtWriter(request);
		if (type == ReportWriterTypes.MD.identifier)   return new MarkdownWriter(request);
		if (type == ReportWriterTypes.HTML.identifier) return new HtmlWriter(request);
		
		throw new AssertionError("This code is supposed to be unreachable");
	}
}