package myvote.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MyExceptions {
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Parameters")  
    public static class MyInvalidParameterException extends RuntimeException {
		public MyInvalidParameterException(){}
    }
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Username or Password")  
    public static class MyAuthenticationException extends RuntimeException {
		public MyAuthenticationException(){}
    }
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Parameters for Poll")  
    public static class MyInvalidPollException extends RuntimeException {
		public MyInvalidPollException(){}
    }
}
