package it.unibs.ingswproject.auth;

import it.unibs.ingswproject.models.EntityRepository;
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
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "admin";
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

    /**
     * Questo metodo permette di creare un utente configuratore di default.
     * Se un utente configuratore esiste già, lancia un'eccezione.
     *
     * @return L'utente creato
     * @throws UserAlreadyExistsException Se un utente configuratore esiste già
     */
    public Utente createDefaultUser() throws UserAlreadyExistsException {
        EntityRepository<Utente> utenteRepository = this.storageService.getRepository(Utente.class);
        boolean utenteConfiguratoreExists = utenteRepository.findBy("ruolo", Utente.Ruolo.CONFIGURATORE) != null;
        if (utenteConfiguratoreExists) {
            throw new UserAlreadyExistsException("Default user already exists");
        }

        // Find a valid username
        String username = DEFAULT_USERNAME;
        int i = 1;
        while (utenteRepository.find(username) != null) {
            username = DEFAULT_USERNAME + i++;
        }

        Utente user = new Utente(username, DEFAULT_PASSWORD, Utente.Ruolo.CONFIGURATORE);
        utenteRepository.save(user);

        return user;
    }
}
