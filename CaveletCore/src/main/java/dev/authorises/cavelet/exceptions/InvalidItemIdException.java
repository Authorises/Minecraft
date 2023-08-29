package dev.authorises.cavelet.exceptions;

public class InvalidItemIdException extends Exception{
    public InvalidItemIdException(String errorMessage){
        super(errorMessage);
    }
}
