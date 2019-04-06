package FileParser;

/**
 * An exception thrown if a <code>FileParser</code>'s converter
 * in FileParser.convertLines() enforces a lines matching a
 * specified regex pattern and observes a deviation from this pattern.
 */
public class InvalidLineFormatException extends Exception {
  private String filePath;
  private String pattern;
  private String observed;
  private int lineNo;

  /**
   * Constructor.
   * Reports an input line that observed to match
   * the specified pattern
   * @param filePath the path to the relevant file
   * @param pattern the expected header pattern
   * @param observed the observed header
   * @param lineNo the line at which the error occurred
   */
  public InvalidLineFormatException(String filePath, String pattern,
                                    String observed, int lineNo) {
    this.filePath = filePath;
    this.pattern = pattern;
    this.observed = observed;
    this.lineNo = lineNo;
  }

  @Override
  public String getMessage() {
    return String.format("encountered invalid input line (%d) while reading %s",
            lineNo, filePath);
  }

  /**
   * @return the file, the expected pattern and the corresponding input
   * line that was observed to fail the match.
   */
  public String getDetailedMessage() {
    return String.format("encountered invalid input line (%d) "
                    + "while reading %s\nexpected: %s\nobserved: %s", lineNo,
            filePath, pattern, observed);
  }
}
