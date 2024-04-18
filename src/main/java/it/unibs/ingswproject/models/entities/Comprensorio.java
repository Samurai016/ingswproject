package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
@Entity
public class Comprensorio {
    @Id
    @Length(64) // Massimo 64 caratteri per poter utilizzare l'username come chiave primaria
    protected String nome;
    @OneToMany(mappedBy = "comprensorio")
    protected final List<Utente> utenti = new ArrayList<>();

    public Comprensorio() {
        // Costruttore vuoto per Ebean
    }
    public Comprensorio(String nome) {
        this();
        this.setNome(nome);
    }

    public String getNome() {
        return this.nome;
    }

    public Comprensorio setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("comprensorio_name_not_empty");
        }
        this.nome = nome;
        return this;
    }

    public List<Utente> getUtenti() {
        return this.utenti;
    }
}
