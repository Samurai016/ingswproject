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

    public NodoRepository() {
        super(Nodo.class);
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

    @Override
    public void create(Nodo entity) {
        // Generate the parent
        super.create(entity);

        // Generate all children
        Nodo[] figli = entity.getFigli().toArray(Nodo[]::new);
        for (Nodo figlio : figli) {
            figlio.setParent(entity);
            super.create(figlio);
        }
    }

    @Override
    protected void validate(Nodo entity) {
        super.validate(entity);

        ExpressionList<Nodo> query = this.database
                .find(Nodo.class)
                .where()
                .eq("nome", entity.getNome())
                .and()
                .ne("id", entity.getId())
                .and();

        if (entity.isRoot()) {
            query.isNull("parent");
        } else {
            query.eq("parent", entity.getParent());
        }

        Optional<Nodo> existsRoot = query.findOneOrEmpty();
        if (existsRoot.isPresent()) {
            throw new IllegalArgumentException("Esiste già una foglia con lo stesso nome");
        }
    }
}
