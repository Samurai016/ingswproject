package it.unibs.ingswproject.models.repositories;

import io.ebean.Database;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.entities.Nodo;

public class NodoRepository extends EntityRepository<Nodo> {
    public NodoRepository(Database db) {
        super(Nodo.class, db);
    }
    public NodoRepository() {
        super(Nodo.class);
    }

    @Override
    public void create(Nodo entity) {
        // If the id is not null, the entity is already in the database
        if (entity.getId()!=null) {
            return;
        }

        // Generate all children
        Nodo[] figli = entity.getFigli().toArray(Nodo[]::new);
        for (Nodo figlio : figli) {
            figlio.setParent(entity);
            super.create(figlio);
        }

        // Generate the parent
        super.create(entity);
    }
}
