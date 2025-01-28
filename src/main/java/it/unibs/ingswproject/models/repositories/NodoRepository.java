package it.unibs.ingswproject.models.repositories;

import io.ebean.Database;
import io.ebean.Transaction;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.query.QNodo;

import java.util.List;

public class NodoRepository extends EntityRepository<Nodo> {
    public NodoRepository(Database db) {
        super(Nodo.class, db);
    }

    /**
     * Trova tutte le entità.
     *
     * @return La lista di entità trovate
     */
    public List<Nodo> findGerarchie() {
        return new QNodo()
                .parent.isNull()
                .findList();
    }

    /**
     * Controllo se esiste un nodo con lo stesso nome.
     * I nodi controllati sono quelli con lo stesso nome (case insensitive) e lo stesso parent.
     *
     * @param entity L'entità da controllare
     * @return true se esiste un nodo con lo stesso nome, false altrimenti
     */
    public boolean existsWithSameName(Nodo entity) {
        QNodo query = new QNodo()
                .and()
                    .nome.ieq(entity.getNome())
                    .id.ne(entity.getId());

        if (entity.isRoot()) {
            query = query.parent.isNull();
        } else {
            query = query.parent.eq(entity.getParent());
        }

        query = query.endAnd();

        return query.exists();
    }

    /**
     * Controllo se esiste un nodo con lo stesso attributo.
     * I nodi controllati sono quelli con lo stesso attributo (case insensitive) e lo stesso parent.
     *
     * @param entity L'entità da controllare
     * @return true se esiste un nodo con lo stesso attributo, false altrimenti
     */
    public boolean existsWithSameAttributeValue(Nodo entity) {
        if (entity.getValoreAttributo() == null) {
            return false;
        }

        QNodo query = new QNodo()
                .and()
                    .valoreAttributo.ieq(entity.getValoreAttributo())
                    .id.ne(entity.getId());

        if (entity.isRoot()) {
            query = query.parent.isNull();
        } else {
            query = query.parent.eq(entity.getParent());
        }

        query = query.endAnd();

        return query.exists();
    }

    @Override
    public void save(Nodo entity) {
        try (Transaction transaction = this.database.beginTransaction()) {
            // Generate the parent
            this.validate(entity);
            this.database.save(entity);

            // Generate all children
            List<Nodo> figli = entity.getFigli();
            for (Nodo figlio : figli) {
                figlio.setParent(entity);
                this.validate(figlio);
                this.database.save(figlio);
            }

            transaction.commit();
        }
        this.database.refresh(entity);
    }

    @Override
    protected void validate(Nodo entity) {
        super.validate(entity);

        // Controllo se esiste già un nodo con lo stesso nome
        if (this.existsWithSameName(entity)) {
            throw new IllegalArgumentException("nodo_same_name_not_allowed");
        }

        // Controllo se esiste già un nodo con lo stesso valore di attributo
        if (!entity.isRoot() && this.existsWithSameAttributeValue(entity)) {
            throw new IllegalArgumentException("nodo_same_attribute_value_not_allowed");
        }
    }
}
