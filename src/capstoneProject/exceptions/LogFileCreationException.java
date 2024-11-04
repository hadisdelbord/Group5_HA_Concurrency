package capstoneProject.exceptions;

import java.io.IOException;

public class LogFileCreationException extends Exception {

	private static final long serialVersionUID = 7498145587714938369L;

	public LogFileCreationException() {
		super();
	}

	public LogFileCreationException(String reason) {
		super(reason);
	}

	public LogFileCreationException(String string, IOException e) {
		// TODO Auto-generated constructor stub
		super(string);
	}
}
