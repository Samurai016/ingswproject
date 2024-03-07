package it.unibs.ingswproject.auth;

/**
 * Questa classe è un servizio singleton che gestisce l'autenticazione degli utenti.
 * TODO: Implementare l'autenticazione degli utenti
 * @see AuthService
 * @author Nicolò Rebaioli
 */
public class AuthService {
    public static AuthService instance;

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        return true;
    }
}
