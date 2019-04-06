package FileParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Abstracts the parseFileName of reading in lines from a given
 * text file.
 * @param <T> signifies the type to which read lines will be converted
 * in FileParser.convertLines()
 */
public class FileParser<T> {
  private BufferedReader reader;
  private String fullPath;
  private String fileName;

  /**
   * Constructor.
   * Builds a reader based on on the specified path. With this
   * constructor, it is a assumed that no headerMatch matching is
   * necessary.
   * @param sourcePath the destination of the file to open.
   * @throws IOException for generic predictable behavior, it
   * is the responsibility of the caller to handle exceptions
   * associated with an invalid path.
   */
  public FileParser(String sourcePath) throws IOException {
    this.parseFileName(sourcePath);
    this.reader = new BufferedReader(new FileReader(sourcePath));
  }

  /**
   * With the specified options dictating how to process
   * text at a character level, returns the concatenated
   * full file text.
   * @param options the <code>FilterOptions</code> applied to characters
   * @param replaceInFile whether or not to overwrite the original contents
   * of the source file with the processed text.
   * @return the full text of the file as specified by the options
   * @throws IOException responsibility of the caller to handle
   * exceptions caused by the actual reading process
   */
  public String processFullText(FilterOptions options, boolean replaceInFile)
          throws IOException {
    StringBuilder raw = new StringBuilder();
    int next;

    while ((next = reader.read()) > 0) {
      raw.append((char) next);
    }
    reader.close();

    String body = raw.toString();
    body = (options == null) ? body : options.process(body);
    if (replaceInFile) {
      writeFile(fullPath, body);
    }

    return body;
  }

  /**
   * Writes a cleaned version of the file.
   * @param source the file path to which to write
   * @param body the content to write
   */
  public void writeFile(String source, String body) {
    try {
      FileWriter writer = new FileWriter(source);
      writer.write(body);
      writer.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Constructor.
   * Builds a reader based on on the specified path.
   * @param sourcePath the destination of the file to open.
   * @param headerRegex indicates that the file reader should
   * verify that the first line read in matches the specified pattern
   * @throws IOException for generic predictable behavior, it
   * is the responsibility of the caller to handle exceptions
   * associated with an invalid path
   * @throws InvalidHeaderException reports a header (empty or malformed)
   * that differs from the given pattern
   */
  public FileParser(String sourcePath, String headerRegex)
          throws IOException, InvalidHeaderException {
    this.parseFileName(sourcePath);
    this.reader = new BufferedReader(new FileReader(sourcePath));
    Pattern headerMatch = Pattern.compile(headerRegex);
    String observed = reader.readLine();
    boolean validHeader = observed != null
            && headerMatch.matcher(observed.trim()).find();
    if (!validHeader) {
      observed = observed == null ? "EOF" : observed.trim();
      throw new InvalidHeaderException(fileName, headerRegex, observed);
    }
  }

  private void parseFileName(String sourcePath) {
    fullPath = sourcePath;
    String[] pathElements = sourcePath.split("/");
    fileName = pathElements[pathElements.length - 1];
  }

  /**
   * Returns the file's contents as a list of
   * individual <code>String</code> lines.
   * Here, no line pattern matching is applied, just
   * simple lexing.
   * @return the lines of the file as Strings
   * @throws IOException caller is responsible if
   * an exception occurs during reading.
   */
  public List<String> getLines() throws IOException {
    List<String> lines = new ArrayList<>();
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(line.trim());
    }
    reader.close();
    return lines;
  }

  /**
   * Returns the file's contents as an iterable
   * list of <code>String</code> lines.
   * @param converter the method used to convert input lines to
   * the desired target type
   * @return the lines in the content's file
   * @throws IOException reports an error with the actual reading operation
   * @throws InvalidLineFormatException reports lines that fail to match
   * the specified regex.
   */
  public List<T> convertLines(LineInputConverter<T> converter)
          throws IOException, InvalidLineFormatException {
    List<T> lines = new ArrayList<>();
    int lineNo = 1;
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(converter.convert(line.trim(), fileName, lineNo));
      lineNo++;
    }
    reader.close();
    return lines;
  }
}
