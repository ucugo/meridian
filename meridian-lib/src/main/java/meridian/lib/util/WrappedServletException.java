package meridian.lib.util;

import java.io.IOException;

import javax.servlet.ServletException;

public class WrappedServletException extends RuntimeException {
	
	private static final long serialVersionUID = 407372780605637245L;

	public WrappedServletException(ServletException servletException) {
		super(servletException);
	}
	
	public WrappedServletException(IOException ioException) {
		super(ioException);
	}
}
