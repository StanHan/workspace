package demo.java.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Search a list of files for lines that match a given regular-expression pattern. Demonstrates NIO mapped byte buffers,
 * charsets, and regular expressions.
 *
 */
public class Grep {

    // Charset and decoder for ISO-8859-15
    private static Charset charset = Charset.forName("ISO-8859-15");
    private static CharsetDecoder charsetDecoder = charset.newDecoder();

    // Pattern used to parse lines
    private static Pattern linePattern = Pattern.compile(".*\r?\n");

    // The input pattern that we're looking for
    private static Pattern pattern;

    // Compile the pattern from the command line
    //
    private static void compile(String pat) {
        try {
            pattern = Pattern.compile(pat);
        } catch (PatternSyntaxException x) {
            System.err.println(x.getMessage());
            System.exit(1);
        }
    }

    // Use the linePattern to break the given CharBuffer into lines, applying
    // the input pattern to each line to see if we have a match
    private static void grep(File file, CharBuffer cb) {
        Matcher lineMatcher = linePattern.matcher(cb); // Line matcher
        Matcher matcher = null; // Pattern matcher
        int lines = 0;
        while (lineMatcher.find()) {
            lines++;
            CharSequence charSquence = lineMatcher.group(); // The current line
            if (matcher == null)
                matcher = pattern.matcher(charSquence);
            else
                matcher.reset(charSquence);
            if (matcher.find())
                System.out.print(file + ":" + lines + ":" + charSquence);
            if (lineMatcher.end() == cb.limit())
                break;
        }
    }

    // Search for occurrences of the input pattern in the given file
    private static void grep(File file) throws IOException {

        // Open the file and then get a channel from the stream
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();

        // Get the file's size and then map it into memory
        int size = (int) fileChannel.size();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);

        // Decode the file into a char buffer
        CharBuffer charBuffer = charsetDecoder.decode(mappedByteBuffer);

        // Perform the search
        grep(file, charBuffer);

        // Close the channel and the stream
        fileChannel.close();

        fileInputStream.close();
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Grep pattern file...");
            return;
        }
        compile(args[0]);
        for (int i = 1; i < args.length; i++) {
            File f = new File(args[i]);
            try {
                grep(f);
            } catch (IOException x) {
                System.err.println(f + ": " + x);
            }
        }
    }
    

}
