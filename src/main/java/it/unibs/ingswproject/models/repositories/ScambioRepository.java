package it.unibs.ingswproject.models.repositories;

import io.ebean.Database;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.entities.Utente;

import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ScambioRepository  extends EntityRepository<Scambio> {
    public ScambioRepository(Database db) {
        super(Scambio.class, db);
    }

    /**
     * Trova gli scambi inseriti da un determinato utente
     *
     * @param autore Utente autore
     * @return Lista di scambi
     */
    public List<Scambio> findByAutore(Utente autore) {
        return this.database
                .find(this.entityClass)
                .where()
                .eq("autore", autore)
                .findList();
    }
}
