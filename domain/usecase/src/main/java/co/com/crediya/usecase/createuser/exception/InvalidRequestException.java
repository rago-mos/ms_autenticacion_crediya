package co.com.crediya.usecase.createuser.exception;



public class InvalidRequestException extends ApiException {
    public InvalidRequestException(String message) {
        super(message);
    }
}

