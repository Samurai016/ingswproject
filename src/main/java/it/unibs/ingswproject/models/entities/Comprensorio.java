package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.*;
import jakarta.persistence.*;

/**
 * @author Nicolò Rebaioli
 */
@Entity
public class Comprensorio {
    @Id
    @Length(64)
    private String nome;

    public Comprensorio(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public Comprensorio setNome(String nome) {
        this.nome = nome;
        return this;
    }
}
