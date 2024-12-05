package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.NotNull;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author Nicolò Rebaioli
 */
@Entity
public class Nodo {
    @Id
    protected UUID id;
    @NotNull
    protected String nome;
    protected String descrizione;
    protected String nomeAttributo;
    protected String valoreAttributo;
    @ManyToOne(cascade = CascadeType.ALL)
    protected Nodo parent;
    @OneToMany(mappedBy = "parent")
    protected List<Nodo> figli = new ArrayList<>();
    @OneToMany(mappedBy = "nodo1", cascade = CascadeType.ALL)
    protected List<FattoreDiConversione> fattoriDiConversione1 = new ArrayList<>();
    @OneToMany(mappedBy = "nodo2", cascade = CascadeType.ALL)
    protected List<FattoreDiConversione> fattoriDiConversione2 = new ArrayList<>();

    public Nodo() {
        // Costruttore vuoto per Ebean
        this.id = UUID.randomUUID();
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

    public Nodo getRoot() {
        if (this.isRoot()) {
            return this;
        }
        return this.parent.getRoot();
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

    public List<FattoreDiConversione> getFattoriDiConversioneAndata() {
        return this.fattoriDiConversione1;
    }

    public List<FattoreDiConversione> getFattoriDiConversioneRitorno() {
        return this.fattoriDiConversione2;
    }

    public List<FattoreDiConversione> getFattoriDiConversione() {
        return Stream.concat(this.fattoriDiConversione1.stream(), this.fattoriDiConversione2.stream()).toList();
    }

    // SETTERS (Builder pattern)
    public Nodo setId(UUID id) {
        this.id = id;
        return this;
    }

    public Nodo setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("nodo_name_not_empty");
        }
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
        this.figli = figli == null ? new ArrayList<>() : figli;
        return this;
    }

    public Nodo setFattoDiConversioneAndata(List<FattoreDiConversione> fdc) {
        this.fattoriDiConversione1 = fdc == null ? new ArrayList<>() : fdc;
        return this;
    }

    public Nodo setFattoDiConversioneRitorno(List<FattoreDiConversione> fdc) {
        this.fattoriDiConversione2 = fdc == null ? new ArrayList<>() : fdc;
        return this;
    }

    // OVERRIDE
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Nodo nodo = (Nodo) obj;
        return this.getId().equals(nodo.getId());
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
