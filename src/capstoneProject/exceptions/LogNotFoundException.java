package capstoneProject.exceptions;

public class LogNotFoundException extends Exception{

	private static final long serialVersionUID = 5308708851209151857L;

	public LogNotFoundException() {
		super();
	}

	public LogNotFoundException(String reason) {
		super(reason);
	}
}
