package FileParser;

/**
 * An exception thrown in <code>FileParser</code> if the
 * observed file header fails to match the specified
 * regex pattern.
 */
class InvalidHeaderException extends Exception {
  private final String filePath;
  private final String pattern;
  private final String observed;

  /**
   * Constructor.
   * Reports a file header that observed to match
   * the specified pattern
   * @param filepath the path to the relevant file
   * @param pattern the expected header pattern
   * @param observed the observed header
   */
  InvalidHeaderException(String filepath, String pattern, String observed) {
    super();
    this.filePath = filepath;
    this.pattern = pattern;
    this.observed = observed;
  }

  /**
   * @return the file in which the
   * exception was thrown.
   */
  @Override
  public String getMessage() {
    return String.format("encountered invalid header while reading %s",
            filePath);
  }

  /**
   * @return the file, the expected pattern and the corresponding header
   * line that was observed to fail the match.
   */
  public String getDetailedMessage() {
    return String.format("encountered invalid header while reading %s."
            + "\nexpected: %s\nobserved: %s", filePath, pattern, observed);
  }
}
