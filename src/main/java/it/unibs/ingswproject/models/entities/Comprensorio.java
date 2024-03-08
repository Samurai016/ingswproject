package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.*;
import jakarta.persistence.*;

/**
 * @author Nicolò Rebaioli
 */
@Entity
public class Comprensorio {
    @Id
    @Length(64) // Massimo 64 caratteri per poter utilizzare l'username come chiave primaria
    private String nome;

    public Comprensorio(String nome) {
        this.setNome(nome);
    }

    public String getNome() {
        return this.nome;
    }

    public Comprensorio setNome(String nome) {
        this.nome = nome;
        return this;
    }
}
