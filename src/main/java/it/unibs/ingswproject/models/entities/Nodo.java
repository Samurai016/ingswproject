package it.unibs.ingswproject.models.entities;

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
    private String nome;
    private String descrizione;
    private String nomeAttributo;
    private String valoreAttributo;
    @ManyToOne()
    private Nodo parent;
    @OneToMany(mappedBy = "parent")
    private List<Nodo> figli = new ArrayList<>();

    protected Nodo(String nome, String descrizione, String nomeAttributo, String valoreAttributo, Nodo parent) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.nomeAttributo = nomeAttributo;
        this.valoreAttributo = valoreAttributo;
        this.parent = parent;
    }

    public static Nodo createRoot(String nome, String descrizione, String nomeAttributo) {
        return new Nodo(nome, descrizione, nomeAttributo, null, null);
    }
    public static Nodo createFoglia(String nome, String descrizione, String nomeAttributo, String valoreAttributo, Nodo parent) {
        return new Nodo(nome, descrizione, nomeAttributo, valoreAttributo, parent);
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

    // GETTER AND SETTER
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNomeAttributo() {
        return this.nomeAttributo;
    }

    public void setNomeAttributo(String nomeAttributo) {
        this.nomeAttributo = nomeAttributo;
    }

    public String getValoreAttributo() {
        return this.valoreAttributo;
    }

    public void setValoreAttributo(String valoreAttributo) {
        this.valoreAttributo = valoreAttributo;
    }

    public Nodo getParent() {
        return this.parent;
    }

    public void setParent(Nodo parent) {
        this.parent = parent;
    }

    public List<Nodo> getFigli() {
        return this.figli;
    }

    public void setFigli(ArrayList<Nodo> figli) {
        this.figli = figli;
    }
}
