package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.Length;
import it.unibs.ingswproject.auth.AuthService;
import jakarta.persistence.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.CharacterData;
import org.passay.*;

import static org.passay.AllowedCharacterRule.ERROR_CODE;

@Entity
public class Utente {
    @Id
    @Length(64) // Massimo 64 caratteri per poter utilizzare l'username come chiave primaria
    protected String username;
    protected String password;
    protected String emailAddress;
    @Enumerated(EnumType.STRING)
    protected Ruolo ruolo;
    protected boolean hasMadeFirstLogin = false;
    @ManyToOne()
    Comprensorio comprensorio;

    public Utente() {
        // Costruttore vuoto per Ebean
    }

    public Utente(String username, String password, Ruolo ruolo) {
        this();
        this.setUsername(username)
                .setPassword(password)
                .setRuolo(ruolo);
    }

    private static String generateRandomPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        return gen.generatePassword(8, new Rule[]{
                splCharRule, lowerCaseRule, upperCaseRule, digitRule
        });
    }

    // GETTERS
    public String getUsername() {
        return this.username;
    }

    // SETTERS
    public Utente setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("utente_username_not_empty");
        }
        this.username = username.toLowerCase().trim();
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    protected Utente setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("utente_password_not_empty");
        }
        this.password = AuthService.hashPassword(password);
        return this;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Utente setEmailAddress(String emailAddress) {
        if (!EmailValidator.getInstance().isValid(emailAddress)) {
            throw new IllegalArgumentException("utente_email_not_valid");
        }
        this.emailAddress = emailAddress;
        return this;
    }

    public Ruolo getRuolo() {
        return this.ruolo;
    }

    public Utente setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
        return this;
    }

    public Comprensorio getComprensorio() {
        return this.comprensorio;
    }

    public Utente setComprensorio(Comprensorio comprensorio) {
        this.comprensorio = comprensorio;
        return this;
    }

    public boolean hasMadeFirstLogin() {
        return this.hasMadeFirstLogin;
    }

    public boolean isConfiguratore() {
        return this.ruolo == Ruolo.CONFIGURATORE;
    }

    public boolean isFruitore() {
        return this.ruolo == Ruolo.FRUITORE;
    }

    public Utente changePassword(String password) {
        this.setPassword(password);
        this.hasMadeFirstLogin = true;
        return this;
    }

    public String setRandomPassword() {
        String password = generateRandomPassword();
        this.setPassword(password);
        return password;
    }

    public enum Ruolo {
        CONFIGURATORE, FRUITORE
    }
}
