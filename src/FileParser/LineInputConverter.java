package FileParser;

/**
 * Defines a method that converts a read-in
 * line of input to an instance of the specified type.
 * May or may not process error checking with the given
 * <code>InvalidLineFormatException</code>.
 *
 * It is up to the overrider of this method to determine
 * whether or not lines must match a certain (regex) pattern.
 * @param <T> the target type of the conversion
 */
interface LineInputConverter<T> {
  /**
   * Converts a line of input to an instance of T.
   * @param lineIn the input to be converted
   * @param fileName the file being read
   * @param lineNo the line at which the exception occurred
   * @return the new instance of T
   */
  T convert(String lineIn, String fileName, int lineNo)
  ;
}
