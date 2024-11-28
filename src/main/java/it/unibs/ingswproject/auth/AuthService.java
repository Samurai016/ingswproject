package it.unibs.ingswproject.auth;

import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Utente;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Questa classe è un servizio singleton che gestisce l'autenticazione degli utenti.
 * @author Nicolò Rebaioli
 */
public class AuthService {
    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String SALT = "PBKDF2WithHmacSHA1";
    protected Utente currentUser;
    protected final StorageService storageService;
    
    public AuthService(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Questo metodo permette di effettuare il login di un utente.
     * @param username Username dell'utente
     * @param password Password dell'utente
     * @return true se il login è avvenuto con successo, false altrimenti
     */
    public boolean login(String username, String password) {
        // Search user by username
        Utente user = this.storageService.getRepository(Utente.class).find(username);

        // If user not found, return false
        if (user == null) {
            return false;
        }

        // If password is correct, set current user and return true
        if (Objects.equals(hashPassword(password), user.getPassword())) {
            this.currentUser = user;
            return true;
        }

        return false;
    }

    public boolean isLoggedIn() {
        return this.currentUser != null;
    }

    public Utente getCurrentUser() {
        return this.currentUser;
    }

    public static String hashPassword(String password) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }
}
