package it.unibs.ingswproject.models;

import io.ebean.DB;
import io.ebean.Database;
import it.unibs.ingswproject.models.entities.*;
import it.unibs.ingswproject.models.repositories.ComprensorioRepository;
import it.unibs.ingswproject.models.repositories.FattoreDiConversioneRepository;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.models.repositories.ScambioRepository;

import java.util.HashMap;

/**
 * Questa classe è un servizio singleton per lo storage.
 * Gestisce la connessione al database.
 *
 * @see Database
 * @see DB
 * @author Nicolò Rebaioli
 */
public class StorageService {
    @SuppressWarnings("rawtypes")
    protected final HashMap<Class, EntityRepository> repositories = new HashMap<>();
    protected final Database database;

    /**
     * Costruttore del servizio di storage.
     * @param database Il database
     */
    public StorageService(Database database) {
        this.database = database;
        this.repositories.put(Comprensorio.class, new ComprensorioRepository(this.database));
        this.repositories.put(Nodo.class, new NodoRepository(this.database));
        this.repositories.put(FattoreDiConversione.class, new FattoreDiConversioneRepository(this.database));
        this.repositories.put(Utente.class, new EntityRepository<>(Utente.class, this.database));
        this.repositories.put(Scambio.class, new ScambioRepository(this.database));
    }

    /**
     * Get the database.
     * @return The database
     */
    public Database getDatabase() {
        return this.database;
    }

    /**
     * Get the repository for an entity class.
     * @param entityClass The entity class
     * @param <T> The entity class
     * @return The repository for the entity class
     */
    @SuppressWarnings("unchecked")
    public <T> EntityRepository<T> getRepository(Class<T> entityClass) {
        return this.repositories.get(entityClass);
    }
}
