package fusion;

@SuppressWarnings("serial")
public class InvalidRepositoryURLException extends Exception {
	
	public InvalidRepositoryURLException(String message) {
		super(message);
	}
	
	public InvalidRepositoryURLException(Exception cause) {
		super(cause);
	}
	
	public InvalidRepositoryURLException(String message, Exception cause) {
		super(message, cause);
	}
}
