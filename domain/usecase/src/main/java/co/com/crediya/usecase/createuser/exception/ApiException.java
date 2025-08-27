package co.com.crediya.usecase.createuser.exception;


public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }

}
