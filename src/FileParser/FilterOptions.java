package FileParser;

import java.util.HashMap;

/**
 * The <code>FilterOptions</code> class allows callers of the
 * <code>FileParser</code> to apply rules to and strip
 * characters while calling FileParser.processFullText().
 */
public class FilterOptions {
  private HashMap<String, String> replacements = new HashMap<>();
  private boolean toLowerCase;

  /**
   * Constructor.
   * @param toLowerCase whether or not the
   * full text is processed to lower case
   */
  public FilterOptions(boolean toLowerCase) {
    this.toLowerCase = toLowerCase;
  }

  /**
   * Maps a regex to a replacement.
   * @param match the regex used to match patterns
   * @param replace the text to replace such found instances
   * @return true if no mapping previously existed for the
   * specified match, false if attempted overwrite
   */
  public boolean addReplacement(String match, String replace) {
    if (replacements.containsKey(match)) {
      return false;
    }
    replacements.put(match, replace);
    return true;
  }

  /**
   * Receives a line of text and applies
   * all of the specified operations to it.
   * @param line the line of input
   * @return the processed string
   */
  public String process(String line) {
    for (String pattern : replacements.keySet()) {
      line = line.replaceAll(pattern, replacements.get(pattern));
    }
    return toLowerCase ? line.toLowerCase() : line;
  }
}
