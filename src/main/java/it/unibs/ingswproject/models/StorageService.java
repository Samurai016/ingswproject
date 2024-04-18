package it.unibs.ingswproject.models;

import io.ebean.DB;
import io.ebean.Database;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.models.repositories.FattoreDiConversioneRepository;
import it.unibs.ingswproject.models.repositories.NodoRepository;

import javax.xml.crypto.Data;
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
    private final HashMap<Class, EntityRepository> repositories = new HashMap<>();
    protected final Database database;

    public StorageService(Database database) {
        this.database = database;
        this.repositories.put(Comprensorio.class, new EntityRepository<>(Comprensorio.class, this.database));
        this.repositories.put(Nodo.class, new NodoRepository(this.database));
        this.repositories.put(FattoreDiConversione.class, new FattoreDiConversioneRepository(this.database));
        this.repositories.put(Utente.class, new EntityRepository<>(Utente.class, this.database));
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
