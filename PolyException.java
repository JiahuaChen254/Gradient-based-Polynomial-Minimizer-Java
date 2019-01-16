package poly;

/** A simple specialized Exception class for our 'poly' package.  Use this
 *  class as you would any Exception.  You do not need to modify this file!
 *
 */
public class PolyException extends Exception {
	
	/** Exception constructor should always pass error message to its superclass
	 *  (we should not redundantly store the message since the superclass stores 
	 *   the message and provides methods for accessing it that we inherit here).
	 * 
	 * @param message
	 */
	public PolyException(String message) {
		super(message);
	}
}
