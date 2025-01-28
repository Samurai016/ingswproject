package it.unibs.ingswproject.auth;

/**
 * @author Nicolò Rebaioli
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
