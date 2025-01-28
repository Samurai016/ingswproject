package it.unibs.ingswproject.models.repositories;

import io.ebean.Database;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.query.QFattoreDiConversione;

import java.util.List;

public class FattoreDiConversioneRepository extends EntityRepository<FattoreDiConversione> {

    public FattoreDiConversioneRepository(Database db) {
        super(FattoreDiConversione.class, db);
    }

    public FattoreDiConversione findByNodi(Nodo nodo1, Nodo nodo2) {
        // SELECT * FROM fattore_di_conversione WHERE (nodo1 = nodo1 AND nodo2 = nodo2) OR (nodo1 = nodo2 AND nodo2 = nodo1)
        return new QFattoreDiConversione()
                .or()
                    .and()
                        .nodo1.eq(nodo1)
                        .nodo2.eq(nodo2)
                    .endAnd()
                    .and()
                        .nodo1.eq(nodo2)
                        .nodo2.eq(nodo1)
                    .endAnd()
                .endOr()
                .findOne();
    }

    public List<FattoreDiConversione> findByNodo(Nodo nodo) {
        // SELECT * FROM fattore_di_conversione WHERE nodo1 = nodo OR nodo2 = nodo
        return new QFattoreDiConversione()
                .or()
                    .nodo1.eq(nodo)
                    .nodo2.eq(nodo)
                .endOr()
                .findList();
    }
}
