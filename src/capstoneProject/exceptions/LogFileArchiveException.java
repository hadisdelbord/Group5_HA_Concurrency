package capstoneProject.exceptions;

import java.io.IOException;

public class LogFileArchiveException extends Exception {

	private static final long serialVersionUID = -3883206292973439967L;

	public LogFileArchiveException() {
		super();
	}

	public LogFileArchiveException(String reason) {
		super(reason);
	}

	public LogFileArchiveException(String string, IOException e) {
		// TODO Auto-generated constructor stub
		super(string);
	}
}
