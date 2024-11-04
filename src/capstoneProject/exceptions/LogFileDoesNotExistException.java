package capstoneProject.exceptions;

import java.io.IOException;

public class LogFileDoesNotExistException extends Exception {

	private static final long serialVersionUID = 5693545710245129606L;

	public LogFileDoesNotExistException() {
		super();
	}

	public LogFileDoesNotExistException(String reason) {
		super(reason);
	}

	public LogFileDoesNotExistException(String string, IOException e) {
		// TODO Auto-generated constructor stub
		super(string);
	}
}
