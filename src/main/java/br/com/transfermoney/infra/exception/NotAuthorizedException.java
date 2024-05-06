package br.com.transfermoney.infra.exception;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String msg){
        super(msg);
    }

    public NotAuthorizedException(String msg, Throwable cause){
        super(msg, cause);
    }
}
