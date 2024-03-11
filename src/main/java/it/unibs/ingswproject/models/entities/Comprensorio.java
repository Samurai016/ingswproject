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
    private String nome;
    @OneToMany(mappedBy = "comprensorio")
    private List<Utente> utenti = new ArrayList<>();

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
        this.nome = nome;
        return this;
    }

    public List<Utente> getUtenti() {
        return this.utenti;
    }
}
