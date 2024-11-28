package it.unibs.ingswproject.models.repositories;

import io.ebean.Database;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.List;

public class FattoreDiConversioneRepository extends EntityRepository<FattoreDiConversione> {

    public FattoreDiConversioneRepository(Database db) {
        super(FattoreDiConversione.class, db);
    }

    public FattoreDiConversione findByNodi(Nodo nodo1, Nodo nodo2) {
        // SELECT * FROM fattore_di_conversione WHERE (nodo1 = nodo1 AND nodo2 = nodo2) OR (nodo1 = nodo2 AND nodo2 = nodo1)
        return this.database
                .find(FattoreDiConversione.class)
                .where(
                        this.database.expressionFactory().or(
                                this.database.expressionFactory().and(
                                        this.database.expressionFactory().eq("nodo1", nodo1),
                                        this.database.expressionFactory().eq("nodo2", nodo2)
                                ),
                                this.database.expressionFactory().and(
                                        this.database.expressionFactory().eq("nodo1", nodo2),
                                        this.database.expressionFactory().eq("nodo2", nodo1)
                                )
                        )
                )
                .findOne();
    }

    public List<FattoreDiConversione> findByNodo(Nodo nodo) {
        // SELECT * FROM fattore_di_conversione WHERE nodo1 = nodo OR nodo2 = nodo
        return this.database
                .find(FattoreDiConversione.class)
                .where(
                        this.database.expressionFactory().or(
                                this.database.expressionFactory().eq("nodo1", nodo),
                                this.database.expressionFactory().eq("nodo2", nodo)
                        )
                )
                .findList();
    }


}
