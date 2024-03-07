package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.*;
import jakarta.persistence.*;

/**
 * @author Nicolò Rebaioli
 */
@Entity
public final class Comprensorio {
    @Id
    @Length(64)
    private String nome;

    public Comprensorio(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
