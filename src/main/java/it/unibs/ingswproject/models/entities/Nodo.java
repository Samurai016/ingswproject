package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.NotNull;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Nicolò Rebaioli
 */
@Entity
public class Nodo {
    @Id
    private UUID id;
    @NotNull
    private String nome;
    private String descrizione;
    private String nomeAttributo;
    private String valoreAttributo;
    @ManyToOne()
    private Nodo parent;
    @OneToMany(mappedBy = "parent")
    private List<Nodo> figli = new ArrayList<>();

    public Nodo() {
        // Costruttore vuoto per Ebean
    }
    protected Nodo(String nome, String descrizione, String nomeAttributo, String valoreAttributo, Nodo parent) {
        this();
        this.setNome(nome)
            .setDescrizione(descrizione)
            .setNomeAttributo(nomeAttributo)
            .setValoreAttributo(valoreAttributo)
            .setParent(parent);
    }

    public static Nodo createRoot(String nome, String descrizione, String nomeAttributo) {
        return new Nodo(nome, descrizione, nomeAttributo, null, null);
    }
    public static Nodo createFoglia(String nome, String descrizione, String valoreAttributo, Nodo parent) {
        return new Nodo(nome, descrizione, null, valoreAttributo, parent);
    }

    public boolean isFoglia() {
        return this.figli.isEmpty();
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public String[] getDominioAttributo() {
        if (this.isFoglia()) {
            return null;
        }
        return this.figli.stream().map(Nodo::getValoreAttributo).toArray(String[]::new);
    }

    // GETTERS
    public UUID getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public String getNomeAttributo() {
        return this.nomeAttributo;
    }

    public String getValoreAttributo() {
        return this.valoreAttributo;
    }

    public Nodo getParent() {
        return this.parent;
    }

    public List<Nodo> getFigli() {
        return this.figli;
    }

    // SETTERS (Builder pattern)
    public Nodo setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public Nodo setDescrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

    public Nodo setNomeAttributo(String nomeAttributo) {
        this.nomeAttributo = nomeAttributo;
        return this;
    }

    public Nodo setValoreAttributo(String valoreAttributo) {
        this.valoreAttributo = valoreAttributo;
        return this;
    }

    public Nodo setParent(Nodo parent) {
        this.parent = parent;
        return this;
    }

    public Nodo setFigli(List<Nodo> figli) {
        this.figli = figli==null ? new ArrayList<>() : figli;
        return this;
    }

    // OVERRIDE
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Nodo nodo && nodo.getId().equals(this.getId());
    }
}
