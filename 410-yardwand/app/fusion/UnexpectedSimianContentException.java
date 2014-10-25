/*
 * UnexpectedSimianContentException - a simple exception that acts like
 * a standard <code>Exception</code>. To be thrown when a <code>
 * SimianOutputParser</code> parse attempt could not be resolved.
 * Part of the UBC CPSC 410 yardwand project.
 * 
 * Author: Eric Furugori
 */

package fusion;

@SuppressWarnings("serial")
public class UnexpectedSimianContentException extends Exception {
	UnexpectedSimianContentException(String e) {
		super(e);
	}
}
