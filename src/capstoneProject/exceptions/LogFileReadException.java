package capstoneProject.exceptions;

import java.io.IOException;

public class LogFileReadException extends Exception {

	private static final long serialVersionUID = -3891279592447815901L;

	public LogFileReadException() {
		super();
	}

	public LogFileReadException(String reason) {
		super(reason);
	}

	public LogFileReadException(String string, IOException e) {
		// TODO Auto-generated constructor stub
		super(string);
	}
}
