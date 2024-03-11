package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.DbEnumValue;
import io.ebean.annotation.Length;
import it.unibs.ingswproject.auth.AuthService;
import jakarta.persistence.*;

@Entity
public class Utente {
    public enum Ruolo {
        CONFIGURATORE, FRUITORE;

        @DbEnumValue
        public String getDbValue() {
            return this.name().toLowerCase();
        }
    }

    @Id
    @Length(64) // Massimo 64 caratteri per poter utilizzare l'username come chiave primaria
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;
    @ManyToOne()
    Comprensorio comprensorio;
    private boolean hasMadeFirstLogin = false;

    public Utente() {
        // Costruttore vuoto per Ebean
    }
    public Utente(String username, String password, Ruolo ruolo) {
        this();
        this.setUsername(username)
            .setPassword(password)
            .setRuolo(ruolo);
    }

    // GETTERS
    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Ruolo getRuolo() {
        return this.ruolo;
    }

    public Comprensorio getComprensorio() {
        return this.comprensorio;
    }

    public boolean isHasMadeFirstLogin() {
        return this.hasMadeFirstLogin;
    }

    // SETTERS
    public Utente setUsername(String username) {
        this.username = username;
        return this;
    }

    public Utente setPassword(String password) {
        this.password = AuthService.hashPassword(password);
        return this;
    }

    public Utente setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
        return this;
    }

    public Utente setComprensorio(Comprensorio comprensorio) {
        this.comprensorio = comprensorio;
        return this;
    }

    public Utente flagHasMadeFirstLogin() {
        this.hasMadeFirstLogin = true;
        return this;
    }
}
