package it.unibs.ingswproject.models.repositories;

import io.ebean.Database;
import io.ebean.ExpressionList;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.List;
import java.util.Optional;

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
        return this.database
                .find(this.entityClass)
                .where()
                .isNull("parent")
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
        ExpressionList<Nodo> query = this.database
                .find(Nodo.class)
                .where()
                .ieq("nome", entity.getNome())
                .and()
                .ne("id", entity.getId())
                .and();
        if (entity.isRoot()) {
            query.isNull("parent");
        } else {
            query.eq("parent", entity.getParent());
        }
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

        ExpressionList<Nodo> query = this.database
                .find(Nodo.class)
                .where()
                .ieq("valoreAttributo", entity.getValoreAttributo())
                .and()
                .ne("id", entity.getId())
                .and();
        if (entity.isRoot()) {
            query.isNull("parent");
        } else {
            query.eq("parent", entity.getParent());
        }
        return query.exists();
    }

    @Override
    public void save(Nodo entity) {
        // Generate the parent
        super.save(entity);

        // Generate all children
        Nodo[] figli = entity.getFigli().toArray(Nodo[]::new);
        for (Nodo figlio : figli) {
            figlio.setParent(entity);
            super.save(figlio);
        }
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
