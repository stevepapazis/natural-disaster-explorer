package server.parsing;


import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Files;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import server.commons.NaturalDisaster;


/**
 * A simple parser that reads delimiter-separated text files.
 */
final class DelimitedTxtParser implements IParser {
    final private String delimiter;
    
    /**
     * Constructs a parser with specified delimiter
     * @param delimiter the delimiter used by the file to parse
     */
    protected DelimitedTxtParser(final String delimiter) {
        this.delimiter = delimiter;
    }
    
    @Override
    final public List<NaturalDisaster> parse(
            final Path filePath,
            final int linesToSkip,
            final int initialYear
        ) throws IOException {
        
        final List<NaturalDisaster> disasters = new ArrayList<>();
        
        try (final BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineIndex = 0;

            while ( (line = reader.readLine()) != null ) {
                lineIndex += 1;
                
                if (lineIndex <= linesToSkip) continue;
                
                try {
                    final NaturalDisaster disaster = NaturalDisaster.parse(this.split(line), initialYear);
                    disasters.add(disaster);
                } catch (IllegalArgumentException e) {
                    throw new IOException(e.getMessage());
                }
            }
        }

        return disasters;
    }
    
    /**
     * Splits the line on the delimiters. 
     * <p>
     * Text within double quotes is treated as a block
     * 
     * @param line          a {@code String} to be splitted on the delimiters
     * @return              the splitted chunks of text
     * @throws IOException  when an opening quote is encountered without a corresponding closing quote
     */
    final private List<String> split(final String line) throws IOException {
        final List<String> splittedLine = new ArrayList<>();

        final String quote = "\"";
        
        final List<String> naiveSplit = Arrays.asList( line.split(delimiter) );
        
        final Iterator<String> naiveIterator = naiveSplit.iterator();

        while (naiveIterator.hasNext()) {
            String word = naiveIterator.next();

            if (!word.contains(quote)) {
                splittedLine.add(word);
                continue;
            }
            
            if (word.contains(quote)) {
                final List<String> capturedContext = new ArrayList<String>();
                capturedContext.add(word); 
            
                while (naiveIterator.hasNext()) {
                    word = naiveIterator.next();
                    capturedContext.add(word);
                    if (word.contains(quote)) break;
                }

                if (naiveIterator.hasNext() || word.contains(quote))
                    splittedLine.add(String.join(delimiter, capturedContext));
                else
                    throw new IOException("There is an opening quote without a corresponding closing quote");                
            }
        }

        return splittedLine;
    }
}