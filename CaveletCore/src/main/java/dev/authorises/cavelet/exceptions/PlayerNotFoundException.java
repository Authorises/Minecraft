package dev.authorises.cavelet.exceptions;

public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException(String errorMessage){
        super(errorMessage);
    }
}