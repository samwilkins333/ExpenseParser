package FileParser;

import java.util.HashMap;

/**
 * The <code>FilterOptions</code> class allows callers of the
 * <code>FileParser</code> to apply rules to and strip
 * characters while calling FileParser.processFullText().
 */
public class FilterOptions {
  private final HashMap<String, String> replacements = new HashMap<>();
  private final boolean toLowerCase;

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
   */
  public void addReplacement(String match, String replace) {
    if (replacements.containsKey(match)) {
      return;
    }
    replacements.put(match, replace);
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
